<template>
  <div>
    <div
      style="min-height: calc(70vh);"
    >
      <!-- 操作栏 ----------------------------------------------------------------------------------------------------------------------------------------->
      <el-row>
        <el-col :span="18">
          <el-button
            size="mini"
            type="primary"
            plain
            @click="NfolderDialogInitialize('新建文件夹')"
          >
            <i class="el-icon-circle-plus-outline el-icon--left" />
            新建文件夹
          </el-button>
          <el-button
            v-show="select.checkedList.length > 0"
            size="mini"
            type="primary"
            plain
            @click="deleteUserNFolder"
          >
            <i class="el-icon-delete el-icon--left" />
            删除文件夹
          </el-button>
          <el-button
            v-show="select.checkedList.length == 1"

            size="mini"
            type="primary"
            plain
            @click="NfolderDialogInitialize('编辑文件夹')"
          >
            <i class="el-icon-edit el-icon--left" />
            编辑文件夹
          </el-button>
        </el-col>
        <el-col :span="6">
          <!-- <el-input
            v-model="search.searchKeyWord"
            size="mini"
            placeholder="请输入内容"
            clearable
          /> -->
        </el-col>
      </el-row>
      <!----------------------------------------------------------------------------------------------------------------------------------------->

      <!-- 全选框 ----------------------------------------------------------------------------------------------------------------------------------------->
      <div style="margin: 10px;">
        <el-checkbox
          v-model="select.selectAll"
          :indeterminate="select.isIndeterminate"
          :label="select.selectSum"
          @change="handleCheckAll"
        />
      </div>
      <!----------------------------------------------------------------------------------------------------------------------------------------->

      <el-divider />

      <!-- 用户文件夹列表 ----------------------------------------------------------------------------------------------------------------------------------------->
      <el-row v-if="!search.isSearching">
        <el-col v-for="(o) in list" :key="o.id" :lg="{span: '4-8'}">
          <el-tooltip class="item" effect="dark" placement="top">
            <div slot="content">
              笔记数量: {{ o.noteCount }}<br>描述: {{ o.folderDescription }}
            </div>
            <el-card
              :ref="'NoteFolder-'+o.id"
              :body-style="{ padding: '10px' }"
              class="NoteFolderIndex-el-card"
              shadow="hover"
              style="position: relative;"
            >
              <input
                v-if="o.id !== o.userId"
                :ref="'NoteFolderInput-'+o.id"
                v-model="o.ischecked"
                style="position:absolute;top: 10px;left: 10px;"
                type="checkbox"
                @change="handleChecked(o)"
              >

              <nuxt-link :to="'/user/nfolder/'+o.id">
                <div style="display: flex;justify-content:center;">
                  <img width="56px" height="56px" src="~/assets/img/mine/folder.png" alt>
                </div>
                <div style="display: flex;justify-content:center;">
                  <span

                    style="
                      font-size: 12px;
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 1;
                        overflow: hidden;
                        word-break: break-all;"
                  >{{ (o.folderName === '')? '&nbsp;' : o.folderName }}</span>
                </div>
              </nuxt-link>
            </el-card>
          </el-tooltip>
        </el-col>
      </el-row>
      <!----------------------------------------------------------------------------------------------------------------------------------------->

      <!-- 搜索结果列表 ----------------------------------------------------------------------------------------------------------------------------------------->
      <el-row v-if="search.isSearching">
        <el-col v-for="(so) in search.searchResult" :key="so.id" :lg="{span: '4-8'}">
          <el-card
            :ref="'NoteFolder-'+so.id"
            class="NoteFolderIndex-el-card"
            shadow="hover"
            style="position: relative;"
          >
            <input
              v-if="so.id !== so.userId"
              v-model="so.ischecked"
              style="position:absolute;top: 10px;left: 10px;"
              type="checkbox"
              @change="handleChecked(so)"
            >
            <div style="display: flex;justify-content:center;">
              <img src="~/assets/img/mine/note.png" alt>
            </div>
            <div style="display: flex;justify-content:center;">
              <span>{{ so.folderName }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!----------------------------------------------------------------------------------------------------------------------------------------->
    </div>
    <!-- 分页 ----------------------------------------------------------------------------------------------------------------------------------->
    <el-row justify="center" type="flex">
      <el-pagination
        v-if="!search.isSearching"
        layout="prev, pager, next"
        :current-page="pagination1.current"
        :page-size="pagination1.limit"
        :total="pagination1.total"
        @current-change="getList"
      />
    </el-row>
    <!------------------------------------------------------------------------------------------------------------------------------------------------->

    <!-- 新增与修改文件夹对话框 ----------------------------------------------------------------------------------------------------------------------------------------->
    <el-dialog
      width="30%"
      :title="NfolderDialog.title"
      :visible.sync="NfolderDialog.visible"
      append-to-body
    >
      <el-row justify="center" type="flex">
        <el-col :span="20">
          <el-form :model="NfolderDialog.form">
            <el-form-item
              prop="nfolderName"
              :rules="[
                { required: true, trigger: ['blur'], message: '文件夹名不能为空'}]"
            >
              <el-input
                v-model="NfolderDialog.form.nfolderName"
                maxlength="10"
                show-word-limit
                placeholder="请输入文件夹名"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="NfolderDialog.form.description"
                maxlength="90"
                :autosize="{ minRows: 5, maxRows: 6}"
                type="textarea"
                placeholder="请输入描述信息"
              />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>

      <span slot="footer" class="dialog-footer">
        <el-button @click="NfolderDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="submitNfolder()">确认</el-button>
      </span>
    </el-dialog>
    <!------------------------------------------------------------------------------------------------------------------------------------------------->
  </div>
