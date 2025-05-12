<template>
  <div class="team-container">
    <van-search
      v-model="searchText"
      placeholder="搜索團隊/聊天室"
      shape="round"
      background="#f7f8fa"
      show-action
      @search="handleSearch"
      @clear="clearSearch"
    >
      <template #action>
        <div class="search-action" @click="handleSearch">搜索</div>
      </template>
    </van-search>

    <div v-if="isSearching" class="search-loading">
      <Loading color="#1989fa" />
      <span>搜索中...</span>
    </div>

    <div v-else-if="searchResults.length > 0" class="search-results">
      <section class="section-header">
        <h2 class="section-title">搜索結果</h2>
        <span class="clear-link" @click="clearSearch">清除</span>
      </section>

      <div class="team-cards">
        <div
          v-for="team in searchResults"
          :key="team.id"
          class="team-card"
          @click="onTeamClick(team)"
        >
          <div
            class="team-logo"
            :style="{ backgroundColor: getTeamColor(team.type) }"
          >
            <van-icon :name="getTeamIcon(team.type)" color="#fff" size="24" />
          </div>
          <div class="team-info">
            <h3>{{ team.name }}</h3>
            <p class="team-desc" v-if="team.information">
              {{ team.information }}
            </p>
            <div class="team-type-badge" :class="team.type.toLowerCase()">
              {{
                team.type === "TEAM"
                  ? "加密團隊"
                  : team.type === "ROOM"
                  ? "公開聊天室"
                  : "私聊"
              }}
            </div>
          </div>
          <div class="team-action">
            <template v-if="isTeamJoined(team.id)">
              <van-icon name="arrow" color="#c8c9cc" />
            </template>
            <template v-else>
              <van-button
                size="small"
                type="primary"
                class="join-btn"
                @click.stop="handleJoinTeam(team)"
              >
                加入
              </van-button>
            </template>
          </div>
        </div>
      </div>
    </div>

    <template v-else>
      <section class="section-header">
        <h2 class="section-title">我創建的團隊</h2>
        <van-button
          round
          size="small"
          type="primary"
          color="#1989fa"
          icon="plus"
          @click="showCreateTeamDialog"
          >創建</van-button
        >
      </section>

      <div v-if="teams.length === 0" class="empty-state">
        <van-empty description="您還沒有創建過團隊，點擊上方創建按鈕開始創建" />
      </div>

      <div v-else class="team-cards">
        <div v-for="team in teams" :key="team.id" class="team-card">
          <div
            class="team-logo"
            :style="{ backgroundColor: team.color }"
            @click="onTeamClick(team)"
          >
            <van-icon :name="team.icon" color="#fff" size="24" />
          </div>
          <div class="team-info" @click="onTeamClick(team)">
            <h3>{{ team.name }}</h3>
            <p class="team-desc" v-if="team.information">
              {{ team.information }}
            </p>
            <div class="team-type-badge" :class="team.type.toLowerCase()">
              {{
                team.type === "TEAM"
                  ? "加密團隊"
                  : team.type === "ROOM"
                  ? "公開聊天室"
                  : "私聊"
              }}
            </div>
          </div>
          <div class="team-action">
            <van-button
              size="small"
              type="danger"
              plain
              class="action-btn delete-btn"
              @click.stop="handleDeleteTeam(team)"
            >
              解散
            </van-button>
          </div>
        </div>
      </div>
    </template>

    <!-- 創建團隊對話框 -->
    <van-dialog
      v-model:show="showDialog"
      title="創建團隊/聊天室"
      show-cancel-button
      :before-close="beforeDialogClose"
      @cancel="onDialogCancel"
    >
      <div class="dialog-content">
        <van-field
          v-model="teamForm.name"
          label="團隊名稱"
          placeholder="請輸入團隊名稱"
          :rules="[{ required: true, message: '請填寫團隊名稱' }]"
        />
        <van-field
          v-model="teamForm.information"
          label="團隊介紹"
          type="textarea"
          placeholder="請輸入團隊介紹"
          :rules="[{ required: true, message: '請填寫團隊介紹' }]"
          :autosize="{ minHeight: 60, maxHeight: 120 }"
        />

        <div class="form-item">
          <div class="form-label">訪問類型</div>
          <div class="access-options">
            <div
              class="access-option"
              :class="{ active: teamForm.type === 'ROOM' }"
              @click="selectAccessType('ROOM')"
            >
              <van-icon
                name="friends-o"
                size="20"
                :color="teamForm.type === 'ROOM' ? '#07c160' : '#969799'"
              />
              <div class="option-content">
                <div class="option-title">公開聊天室</div>
                <div class="option-desc">所有人可以自由加入，適合公開討論</div>
              </div>
            </div>

            <div
              class="access-option"
              :class="{ active: teamForm.type === 'TEAM' }"
              @click="selectAccessType('TEAM')"
            >
              <van-icon
                name="shield-o"
                size="20"
                :color="teamForm.type === 'TEAM' ? '#1989fa' : '#969799'"
              />
              <div class="option-content">
                <div class="option-title">加密團隊</div>
                <div class="option-desc">需要密碼才能加入，適合私密交流</div>
              </div>
            </div>
          </div>
        </div>

        <van-field
          v-if="teamForm.type === 'TEAM'"
          v-model="teamForm.password"
          label="訪問密碼"
          type="password"
          placeholder="請設置訪問密碼"
          :rules="[{ required: true, message: '請設置訪問密碼' }]"
        />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <van-button plain round size="small" @click="onDialogCancel"
            >取消</van-button
          >
          <van-button
            type="primary"
            round
            size="small"
            :loading="loading"
            @click="handleCreateTeam"
          >
            確定
          </van-button>
        </div>
      </template>
    </van-dialog>

    <!-- 密碼輸入對話框 -->
    <van-dialog
      v-model:show="showPasswordDialog"
      title="加入加密團隊"
      show-cancel-button
      @cancel="showPasswordDialog = false"
    >
      <div class="dialog-content">
        <van-field
          v-model="teamPassword"
          label="密碼"
          type="password"
          placeholder="請輸入團隊密碼"
          :rules="[{ required: true, message: '請輸入團隊密碼' }]"
        />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <van-button
            plain
            round
            size="small"
            @click="showPasswordDialog = false"
            >取消</van-button
          >
          <van-button
            type="primary"
            round
            size="small"
            :loading="loading"
            @click="confirmJoinTeam"
          >
            確定
          </van-button>
        </div>
      </template>
    </van-dialog>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, watch, onMounted } from "vue";
