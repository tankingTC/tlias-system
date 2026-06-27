<template>
  <el-container class="main-layout">
    <!-- 第三层流光：小尺寸快速光斑，增强动态层次 -->
    <div class="flow-orb flow-orb-1"></div>
    <div class="flow-orb flow-orb-2"></div>
    <div class="flow-orb flow-orb-3"></div>
    <div class="flow-orb flow-orb-4"></div>

    <!-- 顶部导航：渐变流光 + 毛玻璃 -->
    <el-header class="header">
      <div class="header-left">
        <el-icon class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <Fold v-if="!isCollapsed" />
          <Expand v-else />
        </el-icon>
        <!-- Logo：蓝青渐变六边形 -->
        <svg class="logo-icon" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M16 2L28 9v14l-12 7L4 23V9L16 2z" fill="url(#headerGrad)" />
          <path d="M16 10l-5 3v6l5 3 5-3v-6l-5-3z" fill="rgba(255,255,255,0.7)" />
          <defs>
            <linearGradient id="headerGrad" x1="4" y1="4" x2="28" y2="28">
              <stop offset="0%" stop-color="#93b9eb" />
              <stop offset="100%" stop-color="#8cc8e1" />
            </linearGradient>
          </defs>
        </svg>
        <span class="logo-text">智学云帆-教学管理系统</span>
      </div>
      <div class="header-right">
        <el-tooltip content="刷新页面" placement="bottom">
          <el-icon class="header-icon" @click="refreshPage"><Refresh /></el-icon>
        </el-tooltip>
        <el-tooltip content="全屏" placement="bottom">
          <el-icon class="header-icon" @click="toggleFullscreen"><FullScreen /></el-icon>
        </el-tooltip>
        <div class="header-divider"></div>
        <el-dropdown @command="handleCommand" trigger="click">
          <div class="user-info">
            <el-avatar :size="32" :src="userImage || undefined" class="user-avatar">
              {{ username.charAt(0) }}
            </el-avatar>
            <span class="user-name">{{ username }}</span>
            <el-icon class="el-icon--right" style="color: #94a3b8;"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container>
      <!-- 侧边栏：极光玻璃效果 -->
      <el-aside :width="isCollapsed ? '64px' : '190px'" class="aside">
        <div class="aurora aurora-1"></div>
        <div class="aurora aurora-2"></div>
        <div class="aurora aurora-3"></div>
        <div class="aurora aurora-4"></div>
        <div class="aurora aurora-5"></div>
        
        <el-scrollbar class="glass-layer">
          <el-menu
            :default-active="activeMenu"
            :collapse="isCollapsed"
            :collapse-transition="false"
            router
            class="sidebar-menu"
          >
            <el-menu-item index="/dashboard">
              <el-icon><DataBoard /></el-icon>
              <template #title>首页</template>
            </el-menu-item>
            <div class="menu-divider"></div>

            <el-sub-menu index="teaching">
              <template #title><el-icon><Reading /></el-icon><span>教学管理</span></template>
              <el-menu-item index="/dept"><el-icon><OfficeBuilding /></el-icon><template #title>部门管理</template></el-menu-item>
              <el-menu-item index="/emp"><el-icon><UserFilled /></el-icon><template #title>员工管理</template></el-menu-item>
              <el-menu-item index="/class"><el-icon><Collection /></el-icon><template #title>班级管理</template></el-menu-item>
              <el-menu-item index="/student"><el-icon><Avatar /></el-icon><template #title>学员管理</template></el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="operation">
              <template #title><el-icon><Suitcase /></el-icon><span>运营管理</span></template>
              <el-menu-item index="/exam"><el-icon><Document /></el-icon><template #title>考试管理</template></el-menu-item>
              <el-menu-item index="/job"><el-icon><Briefcase /></el-icon><template #title>就业管理</template></el-menu-item>
              <el-menu-item index="/feedback"><el-icon><ChatDotRound /></el-icon><template #title>反馈管理</template></el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="system">
              <template #title><el-icon><Setting /></el-icon><span>系统管理</span></template>
              <el-menu-item index="/statistics"><el-icon><DataAnalysis /></el-icon><template #title>数据统计</template></el-menu-item>
              <el-menu-item index="/log"><el-icon><Tickets /></el-icon><template #title>操作日志</template></el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-scrollbar>
      </el-aside>

      <!-- 内容区 -->
      <el-main class="main">
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item :to="{ path: '/dashboard' }">
            <el-icon style="vertical-align: middle;"><HomeFilled /></el-icon> 首页
          </el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentTitle">{{ currentTitle }}</el-breadcrumb-item>
        </el-breadcrumb>
        <router-view v-slot="{ Component }">
          <transition name="fade-transform">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserInfo } from '../api/user'

