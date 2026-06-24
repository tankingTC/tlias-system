<!--
  数据统计页面
  功能：展示系统数据概览和多维度ECharts可视化图表
  布局：概览卡片 -> 饼图+柱状图 -> 仪表盘+雷达图
  图表类型：饼图（数据总览）、柱状图（指标对比）、仪表盘（就业率）、雷达图（模块数据量）
-->
<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left">
        <h2>数据统计</h2>
        <p>多维度数据分析与可视化展示</p>
      </div>
    </div>

    <!-- 概览卡片：显示部门、员工、学员、已就业数量 -->
    <el-row :gutter="16" v-loading="loading">
      <el-col :xs="12" :sm="6" v-for="item in overviewCards" :key="item.key">
        <el-card class="stat-card" shadow="hover" :body-style="{ padding: '16px' }">
          <div class="stat-icon" :class="item.color">
            <el-icon><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-value">{{ stats[item.key] || 0 }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第一行图表：饼图（数据总览）+ 柱状图（指标对比） -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" v-loading="loading">
          <template #header>
            <div class="card-header"><span>数据总览</span></div>
          </template>
          <!-- 饼图容器 -->
          <div ref="pieChartRef" style="width: 100%; height: 360px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" v-loading="loading">
          <template #header>
            <div class="card-header"><span>指标对比</span></div>
          </template>
          <!-- 柱状图容器 -->
          <div ref="barChartRef" style="width: 100%; height: 360px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行图表：仪表盘（就业率）+ 雷达图（模块数据量） -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" v-loading="loading">
          <template #header>
            <div class="card-header"><span>就业率分析</span></div>
          </template>
          <!-- 仪表盘容器 -->
          <div ref="gaugeChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" v-loading="loading">
          <template #header>
            <div class="card-header"><span>各模块数据量</span></div>
          </template>
          <!-- 雷达图容器 -->
          <div ref="radarChartRef" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
/**
 * 数据统计页面逻辑
 * 负责加载统计数据并初始化四种ECharts图表
 */
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '../../utils/request'

// 页面加载状态
const loading = ref(true)
// 统计数据
const stats = ref({})
// 四个图表的DOM引用
const pieChartRef = ref(null)
const barChartRef = ref(null)
const gaugeChartRef = ref(null)
const radarChartRef = ref(null)
// 图表实例集合（用于统一管理和销毁）
let charts = []
// 窗口resize事件处理器引用
let resizeHandler = null

// 概览卡片配置
const overviewCards = [
  { key: 'deptCount', label: '部门总数', icon: 'OfficeBuilding', color: 'blue' },
  { key: 'empCount', label: '员工总数', icon: 'UserFilled', color: 'green' },
  { key: 'studentCount', label: '学员总数', icon: 'Avatar', color: 'pink' },
  { key: 'jobCount', label: '已就业', icon: 'Suitcase', color: 'cyan' }
]

/**
 * 初始化所有ECharts图表
 * 等待DOM更新后依次创建饼图、柱状图、仪表盘、雷达图
 */
