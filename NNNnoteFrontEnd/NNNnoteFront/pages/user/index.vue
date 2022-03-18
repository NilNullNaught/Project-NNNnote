<template>
  <div>
    <el-container>
      <el-main>
        <div class="centerVertical">
          <el-avatar style="display:inline-block;margin:0 20px;" :src="userInfo.avatar" :size="70" />
          <div style="display:inline-block;font-size:12px;color:#606266;line-height: 24px;">
            <p style="font-size:18px;font-weight:bolder">
              {{ userInfo.nickname ? userInfo.nickname : '&nbsp;' }}
            </p>
            <p>
              <el-tooltip effect="dark" content="开通会员后将解除保存上限">
                <i class="el-icon-info" />
              </el-tooltip>
              笔记：{{ dataCount.noteCount }}/40
            </p>
          </div>
        </div>

        <div style="height:10px" />

        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="笔记" name="first">
            <NoteFolderComponent />
          </el-tab-pane>
          <el-tab-pane label="收藏" name="second" />
          <!-- <el-tab-pane label="动态" name="second" /> -->
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import NoteFolderComponent from '@/components/user/NoteFolderComponent'
export default {
  name: 'UserNfolderIndexPage',
  components: {
    NoteFolderComponent
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
    }
  },

  methods: {

  }
}
</script>
<style scoped>

</style>
