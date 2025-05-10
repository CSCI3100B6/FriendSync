<template>
    <van-cell title="昵称" is-link to="/user/edit" :value="user.username" />
    <van-cell title="账号" :value="user.userAccount" />
    <van-cell title="头像" is-link to="/user/edit" :value="user.avatarUrl" />
    <van-cell title="性别" is-link to="/user/edit" :value="user.gender" />
    <van-cell title="电话" is-link to="/user/edit" :value="user.phone" />
    <van-cell title="邮箱" is-link to="/user/edit" :value="user.email" />
    <van-cell title="编号" arrow-direction="down" :value="user.planetCode" />
    <van-cell title="注册时间" arrow-direction="down" :value="user.createTime" />
</template>

<script>
import BasicLayout from "../App.vue";

export default {
  name: "User",
  data() {
    return {
      user: {
        id: 1,
        username: "stevenpang",
        userAccount: "Stevenpang",
        avatarUrl: "asdasdasdad",
        gender: "男",
        phone: "12312341234",
        email: "1234@qq.com",
        planetCode: "1234",
        createTime: new Date().toLocaleString(), // 格式化日期
      },
    };
  },
};
</script>

<style scoped>
.van-cell {
  position: relative;
  left: 0;
  width: 100%;
  z-index: 1000;
  background: #fff;
}
.content {
  padding-top: 46px;
  padding-bottom: 50px;
}


</style>