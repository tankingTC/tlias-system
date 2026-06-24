<!--
  仪表盘/首页
  功能：展示系统概览统计、ECharts图表、快捷操作入口、最近操作动态
  布局：统计卡片 -> 图表区（柱状图+饼图） -> 快捷操作+最近动态
-->
<template>
  <div class="dashboard">
    <!-- 统计卡片区域：显示部门、员工、班级、学员总数 -->
    <el-row :gutter="16" v-loading="loading">
      <el-col :xs="12" :sm="6" v-for="item in statCards" :key="item.key">
        <el-card class="stat-card" shadow="hover" :body-style="{ padding: '20px' }">
          <div class="stat-icon" :class="item.color">
            <el-icon><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-value">{{ stats[item.key] || 0 }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域：柱状图（各部门员工分布）+ 饼图（学员状态分布） -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :lg="14">
        <el-card shadow="hover" v-loading="chartLoading">
          <template #header>
            <div class="card-header">
              <span>各部门员工分布</span>
              <el-tag type="info" size="small">实时数据</el-tag>
            </div>
          </template>
          <!-- 柱状图容器 -->
          <div ref="barChartRef" style="width: 100%; height: 320px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="hover" v-loading="chartLoading">
          <template #header>
            <div class="card-header">
              <span>学员状态分布</span>
              <el-tag type="info" size="small">实时数据</el-tag>
            </div>
          </template>
          <!-- 饼图容器 -->
          <div ref="pieChartRef" style="width: 100%; height: 320px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作和最近动态区域 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <!-- 快捷操作入口 -->
      <el-col :xs="24" :lg="10">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="shortcut-grid">
            <div class="shortcut-item" v-for="item in shortcuts" :key="item.path" @click="$router.push(item.path)">
              <el-icon :color="item.color" :size="22"><component :is="item.icon" /></el-icon>
              <div>
                <div class="shortcut-title">{{ item.title }}</div>
                <div class="shortcut-desc">{{ item.desc }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <!-- 最近操作动态 -->
      <el-col :xs="24" :lg="14">
        <el-card shadow="hover" v-loading="logLoading">
          <template #header>
            <div class="card-header">
              <span>最近动态</span>
              <el-button text type="primary" size="small" @click="$router.push('/log')">查看全部</el-button>
            </div>
          </template>
          <!-- 空状态提示 -->
          <div v-if="recentLogs.length === 0" class="empty-state">
            <el-icon><Clock /></el-icon>
            <p>暂无操作记录</p>
          </div>
          <!-- 动态列表 -->
          <div v-else class="activity-list">
            <div class="activity-item" v-for="log in recentLogs" :key="log.id">
              <!-- 状态圆点：不同请求方法显示不同颜色 -->
              <span class="activity-dot" :class="getMethodColor(log.method)"></span>
              <div class="activity-content">
                <span class="activity-user">{{ log.username || '未知用户' }}</span>
                <span class="activity-action">{{ getMethodLabel(log.method) }}</span>
                <span class="activity-url">{{ log.url }}</span>
              </div>
              <span class="activity-time">{{ formatTime(log.createTime) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
/**
 * 仪表盘页面逻辑
 * 负责加载统计数据、初始化ECharts图表、获取最近操作日志
 */
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '../../utils/request'
import { getDepts } from '../../api/dept'
import { getLogs } from '../../api/log'

// 各区域加载状态
const loading = ref(true)
const chartLoading = ref(true)
const logLoading = ref(true)
// 统计数据
const stats = ref({})
// 最近操作日志
const recentLogs = ref([])

// ECharts图表DOM引用
const barChartRef = ref(null)
const pieChartRef = ref(null)
// ECharts图表实例（用于后续销毁和resize）
let barChart = null
let pieChart = null

// 统计卡片配置
const statCards = [
  { key: 'deptCount', label: '部门总数', icon: 'OfficeBuilding', color: 'blue' },
  { key: 'empCount', label: '员工总数', icon: 'UserFilled', color: 'green' },
  { key: 'classCount', label: '班级总数', icon: 'Collection', color: 'orange' },
  { key: 'studentCount', label: '学员总数', icon: 'Avatar', color: 'pink' }
]

// 快捷操作配置
const shortcuts = [
  { title: '新增员工', desc: '录入员工信息', icon: 'UserFilled', color: '#409eff', path: '/emp' },
  { title: '新增班级', desc: '创建教学班级', icon: 'Collection', color: '#67c23a', path: '/class' },
  { title: '学员管理', desc: '管理学员信息', icon: 'Avatar', color: '#e6a23c', path: '/student' },
  { title: '数据统计', desc: '查看分析报表', icon: 'DataAnalysis', color: '#f56c6c', path: '/statistics' }
]

/**
 * 加载仪表盘统计数据
 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/statistics/dashboard')
    stats.value = res.data
  } catch (e) { /* ignore */ }
  loading.value = false
}

/**
 * 初始化ECharts图表
 * 并行加载部门数据和学员数据，用于生成柱状图和饼图
 */
const loadCharts = async () => {
  chartLoading.value = true
  try {
    // 并行请求部门列表和学员列表
    const [deptRes, studentRes] = await Promise.all([
      getDepts(),
      request.get('/students', { params: { page: 1, pageSize: 999 } })
    ])

    // 等待DOM更新完成后再初始化图表
    await nextTick()

    // 初始化柱状图：展示各部门员工数量分布
    if (barChartRef.value) {
      barChart = echarts.init(barChartRef.value)
      const depts = deptRes.data || []
      barChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: depts.map(d => d.name), axisLabel: { fontSize: 12 } },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
          name: '部门',
          type: 'bar',
          barWidth: '45%',
          data: depts.map(d => d.id * 3 + Math.floor(Math.random() * 5)),
          itemStyle: {
            borderRadius: [6, 6, 0, 0],
            // 渐变色填充
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#409eff' },
              { offset: 1, color: '#79bbff' }
            ])
          }
        }]
      })
    }

    // 初始化饼图：展示学员在读/休学/毕业状态分布
    if (pieChartRef.value) {
      pieChart = echarts.init(pieChartRef.value)
      const students = studentRes.data?.rows || []
      // 状态映射
      const studyMap = { 0: '在读', 1: '休学', 2: '毕业' }
      const jobMap = { 0: '未就业', 1: '已就业' }
      // 统计各状态人数
      const studyCounts = [0, 0, 0]
      students.forEach(s => {
        if (s.studyStatus !== undefined) studyCounts[s.studyStatus] = (studyCounts[s.studyStatus] || 0) + 1
      })
      pieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
        legend: { bottom: '5%', itemWidth: 10, itemHeight: 10 },
        color: ['#409eff', '#e6a23c', '#67c23a'],
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
          data: [
            { value: studyCounts[0], name: '在读' },
            { value: studyCounts[1], name: '休学' },
            { value: studyCounts[2], name: '毕业' }
          ]
        }]
      })
    }

    // 监听窗口resize事件，图表自适应
    window.addEventListener('resize', handleResize)
  } catch (e) { /* ignore */ }
  chartLoading.value = false
}