const router = useRouter()
const route = useRoute()
const isCollapsed = ref(false)
const username = ref('管理员')
const userImage = ref('')

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title)

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    if (res.data) {
      username.value = res.data.username || '管理员'
      userImage.value = res.data.image || ''
    }
  } catch (e) {}
}

onMounted(() => { loadUserInfo() })

const refreshPage = () => { router.go(0) }

const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token')
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
.main-layout {
  height: 100vh;
  overflow: hidden;
  position: relative;
  /* 柔和浅色基底 */
  background: #f4f7fc;
}

/* ==================== 多层流光背景系统 ==================== */
/* 使用伪元素 + transform 动画实现 GPU 加速的 60fps 流光效果 */
/* 降饱和、提亮度，保留蓝-青-紫色彩基调 */
.main-layout::before,
.main-layout::after {
  content: '';
  position: absolute;
  inset: -120px; /* 扩大区域避免动画露出边缘 */
  pointer-events: none;
  z-index: 0;
  will-change: transform;
}

/* 第一层：大尺寸柔光 — 慢速漂移，营造整体氛围 */
.main-layout::before {
  background:
    radial-gradient(ellipse 650px 550px at 15% 20%,
      rgba(167, 199, 239, 0.55) 0%, transparent 70%),
    radial-gradient(ellipse 500px 600px at 80% 15%,
      rgba(178, 210, 245, 0.45) 0%, transparent 70%),
    radial-gradient(ellipse 600px 500px at 70% 85%,
      rgba(190, 200, 235, 0.4) 0%, transparent 70%),
    radial-gradient(ellipse 550px 450px at 25% 80%,
      rgba(170, 215, 240, 0.35) 0%, transparent 70%);
  animation: flowLayer1 28s ease-in-out infinite alternate;
}

/* 第二层：中尺寸光斑 — 中速游走，增加动态层次 */
.main-layout::after {
  background:
    radial-gradient(ellipse 380px 340px at 40% 35%,
      rgba(186, 210, 248, 0.5) 0%, transparent 65%),
    radial-gradient(ellipse 320px 380px at 65% 60%,
      rgba(196, 220, 250, 0.4) 0%, transparent 65%),
    radial-gradient(ellipse 350px 300px at 20% 65%,
      rgba(200, 218, 245, 0.35) 0%, transparent 65%);
  animation: flowLayer2 22s ease-in-out infinite alternate;
}

@keyframes flowLayer1 {
  0%   { transform: translate(0, 0) scale(1); }
  33%  { transform: translate(50px, -35px) scale(1.04); }
  66%  { transform: translate(-30px, 25px) scale(0.97); }
  100% { transform: translate(20px, -15px) scale(1.02); }
}

@keyframes flowLayer2 {
  0%   { transform: translate(0, 0) scale(1); }
  33%  { transform: translate(-40px, 30px) scale(1.05); }
  66%  { transform: translate(35px, -20px) scale(0.96); }
  100% { transform: translate(-15px, 40px) scale(1.03); }
}

