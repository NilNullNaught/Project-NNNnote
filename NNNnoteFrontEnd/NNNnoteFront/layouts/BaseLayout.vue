<template>
  <div>
    <el-container>
      <el-header class="head-bg">
        <el-row :gutter="20" align="middle" type="flex">
          <el-col :span="4">
            <el-image
              :src="require('~/assets/img/logo.png')"
              alt="NNN 笔记"
              style="width: 50px;width: 50px;padding:10px 0px 0px 0px;"
            />
          </el-col>
          <el-col :span="5">
            <el-menu
              :default-active="style.activeIndex"
              mode="horizontal"
              background-color="#323232"
              text-color="#fff"
              active-text-color="#7AFFA0"
              @select="handleSelect"
            >
              <el-menu-item index="1">
                主页
              </el-menu-item>
              <el-menu-item index="2">
                推荐
              </el-menu-item>
            </el-menu>
          </el-col>
          <el-col :span="6">
            <el-input placeholder="请输入内容" class="input-with-select">
              <el-button slot="append" icon="el-icon-search" />
            </el-input>
          </el-col>
          <el-col :span="5" />
          <el-col :span="4" align="end">
            <el-menu
              v-if="loginInfo.id"
              :default-active="style.activeIndex"
              class="el-menu-demo"
              mode="horizontal"
              background-color="#323232"
              text-color="#fff"
              active-text-color="#ffd04b"
              @select="handleSelect"
            >
              <el-submenu index="3">
                <template slot="title">
                  <el-avatar :size="size" :src="circleUrl" />
                </template>
                <el-menu-item index="3-1">
                  选项1
                </el-menu-item>
                <el-menu-item index="3-2">
                  选项2
                </el-menu-item>
                <el-menu-item index="3-3">
                  选项3
                </el-menu-item>
                <el-menu-item index="3-4">
                  退出登录
                </el-menu-item>
              </el-submenu>
              <el-menu-item index="4">
                写笔记
              </el-menu-item>
            </el-menu>

            <el-menu
              v-if="!loginInfo.id"
              :default-active="style.activeIndex"
              class="el-menu-demo"
              mode="horizontal"
              background-color="#323232"
              text-color="#fff"
              active-text-color="#ffd04b"
              @select="handleSelect"
            >
              <el-menu-item index="5">
                登录
              </el-menu-item>
              <el-menu-item index="6">
                注册
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-header>

      <el-main>
        <el-row :gutter="20" align="middle" type="flex">
          <el-col :span="4" />
          <el-col :span="16">
            <nuxt />
          </el-col>
          <el-col :span="4" />
        </el-row>
      </el-main>

      <el-footer class="head-bg" height="160px">
        <el-row :gutter="20" align="middle" justify="center" type="flex">
          <el-col :span="4" />

          <el-col :span="16" align="middle" justify="center" type="flex">
            <h3 class="h3">
              Present by NilNullNaught
            </h3>
            <el-divider />
            <div style="color:#666;">
              <span>Email：nilnullnaught@gmail.com</span>
              <el-divider direction="vertical" />
              <el-link :underline="false" href="https://github.com/NilNullNaught/Project-NNNnote">
                项目地址：github.com/NilNullNaught/Project-NNNnote
              </el-link>
              <el-divider direction="vertical" />
              <span>联系方式：188********</span>
            </div>
          </el-col>
          <el-col :span="4" />
        </el-row>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import jsCookie from 'js-cookie'

export default {
  data () {
    return {
      style: {
        activeIndex: '1'
      },
      loginInfo: {
        id: '',
        nickname: '',
        sex: '',
        age: '',
        avatar: '',
        sign: ''
      }
    }
  },
  created () {
    // 判断是否通过微信登录
    const token = this.$route.query.token
    if (token) { // 判断路径是否有token值
      alert('通过微信登录')
      // this.wxLogin()
    }

    // 判断是否通过密码登录
    // 从cookie获取用户信息
    const userStr = jsCookie.get('NNNnote_userInfo')
    // 把字符串转换json对象(js对象)
    if (userStr) {
      this.loginInfo = JSON.parse(userStr)
    }
  },
  methods: {

    // 退出登录
    signOut () {
      if (jsCookie.get('NNNnote_token')) {
        jsCookie.remove('NNNnote_token', { domain: 'localhost' }) // 删除成功
        jsCookie.remove('NNNnote_userInfo', { domain: 'localhost' })
        this.loginInfo = ''
        this.style.activeIndex = 1
        this.$router.push({ path: '/' })
      }
    },
    handleSelect (key, keyPath) {
      switch (key) {
        case '1':
          this.style.activeIndex = 1
          this.$router.push({ path: '/' })
          break
        case '2':
          this.style.activeIndex = 2
          this.$router.push({ path: '/recommend' })
          break
        case '3-1':
          this.style.activeIndex = 3
          this.$router.push({ path: '/login' })
          break
        case '3-2':
          this.style.activeIndex = 3
          this.$router.push({ path: '/register' })
          break
        case '3-3':
          this.style.activeIndex = 3
          this.$router.push({ path: '/recommend' })
          break
        case '3-4':
          this.signOut()
          break
        case '5':
          this.$router.push({ path: '/login' })
          break
        case '6':
          this.$router.push({ path: '/register' })
          break
        default:
          this.$router.push({ path: '/' })
      }
    }

  }

}
</script>
<style>
*{padding:0;margin:0;box-sizing: border-box;font-size: 14px;}
  .head-bg {
    background-color: #323232;
  }

  .h3 {
    color:#999;
    padding:30px 0px 0px 0px;
    font-size: 20px;
    font-family: "Lucida Console", "Courier New", monospace;
  }
</style>
