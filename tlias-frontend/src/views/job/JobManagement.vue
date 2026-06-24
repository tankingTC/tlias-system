<!--
  就业管理页面
  功能：展示就业记录列表，支持条件搜索、批量删除、新增/编辑就业记录
  布局：标题 -> 搜索栏 -> 批量操作栏 -> 数据表格 -> 新增/编辑弹窗
-->
<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>就业管理</h2>
        <p>跟踪学员就业情况，记录就业信息</p>
      </div>
      <div class="page-header-actions">
        <el-button type="primary" @click="openDialog()"><el-icon><Plus /></el-icon>新增记录</el-button>
      </div>
    </div>

    <el-card class="content-card" shadow="hover">
      <!-- 搜索栏：按公司名称和城市筛选 -->
      <div class="search-bar">
        <el-input v-model="search.company" placeholder="搜索公司名称" clearable prefix-icon="Search" @keyup.enter="loadData" />
        <el-input v-model="search.city" placeholder="搜索城市" clearable @keyup.enter="loadData" />
        <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button @click="resetSearch"><el-icon><Refresh /></el-icon>重置</el-button>
      </div>

      <!-- 批量操作栏 -->
      <div class="batch-bar" v-if="selectedRows.length > 0">
        <span>已选择 <strong>{{ selectedRows.length }}</strong> 项</span>
        <el-button type="danger" size="small" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
        <el-button size="small" @click="clearSelection">取消选择</el-button>
      </div>

      <!-- 就业数据表格 -->
      <el-table :data="filteredData" v-loading="loading" border stripe style="width: 100%"
        @selection-change="handleSelectionChange" empty-text="暂无就业记录">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="60" sortable />
        <el-table-column prop="studentName" label="学员姓名" width="100" />
        <el-table-column prop="company" label="就业公司" min-width="160" show-overflow-tooltip />
        <el-table-column prop="position" label="岗位" width="130" />
        <!-- 薪资列：格式化显示带千分符 -->
        <el-table-column prop="salary" label="薪资" width="110" sortable>
          <template #default="{ row }">
            <span style="color: #f56c6c; font-weight: 600;">¥{{ formatSalary(row.salary) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="city" label="城市" width="100" />
        <el-table-column prop="jobDate" label="就业日期" width="120" sortable />
        <!-- 操作列 -->
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" link @click="openDialog(row)"><el-icon><Edit /></el-icon>编辑</el-button>
              <el-button size="small" type="danger" link @click="handleDelete(row.id)"><el-icon><Delete /></el-icon>删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑就业记录弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑记录' : '新增记录'" width="520px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="学员" prop="studentId">
          <el-input-number v-model="form.studentId" :min="1" style="width: 100%" placeholder="请输入学员ID" />
        </el-form-item>
        <el-form-item label="就业公司" prop="company">
          <el-input v-model="form.company" placeholder="请输入公司名称" />
        </el-form-item>
        <el-form-item label="岗位" prop="position">
          <el-input v-model="form.position" placeholder="请输入岗位" />
        </el-form-item>
        <el-form-item label="薪资">
          <el-input-number v-model="form.salary" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="form.city" placeholder="请输入城市" />
        </el-form-item>
        <el-form-item label="就业日期">
          <el-date-picker v-model="form.jobDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 就业管理页面逻辑
 * 包含就业记录的增删改查、前端筛选、批量删除
 */
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

// 表格加载状态
const loading = ref(false)
// 保存按钮加载状态
const saving = ref(false)
// 表格原始数据
const tableData = ref([])
// 多选选中的行
const selectedRows = ref([])
// 弹窗显示状态
const dialogVisible = ref(false)
// 表单引用
const formRef = ref(null)
// 搜索条件
const search = ref({ company: '', city: '' })
// 表单数据
const form = ref({ id: null, studentId: null, company: '', position: '', salary: null, city: '', jobDate: '' })

// 表单验证规则
const rules = {
  studentId: [{ required: true, message: '请输入学员ID', trigger: 'blur' }],
  company: [{ required: true, message: '请输入公司名称', trigger: 'blur' }]
}

/**
 * 计算属性：根据搜索条件过滤就业数据
 * 前端模糊匹配公司名称和城市
 */
const filteredData = computed(() => {
  let list = tableData.value
  if (search.value.company) list = list.filter(j => j.company?.includes(search.value.company))
  if (search.value.city) list = list.filter(j => j.city?.includes(search.value.city))
  return list
})

/**
 * 格式化薪资显示（添加千分符和两位小数）
 * @param {number} val - 原始薪资值
 * @returns {string} 格式化后的薪资字符串
 */
const formatSalary = (val) => {
  if (!val) return '0.00'
  return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

/**
 * 加载就业记录列表数据
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/jobs')
    tableData.value = res.data || []
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 重置搜索条件
 */
const resetSearch = () => { search.value = { company: '', city: '' } }
// 表格多选变化回调
const handleSelectionChange = (rows) => { selectedRows.value = rows }
// 取消所有选中
const clearSelection = () => { selectedRows.value = [] }

/**
 * 打开新增/编辑弹窗
 * @param {Object} row - 行数据，有值为编辑模式
 */
const openDialog = (row) => {
  form.value = row ? { ...row } : { id: null, studentId: null, company: '', position: '', salary: null, city: '', jobDate: '' }
  dialogVisible.value = true
}

/**
 * 保存就业记录（新增或编辑）
 */
const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (form.value.id) { await request.put('/jobs', form.value) } else { await request.post('/jobs', form.value) }
    dialogVisible.value = false
    ElMessage.success('操作成功')
    loadData()
  } catch (e) { /* ignore */ }
  saving.value = false
}

/**
 * 删除单条就业记录
 * @param {number} id - 记录ID
 */
const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除该记录吗？', '提示', { type: 'warning' })
  await request.delete(`/jobs/${id}`)
  ElMessage.success('删除成功')
  loadData()
}

/**
 * 批量删除选中的就业记录
 */
const handleBatchDelete = async () => {
  await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 条记录吗？`, '批量删除', { type: 'warning' })
  for (const row of selectedRows.value) { await request.delete(`/jobs/${row.id}`) }
  ElMessage.success('批量删除成功')
  selectedRows.value = []
  loadData()
}

// 页面挂载时加载就业记录数据
onMounted(loadData)
</script>