/* 第三层：小尺寸快速光斑 — 小椭圆径向渐变，纯 transform 驱动，GPU 加速 */
.flow-orb {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;
  will-change: transform;
}
.flow-orb-1 {
  width: 300px; height: 280px;
  background: radial-gradient(ellipse, rgba(165, 200, 240, 0.5) 0%, transparent 70%);
  top: 10%; left: 30%;
  animation: orbFloat1 18s ease-in-out infinite alternate;
}
.flow-orb-2 {
  width: 240px; height: 260px;
  background: radial-gradient(ellipse, rgba(175, 210, 245, 0.4) 0%, transparent 70%);
  top: 50%; right: 15%;
  animation: orbFloat2 15s ease-in-out infinite alternate;
}
.flow-orb-3 {
  width: 280px; height: 220px;
  background: radial-gradient(ellipse, rgba(185, 205, 240, 0.35) 0%, transparent 70%);
  bottom: 10%; left: 10%;
  animation: orbFloat3 20s ease-in-out infinite alternate;
}
.flow-orb-4 {
  width: 200px; height: 200px;
  background: radial-gradient(ellipse, rgba(170, 215, 235, 0.3) 0%, transparent 70%);
  top: 30%; left: 55%;
  animation: orbFloat4 16s ease-in-out infinite alternate;
}
@keyframes orbFloat1 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(60px, -40px) scale(1.08); }
  100% { transform: translate(-30px, 50px) scale(0.95); }
}
@keyframes orbFloat2 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(-50px, 35px) scale(1.06); }
  100% { transform: translate(40px, -25px) scale(0.97); }
}
@keyframes orbFloat3 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(45px, -55px) scale(1.1); }
  100% { transform: translate(-35px, 30px) scale(0.93); }
}
@keyframes orbFloat4 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(-40px, -30px) scale(1.05); }
  100% { transform: translate(50px, 45px) scale(0.96); }
}

/* ==================== 顶部导航：透明玻璃 + 环绕流光 ==================== */
.header {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: transparent;
  backdrop-filter: blur(16px) saturate(150%);
  -webkit-backdrop-filter: blur(16px) saturate(150%);
  border: none;
  padding: 0 24px;
  height: 56px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  flex-shrink: 0;
  z-index: 10;
}

