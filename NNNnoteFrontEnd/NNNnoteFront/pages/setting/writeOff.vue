<template>
  <el-container>
    <!-- 侧边栏 ----------------------------------------------------------------------------------------------------------------------------------------->
    <el-aside>
      <el-menu
        default-active="/setting/writeOff"
        class="el-menu-vertical-demo"
        router
      >
        <el-menu-item index="/setting">
          <i class="el-icon-user-solid" />
          <span slot="title">个人资料</span>
        </el-menu-item>
        <el-menu-item index="/setting/safe">
          <i class="el-icon-lock" />
          <span slot="title">安全设置</span>
        </el-menu-item>
        <el-menu-item index="/setting/writeOff">
          <i class="el-icon-error" />
          <span slot="title">账号注销</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <!------------------------------------------------------------------------------------------------------------------------------------------->

    <el-main id="my-main">
      <el-row>
        <el-col :span="24" align="middle">
          <h1>警告</h1>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18" :offset="6">
          1. 该操作执行完成不可撤销。
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18" :offset="6">
          2. 注销后，将会删除您账号下的所有数据，且不可恢复。
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18" :offset="6">
          3. 注销前，需要通过邮箱进行验证，如果没有绑定，请先绑定。
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" align="middle">
          <el-button type="danger" round @click="dialogVisible = true">
            确认注销
          </el-button>
        </el-col>
      </el-row>
    </el-main>

    <el-dialog
      title="请先通过验证"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <el-input type="text" placeholder="向绑定的邮箱发送验证码">
        <el-button slot="append" style="width:120px">
          发送验证码
        </el-button>
      </el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">取 消</el-button>
        <el-button type="danger" @click="innerVisible = true">确 定</el-button>
      </span>

      <el-dialog
        width="20%"
        title="警告，该操作不可撤销"
        :visible.sync="innerVisible"
        append-to-body
        center
      >
        <el-row>
          <el-col :span="24" align="middle">
            <el-button type="primary" @click="innerVisible = false">
              取 消
            </el-button>
            <el-button type="danger" @click="innerVisible = false">
              确 定
            </el-button>
          </el-col>
        </el-row>
      </el-dialog>
    </el-dialog>
  </el-container>
</template>

<script>
export default {
  name: 'WriteOffSafePage',
  layout: 'BaseLayout',

  data () {
    return {
      dialogVisible: false,
      innerVisible: false
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

#my-main .el-row{
  margin: 20px;
}
</style>
