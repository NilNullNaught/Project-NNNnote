<template>
  <div class="in-wrap">
    <!-- 开始 -- 公共头 --------------------------------------------------------------------------------------------------------------------------------->
    <header id="header" style="padding:0;background-color:#323232;">
      <section class="container">
        <h1 id="logo">
          <a href="/" title="NNN 笔记">
            <img src="~/assets/img/logo.png" width="60px" alt="NNN 笔记">
          </a>
        </h1>
        <div class="h-r-nsl">
          <ul class="nav">
            <router-link to="/" tag="li" active-class="current" exact>
              <a>首页</a>
            </router-link>
            <router-link to="/recommond" tag="li" active-class="current" exact>
              <a>推荐</a>
            </router-link>
          </ul>

          <ul class="h-r-login">
            <!-- 开始 -- 未登录显示部分 --------------------------------------------------------------------------------------------------------------------------------->
            <li v-if="!loginInfo.id" id="no-login">
              <a href="/login" title="登录">
                <em class="icon18 login-icon">&nbsp;</em>
                <span class="vam ml5">登录</span>
              </a>
              |
              <a href="/register" title="注册">
                <span class="vam ml5">注册</span>
              </a>
            </li>
            <!-- 结束 -- 未登录显示部分 --------------------------------------------------------------------------------------------------------------------------------->

            <!-- 开始 -- 登录显示部分 --------------------------------------------------------------------------------------------------------------------------------->
            <li v-if="loginInfo.id" id="is-login-one" class="mr10">
              <a id="headerMsgCountId" href="#" title="消息">
                <em class="icon18 news-icon">&nbsp;</em>
              </a>
              <q class="red-point" style="display: none">&nbsp;</q>
            </li>
            <li v-if="loginInfo.id" id="is-login-two" class="h-r-user">
              <a href="/ucenter" title>
                <img
                  :src="loginInfo.avatar"
                  width="30"
                  height="30"
                  class="vam picImg"
                  alt
                >
                <span id="userName" class="vam disIb">{{ loginInfo.nickname }}</span>
              </a>
              <a href="javascript:void(0);" title="退出" class="ml5" @click="logout()">退出</a>
            </li>
            <!-- 结束 -- 登录显示部分 --------------------------------------------------------------------------------------------------------------------------------->
          </ul>

          <aside class="h-r-search">
            <form action="#" method="post">
              <label class="h-r-s-box">
                <input type="text" placeholder="输入" name="queryCourse.courseName" value>
                <button type="submit" class="s-btn">
                  <em class="icon18">&nbsp;</em>
                </button>
              </label>
            </form>
          </aside>
        </div>

        <div class="clear" />
      </section>
    </header>
    <!-- 结束 -- 公共头 --------------------------------------------------------------------------------------------------------------------------------->

    <nuxt />

    <!-- 开始 -- 公共底 --------------------------------------------------------------------------------------------------------------------------------->
    <footer id="footer">
      <section class="container" style="text-align:center">
        <h4>
          <span class="fsize18 f-fM c-999">Present by NilNullNaught</span>
        </h4>
        <div class="b-foot">
          <section class="mr20">
            <section class="b-f-link" style="text-align:center">
              <span><a href="https://github.com/NilNullNaught/Project-NNNnote">GitHub：github.com/NilNullNaught/Project-NNNnote</a></span>
              <span>Email：nilnullnaught@gmail.com</span>
              <span>联系方式：188********</span>
            </section>
          </section>
        </div>
      </section>
      <div class="clear" />
    </footer>
    <!-- 结束 -- 公共底 --------------------------------------------------------------------------------------------------------------------------------->
  </div>
</template>

<script>
import '~/assets/css/reset.css'
import '~/assets/css/theme.css'
import '~/assets/css/global.css'
import '~/assets/css/web.css'
import '~/assets/css/base.css'
import '~/assets/css/activity_tab.css'
import '~/assets/css/bottom_rec.css'
import '~/assets/css/nice_select.css'
import '~/assets/css/order.css'
import '~/assets/css/swiper-3.3.1.min.css'
import '~/assets/css/pages-weixinpay.css'
export default {
  data () {
    return {
      token: '',
      loginInfo: {
        id: '',
        age: '',
        avatar: '',
        mobile: '',
        nickname: '',
        sex: ''
      }
    }
  }
}
</script>
