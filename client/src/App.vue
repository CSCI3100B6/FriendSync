<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { RouterView } from 'vue-router'
// @ts-ignore
import { useUserStore } from '@/stores/user'

// 獲取用户存儲實例
const userStore = useUserStore()
</script>

<template>
  <RouterView />
</template>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, 
               Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 
               'Microsoft Yahei', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f7f8fa;
  color: #323233;
}

/* 全局滾動條樣式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-thumb {
  background-color: rgba(144, 147, 153, 0.3);
  border-radius: 10px;
}

::-webkit-scrollbar-track {
  background-color: transparent;
}

/* 全局過渡效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 安全區適配 */
.safe-area-inset-bottom {
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}

.text-primary {
  color: #1989fa;
}

.text-danger {
  color: #ee0a24;
}

.text-success {
  color: #07c160;
}

.text-warning {
  color: #ff976a;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
