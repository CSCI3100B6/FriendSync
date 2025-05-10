<script setup>
import { ref, computed } from 'vue';
import { Toast } from 'vant';

const searchText = ref('');
const activeIds = ref([]);
const activeIndex = ref(0);

const tagList = [
  {
    text: '性别',
    children: [
      { text: '男', id: '男' },
      { text: '女', id: '女' },
    ],
  },
  {
    text: '年级',
    children: [
      { text: '大一', id: '大一' },
      { text: '大二', id: '大二' },
      { text: '大三', id: '大三' },
    ],
  },
];

const handleUpdateActiveIds = (newIds) => {
  const lastSelectedId = newIds[newIds.length - 1]; // 获取最新选中的标签
  const parentCategory = tagList.find(parent =>
      parent.children.some(child => child.id === lastSelectedId)
  )?.text;

  // 移除同分类的旧标签
  activeIds.value = [
    ...activeIds.value.filter(id => {
      const itemCategory = tagList.find(parent =>
          parent.children.some(child => child.id === id)
      )?.text;
      return itemCategory !== parentCategory; // 保留不同分类的标签
    }),
    lastSelectedId, // 添加新选中的标签
  ];
};



// 计算属性：扁平化的标签列表，用于搜索
const flattenedTags = computed(() =>
    tagList.flatMap(parentTag => parentTag.children)
);

// 优化后的搜索函数
const onSearch = (val) => {
  if (!val.trim()) {
    Toast('请输入搜索内容');
    return;
  }

  const searchTerm = val.toLowerCase();
  const matchedTags = flattenedTags.value
      .filter(item => item.text.toLowerCase().includes(searchTerm))
      .map(item => item.id);

  // 合并现有选中项和新搜索到的项，去重
  activeIds.value = [...new Set([...activeIds.value, ...matchedTags])];

  // 如果有匹配项，显示提示
  if (matchedTags.length) {
    Toast(`找到 ${matchedTags.length} 个匹配标签`);
  } else {
    Toast('没有找到匹配标签');
  }
};

const onCancel = () => {
  searchText.value = '';
};

// 移除标签
const doClose = (tag) => {
  activeIds.value = activeIds.value.filter(item => item !== tag);
};
</script>

<template>
  <div class="container">
    <form @submit.prevent class="search-form">
      <van-search
          v-model="searchText"
          show-action
          placeholder="请输入要搜索的标签"
          @search="onSearch"
          @cancel="onCancel"
          class="search-bar"
      />
    </form>

    <div class="section">
      <van-divider content-position="left">已选标签</van-divider>
      <div v-if="activeIds.length === 0" class="empty-tip">请选择标签</div>
      <van-row gutter="16" class="tag-container">
        <van-col v-for="tag in activeIds" :key="tag">
          <van-tag closeable size="small" type="primary" color= "#439fef"   @close="doClose(tag)">
            {{ tag }}
          </van-tag>
        </van-col>
      </van-row>
    </div>

    <div class="section">
      <van-divider content-position="left">选择标签</van-divider>
      <van-tree-select
          v-model:active-id="activeIds"
          v-model:main-active-index="activeIndex"
          :items="tagList"
          @update:active-id="handleUpdateActiveIds"
          class="tree-select"
      />
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

.search-form {
  margin: 12px;
  padding: 12px;
  width: 100%;
  border-radius: 8px;
  background: #fff;
}

.section {
  margin: 12px;
  padding: 12px;
  z-index: 1000;
  width: 100%;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.empty-tip {
  padding: 12px;
  color: #969799;
  font-size: 14px;
  text-align: center;
}

.tag-container {

  padding: 8px 0;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}



.tree-select {
  --van-tree-select-content-background: #fff;
}

:deep(.van-tree-select__item) {
  color: #333; /* 未选中文字颜色 */
}

:deep(.van-tree-select__item--active) {
  background-color: #fff; /* 选中背景色 */
  color: #439fef !important;
}


</style>