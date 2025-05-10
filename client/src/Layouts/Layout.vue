<template>
  <div class="layout-container">
    <!-- 頂部導航欄 -->
    <div class="app-header">
      <div class="header-left">
        <van-icon name="arrow-left" v-if="showBackButton" @click="goBack" />
      </div>
      <div class="header-title">FriendSync</div>
      <div class="header-right">
        <van-icon name="search" @click="onSearch" />
      </div>
    </div>

    <!-- 主要內容區域 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 底部導航欄 -->
    <van-tabbar v-model="activeTab" route fixed placeholder class="bottom-nav">
      <van-tabbar-item name="home" icon="home-o" to="/layout/home"
        >首頁</van-tabbar-item
      >
      <van-tabbar-item name="team" icon="friends-o" to="/layout/team"
        >團隊</van-tabbar-item
      >
      <van-tabbar-item name="messages" icon="chat-o" to="/layout/messages"
        >消息</van-tabbar-item
      >
      <van-tabbar-item name="profile" icon="manager-o" to="/layout/profile"
        >個人</van-tabbar-item
      >
    </van-tabbar>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, computed } from "vue";
// @ts-ignore
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();
const activeTab = ref("home");

// 判斷是否顯示返回按鈕（除了主頁和底部導航對應的頁面外都顯示）
const showBackButton = computed(() => {
  const noBackRoutes = [
    "/layout/home",
    "/layout/team",
    "/layout/messages",
    "/layout/profile",
  ];
  return !noBackRoutes.includes(route.path);
});

// 返回上一頁
function goBack() {
  router.back();
}

// 搜索功能
function onSearch() {
  router.push("/layout/search");
}
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
}

/* 頂部導航欄 */
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
  padding: 0 16px;
  background-color: #ffffff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left,
.header-right {
  display: flex;
  align-items: center;
  width: 40px;
}

.header-left .van-icon,
.header-right .van-icon {
  font-size: 24px;
  color: #1976d2;
  cursor: pointer;
}

.header-title {
  flex: 1;
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  color: #1976d2;
}

/* 主要內容區域 */
.main-content {
  flex: 1;
  padding-bottom: 50px; /* 為底部導航騰出空間 */
  overflow-y: auto;
}

/* 底部導航欄 */
.bottom-nav {
  background-color: #ffffff;
  --van-tabbar-item-text-color: #646566;
  --van-tabbar-item-active-color: #1976d2;
  --van-tabbar-background-color: #ffffff;
  box-shadow: 0 -1px 4px rgba(0, 0, 0, 0.08);
}

:deep(.van-tabbar-item__icon) {
  font-size: 22px;
}

:deep(.van-tabbar-item__text) {
  font-size: 14px;
}

@media (max-width: 768px) {
  .header-title {
    font-size: 17px;
  }
}
</style>
