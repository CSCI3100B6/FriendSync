<template>
  <div class="chat-room-container">
    <!-- 頂部導航欄 -->
    <div class="chat-header">
      <div class="header-left">
        <van-icon name="arrow-left" @click="goBack" />
      </div>
      <div class="header-content">
        <div class="chat-title">{{ conversationInfo.name || "聊天" }}</div>
      </div>
      <div class="header-right">
        <van-popover
          v-model:show="showMenu"
          placement="bottom-end"
          :actions="menuActions"
          @select="onMenuSelect"
        >
          <template #reference>
            <van-icon name="ellipsis" class="menu-icon" />
          </template>
        </van-popover>
      </div>
    </div>

    <!-- 聊天內容區域 -->
    <div class="chat-content" ref="chatContentRef">
      <van-pull-refresh
        v-model="refreshing"
        @refresh="loadMoreMessages"
        success-text="加載更多消息"
      >
        <van-empty
          v-if="messages.length === 0 && !loading"
          description="暫無消息"
        />

        <template v-else>
          <div class="message-date-divider" v-if="messages.length > 0">
            {{ formatDate(messages[0].createTime) }}
          </div>

          <div
            v-for="(message, index) in messages"
            :key="message.id"
            class="message-item-wrapper"
          >
            <!-- 日期分割線 -->
            <div
              class="message-date-divider"
              v-if="
                index > 0 &&
                shouldShowDateDivider(
                  messages[index - 1].createTime,
                  message.createTime
                )
              "
            >
              {{ formatDate(message.createTime) }}
            </div>

            <!-- 消息氣泡 -->
            <div
              :class="[
                'message-item',
                message.userId === userStore.userInfo?.id
                  ? 'message-mine'
                  : 'message-others',
              ]"
            >
              <div
                class="avatar-container"
                v-if="message.userId !== userStore.userInfo?.id"
              >
                <van-image
                  round
                  width="40"
                  height="40"
                  :src="
                    message.userAvatar ||
                    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStmeAfZR3Qcxz2bdr35DgXK8sehEeern-fNg&s'
                  "
                  fit="cover"
                />
              </div>

              <div class="message-content">
                <div
                  class="sender-name"
                  v-if="
                    message.userId !== userStore.userInfo?.id &&
                    conversationInfo.type !== 'CHAT'
                  "
                >
                  {{ message.userName || "用户" }}
                </div>
                <div class="message-bubble">
                  {{ message.content }}
                </div>
                <div class="message-time">
                  {{ formatTime(message.createTime) }}
                </div>
              </div>

              <div
                class="avatar-container"
                v-if="message.userId === userStore.userInfo?.id"
              >
                <van-image
                  round
                  width="40"
                  height="40"
                  :src="
                    userStore.userInfo?.avatar ||
                    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStmeAfZR3Qcxz2bdr35DgXK8sehEeern-fNg&s'
                  "
                  fit="cover"
                />
              </div>
            </div>
          </div>
        </template>
      </van-pull-refresh>

      <div class="loading-container" v-if="loading">
        <van-loading type="spinner" color="#1989fa" />
      </div>
    </div>

    <!-- 輸入框區域 -->
    <div class="chat-input-container">
      <div class="input-wrapper">
        <van-field
          v-model="inputMessage"
          placeholder="輸入消息..."
          type="textarea"
          rows="1"
          autosize
          maxlength="500"
          class="message-input"
          @keypress.enter.prevent="sendMessage"
        />
        <van-button
          type="primary"
          size="small"
          class="send-btn"
          icon="send-o"
          :disabled="!inputMessage.trim()"
          @click="sendMessage"
          >發送</van-button
        >
      </div>
    </div>

    <!-- 退出會話確認對話框 -->
    <van-dialog
      v-model:show="showExitDialog"
      title="退出會話"
      :showCancelButton="true"
      @confirm="exitConversation"
    >
      <div class="exit-dialog-content">
        <p>確定要退出"{{ conversationInfo.name || "該會話" }}"嗎?</p>
      </div>
    </van-dialog>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import {
  ref,
  onMounted,
  computed,
  nextTick,
  watch,
  onBeforeUnmount,
} from "vue";
// @ts-ignore
import { useRouter, useRoute } from "vue-router";
// @ts-ignore
import { useUserStore } from "@/stores/user";
// @ts-ignore
import { Toast, Dialog } from "vant";
// 導入消息相關API
import {
  getHistoryMessages,
  sendMessage as sendMessageApi,
} from "@/api/message";
// 導入會話相關API
import { ConversationType, leaveConversation } from "@/api/conversation";

