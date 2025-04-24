<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
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
    { required: true, message: "Please enter your account" },
    { min: 4, message: "Account length must be at least 4 characters" },
  ],
  userPassword: [
    { required: true, message: "Please enter password" },
    { min: 8, message: "Password must be at least 8 characters" },
  ],
  checkPassword: [
    { required: true, message: "Please re-enter password" },
    {
      validator: (val: string) => val === formState.userPassword,
      message: "Passwords do not match",
    },
  ],
};

onMounted(() => {
  const inputFields = document.querySelectorAll(".van-field__control");

  inputFields.forEach((input) => {
    input.addEventListener("focus", () => {
      // Add class to parent field when focused
      const parent = input.closest(".van-field");
      if (parent) {
        parent.classList.add("van-field--focused");
      }
    });

    input.addEventListener("blur", () => {
      // Remove class from parent field when focus is lost
      const parent = input.closest(".van-field");
      if (parent) {
        parent.classList.remove("van-field--focused");
      }
    });
  });
});

const onSubmit = async (values: any) => {
  loading.value = true;
  showLoadingToast({
    message: "Registering...",
    forbidClick: true,
  });

  await new Promise((resolve) => setTimeout(resolve, 1000));

  //   try {
  //     // Make API request to backend
  //     const response = await fetch("/api/user/register", {
  //       method: "POST",
  //       headers: {
  //         "Content-Type": "application/json",
  //       },
  //       body: JSON.stringify(values),
  //     });

  //     const result = await response.json();

  //     if (result.code === 0 && result.data) {
  //       // Registration successful
  //       showSuccessToast("Registration successful");
  //       // Redirect to login page
  //       router.push("/login");
  //     } else {
  //       // Registration failed
  //       showFailToast(
  //         result.message || "Registration failed, please check your information"
  //       );
  //     }
  //   } catch (error) {
  //     showFailToast("Registration failed, please try again later");
  //     console.error("Registration error:", error);
  //   } finally {
  //     loading.value = false;
  //   }
  // };

  try {
    // MOCK REGISTRATION INSTEAD OF API CALL
    // Get existing mock users or create an empty array
    const existingUsers = JSON.parse(localStorage.getItem("mockUsers") || "[]");

    // Check if user already exists
    const userExists = existingUsers.some(
      (user) => user.userAccount === values.userAccount
    );

    if (userExists) {
      showFailToast("Account already exists");
      loading.value = false;
      return;
    }

    // Create new user object
    const newUser = {
      userAccount: values.userAccount,
      userPassword: values.userPassword,
      id: Date.now(), // Use timestamp as ID
      username: values.userAccount, // Use account name as username
      avatarUrl: `https://placehold.co/100?text=${values.userAccount[0].toUpperCase()}`,
      gender: 0,
      phone: "",
      email: "",
      userStatus: 0,
      createTime: new Date().toISOString(),
      updateTime: new Date().toISOString(),
      userRole: 0,
    };

    // Add new user to mock users
    existingUsers.push(newUser);

    // Save updated mock users to localStorage
    localStorage.setItem("mockUsers", JSON.stringify(existingUsers));

    showSuccessToast("Registration successful");

    // Redirect to login page
    router.push("/login");
  } catch (error) {
    showFailToast("Registration failed, please try again");
    console.error("Registration error:", error);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="register-container">
    <div class="logo-box">
      <font-awesome-icon icon="user-friends" class="fa-icon" />
      <h1 class="app-name">FriendSync</h1>
      <p class="app-slogan">Create your account</p>
    </div>

    <van-form @submit="onSubmit" class="register-form">
      <van-cell-group inset>
        <van-field
          v-model="formState.userAccount"
          name="userAccount"
          placeholder="Account"
          :rules="rules.userAccount"
        />
        <van-field
          v-model="formState.userPassword"
          type="password"
          name="userPassword"
          placeholder="Password"
          :rules="rules.userPassword"
        />
        <van-field
          v-model="formState.checkPassword"
          type="password"
          name="checkPassword"
          placeholder="Enter your password again"
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
          class="gradient-button"
        >
          Register
        </van-button>
      </div>

      <div class="login-link">
        Already have an account?
        <a @click="router.push('/login')">Login now</a>
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

.fa-icon {
  font-size: 48px;
  color: #1989fa;
  margin-bottom: 16px;
}
</style>
