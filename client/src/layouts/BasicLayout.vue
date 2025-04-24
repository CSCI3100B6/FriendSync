<script setup>
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Toast, showFailToast, showDialog, closeDialog } from "vant";
import Index from "../pages/Index.vue";
import Friends from "../pages/Friends.vue";
import Profile from "../pages/Profile.vue";
import Message from "../pages/Message.vue";

const router = useRouter();
const route = useRoute();
const active = ref(route.path.replace("/", "") || "index");
const isBlurred = ref(false); // Track blur state

const onClickLeft = () => {
  // 处理返回按钮点击事件
  router.back();
  // alert("返回按钮点击");
};

const onClickRight = () => {
  // 处理右侧按钮点击事件
  alert("右侧按钮点击");
};

const isAuthenticated = () => {
  return localStorage.getItem("userInfo") !== null;
};

// const active = ref("index"); // 默认选中的标签索引
const getPageTitle = () => {
  switch (active.value) {
    case "profile":
      return "User Profile";
    case "message":
      return "Message";
    case "friends":
      return "Friends";
    case "index":
    default:
      return "FriendSync";
  }
};

const showLoginPrompt = () => {
  isBlurred.value = true; // Apply blur effect

  showDialog({
    title: "Login Required",
    message: "You need to login first to access this feature.",
    className: "login-prompt-dialog",
    showCancelButton: false, // Hide standard cancel button
    confirmButtonText: "Go to Login",
    beforeClose: (action) => {
      isBlurred.value = false;

      // Then navigate if needed
      if (action === "confirm") {
        router.push("/login");
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
      closeButton.innerHTML = "×"; // Use proper × character instead of x
      closeButton.addEventListener("click", () => {
        isBlurred.value = false;
        closeDialog();
      });

      // Insert the X button as the first child of the header
      dialogHeader.appendChild(closeButton);
    }
  }, 10);
};

const onChange = (index) => {
  const requiresAuth = ["friends", "message", "profile"].includes(index);
  // // 标签切换时触发，显示切换的标签索引
  // // Toast(`标签 ${index + 1}`);
  // if ((index === "team" || index === "profile") && !isAuthenticated()) {
  //   // showFailToast("Please login first");
  //   // router.push("/login");
  //   showLoginPrompt();
  //   return;
  // }

  if (requiresAuth && !isAuthenticated()) {
    showLoginPrompt();

    active.value = route.path.replace("/", "") || "index";
    return;
  }

  router.push(`/${index}`);
  // active.value = name;
};
</script>

<template>
  <div class="basic-layout" :class="{ 'blur-background': isBlurred }">
    <van-nav-bar
      :title="getPageTitle()"
      :left-arrow="active !== 'index'"
      @click-left="onClickLeft"
      @click-right="onClickRight"
      class="page-nav-bar"
    >
      <template v-if="isAuthenticated() && active !== 'profile'" #right>
        <van-icon name="search" size="20" />
      </template>
    </van-nav-bar>

    <div class="content" :class="{ 'blur-background': isBlurred }">
      <template v-if="active === 'index'">
        <Index />
      </template>
      <template v-if="active === 'friends'">
        <Friends />
      </template>
      <template v-if="active === 'message'">
        <Message />
      </template>
      <template v-if="active === 'profile'">
        <Profile />
      </template>
      <!-- <slot name="content"> </slot>
      插槽用于插入页面内容  -->
    </div>

    <van-tabbar v-model="active" @change="onChange">
      <van-tabbar-item icon="home-o" name="index">Home</van-tabbar-item>
      <van-tabbar-item icon="friends-o" name="friends">Friends</van-tabbar-item>
      <van-tabbar-item icon="chat-o" name="message">Messages</van-tabbar-item>
      <van-tabbar-item icon="user-o" name="profile">Profile</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style scoped>
/* Fix navbar positioning */
.page-nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

/* Make content display properly */
.content {
  padding-top: 46px; /* Navbar height */
  padding-bottom: 50px; /* Tabbar height */
  min-height: 100vh;
  width: 100%;
}

/* Fix basic layout container */
.basic-layout {
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100%;
  background-color: white;
}

/* Properly position tabbar at bottom */
.van-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  pointer-events: auto;
}

:deep(.van-overlay) {
  z-index: 1500; /* Higher than tabbar but lower than dialog */
  pointer-events: auto;
}

:deep(.login-prompt-dialog) {
  z-index: 2000; /* Higher than overlay */
}

.blur-background {
  filter: blur(5px);
}

:deep(body.van-overflow-hidden) .van-tabbar {
  z-index: 999; /* Just below the overlay */
  /* This makes the tabbar visible through the overlay but non-interactive */
  pointer-events: none;
}
</style>
