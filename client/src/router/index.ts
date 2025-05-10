/* eslint-disable */
// @ts-ignore
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  // @ts-ignore
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/layout',
      name: 'layout',
      component: () => import('../Layouts/Layout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: 'home',
          name: 'home',
          component: () => import('../views/HomeView.vue'),
        },
        {
          path: 'team',
          name: 'team',
          component: () => import('../views/TeamView.vue'),
        },
        {
          path: 'messages',
          name: 'messages',
          component: () => import('../views/MessagesView.vue'),
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('../views/ProfileView.vue'),
        },
        {
          path: 'profile/edit',
          name: 'profileEdit',
          component: () => import('../views/ProfileEditView.vue'),
        },
        {
          path: 'search',
          name: 'search',
          component: () => import('../views/SearchView.vue'),
        },
      ]
    },
    {
      path: '/team/:id',
      name: 'teamroom',
      component: () => import('../views/ChatRoomView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/chat/:id',
      name: 'chatroom',
      component: () => import('../views/ChatRoomView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../Layouts/Login.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../Layouts/Register.vue'),
    },
    {
      path: '/search-results',
      name: 'searchResults',
      component: () => import('../views/SearchResultsView.vue'),
      meta: { requiresAuth: true },
    },
  ],
})

// 導航守衞
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  // 同步檢查用户登錄狀態
  const isLoggedIn = userStore.checkLoginStatus()
  
  // 如果頁面需要登錄
  if (to.meta.requiresAuth) {
    console.log('路由守衞 - 該頁面需要登錄權限')
    
    // 如果用户已登錄
    if (isLoggedIn) {
      console.log('路由守衞 - 用户已登錄，允許訪問')
      next()
      return
    }
    
    // 未登錄，跳轉到登錄頁
    console.warn('路由守衞 - 用户未登錄，跳轉登錄頁')
    next('/login')
    return
  }
  
  // 不需要驗證的頁面直接通過
  console.log('路由守衞 - 頁面不需要登錄權限，允許訪問')
  next()
})

export default router
