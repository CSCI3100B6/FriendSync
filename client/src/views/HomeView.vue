<template>
  <div class="home-container">
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">歡迎使用<br />FriendSync</h1>
        <p class="welcome-subtitle">與好友聯繫，發現新的機會</p>
      </div>
    </div>

    <div class="auth-section">
      <div class="auth-buttons" v-if="!userStore.isLogin">
        <van-button round block type="primary" class="sign-in-btn" to="/login"
          >登錄</van-button
        >
        <van-button round block plain class="create-account-btn" to="/register"
          >註冊賬號</van-button
        >
      </div>
    </div>

    <div class="search-section">
      <div class="search-header">
        <h2 class="section-title">推薦用户</h2>
      </div>

      <div class="user-list">
        <div v-if="loading" class="loading-container">
          <van-loading type="spinner" color="#1989fa" />
          <p>loading...</p>
        </div>
        <div v-else-if="!users || users.length === 0" class="empty-state">
          <van-empty description="暫無用户" />
        </div>
        <div v-else class="user-cards">
          <div
            v-for="user in users"
            :key="user.id"
            class="user-card"
            @click="viewUserProfile(user)"
          >
            <div class="user-avatar">
              <img :src="user.avatar" :alt="user.username" />
            </div>
            <div class="user-info">
              <div class="user-details">
                <div class="username">{{ user.username }}</div>
                <div class="user-phone">
                  <van-icon name="phone-o" size="12" />
                  <span>{{ user.phone }}</span>
                </div>
                <div class="user-email">
                  <van-icon name="envelop-o" size="12" />
                  <span>{{ user.email }}</span>
                </div>
                <div v-if="user.bio" class="user-bio">{{ user.bio }}</div>
              </div>
              <van-button
                size="small"
                type="primary"
                class="add-friend-btn"
                @click.stop="addFriend(user)"
                >發起私聊</van-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, onMounted } from "vue";
// @ts-ignore
import { useUserStore } from "@/stores/user";
// @ts-ignore
import { Toast } from "vant";
// @ts-ignore
import { useRouter } from "vue-router";
import { searchUsers as apiSearchUsers } from "@/api/user";
import http from "@/api/http";

// 定義後端返回的響應接口
interface ApiResponse<T> {
  code: number;
  data: T;
  message: string;
  description: string;
}

// 定義後端返回的用户接口
interface UserResponse {
  id: number;
  username: string | null;
  userAccount: string;
  avatarUrl: string | null;
  gender: number | null;
  profile: string | null;
  phone: string | null;
  email: string | null;
  createTime: string;
  updateTime: string;
  isDelete: boolean | null;
  tags: string[] | null;
}

// 定義前端顯示用的用户接口
interface UserDisplay {
  id: number;
  username: string;
  avatar: string;
  bio: string;
  phone: string;
  email: string;
}

// 定義接口
interface CreateChatRequest {
  ownerId: number;
  othersId: number;
}

const router = useRouter();
const userStore = useUserStore();
const users = ref<UserDisplay[]>([]);
const loading = ref(false);

// 獲取用户列表
const loadUsers = async () => {
  loading.value = true;
  try {
    const response = await apiSearchUsers({});
    console.log("原始搜索結果完整數據:", response);

    // 獲取響應數據
    let result = response;

    // 檢查是否是 Axios 響應格式
    if (
      response &&
      response.hasOwnProperty("data") &&
      response.hasOwnProperty("status")
    ) {
      result = response.data;
      console.log("從 Axios 響應中提取的數據:", result);
    }

    // 直接檢查是否符合預期結構
    if (result && result.data && Array.isArray(result.data)) {
      // 過濾掉當前登錄用户
      const currentUserId = userStore.userInfo?.id;
      console.log("當前用户ID:", currentUserId);

      const filteredUsers = result.data.filter(
        (user: UserResponse) => user.id !== currentUserId
      );
      console.log("過濾後的用户:", filteredUsers);

      users.value = filteredUsers.map((user: UserResponse) => {
        return {
          id: user.id,
          username: user.username || user.userAccount, // 如果用户名為空，使用賬號
          avatar: user.avatarUrl || "https://img01.yzcdn.cn/vant/cat.jpeg",
          bio: user.profile || "這個人什麼都沒寫",
          phone: user.phone || "未設置",
          email: user.email || "未設置",
        };
      });

      console.log("最終用户列表:", users.value);
    } else {
      console.log("搜索結果格式不正確, 詳細信息:", result);
      users.value = [];
    }
  } catch (error) {
    console.error("獲取用户失敗:", error);
    Toast("獲取用户失敗, 請稍後重試");
    users.value = [];
  } finally {
    loading.value = false;
  }
};

