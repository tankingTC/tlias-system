<!--
  学员管理页面
  功能：展示学员列表，支持条件搜索、分页、批量删除、Excel导入导出、违纪处理
  布局：标题和操作按钮 -> 搜索栏 -> 批量操作栏 -> 数据表格 -> 分页 -> 编辑弹窗 -> 违纪弹窗
-->
<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>学员管理</h2>
        <p>管理学员信息，支持Excel批量导入导出</p>
      </div>
      <div class="page-header-actions">
        <!-- 导出按钮 -->
        <el-button type="success" @click="handleExport"><el-icon><Download /></el-icon>导出</el-button>
        <!-- 导入按钮（隐藏的文件上传组件） -->
        <el-upload action="" :auto-upload="false" :on-change="handleImport" :show-file-list="false" accept=".xlsx,.xls">
          <el-button type="warning"><el-icon><Upload /></el-icon>导入</el-button>
        </el-upload>
        <el-button type="primary" @click="openDialog()"><el-icon><Plus /></el-icon>添加学员</el-button>
      </div>
    </div>

    <el-card class="content-card" shadow="hover">
      <!-- 搜索栏：按班级名称、学历、所属班级筛选 -->
      <div class="search-bar">
        <el-input v-model="search.name" placeholder="请输入班级名称" clearable style="width: 180px" @keyup.enter="loadData" />
        <el-select v-model="search.education" placeholder="最高学历" clearable style="width: 140px">
          <el-option label="初中" value="初中" /><el-option label="高中" value="高中" />
          <el-option label="大专" value="大专" /><el-option label="本科" value="本科" />
          <el-option label="硕士" value="硕士" /><el-option label="博士" value="博士" />
        </el-select>
        <el-select v-model="search.classId" placeholder="所属班级" clearable style="width: 180px">
          <el-option v-for="c in classes" :key="c.id" :label="c.name" :value="c.id" />
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

      <!-- 学员数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%"
        @selection-change="handleSelectionChange" empty-text="暂无学员数据">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="姓名" width="90" />
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="className" label="班级" width="180" />
        <!-- 性别列：数字映射为文字 -->
        <el-table-column prop="gender" label="性别" width="60">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="education" label="最高学历" width="80" />
        <el-table-column prop="violationCount" label="违纪次数" width="90" align="center" />
        <el-table-column prop="violationScore" label="违纪扣分" width="90" align="center" />
        <el-table-column prop="updateTime" label="最后操作时间" width="180" />
        <!-- 操作列：编辑、违纪处理、删除 -->
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" link @click="openDialog(row)">编辑</el-button>
              <el-button size="small" type="warning" link @click="openViolationDialog(row)">违纪</el-button>
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

    <!-- 添加/编辑学员弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学员' : '添加学员'" width="680px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学号" prop="studentNo">
              <el-input v-model="form.studentNo" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择" style="width: 100%">
                <el-option label="男" :value="1" /><el-option label="女" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否院校学员">
              <el-select v-model="form.isCollege" placeholder="请选择" style="width: 100%">
                <el-option label="是" :value="1" /><el-option label="否" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系地址">
              <el-input v-model="form.address" placeholder="请输入学员的联系地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最高学历">
              <el-select v-model="form.education" placeholder="请选择" style="width: 100%">
                <el-option label="初中" value="初中" /><el-option label="高中" value="高中" />
                <el-option label="大专" value="大专" /><el-option label="本科" value="本科" />
                <el-option label="硕士" value="硕士" /><el-option label="博士" value="博士" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业时间">
              <el-date-picker v-model="form.enrollDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属班级" prop="classId">
              <el-select v-model="form.classId" placeholder="请选择" style="width: 100%">
                <el-option v-for="c in classes" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 违纪处理弹窗 -->
    <el-dialog v-model="violationVisible" title="学员违纪处理" width="400px" destroy-on-close>
      <el-form label-width="80px">
        <el-form-item label="违纪扣分">
          <el-input-number v-model="violationScore" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="violationVisible = false">取消</el-button>
        <el-button type="primary" @click="handleViolation">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 学员管理页面逻辑
 * 包含学员的增删改查、分页查询、批量删除、Excel导入导出、违纪扣分处理
 */
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getStudents, addStudent, updateStudent, deleteStudent, importStudents, exportStudents, addViolation } from '../../api/student'
import { getClasses } from '../../api/class'

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
// 多选选中的行
const selectedRows = ref([])
// 搜索条件
const search = ref({ name: '', classId: null, education: '' })
// 编辑弹窗显示状态
const dialogVisible = ref(false)
// 违纪弹窗显示状态
const violationVisible = ref(false)
// 违纪扣分分数
const violationScore = ref(1)
// 当前进行违纪操作的学员
const currentViolationStudent = ref(null)
// 表单引用
const formRef = ref(null)
// 表单数据
const form = ref(getDefaultForm())
// 班级列表（用于下拉选择）
const classes = ref([])

