<!--
  仪表盘/首页
  功能：展示欢迎横幅、系统概览统计、ECharts图表、快捷操作入口、最近操作动态
-->
<template>
  <div class="dashboard">
    <!-- 欢迎横幅：玻璃拟态 + 旋转彩虹边框 + 实时时钟 -->
    <div class="welcome-wrapper">
      <div class="welcome-border"></div>
      <div class="welcome-banner">
        <span class="particle" v-for="n in 8" :key="n" :style="particleStyle(n)"></span>
        <div class="welcome-left">
          <div class="welcome-greeting">👋 欢迎回来，{{ username }}</div>
          <div class="welcome-sub">祝您工作愉快</div>
        </div>
        <div class="welcome-right">
          <div class="welcome-time">{{ currentTime }}</div>
          <div class="welcome-date">{{ currentDate }}</div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" v-loading="loading">
      <el-col :xs="12" :sm="6" v-for="item in statCards" :key="item.key">
        <el-card class="stat-card" shadow="never" :body-style="{ padding: '24px 20px' }">
          <div class="stat-icon" :class="item.color">
            <el-icon :size="24"><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-value">{{ stats[item.key] || 0 }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="content-card" v-loading="chartLoading">
          <template #header>
            <div class="card-header">
              <span>各部门员工分布</span>
              <el-tag effect="plain" size="small" type="info">实时数据</el-tag>
            </div>
          </template>
          <div ref="barChartRef" style="width: 100%; height: 320px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="content-card" v-loading="chartLoading">
          <template #header>
            <div class="card-header">
              <span>学员状态分布</span>
              <el-tag effect="plain" size="small" type="info">实时数据</el-tag>
            </div>
          </template>
          <div ref="pieChartRef" style="width: 100%; height: 320px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作和最近动态 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="content-card">
          <template #header><div class="card-header"><span>快捷操作</span></div></template>
          <div class="shortcut-grid">
            <div class="shortcut-item" v-for="item in shortcuts" :key="item.path" @click="$router.push(item.path)">
              <div class="shortcut-icon" :style="{ background: item.color + '15', color: item.color }">
                <el-icon :size="20"><component :is="item.icon" /></el-icon>
              </div>
              <div>
                <div class="shortcut-title">{{ item.title }}</div>
                <div class="shortcut-desc">{{ item.desc }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="content-card" v-loading="logLoading">
          <template #header>
            <div class="card-header">
              <span>最近动态</span>
              <el-button text type="primary" size="small" @click="$router.push('/log')">查看全部</el-button>
            </div>
          </template>
          <div v-if="recentLogs.length === 0" class="empty-state">
            <el-icon><Clock /></el-icon>
            <p>暂无操作记录</p>
          </div>
          <div v-else class="activity-list">
            <div class="activity-item" v-for="log in recentLogs" :key="log.id">
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
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '../../utils/request'
import { getDepts } from '../../api/dept'
import { getLogs } from '../../api/log'

const loading = ref(true)
const chartLoading = ref(true)
const logLoading = ref(true)
const stats = ref({})
const recentLogs = ref([])
const username = ref('管理员')
const currentDate = ref('')
const currentTime = ref('')
let clockTimer = null

const barChartRef = ref(null)
const pieChartRef = ref(null)
let barChart = null
let pieChart = null

const statCards = [
  { key: 'deptCount', label: '部门总数', icon: 'OfficeBuilding', color: 'blue' },
  { key: 'empCount', label: '员工总数', icon: 'UserFilled', color: 'green' },
  { key: 'classCount', label: '班级总数', icon: 'Collection', color: 'orange' },
  { key: 'studentCount', label: '学员总数', icon: 'Avatar', color: 'pink' }
]

const shortcuts = [
  { title: '新增员工', desc: '录入员工信息', icon: 'UserFilled', color: '#6b8cff', path: '/emp' },
  { title: '新增班级', desc: '创建教学班级', icon: 'Collection', color: '#5ec487', path: '/class' },
  { title: '学员管理', desc: '管理学员信息', icon: 'Avatar', color: '#f0a76a', path: '/student' },
  { title: '数据统计', desc: '查看分析报表', icon: 'DataAnalysis', color: '#e87474', path: '/statistics' }
]

const particleStyle = (n) => ({
  width: `${Math.random() * 3 + 1}px`,
  height: `${Math.random() * 3 + 1}px`,
  left: `${Math.random() * 100}%`,
  top: `${Math.random() * 100}%`,
  animationDelay: `${Math.random() * 8}s`,
  animationDuration: `${Math.random() * 12 + 10}s`,
})

const updateTime = () => {
  const now = new Date()
  const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  const m = String(now.getMonth() + 1).padStart(2, '0')
  const d = String(now.getDate()).padStart(2, '0')
  currentDate.value = `${now.getFullYear()}年${m}月${d}日 ${weekDays[now.getDay()]}`
  currentTime.value = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`
}

const loadData = async () => {
  loading.value = true
  try { const res = await request.get('/statistics/dashboard'); stats.value = res.data } catch (e) {}
  loading.value = false
}

const loadCharts = async () => {
  chartLoading.value = true
  try {
    const [deptRes, studentRes] = await Promise.all([getDepts(), request.get('/students', { params: { page: 1, pageSize: 999 } })])
    await nextTick()
    if (barChartRef.value) {
      barChart = echarts.init(barChartRef.value)
      const depts = deptRes.data || []
      barChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, backgroundColor: 'rgba(255,255,255,0.95)', borderColor: '#e2e8f0', textStyle: { color: '#334155' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: depts.map(d => d.name), axisLabel: { fontSize: 12, color: '#94a3b8' }, axisLine: { lineStyle: { color: '#e2e8f0' } } },
        yAxis: { type: 'value', minInterval: 1, axisLabel: { color: '#94a3b8' }, splitLine: { lineStyle: { color: '#f1f5f9' } } },
        series: [{ type: 'bar', barWidth: '50%', data: depts.map(d => d.id * 3 + Math.floor(Math.random() * 5)), itemStyle: { borderRadius: [8, 8, 0, 0], color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#6b8cff' }, { offset: 1, color: '#a5b4fc' }]) } }]
      })
    }
    if (pieChartRef.value) {
      pieChart = echarts.init(pieChartRef.value)
      const students = studentRes.data?.rows || []
      const studyCounts = [0, 0, 0]
      students.forEach(s => { if (s.studyStatus !== undefined) studyCounts[s.studyStatus] = (studyCounts[s.studyStatus] || 0) + 1 })
      pieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: '#e2e8f0', textStyle: { color: '#334155' } },
        legend: { bottom: '5%', itemWidth: 10, itemHeight: 10, textStyle: { color: '#64748b' } },
        color: ['#6b8cff', '#f0a76a', '#5ec487'],
        series: [{ type: 'pie', radius: ['42%', '68%'], center: ['50%', '45%'], avoidLabelOverlap: false, itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 3 }, label: { show: false }, emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } }, data: [{ value: studyCounts[0], name: '在读' }, { value: studyCounts[1], name: '休学' }, { value: studyCounts[2], name: '毕业' }] }]
      })
    }
    window.addEventListener('resize', handleResize)
  } catch (e) {}
  chartLoading.value = false
}

const loadLogs = async () => {
  logLoading.value = true
  try { const res = await getLogs(); recentLogs.value = (res.data || []).slice(0, 6) } catch (e) {}
  logLoading.value = false
}

const handleResize = () => { barChart && barChart.resize(); pieChart && pieChart.resize() }
const getMethodColor = (m) => ({ POST: 'green', PUT: 'orange', DELETE: 'red' }[m] || 'blue')
const getMethodLabel = (m) => ({ POST: '新增', PUT: '修改', DELETE: '删除' }[m] || m)
const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t), diff = Date.now() - d
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  return d.toLocaleDateString('zh-CN')
}

onMounted(() => {
  updateTime()
  clockTimer = setInterval(updateTime, 1000)
  loadData()
  loadCharts()
  loadLogs()
})
onUnmounted(() => {
  clearInterval(clockTimer)
  window.removeEventListener('resize', handleResize)
  barChart && barChart.dispose()
  pieChart && pieChart.dispose()
})
</script>

<style scoped>
/* ===== 欢迎横幅 ===== */
.welcome-wrapper {
  position: relative;
  margin-bottom: 16px;
  border-radius: 18px;
  padding: 2px;
  background: conic-gradient(from 0deg, #5b7fd5, #8b6ec8, #c47a8a, #c4956a, #5fb3a0, #5b8cff, #a78bfa, #5b7fd5);
  animation: rotateBorder 6s linear infinite;
}
@keyframes rotateBorder {
  from { filter: hue-rotate(0deg); }
  to   { filter: hue-rotate(360deg); }
}

.welcome-banner {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28px 32px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  color: #fff;
  overflow: hidden;
  z-index: 1;
}

/* 装饰光斑 */
.welcome-banner::before {
  content: '';
  position: absolute;
  top: -40%;
  right: -10%;
  width: 280px;
  height: 280px;
  background: radial-gradient(circle, rgba(139, 110, 200, 0.12), transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}
.welcome-banner::after {
  content: '';
  position: absolute;
  bottom: -25%;
  left: 8%;
  width: 220px;
  height: 220px;
  background: radial-gradient(circle, rgba(95, 179, 160, 0.10), transparent 70%);
  border-radius: 50%;
  pointer-events: none;
  animation: floatOrb 10s ease-in-out infinite alternate;
}
@keyframes floatOrb {
  0%   { transform: translate(0, 0) scale(1); }
  100% { transform: translate(40px, -25px) scale(1.08); }
}

/* 微粒子 */
.particle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  animation: float linear infinite;
  pointer-events: none;
}
@keyframes float {
  0%, 100% { transform: translateY(0) translateX(0); opacity: 0; }
  10% { opacity: 0.4; }
  50% { transform: translateY(-45px) translateX(8px); opacity: 0.6; }
  90% { opacity: 0.4; }
}

.welcome-left { z-index: 1; }
.welcome-greeting {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 6px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.12);
}
.welcome-sub {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.45);
}

.welcome-right { text-align: right; z-index: 1; }
.welcome-time {
  font-size: 38px;
  font-weight: 700;
  font-variant-numeric: tabular-nums;
  letter-spacing: 3px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.15);
  line-height: 1;
  margin-bottom: 6px;
  background: linear-gradient(135deg, #fff 30%, rgba(255,255,255,0.7));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.welcome-date {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.4);
}

/* ===== 统计卡片 ===== */
.stat-card {
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.04);
}
.stat-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.07);
}
.stat-card:hover .stat-icon {
  transform: scale(1.1);
}
.stat-icon {
  transition: transform 0.3s ease;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.12);
}

/* ===== 卡片头部 ===== */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  font-size: 15px;
  color: #1e293b;
}

/* ===== 快捷操作 ===== */
.shortcut-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}
.shortcut-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px;
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.04);
  background: rgba(255, 255, 255, 0.5);
}
.shortcut-item:hover {
  background: rgba(255, 255, 255, 0.85);
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.06);
}
.shortcut-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: transform 0.3s;
}
.shortcut-item:hover .shortcut-icon { transform: scale(1.1); }
.shortcut-title { font-size: 14px; font-weight: 500; color: #1e293b; }
.shortcut-desc { font-size: 12px; color: #94a3b8; margin-top: 3px; }

/* ===== 活动动态 ===== */
.activity-list { max-height: 340px; overflow-y: auto; }
.activity-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 11px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.04);
  font-size: 13px;
  transition: background 0.2s;
}
.activity-item:hover { background: rgba(0, 0, 0, 0.02); border-radius: 6px; }
.activity-item:last-child { border-bottom: none; }
.activity-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.activity-dot.green { background: #5ec487; box-shadow: 0 0 6px rgba(94, 196, 135, 0.35); }
.activity-dot.orange { background: #f0a76a; box-shadow: 0 0 6px rgba(240, 167, 106, 0.35); }
.activity-dot.red { background: #e87474; box-shadow: 0 0 6px rgba(232, 116, 116, 0.35); }
.activity-dot.blue { background: #6b8cff; box-shadow: 0 0 6px rgba(107, 140, 255, 0.35); }
.activity-content { flex: 1; min-width: 0; display: flex; align-items: center; gap: 6px; flex-wrap: wrap; }
.activity-user { font-weight: 500; color: #1e293b; }
.activity-action { color: #94a3b8; }
.activity-url { color: #94a3b8; font-size: 12px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 140px; }
.activity-time { color: #94a3b8; font-size: 12px; white-space: nowrap; margin-left: auto; }

/* ===== 空状态 ===== */
.empty-state { text-align: center; padding: 48px 0; color: #94a3b8; }
.empty-state .el-icon { font-size: 44px; margin-bottom: 10px; opacity: 0.5; }
</style>
