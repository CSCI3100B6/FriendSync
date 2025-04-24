<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { showFailToast, showSuccessToast, showLoadingToast } from "vant";

const router = useRouter();

const formState = reactive({
  userAccount: "",
  userPassword: "",
});

const loading = ref(false);
const rules = {
  userAccount: [{ required: true, message: "Please enter your account" }],
  userPassword: [{ required: true, message: "Please enter your password" }],
};

const mockUsers = [
  {
    userAccount: "testuser",
    userPassword: "password123",
    id: 1,
    username: "Test User",
    avatarUrl: "https://via.placeholder.com/100",
    gender: 1,
    phone: "13812345678",
    email: "test@example.com",
    userStatus: 0,
    createTime: "2025-02-20T14:30:00.000+00:00",
    updateTime: "2025-03-22T09:15:00.000+00:00",
    userRole: 0,
  },
  {
    userAccount: "admin",
    userPassword: "admin123",
    id: 2,
    username: "Admin User",
    avatarUrl: "https://via.placeholder.com/100?text=Admin",
    gender: 0,
    phone: "13987654321",
    email: "admin@example.com",
    userStatus: 0,
    createTime: "2025-01-15T10:20:00.000+00:00",
    updateTime: "2025-03-20T15:30:00.000+00:00",
    userRole: 1,
  },
];

// const onSubmit = async (values: any) => {
//   loading.value = true;
//   showLoadingToast({
//     message: "Loading...",
//     forbidClick: true,
//   });

onMounted(() => {
  const inputFields = document.querySelectorAll(".van-field__control");

  inputFields.forEach((input) => {
    input.addEventListener("focus", () => {
      // Add null check before accessing classList
      const parent = input.closest(".van-field");
      if (parent) {
        parent.classList.add("van-field--focused");
      }
    });

    input.addEventListener("blur", () => {
      // Add null check here too
      const parent = input.closest(".van-field");
      if (parent) {
        parent.classList.remove("van-field--focused");
      }
    });
  });
});

const onSubmit = async (values: any) => {
  // Add explicit validation check for empty fields
  if (!values.userAccount || !values.userPassword) {
    showFailToast(
      !values.userAccount
        ? "Please enter your account"
        : "Please enter your password"
    );
    return; // Stop execution if validation fails
  }

  loading.value = true;

  showLoadingToast({
    message: "Logging in...",
    forbidClick: true,
    duration: 0,
  });

  await new Promise((resolve) => setTimeout(resolve, 1000));

  try {
    // Make API request to backend
    // const response = await fetch("/api/user/login", {
    //   method: "POST",
    //   headers: {
    //     "Content-Type": "application/json",
    //   },
    //   body: JSON.stringify(values),
    // });

    // const result = await response.json();

    const registerdUsers = JSON.parse(
      localStorage.getItem("mockUsers") || "[]"
    );

    const allUsers = [...mockUsers, ...registerdUsers];
    const user = allUsers.find(
      (user) =>
        user.userAccount === values.userAccount &&
        user.userPassword === values.userPassword
    );

    if (user) {
      // Login successful
      const userData = { ...user };
      delete userData.userPassword;

      localStorage.setItem("userInfo", JSON.stringify(userData));
      showSuccessToast({
        message: "Login successful",
        duration: 1500,
      });
      router.push("/");
    } else {
      // Login failed
      showFailToast("Login failed, please check your account and password");
    }
  } catch (error) {
    showFailToast("Login failed");
    console.error("Login error:", error);
  } finally {
    loading.value = false;
  }
};

//     if (result.code === 0 && result.data) {
//       // Login successful
//       localStorage.setItem("userInfo", JSON.stringify(result.data));
//       showSuccessToast("登录成功");
//       router.push("/");
//     } else {
//       // Login failed
//       showFailToast(result.message || "登录失败，请检查账号和密码");
//     }
//   } catch (error) {
//     showFailToast("登录失败，请稍后再试");
//     console.error("Login error:", error);
//   } finally {
//     loading.value = false;
//   }
// };
</script>

<template>
  <div class="login-container">
    <div class="logo-box">
      <font-awesome-icon icon="user-friends" class="fa-icon" />
      <h1 class="app-name">FriendSync</h1>
      <p class="app-slogan">Connecting to your social world!</p>
    </div>

    <van-form @submit="onSubmit" class="login-form">
      <van-cell-group inset>
        <van-field
          v-model="formState.userAccount"
          name="userAccount"
          placeholder="UserAccount"
          :rules="rules.userAccount"
        />
        <van-field
          v-model="formState.userPassword"
          type="password"
          name="userPassword"
          placeholder="Password"
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
          class="gradient-button"
        >
          Link start
        </van-button>
      </div>

      <div class="register-link">
        Not a user yet？
        <a @click="router.push('/register')">Go register!</a>
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
  text-decoration: underline;
  cursor: pointer;
}

:deep(.gradient-button) {
  background: linear-gradient(
    135deg,
    #42a6ff 0%,
    #0075e6 50%,
    #005bb3 100%
  ) !important;
  border: none !important;
  color: white !important;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 117, 230, 0.2);
  transition: transform 0.2s, box-shadow 0.2s;
}

:deep(.gradient-button:active),
:deep(.gradient-button:hover) {
  background: linear-gradient(
    135deg,
    #3b95e6 0%,
    #0069cf 50%,
    #00509e 100%
  ) !important;
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 117, 230, 0.2);
}

:deep(.gradient-button .van-button__loading) {
  color: white !important;
}

:deep(.van-button--primary) {
  background: #1989fa;
}

.fa-icon {
  font-size: 48px;
  color: #1989fa;
  margin-bottom: 16px;
}

:deep(.van-field) {
  border-radius: 4px;
  transition: all 0.3s ease;
}

:deep(.van-field--focused) {
  border: 2px solid #1989fa;
  box-shadow: 0 0 0 2px rgba(25, 137, 250, 0.1);
}

:deep(.van-cell) {
  padding: 12px 16px;
  margin-bottom: 8px;
  background-color: #fff;
}

:deep(.van-field__control) {
  font-size: 16px;
}

/* Make the input fields have a light border by default */
:deep(.van-cell-group--inset .van-field) {
  border: 1px solid #ebedf0;
}
</style>