// 定義消息類型接口
interface Message {
  id: number;
  conversationId: number;
  userId: number;
  userName?: string;
  userAvatar?: string;
  content: string;
  createTime: string;
}

// 定義會話信息接口
interface ConversationInfo {
  id: number;
  type: string;
  name: string | null;
  information: string | null;
  ownerId: number;
  license: string | null;
}

// 定義菜單操作接口
interface MenuAction {
  text: string;
  icon: string;
  color?: string;
}

// 定義後端消息類型接口
interface ServerMessage {
  id: number;
  conversationId: number;
  senderId: number;
  msgContent: string;
  createTime: string;
}

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const conversationId = computed(() => Number(route.params.id) || 0);

// 響應式數據
const messages = ref<Message[]>([]);
const inputMessage = ref("");
const loading = ref(false);
const refreshing = ref(false);
const showMenu = ref(false);
const showExitDialog = ref(false);
const chatContentRef = ref<HTMLElement | null>(null);
const memberCount = ref(0);
const conversationInfo = ref<ConversationInfo>({
  id: 0,
  type: "CHAT",
  name: null,
  information: null,
  ownerId: 0,
  license: null,
});

// 是否可以退出會話（團隊和聊天室可以退出，私聊不可以）
const canExit = computed(() => {
  return (
    conversationInfo.value.type === "TEAM" ||
    conversationInfo.value.type === "ROOM"
  );
});

// 菜單選項
const menuActions = computed<MenuAction[]>(() => {
  const actions: MenuAction[] = [{ text: "會話信息", icon: "info-o" }];

  // 只有團隊和聊天室顯示退出選項
  if (canExit.value) {
    actions.push({ text: "退出會話", icon: "delete-o", color: "#ee0a24" });
  }

  return actions;
});

// 根據聊天類型設置標題
const setTitle = () => {
  let title = conversationInfo.value.name || "";

  if (conversationInfo.value.type === "CHAT") {
    // 私聊默認顯示對方名稱
    title = title || "私聊";
  } else if (conversationInfo.value.type === "TEAM") {
    title = title || "加密團隊";
  } else if (conversationInfo.value.type === "ROOM") {
    title = title || "聊天室";
  }

  // 設置文檔標題
  document.title = title;
};

// 獲取會話信息
const fetchConversationInfo = async () => {
  if (!conversationId.value) return;

  try {
    // 從路由參數中獲取會話信息
    const { name, information, type, ownerId } = route.query;

    // 設置會話信息，優先使用路由參數中的值
    conversationInfo.value = {
      id: conversationId.value,
      type:
        (type as string) || (route.path.includes("/chat/") ? "CHAT" : "TEAM"),
      name: (name as string) || null,
      information: (information as string) || null,
      ownerId: ownerId ? parseInt(ownerId as string) : 0,
      license: null,
    };

    console.log("初始化會話信息:", conversationInfo.value);

    // 如果需要從API獲取更詳細的會話信息，可以在這裏添加對API的調用
    // const response = await getConversationDetails(conversationId.value);
    // if (response) {
    //   conversationInfo.value = { ...conversationInfo.value, ...response };
    // }

    // 模擬成員數量 - 實際項目中應從API獲取
    if (conversationInfo.value.type !== "CHAT") {
      memberCount.value = Math.floor(Math.random() * 20) + 2;
    } else {
      memberCount.value = 2; // 私聊默認為2人
    }

    // 保存會話信息到本地存儲以便後續使用
    localStorage.setItem(
      `conversation_${conversationId.value}`,
      JSON.stringify(conversationInfo.value)
    );

    // 設置標題
    setTitle();
  } catch (error) {
    console.error("獲取會話信息失敗:", error);
    Toast.fail("獲取會話信息失敗");
  }
};

// 輪詢間隔（毫秒）
const POLL_INTERVAL = 2500;
let pollTimer: number | null = null;

