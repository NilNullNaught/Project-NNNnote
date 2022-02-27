<template>
  <div>
    <el-container class="NoteFolderIndex-height">
      <el-main>
        <!-- 操作栏 ----------------------------------------------------------------------------------------------------------------------------------------->
        <el-row>
          <el-col :span="18">
            <el-button
              type="primary"
              @click="NfolderDialogInitialize('新建文件夹')"
            >
              新建文件夹
            </el-button>
            <el-button-group
              v-show="select.checkedList.length !== 0"
              ref="userIndex_ebg"
            >
              <el-button type="primary" @click="deleteDialog.visible = true">
                <i class="el-icon-delete el-icon--left" />
                删除文件夹
              </el-button>
              <el-button
                v-if="select.checkedList.length < 2"
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
              @change="searchNfolderANDNote"
            />
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
          <el-col v-for="(o) in list" :key="o.id" :span="4">
            <el-card :id="'ID-'+o.id" class="NoteFolderIndex-el-card" shadow="hover" style="position: relative;">
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
                  <span
                    style="
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 1;
                        overflow: hidden;
                        word-break: break-all;"
                  >{{ o.folderName }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <!----------------------------------------------------------------------------------------------------------------------------------------->

        <!-- 搜索结果列表 ----------------------------------------------------------------------------------------------------------------------------------------->
        <el-row v-if="search.isSearching">
          <el-col v-for="(so) in search.searchResult" :key="so.id" :span="4">
            <el-card :id="'SID-'+so.id" class="NoteFolderIndex-el-card" shadow="hover" style="position: relative;">
              <input
                v-if="so.id !== so.userId"
                v-model="so.ischecked"
                style="position:absolute;top: 10px;left: 10px;"
                type="checkbox"
                @change="handleChecked(so)"
              >
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
        <!----------------------------------------------------------------------------------------------------------------------------------------->
      </el-main>

      <!-- 分页 ----------------------------------------------------------------------------------------------------------------------------------->
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
            @current-change="searchNfolderANDNote"
          />
        </el-row>
      </el-footer>
    <!------------------------------------------------------------------------------------------------------------------------------------------------->
    </el-container>

    <!-- 新增与删除文件夹对话框 ----------------------------------------------------------------------------------------------------------------------------------------->
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
    <!------------------------------------------------------------------------------------------------------------------------------------------------->

    <!-- 删除确认对话框 ----------------------------------------------------------------------------------------------------------------------------------------->
    <el-dialog
      title="提示"
      :visible.sync="deleteDialog.visible"
      width="30%"
    >
      <span>确认删除{{ select.checkedList.length }}个文件夹？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="deleteUserNFolder">确 定</el-button>
      </span>
    </el-dialog>
    <!------------------------------------------------------------------------------------------------------------------------------------------------->
  </div>
</template>

<script>
import qs from 'qs'
import userApi from '@/api/user'

export default {
  name: 'UserNfolderIndexPage',
  layout: 'BaseLayout',

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
        limit: 24,
        total: null
      },
      pagination2: {
        current: 1,
        limit: 24,
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

        if (this.list) {
          this.list.forEach((item) => {
            if (item.shapes) {
              item.shapes.forEach((tem) => {
                tem.ischecked = false
              })
            }
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
      let element
      if (this.search.isSearching) {
        const ID = `SID-${id}`
        element = document.getElementById(ID)
      } else {
        const ID = `ID-${id}`
        element = document.getElementById(ID)
      }
      if (checked) {
        element.classList.add('NoteFolderIndex-el-card--select')
        // input[0].style.visibility = 'visible'
      } else {
        element.classList.remove('NoteFolderIndex-el-card--select')
        // input[0].style.visibility = 'hidden'
      }
    },
    // ->

    route (id) {
      // 如果没有被选中的文件夹，则进行跳转
      if (this.select.checkedList.length === 0) {
        this.$router.push({ path: '/user/nfolder/' + id })
      }
    },

    // 分页条件查询笔记与笔记文件夹(注意，查询笔记功能暂未完成，只能查询笔记文件夹)
    searchNfolderANDNote (page = 1) {
      if (this.search.searchKeyWord !== '') {
        this.search.isSearching = true
        this.pagination1.current = page
        this.select = {
          checkedList: [],
          selectAll: false,
          isIndeterminate: false,
          selectSum: '全选'
        }
        userApi.getNfolderANDNote(this.pagination2.current, this.pagination2.limit, this.search.searchKeyWord).then((response) => {
          this.search.searchResult = response.data.data.items
          this.pagination1.total = response.data.data.total
          this.list.forEach((item) => {
            item.shapes.forEach((tem) => {
              tem.ischecked = false
            })
          })
          // 清除选中样式
          this.list.forEach((o) => {
            this.addOrRemoveStyle(o.id, false)
          })
        })
      } else {
        // 清除选中样式
        this.search.searchResult.forEach((o) => {
          this.addOrRemoveStyle(o.id, false)
        })
        this.search.isSearching = false
        this.select = {
          checkedList: [],
          selectAll: false,
          isIndeterminate: false,
          selectSum: '全选'
        }
      }
    },

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
            this.getList()// 获取新的笔记文件夹列表
            this.select.checkedList = []// 清空 checkedList
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
    // ->

    // 批量删除
    deleteUserNFolder () {
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
      this.deleteDialog.visible = false
    }

  }
}
</script>
<style>
.NoteFolderIndex-height{
  min-height: calc(75vh);
}
.NoteFolderIndex-el-button{
  margin-bottom: 10px;
}
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
.NoteFolderIndex-el-card--select{
  border: 1px solid #90d8ff;
  background-color: #f1f5fa;
}
.el-divider--horizontal{
   margin:10px;
}
</style>
