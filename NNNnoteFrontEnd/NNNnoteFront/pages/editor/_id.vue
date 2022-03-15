<template>
  <div>
    <el-row style="margin:5px;">
      <el-col :span="8">
        <el-button type="danger" @click="exit()">
          退出编辑
        </el-button>
      </el-col>
      <el-col :span="8">
        <el-input
          v-model="saveNote.title"
          placeholder="请输入标题"
          maxlength="20"
          show-word-limit
          clearable
        >
          <template slot="prepend">
            标题
          </template>
        </el-input>
      </el-col>
    </el-row>

    <client-only>
      <mavon-editor
        ref="md"
        v-model="saveNote.text"
        style="margin:5px;"
        :toolbars="markdownOption"
        @imgAdd="$addImg"
        @save="saveDialog.visible = true"
        @change="$change"
      />
    </client-only>

    <el-dialog
      title="请选择文件夹"
      :visible.sync="saveDialog.visible"
      width="600px"
      center
    >
      <el-row style="height:calc(30vh);overflow-y:scroll" justify="center" :gutter="10" type="flex">
        <el-radio-group v-model="saveNote.noteFolderId">
          <el-col v-for="folder in folders" :key="folder.id" :span="12">
            <el-radio :label="folder.id" border style="width:100%;margin:5px;">
              {{ folder.folderName }}
            </el-radio>
          </el-col>
          <el-col :span="12">
            <el-button style="width:100%;margin:5px;" @click="addNfolderDialog.visible = true">
              <i class="el-icon-plus" />
            </el-button>
          </el-col>
        </el-radio-group>
      </el-row>

      <span slot="footer" class="dialog-footer">
        <el-button @click="saveDialog.visible = false">&nbsp;&nbsp;&nbsp;取&nbsp;&nbsp;消&nbsp;&nbsp;&nbsp;</el-button>

        <el-button type="primary" @click="coverNfolderDialog.visible = true">
          <i class="el-icon-upload" />
          上传封面
        </el-button>

        <el-button type="primary" @click="submitSave()">
          <i class="alibaba_icons_baocun" />
          保&nbsp;&nbsp;&nbsp;&nbsp;存&nbsp;</el-button>
        <el-checkbox v-model="saveDialog.isPrivate">私密</el-checkbox>

      </span>

      <el-dialog
        id="coverNfolderDialog"
        width="20%"
        title="上传封面"
        :visible.sync="coverNfolderDialog.visible"
        append-to-body
      >
        <el-row>
          <el-col :span="24" align="center">
            <el-upload
              class="cover-uploader "
              action="fakeaction"
              :show-file-list="false"
              :before-upload="beforeCoverUpload"
              :http-request="uploadCover"
            >
              <img v-if="saveNote.cover" :src="saveNote.cover" class="cover">
              <i v-else class="el-icon-plus cover-uploader-icon" />
            </el-upload>
          </el-col>
        </el-row>

        <span slot="footer" class="dialog-footer">
          <el-button @click="cancelUpload">取 消</el-button>
          <el-button type="primary" @click="coverNfolderDialog.visible = false">确认</el-button>
        </span>
      </el-dialog>

      <el-dialog
        width="20%"
        title="新增文件夹"
        :visible.sync="addNfolderDialog.visible"
        append-to-body
      >
        <el-form :model="addNfolderDialog.form">
          <el-form-item
            prop="nfolderName"
            :rules="[
              { required: true, trigger: ['blur'], message: '文件夹名不能为空'}]"
          >
            <el-input
              v-model="addNfolderDialog.form.nfolderName"
              maxlength="10"
              show-word-limit
              placeholder="请输入文件夹名"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="addNfolderDialog.form.description"
              maxlength="30"
              show-word-limit
              type="textarea"
              placeholder="请输入描述"
            />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="addNfolderDialog.visible = false">取 消</el-button>
          <el-button type="primary" @click="submitNfolder()">确认</el-button>
        </span>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script>
import 'mavon-editor/dist/css/index.css'
import qs from 'qs'
import noteApi from '@/api/note'
import ossApi from '@/api/oss'
import userApi from '@/api/user'