// 加載聊天記錄
const loadMessages = async (refresh = false) => {
  if (!conversationId.value) return;

  try {
    loading.value = true;

    // 使用偏移量和數量參數，適配後端API
    const offset = refresh ? 0 : messages.value.length;
    const num = 20;

    const result = await getHistoryMessages(conversationId.value, offset, num);
    console.log("獲取消息響應:", result);

    // 轉換後端消息格式為前端消息格式
    const formattedMessages = Array.isArray(result)
      ? result.map((msg: ServerMessage) => ({
          id: msg.id,
          conversationId: msg.conversationId,
          userId: msg.senderId,
          content: msg.msgContent,
          createTime: msg.createTime,
          // 其他字段如用户名和頭像可以從後續API獲取，或從緩存中獲取
        }))
      : [];

    if (formattedMessages.length > 0) {
      if (refresh) {
        messages.value = formattedMessages;
      } else {
        // 合併消息，避免重複
        const newMessageIds = new Set(formattedMessages.map((msg) => msg.id));
        const existingMessages = messages.value.filter(
          (msg: Message) => !newMessageIds.has(msg.id)
        );
        messages.value = [...existingMessages, ...formattedMessages];
      }

      // 如果消息少於 num，則認為加載完成
      if (formattedMessages.length < num) {
        refreshing.value = false;
      }
    } else if (refresh) {
      // 如果是刷新但沒有消息，則清空列表
      messages.value = [];
    }
  } catch (error) {
    console.error("加載消息失敗:", error);
    Toast.fail("加載消息失敗");
  } finally {
    loading.value = false;
    refreshing.value = false;

    // 滾動到底部
    if (refresh) {
      scrollToBottom();
    }
  }
};

// 啓動輪詢
const startPolling = () => {
  // 確保不重複啓動
  if (pollTimer) {
    clearInterval(pollTimer);
  }

  // 創建輪詢定時器
  pollTimer = window.setInterval(() => {
    // 只有當前顯示的消息頁面才進行輪詢
    if (document.visibilityState === "visible") {
      loadMessages(true);
    }
  }, POLL_INTERVAL);

  console.log("已啓動消息輪詢");
};

// 停止輪詢
const stopPolling = () => {
  if (pollTimer) {
    clearInterval(pollTimer);
    pollTimer = null;
    console.log("已停止消息輪詢");
  }
};

// 發送消息
const sendMessage = async () => {
  if (
    !inputMessage.value.trim() ||
    !userStore.userInfo ||
    !conversationId.value
  )
    return;

  try {
    const userId = userStore.userInfo.id;
    const content = inputMessage.value.trim();

    // 發送消息前顯示臨時消息
    const tempMessage: Message = {
      id: Date.now(), // 臨時ID
      conversationId: conversationId.value,
      userId: userId,
      content: content,
      createTime: new Date().toISOString(),
    };
    messages.value.push(tempMessage);

    // 清空輸入框
    inputMessage.value = "";

    // 滾動到底部
    scrollToBottom();

    // 發送消息
    const response = await sendMessageApi(
      conversationId.value,
      content,
      userId
    );
    console.log("發送消息響應:", response);

    // 重新加載消息列表，獲取服務器分配的正式ID
    setTimeout(() => {
      loadMessages(true);
    }, 300);
  } catch (error) {
    console.error("發送消息失敗:", error);
    Toast.fail("發送消息失敗");
  }
};

// 菜單選擇處理
const onMenuSelect = (action: MenuAction) => {
  if (action.text === "會話信息") {
    // 查看會話信息（可以添加跳轉邏輯）
    Toast("會話信息功能開發中");
  } else if (action.text === "退出會話") {
    // 顯示退出確認框
    showExitDialog.value = true;
  }
};

// 退出會話
const exitConversation = async () => {
  try {
    // 調用離開會話的API
    Toast.loading({
      message: "正在退出...",
      forbidClick: true,
      duration: 0,
    });

    const response = await leaveConversation(conversationId.value);

    Toast.clear();

    // 判斷響應是否成功
    const isSuccess =
      (typeof response === "string" && response === "success") ||
      (response &&
        typeof response === "object" &&
        (response.data === "success" || response.status === 200));

    if (isSuccess) {
      Toast.success("已退出會話");
      // 返回會話列表
      router.replace("/layout/messages");
    } else {
      Toast.fail("退出會話失敗");
    }
  } catch (error) {
    console.error("退出會話失敗:", error);
    Toast.clear();
    Toast.fail("退出會話失敗");
  }
};

// 返回上一頁
const goBack = () => {
  router.back();
};

// 滾動到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatContentRef.value) {
      chatContentRef.value.scrollTop = chatContentRef.value.scrollHeight;
    }
  });
};

// 日期格式化
const formatDate = (dateStr?: string) => {
  if (!dateStr) return "";

  const date = new Date(dateStr);
  const now = new Date();
  const yesterday = new Date(now);
  yesterday.setDate(now.getDate() - 1);

  if (date.toDateString() === now.toDateString()) {
    return "今天";
  } else if (date.toDateString() === yesterday.toDateString()) {
    return "昨天";
  } else {
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
  }
};

// 時間格式化
const formatTime = (dateStr?: string) => {
  if (!dateStr) return "";

  const date = new Date(dateStr);
  return date.toLocaleTimeString("zh-HK", {
    hour: "2-digit",
    minute: "2-digit",
  });
};

