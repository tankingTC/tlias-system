<template>
  <el-container class="main-layout">
    <!-- 顶部导航 -->
    <el-header class="header" :class="{ 'header-collapsed': isCollapsed }">
      <div class="header-left">
        <el-icon class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <Fold v-if="!isCollapsed" />
          <Expand v-else />
        </el-icon>
        <el-icon :size="22" color="#409eff"><School /></el-icon>
        <span class="logo-text" v-show="!isCollapsed">智学云帆</span>
      </div>
      <div class="header-right">
        <el-tooltip content="刷新页面" placement="bottom">
          <el-icon class="header-icon" @click="refreshPage"><Refresh /></el-icon>
        </el-tooltip>
        <el-tooltip content="全屏" placement="bottom">
          <el-icon class="header-icon" @click="toggleFullscreen"><FullScreen /></el-icon>
        </el-tooltip>
        <el-dropdown @command="handleCommand" trigger="click">
          <div class="user-info">
            <el-avatar :size="32" :src="userImage || undefined" class="user-avatar">
              {{ username.charAt(0) }}
            </el-avatar>
            <span class="user-name">{{ username }}</span>
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
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
      <!-- 侧边栏 -->
      <el-aside :width="isCollapsed ? '64px' : '220px'" class="aside" :class="{ 'aside-collapsed': isCollapsed }">
        <el-scrollbar>
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

            <el-sub-menu index="teaching">
              <template #title>
                <el-icon><Reading /></el-icon>
                <span>教学管理</span>
              </template>
              <el-menu-item index="/dept">
                <el-icon><OfficeBuilding /></el-icon>
                <template #title>部门管理</template>
              </el-menu-item>
              <el-menu-item index="/emp">
                <el-icon><UserFilled /></el-icon>
                <template #title>员工管理</template>
              </el-menu-item>
              <el-menu-item index="/class">
                <el-icon><Collection /></el-icon>
                <template #title>班级管理</template>
              </el-menu-item>
              <el-menu-item index="/student">
                <el-icon><Avatar /></el-icon>
                <template #title>学员管理</template>
              </el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="operation">
              <template #title>
                <el-icon><Briefcase /></el-icon>
                <span>运营管理</span>
              </template>
              <el-menu-item index="/exam">
                <el-icon><Document /></el-icon>
                <template #title>考试管理</template>
              </el-menu-item>
              <el-menu-item index="/job">
                <el-icon><Suitcase /></el-icon>
                <template #title>就业管理</template>
              </el-menu-item>
              <el-menu-item index="/feedback">
                <el-icon><ChatDotRound /></el-icon>
                <template #title>反馈管理</template>
              </el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="system">
              <template #title>
                <el-icon><Setting /></el-icon>
                <span>系统管理</span>
              </template>
              <el-menu-item index="/statistics">
                <el-icon><DataAnalysis /></el-icon>
                <template #title>数据统计</template>
              </el-menu-item>
              <el-menu-item index="/log">
                <el-icon><Tickets /></el-icon>
                <template #title>操作日志</template>
              </el-menu-item>
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
/**
 * 主布局组件
 * 采用经典后台管理布局：固定 Header + 可折叠侧边栏 + 内容区
 * 功能：
 * 1. 侧边栏折叠/展开切换
 * 2. 页面刷新、全屏切换
 * 3. 用户信息展示和退出登录
 * 4. 面包屑导航（根据路由 meta.title 自动显示）
 */
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserInfo } from '../api/user'

const router = useRouter()
const route = useRoute()
const isCollapsed = ref(false) // 侧边栏折叠状态
const username = ref('管理员') // 当前登录用户名
const userImage = ref('') // 用户头像 URL

// 计算当前激活的菜单项（根据路由路径）
const activeMenu = computed(() => route.path)
// 计算当前页面标题（用于面包屑）
const currentTitle = computed(() => route.meta.title)

/**
 * 加载当前登录用户信息
 * 从后端 API 获取用户名和头像，显示在顶部导航栏
 */
const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    if (res.data) {
      username.value = res.data.username || '管理员'
      userImage.value = res.data.image || ''
    }
  } catch (e) { /* 加载失败时使用默认值 */ }
}

onMounted(() => { loadUserInfo() })

/** 刷新当前页面 */
const refreshPage = () => {
  router.go(0)
}

/** 切换全屏模式 */
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

/** 处理下拉菜单命令 */
const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token') // 清除 Token
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

/* 顶部导航 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 20px;
  height: 56px;
  transition: all 0.3s;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  flex-shrink: 0;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
  transition: color 0.2s;
  padding: 4px;
}
.collapse-btn:hover { color: #409eff; }
.logo-text {
  font-size: 17px;
  font-weight: 700;
  background: linear-gradient(135deg, #409eff, #337ecc);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.header-icon {
  font-size: 18px;
  color: #606266;
  cursor: pointer;
  padding: 6px;
  border-radius: 6px;
  transition: all 0.2s;
}
.header-icon:hover {
  background: #f5f7fa;
  color: #409eff;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;
}
.user-info:hover { background: #f5f7fa; }
.user-avatar {
  background: linear-gradient(135deg, #409eff, #337ecc);
  color: #fff;
  font-weight: 600;
}
.user-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

/* 侧边栏 */
.aside {
  background: #fff;
  border-right: 1px solid #f0f0f0;
  transition: width 0.3s;
  overflow: hidden;
  height: calc(100vh - 56px);
  flex-shrink: 0;
}
.aside :deep(.el-aside) {
  overflow: hidden;
}
.sidebar-menu {
  border-right: none;
  padding: 8px 0;
}
.sidebar-menu :deep(.el-menu-item),
.sidebar-menu :deep(.el-sub-menu__title) {
  height: 42px;
  line-height: 42px;
  margin: 2px 8px;
  border-radius: 8px;
}
.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: #ecf5ff;
}
.sidebar-menu :deep(.el-menu-item.is-active) {
  background: #ecf5ff;
  color: #409eff;
}
.sidebar-menu :deep(.el-sub-menu .el-menu-item) {
  min-width: auto;
  padding-left: 52px !important;
}

/* 内容区 */
.main {
  background: #f5f7fa;
  padding: 16px 20px;
  overflow-y: auto;
  height: calc(100vh - 56px);
}
.breadcrumb {
  margin-bottom: 16px;
}
</style>