// @ts-ignore
import { useRouter } from "vue-router";
// @ts-ignore
import { Toast, Loading, Dialog } from "vant";
// @ts-ignore
import {
  createTeamRoom,
  ConversationType,
  searchConversations,
  getOwnConversations,
  getJoinedConversations,
  joinPublicRoom,
  joinTeamWithLicense,
  deleteConversation,
} from "@/api/conversation";
// @ts-ignore
import { useUserStore } from "@/stores/user";
import http from "@/api/http";

// 定義會話類型接口
interface Conversation {
  id: number;
  type: string;
  ownerId: number;
  name: string;
  information: string;
  license: string | null;
  createTime?: string;
}

// 定義團隊表單類型
interface TeamForm {
  name: string;
  information: string;
  type: string;
  isPrivate: boolean;
  password: string;
}

const router = useRouter();
const userStore = useUserStore();
const searchText = ref("");
const showDialog = ref(false);
const loading = ref(false);
const isSearching = ref(false);
const searchResults = ref<Conversation[]>([]);
// 存儲用户是否已加入會話的狀態
const joinedTeamIds = ref<number[]>([]);
// 添加密碼輸入框變量
const teamPassword = ref("");
const showPasswordDialog = ref(false);
const currentTeam = ref<Conversation | null>(null);

// 團隊表單
const teamForm = ref<TeamForm>({
  name: "",
  information: "",
  type: "ROOM",
  isPrivate: false,
  password: "",
});

// 選擇訪問類型
function selectAccessType(type: string) {
  teamForm.value.type = type;
  teamForm.value.isPrivate = type === "TEAM";

  // 如果切換到公開，清空密碼
  if (type === "ROOM") {
    teamForm.value.password = "";
  }
}

// 監聽類型變化，用於更新UI
watch(
  () => teamForm.value.type,
  (newType: string) => {
    console.log("當前選擇的類型:", newType);
  }
);

