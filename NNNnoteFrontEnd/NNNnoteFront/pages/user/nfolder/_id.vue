<template>
  <div>
    <el-container>
      <el-main>
        <el-row align="middle" justify="center" type="flex">
          <el-col :span="12" align="center" justify="center" type="flex">
            <span style="font-size:20px;font-weight:bold;">{{ folderInfo.folderName }} </span>
          </el-col>
        </el-row>

        <div style="padding:5px" />

        <!-- #region 操作栏 -->
        <el-row align="middle" justify="center" type="flex">
          <el-col :span="16">
            <el-button
              size="mini"
              type="primary"
              plain
              @click="$router.push({ path: '/user'})"
            >
              <i class="el-icon-top-left el-icon--left" />
              返回主页
            </el-button>
            <el-button
              size="mini"
              type="primary"
              plain
              @click="CreateNoteDialog.visible = true"
            >
              &nbsp;<i class="el-icon-notebook-1 el-icon--left" />
              写笔记&nbsp;
            </el-button>

            <el-button
              v-show="select.checkedList.length > 0"
              size="mini"
              plain
              type="primary"
              @click="deleteNotes"
            >
              <i class="el-icon-delete el-icon--left" />
              删除笔记
            </el-button>

            <el-button
              v-show="select.checkedList.length == 1"
              size="mini"
              plain
              type="primary"
              @click="editNote"
            >
              <i class="el-icon-edit el-icon--left" />
              编辑笔记
            </el-button>
          </el-col>

          <el-col :span="8">
            <el-input
              v-model="list.keyword"
              size="mini"
              placeholder="请输入内容"
              clearable
              @change="getList"
            />
          </el-col>
        </el-row>
        <!-- #endregion -->

        <!-- #region 全选框 -->
        <div style="margin: 10px;">
          <el-checkbox
            v-model="select.selectAll"
            :indeterminate="select.isIndeterminate"
            :label="select.selectSum"
            @change="handleCheckAll"
          />
        </div>
        <!-- #endregion -->

        <div style="margin:10px 0px">
          <el-divider />
        </div>

        <!-- #region 笔记列表 -->
        <el-row>
          <el-col v-for="(o) in list.result" :key="o.id" :span="6">
            <el-card
              :ref="'Note-'+o.id"
              class="NoteFolderId-el-card"
              shadow="hover"
              :body-style="{ padding: '10px' }"
            >
              <input
                v-if="o.id !== o.userId"
                v-model="o.ischecked"
                style="position:absolute;top: 5px;left: 5px;height:15px;width:15px;"
                type="checkbox"
                @change="handleChecked(o)"
              >
              <el-tooltip
                v-if="o.status === 0"
                style="position:absolute;top: 5px;right: 5px;"
                content="尚未保存，草稿状态"
                placement="top"
              >
                <i class="el-icon-warning" />
              </el-tooltip>

              <el-tooltip
                v-if="o.status === 2"
                style="position:absolute;top: 5px;right: 5px;"
                content="该笔记已公开，可被其他用户查阅。"
                placement="top"
              >
                <i class="alibaba_icons_public" />
              </el-tooltip>

              <div style="margin:0px 10px;height:220px" @click="route(o.id)">
                <div style="display: flex;justify-content:center;">
                  <p
                    style="
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 1;
                        overflow: hidden;
                        word-break: break-all;
                        font-size:12px;"
                  >
                    {{ (o.title === '')? '&nbsp;' : o.title }}
                  </p>
                </div>
                <div style="margin:5px 0px">
                  <el-divider />
                </div>
                <el-image
                  style="width: 160px; height: 120px;dispaly:inline-block !important;"
                  fit="scale-down"
                  :src="o.cover"
                >
                  <div slot="error" class="image-slot">
                    <el-skeleton-item variant="image" style="width: 160px; height: 120px;" />
                  </div>
                </el-image>

                <p
                  style="
                    margin:5px 0px;
                    display: flex;
                    justify-content:center;
                    display:-webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 4;
                    overflow: hidden;
                    word-break: break-all;
                    font-size:10px;"
                >
                  {{ o.preview }}
                </p>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <!-- #endregion -->
      </el-main>

      <!-- #region 分页 -->
      <el-footer height="40px">
        <el-row justify="center" type="flex">
          <el-pagination
            layout="prev, pager, next"
            :current-page="list.current"
            :page-size="list.limit"
            :total="list.total"
            @current-change="getList"
          />
        </el-row>
      </el-footer>
      <!-- #endregion -->
    </el-container>

    <!-- 添加笔记对话框 ----------------------------------------------------------------------------------------------------------------------------------------->
    <el-dialog
      title="提示"
      :visible.sync="CreateNoteDialog.visible"
      width="30%"
    >
      <span>确认在当前文件夹中添加新的笔记？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="CreateNoteDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="createNoteFun">确 定</el-button>
      </span>
    </el-dialog>
    <!------------------------------------------------------------------------------------------------------------------------------------------------->
  </div>
</template>

<script>
import qs from 'qs'
import noteApi from '@/api/note'
import userApi from '@/api/user'

