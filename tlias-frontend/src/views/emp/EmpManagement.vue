<!--
  员工管理页面
  功能：展示员工列表，支持条件搜索、分页、批量删除、新增/编辑员工
  布局：标题 -> 搜索栏 -> 批量操作栏 -> 数据表格 -> 分页 -> 新增/编辑弹窗
-->
<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>员工管理</h2>
        <p>管理教职工信息，包括基本资料和部门归属</p>
      </div>
      <div class="page-header-actions">
        <el-button type="primary" @click="openDialog()"><el-icon><Plus /></el-icon>新增员工</el-button>
      </div>
    </div>

    <el-card class="content-card" shadow="hover">
      <!-- 搜索栏：姓名、性别、入职日期范围筛选 -->
      <div class="search-bar">
        <span class="search-label">姓名</span>
        <el-input v-model="search.name" placeholder="请输入员工姓名" clearable style="width: 180px" @keyup.enter="loadData" />
        <span class="search-label">性别</span>
        <el-select v-model="search.gender" placeholder="请选择" clearable style="width: 120px">
          <el-option label="男" :value="1" /><el-option label="女" :value="0" />
        </el-select>
        <span class="search-label">入职时间</span>
        <el-date-picker v-model="search.begin" type="date" placeholder="开始日期" value-format="YYYY-MM-DD" style="width: 150px" />
        <span style="color: #909399">到</span>
        <el-date-picker v-model="search.end" type="date" placeholder="结束日期" value-format="YYYY-MM-DD" style="width: 150px" />
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">清空</el-button>
      </div>

      <!-- 批量操作栏：选中行时显示 -->
      <div class="batch-bar" v-if="selectedRows.length > 0">
        <span>已选择 <strong>{{ selectedRows.length }}</strong> 项</span>
        <el-button type="danger" size="small" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
        <el-button size="small" @click="clearSelection">取消选择</el-button>
      </div>

      <!-- 员工数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%"
        @selection-change="handleSelectionChange" empty-text="暂无员工数据">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="姓名" width="100" />
        <!-- 性别列：将数字映射为文字显示 -->
        <el-table-column prop="gender" label="性别" width="70">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <!-- 头像列：有图片显示图片，无图片显示姓名首字 -->
        <el-table-column prop="image" label="头像" width="80" align="center">
          <template #default="{ row }">
            <el-avatar :size="36" :src="row.image" v-if="row.image" />
            <el-avatar :size="36" v-else>{{ row.name?.charAt(0) }}</el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="deptName" label="所属部门" width="120" />
        <el-table-column prop="job" label="职位" width="120" />
        <el-table-column prop="entryDate" label="入职日期" width="120" />
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '修改员工' : '新增员工'" width="700px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-row :gutter="16">
          <!-- 用户名字段：仅新增时显示 -->
          <el-col :span="12" v-if="!form.id">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入员工用户名，2-20个字" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入员工姓名，2-10个字" />
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
              <el-input v-model="form.phone" placeholder="请输入员工手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="job">
              <el-select v-model="form.job" placeholder="请选择" style="width: 100%">
                <el-option label="班主任" value="班主任" />
                <el-option label="讲师" value="讲师" />
                <el-option label="学工主管" value="学工主管" />
                <el-option label="教研主管" value="教研主管" />
                <el-option label="咨询师" value="咨询师" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资">
              <el-input-number v-model="form.salary" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <el-select v-model="form.deptId" placeholder="请选择" style="width: 100%">
                <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="form.entryDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 头像上传区域 -->
        <el-form-item label="头像">
          <div style="display: flex; align-items: center; gap: 16px">
            <el-upload action="" :auto-upload="false" :before-upload="beforeAvatarUpload" :on-change="handleFileChange" :show-file-list="false" accept="image/png,image/jpeg,image/jpg">
              <el-button size="small"><el-icon><Upload /></el-icon>选择图片</el-button>
            </el-upload>
            <el-avatar :size="80" :src="form.image" v-if="form.image" />
          </div>
          <div style="font-size: 12px; color: #909399; margin-top: 4px">
            图片大小不超过2M，仅能上传PNG，JPEG，JPG等图片，建议上传200*200或300*300尺寸的照片
          </div>
        </el-form-item>

        <!-- 工作经历动态列表：支持添加和删除多条经历 -->
        <el-form-item label="工作经历">
          <div style="width: 100%">
            <div v-for="(we, index) in form.workExperience" :key="index" style="display: flex; gap: 8px; margin-bottom: 8px; align-items: center">
              <el-date-picker v-model="we.startDate" type="date" placeholder="开始日期" value-format="YYYY-MM-DD" style="width: 140px" />
              <span>到</span>
              <el-date-picker v-model="we.endDate" type="date" placeholder="结束日期" value-format="YYYY-MM-DD" style="width: 140px" />
              <el-input v-model="we.company" placeholder="请输入公司名字" style="width: 160px" />
              <el-input v-model="we.position" placeholder="请输入职位" style="width: 140px" />
              <el-button type="danger" link @click="removeWorkExp(index)"><el-icon><Delete /></el-icon></el-button>
            </div>
            <el-button type="primary" link @click="addWorkExp"><el-icon><Plus /></el-icon>添加工作经历</el-button>
          </div>
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
 * 员工管理页面逻辑
 * 包含员工的增删改查、分页查询、批量删除、头像上传、工作经历管理
 */
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEmps, addEmp, updateEmp, deleteEmp, uploadFile } from '../../api/emp'
import { getDepts } from '../../api/dept'

