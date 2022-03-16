<template>
  <el-container>
    <el-main>
      <el-card
        v-for="(item) in list"
        :key="item.id"
        style="margin-bottom:20px;"
        :body-style="{ padding: '15px' }"
      >
        <div style="display:inline-block;width:650px">
          <nuxt-link :to="'/note/'+item.id">
            <p class="card-title" v-text="item.title ? item.title : '作者未设置标题'" />
          </nuxt-link>
          <nuxt-link :to="'/note/'+item.id">
            <p class="card-preview" v-text="item.preview" />
          </nuxt-link>

          <div class="centerVertical">
            <el-avatar style="margin-right:5px" :src="avatarAndNickName[item.userId].avatar" :size="20" />
            <span> {{ avatarAndNickName[item.userId].nickname }}</span>

            <el-divider direction="vertical" />
            <span><i class="alibaba_icons_good" /> {{ item.likes }}</span>

            <el-divider direction="vertical" />
            <span><i class="el-icon-chat-line-round" /> {{ item.commentCount }}</span>

            <el-divider direction="vertical" />
            <span><i class="el-icon-star-off" /> {{ item.collectionCount }}</span>
          </div>
        </div>
        <nuxt-link :to="'/note/'+item.id">
          <el-image
            :src="item.cover"
            style="margin:0px 5px;width: 160px; height: 120px;dispaly:inline-block;"
            fit="scale-down"
          >
            <div slot="error" class="image-slot">
              <el-skeleton-item variant="image" style="width: 160px; height: 120px;" />
            </div>
          </el-image>
        </nuxt-link>
      </el-card>
      <p v-if="loading" class="centerHorizontal">
        加载中<i class="el-icon-loading" />
      </p>
      <p v-if="noMore" class="centerHorizontal">
        没有更多了
      </p>
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
      avatarAndNickName: [],
      data: {
        limit: 5,
        page: 1,
        sortField: 'gmt_create'
      },
      total: 0,
      loading: false
    }
  },
  computed: {
    noMore () {
      return (this.list.length >= this.total) && (this.total !== 0)
    }
  },
  created () {
    noteApi.searchNoteList(this.data)
      .then((response) => {
        this.list = response.data.data.data
        this.total = response.data.data.total
        const temp = response.data.data.avatarAndNickname
        if (temp) {
          for (let i = 0; i < temp.length; i++) {
            this.avatarAndNickName[temp[i].id] = {
              avatar: temp[i].avatar,
              nickname: temp[i].nickname
            }
          }
        }
      })
  },
  mounted () {
    window.addEventListener('scroll', this.handleScroll)
  },
  methods: {
    handleScroll () {
      if (this.noMore || this.loading) { return }

      // !important 该实现方式兼容性极差
      if ((document.documentElement.scrollTop + window.innerHeight) * 1.1 >= document.body.offsetHeight) {
        this.loading = true
        this.data.page += 1

        noteApi.searchNoteList(this.data)
          .then((response) => {
            this.list.push(...response.data.data.data)
            this.total = response.data.data.total
            const temp = response.data.data.avatarAndNickname
            if (temp) {
              for (let i = 0; i < temp.length; i++) {
                this.avatarAndNickName[temp[i].id] = {
                  avatar: temp[i].avatar,
                  nickname: temp[i].nickname
                }
              }
            }
            this.loading = false
          })
      }
    },
    route (id) {
      this.$router.push({ path: '/note/' + id })
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
    font-size: 14px;
    color: #999;
    height:42px;
    text-indent: 14px;
    margin: 15px 0px;
    margin-right: 20px;
    line-height: 18px;
}
.centerVertical{
    display: flex;
    align-items: center;
    font-size: 10px;
    color: #b4b4b4;
}
.centerHorizontal{
    display: flex;
    justify-content:center;
}
</style>
