<template>
  <div class="main">
    <!-- 开始 -- 跳转部分 --------------------------------------------------------------------------------------------------------------------------------->
    <div class="title">
      <a href="/login">登录</a>
      <span>·</span>
      <a class="active" href="/login/register">注册</a>
    </div>
    <!-- 结束 -- 跳转部分 --------------------------------------------------------------------------------------------------------------------------------->

    <div class="sign-up-container">
      <!-- 开始 -- 表单 --------------------------------------------------------------------------------------------------------------------------------->
      <el-form ref="userForm" :model="params">
        <el-form-item prop="nickname" :rules="[{ required: true, message: '请输入你的昵称', trigger: 'blur' }]">
          <div>
            <el-input v-model="params.nickname" type="text" placeholder="你的昵称" maxlength="10" show-word-limit>
              <i slot="prefix" class="el-input__icon el-icon-user-solid" />
            </el-input>
          </div>
        </el-form-item>

        <el-form-item
          prop="email"
          :rules="[
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
            { validator: checkEmail, trigger: ['blur', 'change'] },
          ]"
        >
          <div>
            <el-input
              v-model="params.email"
              type="text"
              placeholder="邮箱地址"
            >
              <i slot="prefix" class="el-input__icon el-icon-message" />
            </el-input>
          </div>
        </el-form-item>

        <el-form-item
          prop="code"
          :rules="[
            { required: true, message: '请输入验证码', trigger: 'blur' },
            { validator: checkCodeFormat, trigger: ['blur', 'change'] },
          ]"
        >
          <el-input v-model="params.code" type="text" placeholder="请输入验证码">
            <i slot="prefix" class="el-input__icon el-icon-s-promotion" />
            <el-button slot="append" :disabled="activeSendCodeBtn" @click="sendCode()">
              {{ codeTest }}
            </el-button>
          </el-input>
        </el-form-item>

        <el-form-item prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input v-model="params.password" type="password" placeholder="设置密码">
              <i slot="prefix" class="el-input__icon el-icon-lock" />
            </el-input>
          </div>
        </el-form-item>

        <div class="btn">
          <el-button class="sign-up-button" value="注册" @click="submitRegister()">
            注册
          </el-button>
        </div>
        <p class="sign-up-msg">
          点击 “注册” 即表示您同意并愿意遵守 NNN 笔记
          <br>
          <a target="_blank" href="http://www.jianshu.com/p/c44d171298ce">用户协议</a>
          和
          <a target="_blank" href="http://www.jianshu.com/p/2ov8x3">隐私政策</a> 。
        </p>
      </el-form>
      <!-- 结束 -- 表单 --------------------------------------------------------------------------------------------------------------------------------->

      <!-- 开始 -- 更多注册方式 --------------------------------------------------------------------------------------------------------------------------------->
      <div class="more-sign">
        <h6>社交帐号直接注册</h6>
        <ul>
          <li>
            <a id="weixin" class="weixin" target="_blank" href="http://huaan.free.idcfengye.com/api/ucenter/wx/login">
              <i class="alibaba_icons_weixin" style="color: #00bb29;font-size: 36px;" />
            </a>
          </li>
          <li>
            <a id="qq" class="qq" target="_blank" href="#">
              <i class="alibaba_icons_QQ" style="color: #498ad5;font-size: 36px;" />
            </a>
          </li>
        </ul>
      </div>
      <!-- 结束 -- 更多登录方式 --------------------------------------------------------------------------------------------------------------------------------->
    </div>
  </div>
</template>

<script>
import '~/assets/css/loginCss/sign.css'
import registerApi from '@/api/register'

export default {
  name: 'LoginRegisterPage',
  layout: 'SignLayout',

  data () {
    return {
      // 发送验证码按钮状态
      activeSendCodeBtn: true,
      params: { // 封装注册输入数据
        email: '',
        code: '', // 验证码
        nickname: '',
        password: ''
      },
      second: 60, // 倒计时长度
      codeTest: '获取验证码'
    }
  },
  methods: {
    // 验证邮箱格式与邮箱是否已经被注册
    checkEmail (rule, value, callback) {
      // debugger
      if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value))) {
        this.activeSendCodeBtn = true
        return callback(new Error('邮箱地址格式不正确'))
      }
      registerApi.checkEmail(this.params.email)
        .then((response) => {
          const result = response.data.data.result
          this.activeSendCodeBtn = result
          if (result) {
            return callback(new Error('邮箱地址已被注册'))
          }
          return callback()
        })
    },
    // 校验验证码格式
    checkCodeFormat (rule, value, callback) {
      if (!(/^[0-9]{6}$/.test(value))) {
        return callback(new Error('验证码应为六位数字'))
      }
      return callback()
    },

    // 发送验证码
    sendCode () {
      this.activeSendCodeBtn = true
      registerApi.sendCode(this.params.email)
      // 设置倒计时
      const result = setInterval(() => {
        --this.second
        this.codeTest = this.second
        if (this.second < 1) {
          clearInterval(result)
          this.activeSendCodeBtn = false
          this.second = 60
          this.codeTest = '获取验证码'
        }
      }, 1000)
    },

    // 发送注册请求
    submitRegister () {
      // 判断注册信息合法性
      if (this.params.email === '' ||
          this.params.code === '' ||
          this.params.nickname === '' ||
          this.params.password === ''
      ) {
        this.$message({
          type: 'warning',
          message: '请完善注册信息'
        })
        return
      }

      registerApi.register(this.params)
        .then((response) => {
          if (response.data.code === 20000) {
            // 提示注册成功
            this.$message({
              type: 'success',
              message: '注册成功'
            })

            // 跳转至登录页
            this.$router.push({ path: '/login' })
          } else {
            // 提示注册失败
            this.$message.error('注册失败')
          }
        })
    }

  }
}
</script>
