import { createRouter, createWebHistory } from "vue-router";
import Index from "../pages/Index.vue";
import Team from "../pages/Team.vue";
import Login from "../pages/Login.vue";
import Register from "../pages/Register.vue";
import Profile from "../pages/Profile.vue";

const routes = [
  {
    path: "/",
    component: Index,
  },
  {
    path: "/team",
    component: Team,
  },
  {
    path: "/login",
    component: Login,
    meta: {
      layout: "empty", // No navbar/tabbar for login page
    },
  },
  {
    path: "/register",
    component: Register,
    meta: {
      layout: "empty", // No navbar/tabbar for register page
    },
  },
  {
    path: "/profile",
    component: Profile,
    meta: {
      requiresAuth: false, // User must be logged in to access this page
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigation guard to check login status
router.beforeEach((to, _, next) => {
  const userInfo = localStorage.getItem("userInfo");

  // If route requires auth and user is not logged in
  if (to.meta.requiresAuth && !userInfo) {
    next("/login");
  } else {
    next();
  }
});

export default router;