const initCharts = async () => {
  await nextTick()
  const d = stats.value

  // 饼图 - 数据总览：展示各部门/模块的数据占比
  if (pieChartRef.value) {
    const c = echarts.init(pieChartRef.value)
    charts.push(c)
    c.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { orient: 'vertical', left: 'left', top: 'center' },
      color: ['#409eff', '#67c23a', '#e6a23c', '#f093fb', '#00bcd4'],
      series: [{
        type: 'pie',
        radius: ['35%', '65%'],
        center: ['55%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 3 },
        label: { show: true, formatter: '{b}\n{c}' },
        data: [
          { value: d.deptCount || 0, name: '部门' },
          { value: d.empCount || 0, name: '员工' },
          { value: d.classCount || 0, name: '班级' },
          { value: d.studentCount || 0, name: '学员' },
          { value: d.jobCount || 0, name: '已就业' }
        ]
      }]
    })
  }

  // 柱状图 - 指标对比：各模块数据量的横向对比
  if (barChartRef.value) {
    const c = echarts.init(barChartRef.value)
    charts.push(c)
    c.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: ['部门', '员工', '班级', '学员', '已就业'] },
      yAxis: { type: 'value', minInterval: 1 },
      series: [{
        type: 'bar',
        barWidth: '50%',
        data: [
          { value: d.deptCount || 0, itemStyle: { color: '#409eff' } },
          { value: d.empCount || 0, itemStyle: { color: '#67c23a' } },
          { value: d.classCount || 0, itemStyle: { color: '#e6a23c' } },
          { value: d.studentCount || 0, itemStyle: { color: '#f093fb' } },
          { value: d.jobCount || 0, itemStyle: { color: '#00bcd4' } }
        ],
        itemStyle: { borderRadius: [6, 6, 0, 0] }
      }]
    })
  }

  // 仪表盘 - 就业率：以仪表盘形式展示就业率百分比
  if (gaugeChartRef.value) {
    const c = echarts.init(gaugeChartRef.value)
    charts.push(c)
    const rate = parseFloat(d.employmentRate) || 0
    c.setOption({
      series: [{
        type: 'gauge',
        startAngle: 200,
        endAngle: -20,
        min: 0,
        max: 100,
        splitNumber: 10,
        axisLine: {
          lineStyle: {
            width: 20,
            // 三段颜色：红色(<30%) -> 橙色(<70%) -> 绿色(>=70%)
            color: [[0.3, '#f56c6c'], [0.7, '#e6a23c'], [1, '#67c23a']]
          }
        },
        pointer: { itemStyle: { color: 'auto' }, width: 5 },
        axisTick: { distance: -20, length: 6, lineStyle: { color: '#fff', width: 1 } },
        splitLine: { distance: -24, length: 14, lineStyle: { color: '#fff', width: 2 } },
        axisLabel: { color: 'inherit', distance: 30, fontSize: 12 },
        detail: { valueAnimation: true, formatter: '{value}%', color: 'inherit', fontSize: 28, fontWeight: 'bold', offsetCenter: [0, '60%'] },
        title: { offsetCenter: [0, '85%'], fontSize: 14, color: '#909399' },
        data: [{ value: rate, name: '就业率' }]
      }]
    })
  }

  // 雷达图 - 模块数据量：各模块数据量的多维度对比
  if (radarChartRef.value) {
    const c = echarts.init(radarChartRef.value)
    charts.push(c)
    // 计算雷达图最大值（取最大数据的1.2倍）
    const maxVal = Math.max(d.deptCount || 1, d.empCount || 1, d.classCount || 1, d.studentCount || 1, d.jobCount || 1) * 1.2
    c.setOption({
      tooltip: {},
      legend: { bottom: 0, data: ['当前数据'] },
      radar: {
        indicator: [
          { name: '部门', max: maxVal },
          { name: '员工', max: maxVal },
          { name: '班级', max: maxVal },
          { name: '学员', max: maxVal },
          { name: '已就业', max: maxVal }
        ],
        shape: 'circle'
      },
      series: [{
        type: 'radar',
        data: [{
          value: [d.deptCount || 0, d.empCount || 0, d.classCount || 0, d.studentCount || 0, d.jobCount || 0],
          name: '当前数据',
          areaStyle: { color: 'rgba(64, 158, 255, 0.2)' },
          lineStyle: { color: '#409eff' },
          itemStyle: { color: '#409eff' }
        }]
      }]
    })
  }
}

/**
 * 窗口resize时调整所有图表尺寸
 */
const handleResize = () => {
  charts.forEach(c => c && c.resize())
}

// 页面挂载时加载统计数据并初始化图表
onMounted(async () => {
  try {
    const res = await request.get('/statistics/dashboard')
    stats.value = res.data
  } catch (e) { /* ignore */ }
  loading.value = false
  await initCharts()
  // 注册窗口resize事件
  resizeHandler = () => handleResize()
  window.addEventListener('resize', resizeHandler)
})

// 页面卸载时清理所有图表实例和事件监听
onUnmounted(() => {
  window.removeEventListener('resize', resizeHandler)
  charts.forEach(c => c && c.dispose())
  charts = []
})
</script>

<style scoped>
/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
}
</style>