export default {
  name: 'UserNfolderIDPage',
  layout: 'BaseLayout',

  data () {
    return {
      folderInfo: '',
      list: {
        keyword: '',
        current: 1,
        limit: 20,
        total: null,
        result: []
      },
      select: {
        checkedList: [],
        selectAll: false,
        isIndeterminate: false,
        selectSum: '全选'
      },
      CreateNoteDialog: {
        visible: false,
        folderID: ''
      }
    }
  },
  created () {
    this.CreateNoteDialog.folderID = this.$route.params.id
    userApi.getNotefolderBynFolderId(this.CreateNoteDialog.folderID).then((response) => {
      this.folderInfo = response.data.data.data
    })
    this.getList()
  },
  mounted () {
  },
  methods: {
    // 获取文件夹数据
    getList (page = 1) {
      this.list.current = page
      // <- 清除选中
      if (this.list.result) {
        this.list.result.forEach((o) => {
          this.addOrRemoveStyle(o.id, false)
        })
      }
      this.select = {
        checkedList: [],
        selectAll: false,
        isIndeterminate: false,
        selectSum: '全选'
      }
      // ->

      // 封装数据
      const data = {
        page: this.list.current,
        limit: this.list.limit,
        noteFolderId: this.CreateNoteDialog.folderID,
        condition: null
      }
      if (this.list.keyword !== '') {
        data.condition = this.list.keyword
      }

      noteApi.getNotes(qs.stringify(data)).then((response) => {
        if (response.data.code === 20000) {
          this.list.result = response.data.data.items
          this.list.total = response.data.data.total

          if (this.list.result) {
            this.list.result.forEach((item) => {
              item.ischecked = false
            })
          }
        } else {
          this.$message.error('查询出错')
        }
      })
    },

    // <- 选中与全选
    // 选取全部
    handleCheckAll () {
      this.select.isIndeterminate = false

      if (this.select.selectAll) {
        this.select.checkedList = [...this.list.result]
        this.select.selectSum = '已选中' + this.select.checkedList.length + '篇笔记'
      } else {
        this.select.checkedList = []
        this.select.selectSum = '全选'
      }

      if (this.list.result) {
        this.list.result.forEach((o) => {
          o.ischecked = this.select.selectAll
          this.addOrRemoveStyle(o.id, this.select.selectAll)
        })
      }
    },
    // 选取单个
    handleChecked (o) {
      this.addOrRemoveStyle(o.id, o.ischecked)
      const include = this.select.checkedList.findIndex(O => O.id === o.id)
      if (include !== -1) {
        this.select.checkedList.splice(include, 1)
      } else {
        this.select.checkedList.push(o)
      }
      if (this.select.checkedList.length === 0) {
        this.select.selectAll = false
        this.select.isIndeterminate = false
        this.select.selectSum = '全选'
      } else {
        this.select.isIndeterminate = true
        this.select.selectSum = '已选中' + this.select.checkedList.length + '个文件夹'
      }
    },
    // 选中后修改样式以及取消选中后去除样式
    // 问题：通过 JS 设置 visibility 后，再通过 css 设置 visibility 会失效
    addOrRemoveStyle (id, checked) {
      const style = this.$refs[`Note-${id}`][0].$el.style
      if (checked) {
        style.background = '#f1f5fa'
        style.border = '1px solid #90d8ff'
      } else {
        style.background = null
        style.border = null
      }
    },
    // ->

    route (id) {
      // 如果没有被选中的笔记，则进行跳转
      if (this.select.checkedList.length === 0) {
        this.$router.push({ path: '/note/' + id })
      }
    },

    // 在当前文件夹下新建笔记
    createNoteFun () {
      noteApi.initializeNote(this.CreateNoteDialog.folderID).then((response) => {
        if (response.data.code === 20000) {
          this.getCountOfNoteInfo()
          this.$router.push({ path: `/editor/${response.data.data.data}` })
        } else {
          this.$message.error(response.data.message)
        }
      })
      this.CreateNoteDialog.visible = false
    },

    // 编辑笔记跳转
    editNote () {
      // 跳转到选中笔记的编辑页面
      if (this.select.checkedList.length === 1) {
        this.$router.push({ path: '/editor/' + this.select.checkedList[0].id })
      }
    },

    // 批量删除
    deleteNotes () {
      this.$confirm('笔记将暂时保存在回收站中，确认删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const deleteIdList = []
        this.select.checkedList.forEach((o) => {
          deleteIdList.push(o.id)
        })

        noteApi.deleteNotes(deleteIdList).then((response) => {
          if (response.data.code === 20000) {
            this.$message('删除成功')
            this.getList(this.list.current)
            this.getCountOfNoteInfo()
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {
      })
    },
    // 将笔记相关数据更新到 store
    getCountOfNoteInfo () {
      noteApi.getCountOfNoteInfo().then((response) => {
        if (response.data.code === 20000) {
          const data = response.data.data
          this.$store.dispatch('userData/updateDataCount', { data })
        }
      })
    }

  }
}
</script>
<style scoped>
.NoteFolderId-el-button{
  margin-bottom: 10px;
}

.NoteFolderId-el-card{
  padding: 0px;
  margin: 5px;
  position: relative;
}

.NoteFolderId-el-card:hover{
  background-color: #d0f0f0;
}
.NoteFolderId-el-card input {
  visibility: hidden;
}
.NoteFolderId-el-card:hover input {
  visibility: visible;
}
.NoteFolderId-el-card--select{
  border: 1px solid #90d8ff;
  background-color: #f1f5fa;
}
.el-divider--horizontal{
   margin:0px;
}
.el-button+.el-button {
    margin-left: 0px;
}

</style>