// 我的團隊數據
const teams = ref<
  {
    id: number;
    name: string;
    membersCount: number;
    icon: string;
    color: string;
    information: string;
    ownerId: number;
    isOwner: boolean;
    type: string;
  }[]
>([]);

// 加載用户創建的會話
async function loadOwnTeams() {
  loading.value = true;

  try {
    // 獲取當前用户ID
    const userId = userStore.userInfo?.id;

    if (!userId) {
      console.warn("當前用户ID不存在, 可能未登錄");
      Toast.fail("請先登錄");
      setTimeout(() => {
        router.push("/login");
      }, 1500);
      loading.value = false;
      return;
    }

    // 調用獲取用户創建的會話接口，傳遞用户ID
    const response = await getOwnConversations(userId);
    console.log("獲取用户創建的團隊響應:", response);

    let conversationList: any[] = [];

    if (Array.isArray(response)) {
      conversationList = response;
    } else if (response && Array.isArray(response.data)) {
      conversationList = response.data;
    } else {
      console.warn("未收到預期的團隊列表格式:", response);
      conversationList = [];
    }

    // 處理團隊數據，為每個團隊添加UI相關屬性
    const transformedTeams = conversationList.map((team) => {
      // 用户創建的會話，所有條目都標記為創建者
      const isOwner = true;

      // 根據類型設置圖標
      const icon = getTeamIcon(team.type);

      // 生成隨機顏色
      const color = getTeamColor(team.type);

      return {
        ...team,
        isOwner,
        icon,
        color,
      };
    });

    teams.value = transformedTeams;
  } catch (error) {
    console.error("獲取用户創建的團隊失敗", error);
    teams.value = [];
  } finally {
    loading.value = false;
  }
}

// 加載用户已加入的會話ID列表
async function loadJoinedTeamIds() {
  try {
    const response = await getJoinedConversations();

    if (Array.isArray(response)) {
      // 提取已加入會話的ID
      joinedTeamIds.value = response.map((item) => item.id);
      console.log("已加入的會話ID:", joinedTeamIds.value);
    } else {
      console.warn("獲取已加入會話列表格式異常:", response);
      joinedTeamIds.value = [];
    }
  } catch (error) {
    console.error("獲取已加入會話ID失敗", error);
    joinedTeamIds.value = [];
  }
}

// 修改onMounted調用，同時加載創建的會話和已加入的會話ID
onMounted(() => {
  loadOwnTeams();
  loadJoinedTeamIds();
});

// 顯示創建團隊對話框
function showCreateTeamDialog() {
  // 重置表單
  teamForm.value = {
    name: "",
    information: "",
    type: "ROOM",
    isPrivate: false,
    password: "",
  };
  showDialog.value = true;
}

// 取消對話框
function onDialogCancel() {
  showDialog.value = false;
}

// 對話框關閉前的驗證
function beforeDialogClose(action: string, done: () => void) {
  // 如果正在加載，阻止關閉
  if (loading.value) {
    return false;
  }

  if (action === "confirm") {
    // 驗證表單
    if (!teamForm.value.name) {
      Toast("請輸入團隊名稱");
      return false;
    } else if (!teamForm.value.information) {
      Toast("請輸入團隊介紹");
      return false;
    } else if (teamForm.value.type === "TEAM" && !teamForm.value.password) {
      Toast("請設置訪問密碼");
      return false;
    }
  }

  done();
}

