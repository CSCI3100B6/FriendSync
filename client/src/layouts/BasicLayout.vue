<script setup>
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Toast } from "vant";
import Index from "../pages/Index.vue";
import Team from "../pages/Team.vue";
import Profile from "../pages/Profile.vue";

const router = useRouter();
const route = useRoute();
const active = ref(route.path.replace("/", "") || "index");

const onClickLeft = () => {
  // 处理返回按钮点击事件
  router.back();
  alert("返回按钮点击");
};

const onClickRight = () => {
  // 处理右侧按钮点击事件
  alert("右侧按钮点击");
};

// const active = ref("index"); // 默认选中的标签索引
const getPageTitle = () => {
  switch (active.value) {
    case "profile":
      return "User Profile";
    case "team":
      return "队伍";
    case "index":
    default:
      return "FriendSync";
  }
};

const onChange = (index) => {
  // 标签切换时触发，显示切换的标签索引
  // Toast(`标签 ${index + 1}`);
  router.push(`/${name}`);
  // active.value = name;
};
</script>

<template>
  <div class="basic-layout">
    <van-nav-bar
      :title="getPageTitle()"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
    >
      <template v-if="active !== 'profile'" #right>
        <van-icon name="search" size="20" />
      </template>
    </van-nav-bar>

    <div class="content">
      <template v-if="active === 'index'">
        <Index />
      </template>
      <template v-if="active === 'team'">
        <Team />
      </template>
      <template v-if="active === 'profile'">
        <Profile />
      </template>
      <!-- <slot name="content"> </slot>
      插槽用于插入页面内容  -->
    </div>

    <van-tabbar v-model="active" @change="onChange">
      <van-tabbar-item icon="home-o" name="index">主页</van-tabbar-item>
      <van-tabbar-item icon="search" name="team">队伍</van-tabbar-item>
      <van-tabbar-item icon="friends-o" name="profile">个人</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style scoped>
van-nav-bar {
  height: 46px;
  line-height: 46px;
  font-size: 16px;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.content {
  padding-top: 46px;
  flex-grow: 1;
  overflow-y: auto;
}

.basic-layout {
  height: 46px; /* 固定高度 */
  line-height: 46px; /* 保持文字居中 */
  font-size: 16px; /* 避免字体继承导致变大 */
  position: fixed; /* 固定在顶部 */
  top: 0;
  left: 0;
  width: 100%;
  z-index: 999; /* 确保不被其他内容遮挡 */
  background-color: white; /* 避免透明影响 */
}
</style>
