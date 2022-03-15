<template>
  <div>
    <el-container>
      <el-header class="head-bg">
        <el-row align="middle" type="flex">
          <el-col :xs="0" :sm="2" :md="2" :lg="4" :xl="4">
            <el-image
              style="width: 55px; height: 55px;padding:5px 0px 0px 0px;"
              :src="require('@/static/logo.png')"
            />
          </el-col>

          <el-col :xs="7" :sm="6" :md="6" :lg="5" :xl="5">
            <el-menu
              mode="horizontal"
              background-color="#323232"
              text-color="#fff"
              active-text-color="#7AFFA0"
              router
            >
              <el-menu-item index="/">
                主页
              </el-menu-item>
              <el-menu-item index="/recommend">
                推荐
              </el-menu-item>
            </el-menu>
          </el-col>
          <el-col :xs="10" :sm="8" :md="8" :lg="6" :xl="6">
            <div>
              <el-input placeholder="请输入内容" class="input-with-select">
                <el-button slot="append" icon="el-icon-search" />
              </el-input>
            </div>
          </el-col>
          <el-col :xs="0" :sm="2" :md="2" :lg="5" :xl="5" />
          <el-col :xs="7" :sm="6" :md="6" :lg="4" :xl="4">
            <div>
              <div v-if="userInfo.id">
                <el-menu
                  class="el-menu-demo"
                  mode="horizontal"
                  background-color="#323232"
                  text-color="#fff"
                  active-text-color="#ffd04b"
                  router
                >
                  <el-submenu index="3">
                    <template slot="title">
                      <el-avatar :src="userInfo.avatar" />
                    </template>
                    <el-menu-item index="/user">
                      我的主页
                    </el-menu-item>
                    <el-menu-item index="/user/collection">
                      收藏夹
                    </el-menu-item>
                    <el-menu-item index="/user/draft">
                      草稿箱（{{ dataCount.draftCount }}）
                    </el-menu-item>
                    <el-menu-item index="/user/recyclebin">
                      回收站（{{ dataCount.deletedCount }}）
                    </el-menu-item>
                    <el-menu-item index="/setting">
                      设置
                    </el-menu-item>
                    <el-menu-item index="/" @click="signOut">
                      退出
                    </el-menu-item>
                  </el-submenu>
                  <el-menu-item index="/" @click="toEditor">
                    写笔记
                  </el-menu-item>
                </el-menu>
              </div>
              <div v-if="!userInfo.id">
                <el-menu
                  class="el-menu-demo"
                  mode="horizontal"
                  background-color="#323232"
                  text-color="#fff"
                  active-text-color="#ffd04b"
                  router
                >
                  <el-menu-item index="/login">
                    登录
                  </el-menu-item>
                  <el-menu-item index="/login/register">
                    注册
                  </el-menu-item>
                </el-menu>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-header>

      <div class="el-backtop-target" />

      <el-main class="main">
        <el-row>
          <el-col
            :span="24"
            style="  display:flex;justify-content:center;"
          >
            <el-card style="width:900px;" :body-style="{ padding: '0px' }" shadow="always">
              <nuxt />
            </el-card>
          </el-col>
        </el-row>
      </el-main>
      <el-footer class="head-bg" height="160px">
        <el-row justify="center" type="flex">
          <el-col align="center" justify="center" type="flex">
            <div>
              <h3 class="h3">
                Present by NilNullNaught
              </h3>
              <el-divider />
              <div style="color:#666;">
                <span>Email: nilnullnaught@gmail.com</span>
                <el-divider direction="vertical" />
                <el-link :underline="false" href="https://github.com/NilNullNaught/Project-NNNnote">
                  项目地址: github.com/NilNullNaught/Project-NNNnote
                </el-link>
                <el-divider direction="vertical" />
                <span>联系方式: 188********</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import jsCookie from 'js-cookie'
import noteApi from '@/api/note'
import userApi from '@/api/user'

export default {
  data () {
    return {
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
  created () {
    // 判断是否通过微信登录
    const token = this.$route.query.token
    if (token) { // 判断路径是否有token值
      alert('通过微信登录')
      // this.wxLogin()
    }

    // // 判断是否通过密码登录
    // // 从cookie获取用户信息
    // const userStr = jsCookie.get('NNNnote_userInfo')
    // // 把字符串转换json对象(js对象)
    // if (userStr) {
    //   this.userInfo = JSON.parse(userStr)
    // }
    // if (this.userInfo) {
    //   this.getCountOfNoteInfo()
    // }

    // 判断是否通过密码登录
    // 从 cookie 获取用户信息
    const cookie = jsCookie.get('NNNnote_token')
    // 把字符串转换json对象(js对象)
    if (cookie) {
      this.getUserInfo()
      this.getCountOfNoteInfo()
    }
  },
  methods: {

    // 退出登录
    signOut () {
      if (jsCookie.get('NNNnote_token')) {
        // 删除 token
        jsCookie.remove('NNNnote_token', { domain: 'localhost' })

        const data = {}
        // 清除用户信息
        this.$store.dispatch('userData/updateUserInfo', { data })
        this.$router.push({ path: '/' })
      }
    },

    // 跳转到编辑页面前，对笔记进行初始化
    toEditor () {
      noteApi.initializeNote(this.userInfo.id).then((response) => {
        if (response.data.code === 20000) {
          this.getCountOfNoteInfo()
          this.$router.push({ path: `/editor/${response.data.data.data}` })
        } else {
          this.$message.error(response.data.message)
        }
      })
    },
    // 将笔记相关数据更新到 store
    getCountOfNoteInfo () {
      noteApi.getCountOfNoteInfo().then((response) => {
        if (response.data.code === 20000) {
          const data = response.data.data
          this.$store.dispatch('userData/updateDataCount', { data })
        }
      })
    },
    // 获取用户信息
    getUserInfo () {
      userApi.getUserInfo()
        .then((response) => {
          if (response.data.code === 20000) {
            const data = response.data.data.data
            this.$store.dispatch('userData/updateUserInfo', { data })
          }
        })
    }
  }

}
</script>
<style>
body{
      background-color: #dddddd;
}
  *{
    padding:0px;
    margin:0px;
    box-sizing: border-box;
  }
  .head-bg {
    background-color: #323232;
  }
  .main{
    margin: 0px;
    padding: 10px;
    overflow: auto;
  }
  .h3 {
          color:#999;
    padding:30px 0px 0px 0px;
    font-size: 20px;
    font-family: "Lucida Console", "Courier New", monospace;
  }

.el-menu.el-menu--horizontal {
     border-bottom: 0px;
}
/* 设置 BaseLayout 下所有 <el-container/> 标签的最低高度 */
/* 但是这也导致，只要是 BaseLayout 基于的页面 el-container 的最低高度都是相同的*/
.el-container {
     min-height: calc(90vh);
}
</style>
