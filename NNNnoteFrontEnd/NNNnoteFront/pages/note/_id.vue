<template>
  <el-container>
    <el-header height="140px">
      <h1 style="font-size:30px;margin:20px 0px 15px 0px">
        {{ noteInfo.title ? noteInfo.title:"&nbsp;" }}
      </h1>
      <el-row>
        <el-col :span="2">
          <el-avatar :src="userInfo.avatar" :size="50" />
        </el-col>
        <el-col :span="22">
          <el-descriptions :colon="false" size="mini" :column="4">
            <el-descriptions-item label="作者">
              {{ userInfo.nickname ? userInfo.nickname : '&nbsp;' }}
            </el-descriptions-item>
            <el-descriptions-item />
            <el-descriptions-item />
            <el-descriptions-item />
          </el-descriptions>
          <el-descriptions :colon="false" size="mini" :column="3">
            <el-descriptions-item label="创建时间">
              {{ formatDate(noteInfo.gmtCreate) }}
            </el-descriptions-item>
            <el-descriptions-item label="最后编辑">
              {{ formatDate(noteInfo.gmtModified) }}
            </el-descriptions-item>
            <el-descriptions-item label="长度">
              {{ noteInfo.length }}字
            </el-descriptions-item>
          </el-descriptions>
        </el-col>
      </el-row>
    </el-header>

    <div style="width:100px;position:fixed; top:300px; right:calc((100vw - 900px)/2 - 100px);z-index:9999;">
      <el-row>
        <el-col :span="24" align="middle" justify="middle">
          <el-button style="display:block;" type="info" circle>
            <i class="alibaba_icons_good" />
          </el-button>
          <span style="display:block;margin:3px 0px;color:#606266;font-size: 12px;">
            1点赞
          </span>
          <el-button style="display:block;" type="info" circle>
            <i class="el-icon-star-off" />
          </el-button>
          <span style="display:block;margin:3px 0px;color:#606266;font-size: 12px;">
            1收藏
          </span>
          <el-button style="display:block;" type="info" circle>
            <i class="el-icon-edit" />
          </el-button>
          <span style="display:block;margin:3px 0px;color:#606266;font-size: 12px;">
            编辑
          </span>
        </el-col>
      </el-row>
    </div>

    <div style="position: relative;width: 100%;height:5px;background-color: #dddddd;" />

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

    <div style="margin-bottom:20px;position: relative;width: 100%;height:5px;background-color: #dddddd;" />

    <el-footer style="height:100%;">
      <NoteCommentComponent />
    </el-footer>
  </el-container>
</template>

<script>
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

      }

    }
  },
  created () {
    noteApi.getNoteInfoToRead(this.$route.params.id).then((response) => {
      if (response.data.code === 20000) {
        this.noteInfo = response.data.data.noteInfo
        this.noteText = response.data.data.noteText
        userApi.getUserInfo(response.data.data.noteInfo.userId).then((response) => {
          if (response.data.code === 20000) {
            this.userInfo = response.data.data.data
          }
        })
      }
    })
  },
  methods: {

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
