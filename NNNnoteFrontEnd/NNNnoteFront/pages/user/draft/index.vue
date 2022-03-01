<template>
  <div>
    <el-container>
      <el-main>
        <el-row>
          <el-col :offset="9" :span="6" align="center">
            <div style="margin:10px">
              <el-tooltip class="item" effect="dark" content="存放没有保存的笔记" placement="right-start">
                <el-badge is-dot class="item">
                  <span style="font-size: 20px;font-weight:bolder;padding:0px 3px">草稿箱</span>
                </el-badge>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>

        <div style="margin-bottom: 10px">
          <el-button size="mini" type="primary" plain @click="toggleSelection()">
            <i class="el-icon-close" />
            取消选中
          </el-button>
          <el-button size="mini" type="primary" plain @click="deleteSelection()">
            <i class="el-icon-delete" />
            删除选中
          </el-button>
        </div>

        <el-table
          ref="multipleTable"
          :data="list.result"
          tooltip-effect="dark"
          style="width: 100%"
          stripe
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            type="selection"
            width="55"
            fixed
          />
          <el-table-column
            prop="title"
            label="标题"
            width="120"
            fixed
          />
          <el-table-column
            prop="preview"
            label="预览"
            width="300"
          />

          <el-table-column
            prop="noteFolderId"
            label="所属文件夹"
            width="120"
          />
          <el-table-column
            prop="gmtCreate"
            label="创建时间"
            width="170"
          />
          <el-table-column
            prop="gmtModified"
            label="最后修改时间"
            width="170"
          />

          <el-table-column
            fixed="right"
            label="操作"
            width="150"
          >
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="$router.push({ path: '/note/' + scope.row.id })">
                查看
              </el-button>
              <el-button type="text" size="mini" @click="$router.push({ path: '/editor/' + scope.row.id })">
                编辑
              </el-button>
              <el-button type="text" size="mini" @click="deleteSingle(scope.row.id)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>

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
    </el-container>
  </div>
</template>

<script>
import qs from 'qs'
import noteApi from '@/api/note'
import userApi from '@/api/user'

export default {
  name: 'UserDraftIndexPage',
  layout: 'BaseLayout',
  data () {
    return {
      list: {
        keyword: '',
        current: 1,
        limit: 10,
        total: null,
        result: []
      },
      folderNameList: [],
      multipleSelection: []
    }
  },
  created () {
    this.getList()
  },
  methods: {
    // 查询数据
    getList (page = 1) {
      this.list.current = page

      // 封装数据
      const data = {
        page: this.list.current,
        limit: this.list.limit
      }
      noteApi.getDraftList(qs.stringify(data)).then((response) => {
        if (response.data.code === 20000) {
          const result = response.data.data.items
          this.list.total = response.data.data.total

          const folderIdList = []
          if (result.length === 0) {
            this.list.result = []
            return
          }
          if (result) {
            result.forEach((o) => {
              if (!folderIdList.includes(o.noteFolderId)) { folderIdList.push(o.noteFolderId) }
            })
          }
          if (folderIdList.length !== 0) {
            userApi.getNoteFolderNameByFolderId(folderIdList).then((response) => {
              if (response.data.code === 20000) {
                this.folderNameList = response.data.data.data
                if (result) {
                  result.forEach((o) => {
                    o.noteFolderId = this.formatFolderName(o.noteFolderId)
                    o.gmtCreate = this.formatDate(o.gmtCreate)
                    o.gmtModified = this.formatDate(o.gmtModified)
                  })
                }
                this.list.result = result
              }
            })
          }
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
    // 格式化文件夹名
    formatFolderName (data) { // 设置时间格式
      if (data == null) {
        return null
      }
      const folderName = this.folderNameList[`${data}`]
      if (folderName !== null) {
        return folderName
      } else {
        return "<span style='color:red'>文件夹已被删除</span>"
      }
    },

    // 取消选择
    toggleSelection (rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    // 全选与取消全选
    handleSelectionChange (val) {
      this.multipleSelection = val
    },

    // 删除单个
    deleteSingle (id) {
      const idList = [`${id}`]
      this.deleteDrafts(idList)
    },

    // 删除选中
    deleteSelection () {
      const idList = []

      if (this.multipleSelection) {
        this.multipleSelection.forEach((o) => {
          idList.push(o.id)
        })
      }
      this.deleteDrafts(idList)
    },
    deleteDrafts (idList) {
      this.$confirm('删除后不可恢复, 是否确认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        noteApi.deleteDrafts(idList).then((response) => {
          if (response.data.code === 20000) {
            this.$message('删除成功')
            this.getList(this.list.current)
            this.getCountOfNoteInfo()
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
.el-container {
     min-height: calc(80vh);
}
</style>
