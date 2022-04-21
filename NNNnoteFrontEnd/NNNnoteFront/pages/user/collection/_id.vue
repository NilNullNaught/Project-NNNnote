<template>
  <div>
    <el-container>
      <el-main>
        <el-page-header :content="folderInfo.folderName" @back="$router.push({ path: `/user` })" />

        <!-- #region 操作栏 -->
        <el-row style="margin-top:20px;" align="middle" type="flex">
          <el-col :span="16">
            <el-button
              size="mini"
              type="primary"
              plain
              @click="cancelCollectNote"
            >
              <i class="el-icon-star-off" />
              取消收藏
            </el-button>
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
                v-model="o.ischecked"
                style="position:absolute;top: 10px;left: 5px;height:14px;width:14px;"
                type="checkbox"
                @change="handleChecked(o)"
              >

              <nuxt-link :to="'/note/'+o.id">
                <div style="margin:0px 10px;height:220px">
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
                    v-text="o.preview"
                  />
                </div>
              </nuxt-link>
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
  </div>
</template>

<script>
// import qs from 'qs'
import noteApi from '@/api/note'
import userApi from '@/api/user'

export default {
  name: 'UserNfolderIDPage',
  layout: 'BaseLayout',

  data () {
    return {
      folderInfo: '',
      list: {
        current: 1,
        limit: 12,
        total: null,
        result: []
      },
      select: {
        checkedList: [],
        selectAll: false,
        isIndeterminate: false,
        selectSum: '全选'
      }
    }
  },
  created () {
    this.getCfolderInfo()
    this.getList()
  },

  methods: {
    // 获取收藏夹信息
    getCfolderInfo () {
      const param = { cfolderId: this.$route.params.id }
      userApi.getUserCfolderById(param).then((response) => {
        this.folderInfo = response.data.data.data
      })
    },
    // 获取收藏夹数据
    getList (page = 1) {
      this.list.current = page

      // #region <- 清除选中 ->
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
      // #endregion

      // 封装数据
      const data = {
        page: this.list.current,
        limit: this.list.limit,
        cfolderId: this.$route.params.id
      }

      noteApi.getNoteInCfolderPaging(data).then((response) => {
        if (response.data.code === 20000) {
          if (response.data.data == null) {
            this.list.result = []
            return
          }
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
        this.select.selectSum = '已选中' + this.select.checkedList.length + '篇笔记'
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

    // 取消收藏
    cancelCollectNote () {
      const param = { cfolderId: this.$route.params.id }
      const deleteIdList = []
      this.select.checkedList.forEach((o) => {
        deleteIdList.push(o.id)
      })
      noteApi.cancelCollectNote(param, deleteIdList)
        .then((response) => {
          if (response.data.code === 20000) {
            this.getList(this.list.current)
            // 更新笔记的收藏数
            noteApi.updateNoteCollectionCount(deleteIdList)
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