export default {
  name: 'EditorIdPage',
  beforeRouteLeave (to, from, next) {
    this.$confirm('离开前,请确认已经保存', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(() => {
        window.onbeforeunload = null
        next()
      })
      .catch(() => {
        // alert("router")
      })
  },
  data () {
    return {
      folders: [],
      saveNote: {
        id: '',
        noteFolderId: '',
        title: '',
        preview: '',
        cover: null,
        text: '',
        // 笔记状态 0 代表用户没有主动保存过，1 代表以私密状态保存，2 代表以公开状态保存
        status: 0,
        resourceUrlList: []
      },
      saveDialog: {
        visible: false,
        // 笔记默认以私密状态保存
        isPrivate: true
      },
      addNfolderDialog: {
        visible: false,
        form: {
          nfolderName: '',
          description: ''
        }
      },
      coverNfolderDialog: {
        visible: false,
        cover: null
      },
      markdownOption: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      },
      saveCount: 0
    }
  },
  created () {
    noteApi.getNoteInfoToEdit(this.$route.params.id).then((response) => {
      if (response.data.code === 20000) {
        this.saveNote.id = response.data.data.noteInfo.id
        this.saveNote.noteFolderId = response.data.data.noteInfo.noteFolderId
        this.saveNote.title = response.data.data.noteInfo.title
        this.saveNote.status = response.data.data.noteInfo.status
        this.saveNote.text = response.data.data.noteText.text
        this.saveNote.cover = response.data.data.noteInfo.cover
        this.folders = response.data.data.NfolderList

        if (this.saveNote.status !== 2) {
          this.saveDialog.isPrivate = true
        } else {
          this.saveDialog.isPrivate = false
        }
        this.saveNote.resourceUrlList = this.saveNote.text.match(/\]\(https:\/\/nilnullnaught(.*?)\)/g)
      }
    }).catch(error =>
      // eslint-disable-next-line no-console
      console.log(error)
    )
  },
  mounted () {
    window.onbeforeunload = function (e) { // 刷新与关闭前弹出提示
      e = e || window.event
      // 兼容IE8和Firefox 4之前的版本
      if (e) {
        e.returnValue = '关闭提示'
      }
      // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
      return '关闭提示'
    }
  },
  methods: {

    // 保存图片
    $addImg (pos, file) {
      // 封装 file
      const fileData = new FormData()
      fileData.append('file', file)

      ossApi.uploadFile(fileData)
        .then((response) => {
          const url = response.data.data.url
          this.$refs.md.$img2Url(pos, url)
          this.saveNote.resourceUrlList.push(url)
        })
    },
    // 自动保存
    $change (pos, file) {
      this.saveCount = ++this.saveCount
      if (this.saveCount % 5 !== 0) {
        return
      }
      // 生成笔记预览
      this.generateNotePreivew()

      noteApi.autoSaveNote(this.saveNote)
      // .then((response) => {
      //   if (response.data.code === 20000) {
      //     this.$message({
      //       type: 'success',
      //       message: '保存成功'
      //     })
      //   } else {
      //     this.$message.error(response.data.message)
      //   }
      // })
    },
    // 提交保存
    submitSave () {
      // <- 修改笔记状态
      if (this.saveDialog.isPrivate) {
        this.saveNote.status = 1
      } else {
        this.saveNote.status = 2
      }
      this.saveDialog.visible = false
      // ->

      // 生成笔记预览
      this.generateNotePreivew()

      noteApi.saveNote(this.saveNote).then((response) => {
        if (response.data.code === 20000) {
          this.$message({
            type: 'success',
            message: '保存成功'
          })
        } else {
          this.$message.error(response.data.message)
        }
      })
    },
    // 创建新的笔记文件夹
    submitNfolder () {
      userApi.addUserNfolder(qs.stringify(this.addNfolderDialog.form)).then((response) => {
        if (response.data.code === 20000) {
          // 获取新的笔记文件夹列表
          userApi.getUserNfolder().then((response) => {
            this.folders = response.data.data.data
          })
          this.addNfolderDialog.visible = false
        } else {
          this.$message.error(response.data.message)
        }
      })
    },
    // 生成笔记预览
    generateNotePreivew () {
      const raw = document.getElementsByClassName('v-show-content').item(0).innerText
      if (raw.length >= 60) {
        // 获取不含 markdown 语法的笔记预览
        this.saveNote.preview = raw.substring(0, 60) + '…'
      } else {
        this.saveNote.preview = raw
      }
    },
    // 取消上传
    cancelUpload () {
      this.saveNote.cover = null
      this.coverNfolderDialog.visible = false
    },
    // 上传封面
    uploadCover (params) {
      const data = new FormData()
      // 创建一个表单数据
      data.append('file', params.file)
      ossApi.uploadFile(data).then((response) => {
        if (response.data.code === 20000) {
          // 提示修改成功
          this.$message({
            type: 'success',
            message: '封面上传成功'
          })
          this.saveNote.cover = response.data.data.url
        } else {
          this.$message.error('封面上传失败')
        }
      })
    },

    beforeCoverUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 5

      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 5MB!')
      }
      return isJPG && isLt2M
    },

    exit () {
      window.history.back()
    }
  }
}
</script>

<style scoped>
.cover-uploader ::v-deep .el-upload {
    border: 1px dashed #d9d9d9;
    width: 160px;
    height: 120px;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .cover-uploader ::v-deep .el-upload:hover {
    border-color: #409EFF;
  }
  .cover-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 160px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
  .cover {
    width: 178px;
    height: 178px;
    display: block;
    border: 1px dashed #8c939d;
  }
#coverNfolderDialog ::v-deep .el-dialog__body{
  padding: 0px;
}
</style>
