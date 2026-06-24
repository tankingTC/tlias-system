/**
 * Vite构建工具配置文件
 * 配置Vue插件、开发服务器端口、API代理转发规则
 */
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  // 注册Vue单文件组件插件
  plugins: [vue()],
  server: {
    // 开发服务器端口
    port: 5173,
    // API代理配置：将前缀为/api的请求转发到后端服务
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务地址
        changeOrigin: true, // 修改请求头中的Origin为目标地址
        rewrite: (path) => path.replace(/^\/api/, '') // 移除/api前缀，转发到后端根路径
      }
    }
  }
})
