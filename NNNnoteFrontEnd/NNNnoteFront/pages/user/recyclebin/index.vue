<template>
  <div>
    <el-container>
      <el-main>
        <el-row>
          <el-col :offset="9" :span="6" align="center">
            <div style="margin:10px">
              <el-tooltip class="item" effect="dark" content="暂时存放被删除的笔记" placement="right-start">
                <el-badge is-dot class="item">
                  <span style="font-size: 20px;font-weight:bolder;padding:0px 3px">回收站</span>
                </el-badge>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>

        <div style="margin-bottom: 10px">
          <el-button type="primary" plain size="small" @click="toggleSelection()">
            <i class="el-icon-close" />
            取消选中
          </el-button>
          <el-button type="primary" plain size="small" @click="deleteSelection()">
            <i class="el-icon-delete" />
            删除选中
          </el-button>
          <el-button type="primary" plain size="small" @click="restoreSelection()">
            <i class="el-icon-upload2" />
            还原选中
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
            prop="gmtModified"
            label="剩余时间"
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
            fixed="right"
            label="操作"
            width="100"
          >
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="restoreSingle(scope.row.id)">
                还原
              </el-button>
              <el-button type="text" size="small" @click="deleteSingle(scope.row.id)">
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
  name: 'UserRecyclebinIndexPage',
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
      noteApi.getLogicDeletedNoteList(qs.stringify(data)).then((response) => {
        if (response.data.code === 20000) {
          const result = response.data.data.items
          this.list.total = response.data.data.total

          const folderIdList = []
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
                    o.gmtModified = this.formatCountdown(o.gmtModified)
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
      return this.folderNameList[`${data}`]
    },
    // 格式化文件夹名
    formatCountdown (data) { // 设置时间格式
      if (data == null) {
        return null
      }
      // 指定日期和时间
      const DeleteTime = new Date(data)
      // 当前系统时间
      const NowTime = new Date()
      const t = 7 * 24 * 60 * 60 * 1000 - (NowTime.getTime() - DeleteTime.getTime())

      return `${Math.floor(t / (1000 * 60 * 60 * 24))} 天 ` +
             `${Math.floor(t / (1000 * 60 * 60) % 24)} 小时 `
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

      if (this.multipleSelection && this.multipleSelection.length >= 1) {
        this.multipleSelection.forEach((o) => {
          idList.push(o.id)
        })
        this.deleteDrafts(idList)
      }
    },
    deleteDrafts (idList) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        noteApi.deleteDrafts(idList).then((response) => {
          if (response.data.code === 20000) {
            this.$message('删除成功')
            this.getList(this.list.current)
          }
        })
      }).catch(() => {
      })
    },
    // 还原单个
    restoreSingle (id) {
      const idList = [`${id}`]
      this.restoreDeletedNote(idList)
    },
    // 还原选中
    restoreSelection () {
      const idList = []

      if (this.multipleSelection && this.multipleSelection.length >= 1) {
        this.multipleSelection.forEach((o) => {
          idList.push(o.id)
        })
        this.restoreDeletedNote(idList)
      }
    },
    restoreDeletedNote (idList) {
      this.$confirm('确认还原？（如果笔记文件夹已经删除，笔记将会转移到默认文件夹）', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        noteApi.restoreDeletedNote(idList).then((response) => {
          if (response.data.code === 20000) {
            this.$message('成功')
            this.getList(this.list.current)
          }
        })
      }).catch(() => {
      })
    }

  }
}

</script>

<style>
.el-container {
     min-height: calc(80vh);
}
</style>
