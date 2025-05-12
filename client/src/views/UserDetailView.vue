<template>
  <div class="user-detail-container">
    <div v-if="loading" class="loading-container">
      <van-loading color="#1976d2" />
      <span class="loading-text">加載中...</span>
    </div>

    <div v-else-if="user" class="user-profile">
      <div class="user-header">
        <van-image
          round
          width="80"
          height="80"
          :src="
            user.avatarUrl ||
            'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStmeAfZR3Qcxz2bdr35DgXK8sehEeern-fNg&s'
          "
          fit="cover"
        />
        <h2 class="user-name">{{ user.username }}</h2>
        <p class="user-id">ID: {{ user.id }}</p>
      </div>

      <div class="action-buttons">
        <van-button
          round
          type="primary"
          icon="chat-o"
          block
          @click="onMessageClick"
        >
          發起聊天
        </van-button>
      </div>
    </div>

    <div v-else class="error-container">
      <van-empty description="未找到用户信息" />
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, onMounted } from "vue";
// @ts-ignore
import { useRoute, useRouter } from "vue-router";
// @ts-ignore
import { Toast } from "vant";
// @ts-ignore
import { getCurrentUser } from "@/api/user";
// @ts-ignore
import { createPrivateChat } from "@/api/conversation";

const route = useRoute();
const router = useRouter();
const userId = ref(route.params.id);
const user = ref(null);
const loading = ref(true);

// 模擬獲取用户詳情
onMounted(async () => {
  try {
    // 這裏應該調用獲取用户詳情的API，目前後端沒有提供，所以模擬返回數據
    // 實際開發中，應該調用類似 getUserById(userId.value) 的API
    // const result = await getUserById(userId.value)

    // 模擬數據
    setTimeout(() => {
      user.value = {
        id: userId.value,
        username: `用户${userId.value}`,
        avatarUrl: "",
      };
      loading.value = false;
    }, 1000);
  } catch (error) {
    console.error("獲取用户詳情失敗", error);
    Toast.fail("獲取用户信息失敗");
    loading.value = false;
  }
});

// 發起私聊
async function onMessageClick() {
  try {
    Toast.loading({
      message: "創建聊天...",
      forbidClick: true,
    });

    // 獲取當前用户
    const currentUser = await getCurrentUser();

    // 創建私聊
    const chatResult = await createPrivateChat(
      currentUser.id,
      Number(userId.value)
    );

    // 跳轉到聊天頁面
    router.push(`/chat/${chatResult.id}`);

    Toast.clear();
  } catch (error) {
    console.error("創建聊天失敗", error);
    Toast.fail("創建聊天失敗，請稍後再試");
  }
}
</script>

<style scoped>
.user-detail-container {
  min-height: 100%;
  background-color: #f8f8f8;
  padding: 20px 16px;
}

.loading-container {
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.loading-text {
  font-size: 14px;
  color: #969799;
}

.user-profile {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.user-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  color: #323233;
  margin: 12px 0 4px;
}

.user-id {
  font-size: 14px;
  color: #969799;
  margin: 0;
}

.action-buttons {
  margin-top: 24px;
}

.error-container {
  padding: 40px 0;
}
</style>
