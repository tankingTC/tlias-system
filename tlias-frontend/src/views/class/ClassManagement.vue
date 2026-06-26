<!--
  班级管理页面
  功能：展示班级列表，支持条件搜索、分页、新增/编辑/删除班级
  布局：标题 -> 搜索栏 -> 数据表格 -> 分页 -> 新增/编辑弹窗
-->
<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>班级管理</h2>
        <p>管理教学班级，跟踪班级状态和授课安排</p>
      </div>
      <div class="page-header-actions">
        <el-button type="primary" @click="openDialog()"><el-icon><Plus /></el-icon>新增班级</el-button>
      </div>
    </div>

    <el-card class="content-card" shadow="hover">
      <!-- 搜索栏：按班级名称和结课时间范围筛选 -->
      <div class="search-bar">
        <span class="search-label">班级名称</span>
        <el-input v-model="search.name" placeholder="请输入班级名称" clearable style="width: 200px" @keyup.enter="loadData" />
        <span class="search-label">结课时间</span>
        <el-date-picker v-model="search.endBegin" type="date" placeholder="开始时间" value-format="YYYY-MM-DD" style="width: 150px" />
        <span style="color: #909399">至</span>
        <el-date-picker v-model="search.endEnd" type="date" placeholder="结束时间" value-format="YYYY-MM-DD" style="width: 150px" />
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">清空</el-button>
      </div>

      <!-- 班级数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%" empty-text="暂无班级数据">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="班级名称" min-width="180" />
        <el-table-column prop="classroom" label="班级教室" width="100" />
        <el-table-column prop="teacherName" label="班主任" width="100" />
        <el-table-column prop="startDate" label="开课时间" width="120" />
        <el-table-column prop="endDate" label="结课时间" width="120" />
        <!-- 状态列：将数字状态映射为标签显示 -->
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="['info', 'success', 'warning'][row.status]" size="small">
              {{ ['未开班', '在读', '已结课'][row.status] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="最后操作时间" width="180" />
        <!-- 操作列 -->
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" link @click="openDialog(row)">编辑</el-button>
              <el-button size="small" type="danger" link @click="handleDelete(row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination style="margin-top: 16px; justify-content: flex-end" background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total" :page-sizes="[10, 20, 50]" v-model:page-size="pageSize"
        v-model:current-page="page" @current-change="loadData" @size-change="loadData" />
    </el-card>

    <!-- 新增/编辑班级弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '修改班级' : '新增班级'" width="560px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入班级名称，如：西安黑马JavaEE就业100期" />
        </el-form-item>
        <el-form-item label="班级教室">
          <el-input v-model="form.classroom" placeholder="请填写班级教室" />
        </el-form-item>
        <el-form-item label="开课时间" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结课时间" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <!-- 班主任下拉选择（从员工列表中选取） -->
        <el-form-item label="班主任">
          <el-select v-model="form.teacherId" placeholder="请选择" style="width: 100%">
            <el-option v-for="e in emps" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="学科" prop="subject">
          <el-select v-model="form.subject" placeholder="请选择" style="width: 100%">
            <el-option label="Java" value="Java" />
            <el-option label="前端" value="前端" />
            <el-option label="大数据" value="大数据" />
            <el-option label="Python" value="Python" />
            <el-option label="Go" value="Go" />
            <el-option label="嵌入式" value="嵌入式" />
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
 * 班级管理页面逻辑
 * 包含班级的增删改查和分页查询功能
 */
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { getEmps } from '../../api/emp'

// 表格加载状态
const loading = ref(false)
// 保存按钮加载状态
const saving = ref(false)
// 表格数据
const tableData = ref([])
// 总记录数
const total = ref(0)
// 当前页码
const page = ref(1)
// 每页条数
const pageSize = ref(10)
// 弹窗显示状态
const dialogVisible = ref(false)
// 表单引用
const formRef = ref(null)
// 表单数据
const form = ref(getDefaultForm())
// 员工列表（用于班主任下拉选择）
const emps = ref([])
// 搜索条件
const search = ref({ name: '', endBegin: '', endEnd: '' })

/**
 * 获取默认表单数据
 * @returns {Object} 班级表单初始值
 */
function getDefaultForm() {
  return { id: null, name: '', classroom: '', teacherId: null, startDate: '', endDate: '', status: 0, subject: '' }
}

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }]
}

/**
 * 加载班级列表数据（分页+条件筛选）
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/classes', { params: { page: page.value, pageSize: pageSize.value, ...search.value } })
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 加载员工列表（用于班主任下拉选择）
 */
const loadEmps = async () => {
  const res = await getEmps({ page: 1, pageSize: 200 })
  emps.value = res.data?.rows || []
}

/**
 * 重置搜索条件并重新加载数据
 */
const resetSearch = () => { search.value = { name: '', endBegin: '', endEnd: '' }; loadData() }

/**
 * 打开新增/编辑弹窗
 * @param {Object} row - 行数据，有值为编辑模式
 */
const openDialog = (row) => {
  form.value = row ? { ...row } : getDefaultForm()
  dialogVisible.value = true
}

/**
 * 保存班级（新增或编辑）
 * 先验证表单，再调用对应接口
 */
const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (form.value.id) {
      // 编辑模式
      await request.put('/classes', form.value)
    } else {
      // 新增模式
      await request.post('/classes', form.value)
    }
    dialogVisible.value = false
    ElMessage.success('操作成功')
    loadData()
  } catch (e) { /* ignore */ }
  saving.value = false
}

/**
 * 删除班级
 * 弹出确认框，确认后调用删除接口
 * @param {number} id - 班级ID
 */
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('您确定要删除该班级吗？', '提示', { type: 'warning' })
    await request.delete(`/classes/${id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    // 用户取消操作不处理，其他错误显示提示
    if (e !== 'cancel' && e?.message) {
      ElMessage.error(e.message)
    }
  }
}

// 页面挂载时加载班级列表和员工列表
onMounted(() => { loadData(); loadEmps() })
</script>
