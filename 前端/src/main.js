import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './assets/styles/global.scss'

const app = createApp(App)

// 注册Pinia
const pinia = createPinia()
app.use(pinia)

// 注册路由
app.use(router)

// 注册Element Plus
app.use(ElementPlus, {
  locale: zhCn,
  size: 'default'
})

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
