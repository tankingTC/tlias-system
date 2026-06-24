<!--
  登录页面
  功能：用户身份认证，输入用户名和密码进行登录
  布局：背景图层 -> 半透明遮罩 -> 微粒子动画 -> 玻璃拟态登录卡片
  视觉特效：背景缩放动画、旋转渐变边框、微粒子漂浮、玻璃拟态效果
-->
<template>
  <div class="login-container">
    <!-- 背景图层：带缩放动画的背景图片 -->
    <div class="bg-layer" :style="{ backgroundImage: `url(${bgUrl})` }"></div>
    <!-- 半透明遮罩层：增加文字可读性 -->
    <div class="bg-overlay"></div>
    <!-- 微粒子动画层：20个随机漂浮的光点 -->
    <div class="particles">
      <span v-for="n in 20" :key="n" :style="particleStyle(n)"></span>
    </div>

    <!-- 玻璃拟态登录卡片 -->
    <div class="login-card-wrapper">
      <!-- 旋转渐变边框 -->
      <div class="login-card-border"></div>
      <div class="login-card">
        <!-- 顶部高光线条 -->
        <div class="card-inner-glow"></div>

        <!-- 登录头部：Logo + 系统名称 -->
        <div class="login-header">
          <div class="logo-icon">
            <!-- SVG Logo图标 -->
            <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M24 4L4 14v20l20 10 20-10V14L24 4z" fill="url(#lg)" opacity="0.9" />
              <path d="M24 16l-8 4v8l8 4 8-4v-8l-8-4z" fill="rgba(255,255,255,0.5)" />
              <defs>
                <linearGradient id="lg" x1="4" y1="4" x2="44" y2="44">
                  <stop stop-color="#fff" stop-opacity="0.9" />
                  <stop offset="1" stop-color="#b8d4ff" />
                </linearGradient>
              </defs>
            </svg>
          </div>
          <h2 class="login-title">智学云帆</h2>
          <p class="login-subtitle">教学管理系统</p>
        </div>

        <!-- 登录表单 -->
        <el-form :model="loginForm" :rules="rules" ref="formRef" class="login-form" @submit.prevent="handleLogin">
          <el-form-item prop="username" class="form-item">
            <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" size="large" />
          </el-form-item>
          <el-form-item prop="password" class="form-item">
            <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock"
              size="large" show-password @keyup.enter="handleLogin" />
          </el-form-item>
          <el-form-item class="form-item">
            <el-button class="login-btn" size="large" :loading="loading" @click="handleLogin">
              登 录
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 卡片底部版权信息 -->
        <div class="card-footer">
          <span>智学云帆教学管理系统</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 登录页面逻辑
 * 处理用户登录认证，成功后存储Token并跳转到首页
 */
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import bgImage from '../assets/images/background.png'

// 背景图片URL
const bgUrl = bgImage
// 路由实例
const router = useRouter()
// 表单引用
const formRef = ref(null)
// 登录按钮加载状态
const loading = ref(false)

// 登录表单数据（预填默认账号）
const loginForm = reactive({ username: 'admin', password: '123456' })

// 表单验证规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

/**
 * 生成微粒子的随机样式
 * @param {number} n - 粒子编号
 * @returns {Object} CSS内联样式对象
 */
const particleStyle = (n) => {
  const size = Math.random() * 3 + 1
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${Math.random() * 100}%`,
    top: `${Math.random() * 100}%`,
    animationDelay: `${Math.random() * 8}s`,
    animationDuration: `${Math.random() * 15 + 12}s`,
  }
}

/**
 * 处理登录操作
 * 验证表单 -> 调用登录接口 -> 存储Token -> 跳转首页
 */
const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await request({ url: '/login', method: 'post', data: loginForm })
    // 将Token存储到localStorage
    localStorage.setItem('token', res.data.token)
    ElMessage.success('登录成功')
    // 跳转到首页
    router.push('/')
  } catch (e) { /* 错误由拦截器统一处理 */ }
  finally { loading.value = false }
}
</script>

<style scoped>
/* ==================== 背景 ==================== */
.login-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  overflow: hidden;
}

/* 背景图层：带缓慢缩放动画 */
.bg-layer {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  animation: bgZoom 30s ease-in-out infinite alternate;
}

@keyframes bgZoom {
  0%   { transform: scale(1); }
  100% { transform: scale(1.05); }
}

/* 半透明遮罩层 */
.bg-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(10, 30, 60, 0.20) 0%,
    rgba(10, 30, 60, 0.10) 40%,
    rgba(10, 30, 60, 0.25) 100%
  );
}

/* ==================== 微粒子 ==================== */
.particles {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 1;
}
.particles span {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  animation: float linear infinite;
  pointer-events: none;
}
/* 微粒子漂浮动画 */
@keyframes float {
  0%, 100% { transform: translateY(0) translateX(0); opacity: 0; }
  10% { opacity: 0.6; }
  50% { transform: translateY(-60px) translateX(12px); opacity: 0.8; }
  90% { opacity: 0.6; }
}

/* ==================== 炫彩渐变边框 ==================== */
.login-card-wrapper {
  position: relative;
  z-index: 2;
  padding: 2px;
  border-radius: 20px;
  animation: cardIn 0.8s cubic-bezier(0.16, 1, 0.3, 1) both;
}

/* 旋转渐变边框：使用mask实现只显示边框效果 */
.login-card-border {
  position: absolute;
  inset: 0;
  border-radius: 20px;
  background: conic-gradient(
    from 0deg,
    rgba(255,255,255,0.7),
    rgba(160,200,255,0.6),
    rgba(255,255,255,0.4),
    rgba(180,220,255,0.6),
    rgba(255,255,255,0.7)
  );
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  animation: rotateBorder 6s linear infinite;
}

/* 边框色相旋转动画 */
@keyframes rotateBorder {
  from { filter: hue-rotate(0deg); }
  to   { filter: hue-rotate(360deg); }
}

/* 卡片入场动画 */
@keyframes cardIn {
  from { opacity: 0; transform: translateY(30px) scale(0.97); }
  to   { opacity: 1; transform: translateY(0) scale(1); }
}

/* ==================== 玻璃拟态卡片 ==================== */
.login-card {
  position: relative;
  width: 420px;
  padding: 48px 40px 32px;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border-radius: 18px;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(255, 255, 255, 0.3) inset,
    0 1px 0 rgba(255, 255, 255, 0.4) inset;
  overflow: hidden;
}

/* 顶部高光线条 */
.card-inner-glow {
  position: absolute;
  top: 0;
  left: 15%;
  right: 15%;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.5), transparent);
}

/* ==================== 头部 ==================== */
.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.logo-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 18px;
}
.logo-icon svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 2px 8px rgba(0, 50, 120, 0.2));
}

.login-title {
  margin: 0 0 8px;
  font-size: 26px;
  font-weight: 600;
  color: #1a3a5c;
  letter-spacing: 2px;
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.5);
}

.login-subtitle {
  margin: 0;
  font-size: 13px;
  color: rgba(30, 60, 100, 0.5);
  letter-spacing: 6px;
}

/* ==================== 表单 ==================== */
.login-form {
  margin-bottom: 24px;
}

.form-item {
  margin-bottom: 20px;
}

/* 覆盖Element Plus输入框样式，实现玻璃拟态效果 */
:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.45) !important;
  border: 1px solid rgba(255, 255, 255, 0.5) !important;
  box-shadow: 0 2px 8px rgba(0, 50, 120, 0.06) !important;
  border-radius: 10px !important;
  height: 46px !important;
  transition: all 0.3s ease !important;
}
:deep(.el-input__wrapper:hover) {
  border-color: rgba(100, 160, 220, 0.5) !important;
  background: rgba(255, 255, 255, 0.55) !important;
}
:deep(.el-input__wrapper.is-focus) {
  border-color: rgba(60, 130, 200, 0.6) !important;
  background: rgba(255, 255, 255, 0.6) !important;
  box-shadow: 0 0 0 3px rgba(60, 130, 200, 0.1) !important;
}
:deep(.el-input__inner) {
  color: #1a3a5c !important;
  font-size: 14px !important;
}
:deep(.el-input__inner::placeholder) {
  color: rgba(30, 60, 100, 0.4) !important;
}
:deep(.el-input__prefix-inner) {
  color: rgba(30, 60, 100, 0.4) !important;
}
:deep(.el-input__suffix-inner) {
  color: rgba(30, 60, 100, 0.4) !important;
}

/* 登录按钮：渐变背景 + 悬浮阴影效果 */
.login-btn {
  width: 100% !important;
  height: 46px !important;
  border-radius: 10px !important;
  font-size: 15px !important;
  font-weight: 500 !important;
  letter-spacing: 3px !important;
  border: none !important;
  background: linear-gradient(135deg, #3b82f6, #2563eb) !important;
  color: #fff !important;
  transition: all 0.3s ease !important;
  box-shadow: 0 4px 16px rgba(37, 99, 235, 0.3) !important;
}
.login-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 24px rgba(37, 99, 235, 0.4) !important;
  background: linear-gradient(135deg, #4a90f7, #3578f0) !important;
}
.login-btn:active {
  transform: translateY(0);
}

/* ==================== 底部 ==================== */
.card-footer {
  text-align: center;
}
.card-footer span {
  font-size: 12px;
  color: rgba(30, 60, 100, 0.35);
}

/* ==================== 响应式 ==================== */
@media (max-width: 520px) {
  .login-card { width: 90vw; padding: 36px 28px 24px; }
  .login-title { font-size: 22px; }
}
</style>