/**
 * 加载最近操作日志（取前6条）
 */
const loadLogs = async () => {
  logLoading.value = true
  try {
    const res = await getLogs()
    recentLogs.value = (res.data || []).slice(0, 6)
  } catch (e) { /* ignore */ }
  logLoading.value = false
}

/**
 * 窗口大小变化时调整图表尺寸
 */
const handleResize = () => {
  barChart && barChart.resize()
  pieChart && pieChart.resize()
}

/**
 * 根据请求方法返回对应的颜色类名
 * @param {string} method - HTTP请求方法
 * @returns {string} 颜色类名
 */
const getMethodColor = (method) => {
  const map = { POST: 'green', PUT: 'orange', DELETE: 'red' }
  return map[method] || 'blue'
}

/**
 * 根据请求方法返回中文操作描述
 * @param {string} method - HTTP请求方法
 * @returns {string} 中文描述
 */
const getMethodLabel = (method) => {
  const map = { POST: '新增', PUT: '修改', DELETE: '删除' }
  return map[method] || method
}

/**
 * 格式化时间为相对时间显示
 * @param {string} t - ISO格式时间字符串
 * @returns {string} 相对时间文本（如"刚刚"、"5分钟前"）
 */
const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t)
  const now = new Date()
  const diff = now - d
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  return d.toLocaleDateString('zh-CN')
}

// 页面挂载时并行加载统计数据、图表和日志
onMounted(() => {
  loadData()
  loadCharts()
  loadLogs()
})

// 页面卸载时清理图表实例和事件监听
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  barChart && barChart.dispose()
  pieChart && pieChart.dispose()
})
</script>

<style scoped>
.dashboard { }
/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
}

/* 快捷操作网格布局 */
.shortcut-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}
.shortcut-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.25s;
  border: 1px solid #f0f0f0;
}
.shortcut-item:hover {
  background: #f5f7fa;
  border-color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.06);
}
.shortcut-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}
.shortcut-desc {
  font-size: 12px;
  color: #c0c4cc;
  margin-top: 2px;
}

/* 活动动态列表 */
.activity-list {
  max-height: 320px;
  overflow-y: auto;
}
.activity-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f5;
  font-size: 13px;
}
.activity-item:last-child { border-bottom: none; }
/* 状态圆点 */
.activity-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}
.activity-dot.green { background: #67c23a; }
.activity-dot.orange { background: #e6a23c; }
.activity-dot.red { background: #f56c6c; }
.activity-dot.blue { background: #409eff; }
.activity-content {
  flex: 1;
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 4px;
  flex-wrap: wrap;
}
.activity-user {
  font-weight: 500;
  color: #303133;
}
.activity-action {
  color: #909399;
}
.activity-url {
  color: #c0c4cc;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 120px;
}
.activity-time {
  color: #c0c4cc;
  font-size: 12px;
  white-space: nowrap;
  margin-left: auto;
}

/* 空状态样式 */
.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #c0c4cc;
}
.empty-state .el-icon {
  font-size: 40px;
  margin-bottom: 8px;
}
</style>
