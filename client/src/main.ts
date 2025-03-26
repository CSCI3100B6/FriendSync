import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import { Button, NavBar, Icon, Tabbar, TabbarItem, Toast } from 'vant'
import { createWebHistory, createRouter } from 'vue-router' // 修改导入
import routes from "./config/route.ts"
import { Divider } from 'vant';
import { Tag } from 'vant';
import { Collapse, CollapseItem } from 'vant';

const app = createApp(App)
// 先注册 UI 组件
app.use(Button)
app.use(NavBar)
app.use(Icon)
app.use(Tabbar)
app.use(TabbarItem)
app.use(Toast)
app.use(Divider);
app.use(Tag);
app.use(Collapse);
app.use(CollapseItem);

// 最后注册路由
const router = createRouter({
    history: createWebHistory(), // 使用 WebHistory
    routes,
})
app.use(router)

app.mount('#app')