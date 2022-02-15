<template>
  <div>
    <el-container class="userindex-height">
      <el-main>
        <el-row>
          <el-col :span="18">
            <el-button
              type="primary"
              :disabled="search.isSearching"
              @click="NfolderDialogInitialize('新建文件夹')"
            >
              新建文件夹
            </el-button>
            <el-button-group
              v-show="checkedList.length !== 0"
              ref="userIndex_ebg"
              :disabled="search.isSearching"
            >
              <el-button type="primary" @click="deleteDialog.visible = true">
                <i class="el-icon-delete el-icon--left" />
                删除文件夹
              </el-button>
              <el-button
                v-show="checkedList.length < 2"
                type="primary"
                @click="NfolderDialogInitialize('编辑文件夹')"
              >
                <i class="el-icon-edit el-icon--left" />
                编辑文件夹
              </el-button>
            </el-button-group>
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="search.searchKeyWord"
              placeholder="请输入内容"
              clearable
              @change="searchNfolderANDNote()"
            />
          </el-col>
        </el-row>

        <div style="margin:10px">
          <el-checkbox
            v-model="selectAll"
            :disabled="search.isSearching"
            :indeterminate="isIndeterminate"
            :label="selectSum"
            @change="handleCheckAll"
          />
        </div>

        <el-divider />

        <el-row v-if="!search.isSearching">
          <el-col v-for="(o) in list" :key="o.id" :span="4">
            <el-card :id="'no'+o.id" class="userindex-el-card" shadow="hover" style="position: relative;">
              <input
                v-if="o.id !== o.userId"
                v-model="o.ischecked"
                style="position:absolute;top: 10px;left: 10px;"
                type="checkbox"
                @change="handleChecked(o)"
              >

              <div @click="route(o.id)">
                <div style="display: flex;justify-content:center;">
                  <img src="~/assets/img/mine/folder.png" alt>
                </div>
                <div style="display: flex;justify-content:center;">
                  <span>{{ o.folderName }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row v-if="search.isSearching">
          <el-col v-for="(so) in search.searchResult" :key="so.id" :span="4">
            <el-card :id="'sno'+so.id" class="userindex-el-card" shadow="hover" style="position: relative;">
              <div @click="route($event,so.id)">
                <div style="display: flex;justify-content:center;">
                  <img src="~/assets/img/mine/note.png" alt>
                </div>
                <div style="display: flex;justify-content:center;">
                  <span>{{ so.folderName }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>

      <el-footer height="30px">
        <el-row justify="center" type="flex">
          <el-pagination
            v-if="!search.isSearching"
            layout="prev, pager, next"
            :current-page="pagination1.current"
            :page-size="pagination1.limit"
            :total="pagination1.total"
            @current-change="getList"
          />
          <el-pagination
            v-if="search.isSearching"
            layout="prev, pager, next"
            :current-page="pagination2.current"
            :page-size="pagination2.limit"
            :total="pagination2.total"
          />
        </el-row>
      </el-footer>
    </el-container>

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
                maxlength="30"
                show-word-limit
                type="textarea"
                placeholder="请输入描述"
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

    <el-dialog
      title="提示"
      :visible.sync="deleteDialog.visible"
      width="30%"
      :before-close="handleClose"
    >
      <span>确认删除{{ checkedList.length }}个文件夹？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="deleteUserNFolder">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import qs from 'qs'
import userApi from '@/api/user'

export default {
  name: 'UserIndexPage',
  layout: 'BaseLayout',

  data () {
    return {
      list: [],
      checkedList: [],
      selectAll: false,
      isIndeterminate: false,
      selectSum: '全选',
      pagination1: {
        current: 1,
        limit: 24,
        total: ''
      },
      pagination2: {
        current: 1,
        limit: 24,
        total: ''
      },
      NfolderDialog: {
        visible: false,
        title: '',
        form: {
          nfolderName: '',
          description: ''
        }
      },
      deleteDialog: {
        visible: false
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
  mounted () {
  },
  methods: {
    // 获取文件夹数据
    getList (page = 1) {
      this.pagination1.current = page
      userApi.getUserNfolderPage(this.pagination1.current, this.pagination1.limit).then((response) => {
        this.list = response.data.data.items
        this.pagination1.total = response.data.data.total

        this.list.forEach((item) => {
          item.shapes.forEach((tem) => {
            tem.ischecked = false
          })
        })
      })
    },

    // <- 选中与全选
    // 选取全部，参数 clear 用于在方法中判断是否需要清空
    handleCheckAll () {
      if (this.selectAll) {
        this.checkedList = [...this.list]
        const hasDefaultFolder = this.checkedList.findIndex(o => o.id === o.userId)
        if (hasDefaultFolder !== -1) {
          this.checkedList.splice(hasDefaultFolder, 1)
        }
        this.selectSum = '已选中' + this.checkedList.length + '个文件夹'
      } else {
        this.checkedList = []
        this.selectSum = '全选'
      }
      this.isIndeterminate = false

      this.list.forEach((o) => {
        if (o.id !== o.userId) {
          o.ischecked = this.selectAll
          this.addOrRemoveStyle(o.id, this.selectAll)
        }
      })
    },
    // 选取单个
    handleChecked (o) {
      this.addOrRemoveStyle(o.id, o.ischecked)
      const include = this.checkedList.findIndex(O => O.id === o.id)
      if (include !== -1) {
        this.checkedList.splice(include, 1)
      } else {
        this.checkedList.push(o)
      }
      if (this.checkedList.length === 0) {
        this.selectAll = false
        this.isIndeterminate = false
        this.selectSum = '全选'
      } else {
        this.isIndeterminate = true
        this.selectSum = '已选中' + this.checkedList.length + '个文件夹'
      }
    },
    // 选中后修改样式以及取消选中后去除样式
    // 问题：通过 JS 设置 visibility 后，再通过 css 设置 visibility 会失效
    addOrRemoveStyle (id, checked) {
      let element
      if (this.search.isSearching) {
        element = document.getElementById(`sno${id}`)
      } else {
        element = document.getElementById(`no${id}`)
      }

      if (checked) {
        element.classList.add('userindex-el-card--select')
        // input[0].style.visibility = 'visible'
      } else {
        element.classList.remove('userindex-el-card--select')
        // input[0].style.visibility = 'hidden'
      }
    },
    // 选中与全选->

    route (id) {
      // 如果没有被选中的文件夹，则进行跳转
      if (this.checkedList.length === 0) {
        this.$router.push({ path: '/user/nfolder/' + id })
      }
    },

    // 分页条件查询 笔记与笔记文件夹
    searchNfolderANDNote (page = 1) {
      if (this.search.searchKeyWord !== '') {
        this.search.isSearching = true
        this.pagination1.current = page
        userApi.getNfolderANDNote(this.pagination2.current, this.pagination2.limit, this.search.searchKeyWord).then((response) => {
          this.search.searchResult = response.data.data.items
          this.pagination1.total = response.data.data.total
        })
      } else {
        this.search.isSearching = false
      }
    },

    // <- NfolderDialog
    // 对话框初始化
    NfolderDialogInitialize (title) {
      this.NfolderDialog.title = title
      this.NfolderDialog.visible = true
      if (title === '编辑文件夹') {
        this.NfolderDialog.form.nfolderName = this.checkedList[0].folderName
        this.NfolderDialog.form.description = this.checkedList[0].folderDescription
      } else {
        this.NfolderDialog.form.nfolderName = ''
        this.NfolderDialog.form.description = ''
      }
    },
    // 创建或编辑新的笔记文件夹
    submitNfolder () {
      if (this.NfolderDialog.title === '编辑文件夹') {
        const data = {
          nfolderID: this.checkedList[0].id,
          folderName: this.NfolderDialog.form.nfolderName,
          description: this.NfolderDialog.form.description
        }
        userApi.alterUserNfolder(qs.stringify((data))).then((response) => {
          if (response.data.code === 20000) {
            this.getList()// 获取新的笔记文件夹列表
            this.checkedList = []// 清空 checkedList
            this.NfolderDialog.visible = false
          } else {
            this.$message.error(response.data.message)
          }
        })
      } else {
        userApi.addUserNfolder(qs.stringify(this.NfolderDialog.form)).then((response) => {
          if (response.data.code === 20000) {
            this.getList()
            this.NfolderDialog.visible = false
          } else {
            this.$message.error(response.data.message)
          }
        })
      }
    },
    // NfolderDialog ->

    // 批量删除
    deleteUserNFolder () {
      const deleteIdList = []
      this.checkedList.forEach((o) => {
        deleteIdList.push(o.id)
      })

      userApi.deleteUserNFolder(deleteIdList).then((response) => {
        if (response.data.code === 20000) {
          this.$message('删除成功')
          this.getList()
          this.checkedList = []
          this.selectAll = false
          this.isIndeterminate = false
          this.selectSum = '全选'
        } else {
          this.$message.error(response.data.message)
        }
      })
      this.deleteDialog.visible = false
    }
  }
}
</script>
<style>
.userindex-height{
  min-height: calc(75vh);
}
.userindex-el-button{
  margin-bottom: 10px;
}
.userindex-el-card{
  margin:10px;
}
.userindex-el-card:hover{
  background-color: #f1f5fa;
}
.userindex-el-card input {
  visibility: hidden;
}
.userindex-el-card:hover input {
  visibility: visible;
}
.userindex-el-card--select{
  border: 1px solid #90d8ff;
  background-color: #f1f5fa;
}
.el-divider--horizontal{
   margin:10px;
}
</style>
