<template>
  <el-container>
    <!-- 侧边栏 ----------------------------------------------------------------------------------------------------------------------------------------->
    <el-aside>
      <el-menu
        default-active="/setting/safe"
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

    <!-- 开始 -- 主要部分 --------------------------------------------------------------------------------------------------------------------------------->
    <el-main>
      <el-form ref="form" label-width="15%">
        <el-form-item label="邮箱地址">
          <div v-if="userSafeInfo.email">
            <el-tag effect="plain">
              {{ userSafeInfo.email }}
            </el-tag>
            <el-button size="mini" type="primary" round @click="dialogEmailInitialize">
              解除绑定
            </el-button>
          </div>

          <div v-if="!userSafeInfo.email">
            <el-button size="mini" type="primary" round @click="dialogEmailInitialize">
              绑定邮箱地址
            </el-button>
          </div>
        </el-form-item>
        <el-divider />
        <el-form-item label="手机号码">
          <div v-if="userSafeInfo.phone">
            <el-tag disabled>
              {{ userSafeInfo.phone }}
            </el-tag>
            <el-button size="mini" type="primary" round disabled>
              解除绑定
            </el-button>
          </div>

          <div v-if="!userSafeInfo.phone">
            <el-button size="mini" type="primary" round disabled>
              绑定手机号
            </el-button>
          </div>
        </el-form-item>
        <el-divider />
        <el-form-item label="社交账号">
          <el-row>
            <el-col :span="2" :offset="1">
              <i class="iconfont icon-weixin" style="color: #00bb29;font-size: 28px;" />
            </el-col>
            <el-col :span="10">
              <div v-if="userSafeInfo.openidWx">
                <el-tag>
                  已绑定
                </el-tag>
                <el-button size="mini" type="primary" round disabled>
                  解除绑定
                </el-button>
              </div>

              <div v-if="!userSafeInfo.openidWx">
                <el-button size="mini" type="primary" round disabled>
                  绑定微信账号
                </el-button>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="2" :offset="1">
              <i class="iconfont icon-qq" style="color: #498ad5;font-size: 28px;" />
            </el-col>
            <el-col :span="10">
              <div v-if="userSafeInfo.openidQq">
                <el-tag>
                  已绑定
                </el-tag>
                <el-button size="mini" type="primary" round disabled>
                  解除绑定
                </el-button>
              </div>

              <div v-if="!userSafeInfo.openidQq">
                <el-button size="mini" type="primary" round disabled>
                  绑定QQ账号
                </el-button>
              </div>
            </el-col>
          </el-row>
          <!--
          <el-row>
            <el-col :span="2" :offset="1">
              <i class="iconfont icon-wb" style="color: #498ad5;font-size: 28px;" />
            </el-col>
            <el-col :span="10">
              <div v-if="userSafeInfo.openidQq">
                <el-tag>
                  已绑定
                </el-tag>
                <el-button size="mini" type="primary" round disabled>
                  解除绑定
                </el-button>
              </div>

              <div v-if="!userSafeInfo.openidQq">
                <el-button size="mini" type="primary" round disabled>
                  绑定微博账号
                </el-button>
              </div>
            </el-col>
          </el-row> -->
        </el-form-item>
      </el-form>
      <el-row>
        <el-col :span="24" align="middle">
          <el-tag type="danger" effect="dark">
            注意，如果解除所有绑定，该账号将无法登录！
          </el-tag>
        </el-col>
      </el-row>
    </el-main>
    <!-- 结束 -- 主要部分 --------------------------------------------------------------------------------------------------------------------------------->

    <!-- 开始 -- 邮箱对话框 --------------------------------------------------------------------------------------------------------------------------------->
    <el-dialog :title="userSafeInfo.email ? '解除邮箱地址绑定' : '绑定邮箱地址'" :visible.sync="dialogEmail.visible" width="30%" center>
      <el-form :model="dialogEmail" label-width="20%">
        <el-form-item
          label="邮箱地址"
          prop="email"
          :rules="[
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
            { validator: checkEmail, trigger: ['blur', 'change'] },
          ]"
        >
          <el-input
            v-model="dialogEmail.email"

            type="text"
            placeholder="请输入邮箱地址"
            :disabled="userSafeInfo.email"
          />
        </el-form-item>
        <el-form-item
          prop="emailCode"
          label="验证码"
          :rules="[
            { required: true, message: '请输入验证码', trigger: 'blur' },
            { validator: checkEmailCodeFormat, trigger: ['blur', 'change'] },
          ]"
        >
          <el-input v-model="dialogEmail.emailCode" type="text" placeholder="请输入验证码">
            <el-button slot="append" style="width:120px" :disabled="dialogEmail.activeSendCodeBtn" @click="sendEmailCode()">
              {{ dialogEmail.sendCodeBtnText }}
            </el-button>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEmail.visible = false">
          取 消
        </el-button>
        <el-button type="primary" :disabled="dialogEmail.activeConfirmBtn" @click="submitAlterEmail">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <!-- 结束 -- 邮箱对话框 --------------------------------------------------------------------------------------------------------------------------------->
  </el-container>
