<template>
  <div class="main">
    <!-- 开始 -- 跳转部分 --------------------------------------------------------------------------------------------------------------------------------->
    <div class="title">
      <a class="active" href="/login">
        登录
      </a>
      <span>·</span>
      <a href="/register">
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
          <li><a id="weixin" class="weixin" target="_blank" href="http://qy.free.idcfengye.com/api/ucenter/weixinLogin/login"><i class="iconfont icon-weixin" /></a></li>
          <li><a id="qq" class="qq" target="_blank" href="#"><i class="iconfont icon-qq" /></a></li>
        </ul>
      </div>
      <!-- 结束 -- 更多登录方式 --------------------------------------------------------------------------------------------------------------------------------->
    </div>
  </div>
</template>

<script>
import '~/assets/css/sign.css'
import '~/assets/css/iconfont.css'
// import testApi from '@/api/test'
import jsCookie from 'js-cookie'
import loginApi from '@/api/login'
import userApi from '@/api/user'

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
      activeLoginBtn: false
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
          // 提示登录成功
          this.$message({
            type: 'success',
            message: '登录成功'
          })

          let expiration
          if (this.rememberMe) {
            expiration = 30
          } else {
            expiration = 1
          }

          // 将返回的token保存在 cookie 中
          jsCookie.set('NNNnote_token', response.data.data.token, { domain: 'localhost' }, { expires: expiration })

          // 调用接口 根据token获取用户信息，为了首页面显示
          userApi.getUserInfo()
            .then((response) => {
              const userInfo = JSON.stringify(response.data.data.data)
              jsCookie.set('NNNnote_userInfo', userInfo, { domain: 'localhost' }, { expires: expiration })
              // 跳转至登录页
              this.$router.push({ path: '/' })
            })
        } else {
          this.user = ''
          this.activeLoginBtn = false
          // 提示登录失败
          this.$message.error('登录失败')
        }
      })
    }
  }
}
</script>
