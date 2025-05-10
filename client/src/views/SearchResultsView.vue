<template>
  <div class="search-results-container">
    <div class="header">
      <van-nav-bar 
        title="搜索結果" 
        left-arrow 
        @click-left="goBack"
      />
    </div>

    <div class="search-conditions">
      <div class="condition-item" v-if="keyword">
        <span class="label">關鍵詞：</span>
        <span class="value">{{ keyword }}</span>
      </div>
      <div class="condition-item" v-if="tagList.length > 0">
        <span class="label">標籤：</span>
        <div class="tag-list">
          <van-tag 
            v-for="tag in tagList" 
            :key="tag"
            type="primary"
            plain
            size="medium"
            class="tag"
          >
            {{ tag }}
          </van-tag>
        </div>
      </div>
    </div>

    <div class="results-content">
      <div v-if="loading" class="loading-container">
        <van-loading color="#1976d2" />
        <span class="loading-text">搜索中...</span>
      </div>

      <div v-else-if="!hasResults" class="empty-result">
        <van-empty description="未找到匹配的用户" />
      </div>

      <div v-else class="user-list">
        <div v-for="user in searchResults" :key="user.id" class="user-card" @click="viewUserProfile(user)">
          <div class="user-avatar">
            <img :src="user.avatarUrl || 'https://img01.yzcdn.cn/vant/cat.jpeg'" :alt="user.username || user.userAccount">
          </div>
          <div class="user-info">
            <div class="user-details">
              <div class="username">{{ user.username || user.userAccount }}</div>
              <div class="user-phone" v-if="user.phone">
                <van-icon name="phone-o" size="12" />
                <span>{{ user.phone }}</span>
              </div>
              <div class="user-email" v-if="user.email">
                <van-icon name="envelop-o" size="12" />
                <span>{{ user.email }}</span>
              </div>
              <div v-if="user.profile" class="user-bio">{{ user.profile }}</div>
              <div class="user-tags" v-if="user.tags && user.tags.length">
                <van-tag plain type="primary" v-for="tag in user.tags" :key="tag" class="tag">
                  {{ tag }}
                </van-tag>
              </div>
            </div>
            <van-button size="small" type="primary" class="add-friend-btn" @click.stop="addFriend(user)">發起私聊</van-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, computed, onMounted } from 'vue'
// @ts-ignore
import { useRouter, useRoute } from 'vue-router'
// @ts-ignore
import { Toast } from 'vant'
// @ts-ignore
import { searchUsers } from '@/api/user'
// @ts-ignore
import { useUserStore } from '@/stores/user'
import http from '@/api/http'

interface UserType {
  id: number;
  username: string | null;
  userAccount: string;
  avatarUrl: string | null;
  profile: string | null;
  phone: string | null;
  email: string | null;
  tags: string[] | null;
  [key: string]: any;
}

const router = useRouter()
const route = useRoute()
const searchResults = ref<UserType[]>([])
const loading = ref(false)

// 從路由查詢參數中獲取搜索條件
const keyword = ref(route.query.keyword as string || '')
const tagList = ref((route.query.tags as string || '').split(',').filter(Boolean))

// 計算是否有搜索結果
const hasResults = computed(() => searchResults.value.length > 0)

const userStore = useUserStore()

