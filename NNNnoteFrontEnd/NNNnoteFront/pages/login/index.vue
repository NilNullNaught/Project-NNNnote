<template>
  <div class="main">
    <!-- 开始 -- 跳转部分 --------------------------------------------------------------------------------------------------------------------------------->
    <div class="title">
      <a class="active" href="/login">
        登录
      </a>
      <span>·</span>
      <a href="/login/register">
        注册
      </a>
    </div>
    <!-- 结束 -- 跳转部分 --------------------------------------------------------------------------------------------------------------------------------->

    <div class="sign-up-container">
      <!-- 开始 -- 表单 --------------------------------------------------------------------------------------------------------------------------------->

      <el-form ref="userForm" :model="user">
        <el-form-item
          prop="mobile"
          :rules="[
            { validator: checkEmail, trigger: ['blur'] },
          ]"
        >
          <div>
            <el-input v-model="user.email" type="text" placeholder="请输入邮箱地址">
              <i slot="prefix" class="el-input__icon el-icon-user-solid" />
            </el-input>
          </div>
        </el-form-item>

        <el-form-item prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input v-model="user.password" type="password" placeholder="请输入密码">
              <i slot="prefix" class="el-input__icon el-icon-lock" />
            </el-input>
          </div>
        </el-form-item>
        <el-row justify="center">
          <el-col :span="8">
            <el-switch
              v-model="user.rememberMe"
              style="display: block"
              active-color="#13ce66"
              inactive-color="#b5b5b5"
              active-text="记住我"
            />
          </el-col>

          <el-col :offset="10" :span="6">
            <el-link href="login/passwordRest" type="success">
              忘记密码?
            </el-link>
          </el-col>
        </el-row>

        <div class="btn">
          <el-button :disabled="activeLoginBtn" class="sign-in-button" @click="submitLogin()">
            登录
          </el-button>
        </div>
      </el-form>
      <!-- 结束 -- 表单 --------------------------------------------------------------------------------------------------------------------------------->

      <!-- 开始 -- 更多登录方式 --------------------------------------------------------------------------------------------------------------------------------->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li>
            <el-link :underline="false" :href="weChatLoginUrl">
              <i class="alibaba_icons_weixin" style="color: #00bb29;font-size: 36px;" />
            </el-link>
          </li>
        </ul>
      </div>
      <!-- 结束 -- 更多登录方式 --------------------------------------------------------------------------------------------------------------------------------->
    </div>
  </div>
</template>

<script>
import '~/assets/css/loginCss/sign.css'

import jsCookie from 'js-cookie'
import loginApi from '@/api/login'
import request from '@/utils/request'

export default {
  name: 'LoginIndexPage',
  layout: 'SignLayout',

  data () {
    return {
      // 封装登录手机号和密码对象
      user: {
        email: '',
        password: '',
        rememberMe: false
      },
      activeLoginBtn: false,
      prePage: '',
      weChatLoginUrl: request.defaults.baseURL + '/user/user-wechat/login'
    }
  },
  methods: {

    // 验证邮箱的合法性
    checkEmail (rule, value, callback) {
      if (this.user.email === '') {
        return callback(new Error('请输入邮箱地址'))
      }
      if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(this.user.email))) {
        return callback(new Error('邮箱地址格式不正确'))
      }
    },

    // 发送登录请求
    submitLogin () {
      // 判断登录信息合法性
      if (this.user.email === '' ||
          this.user.password === ''
      ) {
        this.$message({
          type: 'warning',
          message: '请填写邮箱地址和密码'
        })
        return
      }
      this.activeLoginBtn = true
      loginApi.login(this.user).then((response) => {
        if (response.data.code === 20000) {
          let expiration
          if (this.rememberMe) {
            expiration = 30
          } else {
            expiration = 1
          }

          // 将返回的token保存在 cookie 中
          jsCookie.set('NNNnote_token', response.data.data.token, { domain: 'localhost' }, { expires: expiration })

          // 跳转至登录前页面
          this.$router.push({ path: '/' })
        } else {
          this.user.password = ''
          this.activeLoginBtn = false
          // 提示登录失败
          this.$message.error('登录失败')
        }
      })
    }
  }
}
</script>
