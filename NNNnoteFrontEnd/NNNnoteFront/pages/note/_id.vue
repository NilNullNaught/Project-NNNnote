<template>
  <el-container>
    <!-- #region 文章头部信息 -->
    <el-header height="150px">
      <h1>
        {{ noteInfo.title ? noteInfo.title:"&nbsp;" }}
      </h1>
      <div class="centerVertical">
        <el-avatar style="display:inline-block;margin-right:10px;" :src="userInfo.avatar" :size="50" />
        <div style="display:inline-block;font-size:12px;color:#606266;line-height: 20px;">
          <p style="font-size:15px;font-weight:bolder">
            {{ userInfo.nickname ? userInfo.nickname : '&nbsp;' }}
          </p>
          <p>
            <span style="display:inline-block;width:50px;"><i class="alibaba_icons_good" />  {{ noteInfo.likes }}</span>
            <el-divider direction="vertical" />
            <span style="display:inline-block;width:50px;"><i class="el-icon-chat-line-round" />  {{ noteInfo.commentCount }}</span>
            <el-divider direction="vertical" />
            <span style="display:inline-block;width:50px;"><i class="el-icon-star-off" />  {{ noteInfo.collectionCount }}</span>
            <el-divider direction="vertical" />
            <span style="display:inline-block;width:50px;">长度  {{ noteInfo.length }}</span>
            <el-divider direction="vertical" />
            <span>创建于    {{ formatDate(noteInfo.gmtCreate) }}</span>
            <el-divider direction="vertical" />
            <span>最后编辑    {{ formatDate(noteInfo.gmtModified) }}</span>
          </p>
        </div>
      </div>
    </el-header>
    <!-- #endregion -->

    <!-- #region 侧边工具条 -->
    <div style="position:fixed; top:240px; right:calc((100vw - 900px)/2 - 60px);">
      <div v-if="userInfo.id != $store.state.userData.userInfo.id && $store.state.userData.userInfo.id !=null">
        <el-button style="display:block;" :type="likeStyle.color" circle @click="noteLike">
          <i :class="likeStyle.icon" />
        </el-button>
        <span style="display:block;margin:3px 0px;color:#606266;font-size: 12px;text-align:center;" v-text="likeStyle.text" />

        <el-button style="display:block;" :type="collectstyle.color" circle @click="noteCollect">
          <i :class="collectstyle.icon" />
        </el-button>
        <span style="display:block;margin:3px 0px;color:#606266;font-size: 12px;text-align:center;" v-text="collectstyle.text" />
      </div>

      <nuxt-link v-if="userInfo.id == $store.state.userData.userInfo.id && $store.state.userData.userInfo.id != null" :to="'/editor/'+noteInfo.id">
        <el-button style="display:block;" circle>
          <i class="el-icon-edit" />
        </el-button>
        <span style="display:block;margin:3px 0px;color:#606266;font-size: 12px;text-align:center;">
          编辑
        </span>
      </nuxt-link>
    </div>
    <!-- #endregion -->

    <div style="position: relative;width: 100%;height:5px;background-color: #dddddd;" />

    <!-- #region 笔记主体 -->
    <el-main style="padding:0px">
      <mavon-editor
        :value="noteText.text"
        :subfield="false"
        :default-open="'preview'"
        :toolbars-flag="false"
        :editable="false"
        :scroll-style="true"
        :ishljs="true"
        preview-background="#fff"
      />
    </el-main>
    <!-- #endregion -->

    <div style="margin-bottom:20px;position: relative;width: 100%;height:5px;background-color: #dddddd;" />

    <!-- #region 笔记评论 -->
    <el-footer style="height:100%;">
      <NoteCommentComponent />
    </el-footer>
    <!-- #endregion -->
  </el-container>
</template>

<script>
import qs from 'qs'

import noteApi from '@/api/note'
import userApi from '@/api/user'
import 'mavon-editor/dist/css/index.css'
import NoteCommentComponent from '@/components/note/NoteCommentComponent'

export default {
  name: 'NoteIdPage',
  components: {
    NoteCommentComponent
  },
  layout: 'BaseLayout',
  data () {
    return {
      noteInfo: {

      },
      noteText: {

      },
      userInfo: {

      },
      liked: false,
      likeStyle: {
        color: 'danger',
        icon: 'alibaba_icons_good',
        text: '点赞'
      },
      collectstyle: {
        color: 'warning',
        icon: 'el-icon-star-off',
        text: '收藏'
      },
      collectionList: {
        visible: false
      }

    }
  },
  created () {
    this.getNoteInfo()
  },
  methods: {
    // #region 获取文章信息(包括点赞于收藏)与用户信息
    getNoteInfo () {
      noteApi.getNoteInfoToRead(this.$route.params.id).then((response) => {
        if (response.data.code === 20000) {
          this.noteInfo = response.data.data.noteInfo
          this.noteText = response.data.data.noteText
          this.getUserInfo(this.noteInfo.userId)
          this.getStatus()
        }
      })
    },
    getUserInfo () {
      userApi.getUserInfoById(this.noteInfo.userId).then((response) => {
        if (response.data.code === 20000) {
          this.userInfo = response.data.data.data
        }
      })
    },
    // 格式化日期显示
    formatDate (data) {
      // 设置时间格式
      const format = 'YY年MM月DD日 hh:mm'
      // 获取单元格数据
      if (data == null) {
        return null
      }
      const date = new Date(data)

      // 创建数组，如果数字小于 10，则在十位上填充 0
      const preArr = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09']

      const year = date.getFullYear()
      const month = date.getMonth() + 1 // 月份是从0开始的
      const day = date.getDate()
      const hour = date.getHours()
      const min = date.getMinutes()
      const sec = date.getSeconds()

      const newTime = format.replace(/YY/g, year)
        .replace(/MM/g, preArr[month] || month)
        .replace(/DD/g, preArr[day] || day)
        .replace(/hh/g, preArr[hour] || hour)
        .replace(/mm/g, preArr[min] || min)
        .replace(/ss/g, preArr[sec] || sec)

      return newTime
    },

    getStatus () {
      if (this.$store.state.userData.userInfo.id != null) {
        const data = {
          noteId: this.noteInfo.id
        }
        noteApi.userLikeNote(data)
          .then((response) => {
            this.liked = response.data.data.data
            if (this.liked) {
              this.likeStyle = {
                color: 'info',
                icon: 'alibaba_icons_good-fill',
                text: '已赞'
              }
            } else {
              this.likeStyle = {
                color: 'danger',
                icon: 'alibaba_icons_good',
                text: '点赞'
              }
            }

            // 更新文章的点赞数
            noteApi.getNoteLikeCount(this.noteInfo.id)
              .then((response) => {
                this.noteInfo.likes = response.data.data.data
              })
          })
      }
    },
    noteLike () {
      // 封装数据
      const data = { noteId: this.noteInfo.id }
      noteApi.noteLike(qs.stringify(data)).then((response) => {
        this.getStatus()
      })
    },
    noteCollect () {
      this.collectionList.visible = true
    }

  }
}

</script>

<style scoped>
.el-button+.el-button {
    margin-left: 0px;
}
.el-button--mini{
  padding: 3px 15px;
}
</style>
