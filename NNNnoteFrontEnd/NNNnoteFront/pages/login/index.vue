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
        <el-form-item prop="mobile" :rules="[{ required: true, message: '请输入手机号码', trigger: 'blur' },{validator: checkPhone, trigger: 'blur'}]">
          <div>
            <el-input v-model="user.email" type="text" placeholder="手机号">
              <i slot="prefix" class="el-input__icon el-icon-user-solid" />
            </el-input>
          </div>
        </el-form-item>

        <el-form-item prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input v-model="user.password" type="password" placeholder="密码">
              <i slot="prefix" class="el-input__icon el-icon-lock" />
            </el-input>
          </div>
        </el-form-item>

        <div class="btn">
          <input type="button" class="sign-in-button" value="登录" @click="submitLogin()">
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

export default {
  name: 'LoginIndexPage',
  layout: 'SignLayout',

  data () {
    return {
      // 封装登录手机号和密码对象
      user: {
        mobile: '',
        password: ''
      },
      // 用户信息
      loginInfo: {}
    }
  },
  methods: {
    checkPhone (rule, value, callback) {
      // debugger
      if (!(/^1[34578]\d{9}$/.test(value))) {
        return callback(new Error('手机号码格式不正确'))
      }
      return callback()
    }
  }
}
</script>