/* 环绕流光边框：降饱和柔和色 + hue-rotate */
.header::before {
  content: '';
  position: absolute;
  inset: 0;
  padding: 1.5px;
  background: conic-gradient(
    from 0deg,
    rgba(147, 185, 235, 0.8),
    rgba(140, 200, 225, 0.7),
    rgba(155, 205, 195, 0.65),
    rgba(147, 185, 235, 0.8)
  );
  -webkit-mask:
    linear-gradient(#fff 0 0) content-box,
    linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask:
    linear-gradient(#fff 0 0) content-box,
    linear-gradient(#fff 0 0);
  mask-composite: exclude;
  pointer-events: none;
  animation: borderGlow 4s linear infinite;
}
@keyframes borderGlow {
  from { filter: hue-rotate(0deg); }
  to   { filter: hue-rotate(360deg); }
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #64748b;
  transition: all 0.2s;
  padding: 6px;
  border-radius: 8px;
}
.collapse-btn:hover {
  color: #7ba3d6;
  background: rgba(147, 185, 235, 0.1);
}
.logo-icon {
  width: 30px;
  height: 30px;
  filter: drop-shadow(0 2px 6px rgba(59, 130, 246, 0.3));
}
.logo-text {
  font-size: 16px;
  font-weight: 700;
  background: linear-gradient(135deg, #7ba3d6, #8cc8e1);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 0.5px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
.header-icon {
  font-size: 18px;
  color: #64748b;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.2s;
}
.header-icon:hover {
  background: rgba(147, 185, 235, 0.1);
  color: #7ba3d6;
}
.header-divider {
  width: 1px;
  height: 20px;
  background: rgba(0, 0, 0, 0.08);
  margin: 0 8px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 10px;
  border-radius: 10px;
  transition: background 0.2s;
}
.user-info:hover { background: rgba(0, 0, 0, 0.04); }
.user-avatar {
  background: linear-gradient(135deg, #93b9eb, #8cc8e1) !important;
  color: #fff !important;
  font-weight: 600;
  font-size: 14px;
}
.user-name {
  font-size: 14px;
  color: #1e293b;
  font-weight: 500;
}

/* ==================== 侧边栏：极光玻璃（浅色柔和版） ==================== */
.aside {
  position: relative;
  /* 优雅降级：不支持backdrop-filter时使用接近不透明的白色 */
  background: rgba(255, 255, 255, 0.95) !important;
  border-right: none;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.04),
    1px 0 0 rgba(255, 255, 255, 0.6) inset; /* 模拟玻璃边缘高光 */
}

@supports (backdrop-filter: blur(1px)) or (-webkit-backdrop-filter: blur(1px)) {
  .aside {
    /* 极致通透玻璃效果：参考登录页的高通透、高模糊、高饱和参数 */
    background: rgba(255, 255, 255, 0.25) !important;
    backdrop-filter: blur(24px) saturate(180%);
    -webkit-backdrop-filter: blur(24px) saturate(180%);
  }
}

/* 背景微动画层：浅色渐变缓慢上下移动 */
.aside::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg,
    rgba(147, 185, 235, 0.06),
    rgba(140, 200, 225, 0.04),
    rgba(175, 170, 220, 0.06),
    rgba(210, 175, 200, 0.04)
  );
  background-size: 100% 200%;
  animation: sidebarBg 8s ease-in-out infinite alternate;
  pointer-events: none;
  z-index: 0;
}
@keyframes sidebarBg {
  0%   { background-position: 0% 0%; }
  100% { background-position: 0% 100%; }
}

/* 右侧边缘浅色分割线 */
.aside::after {
  content: '';
  position: absolute;
  top: 0; bottom: 0; right: 0;
  width: 1px;
  background: linear-gradient(180deg,
    transparent,
    rgba(0, 0, 0, 0.06),
    rgba(0, 0, 0, 0.06),
    transparent
  );
  z-index: 2;
  pointer-events: none;
}

/* 极光光晕：柔和浅色版 — 降饱和、提亮度，与主背景流光协调统一 */
.aurora {
  position: absolute;
  border-radius: 50%;
  filter: blur(50px);
  pointer-events: none;
  will-change: transform;
}
.aurora-1 {
  width: 240px; height: 240px;
  background: rgba(147, 185, 235, 0.45);
  top: -50px; left: -50px;
  animation: drift1 14s ease-in-out infinite alternate;
}
.aurora-2 {
  width: 200px; height: 200px;
  background: rgba(140, 200, 225, 0.38);
  top: 40%; left: 20%;
  animation: drift2 16s ease-in-out infinite alternate;
}
.aurora-3 {
  width: 220px; height: 220px;
  background: rgba(155, 205, 195, 0.32);
  bottom: -30px; right: -20px;
  animation: drift3 14s ease-in-out infinite alternate;
}
.aurora-4 {
  width: 180px; height: 180px;
  background: rgba(175, 170, 220, 0.35);
  top: 20%; right: -30px;
  animation: drift4 18s ease-in-out infinite alternate;
}
.aurora-5 {
  width: 160px; height: 160px;
  background: rgba(210, 175, 200, 0.3);
  bottom: 20%; left: 10%;
  animation: drift5 15s ease-in-out infinite alternate;
}
@keyframes drift1 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(35px, -30px) scale(1.1); }
  100% { transform: translate(-15px, 20px) scale(0.92); }
}
@keyframes drift2 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(-30px, 35px) scale(1.08); }
  100% { transform: translate(20px, -20px) scale(0.95); }
}
@keyframes drift3 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(25px, -25px) scale(1.06); }
  100% { transform: translate(-20px, 30px) scale(0.96); }
}
@keyframes drift4 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(-20px, -30px) scale(1.05); }
  100% { transform: translate(25px, 15px) scale(0.94); }
}
@keyframes drift5 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(30px, 20px) scale(1.07); }
  100% { transform: translate(-25px, -25px) scale(0.93); }
}

