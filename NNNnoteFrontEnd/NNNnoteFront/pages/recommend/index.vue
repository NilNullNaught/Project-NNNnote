<template>
  <el-container>
    <el-main>
      <!-- <el-card
        v-for="(item) in list"
        :key="item.id"
        style="margin-bottom:20px;"
        :body-style="{ padding: '15px' }"
      >
        <div style="display:inline-block;width:650px">
          <p class="card-title" v-text="item.title ? item.title : '作者未设置标题'" />
          <p class="card-preview" />

          <div class="centerVertical">
            <el-avatar style="margin-right:5px" :size="20" />
            <span> 作者昵称一二三四五六</span>

            <el-divider direction="vertical" />
            <span><i class="alibaba_icons_good" /> {{ item.likes }}</span>

            <el-divider direction="vertical" />
            <span><i class="el-icon-chat-line-round" /> {{ item.commentCount }}</span>

            <el-divider direction="vertical" />
            <span><i class="el-icon-star-off" /> {{ item.collectionCount }}</span>
          </div>
        </div>
        <el-image
          :src="item.cover"
          style="margin:0px 5px;width: 160px; height: 120px;dispaly:inline-block;"
          fit="scale-down"
        >
          <div slot="error" class="image-slot">
            <el-skeleton-item variant="image" style="width: 160px; height: 120px;" />
          </div>
        </el-image>
      </el-card> -->
      <div class="infinite-list-wrapper" style="overflow:auto">
        <ul
          v-infinite-scroll="load"
          class="list"
          infinite-scroll-disabled="disabled"
        >
          <li v-for="i in count" :key="i" class="list-item">
            {{ i }}
          </li>
        </ul>
        <p v-if="loading">
          加载中...
        </p>
        <p v-if="noMore">
          没有更多了
        </p>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import noteApi from '@/api/note'

export default {
  name: 'RecommendIndexPage',
  layout: 'BaseLayout',

  data () {
    return {
      list: [],
      count: 10,
      loading: false
    }
  },
  computed: {
    noMore () {
      return this.count >= 20
    },
    disabled () {
      return this.loading || this.noMore
    }
  },
  created () {
    const data = {
      limit: 5,
      page: 1,
      sortField: 'gmt_create'
    }
    noteApi.searchNoteList(data)
      .then((response) => {
        this.list = response.data.data.data
      })
  },
  methods: {
    load () {
      this.loading = true
      setTimeout(() => {
        this.count += 2
        this.loading = false
      }, 2000)
    }
  }
}
</script>
<style scoped>
.card-title{
    font-weight: bolder;
    font-size: 20px;
}
.card-preview{
    font-size: 12px;
    color: #999;
    height:42px;
    margin: 15px 0px;
}
.centerVertical{
    display: flex;
    align-items: center;
    font-size: 10px;
    color: #b4b4b4;
}
</style>
