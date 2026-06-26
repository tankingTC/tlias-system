/**
 * 应用入口文件
 * 负责创建Vue应用实例并注册全局插件（Element Plus UI库、路由等）
 */
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/global.css'
// 引入Element Plus中文语言包
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
// 引入Element Plus所有图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

// 创建Vue应用实例
const app = createApp(App)

// 全局注册所有Element Plus图标组件，可在模板中直接使用
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 按顺序注册插件：路由 -> UI框架（配置中文语言）
app.use(router)
app.use(ElementPlus, { locale: zhCn })
// 挂载应用到#app根节点
app.mount('#app')
