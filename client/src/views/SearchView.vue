<template>
  <div class="search-container">
    <div class="search-header">
      <h1 class="page-title">搜索用户</h1>
      <van-search
        v-model="searchKeyword"
        placeholder="輸入用户名進行搜索"
        shape="round"
        background="transparent"
        input-align="center"
        class="search-input"
      >
        <template #action>
          <div class="cancel-btn" @click="goBack">取消</div>
        </template>
      </van-search>
    </div>

    <!-- 標籤選擇區域 -->
    <div class="tags-section" v-if="tags.length > 0">
      <div class="tags-header">
        <span class="section-title">選擇標籤</span>
        <span v-if="selectedTags.length > 0" 
              class="clear-tags" 
              @click="clearTags">清空</span>
      </div>
      <div class="tags-content">
        <van-tag 
          v-for="tag in tags" 
          :key="tag.id"
          :class="{ 'tag-selected': isTagSelected(tag.tagname) }"
          @click="toggleTag(tag.tagname)"
          plain
          size="medium"
          :type="isTagSelected(tag.tagname) ? 'primary' : 'default'"
          class="tag-item"
        >
          {{ tag.tagname }}
        </van-tag>
      </div>
    </div>

    <!-- 搜索按鈕 -->
    <div class="search-button-container">
      <van-button 
        type="primary" 
        block 
        round 
        :disabled="!hasSearchConditions"
        @click="navigateToResults"
        class="search-btn"
        icon="search"
      >
        查找用户
      </van-button>
    </div>

    <div class="search-tips" v-if="!hasSearchConditions">
      <div class="search-tip">
        <van-icon name="search" size="44" color="#e0e0e0" />
        <p class="search-tip-text">請輸入關鍵詞或選擇標籤搜索用户</p>
        <p class="search-tip-subtext">可以通過用户名或興趣標籤查找匹配的用户</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable */
// @ts-ignore
import { ref, computed, onMounted } from 'vue'
// @ts-ignore
import { useRouter } from 'vue-router'
// @ts-ignore
import { Toast } from 'vant'
import http from '@/api/http'

// 標籤類型定義
interface TagType {
  id: number;
  tagname: string;
  createtime: string;
  updatetime: string;
  isdelete: number;
}

const router = useRouter()
const searchKeyword = ref('')
const tags = ref<TagType[]>([])
const selectedTags = ref<string[]>([])
const loadingTags = ref(false)

// 計算屬性：是否有搜索條件
const hasSearchConditions = computed(() => {
  return searchKeyword.value.trim() !== '' || selectedTags.value.length > 0
})

// 檢查標籤是否被選中
function isTagSelected(tagName: string): boolean {
  return selectedTags.value.includes(tagName)
}

// 清空標籤選擇
function clearTags() {
  selectedTags.value = []
}

// 切換標籤選擇狀態
function toggleTag(tagName: string) {
  const index = selectedTags.value.indexOf(tagName)
  if (index === -1) {
    selectedTags.value.push(tagName)
  } else {
    selectedTags.value.splice(index, 1)
  }
}

// 跳轉到搜索結果頁
function navigateToResults() {
  if (!hasSearchConditions.value) {
    Toast('請輸入關鍵詞或選擇標籤')
    return
  }

  // 構建查詢參數
  const query: Record<string, string> = {}
  
  if (searchKeyword.value.trim()) {
    query.keyword = searchKeyword.value.trim()
  }
  
  if (selectedTags.value.length > 0) {
    query.tags = selectedTags.value.join(',')
  }
  
  // 跳轉到結果頁
  router.push({
    path: '/search-results',
    query
  })
}

// 返回上一頁
function goBack() {
  router.back()
}

// 獲取標籤列表
async function fetchTags() {
  loadingTags.value = true
  try {
    const response = await http.get('/tag/list')
    console.log('標籤列表原始數據:', response)
    
    if (response && Array.isArray(response.data)) {
      tags.value = response.data
    } else if (Array.isArray(response)) {
      tags.value = response
    } else {
      console.error('無法解析標籤數據:', response)
      tags.value = []
    }
    console.log('解析後的標籤列表:', tags.value)
  } catch (error) {
    console.error('獲取標籤失敗', error)
    tags.value = []
  } finally {
    loadingTags.value = false
  }
}

onMounted(() => {
  fetchTags()
})
</script>

<style scoped>
.search-container {
  min-height: 100%;
  background-color: #f7f8fa;
  display: flex;
  flex-direction: column;
  padding-bottom: 32px;
}

.search-header {
  background: linear-gradient(135deg, #1976d2, #2196f3);
  padding: 32px 16px 24px;
  border-radius: 0 0 24px 24px;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.2);
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: white;
  margin: 0 0 16px;
  text-align: center;
  letter-spacing: 0.5px;
}

.search-input {
  background-color: rgba(255, 255, 255, 0.9) !important;
  border-radius: 24px !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-input :deep(.van-field__control) {
  font-size: 16px;
  color: #333;
}

.cancel-btn {
  font-size: 14px;
  color: white;
  padding: 0 12px;
  font-weight: 500;
}

.tags-section {
  padding: 16px;
  background-color: white;
  border-radius: 12px;
  margin: 0 16px 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.tags-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
}

.clear-tags {
  font-size: 13px;
  color: #1976d2;
  cursor: pointer;
  padding: 2px 6px;
  border-radius: 4px;
  background-color: #f0f9ff;
}

.tags-content {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag-item {
  margin-bottom: 6px;
  cursor: pointer;
  user-select: none;
  transition: all 0.25s cubic-bezier(0.25, 0.8, 0.25, 1);
  font-size: 13px;
  padding: 6px 12px;
}

.tag-selected {
  background-color: #e6f7ff !important;
  border-color: #1976d2 !important;
  font-weight: 500;
  transform: scale(1.05);
  box-shadow: 0 2px 4px rgba(25, 118, 210, 0.2);
}

.search-button-container {
  padding: 0 32px;
  margin-top: 8px;
}

.search-btn {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
  background: linear-gradient(to right, #1976d2, #2196f3);
  border: none;
}

.search-btn:active {
  transform: translateY(1px);
}

.search-btn:disabled {
  opacity: 0.6;
  background: linear-gradient(to right, #78909c, #90a4ae);
}

.search-tips {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 48px 24px;
}

.search-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 280px;
  text-align: center;
}

.search-tip-text {
  margin: 16px 0 8px;
  font-size: 16px;
  color: #616161;
  font-weight: 500;
}

.search-tip-subtext {
  margin: 0;
  font-size: 14px;
  color: #9e9e9e;
  line-height: 1.5;
}
</style> 