// 查看用户資料
const viewUserProfile = (user: UserDisplay) => {
  console.log("查看用户資料:", user);
  Toast(`查看用户: ${user.username} 的資料`);
};

// 添加好友/發起私聊
const addFriend = async (user: UserDisplay) => {
  if (!userStore.isLogin) {
    Toast("請先登錄");
    router.push("/login");
    return;
  }

  try {
    Toast.loading({
      message: "正在創建聊天...",
      forbidClick: true,
      duration: 0,
    });

    try {
      const currentUserId = userStore.userInfo?.id;
      if (!currentUserId) {
        throw new Error("未獲取到當前用户ID");
      }

      const params: CreateChatRequest = {
        ownerId: currentUserId,
        othersId: user.id,
      };

      const response = await http.post("/conversation/create-chat", params);
      console.log("創建聊天響應:", response);

      // 獲取響應數據
      const responseData = response.data || response;
      console.log("處理後的響應數據:", responseData);

      Toast.clear();

      // 檢查響應數據是否包含id
      if (responseData && responseData.id) {
        console.log("聊天會話ID:", responseData.id);
        // 使用提取的id進行路由跳轉
        router.push(`/chat/${responseData.id}`);
        return;
      } else {
        console.error("創建聊天返回數據無效:", responseData);
        throw new Error("創建聊天返回數據無效");
      }
    } catch (error) {
      console.error("創建聊天會話失敗:", error);
      Toast.clear();
      Toast.fail("創建聊天失敗, 請稍後重試");
    }
  } catch (error) {
    Toast.clear();
    Toast.fail("操作失敗");
    console.error("發起私聊失敗:", error);
  }
};

// 頁面加載時獲取用户數據
onMounted(() => {
  console.log("組件掛載, 開始獲取用户數據");
  loadUsers();
});
</script>

<style scoped>
.home-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  overflow-y: auto;
  padding-bottom: 0;
}

.welcome-section {
  flex: 0 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 40px 24px 32px;
  background: linear-gradient(135deg, #2979ff, #1565c0);
  color: white;
  position: relative;
  /* min-height: 35vh; */
  box-shadow: 0 4px 16px rgba(25, 137, 250, 0.2);
}

.welcome-content {
  max-width: 100%;
}

.welcome-title {
  font-size: 36px;
  font-weight: 700;
  line-height: 1.2;
  margin-bottom: 12px;
  color: white;
  letter-spacing: -0.5px;
}

.welcome-subtitle {
  font-size: 18px;
  line-height: 1.5;
  margin-bottom: 0;
  opacity: 0.95;
  letter-spacing: 0.2px;
  font-weight: 400;
}

.auth-section {
  flex: 0 0 auto;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 24px 24px 0 0;
  margin-top: -20px;
  position: relative;
  z-index: 10;
  box-shadow: 0 -8px 24px rgba(0, 0, 0, 0.06);
}

.auth-buttons {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.sign-in-btn {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(to right, #1976d2, #2196f3);
  border: none;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
}

.create-account-btn {
  height: 48px;
  font-size: 16px;
  font-weight: 500;
  border-color: #1976d2;
  color: #1976d2;
}

.search-section {
  flex: 1;
  padding: 16px;
  background-color: #f8f9fa;
}

.search-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.user-list {
  margin-top: 16px;
}

.user-cards {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.user-card {
  display: flex;
  align-items: flex-start;
  padding: 16px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s, box-shadow 0.2s;
}

.user-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.user-avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 16px;
  border: 2px solid #f2f3f5;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  min-width: 0; /* 確保flex容器可以正確收縮 */
}

.user-details {
  flex: 1;
  min-width: 0; /* 確保可以收縮 */
  margin-right: 12px;
}

.username {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-phone,
.user-email {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #969799;
  margin-bottom: 4px;
}

.user-phone span,
.user-email span {
  margin-left: 4px;
}

.user-bio {
  font-size: 12px;
  color: #646566;
  margin-top: 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}

.add-friend-btn {
  flex-shrink: 0; /* 防止按鈕縮小 */
  height: 32px;
  font-size: 12px;
  align-self: flex-start;
  margin-top: 0;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 32px 0;
}

.loading-container p {
  margin-top: 12px;
  color: #969799;
  font-size: 14px;
}

.empty-state {
  padding: 32px 0;
}
</style>
