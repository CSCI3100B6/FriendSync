<template>
  <div class="profile-container">
    <!-- 加載狀態 -->
    <div v-if="loading" class="loading-state">
      <van-loading size="24px" vertical color="#1989fa">加載中...</van-loading>
    </div>

    <!-- 錯誤狀態 -->
    <div v-else-if="error" class="error-state">
      <van-empty description="獲取用户信息失敗">
        <template #image>
          <van-icon name="warning-o" size="48" color="#ee0a24" />
        </template>
        <van-button
          class="retry-btn"
          type="primary"
          size="small"
          @click="loadUserInfo"
          >重試</van-button
        >
      </van-empty>
    </div>

    <!-- 成功狀態 -->
    <template v-else>
      <div class="profile-header">
        <div class="profile-avatar">
          <img
            :src="userInfo?.avatar || 'https://img01.yzcdn.cn/vant/cat.jpeg'"
            alt="頭像"
          />
        </div>
        <h2 class="profile-name">
          {{ userInfo?.nickname || userInfo?.username || "未設置暱稱" }}
        </h2>
        <p class="profile-id">ID: {{ userInfo?.id || "--" }}</p>
        <div class="profile-stats">
          <div
            v-for="(tag, index) in displayTags"
            :key="index"
            class="stat-item"
          >
            <span class="stat-value">{{ tag.name }}</span>
            <span class="stat-label" v-if="tag.value > 0">{{ tag.value }}</span>
          </div>
        </div>
      </div>

      <div class="profile-section">
        <van-cell-group inset title="賬號信息">
          <van-cell title="賬號" :value="userInfo?.userAccount || '未設置'" />
          <van-cell title="郵箱" :value="userInfo?.email || '未設置'" />
          <van-cell title="手機" :value="userInfo?.phone || '未設置'" />
          <van-cell
            title="註冊時間"
            :value="formatDate(userInfo?.createTime)"
          />
        </van-cell-group>
      </div>

      <div class="profile-section">
        <van-cell-group inset title="個人設置">
          <van-cell title="我的標籤" is-link @click="showTagSelector" />
        </van-cell-group>
      </div>

      <div class="logout-button-container">
        <van-button
          block
          type="danger"
          size="large"
          class="logout-button"
          @click="handleLogout"
          >退出登錄</van-button
        >
      </div>

      <div class="profile-version">
        <p>FriendSync v1.0.0</p>
        <p>© 2023 FriendSync 保留所有權利</p>
      </div>

      <!-- 標籤選擇彈窗 -->
      <van-dialog
        v-model:show="showTagDialog"
        title="選擇標籤"
        :show-confirm-button="false"
        close-on-click-overlay
        class="tag-dialog"
      >
        <div class="tag-selector">
          <div v-if="loadingTags" class="tag-loading">
            <van-loading size="24px" vertical>加載中...</van-loading>
          </div>
          <div v-else>
            <p class="tag-tip">選擇適合您的標籤，點擊添加或移除</p>
            <div class="tag-list">
              <div
                v-for="tag in allTags"
                :key="tag.id"
                class="tag-item"
                :class="{ active: isTagSelected(tag) }"
                @click="toggleTagSelection(tag)"
              >
                {{ tag.tagname }}
                <van-icon
                  name="success"
                  v-if="isTagSelected(tag)"
                  class="tag-selected-icon"
                />
              </div>
            </div>
            <div
              class="selected-tag-preview"
              v-if="selectedTagNames.length > 0"
            >
              <p class="preview-title">已選標籤:</p>
              <div class="selected-tags">
                <div
                  v-for="(tagName, index) in selectedTagNames"
                  :key="index"
                  class="selected-tag"
                >
                  {{ tagName }}
                  <van-icon
                    name="cross"
                    @click.stop="selectedTagNames.splice(index, 1)"
                  />
                </div>
              </div>
            </div>
            <div class="tag-actions">
              <van-button
                type="default"
                size="small"
                @click="showTagDialog = false"
                >取消</van-button
              >
              <van-button type="primary" size="small" @click="saveSelectedTags"
                >保存</van-button
              >
            </div>
          </div>
        </div>
      </van-dialog>
    </template>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, computed, onMounted, watchEffect } from "vue";
// @ts-ignore
import { useUserStore } from "../stores/user";
// @ts-ignore
import { useRouter } from "vue-router";
// @ts-ignore
import { Toast } from "vant";
// @ts-ignore
import { logout } from "@/api/user";
// @ts-ignore
import { getCurrentUser } from "@/api/user";
// @ts-ignore
import { getAllTags, updateUserTags } from "@/api/tag";

const router = useRouter();
const userStore = useUserStore();
const userInfo = computed(() => userStore.userInfo);
const loading = ref(true);
const error = ref(false);

// 定義標籤類型
interface TagItem {
  name: string;
  value: number;
}