.glass-layer {
  position: relative;
  z-index: 1;
  background: transparent !important;
}

.aside :deep(.el-aside) { background: transparent !important; }
.aside :deep(.el-scrollbar) { background: transparent !important; }
.aside :deep(.el-scrollbar__wrap) { background: transparent !important; }
.aside :deep(.el-scrollbar__view) { background: transparent !important; }
.aside :deep(.el-menu) { background: transparent !important; border-right: none !important; }

/* 菜单项样式优化 */
.sidebar-menu :deep(.el-menu-item),
.sidebar-menu :deep(.el-sub-menu__title) {
  height: 40px;
  line-height: 40px;
  margin: 4px 8px;
  border-radius: 8px;
  color: #475569 !important; /* 调整为柔和的深灰色 */
  background: transparent !important;
  position: relative;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  font-size: 13px;
}

/* 图标过渡动画 */
.sidebar-menu :deep(.el-menu-item .el-icon),
.sidebar-menu :deep(.el-sub-menu__title .el-icon) {
  transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1), color 0.25s;
}

/* Hover 状态 */
.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: linear-gradient(90deg, rgba(147, 185, 235, 0.1), rgba(140, 200, 225, 0.04)) !important;
  color: #1e293b !important;
}
.sidebar-menu :deep(.el-menu-item:hover .el-icon),
.sidebar-menu :deep(.el-sub-menu__title:hover .el-icon) {
  transform: translateY(-2px); /* 悬浮时图标轻微上浮 */
  color: #7ba3d6;
}

/* 激活状态 */
.sidebar-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(147, 185, 235, 0.14), rgba(140, 200, 225, 0.06)) !important;
  color: #6a9ad4 !important; /* 柔和蓝色高亮 */
  font-weight: 600;
}
.sidebar-menu :deep(.el-menu-item.is-active)::before {
  content: '';
  position: absolute;
  left: -4px; top: 50%;
  transform: translateY(-50%);
  width: 4px; height: 20px;
  border-radius: 0 4px 4px 0;
  background: linear-gradient(180deg, #93b9eb, #8cc8e1);
  box-shadow: 0 0 8px rgba(147, 185, 235, 0.3);
}

/* 子菜单样式优化 */
.sidebar-menu :deep(.el-sub-menu .el-menu-item) {
  min-width: auto;
  padding-left: 46px !important;
  color: #64748b !important;
  background: transparent !important;
  position: relative;
  font-size: 13px;
  height: 38px;
  line-height: 38px;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item:hover) {
  color: #1e293b !important;
  background: linear-gradient(90deg, rgba(147, 185, 235, 0.08), transparent) !important;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item:hover .el-icon) {
  transform: translateY(-2px);
  color: #7ba3d6;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item.is-active) {
  color: #6a9ad4 !important;
  background: linear-gradient(90deg, rgba(147, 185, 235, 0.12), transparent) !important;
  font-weight: 600;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item.is-active)::before {
  content: '';
  position: absolute;
  left: -4px; top: 50%;
  transform: translateY(-50%);
  width: 4px; height: 18px;
  border-radius: 0 4px 4px 0;
  background: linear-gradient(180deg, #93b9eb, #8cc8e1);
  box-shadow: 0 0 6px rgba(147, 185, 235, 0.25);
}

/* 子菜单箭头颜色 */
.sidebar-menu :deep(.el-sub-menu__icon-arrow) {
  color: #94a3b8 !important;
}

/* 模块分割线 */
.menu-divider {
  height: 1px;
  background: rgba(0, 0, 0, 0.06);
  margin: 10px 16px;
}

/* ==================== 内容区 ==================== */
.main {
  background: #f1f5f9;
  padding: 12px 16px;
  height: calc(100vh - 56px);
  overflow-y: auto;
}
.breadcrumb {
  margin-bottom: 10px;
}
</style>
