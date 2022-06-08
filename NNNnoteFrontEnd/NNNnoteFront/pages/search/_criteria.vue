<template>
  <el-container>
    <el-main style="padding-bottom:0px;">
      <el-card
        v-for="(item) in list"
        :key="item.id"
        style="margin-bottom:20px;"
        :body-style="{ padding: '15px' }"
      >
        <div style="display:inline-block;width:650px">
          <nuxt-link :to="'/note/'+item.id">
            <p v-dompurify-html="item.title ? item.title : '作者未设置标题'" class="card-title" />
          </nuxt-link>
          <nuxt-link :to="'/note/'+item.id">
            <p v-dompurify-html="item.preview" class="card-preview" />
          </nuxt-link>

          <div class="centerVertical card-span">
            <nuxt-link :to="'/visitor/'+item.userId">
              <el-avatar style="margin-right:5px" :src="avatarAndNickName[item.userId].avatar" :size="20" />
            </nuxt-link>

            <nuxt-link :to="'/visitor/'+item.userId">
              <span> {{ avatarAndNickName[item.userId].nickname }}</span>
            </nuxt-link>

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
    </el-main>
    <el-footer class="centerVertical centerHorizontal">
      <el-pagination
        :current-page.sync="data.page"
        :page-size="data.limit"
        layout="total, prev, pager, next,jumper"
        :total="total"
        @current-change="getList"
      />
    </el-footer>
  </el-container>
</template>

<script>
import noteApi from '@/api/note'

export default {
  name: 'SearchPage',
  layout: 'BaseLayout',

  data () {
    return {
      list: [],
      avatarAndNickName: [],
      data: {
        criteria: '',
        limit: 5,
        page: 1
        // sortField: 'gmt_create'
      },
      total: 0,
      loading: false
    }
  },
  created () {
    this.getList()
  },

  methods: {
    getList (page = 1) {
      this.data.current = page
      this.data.criteria = decodeURIComponent(this.$route.params.criteria)
      noteApi.searchNoteList(this.data)
        .then((response) => {
          const rawlist = response.data.data.data
          if (rawlist) {
            rawlist.forEach((element) => {
              element.title = element.title.replaceAll('<em>', '<span style="color:red;font-weight:bolder;">')

              element.title = element.title.replaceAll('</em>', '</span>')
              element.preview = element.preview.replaceAll('<em>', '<span style="color:red;font-weight:bolder;">')
              element.preview = element.preview.replaceAll('</em>', '</span>')
            })
          }

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
.card-span{
    font-size: 10px;
    color: #b4b4b4;
}
</style>
