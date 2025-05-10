<template>
  <div class="messages-container">
    <!-- 添加Vant Tabs標籤欄 -->
    <van-tabs v-model:active="activeType" swipeable animated sticky>
      <van-tab title="全部會話" name="ALL"></van-tab>
      <van-tab title="加密團隊" name="TEAM"></van-tab>
      <van-tab title="聊天室" name="ROOM"></van-tab>
      <van-tab title="私聊" name="CHAT"></van-tab>
    </van-tabs>
    
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh" success-text="刷新成功">
      <van-empty v-if="filteredConversations.length === 0 && !loading" description="暫無會話" />
      
      <van-list
        v-else
        v-model:loading="loading"
        :finished="finished"
        finished-text="沒有更多了"
        @load="onLoad"
      >
        <div class="conversation-list">
          <div 
            v-for="item in filteredConversations" 
            :key="item.id" 
            class="conversation-item"
            @click="navigateToChat(item)"
          >
            <!-- 添加會話類型圖標 -->
            <div class="type-icon-wrapper">
              <van-icon 
                :name="getIconByType(item.type)" 
                :class="['conversation-type-icon', `type-${item.type.toLowerCase()}`]" 
              />
            </div>
            
            <div class="info">
              <div class="top-row">
                <div class="name">{{ item.name || (item.type === 'CHAT' ? '私聊會話' : '未命名會話') }}</div>
                <div class="owner" v-if="item.ownerId === userStore.userInfo?.id">
                  <van-tag type="danger">我創建的</van-tag>
                </div>
              </div>
              <div class="middle-row">
                <div class="description">{{ item.information || '暫無描述' }}</div>
              </div>
              <div class="bottom-row">
                <div class="type-info">
                  <van-icon :name="getIconByType(item.type)" class="type-icon" />
                  <span>{{ getTypeText(item.type) }}</span>
                  <van-icon v-if="item.type === 'TEAM' && item.license" name="lock" class="lock-icon" />
                </div>
                <div class="id-info">ID: {{ item.id }}</div>
              </div>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
    
    <van-empty v-if="error" description="加載失敗，請重試" />

    <!-- 懸浮按鈕羣組 -->
    <!-- 刪除了創建會話按鈕 -->
    
    <!-- 刪除了創建會話菜單 -->
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, onMounted, computed, watch } from 'vue'
// @ts-ignore
import { useRouter } from 'vue-router'
// @ts-ignore
import { useUserStore } from '@/stores/user'
// @ts-ignore
import { Toast } from 'vant'
import { getJoinedConversations } from '@/api/conversation'
import { ConversationType } from '@/api/conversation'

// 定義後端返回的會話類型接口
interface ConversationResponse {
  id: number
  type: string // "TEAM" | "ROOM" | "CHAT"
  ownerId: number
  name: string | null
  information: string | null
  license: string | null
}

const router = useRouter()
const userStore = useUserStore()

// 響應式數據
const conversations = ref<ConversationResponse[]>([])
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)
const error = ref(false)
const activeType = ref('ALL')

// 過濾後的會話列表
const filteredConversations = computed(() => {
  if (activeType.value === 'ALL') {
    return conversations.value
  }
  return conversations.value.filter((item: ConversationResponse) => item.type === activeType.value)
})

// 當標籤切換時，重置列表狀態
watch(activeType, () => {
  if (loading.value) return
  finished.value = false
  if (conversations.value.length === 0) {
    fetchConversations()
  }
})

// 獲取會話列表
const fetchConversations = async () => {
  if (!userStore.isLogin) {
    router.push('/login')
    return
  }

  try {
    loading.value = true
    const result = await getJoinedConversations()
    
    // 處理返回的數據
    if (Array.isArray(result)) {
      conversations.value = result
    }
    
    error.value = false
    finished.value = true
  } catch (err) {
    console.error('獲取會話列表失敗:', err)
    error.value = true
    Toast.fail('獲取會話列表失敗')
  } finally {
    loading.value = false
    refreshing.value = false
  }
}


