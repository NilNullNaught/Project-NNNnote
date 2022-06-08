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
              笔记数量：{{ dataCount.noteCount }}/<span v-if="userMember.isMember">∞</span><span v-else>40</span>
            </p>
          </div>
        </div>

        <div style="height:10px" />

        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="笔记" name="first">
            <NoteNfolderComponent />
          </el-tab-pane>
          <el-tab-pane label="收藏" name="second">
            <NoteCfolderComponent />
          </el-tab-pane>
          <el-tab-pane label="动态" name="third">
            <UserDynamicComponent />
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import NoteNfolderComponent from '@/components/user/NoteNfolderComponent'
import NoteCfolderComponent from '@/components/user/NoteCfolderComponent'
import UserDynamicComponent from '@/components/user/UserDynamicComponent'

export default {
  name: 'UserNfolderIndexPage',
  components: {
    NoteNfolderComponent,
    NoteCfolderComponent,
    UserDynamicComponent
  },
  layout: 'BaseLayout',
  data () {
    return {
      activeTab: 'first'
    }
  },
  computed: {
    dataCount () {
      return this.$store.state.userData.dataCount
    },
    userInfo () {
      return this.$store.state.userData.userInfo
    },
    userMember () {
      return this.$store.state.userData.userMember
    }
  },

  methods: {

  }
}
</script>
<style scoped>

</style>
