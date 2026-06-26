<!--
  考试管理页面
  功能：展示考试列表，支持条件搜索、批量删除、新增/编辑考试
  布局：标题 -> 搜索栏 -> 批量操作栏 -> 数据表格 -> 新增/编辑弹窗
-->
<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>考试管理</h2>
        <p>管理各类考试安排，跟踪考试状态</p>
      </div>
      <div class="page-header-actions">
        <el-button type="primary" @click="openDialog()"><el-icon><Plus /></el-icon>新增考试</el-button>
      </div>
    </div>

    <el-card class="content-card" shadow="hover">
      <!-- 搜索栏：按标题和状态筛选 -->
      <div class="search-bar">
        <span class="search-label">标题</span>
        <el-input v-model="search.title" placeholder="搜索考试标题" clearable style="width: 200px" @keyup.enter="loadData" />
        <span class="search-label">状态</span>
        <el-select v-model="search.status" placeholder="考试状态" clearable style="width: 140px">
          <el-option label="未发布" :value="0" /><el-option label="进行中" :value="1" /><el-option label="已结束" :value="2" />
        </el-select>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">清空</el-button>
      </div>

      <!-- 批量操作栏 -->
      <div class="batch-bar" v-if="selectedRows.length > 0">
        <span>已选择 <strong>{{ selectedRows.length }}</strong> 项</span>
        <el-button type="danger" size="small" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
        <el-button size="small" @click="clearSelection">取消选择</el-button>
      </div>

      <!-- 考试数据表格（使用计算属性filteredData实现前端筛选） -->
      <el-table :data="filteredData" v-loading="loading" border stripe style="width: 100%"
        @selection-change="handleSelectionChange" empty-text="暂无考试数据">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="60" sortable />
        <el-table-column prop="title" label="考试标题" min-width="160" sortable />
        <el-table-column prop="className" label="参考班级" width="140" />
        <el-table-column prop="startTime" label="开始时间" width="170" sortable />
        <el-table-column prop="endTime" label="结束时间" width="170" />
        <el-table-column prop="totalScore" label="总分" width="80" align="center" />
        <!-- 状态列：数字映射为标签 -->
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="['info', 'success', 'warning'][row.status]" size="small">
              {{ ['未发布', '进行中', '已结束'][row.status] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" link @click="openDialog(row)">编辑</el-button>
              <el-button size="small" type="danger" link @click="handleDelete(row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑考试弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑考试' : '新增考试'" width="540px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="考试标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入考试标题" />
        </el-form-item>
        <!-- 参考班级下拉选择 -->
        <el-form-item label="参考班级" prop="classId">
          <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%">
            <el-option v-for="c in classes" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="总分">
          <el-input-number v-model="form.totalScore" :min="0" :max="1000" />
        </el-form-item>
        <!-- 考试状态选择 -->
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="未发布" :value="0" /><el-option label="进行中" :value="1" /><el-option label="已结束" :value="2" />
          </el-select>
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
 * 考试管理页面逻辑
 * 包含考试的增删改查、前端筛选、批量删除
 */
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { getClasses } from '../../api/class'

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
const search = ref({ title: '', status: null })
// 表单数据
const form = ref({ id: null, title: '', classId: null, startTime: '', endTime: '', totalScore: 100, status: 0 })
// 班级列表
const classes = ref([])

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入考试标题', trigger: 'blur' }]
}

/**
 * 计算属性：根据搜索条件过滤考试数据
 * 前端筛选，不请求后端
 */
const filteredData = computed(() => {
  let list = tableData.value
  // 按标题模糊匹配
  if (search.value.title) list = list.filter(e => e.title?.includes(search.value.title))
  // 按状态精确匹配
  if (search.value.status !== null && search.value.status !== '') list = list.filter(e => e.status === search.value.status)
  return list
})

/**
 * 加载考试列表数据
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/exams')
    tableData.value = res.data || []
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 加载班级列表（用于参考班级下拉选择）
 */
const loadClasses = async () => {
  const res = await getClasses()
  classes.value = res.data || []
}

/**
 * 重置搜索条件
 */
const resetSearch = () => { search.value = { title: '', status: null } }
// 表格多选变化回调
const handleSelectionChange = (rows) => { selectedRows.value = rows }
// 取消所有选中
const clearSelection = () => { selectedRows.value = [] }

/**
 * 打开新增/编辑弹窗
 * @param {Object} row - 行数据，有值为编辑模式
 */
const openDialog = (row) => {
  form.value = row ? { ...row } : { id: null, title: '', classId: null, startTime: '', endTime: '', totalScore: 100, status: 0 }
  dialogVisible.value = true
}

/**
 * 保存考试（新增或编辑）
 */
const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (form.value.id) { await request.put('/exams', form.value) } else { await request.post('/exams', form.value) }
    dialogVisible.value = false
    ElMessage.success('操作成功')
    loadData()
  } catch (e) { /* ignore */ }
  saving.value = false
}

/**
 * 删除单个考试
 * @param {number} id - 考试ID
 */
const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除该考试吗？', '提示', { type: 'warning' })
  await request.delete(`/exams/${id}`)
  ElMessage.success('删除成功')
  loadData()
}

/**
 * 批量删除选中的考试
 */
const handleBatchDelete = async () => {
  await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 场考试吗？`, '批量删除', { type: 'warning' })
  for (const row of selectedRows.value) { await request.delete(`/exams/${row.id}`) }
  ElMessage.success('批量删除成功')
  selectedRows.value = []
  loadData()
}

// 页面挂载时加载考试列表和班级列表
onMounted(() => { loadData(); loadClasses() })
</script>
