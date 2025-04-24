<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { showSuccessToast } from "vant";
import { Dialog } from "vant"; // Import Dialog directly

const router = useRouter();
const userInfo = ref(null);
const isLoggedIn = computed(() => userInfo.value !== null);
const showLogoutPrompt = ref(false); // For our custom dialog solution

// Get user data on component mount
onMounted(() => {
  const storedUserInfo = localStorage.getItem("userInfo");
  if (storedUserInfo) {
    try {
      userInfo.value = JSON.parse(storedUserInfo);
    } catch (e) {
      console.error("Failed to parse user info", e);
    }
  }
});

// Handle logout - Use built-in Dialog component method
const handleLogout = () => {
  // Show the custom dialog
  showLogoutPrompt.value = true;
};

// Confirm logout
const confirmLogout = () => {
  localStorage.removeItem("userInfo");
  showSuccessToast("Logged out successfully");
  router.push("/");
  showLogoutPrompt.value = false;
};
</script>

<template>
  <div class="profile-container">
    <!-- User is logged in -->
    <div v-if="isLoggedIn" class="profile-content">
      <div class="profile-header">
        <van-image
          round
          width="80"
          height="80"
          :src="userInfo.avatarUrl || '/images/default-avatar.png'"
          class="avatar"
        >
          <template #error>
            <div class="avatar-fallback">
              {{ userInfo.username ? userInfo.username[0].toUpperCase() : "U" }}
            </div>
          </template>
        </van-image>
        <h2 class="profile-name">{{ userInfo.username }}</h2>
        <p class="profile-account">@{{ userInfo.userAccount }}</p>
      </div>

      <!-- Profile details remain the same -->
      <div class="profile-details">
        <!-- Rest of profile details remain the same -->
      </div>

      <div class="action-buttons">
        <van-button icon="setting-o" plain type="primary" size="small"
          >Edit Profile</van-button
        >
        <van-button
          icon="cross"
          plain
          type="danger"
          size="small"
          @click="handleLogout"
          >Logout</van-button
        >
      </div>
    </div>

    <!-- User is not logged in -->
    <div v-else class="login-prompt">
      <van-icon name="user-circle-o" size="48" />
      <h2>Not Logged In</h2>
      <p>Please login to view your profile</p>
      <van-button type="primary" round to="/login">Sign In</van-button>
    </div>

    <!-- Custom Logout Dialog -->
    <div
      v-if="showLogoutPrompt"
      class="custom-dialog-overlay"
      @click="showLogoutPrompt = false"
    >
      <div class="custom-dialog" @click.stop>
        <h3 class="dialog-title">Logout</h3>
        <p class="dialog-message">Are you sure you want to logout?</p>
        <div class="dialog-buttons">
          <van-button size="small" plain @click="showLogoutPrompt = false"
            >Cancel</van-button
          >
          <van-button size="small" type="danger" @click="confirmLogout"
            >Logout</van-button
          >
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  padding: 20px;
  margin-top: 16px;
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.avatar {
  margin-bottom: 12px;
}

.profile-name {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #323233;
}

.profile-account {
  font-size: 14px;
  color: #969799;
  margin-bottom: 8px;
}

.profile-details {
  background-color: white;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.profile-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.profile-item:last-child {
  border-bottom: none;
}

.profile-item .van-icon {
  margin-right: 12px;
  color: #1989fa;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
}

.login-prompt {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.login-prompt .van-icon {
  color: #1989fa;
  margin-bottom: 16px;
}

.login-prompt h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #323233;
}

.login-prompt p {
  font-size: 14px;
  color: #969799;
  margin-bottom: 20px;
}

.custom-dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-dialog {
  width: 80%;
  max-width: 300px;
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.dialog-title {
  font-size: 18px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 12px;
}

.dialog-message {
  text-align: center;
  margin-bottom: 20px;
  color: #646566;
}

.dialog-buttons {
  display: flex;
  justify-content: space-between;
}

.avatar-fallback {
  width: 100%;
  height: 100%;
  background-color: #e1e1e1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: #909090;
  border-radius: 50%;
}
</style>
