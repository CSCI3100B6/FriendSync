<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import {
  showSuccessToast,
  showFailToast,
  showLoadingToast,
  showDialog,
} from "vant";

const router = useRouter();
const loading = ref(false);
const fileList = ref([]);

// User profile data
const userInfo = reactive({
  id: 0,
  username: "",
  userAccount: "",
  avatarUrl: "",
  gender: 0, // 0-unknown, 1-male, 2-female
  phone: "",
  email: "",
  bio: "",
  tags: [],
});

// Get user info from backend
const fetchUserInfo = async () => {
  try {
    // First try to get from localStorage
    const localUserInfo = localStorage.getItem("userInfo");

    if (localUserInfo) {
      const parsedInfo = JSON.parse(localUserInfo);
      Object.assign(userInfo, parsedInfo);
    }

    // Then fetch the latest data from the server
    const res = await fetch("/api/user/current", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const result = await res.json();

    if (result.code === 0 && result.data) {
      // Update form values with user data
      Object.assign(userInfo, result.data);

      // Update avatar file list if exists
      if (userInfo.avatarUrl) {
        fileList.value = [{ url: userInfo.avatarUrl }];
      }

      // Store updated user info
      localStorage.setItem("userInfo", JSON.stringify(result.data));
    } else {
      showFailToast("获取用户信息失败");
    }
  } catch (error) {
    console.error("Failed to fetch user info", error);
    showFailToast("获取用户信息失败");
  }
};

// Handle form submission
const onSubmit = async () => {
  loading.value = true;
  showLoadingToast({
    message: "更新中...",
    forbidClick: true,
  });

  try {
    const res = await fetch("/api/user/update", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userInfo),
    });

    const result = await res.json();

    if (result.code === 0) {
      showSuccessToast("个人信息更新成功");

      // Update localStorage with new info
      localStorage.setItem("userInfo", JSON.stringify(userInfo));
    } else {
      showFailToast(result.message || "更新失败");
    }
  } catch (error) {
    console.error("Failed to update user info", error);
    showFailToast("更新失败，请稍后再试");
  } finally {
    loading.value = false;
  }
};

// Handle avatar upload
const afterRead = async (file) => {
  // Show loading
  showLoadingToast({
    message: "上传中...",
    forbidClick: true,
  });

  try {
    // Create form data for file upload
    const formData = new FormData();
    formData.append("file", file.file);

    // Upload file to your backend or a cloud service
    const res = await fetch("/api/file/upload", {
      method: "POST",
      body: formData,
    });

    const result = await res.json();

    if (result.code === 0 && result.data) {
      // Update avatar URL
      userInfo.avatarUrl = result.data;
      showSuccessToast("头像上传成功");
    } else {
      showFailToast(result.message || "头像上传失败");
    }
  } catch (error) {
    console.error("Failed to upload avatar", error);
    showFailToast("头像上传失败，请稍后再试");
  }
};

// Logout function
const handleLogout = () => {
  showDialog({
    title: "确认退出",
    message: "您确定要退出登录吗？",
    showCancelButton: true,
  }).then((action) => {
    if (action === "confirm") {
      // Clear user info from localStorage
      localStorage.removeItem("userInfo");

      // Redirect to login page
      router.push("/login");

      showSuccessToast("已退出登录");
    }
  });
};

// Load user info when component mounts
onMounted(fetchUserInfo);
</script>

<template>
  <div class="profile-container">
    <!-- <van-nav-bar
      title="个人信息"
      left-text="返回"
      left-arrow
      @click-left="router.back()"
    /> -->

    <div class="avatar-container">
      <van-uploader v-model="fileList" :max-count="1" :after-read="afterRead">
        <div class="avatar-wrapper">
          <img
            v-if="userInfo.avatarUrl"
            :src="userInfo.avatarUrl"
            class="avatar"
            alt="用户头像"
          />
          <van-icon v-else name="user-circle-o" size="64" />
          <div class="avatar-edit">点击更换</div>
        </div>
      </van-uploader>
    </div>

    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="userInfo.username"
          name="username"
          label="昵称"
          placeholder="请输入昵称"
          :rules="[{ required: true, message: '请输入昵称' }]"
        />

        <van-field name="gender" label="性别">
          <template #input>
            <van-radio-group v-model="userInfo.gender" direction="horizontal">
              <van-radio :name="1">男</van-radio>
              <van-radio :name="2">女</van-radio>
              <van-radio :name="0">不公开</van-radio>
            </van-radio-group>
          </template>
        </van-field>

        <van-field
          v-model="userInfo.phone"
          name="phone"
          label="手机号"
          placeholder="请输入手机号"
        />

        <van-field
          v-model="userInfo.email"
          name="email"
          label="邮箱"
          placeholder="请输入邮箱"
          :rules="[{ pattern: /.+@.+\..+/, message: '请输入有效的邮箱地址' }]"
        />

        <van-field
          v-model="userInfo.bio"
          rows="3"
          autosize
          label="个人简介"
          type="textarea"
          placeholder="请输入个人简介"
        />
      </van-cell-group>

      <div style="margin: 16px">
        <van-button
          round
          block
          type="primary"
          native-type="submit"
          :loading="loading"
        >
          保存修改
        </van-button>

        <van-button
          round
          block
          type="danger"
          style="margin-top: 12px"
          @click="handleLogout"
        >
          退出登录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<style scoped>
.profile-container {
  padding-bottom: 20px;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin: 30px 0;
}

.avatar-wrapper {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #f8f8f8;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-edit {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 12px;
  padding: 4px 0;
  text-align: center;
}

.van-radio {
  margin-right: 12px;
}
</style>