// 獲取搜索結果
async function fetchSearchResults() {
  if (!keyword.value && tagList.value.length === 0) {
    Toast('搜索條件不能為空')
    goBack()
    return
  }

  loading.value = true
  
  try {
    const params: {username?: string, tagNameList?: string[]} = {}
    
    if (keyword.value) {
      params.username = keyword.value
    }
    
    if (tagList.value.length > 0) {
      params.tagNameList = tagList.value
    }
    
    const response = await searchUsers(params)
    
    // 獲取當前用户ID
    const currentUserId = userStore.userInfo?.id
    
    // 處理響應數據
    if (response && response.data && Array.isArray(response.data)) {
      // 處理標籤數據，如果是字符串則解析為數組
      let results = response.data.map((user: UserType) => {
        if (user.tags && typeof user.tags === 'string') {
          try {
            user.tags = JSON.parse(user.tags);
          } catch (e) {
            user.tags = [];
          }
        }
        return user;
      });
      
      // 過濾掉當前登錄用户
      if (currentUserId) {
        results = results.filter((user: UserType) => user.id !== currentUserId);
        console.log('已過濾當前用户，剩餘結果數量:', results.length);
      }
      
      searchResults.value = results;
    } else if (Array.isArray(response)) {
      // 處理標籤數據，如果是字符串則解析為數組
      let results = response.map((user: UserType) => {
        if (user.tags && typeof user.tags === 'string') {
          try {
            user.tags = JSON.parse(user.tags);
          } catch (e) {
            user.tags = [];
          }
        }
        return user;
      });
      
      // 過濾掉當前登錄用户
      if (currentUserId) {
        results = results.filter((user: UserType) => user.id !== currentUserId);
        console.log('已過濾當前用户，剩餘結果數量:', results.length);
      }
      
      searchResults.value = results;
    } else {
      searchResults.value = []
      console.warn('未收到預期的搜索結果格式:', response)
    }
    
    if (searchResults.value.length === 0) {
      Toast('未找到匹配的用户')
    }
  } catch (error) {
    console.error('搜索用户失敗', error)
    Toast.fail('搜索失敗，請稍後再試')
    searchResults.value = []
  } finally {
    loading.value = false
  }
}

// 查看用户資料
function viewUserProfile(user: UserType) {
  router.push(`/user/${user.id}`)
}

// 返回上一頁
function goBack() {
  router.back()
}

// 添加好友/發起私聊
const addFriend = async (user: UserType) => {
  if (!userStore.isLogin) {
    Toast('請先登錄');
    router.push('/login');
    return;
  }
  
  try {
    Toast.loading({
      message: '正在創建聊天...',
      forbidClick: true,
      duration: 0
    });
    
    try {
      const currentUserId = userStore.userInfo?.id;
      if (!currentUserId) {
        throw new Error('未獲取到當前用户ID');
      }
      
      const params = {
        ownerId: currentUserId,
        othersId: user.id
      };
      
      const response = await http.post('/conversation/create-chat', params);
      console.log('創建聊天響應:', response);
      
      // 獲取響應數據
      const responseData = response.data || response;
      console.log('處理後的響應數據:', responseData);
      
      Toast.clear();
      
      // 檢查響應數據是否包含id
      if (responseData && responseData.id) {
        console.log('聊天會話ID:', responseData.id);
        // 使用提取的id進行路由跳轉
        router.push(`/chat/${responseData.id}`);
        return;
      } else {
        console.error('創建聊天返回數據無效:', responseData);
        throw new Error('創建聊天返回數據無效');
      }
    } catch (error) {
      console.error('創建聊天會話失敗:', error);
      Toast.clear();
      Toast.fail('創建聊天失敗，請稍後重試');
    }
  } catch (error) {
    Toast.clear();
    Toast.fail('操作失敗');
    console.error('發起私聊失敗:', error);
  }
};

onMounted(() => {
  fetchSearchResults()
})
</script>

<style scoped>
.search-results-container {
  min-height: 100%;
  background-color: #f8f8f8;
  display: flex;
  flex-direction: column;
}

.header {
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-conditions {
  padding: 12px 16px;
  background-color: white;
  margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.condition-item {
  display: flex;
  margin-bottom: 8px;
  align-items: flex-start;
}

.condition-item:last-child {
  margin-bottom: 0;
}

.label {
  font-size: 14px;
  color: #646566;
  margin-right: 8px;
  white-space: nowrap;
}

.value {
  font-size: 14px;
  color: #323233;
  font-weight: 500;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  margin-right: 8px;
}

.results-content {
  flex: 1;
  padding: 0 16px 16px;
}

.loading-container {
  padding: 24px 0;
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

.empty-result {
  padding: 40px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.user-list {
  margin-top: 16px;
}

.user-card {
  display: flex;
  align-items: flex-start;
  padding: 16px;
  background-color: white;
  border-radius: 12px;
  margin-bottom: 12px;
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

.user-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 6px;
}

.tag {
  font-size: 12px;
}

.add-friend-btn {
  flex-shrink: 0; /* 防止按鈕縮小 */
  height: 32px;
  font-size: 12px;
  align-self: flex-start;
  margin-top: 0;
}
</style> 