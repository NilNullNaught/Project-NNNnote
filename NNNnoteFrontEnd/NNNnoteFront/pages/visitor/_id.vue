<template>
  <div>
    <el-container>
      <el-main>
        <div class="centerVertical">
          <el-avatar style="display:inline-block;margin:0 20px;" :src="userInfo.avatar" :size="80" />
          <div style="display:inline-block;font-size:12px;color:#606266;line-height: 24px;">
            <p style="font-size:20px;font-weight:bolder">
              {{ userInfo.nickname ? userInfo.nickname : '&nbsp;' }}
              <el-tag v-if="userMember.isMember" size="mini" type="success">
                会员
              </el-tag>
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
            <!-- TODO -->
            暂未实现
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import userApi from '@/api/user'
import noteApi from '@/api/note'
import VisitorPublicNoteComponent from '@/components/visitor/VisitorPublicNoteComponent'

export default {
  name: 'VisitorIdPage',
  components: {
    VisitorPublicNoteComponent
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
      noteCount: 0
    }
  },
  created () {
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
    }
  }
}
</script>
<style scoped>

</style>
