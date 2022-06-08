<template>
  <div
    style="min-height: calc(70vh);position:relative;"
  >
    <el-button style="position:absolute;right:0px;" @click="followListDialog.visible = true">
      关注列表
    </el-button>

    <div style="height:50px;" />

    <el-card
      v-for="(item) in dynamicList.items"
      :key="item.id"
      style="margin-bottom:20px;"
      :body-style="{ padding: '15px' }"
    >
      <div class="centerVertical" style="margin-bottom:5px;">
        <nuxt-link :to="'/visitor/'+item.userId">
          <el-avatar style="display:inline-block;" :size="40" :src="getFollowUserInfo(item.userId).avatar" />
        </nuxt-link>

        <div class="centerVertical" style="position:relative;display:inline-block;font-size:12px;color:#606266;line-height: 10px;">
          <nuxt-link :to="'/visitor/'+item.userId">
            <el-button type="text" style="margin-left:10px;padding:3px;">
              <!-- {{ getFollowUserInfo(item.userId).id }} -->
              {{ getFollowUserInfo(item.userId).nickname }}
            </el-button>
          </nuxt-link>

          <p style="margin-left:10px;">
            {{ item.gmtCreate }}
            <span v-if="item.dynamicType == 1">公开了笔记</span>
            <span v-else-if="item.dynamicType == 2">发表了评论</span>
            <span v-else-if="item.dynamicType == 3">关注了用户</span>
          </p>
        </div>
      </div>
      <div v-if="item.dynamicType == 1">
        <el-card :body-style="{ padding: '10px' }">
          <div style="display:inline-block;width:620px">
            <nuxt-link :to="'/note/'+item.id">
              <p class="card-title" v-text="getExtraData(item.dynamicId).title" />
            </nuxt-link>
            <nuxt-link :to="'/note/'+item.id">
              <p class="card-preview" v-text="getExtraData(item.dynamicId).preview" />
            </nuxt-link>

            <div class="centerVertical card-span">
              <el-divider direction="vertical" />
              <span><i class="alibaba_icons_good" /> {{ getExtraData(item.dynamicId).likes }}</span>

              <el-divider direction="vertical" />
              <span><i class="el-icon-chat-line-round" /> {{ getExtraData(item.dynamicId).commentCount }}</span>

              <el-divider direction="vertical" />
              <span><i class="el-icon-star-off" /> {{ getExtraData(item.dynamicId).collectionCount }}</span>
            </div>
          </div>
          <nuxt-link :to="'/note/'+getExtraData(item.dynamicId).id">
            <el-image
              :src="getExtraData(item.dynamicId).cover"
              style="margin:0px 5px;width: 160px; height: 120px;dispaly:inline-block;"
              fit="scale-down"
            >
              <div slot="error" class="image-slot">
                <el-skeleton-item variant="image" style="width: 160px; height: 120px;" />
              </div>
            </el-image>
          </nuxt-link>
        </el-card>
      </div>

      <div v-else-if="item.dynamicType == 2">
        <el-card :body-style="{ padding: '10px' }">
          <div class="centerVertical">
            <nuxt-link :to="'/visitor/'+item.userId">
              <el-button style="padding:0px;margin-left:5px;" type="text">
                {{ getExtraData(item.dynamicId).nickname }}
              </el-button>
            </nuxt-link>

            <div v-if="getExtraData(item.dynamicId).replyNickname">
              <span style="margin:0 5px;">回复</span>

              <nuxt-link :to="'/visitor/'+getExtraData(item.dynamicId).replyUserId">
                <el-button style="padding:0px" type="text">
                  {{ getExtraData(item.dynamicId).replyNickname }}
                </el-button>
              </nuxt-link>
            </div>
          </div>
          <div style="margin-left:40px;">
            {{ getExtraData(item.dynamicId).content }}
          </div>
          <div style="margin-left:40px">
            <el-button type="text" size="small">
              <i class="alibaba_icons_good" />{{ getExtraData(item.dynamicId).likes }}
            </el-button>
          </div>
        </el-card>
      </div>

      <div v-else-if="item.dynamicType == 3">
        <el-card :body-style="{ padding: '10px' }">
          <div
            class="centerVertical"
            style="position:relative;"
          >
            <el-avatar style="display:inline-block;margin:0 20px;" :src="getExtraData(item.dynamicId).avatar" :size="80" />
            <div
              style="display:inline-block;
          font-size:12px;
          color:#606266;
          line-height: 24px;"
            >
              <p style="font-size:20px;font-weight:bolder">
                {{ getExtraData(item.dynamicId).nickname }}
              </p>
              <p>{{ getExtraData(item.dynamicId).sign }}</p>
            </div>
          </div>
        </el-card>
      </div>
    </el-card>

    <!-- 分页 ----------------------------------------------------------------------------------------------------------------------------------->
    <el-footer height="40px">
      <el-row justify="center" type="flex">
        <el-pagination
          layout="prev, pager, next"
          :current-page="page.current"
          :page-size="page.size"
          :total="page.total"
          @current-change="getFollowUserDynamic"
        />
      </el-row>
    </el-footer>
    <!------------------------------------------------------------------------------------------------------------------------------------------------->

    <!-- #region 关注列表 -->
    <el-dialog
      title="关注列表"
      :visible.sync="followListDialog.visible"
      width="600px"
      :lock-scroll="false"
      center
    >
      <el-table :data="followListDialog.list" :show-header="false" height="calc(40vh)">
        <el-table-column width="50">
          <template slot-scope="scope">
            <nuxt-link :to="'/visitor/'+scope.row.id">
              <el-avatar style="margin-right:5px" :src="scope.row.avatar" :size="25" />
            </nuxt-link>
          </template>
        </el-table-column>

        <el-table-column width="400">
          <template slot-scope="scope">
            <nuxt-link :to="'/visitor/'+scope.row.id">
              <span> {{ scope.row.nickname }}</span>
            </nuxt-link>
          </template>
        </el-table-column>

        <el-table-column width="100">
          <template slot-scope="scope">
            <el-button type="info" @click="followAndCancel(scope.row.id)">
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button @click="followListDialog.visible = false">关闭</el-button>
      </span>
    </el-dialog>
    <!-- #endregion -->
  </div>
