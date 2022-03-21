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

        <el-button style="display:block;" :type="collectstyle.color" circle @click="collectDialog.visible = true">
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

    <!-- #region 收藏对话框 -->
    <el-dialog
      title="请选择收藏夹"
      :visible.sync="collectDialog.visible"
      width="600px"
      :lock-scroll="false"
      center
    >
      <el-table :data="collectDialog.cfolders" :show-header="false" height="calc(40vh)">
        <el-table-column property="folderName" width="450" />
        <el-table-column width="100">
          <template slot-scope="scope">
            <el-button v-if="noteIn.indexOf(scope.row.id) === -1" type="primary" @click="noteCollect(scope.row.id)">
              收藏
            </el-button>
            <el-button v-else type="info" @click="noteCollect(scope.row.id)">
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createCfolderDialog.visible = true">
          创建收藏夹
        </el-button>
      </span>

      <el-dialog
        width="30%"
        title="创建收藏夹"
        :visible.sync="createCfolderDialog.visible"
        append-to-body
        :lock-scroll="false"
      >
        <el-form :model="createCfolderDialog.form">
          <el-form-item
            prop="cfolderName"
            :rules="[
              { required: true, trigger: ['blur'], message: '文件夹名不能为空'}]"
          >
            <el-input
              v-model="createCfolderDialog.form.cfolderName"
              maxlength="10"
              show-word-limit
              placeholder="请输入收藏夹名"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="createCfolderDialog.form.description"
              :autosize="{ minRows: 5, maxRows: 6}"
              show-word-limit
              type="textarea"
              placeholder="请输入描述信息"
            />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="createCfolderDialog.visible = false">取 消</el-button>
          <el-button type="primary" @click="addUserCfolder">确认</el-button>
        </span>
      </el-dialog>
    </el-dialog>
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
      collectDialog: {
        visible: false,
        cfolders: [],
        noteIn: []
      },
      createCfolderDialog: {
        visible: false,
        form: {
          cfolderName: '',
          description: ''
        }
      }
    }
  },
  computed: {
    noteIn () {
      return this.collectDialog.noteIn
    }
  },
  created () {
    this.getNoteInfo()
    this.getLikeStatus()
    this.getUserCfolders()
    this.getCfolderIds()
  },
  methods: {
    // #region 获取文章信息(包括点赞于收藏)与用户信息
    getNoteInfo () {
      noteApi.getNoteInfoToRead(this.$route.params.id).then((response) => {
        if (response.data.code === 20000) {
          this.noteInfo = response.data.data.noteInfo
          this.noteText = response.data.data.noteText
          this.getUserInfo(this.noteInfo.userId)
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
    // #endregion

    // #region 点赞相关
    getLikeStatus () {
      if (this.$store.state.userData.userInfo.id != null) {
        const data = {
          noteId: this.$route.params.id
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
        this.getLikeStatus()
      })
    },
    // #endregion

    // #region 收藏相关
    // 获取关于该用户的收藏夹列表
    getUserCfolders () {
      userApi.getUserCfolders()
        .then((response) => {
          this.collectDialog.cfolders = response.data.data.data
        })
    },
    // 获取收藏了该笔记的收藏夹列表
    getCfolderIds () {
      const params = { noteId: this.$route.params.id }
      noteApi.getCfolderIds(params)
        .then((response) => {
          this.collectDialog.noteIn = response.data.data.data
        })
    },
    // 笔记收藏与取消
    noteCollect (id) {
      const params = { noteId: this.noteInfo.id, cfolderId: id }
      noteApi.noteCollect(qs.stringify(params)).then((response) => {
        if (response.data.code) {
          this.getCfolderIds()
        }
      })
    },
    // 根据用户 id 创建新的收藏夹，文件夹描述可以为空
    addUserCfolder () {
      userApi.addUserCfolder(qs.stringify(this.createCfolderDialog.form)).then((response) => {
        this.getUserCfolders()
        if (response.data.code === 20000) {
          this.createCfolderDialog.form = {
            folderName: '',
            description: ''
          }
          this.createCfolderDialog.visible = false
          this.$message.success('创建收藏夹成功')
        } else {
          this.$message.error(response.data.message)
        }
      })
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
