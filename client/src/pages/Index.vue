<script setup lang="ts">
import { ref, onMounted, computed } from "vue";

// Define user info state
const userInfo = ref(null);
const isLoggedIn = computed(() => userInfo.value !== null);

// Function to get logged in user data
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
</script>

<template>
  <!-- Guest view -->
  <div v-if="!isLoggedIn" class="guest-view">
    <div class="welcome-container">
      <h1 class="homepage-title">Welcome to FriendSync</h1>
      <p class="homepage-subtitle">
        Connect with friends and discover new opportunities
      </p>
    </div>

    <div class="cta-container">
      <van-button
        round
        type="primary"
        size="large"
        to="/login"
        class="cta-button"
        >Sign In</van-button
      >
      <van-button round plain size="large" to="/register" class="cta-button"
        >Create Account</van-button
      >
    </div>

    <div class="features">
      <div class="feature-item">
        <div class="feature-icon">
          <van-icon name="friends-o" size="24" />
        </div>
        <h3>Connect</h3>
        <p>Find and connect with friends</p>
      </div>
      <div class="feature-item">
        <div class="feature-icon">
          <van-icon name="chat-o" size="24" />
        </div>
        <h3>Chat</h3>
        <p>Message your connections easily</p>
      </div>
      <div class="feature-item">
        <div class="feature-icon">
          <van-icon name="like-o" size="24" />
        </div>
        <h3>Share</h3>
        <p>Share moments with your network</p>
      </div>
    </div>
  </div>

  <!-- Logged in view remains unchanged -->
  <div v-else class="user-view">
    <!-- ...existing code for logged-in view... -->
  </div>
</template>

<style scoped>
.guest-view {
  padding: 20px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between; /* Distributes space better */
  height: calc(100vh - 96px); /* Maintain fixed height */
  overflow: hidden;
  background-color: #f7f8fa;
  box-sizing: border-box;
  padding-top: 30px; /* Reduced from 40px */
  padding-bottom: 30px; /* Add bottom padding */
}

.homepage-title {
  font-size: 32px; /* Increased from 28px */
  font-weight: bold;
  margin-bottom: 12px;
  color: #323233;
  text-align: center;
}

.homepage-subtitle {
  font-size: 16px;
  color: #646566;
  margin-bottom: 28px;
  text-align: center;
  max-width: 300px;
  line-height: 1.5;
}

.welcome-container {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #42a6ff 0%, #0075e6 100%);
  padding: 30px 25px; /* Increased padding */
  border-radius: 16px;
  color: white;
  width: 100%;
  max-width: 450px;
  box-shadow: 0 4px 12px rgba(0, 117, 230, 0.2);
}

.welcome-container .homepage-title {
  color: white;
}

.welcome-container .homepage-subtitle {
  color: rgba(255, 255, 255, 0.9);
  margin: 0 auto;
  font-size: 17px;
}

.cta-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
  max-width: 300px;
  margin-bottom: 25px;
  padding: 0 24px;
}

.cta-button {
  width: 100%;
  height: 48px;
  font-weight: 500;
  font-size: 16px;
  transition: all 0.25s ease;
}

.cta-button.van-button--primary:hover {
  background-color: #0062c1; /* Deeper blue */
  box-shadow: 0 6px 12px rgba(0, 98, 193, 0.25);
  transform: translateY(-2px);
}

.cta-button.van-button--default:hover {
  border-color: #0075e6;
  color: #0075e6;
  background-color: rgba(0, 117, 230, 0.05);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
}

.features {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  width: 100%;
  max-width: 500px;
  gap: 18px;
}

.feature-item {
  flex: 1;
  min-width: 100px;
  text-align: center;
  padding: 20px 12px; /* Increased padding */
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.feature-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.feature-icon {
  background-color: rgba(25, 137, 250, 0.1);
  width: 56px; /* Increased from 48px */
  height: 56px; /* Increased from 48px */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
}

.feature-icon .van-icon {
  color: #1989fa;
  font-size: 28px;
}

.feature-item h3 {
  margin: 10px 0 8px;
  font-size: 16px;
  color: #323233;
  font-weight: 600;
}

.feature-item p {
  font-size: 14px;
  color: #969799;
  line-height: 1.4;
}

/* Keep existing logged-in view styles */
.user-view {
  padding: 16px;
}

.welcome-banner {
  background: linear-gradient(135deg, #42a6ff 0%, #0075e6 100%);
  border-radius: 12px;
  padding: 20px;
  color: white;
  margin-bottom: 16px;
}

.welcome-banner .homepage-title {
  color: white;
  margin-bottom: 4px;
}

.last-login {
  font-size: 12px;
  opacity: 0.8;
}

/* Preserve other existing styles */
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin: 16px 0;
  color: #323233;
}

.feed-container {
  background-color: #f7f8fa;
  border-radius: 8px;
  overflow: hidden;
}

.activity-feed {
  padding: 0 8px;
}

.feed-item {
  background: white;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.feed-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.feed-info {
  margin-left: 12px;
}

.feed-name {
  font-weight: 500;
  font-size: 14px;
}

.feed-time {
  font-size: 12px;
  color: #969799;
}

.feed-content {
  margin: 12px 0;
  font-size: 14px;
  line-height: 1.5;
}

.feed-link {
  background-color: #f7f8fa;
  padding: 8px;
  border-radius: 4px;
  margin-top: 8px;
}

.feed-link a {
  color: #1989fa;
  text-decoration: none;
}

.feed-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}
</style>