// 處理創建團隊
async function handleCreateTeam() {
  // 表單驗證
  if (!teamForm.value.name) {
    Toast("請輸入團隊名稱");
    return;
  } else if (!teamForm.value.information) {
    Toast("請輸入團隊介紹");
    return;
  } else if (teamForm.value.type === "TEAM" && !teamForm.value.password) {
    Toast("請設置訪問密碼");
    return;
  }

  loading.value = true;

  try {
    Toast.loading({
      message: "創建中...",
      forbidClick: true,
      duration: 0,
    });

    // 獲取當前用户ID
    const userId = userStore.userInfo?.id;

    if (!userId) {
      console.warn("當前用户ID不存在，可能未登錄");
      Toast.clear();
      Toast.fail("請先登錄");
      setTimeout(() => {
        router.push("/login");
      }, 1500);
      loading.value = false;
      return;
    }

    // 轉換為後端需要的類型
    const conversationType = teamForm.value.type as ConversationType;

    // 獲取密碼參數
    const license =
      teamForm.value.type === "TEAM" ? teamForm.value.password : undefined;

    // 調用創建團隊API
    const response = await createTeamRoom(
      teamForm.value.name,
      teamForm.value.information,
      conversationType,
      license
    );

    console.log("創建團隊響應:", response);

    // 檢查響應是否有效
    let success = false;

    // 嘗試從不同結構中獲取響應數據
    if (response && typeof response === "object") {
      if ("id" in response) {
        success = true;
      } else if (
        response.data &&
        typeof response.data === "object" &&
        "id" in response.data
      ) {
        success = true;
      } else {
        console.warn("創建成功但返回數據格式異常:", response);
        // 假設請求已成功
        success = true;
      }
    }

    if (response) {
      // 刷新創建的團隊列表
      await loadOwnTeams();

      // 關閉加載提示和對話框
      Toast.clear();
      Toast.success("創建成功");
      showDialog.value = false;
    } else {
      Toast.clear();
      Toast.fail("創建失敗，返回數據無效");
    }
  } catch (error) {
    console.error("創建團隊失敗", error);
    Toast.clear();

    // 獲取錯誤詳情
    const axiosError = error as any;
    if (axiosError.response) {
      const status = axiosError.response.status;
      const errorData = axiosError.response.data;

      if (status === 400) {
        // 請求參數錯誤
        let errorMsg = "請求參數錯誤";
        if (errorData && errorData.message) {
          errorMsg = errorData.message;
        } else if (errorData && errorData.description) {
          errorMsg = errorData.description;
        }
        Toast.fail(errorMsg);
      } else if (status === 401) {
        // 未登錄或token過期
        Toast.fail("登錄已過期，請重新登錄");
        setTimeout(() => {
          router.push("/login");
        }, 1500);
      } else {
        Toast.fail("創建失敗，請稍後再試");
      }
    } else {
      Toast.fail("創建失敗，請稍後再試");
    }
  } finally {
    loading.value = false;
  }
}

// 生成隨機顏色
function getRandomColor() {
  const colors = [
    "#1989fa",
    "#FF6B35",
    "#6DD3E0",
    "#7367F0",
    "#28C76F",
    "#EA5455",
  ];
  return colors[Math.floor(Math.random() * colors.length)];
}

// 點擊團隊
function onTeamClick(team: {
  id: number;
  name?: string;
  information?: string;
  type?: string;
  isOwner?: boolean;
  ownerId?: number;
}) {
  // 跳轉到聊天室頁面，同時傳遞會話相關信息
  if (isTeamJoined(team.id)) {
    // User is already a member, allow direct access
    router.push({
      path: `/team/${team.id}`,
      query: {
        name: team.name,
        information: team.information,
        type: team.type,
        ownerId: team.ownerId?.toString(),
      },
    });
  } else {
    // User is not a member, handle according to team type
    if (team.type === "TEAM") {
      // For private teams, show the password dialog
      currentTeam.value = {
        id: team.id,
        name: team.name || "",
        information: team.information || "",
        type: team.type || "",
        ownerId: team.ownerId || 0,
        license: null, // Add the missing license field
      };
      teamPassword.value = ""; // Clear previous input
      showPasswordDialog.value = true;
    } else if (team.type === "ROOM") {
      // For public rooms, use the join function
      handleJoinTeam(team as any);
    } else {
      // For other types (like CHAT)
      Toast("無法訪問此會話");
    }
  }
}

// 處理搜索
async function handleSearch() {
  if (!searchText.value.trim()) {
    return;
  }

  isSearching.value = true;

  try {
    const response = await searchConversations(searchText.value.trim());
    console.log("搜索響應:", response);

    if (Array.isArray(response)) {
      searchResults.value = response;
      if (response.length === 0) {
        Toast("未找到匹配的團隊或聊天室");
      }
    } else {
      searchResults.value = [];
      Toast("未找到匹配的團隊或聊天室");
    }
  } catch (error) {
    console.error("搜索失敗", error);
    Toast.fail("搜索失敗，請稍後再試");
    searchResults.value = [];
  } finally {
    isSearching.value = false;
  }
}

