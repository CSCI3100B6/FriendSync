<template>
  <div class="login-container">
    <div class="login-card">
      <div class="logo-section">
        <div class="logo">
          <img src="../assets/user-icon.svg" alt="Logo" />
        </div>
        <h1 class="title">FriendSync</h1>
        <p class="subtitle">連接你的社交世界</p>
      </div>

      <van-form @submit="handleLogin" class="login-form">
        <van-cell-group inset>
          <van-field
            v-model="userAccount"
            name="用户名"
            label="賬號"
            placeholder="請輸入用户名"
            :rules="[{ required: true, message: '請填寫用户名' }]"
            left-icon="user-o"
          />
          <van-field
            v-model="userPassword"
            type="password"
            name="密碼"
            label="密碼"
            placeholder="請輸入密碼"
            :rules="[{ required: true, message: '請填寫密碼' }]"
            left-icon="lock"
          />
        </van-cell-group>

        <div class="form-actions">
          <van-button
            round
            block
            type="primary"
            native-type="submit"
            class="submit-btn"
            :loading="loading"
          >
            登錄
          </van-button>

          <div class="register-prompt">
            <span>還沒有賬號? </span>
            <a @click="goToRegister">立即註冊</a>
          </div>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref } from "vue";
// @ts-ignore
import { Toast } from "vant";
// @ts-ignore
import { useRouter } from "vue-router";
// @ts-ignore
import { login, getCurrentUser } from "@/api/user";
// @ts-ignore
import { useUserStore } from "@/stores/user";

// 定義接口
interface LoginResponse {
  id: number;
  username?: string | null;
  userAccount: string;
  avatarUrl?: string | null;
  profile?: string | null;
  email?: string | null;
  phone?: string | null;
  createTime: string;
  userRole?: number;
  tags?: string[];
  [key: string]: any;
}

interface UserResponse {
  id: number;
  username?: string | null;
  userAccount: string;
  avatarUrl?: string | null;
  profile?: string | null;
  email?: string | null;
  phone?: string | null;
  createTime: string;
  userRole?: number;
  tags?: string[];
  [key: string]: any;
}

const router = useRouter();
const userStore = useUserStore();
const userAccount = ref("");
const userPassword = ref("");
const loading = ref(false);

const handleLogin = async () => {
  if (!userAccount.value || !userPassword.value) {
    Toast.fail("請輸入賬號和密碼");
    return;
  }

  loading.value = true;
  try {
    // 調用登錄API
    const response = await login(userAccount.value, userPassword.value);
    console.log("登錄響應數據:", response);

    // 從響應中獲取用户數據
    let userData = null;

    // 嘗試從不同的數據結構中獲取用户數據
    if (response && typeof response === "object") {
      if (response.data && typeof response.data === "object") {
        userData = response.data;
      } else {
        // 有些情況下axios可能直接返回數據而不是標準的AxiosResponse
        userData = response;
      }
    }

    if (!userData || !("id" in userData)) {
      console.warn("登錄成功但未獲取到有效的用户數據");
      Toast.fail("登錄異常，請聯繫管理員");
      loading.value = false;
      return;
    }

    // 成功登錄後，存儲用户信息
    userStore.setUserInfo({
      id: userData.id,
      username: userData.userAccount,
      nickname: userData.username || userData.userAccount || "用户",
      avatar: userData.avatarUrl || "",
      bio: userData.profile || "",
      email: userData.email || "",
      phone: userData.phone || "",
      role: userData.userRole === 1 ? "admin" : "user",
      createTime: userData.createTime || new Date().toISOString(),
    });

    Toast.success({
      message: "登錄成功",
      position: "bottom",
    });

    // 登錄成功後跳轉到首頁
    router.replace("/layout/home");
  } catch (error: any) {
    console.error("登錄失敗", error);

    let errorMessage = "登錄失敗，請檢查賬號密碼";

    // 嘗試從錯誤響應中獲取具體錯誤信息
    if (error.response && error.response.data) {
      const errorData = error.response.data;
      if (errorData.message) {
        errorMessage = errorData.message;
      } else if (errorData.description) {
        errorMessage = errorData.description;
      }
    }

    Toast.fail({
      message: errorMessage,
      position: "bottom",
    });
  } finally {
    loading.value = false;
  }
};

const goToRegister = () => {
  // 跳轉到註冊頁面
  router.push("/register");
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #ffffff;
  position: relative;
}

.login-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 200px;
  background-color: #1989fa;
  z-index: 0;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 30px 20px;
  border-radius: 16px;
  background-color: #ffffff;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  z-index: 1;
  margin: 0 20px;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.logo {
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #1989fa;
  border-radius: 20px;
  margin-bottom: 16px;
}

.logo img {
  width: 46px;
  height: 46px;
  filter: brightness(0) invert(1);
}

.title {
  font-family: "SF Pro Display", -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 28px;
  font-weight: 700;
  color: #323233;
  margin: 10px 0 5px;
  text-align: center;
}

.subtitle {
  font-size: 16px;
  color: #969799;
  margin: 0;
  text-align: center;
}

.login-form {
  margin-top: 10px;
}

:deep(.van-cell) {
  padding: 16px;
  font-size: 15px;
}

:deep(.van-cell-group--inset) {
  margin: 0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(100, 101, 102, 0.08);
}

:deep(.van-field__label) {
  color: #323233;
  width: 60px;
  font-weight: 500;
}

:deep(.van-field__control) {
  color: #323233;
}

:deep(.van-field__left-icon) {
  margin-right: 8px;
  color: #1989fa;
}

.form-actions {
  margin: 24px 0 0;
}

.submit-btn {
  height: 44px;
  font-size: 16px;
  background-color: #1989fa;
  border: none;
  font-weight: 500;
}

.register-prompt {
  text-align: center;
  font-size: 14px;
  color: #646566;
  margin-top: 20px;
}

.register-prompt a {
  color: #1989fa;
  font-weight: 500;
}

@media (max-width: 480px) {
  .login-card {
    padding: 25px 16px;
  }

  .title {
    font-size: 24px;
  }

  .subtitle {
    font-size: 14px;
  }
}
</style>
