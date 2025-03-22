<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { showFailToast, showSuccessToast, showLoadingToast } from "vant";

const router = useRouter();

const formState = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
});

const loading = ref(false);
const rules = {
  userAccount: [
    { required: true, message: "请输入账号" },
    { min: 4, message: "账号长度不能少于4个字符" },
  ],
  userPassword: [
    { required: true, message: "请输入密码" },
    { min: 8, message: "密码长度不能少于8个字符" },
  ],
  checkPassword: [
    { required: true, message: "请再次输入密码" },
    {
      validator: (val: string) => val === formState.userPassword,
      message: "两次输入的密码不一致",
    },
  ],
};

const onSubmit = async (values: any) => {
  loading.value = true;
  showLoadingToast({
    message: "注册中...",
    forbidClick: true,
  });

  try {
    // Make API request to backend
    const response = await fetch("/api/user/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    });

    const result = await response.json();

    if (result.code === 0 && result.data) {
      // Registration successful
      showSuccessToast("注册成功");
      // Redirect to login page
      router.push("/login");
    } else {
      // Registration failed
      showFailToast(result.message || "注册失败，请检查输入信息");
    }
  } catch (error) {
    showFailToast("注册失败，请稍后再试");
    console.error("Registration error:", error);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="register-container">
    <div class="logo-box">
      <h1 class="app-name">FriendSync</h1>
      <p class="app-slogan">创建你的账号</p>
    </div>

    <van-form @submit="onSubmit" class="register-form">
      <van-cell-group inset>
        <van-field
          v-model="formState.userAccount"
          name="userAccount"
          label="账号"
          placeholder="请输入账号（至少4个字符）"
          :rules="rules.userAccount"
        />
        <van-field
          v-model="formState.userPassword"
          type="password"
          name="userPassword"
          label="密码"
          placeholder="请输入密码（至少8个字符）"
          :rules="rules.userPassword"
        />
        <van-field
          v-model="formState.checkPassword"
          type="password"
          name="checkPassword"
          label="确认密码"
          placeholder="请再次输入密码"
          :rules="rules.checkPassword"
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
          注册
        </van-button>
      </div>

      <div class="login-link">
        已有账号？
        <a @click="router.push('/login')">立即登录</a>
      </div>
    </van-form>
  </div>
</template>

<style scoped>
.register-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  min-height: 100vh;
  background-color: #f7f8fa;
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

.register-form {
  width: 100%;
  max-width: 400px;
}

.login-link {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #969799;
}

.login-link a {
  color: #1989fa;
  text-decoration: none;
  cursor: pointer;
}
</style>
