<!--
  操作日志页面
  功能：展示系统操作日志列表，支持按操作人和请求方式搜索
  布局：标题和刷新按钮 -> 搜索栏 -> 数据表格
  说明：此页面为只读页面，不支持增删改操作
-->
<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>操作日志</h2>
        <p>查看系统操作审计记录，追踪用户行为</p>
      </div>
      <div class="page-header-actions">
        <el-button @click="loadData"><el-icon><Refresh /></el-icon>刷新</el-button>
      </div>
    </div>

    <el-card class="content-card" shadow="hover">
      <!-- 搜索栏：按操作人和请求方式筛选 -->
      <div class="search-bar">
        <el-input v-model="search.username" placeholder="搜索操作人" clearable prefix-icon="Search" @keyup.enter="loadData" />
        <el-select v-model="search.method" placeholder="请求方式" clearable>
          <el-option label="POST" value="POST" /><el-option label="PUT" value="PUT" /><el-option label="DELETE" value="DELETE" />
        </el-select>
        <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button @click="resetSearch"><el-icon><Refresh /></el-icon>重置</el-button>
      </div>

      <!-- 日志数据表格（使用计算属性filteredData实现前端筛选） -->
      <el-table :data="filteredData" v-loading="loading" border stripe style="width: 100%" empty-text="暂无日志数据">
        <el-table-column prop="id" label="ID" width="60" sortable />
        <el-table-column prop="username" label="操作人" width="100" />
        <!-- 请求方式列：不同方法显示不同颜色标签 -->
        <el-table-column prop="method" label="请求方式" width="100">
          <template #default="{ row }">
            <el-tag :type="row.method === 'DELETE' ? 'danger' : row.method === 'PUT' ? 'warning' : 'success'" size="small">
              {{ row.method }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="请求路径" min-width="180" show-overflow-tooltip />
        <el-table-column prop="params" label="请求参数" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="createTime" label="操作时间" width="170" sortable />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
/**
 * 操作日志页面逻辑
 * 只读页面，仅支持查询和前端筛选
 */
import { ref, computed, onMounted } from 'vue'
import { getLogs } from '../../api/log'

// 表格加载状态
const loading = ref(false)
// 表格原始数据
const tableData = ref([])
// 搜索条件
const search = ref({ username: '', method: '' })

/**
 * 计算属性：根据搜索条件过滤日志数据
 * 按操作人模糊匹配、按请求方式精确匹配
 */
const filteredData = computed(() => {
  let list = tableData.value
  if (search.value.username) list = list.filter(l => l.username?.includes(search.value.username))
  if (search.value.method) list = list.filter(l => l.method === search.value.method)
  return list
})

/**
 * 加载操作日志数据
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getLogs()
    tableData.value = res.data || []
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 重置搜索条件
 */
const resetSearch = () => { search.value = { username: '', method: '' } }

// 页面挂载时加载日志数据
onMounted(loadData)
</script>
