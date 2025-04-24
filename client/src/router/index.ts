import { createRouter, createWebHistory } from "vue-router";
import Index from "../pages/Index.vue";
import Friends from "../pages/Friends.vue";
import Login from "../pages/Login.vue";
import Register from "../pages/Register.vue";
import Message from "../pages/Message.vue";
import Profile from "../pages/Profile.vue";
import { showDialog, closeDialog } from "vant";

const routes = [
  {
    path: "/",
    component: Index,
  },
  {
    path: "/friends",
    component: Friends,
    meta: {
      requiresAuth: true, // Add this to require authentication
    },
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
  { path: "/message", component: Message, meta: { requiresAuth: true } },
  {
    path: "/profile",
    component: Profile,
    meta: {
      requiresAuth: true, // User must be logged in to access this page
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

let isShowingAuthDialog = false;

const showLoginPrompt = (next) => {
  if (isShowingAuthDialog) return;
  isShowingAuthDialog = true;

  showDialog({
    title: "Login Required",
    message: "You need to login first to access this feature.",
    className: "login-prompt-dialog",
    showCancelButton: false,
    confirmButtonText: "Go to Login",
    beforeClose: (action) => {
      isShowingAuthDialog = false;

      // Then navigate as needed
      if (action === "confirm") {
        next("/login");
      } else {
        next("/");
      }
      return true;
    },
  });

  setTimeout(() => {
    // Find the dialog header
    const dialogHeader = document.querySelector(
      ".login-prompt-dialog .van-dialog__header"
    );
    if (dialogHeader) {
      // Create X button
      const closeButton = document.createElement("button");
      closeButton.className = "dialog-close-button";
      closeButton.innerHTML = "×";
      closeButton.addEventListener("click", () => {
        isShowingAuthDialog = false;
        closeDialog();
        next("/");
      });

      // Insert the X button as the first child of the header
      dialogHeader.appendChild(closeButton);
    }
  }, 10);
};

// Navigation guard to check login status
router.beforeEach((to, _, next) => {
  const userInfo = localStorage.getItem("userInfo");

  // If route requires auth and user is not logged in
  if (to.meta.requiresAuth && !userInfo) {
    showLoginPrompt(next);
  } else {
    next();
  }
});

export default router;
