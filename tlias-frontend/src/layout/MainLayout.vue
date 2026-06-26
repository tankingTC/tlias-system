<template>
  <el-container class="main-layout">
    <!-- 顶部导航：渐变流光 + 毛玻璃 -->
    <el-header class="header">
      <div class="header-glow"></div>
      <div class="header-left">
        <el-icon class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <Fold v-if="!isCollapsed" />
          <Expand v-else />
        </el-icon>
        <!-- Logo：蓝青渐变六边形 -->
        <svg class="logo-icon" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M16 2L28 9v14l-12 7L4 23V9L16 2z" fill="url(#headerGrad)" />
          <path d="M16 10l-5 3v6l5 3 5-3v-6l-5-3z" fill="rgba(255,255,255,0.6)" />
          <defs>
            <linearGradient id="headerGrad" x1="4" y1="4" x2="28" y2="28">
              <stop offset="0%" stop-color="#3b82f6" />
              <stop offset="100%" stop-color="#06b6d4" />
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
      <el-aside :width="isCollapsed ? '64px' : '220px'" class="aside">
        <div class="aurora aurora-1"></div>
        <div class="aurora aurora-2"></div>
        <div class="aurora aurora-3"></div>
        
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
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
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
}

/* ==================== 顶部导航：玻璃效果（对齐登录页风格） ==================== */
.header {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border: none;
  padding: 0 24px;
  height: 56px;
  box-shadow:
    0 4px 16px rgba(0, 0, 0, 0.08),
    0 0 0 1px rgba(255, 255, 255, 0.3) inset,
    0 1px 0 rgba(255, 255, 255, 0.4) inset;
  flex-shrink: 0;
  z-index: 10;
}

/* 渐变发光边框（与登录页一致） */
.header::after {
  content: '';
  position: absolute;
  inset: 0;
  padding: 1px;
  border-radius: 0;
  background: conic-gradient(
    from 0deg,
    rgba(255,255,255,0.7),
    rgba(160,200,255,0.6),
    rgba(255,255,255,0.4),
    rgba(180,220,255,0.6),
    rgba(255,255,255,0.7)
  );
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  mask-composite: exclude;
  pointer-events: none;
  animation: rotateBorder 6s linear infinite;
}