// 是否顯示日期分割線
const shouldShowDateDivider = (prevDateStr?: string, currDateStr?: string) => {
  if (!prevDateStr || !currDateStr) return false;

  const prevDate = new Date(prevDateStr);
  const currDate = new Date(currDateStr);

  return prevDate.toDateString() !== currDate.toDateString();
};

// 監聽會話ID變化，重新加載數據
watch(conversationId, () => {
  if (conversationId.value) {
    messages.value = [];
    fetchConversationInfo();
    loadMessages(true);
    // 重新啓動輪詢
    startPolling();
  }
});

// 頁面加載時獲取數據並啓動輪詢
onMounted(() => {
  fetchConversationInfo();
  loadMessages(true);
  startPolling();

  // 添加頁面可見性監聽
  document.addEventListener("visibilitychange", handleVisibilityChange);
});

// 頁面卸載前停止輪詢
onBeforeUnmount(() => {
  stopPolling();
  // 移除頁面可見性監聽
  document.removeEventListener("visibilitychange", handleVisibilityChange);
});

// 處理頁面可見性變化
const handleVisibilityChange = () => {
  if (document.visibilityState === "visible") {
    // 頁面變為可見時，重新加載消息
    loadMessages(true);
  }
};

// 加載更多消息（下拉刷新）
const loadMoreMessages = () => {
  loadMessages(false);
};
</script>

<style scoped>
.chat-room-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f7;
}

/* 頭部樣式 */
.chat-header {
  height: 56px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  padding: 0 16px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  flex: 0 0 24px;
}

.header-left .van-icon {
  font-size: 24px;
  color: #323233;
}

.header-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  overflow: hidden;
}

.chat-title {
  font-size: 17px;
  font-weight: 600;
  color: #323233;
  width: 100%;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chat-subtitle {
  font-size: 12px;
  color: #969799;
  margin-top: 2px;
}

.header-right {
  flex: 0 0 24px;
  display: flex;
  justify-content: flex-end;
}

.header-right .menu-icon {
  font-size: 24px;
  color: #323233;
}

/* 聊天內容區域 */
.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  padding-bottom: 16px;
}

.message-date-divider {
  text-align: center;
  margin: 16px 0;
  color: #969799;
  font-size: 12px;
  position: relative;
}

.message-date-divider::before,
.message-date-divider::after {
  content: "";
  display: inline-block;
  width: 60px;
  height: 1px;
  background-color: #ebedf0;
  vertical-align: middle;
  margin: 0 10px;
}

.message-item-wrapper {
  margin-bottom: 16px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 4px;
}

.message-mine {
  justify-content: flex-end;
}

.avatar-container {
  flex-shrink: 0;
  margin: 0 12px;
}

.message-content {
  display: flex;
  flex-direction: column;
  max-width: 70%;
}

.sender-name {
  font-size: 12px;
  color: #969799;
  margin-bottom: 2px;
  padding-left: 2px;
}

.message-bubble {
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.4;
  word-break: break-word;
  white-space: pre-wrap;
}

.message-mine .message-bubble {
  background-color: #d1e9ff;
  color: #1989fa;
  border-bottom-right-radius: 2px;
}

.message-others .message-bubble {
  background-color: #ffffff;
  color: #323233;
  border-bottom-left-radius: 2px;
}

.message-time {
  font-size: 10px;
  color: #c8c9cc;
  margin-top: 2px;
  text-align: right;
}

.message-mine .message-time {
  text-align: right;
}

.message-others .message-time {
  text-align: left;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 0;
}

/* 輸入框區域 */
.chat-input-container {
  background-color: #ffffff;
  border-top: 1px solid #f2f2f2;
  padding: 8px 16px;
  padding-bottom: calc(8px + env(safe-area-inset-bottom));
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
}

.message-input {
  flex: 1;
  background-color: #f7f8fa;
  border-radius: 20px;
  padding: 8px 12px;
  margin-right: 12px;
}

:deep(.message-input .van-field__control) {
  min-height: 22px;
  max-height: 100px;
  line-height: 22px;
}

.send-btn {
  --van-button-primary-background-color: #1989fa;
  --van-button-primary-border-color: #1989fa;
  border-radius: 20px;
  padding: 0 14px;
  height: 38px;
}

/* 退出對話框樣式 */
.exit-dialog-content {
  padding: 20px 16px;
  text-align: center;
  color: #323233;
  line-height: 1.5;
}

/* 適配iPhone底部安全區域 */
@supports (padding-bottom: env(safe-area-inset-bottom)) {
  .chat-input-container {
    padding-bottom: calc(8px + env(safe-area-inset-bottom));
  }
}
</style>
