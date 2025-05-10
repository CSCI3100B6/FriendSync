<template>
  <div class="register-container">
    <van-nav-bar
      title="註冊賬號"
      left-text="返回"
      left-arrow
      @click-left="goBack"
      :border="false"
    />

    <div class="header-section">
      <div class="logo">
        <img src="../assets/user-icon.svg" alt="Logo" />
      </div>
      <h2 class="title">創建你的賬號</h2>
      <p class="subtitle">加入FriendSync, 開啓你的社交之旅</p>
    </div>

    <van-form @submit="onSubmit" class="register-form">
      <van-cell-group inset>
        <van-field
          v-model="userAccount"
          name="userAccount"
          label="用户名"
          placeholder="請輸入用户名"
          :rules="[{ required: true, message: '請填寫用户名' }]"
          left-icon="user-o"
        />
        <van-field
          v-model="userPassword"
          type="password"
          name="userPassword"
          label="密碼"
          placeholder="請輸入密碼"
          :rules="[{ required: true, message: '請填寫密碼' }]"
          left-icon="lock"
        />
        <van-field
          v-model="checkPassword"
          type="password"
          name="checkPassword"
          label="確認密碼"
          placeholder="請再次輸入密碼"
          :rules="[
            { required: true, message: '請確認密碼' },
            {
              validator: validatePasswordMatch,
              message: '兩次輸入的密碼不一致',
            },
          ]"
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
          立即註冊
        </van-button>
      </div>

      <div class="login-link">
        <span>已有賬號? </span>
        <a @click="goToLogin">登錄</a>
      </div>
    </van-form>

    <div class="footer-info">
      <p>
        註冊即表示同意我們的<a @click="showTerms">服務條款</a>和<a
          @click="showPrivacy"
          >隱私政策</a
        >
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref } from "vue";
// @ts-ignore
import { Toast, Dialog } from "vant";
// @ts-ignore
import { useRouter } from "vue-router";
// @ts-ignore
import http from "@/api/http";

const router = useRouter();
const userAccount = ref("");
const userPassword = ref("");
const checkPassword = ref("");
const loading = ref(false);

// 驗證兩次密碼是否一致
const validatePasswordMatch = () => {
  return userPassword.value === checkPassword.value;
};

// 表單提交
const onSubmit = async () => {
  if (loading.value) return;

  try {
    loading.value = true;

    // 構建註冊請求數據
    const registerData = {
      userAccount: userAccount.value,
      userPassword: userPassword.value,
      checkPassword: checkPassword.value,
    };

    // 調用註冊API
    const response = await http.post("/user/register", registerData);

    if (response) {
      Toast.success({
        message: "註冊成功",
        position: "bottom",
      });

      // 註冊成功後跳轉到登錄頁
      setTimeout(() => {
        router.push("/login");
      }, 1500);
    }
  } catch (error: any) {
    console.error("註冊失敗:", error);
    Toast.fail(error.response?.data?.message || "註冊失敗，請稍後重試");
  } finally {
    loading.value = false;
  }
};

// 返回上一頁
const goBack = () => {
  router.back();
};

// 跳轉到登錄頁
const goToLogin = () => {
  router.push("/login");
};

// 顯示服務條款
const showTerms = () => {
  Dialog.confirm({
    title: "服務條款",
    message: "這裏是FriendSync的服務條款內容...",
    confirmButtonText: "我已閲讀",
    confirmButtonColor: "#1989fa",
  });
};

// 顯示隱私政策
const showPrivacy = () => {
  Dialog.confirm({
    title: "隱私政策",
    message: "這裏是FriendSync的隱私政策內容...",
    confirmButtonText: "我已閲讀",
    confirmButtonColor: "#1989fa",
  });
};
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
}

:deep(.van-nav-bar) {
  background-color: #ffffff;
  height: 56px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

:deep(.van-nav-bar__title) {
  color: #323233;
  font-size: 18px;
  font-weight: 600;
}

:deep(.van-nav-bar__text) {
  color: #1989fa;
  font-size: 15px;
}

:deep(.van-icon-arrow-left) {
  color: #1989fa !important;
}

.header-section {
  text-align: center;
  padding: 20px 0 30px;
  margin-bottom: 10px;
}

.logo {
  width: 70px;
  height: 70px;
  margin: 0 auto 16px;
  background-color: #1989fa;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo img {
  width: 40px;
  height: 40px;
  filter: brightness(0) invert(1);
}

.title {
  font-size: 24px;
  color: #323233;
  margin: 0 0 8px;
  font-weight: 600;
}

.subtitle {
  font-size: 14px;
  color: #969799;
  margin: 0;
  line-height: 1.4;
}

.register-form {
  margin-top: 10px;
  padding: 0 16px;
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
  width: 80px;
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
  margin: 24px 0 16px;
}

.submit-btn {
  height: 44px;
  font-size: 16px;
  background-color: #1989fa;
  border: none;
  font-weight: 500;
}

.login-link {
  text-align: center;
  font-size: 14px;
  margin-top: 16px;
  color: #646566;
}

.login-link a {
  color: #1989fa;
  font-weight: 500;
}

.footer-info {
  text-align: center;
  font-size: 12px;
  color: #969799;
  margin-top: auto;
  padding: 20px 16px;
  border-top: 1px solid #f2f3f5;
}

.footer-info a {
  color: #1989fa;
  text-decoration: none;
}
</style>
