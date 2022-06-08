<template>
  <div>
    <el-container>
      <el-main>
        <div
          class="centerVertical"
          style="position:relative;"
        >
          <el-avatar style="display:inline-block;margin:0 20px;" :src="userInfo.avatar" :size="80" />
          <div
            style="display:inline-block;
          font-size:12px;
          color:#606266;
          line-height: 24px;"
          >
            <p style="font-size:20px;font-weight:bolder">
              {{ userInfo.nickname ? userInfo.nickname : '&nbsp;' }}
              <el-tag v-if="userMember.isMember" size="mini" type="success">
                会员
              </el-tag>

              <el-button
                v-if="userInfo.id != $store.state.userData.userInfo.id && $store.state.userData.userInfo.id != null"
                style="position:absolute;right:0px;"
                :type="followStyle.color"
                size="mini"
                @click="followAndCancel"
              >
                {{ followStyle.text }}
              </el-button>
            </p>

            <p>{{ userInfo.sign ? userInfo.sign : '&nbsp;' }}</p>

            <p>
              <el-tooltip effect="dark" content="开通会员后将解除保存上限">
                <i class="el-icon-info" />
              </el-tooltip>
              笔记数量：{{ noteCount }}/<span v-if="userMember.isMember">∞</span><span v-else>40</span>
            </p>
          </div>
        </div>

        <div style="height:10px" />

        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="分享" name="first">
            <VisitorPublicNoteComponent :user-id="$route.params.id" />
          </el-tab-pane>
          <el-tab-pane label="动态" name="third">
            <VisitorDynamicComponent :user-info="userInfo" />
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import qs from 'qs'
import userApi from '@/api/user'
import noteApi from '@/api/note'
import VisitorPublicNoteComponent from '@/components/visitor/VisitorPublicNoteComponent'
import VisitorDynamicComponent from '@/components/visitor/VisitorDynamicComponent'

export default {
  name: 'VisitorIdPage',
  components: {
    VisitorPublicNoteComponent,
    VisitorDynamicComponent
  },
  layout: 'BaseLayout',
  data () {
    return {
      activeTab: 'first',
      dataCount: {
        noteCount: 1
      },
      userInfo: {
      },
      userMember: {},
      noteCount: 0,
      followStyle: {
        color: 'primary',
        text: '关注'
      },
      isFollow: false
    }
  },
  created () {
    this.isFollowed()
    this.getUserInfo()
    this.getCountOfNoteInfoById()
    this.getUserMemberById()
  },

  methods: {
    getUserInfo () {
      userApi.getUserInfoById(this.$route.params.id).then((response) => {
        if (response.data.code === 20000) {
          this.userInfo = response.data.data.data
        }
      })
    },
    isFollowed () {
      const params = { followUserID: this.$route.params.id }
      userApi.isFollowed(params).then((response) => {
        if (response.data.code === 20000) {
          this.isFollow = response.data.data.data
          if (this.isFollow) {
            this.followStyle = {
              color: 'info',
              text: '取消'
            }
          } else {
            this.followStyle = {
              color: 'primary ',
              text: '关注'
            }
          }
        }
      })
    },
    getCountOfNoteInfoById () {
      const data = { userId: this.$route.params.id }
      noteApi.getCountOfNoteInfoById(data).then((response) => {
        if (response.data.code === 20000) {
          this.noteCount = response.data.data.data
        }
      })
    },
    getUserMemberById () {
      const data = { userId: this.$route.params.id }
      userApi.getUserMemberById(data).then((response) => {
        if (response.data.code === 20000) {
          this.userMember = response.data.data.data
        }
      })
    },
    followAndCancel () {
      const params = { followUserID: this.$route.params.id }
      userApi.followAndCancel(qs.stringify(params)).then((response) => {
        if (response.data.code === 20000) {
          if (this.isFollow) {
            this.followStyle = {
              color: 'primary ',
              text: '关注'
            }
          } else {
            this.followStyle = {
              color: 'info',
              text: '取消'
            }
          }
          this.isFollow = !(this.isFollow)
        }
      })
    }
  }
}
</script>
<style scoped>

</style>