// 清除搜索
function clearSearch() {
  searchText.value = "";
  searchResults.value = [];
}

// 獲取團隊顏色
function getTeamColor(type: string) {
  switch (type) {
    case "TEAM":
      return "#1989fa"; // 加密團隊使用藍色，表示安全
    case "ROOM":
      return "#07c160"; // 公共聊天室使用綠色，表示公開
    case "CHAT":
      return "#ff976a"; // 私聊使用橙色，表示個人
    default:
      return "#969799"; // 默認使用灰色
  }
}

// 獲取團隊圖標
function getTeamIcon(type: string) {
  switch (type) {
    case "TEAM":
      return "shield-o"; // 加密團隊使用盾牌圖標，表示安全性
    case "ROOM":
      return "friends-o"; // 公共聊天室使用好友圖標，表示羣體交流
    case "CHAT":
      return "chat-o"; // 私聊使用聊天氣泡圖標，表示一對一交流
    default:
      return "comment-o"; // 默認使用評論圖標
  }
}

// 修改搜索結果組件，添加是否已加入的判斷
function isTeamJoined(teamId: number): boolean {
  return joinedTeamIds.value.includes(teamId);
}

// 處理加入團隊
async function handleJoinTeam(team: Conversation) {
  // 已加入的團隊直接進入聊天頁面
  if (isTeamJoined(team.id)) {
    router.push(`/team/${team.id}`);
    return;
  }

  try {
    if (team.type === "TEAM") {
      // 團隊類型需要輸入密碼 - 顯示密碼輸入對話框
      currentTeam.value = team;
      teamPassword.value = ""; // 清空上次輸入
      showPasswordDialog.value = true;
    } else {
      // 聊天室類型不需要密碼
      Toast.loading({
        message: "加入中...",
        forbidClick: true,
        duration: 0,
      });

      const result = await joinPublicRoom(team.id);

      if (result) {
        Toast.clear();
        Toast.success("加入成功");

        // 添加到已加入列表
        joinedTeamIds.value.push(team.id);

        // 刷新已加入會話ID列表
        await loadJoinedTeamIds();

        // 加入成功後跳轉到團隊聊天頁面
        setTimeout(() => {
          if (currentTeam.value) {
            router.push(`/team/${currentTeam.value.id}`);
          }
        }, 500);
      }
    }
  } catch (error) {
    console.error("加入失敗", error);
    Toast.clear();
    Toast.fail("加入失敗，請稍後再試");
  }
}

// 確認加入加密團隊
async function confirmJoinTeam() {
  if (!teamPassword.value) {
    Toast("密碼不能為空");
    return;
  }

  if (!currentTeam.value) {
    return;
  }

  try {
    // 顯示加載中
    Toast.loading({
      message: "加入中...",
      forbidClick: true,
      duration: 0,
    });

    // 調用加入團隊API
    const result = await joinTeamWithLicense(
      currentTeam.value.id,
      teamPassword.value
    );

    if (result) {
      Toast.clear();
      Toast.success("加入成功");

      // 添加到已加入列表
      joinedTeamIds.value.push(currentTeam.value.id);

      // 刷新已加入會話ID列表
      await loadJoinedTeamIds();

      // 關閉密碼輸入對話框
      showPasswordDialog.value = false;

      // 加入成功後跳轉到團隊聊天頁面
      setTimeout(() => {
        if (currentTeam.value) {
          router.push(`/team/${currentTeam.value.id}`);
        }
      }, 500);
    }
  } catch (error) {
    console.error("加入團隊失敗", error);
    Toast.clear();
    Toast.fail("加入失敗，密碼可能不正確");
  }
}

