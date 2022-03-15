<template>
  <div>
    <el-container>
      <el-main>
        <el-row>
          <el-col :span="4" :offset="1">
            <el-avatar :src="userInfo.avatar" :size="70" />
          </el-col>
          <el-col :span="19">
            <el-descriptions :colon="false" :column="3">
              <el-descriptions-item>
                <h3>{{ userInfo.nickname ? userInfo.nickname : '&nbsp;' }}</h3>
              </el-descriptions-item>
              <el-descriptions-item />
              <el-descriptions-item />
              <el-descriptions-item>
                <el-tooltip effect="dark" content="开通会员后将解除保存上限">
                  <i class="el-icon-info" />
                </el-tooltip>
                笔记：{{ dataCount.noteCount }}/40
              </el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>

        <div style="height:10px" />

        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="笔记" name="first">
            <NoteFolderComponent />
          </el-tab-pane>
          <el-tab-pane label="评论" name="second" />
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