/**
 * 获取默认表单数据
 * @returns {Object} 学员表单初始值
 */
function getDefaultForm() {
  return {
    id: null, studentNo: '', name: '', gender: 1, phone: '', idCard: '',
    isCollege: 0, address: '', classId: null, education: '', school: '', major: '',
    enrollDate: '', studyStatus: 0, violationCount: 0, violationScore: 0, jobStatus: 0
  }
}

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  classId: [{ required: true, message: '请选择所属班级', trigger: 'change' }]
}

/**
 * 加载学员列表数据（分页+条件筛选）
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getStudents({ page: page.value, pageSize: pageSize.value, ...search.value })
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 加载班级列表（用于搜索和表单中的班级下拉选择）
 */
const loadClasses = async () => {
  const res = await getClasses()
  classes.value = res.data?.rows || res.data || []
}

/**
 * 重置搜索条件并重新加载数据
 */
const resetSearch = () => { search.value = { name: '', classId: null, education: '' }; loadData() }
// 表格多选变化回调
const handleSelectionChange = (rows) => { selectedRows.value = rows }
// 取消所有选中
const clearSelection = () => { selectedRows.value = [] }

/**
 * 打开新增/编辑弹窗
 * @param {Object} row - 行数据，有值为编辑模式
 */
const openDialog = (row) => {
  form.value = row ? { ...row } : getDefaultForm()
  dialogVisible.value = true
}

/**
 * 保存学员（新增或编辑）
 */
const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (form.value.id) { await updateStudent(form.value) } else { await addStudent(form.value) }
    dialogVisible.value = false
    ElMessage.success('操作成功')
    loadData()
  } catch (e) { /* ignore */ }
  saving.value = false
}

/**
 * 删除单个学员
 * @param {number} id - 学员ID
 */
const handleDelete = async (id) => {
  await ElMessageBox.confirm('您确定要删除该学员的信息吗？', '提示', { type: 'warning' })
  await deleteStudent(id)
  ElMessage.success('删除成功')
  loadData()
}

/**
 * 批量删除选中的学员
 */
const handleBatchDelete = async () => {
  await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 名学员吗？`, '批量删除', { type: 'warning' })
  for (const row of selectedRows.value) { await deleteStudent(row.id) }
  ElMessage.success('批量删除成功')
  selectedRows.value = []
  loadData()
}

/**
 * 打开违纪处理弹窗
 * @param {Object} row - 要处理的学员行数据
 */
const openViolationDialog = (row) => {
  currentViolationStudent.value = row
  violationScore.value = 1 // 重置扣分默认值
  violationVisible.value = true
}

/**
 * 提交违纪扣分处理
 * 调用后端接口对学员进行扣分
 */
const handleViolation = async () => {
  if (!currentViolationStudent.value) return
  await addViolation(currentViolationStudent.value.id, violationScore.value)
  ElMessage.success('违纪处理成功')
  violationVisible.value = false
  loadData()
}

/**
 * 处理Excel文件导入
 * @param {Object} file - el-upload的文件对象
 */
const handleImport = async (file) => {
  try {
    await importStudents(file.raw)
    ElMessage.success('导入成功')
    loadData()
  } catch (e) { /* ignore */ }
}

/**
 * 导出学员数据为Excel文件
 * 下载文件到本地
 */
const handleExport = async () => {
  const res = await exportStudents()
  // 创建临时下载链接
  const url = window.URL.createObjectURL(res.data)
  const a = document.createElement('a')
  a.href = url
  a.download = `学员数据_${new Date().toISOString().slice(0, 10)}.xlsx`
  a.click()
  // 释放临时URL资源
  window.URL.revokeObjectURL(url)
}

// 页面挂载时加载学员列表和班级列表
onMounted(() => { loadData(); loadClasses() })
</script>
