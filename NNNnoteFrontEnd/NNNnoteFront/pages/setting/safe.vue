<template>
  <el-container>
    <el-aside>
      <el-menu
        default-active="2"
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
      <el-form ref="form" label-width="80px">
        <el-form-item label="邮箱地址">
          <div v-if="false">
            <el-tag>
              邮箱地址
            </el-tag>
            <el-button size="mini">
              更换绑定的邮箱地址
            </el-button>
          </div>

          <div v-if="true">
            <el-button size="mini">
              绑定邮箱地址
            </el-button>
          </div>
        </el-form-item>
        <el-divider />
        <el-form-item label="手机号码">
          <div v-if="false">
            <el-tag>
              手机号
            </el-tag>
            <el-button size="mini">
              更换绑定的手机号
            </el-button>
          </div>

          <div v-if="true">
            <el-button size="mini">
              绑定手机号
            </el-button>
          </div>
        </el-form-item>
        <el-divider />
        <el-form-item label="社交账号">
          <el-row>
            <el-col :span="1">
              &nbsp;
            </el-col>
            <el-col :span="2">
              <i class="iconfont icon-weixin" style="color: #00bb29;font-size: 28px;" />
            </el-col>
            <el-col :span="10">
              <div v-if="false">
                <el-tag>
                  微信账号
                </el-tag>
                <el-button size="mini">
                  更换绑定的微信账号
                </el-button>
              </div>

              <div v-if="true">
                <el-button size="mini">
                  绑定微信账号
                </el-button>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="1">
              &nbsp;
            </el-col>
            <el-col :span="2">
              <i class="iconfont icon-qq" style="color: #498ad5;font-size: 28px;" />
            </el-col>
            <el-col :span="10">
              <div v-if="false">
                <el-tag>
                  QQ账号
                </el-tag>
                <el-button size="mini">
                  更换绑定的QQ账号
                </el-button>
              </div>

              <div v-if="true">
                <el-button size="mini">
                  绑定QQ账号
                </el-button>
              </div>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
import '~/assets/css/iconfont.css'

export default {
  name: 'SettingSafePage',
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
