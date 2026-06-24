<!--
  部门管理页面
  功能：展示部门列表，支持新增、编辑、删除部门
  布局：顶部标题和操作按钮 -> 数据表格 -> 新增/编辑弹窗
-->
<template>
  <div>
    <!-- 页面头部：标题和新增按钮 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>部门管理</h2>
        <p>管理组织架构，维护部门基本信息</p>
      </div>
      <div class="page-header-actions">
        <el-button type="primary" @click="openDialog()">
          <el-icon><Plus /></el-icon>新增部门
        </el-button>
      </div>
    </div>

    <!-- 数据表格区域 -->
    <el-card class="content-card" shadow="hover">
      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%" empty-text="暂无部门数据">
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column prop="name" label="部门名称" min-width="200" />
        <el-table-column prop="updateTime" label="最后操作时间" width="200" />
        <!-- 操作列：编辑和删除按钮 -->
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" link @click="openDialog(row)">编辑</el-button>
              <el-button size="small" type="danger" link @click="handleDelete(row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑部门弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑部门' : '新增部门'" width="480px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称，长度为2-10位" minlength="2" maxlength="10" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 部门管理页面逻辑
 * 包含部门的增删改查功能
 */
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDepts, addDept, updateDept, deleteDept } from '../../api/dept'

// 表格加载状态
const loading = ref(false)
// 保存按钮加载状态
const saving = ref(false)
// 表格数据
const tableData = ref([])
// 弹窗显示状态
const dialogVisible = ref(false)
// 表单引用（用于表单验证）
const formRef = ref(null)
// 表单数据（id为null表示新增，有值表示编辑）
const form = ref({ id: null, name: '' })

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 10, message: '部门名称长度为2-10位', trigger: 'blur' }
  ]
}

/**
 * 加载部门列表数据
 * 调用后端接口获取所有部门，更新表格数据
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getDepts()
    tableData.value = res.data || []
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 打开新增/编辑弹窗
 * @param {Object} row - 当前行数据，传入则为编辑模式，不传则为新增模式
 */
const openDialog = (row) => {
  // 编辑模式：拷贝行数据到表单；新增模式：重置表单
  form.value = row ? { ...row } : { id: null, name: '' }
  dialogVisible.value = true
}

/**
 * 保存部门（新增或编辑）
 * 先验证表单，再根据id判断调用新增或更新接口
 */
const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (form.value.id) {
      // 编辑模式：调用更新接口
      await updateDept(form.value)
    } else {
      // 新增模式：调用新增接口
      await addDept(form.value)
    }
    dialogVisible.value = false
    ElMessage.success('操作成功')
    loadData()
  } catch (e) { /* ignore */ }
  saving.value = false
}

/**
 * 删除部门
 * 弹出确认框，确认后调用删除接口
 * @param {number} id - 部门ID
 */
const handleDelete = async (id) => {
  await ElMessageBox.confirm('您确定要删除该部门吗？', '提示', { type: 'warning' })
  await deleteDept(id)
  ElMessage.success('删除成功')
  loadData()
}

// 页面挂载时加载部门数据
onMounted(loadData)
</script>
