<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";
import BasicLayout from "./layouts/BasicLayout.vue";

const route = useRoute();
const layoutType = computed(() => route.meta.layout || "default");
</script>

<template>
  <div v-if="layoutType === 'empty'">
    <router-view />
  </div>
  <div v-else>
    <BasicLayout />
  </div>
</template>

<style scoped>
van-nav-bar {
  width: 100%;
  box-sizing: border-box;
}

van-tabbar {
  width: 100%;
  box-sizing: border-box;
}
</style>
