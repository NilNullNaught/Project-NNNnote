<template>
  <div>
    <el-container>
      <el-main>
        <!-- #region 笔记列表 -->
        <el-row>
          <el-col v-for="(o) in list" :key="o.id" :span="6">
            <el-card
              :ref="'Note-'+o.id"
              class="NoteFolderId-el-card"
              shadow="hover"
              :body-style="{ padding: '10px' }"
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
      </el-main>
      <!-- #endregion -->

      <!-- 分页 ----------------------------------------------------------------------------------------------------------------------------------->
      <el-footer height="40px">
        <el-row justify="center" type="flex">
          <el-pagination
            layout="prev, pager, next"
            :current-page="data.page"
            :page-size="data.limit"
            :total="data.total"
            @current-change="getList"
          />
        </el-row>
      </el-footer>
      <!------------------------------------------------------------------------------------------------------------------------------------------------->
    </el-container>
  </div>
</template>
<script>
import noteApi from '@/api/note'

export default {
  name: 'VisitorPublicNoteComponent',
  props: {
    userId: {
      type: String,
      required: false,
      default: ''
    }
  },
  data () {
    return {
      data: {
        limit: 5,
        page: 1,
        sortField: 'gmt_create',
        userId: null,
        total: 0
      },
      list: null
    }
  },
  created () {
    this.getList()
  },
  methods: {
    // 获取文件夹数据
    getList (page = 1) {
      this.data.userId = this.userId
      noteApi.getPublicNotes(this.data)
        .then((response) => {
          this.list = response.data.data.data
          this.data.total = response.data.data.total
        })
    }

  }
}
</script>

<style scoped>
.NoteFolderId-el-card{
  padding: 0px;
  margin: 5px;
  position: relative;
}

.NoteFolderId-el-card:hover{
  background-color: #d0f0f0;
}
</style>
