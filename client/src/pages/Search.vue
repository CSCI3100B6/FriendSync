<script setup>
import { ref } from 'vue';
import { Toast } from 'vant';

const seachText = ref('');
const onSearch = (val) => Toast(val);
const onCancel = () => Toast('取消');

const activeIds = ref([1, 2]);
const activeIndex = ref(0);

const tagList = [
  {
    text: '性别',
    children: [
      { text: '男', id: 1 },
      { text: '女', id: 2 },
    ],
  },
  {
    text: '年级',
    children: [
      { text: '大一', id: 4 },
      { text: '大二', id: 5 },
      { text: '大三', id: 6 },
    ],
  },
  { text: '福建', disabled: true },
];

</script>

<template>
  <form action="/">
    <van-search
        v-model="seachText"
        show-action
        placeholder="请搜索要搜索的标签"
        @search="onSearch"
        @cancel="onCancel"
    />
  </form>


  <van-divider content-position="left">已选标签</van-divider>
  <div v-if="activeIds.length === 0">请选择标签</div>
  <van-tag v-for="tag in activeIds" :show="true" closeable size="small" type="primary" @close="doClose(tag)">
    {{tag}}
  </van-tag>
  <van-divider content-position="left">选择标签</van-divider>
  <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"
  />
</template>

<style scoped>



.van-search {
  position: fixed;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.van-divider{
  left: 0;
  width: 100%;
  z-index: 1000;
}

.van-tree-select{
  position: fixed;
  left: 0;
  width: 100%;
  z-index: 1000;
}
</style>