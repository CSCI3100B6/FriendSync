import Index from '../pages/Index.vue'
import Team from '../pages/Team.vue'
import User from "../pages/User.vue";
import Search from "../pages/Search.vue";
import UserEditPage from "../pages/UserEditPage.vue";

// 定义一些路由，负责项目的全局页面跳转规则
const routes = [
    { path: '/', component: Index},
    { path: '/team', component: Team },
    { path: '/user', component: User },
    { path: '/search', component: Search },
    { path: '/user/edit/', component: UserEditPage },
]

export default routes;