// 處理刪除團隊
async function handleDeleteTeam(team: {
  id: number;
  name: string;
  ownerId: number;
}) {
  try {
    Dialog.confirm({
      title: "解散團隊",
      message: `確定要解散"${team.name}"嗎？此操作不可撤銷。`,
      confirmButtonText: "解散",
      confirmButtonColor: "#ee0a24",
    })
      .then(async () => {
        Toast.loading({
          message: "刪除中...",
          forbidClick: true,
          duration: 0,
        });

        // 獲取當前用户ID
        const userId = userStore.userInfo?.id;

        if (!userId) {
          Toast.clear();
          Toast.fail("用户未登錄，請先登錄");
          return;
        }

        // 調用刪除API，只傳遞會話ID
        const response = await deleteConversation(team.id);

        Toast.clear();

        // 判斷響應是否成功
        const isSuccess =
          (typeof response === "string" && response === "success") ||
          (response &&
            typeof response === "object" &&
            (response.data === "success" || response.status === 200));

        if (isSuccess) {
          Toast.success("已成功解散");
          // 刷新創建的團隊列表
          await loadOwnTeams();
        } else {
          Toast.fail("解散失敗，請稍後再試");
        }
      })
      .catch(() => {
        // 用户取消，不做處理
      });
  } catch (error) {
    console.error("刪除失敗", error);
    Toast.clear();
    Toast.fail("刪除失敗，請稍後再試");
  }
}
</script>

<style scoped>
.team-container {
  padding: 16px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 24px 0 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #323233;
  margin: 0;
}

.more-link {
  font-size: 14px;
  color: #1989fa;
}

.team-cards {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.team-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
}

.team-logo {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.team-info {
  flex: 1;
}

.team-info h3 {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
  margin: 0 0 4px;
}

.team-info p {
  font-size: 12px;
  color: #969799;
  margin: 0;
}

.team-swipe {
  margin: 0 -16px;
}

.recommended-team-card {
  width: 280px;
  margin: 8px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background-color: #fff;
}

.recommended-team-header {
  height: 100px;
  position: relative;
}

.team-logo-large {
  position: absolute;
  bottom: -20px;
  left: 20px;
  width: 56px;
  height: 56px;
  border-radius: 16px;
  background-color: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.recommended-team-content {
  padding: 30px 20px 20px;
}

.recommended-team-content h3 {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
  margin: 0 0 8px;
}

.recommended-team-content p {
  font-size: 13px;
  color: #646566;
  margin: 0 0 12px;
  height: 38px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.member-count {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #969799;
  margin-bottom: 16px;
}

.join-btn {
  width: 100%;
  background-color: #1989fa;
  color: #fff;
  border: none;
}

.dialog-content {
  padding: 16px 0;
}

.form-item {
  padding: 10px 16px;
}

.form-label {
  font-size: 15px;
  margin-bottom: 8px;
  color: #323233;
}

.access-options {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.access-option {
  flex: 1;
  display: flex;
  align-items: flex-start;
  padding: 12px;
  border: 1px solid #ebedf0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.access-option.active {
  border-color: #1989fa;
  background-color: rgba(25, 137, 250, 0.05);
}

.access-option .van-icon {
  margin-right: 8px;
  margin-top: 2px;
}

.option-content {
  flex: 1;
}

.option-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
  color: #323233;
}

.option-desc {
  font-size: 12px;
  color: #969799;
  line-height: 1.3;
}

.access-option.active .option-title {
  color: #1989fa;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  padding: 0 16px 16px;
}

.dialog-footer .van-button {
  min-width: 100px;
}

.search-action {
  font-size: 14px;
  color: #1989fa;
  padding: 0 10px;
}

.search-loading {
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  color: #969799;
  font-size: 14px;
}

.clear-link {
  font-size: 14px;
  color: #1989fa;
  cursor: pointer;
}

.team-desc {
  font-size: 12px;
  color: #646566;
  margin: 4px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}

.team-type-badge {
  display: inline-block;
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 8px;
  margin-top: 4px;
}

.team-type-badge.team {
  background-color: rgba(25, 137, 250, 0.1);
  color: #1989fa;
}

.team-type-badge.room {
  background-color: rgba(255, 107, 53, 0.1);
  color: #ff6b35;
}

.team-type-badge.chat {
  background-color: rgba(255, 151, 106, 0.1);
  color: #ff976a;
}

.action-btn {
  font-size: 12px;
  height: 28px;
  padding: 0 10px;
}

.delete-btn {
  --van-button-danger-color: #ee0a24;
  --van-button-plain-background-color: transparent;
}

/* 添加空狀態樣式 */
.empty-state {
  padding: 30px 0;
  background-color: #fff;
  border-radius: 12px;
  margin-bottom: 20px;
}
</style>