/* 顶部渐变流光条：蓝→青→绿→蓝 */
.header-glow {
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 3px;
  background: linear-gradient(90deg, #3b82f6, #06b6d4, #10b981, #3b82f6);
  background-size: 200% 200%;
  animation: gradientShift 4s ease infinite;
}
@keyframes gradientShift {
  0%   { background-position: 0% 50%; }
  50%  { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
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
  color: #3b82f6;
  background: rgba(59, 130, 246, 0.08);
}
.logo-icon {
  width: 30px;
  height: 30px;
  filter: drop-shadow(0 2px 6px rgba(59, 130, 246, 0.3));
}
.logo-text {
  font-size: 16px;
  font-weight: 700;
  background: linear-gradient(135deg, #3b82f6, #06b6d4);
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
  background: rgba(59, 130, 246, 0.08);
  color: #3b82f6;
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
  background: linear-gradient(135deg, #3b82f6, #06b6d4) !important;
  color: #fff !important;
  font-weight: 600;
  font-size: 14px;
}
.user-name {
  font-size: 14px;
  color: #1e293b;
  font-weight: 500;
}

/* ==================== 侧边栏：极光玻璃 ==================== */
.aside {
  position: relative;
  background: rgba(15, 23, 42, 0.88) !important;
  backdrop-filter: blur(16px) saturate(150%);
  -webkit-backdrop-filter: blur(16px) saturate(150%);
  border-right: none;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

/* 极光光晕：蓝+青+绿 */
.aurora {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  pointer-events: none;
  will-change: transform;
}
.aurora-1 {
  width: 200px; height: 200px;
  background: rgba(59, 130, 246, 0.25);
  top: -40px; left: -40px;
  animation: drift1 20s ease-in-out infinite alternate;
}
.aurora-2 {
  width: 180px; height: 180px;
  background: rgba(6, 182, 212, 0.20);
  top: 45%; left: 25%;
  animation: drift2 25s ease-in-out infinite alternate;
}
.aurora-3 {
  width: 160px; height: 160px;
  background: rgba(16, 185, 129, 0.18);
  bottom: -30px; right: -20px;
  animation: drift3 22s ease-in-out infinite alternate;
}
@keyframes drift1 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(25px, -20px) scale(1.08); }
  100% { transform: translate(-10px, 15px) scale(0.95); }
}
@keyframes drift2 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(-20px, 25px) scale(1.06); }
  100% { transform: translate(15px, -15px) scale(0.97); }
}
@keyframes drift3 {
  0%   { transform: translate(0, 0) scale(1); }
  50%  { transform: translate(20px, -15px) scale(1.04); }
  100% { transform: translate(-15px, 20px) scale(0.98); }
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

/* 菜单项样式 */
.sidebar-menu :deep(.el-menu-item),
.sidebar-menu :deep(.el-sub-menu__title) {
  height: 42px;
  line-height: 42px;
  margin: 2px 8px;
  border-radius: 10px;
  color: rgba(255, 255, 255, 0.65) !important;
  background: transparent !important;
  position: relative;
  transition: all 0.2s ease;
}
.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.08) !important;
  color: #fff !important;
}
.sidebar-menu :deep(.el-menu-item:hover)::before {
  content: '';
  position: absolute;
  left: 0; top: 50%;
  transform: translateY(-50%);
  width: 3px; height: 20px;
  border-radius: 0 3px 3px 0;
  background: rgba(59, 130, 246, 0.5);
}
.sidebar-menu :deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.12) !important;
  color: #fff !important;
  font-weight: 500;
}
.sidebar-menu :deep(.el-menu-item.is-active)::before {
  content: '';
  position: absolute;
  left: 0; top: 50%;
  transform: translateY(-50%);
  width: 3px; height: 24px;
  border-radius: 0 4px 4px 0;
  background: linear-gradient(180deg, #3b82f6, #10b981);
  box-shadow: 0 0 8px rgba(59, 130, 246, 0.4);
}

/* 子菜单 */
.sidebar-menu :deep(.el-sub-menu .el-menu-item) {
  min-width: auto;
  padding-left: 52px !important;
  color: rgba(255, 255, 255, 0.55) !important;
  background: transparent !important;
  position: relative;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item:hover) {
  color: #fff !important;
  background: rgba(255, 255, 255, 0.06) !important;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item:hover)::before {
  content: '';
  position: absolute;
  left: 0; top: 50%;
  transform: translateY(-50%);
  width: 3px; height: 20px;
  border-radius: 0 3px 3px 0;
  background: rgba(59, 130, 246, 0.4);
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item.is-active) {
  color: #fff !important;
  background: rgba(255, 255, 255, 0.1) !important;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item.is-active)::before {
  content: '';
  position: absolute;
  left: 0; top: 50%;
  transform: translateY(-50%);
  width: 3px; height: 22px;
  border-radius: 0 4px 4px 0;
  background: linear-gradient(180deg, #3b82f6, #10b981);
  box-shadow: 0 0 6px rgba(59, 130, 246, 0.35);
}

.sidebar-menu :deep(.el-sub-menu__icon-arrow) {
  color: rgba(255, 255, 255, 0.35) !important;
}

.menu-divider {
  height: 1px;
  background: rgba(255, 255, 255, 0.06);
  margin: 10px 16px;
}

/* ==================== 内容区 ==================== */
.main {
  background: #f1f5f9;
  padding: 16px 20px;
  height: calc(100vh - 56px);
  overflow-y: auto;
}
.breadcrumb {
  margin-bottom: 16px;
}
</style>
