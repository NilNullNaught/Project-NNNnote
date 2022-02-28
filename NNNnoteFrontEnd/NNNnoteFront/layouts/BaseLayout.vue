<template>
  <div>
    <el-container>
      <el-header class="head-bg">
        <el-row :gutter="20" align="middle" type="flex">
          <el-col :span="4">
            <el-image
              style="width: 55px; height: 55px;padding:5px 0px 0px 0px;"
              :src="style.logo"
            />
          </el-col>
          <el-col :span="5">
            <el-menu
              :default-active="style.activeIndex"
              mode="horizontal"
              background-color="#323232"
              text-color="#fff"
              active-text-color="#7AFFA0"
              router
            >
              <el-menu-item index="/">
                主页
              </el-menu-item>
              <el-menu-item index="/note">
                推荐
              </el-menu-item>
            </el-menu>
          </el-col>
          <el-col :span="6">
            <div>
              <el-input placeholder="请输入内容" class="input-with-select">
                <el-button slot="append" icon="el-icon-search" />
              </el-input>
            </div>
          </el-col>
          <el-col :offset="5" :span="4" align="end">
            <div>
              <div v-if="userInfo.id">
                <el-menu
                  :default-active="style.activeIndex"
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
                    <el-menu-item index="/user/nfolder">
                      我的主页
                    </el-menu-item>
                    <el-menu-item index="/user/collection">
                      收藏夹
                    </el-menu-item>
                    <el-menu-item index="/user/draft">
                      草稿箱
                    </el-menu-item>
                    <el-menu-item index="/user/recyclebin">
                      回收站
                    </el-menu-item>
                    <el-menu-item index="/setting">
                      设置
                    </el-menu-item>
                    <el-menu-item @click="signOut">
                      退出
                    </el-menu-item>
                  </el-submenu>
                  <el-menu-item @click="toEditor">
                    写笔记
                  </el-menu-item>
                </el-menu>
              </div>
              <div v-if="!userInfo.id">
                <el-menu
                  :default-active="style.activeIndex"
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
                  <el-menu-item index="/register">
                    注册
                  </el-menu-item>
                </el-menu>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-header>

      <el-main class="main">
        <el-row align="middle" justify="center" type="flex">
          <el-col :span="12">
            <el-card :body-style="{ padding: '0px' }" shadow="always">
              <nuxt />
            </el-card>
          </el-col>
        </el-row>
      </el-main>

      <el-footer class="head-bg" height="120px">
        <el-row justify="center" type="flex">
          <el-col :span="16" align="center" justify="center" type="flex">
            <!-- <el-descriptions id="BaseLayout_el_descriptions_backgroundcolor" title="Present by NilNullNaught">
              <el-descriptions-item :span="1" label="Email">
                nilnullnaught@gmail.com
              </el-descriptions-item>
              <el-descriptions-item label="项目地址">
                github.com/NilNullNaught/Project-NNNnote
              </el-descriptions-item>
              <el-descriptions-item :span="1" label="联系方式">
                188********
              </el-descriptions-item>
            </el-descriptions> -->
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

export default {
  data () {
    return {
      style: {
        activeIndex: '1',
        logo: require('~/assets/img/logo.png')
      },
      userInfo: {
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
      this.userInfo = JSON.parse(userStr)
    }
  },
  methods: {

    // 退出登录
    signOut () {
      if (jsCookie.get('NNNnote_token')) {
        jsCookie.remove('NNNnote_token', { domain: 'localhost' }) // 删除成功
        jsCookie.remove('NNNnote_userInfo', { domain: 'localhost' })
        this.userInfo = ''
        this.style.activeIndex = 1
        this.$router.push({ path: '/' })
      }
    },

    // 跳转到编辑页面前，对笔记进行初始化
    toEditor () {
      noteApi.initializeNote(this.userInfo.id).then((response) => {
        if (response.data.code === 20000) {
          this.$router.push({ path: `/editor/${response.data.data.data}` })
        } else {
          this.$message.error(response.data.message)
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
    font-size: 14px;
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
#BaseLayout_el_descriptions_backgroundcolor .el-descriptions__title{
          color:#999;
    padding:30px 0px 0px 0px;
    font-size: 20px;
    font-family: "Lucida Console", "Courier New", monospace;
}

#BaseLayout_el_descriptions_backgroundcolor .el-descriptions__body{
      background-color: #323232;
}
</style>