// 根據會話類型獲取圖標
const getIconByType = (type: string) => {
  switch (type) {
    case 'TEAM':
      return 'shield-o'
    case 'ROOM':
      return 'friends-o'
    case 'CHAT':
      return 'chat-o'
    default:
      return 'chat-o'
  }
}

// 根據會話類型獲取文本描述
const getTypeText = (type: string) => {
  switch (type) {
    case 'TEAM':
      return '加密團隊'
    case 'ROOM':
      return '公開聊天室'
    case 'CHAT':
      return '私聊'
    default:
      return '未知類型'
  }
}

// 下拉刷新
const onRefresh = () => {
  finished.value = false
  fetchConversations()
}

// 上拉加載
const onLoad = () => {
  // 由於接口一次返回全部數據，這裏直接設置加載完成
  finished.value = true
}

// 跳轉到聊天頁面
const navigateToChat = (conversation: ConversationResponse) => {
  // 使用不同的路由名稱，基於會話類型
  const routeName = conversation.type === 'CHAT' ? 'chatroom' : 'teamroom'
  
  // 跳轉到對應的聊天頁面，傳遞會話ID和會話信息
  router.push({
    name: routeName, 
    params: { id: conversation.id },
    query: { 
      type: conversation.type,
      name: conversation.name || undefined,
      information: conversation.information || undefined
    }
  })
  
  // 暫時將會話信息存儲在本地存儲中，便於聊天頁面獲取
  localStorage.setItem(`conversation_${conversation.id}`, JSON.stringify({
    type: conversation.type,
    name: conversation.name,
    information: conversation.information,
    ownerId: conversation.ownerId,
    license: conversation.license
  }))
}

// 頁面加載時獲取會話列表
onMounted(() => {
  fetchConversations()
})
</script>

<style scoped>
.messages-container {
  padding: 0 0 0 0;
  background-color: #f7f8fa;
  min-height: 100%;
}

.conversation-list {
  padding: 16px;
}

.conversation-item {
  display: flex;
  align-items: center;
  padding: 16px;
  margin-bottom: 12px;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.conversation-item:active {
  background-color: #f5f5f5;
}

.type-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 18px;
  margin-right: 12px;
  background-color: #f5f5f5;
  flex-shrink: 0;
}

.conversation-type-icon {
  font-size: 20px;
}

.type-team {
  color: #1989fa;
}

.type-room {
  color: #07c160;
}

.type-chat {
  color: #ff976a;
}

.avatar {
  position: relative;
  margin-right: 16px;
  flex-shrink: 0;
}

.type-tag {
  position: absolute;
  bottom: -4px;
  right: -4px;
  transform: scale(0.8);
  transform-origin: right bottom;
}

.team-tag {
  --van-tag-primary-color: #1989fa;
}

.room-tag {
  --van-tag-success-color: #07c160;
}

.chat-tag {
  --van-tag-warning-color: #ff976a;
}

.info {
  flex: 1;
  overflow: hidden;
}

.top-row, .middle-row, .bottom-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.name {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
  margin-right: 8px;
}

.description {
  font-size: 14px;
  color: #646566;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.type-info {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #969799;
}

.type-icon, .lock-icon {
  margin-right: 4px;
  font-size: 14px;
}

.id-info {
  font-size: 12px;
  color: #c8c9cc;
}

/* 添加Tabs標籤欄樣式 */
:deep(.van-tabs) {
  --van-tabs-bottom-bar-color: #1976d2;
  --van-tabs-bottom-bar-height: 3px;
}

:deep(.van-tabs__wrap) {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background-color: #fff;
}

:deep(.van-tab) {
  font-size: 15px;
  font-weight: 500;
  color: #666;
}

:deep(.van-tab--active) {
  font-weight: 600;
  color: #1976d2;
}

/* 列表樣式優化 */
:deep(.van-list) {
  min-height: 100px;
  background-color: transparent;
}

:deep(.van-button--primary) {
  background-color: #1976d2;
  border-color: #1976d2;
}

:deep(.van-pull-refresh) {
  min-height: calc(100vh - 120px);
}

/* 刪除多餘的樣式 */
</style>