// 定義後端返回的標籤類型
interface TagData {
  id: number;
  tagname: string;
  createtime?: string;
  updatetime?: string;
  isdelete?: number;
}

// 標籤選擇相關
const showTagDialog = ref(false);
const allTags = ref<TagData[]>([]);
const selectedTagNames = ref<string[]>([]);
const loadingTags = ref(false);

// 用户標籤列表
const userTags = computed(() => {
  if (!userInfo.value?.tags) return [];
  try {
    // 嘗試解析JSON字符串
    const parsedTags = JSON.parse(userInfo.value.tags);
    console.log("解析到的標籤數據:", parsedTags);
    return parsedTags;
  } catch (err) {
    console.error("解析用户標籤失敗:", err);
    return [];
  }
});

// 獲取標籤用於展示
const displayTags = computed<TagItem[]>(() => {
  const tags = userTags.value;
  // 如果沒有標籤，返回默認標籤
  if (!tags || tags.length === 0) {
    return [{ name: "暫無標籤", value: 0 }];
  }

  console.log("準備展示的標籤:", tags);

  // 處理標籤數據，確保格式一致
  return tags.map((tag: any) => {
    // 如果標籤是字符串，轉換為對象格式
    if (typeof tag === "string") {
      return { name: tag, value: 0 };
    }
    // 如果已經是對象格式，保持不變
    if (typeof tag === "object" && tag !== null) {
      return {
        name: tag.name || tag.tagname || "未知標籤",
        value: tag.value || 0,
      };
    }
    return { name: "未知標籤", value: 0 };
  });
});

// 加載用户信息
const loadUserInfo = async () => {
  loading.value = true;
  error.value = false;

  try {
    console.log("ProfileView - 開始獲取用户信息");

    // 直接調用store中的fetchCurrentUser方法獲取最新用户信息
    const userData = await userStore.fetchCurrentUser();

    console.log("ProfileView - fetchCurrentUser返回結果:", userData);

    if (!userData) {
      error.value = true;
      console.error("ProfileView - 獲取用户信息失敗或返回為空");
      Toast.fail("獲取用户信息失敗，請嘗試重新登錄");
    } else {
      console.log("ProfileView - 成功獲取用户信息:", userData);
      // 靜默成功，不顯示提示
    }
  } catch (err) {
    console.error("ProfileView - 獲取用户信息出錯:", err);
    error.value = true;
    Toast.fail(
      "獲取用户信息出錯: " + (err instanceof Error ? err.message : String(err))
    );
  } finally {
    loading.value = false;
  }
};

// 格式化日期函數
const formatDate = (dateString?: string) => {
  if (!dateString) return "未設置";
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) {
      return "日期格式錯誤";
    }
    return date.toLocaleDateString("zh-CN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch (e) {
    console.error("日期格式化錯誤:", e);
    return "日期格式錯誤";
  }
};

// 退出登錄
const handleLogout = async () => {
  try {
    Toast.loading({
      message: "正在退出...",
      forbidClick: true,
      duration: 0,
    });

    // 調用後端退出登錄接口
    const response = await logout();
    console.log("退出登錄響應:", response);

    // 檢查響應是否成功
    if (response && response.data && response.data.code === 200) {
      // 清除前端狀態
      userStore.logout();

      Toast.clear();
      Toast.success("已退出登錄");

      // 跳轉到登錄頁
      router.replace("/login");
    } else {
      // 即使後端返回失敗，也清除前端狀態
      userStore.logout();

      Toast.clear();
      Toast.success("已退出登錄");

      // 跳轉到登錄頁
      router.replace("/login");
    }
  } catch (error) {
    console.error("退出登錄失敗", error);

    // 即使發生錯誤，也清除前端狀態
    userStore.logout();

    Toast.clear();
    Toast.success("已退出登錄");

    // 跳轉到登錄頁
    router.replace("/login");
  }
};

// 監聽用户狀態變化
watchEffect(() => {
  const currentUser = userStore.userInfo;
  console.log("ProfileView - 用户狀態變化:", currentUser ? "已登錄" : "未登錄");
});

// 頁面加載時加載用户信息
onMounted(() => {
  loadUserInfo();
});

// 顯示標籤選擇彈窗
const showTagSelector = async () => {
  try {
    showTagDialog.value = true;
    loadingTags.value = true;

    // 從後端獲取所有標籤
    const res = await getAllTags();
    if (
      res &&
      typeof res === "object" &&
      "code" in res &&
      res.code === 200 &&
      res.data
    ) {
      allTags.value = res.data;

      // 設置已選標籤
      const currentTags = userTags.value;
      if (currentTags && currentTags.length > 0) {
        // 如果標籤是對象格式，提取標籤名
        if (typeof currentTags[0] === "object" && "name" in currentTags[0]) {
          selectedTagNames.value = currentTags.map(
            (tag: { name: string }) => tag.name
          );
        }
        // 如果標籤是純字符串，直接使用
        else if (typeof currentTags[0] === "string") {
          selectedTagNames.value = [...currentTags];
        }
      }
    } else {
      Toast.fail("獲取標籤列表失敗");
    }
  } catch (error) {
    console.error("獲取標籤列表錯誤:", error);
    Toast.fail("獲取標籤列表失敗");
  } finally {
    loadingTags.value = false;
  }
};