</template>
<script>
import qs from 'qs'
import userApi from '@/api/user'

export default {
  name: 'UserDynamicComponent',
  data () {
    return {
      followListDialog: {
        visible: false,
        list: null
      },
      dynamicList: {
        extraData: {

        },
        items: []
      },
      page: {
        size: 5,
        current: 1,
        total: 0
      }
    }
  },
  created () {
    this.getFollowList()
    this.getFollowUserDynamic()
  },
  methods: {
    getFollowList () {
      userApi.getFollowList().then((response) => {
        if (response.data.code === 20000) {
          this.followListDialog.list = response.data.data.data
        }
      })
    },
    followAndCancel (id) {
      const params = { followUserID: id }
      userApi.followAndCancel(qs.stringify(params)).then((response) => {
        if (response.data.code === 20000) {
          this.getFollowList()
        }
      })
    },
    getFollowUserInfo (userId) {
      if (this.followListDialog.list != null && userId != null) {
        const t = this.followListDialog.list.find(follow => follow.id === userId)
        if (t == null) {
          return this.$store.state.userData.userInfo
        } else {
          return t
        }
      } else {
        return {}
      }
    },
    getExtraData (dynamicId) {
      if (this.dynamicList.extraData != null && dynamicId != null) {
        return this.dynamicList.extraData[dynamicId]
      } else {
        return {}
      }
    },
    getFollowUserDynamic (current = 1) {
      this.page.current = current
      userApi.getFollowUserDynamic(this.page).then((response) => {
        if (response.data.code === 20000) {
          this.dynamicList.items = response.data.data.items
          this.dynamicList.extraData = response.data.data.extraData
          this.page.total = response.data.data.total
        }
      })
    }
  }
}
</script>

<style scoped>
.card-title{
    font-weight: bolder;
    font-size: 20px;
}
.card-preview{
    font-size: 14px;
    color: #999;
    height:42px;
    text-indent: 14px;
    margin: 15px 0px;
    margin-right: 20px;
    line-height: 18px;
}
.card-span{
      font-size: 10px;
    color: #b4b4b4;
}
</style>
