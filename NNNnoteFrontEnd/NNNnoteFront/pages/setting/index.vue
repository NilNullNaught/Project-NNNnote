<template>
  <el-container>
    <el-aside>
      <el-menu
        default-active="1"
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
      <el-form ref="userInfo" :model="userInfo" label-width="80px">
        <el-form-item>
          <el-col class="line" :span="5">
            <el-avatar size="large" :src="userInfo.avatar" style="width:80px;height:80px;block:inline" />
          </el-col>
          <el-col :span="8">
            <el-upload
              class="avatar-uploader"
              action="https://jsonplaceholder.typicode.com/posts/"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon" />
            </el-upload>
          </el-col>
        </el-form-item>

        <el-form-item prop="nickname" label="昵称">
          <el-col class="line" :span="12">
            <el-input v-model="userInfo.nickname" maxlength="10" show-word-limit />
          </el-col>
        </el-form-item>

        <el-form-item prop="birthday" label="生日">
          <el-col class="line" :span="12">
            <el-date-picker v-model="userInfo.birthday" type="date" placeholder="选择日期" style="width: 60%;" />
          </el-col>
        </el-form-item>

        <el-form-item prop="sex" label="性别">
          <el-radio-group v-model="userInfo.sex" @change="consoleMessage">
            <el-radio label="0" name="type">
              男
            </el-radio>
            <el-radio label="1" name="type">
              女
            </el-radio>
            <el-radio label="2" name="type">
              保密
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="sign" label="签名">
          <el-input v-model="userInfo.sign" style="height:100px" type="textarea" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="activeSubmitBtn" @click="onSubmit">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
import jsCookie from 'js-cookie'
import userApi from '@/api/user'
export default {

  name: 'SettingIndexPage',
  layout: 'BaseLayout',

  data () {
    return {
      userStr: '',
      imageUrl: '',
      activeSubmitBtn: false,
      userInfo: {
      }
    }
  },
  created () {
    // 从 cookie 中获取用户信息
    this.userStr = jsCookie.get('NNNnote_userInfo')
    // 把字符串转换json对象(js对象)
    if (this.userStr) {
      this.userInfo = JSON.parse(this.userStr)
    }
  },
  methods: {
    // 提交修改的用户信息
    onSubmit () {
      const _userInfo = JSON.parse(this.userStr)

      // 校验用户信息是否被修改
      if (_userInfo.avatar === this.userInfo.avatar &&
          _userInfo.nickname === this.userInfo.nickname &&
          _userInfo.birthday === this.userInfo.birthday &&
          _userInfo.sex === this.userInfo.sex &&
          _userInfo.sign === this.userInfo.sign
      ) {
        // 提示修改成功
        this.$message({
          type: 'warning',
          message: '请在修改后点击保存按钮'
        })
        return
      }

      this.activeSubmitBtn = true
      userApi.alterUserInfo(this.userInfo)
        .then((response) => {
          if (response.data.code === 20000) {
            // 提示修改成功
            this.$message({
              type: 'success',
              message: '修改成功'
            })
            this.activeSubmitBtn = false
            // 重新获取用户信息
            userApi.getUserInfo()
              .then((response) => {
                const userInfo = JSON.stringify(response.data.data.data)
                jsCookie.set('NNNnote_userInfo', userInfo)
                // 刷新页面
                this.$router.push({ path: '/setting' })
              })
          }
        })
    },
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

    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
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

/* 上传头像组件 CSS*/
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 80px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
    border-radius: 80px;
  }
  .avatar-uploader-icon {
    font-size: 20px;
    color: #8c939d;
    width: 80px;
    height: 80px;
    line-height: 80px;
    text-align: center;
  }
  .avatar {
    width: 80px;
    height: 80px;
    display: block;
    border-radius:20px;
  }
</style>