</template>
<script>
import qs from 'qs'
import userApi from '@/api/user'

export default {
  name: 'NoteNfolderComponent',
  data () {
    return {
      list: [],
      select: {
        checkedList: [],
        selectAll: false,
        isIndeterminate: false,
        selectSum: '全选'
      },
      pagination1: {
        current: 1,
        limit: 20,
        total: null
      },
      NfolderDialog: {
        visible: false,
        title: '',
        form: {
          nfolderName: '',
          description: ''
        }
      },
      search: {
        isSearching: false,
        searchKeyWord: '',
        searchResult: []
      }
    }
  },
  created () {
    this.getList()
  },
  methods: {
    // 获取文件夹数据
    getList (page = 1) {
      this.pagination1.current = page

      this.select = {
        checkedList: [],
        selectAll: false,
        isIndeterminate: false,
        selectSum: '全选'
      }
      userApi.getUserNfolderPage(this.pagination1.current, this.pagination1.limit).then((response) => {
        this.list = response.data.data.items
        this.pagination1.total = response.data.data.total

        if (this.list) {
          this.list.forEach((item) => {
            item.ischecked = false
          })
        }
      })
    },
    // <- 选中与全选
    // 选取全部
    handleCheckAll () {
      this.select.isIndeterminate = false

      if (this.select.selectAll) {
        this.select.checkedList = [...this.list]
        const hasDefaultFolder = this.select.checkedList.findIndex(o => o.id === o.userId)
        if (hasDefaultFolder !== -1) {
          this.select.checkedList.splice(hasDefaultFolder, 1)
        }
        this.select.selectSum = '已选中' + this.select.checkedList.length + '个文件夹'
      } else {
        this.select.checkedList = []
        this.select.selectSum = '全选'
      }

      if (this.search.isSearching) {
        this.search.searchResult.forEach((o) => {
          if (o.id !== o.userId) {
            o.ischecked = this.select.selectAll
            this.addOrRemoveStyle(o.id, this.select.selectAll)
          }
        })
      } else {
        this.list.forEach((o) => {
          if (o.id !== o.userId) {
            o.ischecked = this.select.selectAll
            this.addOrRemoveStyle(o.id, this.select.selectAll)
          }
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
      const style = this.$refs[`NoteFolder-${id}`][0].$el.style
      if (checked) {
        style.background = '#f1f5fa'
        style.border = '1px solid #90d8ff'
      } else {
        style.background = null
        style.border = null
      }
    },
    // ->

    // <- NfolderDialog
    // 对话框初始化
    NfolderDialogInitialize (title) {
      this.NfolderDialog.title = title
      this.NfolderDialog.visible = true
      if (title === '编辑文件夹') {
        this.NfolderDialog.form.nfolderName = this.select.checkedList[0].folderName
        this.NfolderDialog.form.description = this.select.checkedList[0].folderDescription
      } else {
        this.NfolderDialog.form.nfolderName = ''
        this.NfolderDialog.form.description = ''
      }
    },
    // 创建或编辑新的笔记文件夹
    submitNfolder () {
      if (this.NfolderDialog.title === '编辑文件夹') {
        const data = {
          nfolderID: this.select.checkedList[0].id,
          folderName: this.NfolderDialog.form.nfolderName,
          description: this.NfolderDialog.form.description
        }
        userApi.alterUserNfolder(qs.stringify((data))).then((response) => {
          if (response.data.code === 20000) {
            this.getList(this.pagination1.current)// 获取新的笔记文件夹列表
            this.select.checkedList = []// 清空 checkedList
            this.NfolderDialog.visible = false
          } else {
            this.$message.error(response.data.message)
          }
        })
      } else {
        userApi.addUserNfolder(qs.stringify(this.NfolderDialog.form)).then((response) => {
          if (response.data.code === 20000) {
            this.getList(this.pagination1.current)// 获取新的笔记文件夹列表
            this.NfolderDialog.visible = false
          } else {
            this.$message.error(response.data.message)
          }
        })
      }
    },
    // ->

    // 批量删除
    deleteUserNFolder () {
      this.$confirm('删除后无法恢复，是否确认？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const deleteIdList = []
        this.select.checkedList.forEach((o) => {
          deleteIdList.push(o.id)
        })

        userApi.deleteUserNFolder(deleteIdList).then((response) => {
          if (response.data.code === 20000) {
            this.$message('删除成功')
            this.getList()
            this.select.checkedList = []
            this.select.selectAll = false
            this.select.isIndeterminate = false
            this.select.selectSum = '全选'
          } else {
            this.$message.error(response.data.message)
          }
        })
      }).catch(() => {
      })
    }
  }
}
</script>

<style scoped>
.NoteFolderIndex-el-card{
  margin:10px;
}
.NoteFolderIndex-el-card:hover{
  background-color: #d0f0f0;
}
.NoteFolderIndex-el-card input {
  visibility: hidden;
}
.NoteFolderIndex-el-card:hover input {
  visibility: visible;
}
.el-divider--horizontal{
   margin:10px;
}
.el-button+.el-button {
    margin-left: 0px;
}
.el-col-lg-4-8 {
    width: 20%;
}
</style>
