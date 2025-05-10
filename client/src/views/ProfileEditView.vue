<template>
  <div class="profile-edit-container">
    
    <van-form @submit="onSubmit" class="edit-form">
      <div class="avatar-upload">
        <div class="avatar-preview">
          <img :src="form.avatar || 'https://img01.yzcdn.cn/vant/cat.jpeg'" alt="頭像">
        </div>
        <van-uploader :after-read="afterRead" :max-count="1" class="avatar-uploader">
          <van-button icon="photograph" type="primary" size="small">更換頭像</van-button>
        </van-uploader>
      </div>
      
      <van-cell-group inset>
        <van-field
          v-model="form.nickname"
          name="暱稱"
          label="暱稱"
          placeholder="請輸入暱稱"
          :rules="[{ required: true, message: '請填寫暱稱' }]"
        />
        <van-field
          v-model="form.bio"
          name="簡介"
          label="個人簡介"
          placeholder="介紹一下自己吧"
          type="textarea"
          rows="3"
          autosize
        />
        <van-field
          v-model="form.email"
          name="郵箱"
          label="郵箱"
          placeholder="請輸入郵箱"
          readonly
          :rules="[{ required: true, message: '請填寫郵箱' }]"
        >
          <template #right-icon>
            <van-button size="small" type="primary" @click="showEmailChangeDialog = true">修改</van-button>
          </template>
        </van-field>
        <van-field
          v-model="form.phone"
          name="手機號"
          label="手機號"
          placeholder="請輸入手機號"
          readonly
        >
          <template #right-icon>
            <van-button size="small" type="primary" @click="showPhoneChangeDialog = true">修改</van-button>
          </template>
        </van-field>
      </van-cell-group>
      
      <div class="submit-btn-wrapper">
        <van-button round block type="primary" native-type="submit" :loading="loading">
          保存修改
        </van-button>
      </div>
    </van-form>
    
    <!-- 修改郵箱對話框 -->
    <van-dialog
      v-model:show="showEmailChangeDialog"
      title="修改郵箱"
      show-cancel-button
      @confirm="confirmEmailChange"
    >
      <van-field
        v-model="newEmail"
        label="新郵箱"
        placeholder="請輸入新郵箱"
        :rules="[
          { required: true, message: '請填寫郵箱' },
          { pattern: emailPattern, message: '請輸入正確的郵箱地址' }
        ]"
      />
    </van-dialog>
    
    <!-- 修改手機對話框 -->
    <van-dialog
      v-model:show="showPhoneChangeDialog"
      title="修改手機號"
      show-cancel-button
      @confirm="confirmPhoneChange"
    >
      <van-field
        v-model="newPhone"
        label="新手機號"
        placeholder="請輸入新手機號"
        :rules="[
          { required: true, message: '請填寫手機號' },
          { pattern: phonePattern, message: '請輸入正確的手機號' }
        ]"
      />
    </van-dialog>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, reactive, onMounted } from 'vue'
// @ts-ignore
import { Toast } from 'vant'
// @ts-ignore
import { useRouter } from 'vue-router'
// @ts-ignore
import { useUserStore } from '@/stores/user'
// @ts-ignore
import { updateUserInfo } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const showEmailChangeDialog = ref(false)
const showPhoneChangeDialog = ref(false)
const newEmail = ref('')
const newPhone = ref('')

// 表單數據
const form = reactive({
  nickname: '',
  avatar: '',
  bio: '',
  email: '',
  phone: ''
})

// 驗證規則
const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/
const phonePattern = /^1[3-9]\d{9}$/

// 初始化表單數據
onMounted(() => {
  if (userStore.userInfo) {
    form.nickname = userStore.userInfo.nickname
    form.avatar = userStore.userInfo.avatar
    form.bio = userStore.userInfo.bio
    form.email = userStore.userInfo.email
    form.phone = userStore.userInfo.phone
  }
})

// 返回上一頁
const goBack = () => {
  router.back()
}

// 上傳頭像後的處理
const afterRead = (file: any) => {
  // 實際開發中應該上傳到服務器，這裏僅做示例
  form.avatar = file.content
  Toast.success('頭像上傳成功')
}

// 確認修改郵箱
const confirmEmailChange = () => {
  if (emailPattern.test(newEmail.value)) {
    form.email = newEmail.value
    Toast.success('郵箱已更新')
  } else {
    Toast.fail('郵箱格式不正確')
  }
}

// 確認修改手機號
const confirmPhoneChange = () => {
  if (phonePattern.test(newPhone.value)) {
    form.phone = newPhone.value
    Toast.success('手機號已更新')
  } else {
    Toast.fail('手機號格式不正確')
  }
}

// 提交表單
const onSubmit = async () => {
  loading.value = true
  try {
    // 調用更新用户信息API
    await updateUserInfo({
      nickname: form.nickname,
      avatar: form.avatar,
      bio: form.bio,
      // 注意：實際API根據後端要求可能需要調整字段
    })
    
    // 更新本地用户信息
    if (userStore.userInfo) {
      userStore.setUserInfo({
        ...userStore.userInfo,
        nickname: form.nickname,
        avatar: form.avatar,
        bio: form.bio,
        email: form.email,
        phone: form.phone
      })
    }
    
    Toast.success('資料已更新')
    router.back()
  } catch (error) {
    console.error('更新用户信息失敗', error)
    Toast.fail('更新失敗，請重試')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.profile-edit-container {
  min-height: 100vh;
  background-color: #f7f8fa;
}

:deep(.van-nav-bar) {
  background-color: #ffffff;
  height: 56px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

:deep(.van-nav-bar__title) {
  color: #323233;
  font-size: 18px;
  font-weight: 600;
}

:deep(.van-nav-bar__text) {
  color: #1989fa;
  font-size: 15px;
}

:deep(.van-icon-arrow-left) {
  color: #1989fa !important;
}

.edit-form {
  padding: 20px 16px;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.avatar-preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin-bottom: 16px;
  border: 3px solid #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

:deep(.van-cell) {
  padding: 16px;
  font-size: 15px;
}

:deep(.van-cell-group--inset) {
  margin: 0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:deep(.van-field__label) {
  width: 80px;
  color: #323233;
  font-weight: 500;
}

.submit-btn-wrapper {
  margin-top: 24px;
  padding: 0 16px;
}

:deep(.van-button--primary) {
  background-color: #1989fa;
  border: none;
}
</style> 