<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { showFailToast, showSuccessToast, showLoadingToast } from "vant";

const router = useRouter();

const formState = reactive({
  userAccount: "",
  userPassword: "",
});

const loading = ref(false);
const rules = {
  userAccount: [{ required: true, message: "请输入账号" }],
  userPassword: [{ required: true, message: "请输入密码" }],
};

const onSubmit = async (values: any) => {
  loading.value = true;
  showLoadingToast({
    message: "登录中...",
    forbidClick: true,
  });

  try {
    // Make API request to backend
    const response = await fetch("/api/user/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });

    const result = await response.json();

    if (result.code === 0 && result.data) {
      // Login successful
      localStorage.setItem("userInfo", JSON.stringify(result.data));
      showSuccessToast("登录成功");
      router.push("/");
    } else {
      // Login failed
      showFailToast(result.message || "登录失败，请检查账号和密码");
    }
  } catch (error) {
    showFailToast("登录失败，请稍后再试");
    console.error("Login error:", error);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="login-container">
    <div class="logo-box">
      <h1 class="app-name">FriendSync</h1>
      <p class="app-slogan">连接你的社交世界</p>
    </div>

    <van-form @submit="onSubmit" class="login-form">
      <van-cell-group inset>
        <van-field
          v-model="formState.userAccount"
          name="userAccount"
          label="账号"
          placeholder="请输入账号"
          :rules="rules.userAccount"
        />
        <van-field
          v-model="formState.userPassword"
          type="password"
          name="userPassword"
          label="密码"
          placeholder="请输入密码"
          :rules="rules.userPassword"
        />
      </van-cell-group>

      <div style="margin: 16px">
        <van-button
          round
          block
          type="primary"
          native-type="submit"
          :loading="loading"
        >
          登录
        </van-button>
      </div>

      <div class="register-link">
        还没有账号？
        <a @click="router.push('/register')">立即注册</a>
      </div>
    </van-form>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  min-height: 100vh;
  background-color: #f7f8fa;
  width: 100vw;
  box-sizing: border-box;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: 0;
}

.logo-box {
  margin-bottom: 40px;
  text-align: center;
}

.app-name {
  font-size: 28px;
  font-weight: bold;
  color: #1989fa;
  margin-bottom: 8px;
}

.app-slogan {
  font-size: 14px;
  color: #969799;
}

.login-form {
  width: 100%;
  max-width: 400px;
}

.register-link {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #969799;
}

.register-link a {
  color: #1989fa;
  text-decoration: none;
  cursor: pointer;
}
</style>
