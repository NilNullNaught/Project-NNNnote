<template>
  <div>
    <el-container>
      <el-main>
        <div class="centerVertical">
          <el-avatar style="display:inline-block;margin:0 20px;" :src="userInfo.avatar" :size="80" />
          <div style="display:inline-block;font-size:12px;color:#606266;line-height: 24px;">
            <p style="font-size:20px;font-weight:bolder">
              {{ userInfo.nickname ? userInfo.nickname : '&nbsp;' }}
            </p>

            <p>{{ userInfo.sign ? userInfo.sign : '&nbsp;' }}</p>
          </div>
        </div>

        <div style="height:10px" />

        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="发布笔记" name="first" />
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

export default {
  name: 'VisitorIdPage',
  layout: 'BaseLayout',
  data () {
    return {
      activeTab: 'first',
      dataCount: {
        noteCount: 1
      },
      userInfo: {
      }
    }
  },
  created () {
    this.getUserInfo()
  },

  methods: {
    getUserInfo () {
      userApi.getUserInfoById(this.$route.params.id).then((response) => {
        if (response.data.code === 20000) {
          this.userInfo = response.data.data.data
        }
      })
    }
  }
}
</script>
<style scoped>

</style>
