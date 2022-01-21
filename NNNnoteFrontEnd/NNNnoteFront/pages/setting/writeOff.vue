<template>
  <el-container>
    <el-aside>
      <el-menu
        default-active="3"
        class="el-menu-vertical-demo"
        @select="handleSelect"
      >
        <el-menu-item index="1">
          <i class="el-icon-user-solid" />
          <span slot="title">个人资料</span>
        </el-menu-item>
        <el-menu-item index="2">
          <i class="el-icon-lock" />
          <span slot="title">安全设置</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-error" />
          <span slot="title">账号注销</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      账号管理
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'WriteOffSafePage',
  layout: 'BaseLayout',

  data () {
    return {
    }
  },
  created () {},
  methods: {
    handleSelect (key, keyPath) {
      switch (key) {
        case '1':
          this.$router.push({ path: '/setting' })
          break
        case '2':
          this.$router.push({ path: '/setting/safe' })
          break
        case '3':
          this.$router.push({ path: '/setting/writeOff' })
          break
        default:
          this.$router.push({ path: '/setting' })
      }
    }
  }
}
</script>
<style>
.el-container {
     min-height: calc(85vh);
}
.el-aside{
  border-right: solid 1px #e6e6e6;
}
.el-menu{
  border: 0px;
}
</style>
