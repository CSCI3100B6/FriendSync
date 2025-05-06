<template>
  <div class="basic-layout">
    <van-nav-bar title="FriendSync" left-arrow @click-left="onClickLeft">
      <template #right>
        <van-icon name="search" size="20" @click="onClickRight" />
      </template>
    </van-nav-bar>

    <div class="content">
      <RouterView />
    </div>

    <!-- 底部导航栏（路由模式） -->
    <van-tabbar route>
      <van-tabbar-item replace to="/" icon="home-o">主页</van-tabbar-item>
      <van-tabbar-item replace to="/team" icon="search">队伍</van-tabbar-item>
      <van-tabbar-item replace to="/user" icon="friends-o">个人</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { Toast } from 'vant';
import {useRouter} from "vue-router";

const router = useRouter()
const onClickLeft = () => {
  router.push('/')
}
const onClickRight = () => {
  router.push('/search')
}
</script>

<style scoped>

.van-nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.content {
  padding-top: 46px;
  padding-bottom: 50px;
}

.basic-layout {
  min-height: 100vh;
}

</style>