// 表格加载状态
const loading = ref(false)
// 保存按钮加载状态
const saving = ref(false)
// 表格数据
const tableData = ref([])
// 总记录数（分页用）
const total = ref(0)
// 当前页码
const page = ref(1)
// 每页显示条数
const pageSize = ref(10)
// 多选选中的行
const selectedRows = ref([])
// 搜索条件
const search = ref({ name: '', gender: null, begin: '', end: '' })
// 弹窗显示状态
const dialogVisible = ref(false)
// 表单引用
const formRef = ref(null)
// 表单数据
const form = ref(getDefaultForm())
// 部门列表（用于下拉选择）
const depts = ref([])

/**
 * 获取默认表单数据
 * @returns {Object} 包含所有员工字段的初始值对象
 */
function getDefaultForm() {
  return {
    id: null, username: '', password: '', name: '', gender: 1,
    phone: '', job: '', salary: null, deptId: null, image: '', entryDate: '',
    workExperience: []
  }
}

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度为2-20个字', trigger: 'blur' },
    { pattern: /^[a-zA-Z]+$/, message: '用户名只能包含字母', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度为2-10个字', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  deptId: [{ required: true, message: '请选择所属部门', trigger: 'change' }]
}

/**
 * 加载员工列表数据（分页+条件筛选）
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getEmps({ page: page.value, pageSize: pageSize.value, ...search.value })
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 加载部门列表（用于表单中的部门下拉选择）
 */
const loadDepts = async () => {
  const res = await getDepts()
  depts.value = res.data || []
}

/**
 * 重置搜索条件并重新加载数据
 */
const resetSearch = () => {
  search.value = { name: '', gender: null, begin: '', end: '' }
  loadData()
}

// 表格多选变化回调
const handleSelectionChange = (rows) => { selectedRows.value = rows }
// 取消所有选中
const clearSelection = () => { selectedRows.value = [] }

/**
 * 打开新增/编辑弹窗
 * @param {Object} row - 行数据，有值为编辑模式
 */
const openDialog = (row) => {
  if (row) {
    form.value = { ...row, workExperience: [] }
  } else {
    form.value = getDefaultForm()
  }
  dialogVisible.value = true
}

/**
 * 头像上传前校验
 * 检查文件类型和大小是否符合要求
 * @param {File} file - 待上传的文件对象
 * @returns {boolean} 是否通过校验
 */
const beforeAvatarUpload = (file) => {
  const isImage = ['image/png', 'image/jpeg', 'image/jpg'].includes(file.type)
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) { ElMessage.error('只能上传PNG，JPEG，JPG图片'); return false }
  if (!isLt2M) { ElMessage.error('图片大小不能超过2M'); return false }
  return true
}

/**
 * 头像文件选择回调
 * 上传文件并更新表单中的头像URL
 * @param {Object} file - el-upload的文件对象
 */
const handleFileChange = async (file) => {
  const res = await uploadFile(file.raw)
  form.value.image = res.data
  ElMessage.success('头像上传成功')
}

/**
 * 添加一条工作经历
 */
const addWorkExp = () => {
  form.value.workExperience.push({ startDate: '', endDate: '', company: '', position: '' })
}

/**
 * 删除指定索引的工作经历
 * @param {number} index - 要删除的索引
 */
const removeWorkExp = (index) => {
  form.value.workExperience.splice(index, 1)
}

/**
 * 保存员工（新增或编辑）
 * 先验证表单，再调用对应接口
 */
const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (form.value.id) {
      // 编辑模式
      await updateEmp(form.value)
    } else {
      // 新增模式
      await addEmp(form.value)
    }
    dialogVisible.value = false
    ElMessage.success('操作成功')
    loadData()
  } catch (e) { /* ignore */ }
  saving.value = false
}

/**
 * 删除单个员工
 * @param {number} id - 员工ID
 */
const handleDelete = async (id) => {
  await ElMessageBox.confirm('您确定要删除该员工信息吗？', '提示', { type: 'warning' })
  await deleteEmp(id)
  ElMessage.success('删除成功')
  loadData()
}

/**
 * 批量删除选中的员工
 * 逐个调用删除接口，全部完成后刷新列表
 */
const handleBatchDelete = async () => {
  await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 名员工吗？`, '批量删除', { type: 'warning' })
  for (const row of selectedRows.value) { await deleteEmp(row.id) }
  ElMessage.success('批量删除成功')
  selectedRows.value = []
  loadData()
}

// 页面挂载时加载员工列表和部门列表
onMounted(() => { loadData(); loadDepts() })
</script>