</template>

<script>
import '~/assets/css/iconfont.css'
import qs from 'qs'
import userApi from '@/api/user'
import registerApi from '@/api/register'

export default {
  name: 'SettingSafePage',
  layout: 'BaseLayout',

  data () {
    return {
      userSafeInfo: {},
      dialogEmail: {
        emial: '',
        visible: false,
        emailCode: '',
        activeSendCodeBtn: false,
        second: 60,
        sendCodeBtnText: '发送验证码',
        activeConfirmBtn: true
      }
    }
  },
  created () {
    userApi.alterUserSafeInfo()
      .then((response) => {
        this.userSafeInfo = response.data.data.data
      })
  },
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
    },
    // 验证邮箱格式与邮箱是否已经被注册
    checkEmail (rule, value, callback) {
      // debugger
      if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value))) {
        this.dialogEmail.activeSendCodeBtn = true
        return callback(new Error('邮箱地址格式不正确'))
      }
      registerApi.checkEmail(this.dialogEmail.email)
        .then((response) => {
          const result = response.data.data.result
          this.dialogEmail.activeSendCodeBtn = result
          if (result) {
            return callback(new Error('邮箱地址已被注册'))
          }
          return callback()
        })
    },
    // 校验邮箱验证码格式
    checkEmailCodeFormat (rule, value, callback) {
      this.dialogEmail.activeConfirmBtn = true
      if (!(/^[0-9]{6}$/.test(value))) {
        return callback(new Error('验证码应为六位数字'))
      }
      this.dialogEmail.activeConfirmBtn = false
      return callback()
    },
    // 发送验证码
    sendEmailCode () {
      this.dialogEmail.activeSendCodeBtn = true
      registerApi.sendCode(this.dialogEmail.email)
        .then((response) => {
          if (response.data.code === 20000) {
            // 提示注册成功
            this.$message({
              type: 'success',
              message: '验证码发送成功'
            })
            // 设置倒计时
            const result = setInterval(() => {
              --this.dialogEmail.second
              this.dialogEmail.sendCodeBtnText = this.dialogEmail.second
              if (this.dialogEmail.second < 1) {
                clearInterval(result)
                this.dialogEmail.activeSendCodeBtn = false
                this.dialogEmail.second = 60
                this.dialogEmail.sendCodeBtnText = '获取验证码'
              }
            }, 1000)
          } else {
            this.$message.error('验证码发送失败')
            this.dialogEmail.activeSendCodeBtn = false
          }
        })
    },
    // 初始化邮箱对话框
    dialogEmailInitialize () {
      // 如果是解除绑定邮箱，直接将邮箱地址设置为绑定地址
      if (this.userSafeInfo.email != null) {
        this.dialogEmail.email = this.userSafeInfo.email
      } else {
        // 绑定邮箱，需要先对邮箱进行检查，才能发送验证码
        this.dialogEmail.activeSendCodeBtn = true
      }

      this.dialogEmail.visible = true
    },
    // 发送绑定或解绑邮箱请求
    submitAlterEmail () {
      // 封装数据
      const data = { email: this.dialogEmail.email, code: this.dialogEmail.code }
      userApi.alterUserEmail(qs.stringify(data))
        .then((response) => {
          if (response.data.code === 20000) {
            // 页面刷新
            window.location.href = '/setting/safe'
          }
        })
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
