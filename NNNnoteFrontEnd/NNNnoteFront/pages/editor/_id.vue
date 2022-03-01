<template>
  <div class="mavonEditor">
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
      <el-col :span="8">
        <br>
      </el-col>
    </el-row>

    <client-only>
      <mavon-editor
        ref="md"
        v-model="saveNote.text"
        :toolbars="markdownOption"
        @imgAdd="$addImg"
        @save="$save"
      />
    </client-only>

    <el-dialog
      title="请选择文件夹"
      :visible.sync="saveDialog.visible"
      width="30%"
      center
    >
      <el-row style="height:calc(30vh);overflow-y:scroll">
        <el-col :span="24" align="center">
          <el-radio-group v-model="saveNote.noteFolderId">
            <el-radio v-for="folder in folders" :key="folder.id" :label="folder.id" border style="width:300px;margin:5px">
              {{ folder.folderName }}
            </el-radio>
          </el-radio-group>
        </el-col>
      </el-row>

      <span slot="footer" class="dialog-footer">
        <el-button @click="saveDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="addNfolderDialog.visible = true">
          创建文件夹
        </el-button>
        <el-button type="primary" @click="submitSave()">保存</el-button>
        <el-checkbox v-model="saveDialog.isPrivate">私密</el-checkbox>

      </span>

      <el-dialog
        width="20%"
        title="创建新文件夹"
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
        clearTimeout(this.timer)
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
      timer: null
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

    // 设置自动保存
    this.timer = setInterval(() => {
      // <- 生成笔记预览
      if (this.saveNote.text.length >= 60) {
        this.saveNote.preview = this.saveNote.text.substring(0, 60) + '…'
      } else {
        this.saveNote.preview = this.saveNote.text
      }
      // ->

      noteApi.autoSaveNote(this.saveNote).then((response) => {
        if (response.data.code === 20000) {
          this.$message({
            type: 'success',
            message: '保存成功'
          })
        } else {
          this.$message.error(response.data.message)
        }
      })
    }, 30000)
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
    $save (value, render) {
      this.saveDialog.visible = true
    },
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
    submitSave () {
      // <- 修改笔记状态
      if (this.saveDialog.isPrivate) {
        this.saveNote.status = 1
      } else {
        this.saveNote.status = 2
      }
      this.saveDialog.visible = false
      // ->

      // <- 生成笔记预览
      if (this.saveNote.text.length >= 60) {
        this.saveNote.preview = this.saveNote.text.substring(0, 60) + '…'
      } else {
        this.saveNote.preview = this.saveNote.text
      }
      // ->

      noteApi.saveNote(this.saveNote)
        .then((response) => {
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
    exit () {
      window.history.back()
    }
  }
}
</script>

<style scoped>
.mavonEditor {
  width: 100%;
  height: 100%;
}
</style>