// 保存選中的標籤
const saveSelectedTags = async () => {
  if (!userInfo.value?.id) {
    Toast.fail("用户未登錄");
    return;
  }

  try {
    Toast.loading({
      message: "保存中...",
      forbidClick: true,
    });

    console.log("準備保存標籤:", {
      id: userInfo.value.id,
      tags: selectedTagNames.value,
    });

    // 調用API保存用户標籤
    const result = await updateUserTags(
      userInfo.value.id,
      selectedTagNames.value
    );
    console.log("保存標籤結果:", result);

    // 檢查響應是否有效
    if (
      result &&
      typeof result === "object" &&
      "code" in result &&
      result.code === 200
    ) {
      // 重新獲取用户信息，更新標籤
      await loadUserInfo();

      Toast.success("標籤更新成功");
      showTagDialog.value = false;
    } else {
      const errorMsg =
        result && typeof result === "object" && "message" in result
          ? typeof result.message === "string"
            ? result.message
            : "保存失敗，請重試"
          : "保存失敗，請重試";
      Toast.fail(errorMsg);
    }
  } catch (error) {
    console.error("保存標籤失敗:", error);
    Toast.fail("保存標籤失敗");
  }
};

// 選擇或取消選擇標籤
const toggleTagSelection = (tag: TagData) => {
  const tagName = tag.tagname;
  const index = selectedTagNames.value.indexOf(tagName);
  if (index > -1) {
    // 取消選擇
    selectedTagNames.value.splice(index, 1);
  } else {
    // 選擇標籤
    selectedTagNames.value.push(tagName);
  }
};

// 判斷標籤是否已選中
const isTagSelected = (tag: TagData) => {
  return selectedTagNames.value.includes(tag.tagname);
};
</script>

<style scoped>
.profile-container {
  padding: 16px;
  background-color: #f7f8fa;
  min-height: 100vh;
  position: relative;
}

.loading-state,
.error-state {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  width: 80%;
}

.retry-btn {
  margin-top: 16px;
}

.profile-header {
  background: linear-gradient(135deg, #1976d2, #2196f3);
  border-radius: 12px;
  padding: 30px 16px;
  text-align: center;
  margin-bottom: 16px;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.2);
  color: white;
}

.profile-avatar {
  position: relative;
  width: 90px;
  height: 90px;
  margin: 0 auto 16px;
  cursor: pointer;
}

.profile-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.profile-name {
  font-size: 22px;
  font-weight: 600;
  margin: 0 0 4px;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.profile-id {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 16px;
}

.profile-stats {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 15px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 6px 12px;
  margin: 4px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  min-width: 80px;
}

.stat-value {
  font-size: 14px;
  font-weight: 500;
  color: white;
}

.stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  margin-top: 2px;
}

.profile-section {
  margin-bottom: 16px;
}

:deep(.van-cell-group__title) {
  font-size: 14px;
  color: #1976d2;
  font-weight: 500;
  padding: 8px 16px;
}

:deep(.van-cell-group--inset) {
  margin: 0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:deep(.van-cell) {
  font-size: 15px;
  padding: 14px 16px;
}

:deep(.van-cell__title) {
  flex: 2;
  color: #323233;
  font-weight: 500;
}

:deep(.van-cell__value) {
  flex: 3;
  color: #646566;
}

.logout-button-container {
  margin: 24px 0;
}

.logout-button {
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  background-color: #f56c6c;
  border: none;
}

.profile-version {
  text-align: center;
  font-size: 12px;
  color: #969799;
  margin-top: 32px;
}

.profile-version p {
  margin: 4px 0;
}

/* 標籤選擇器樣式 */
.tag-selector {
  padding: 16px;
}

.tag-loading {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

.tag-item {
  padding: 8px 12px;
  border-radius: 16px;
  background-color: #f5f5f5;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.tag-item.active {
  background-color: #1989fa;
  color: white;
}

.tag-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.tag-tip {
  font-size: 14px;
  color: #646566;
  margin-bottom: 16px;
}

.selected-tag-preview {
  margin-bottom: 16px;
}

.preview-title {
  font-size: 14px;
  color: #323233;
  font-weight: 500;
  margin-bottom: 8px;
}

.selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.selected-tag {
  display: flex;
  align-items: center;
  padding: 6px 12px;
  border-radius: 16px;
  background-color: #e8f3ff;
  color: #1989fa;
  font-size: 14px;
  transition: all 0.3s;
}

.selected-tag .van-icon {
  margin-left: 4px;
  color: #1989fa;
  cursor: pointer;
}

.tag-selected-icon {
  margin-left: 8px;
}
</style>
