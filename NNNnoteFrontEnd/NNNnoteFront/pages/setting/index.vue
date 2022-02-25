<template>
  <el-container>
    <!-- 侧边栏 ----------------------------------------------------------------------------------------------------------------------------------------->
    <el-aside>
      <el-menu
        default-active="/setting"
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

    <el-main>
      <el-form ref="userInfo" :model="userInfo" label-width="80px">
        <el-form-item>
          <el-col class="line" :span="5">
            <el-avatar size="large" :src="userInfo.avatar" style="width:80px;height:80px;block:inline" />
          </el-col>
          <!-- 头像上传 ----------------------------------------------------------------------------------------------------------------------------------------->
          <el-col :span="8">
            <el-upload
              class="avatar-uploader"
              action="fakeaction"
              :http-request="uploadAvatar"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon" />
            </el-upload>
          </el-col>
          <!------------------------------------------------------------------------------------------------------------------------------------------->
        </el-form-item>

        <el-form-item prop="nickname" label="昵称">
          <el-col class="line" :span="12">
            <el-input v-model="userInfo.nickname" maxlength="10" show-word-limit />
          </el-col>
        </el-form-item>

        <el-form-item prop="birthday" label="生日">
          <el-col class="line" :span="12">
            <el-date-picker
              v-model="userInfo.birthday"
              type="date"
              placeholder="选择日期"
              style="width: 60%;"
              value-format="yyyy-MM-ddThh:mm:ss.sssZ"
            />
          </el-col>
        </el-form-item>

        <el-form-item prop="sex" label="性别">
          <el-radio-group :key="userInfo.sex" v-model="userInfo.sex">
            <el-radio :label="0">
              男
            </el-radio>
            <el-radio :label="1">
              女
            </el-radio>
            <el-radio :label="2">
              保密
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="sign" label="签名">
          <el-col class="line" :span="18">
            <el-input
              v-model="userInfo.sign"
              type="textarea"
              rows="6"
              maxlength="100"
              show-word-limit
            />
          </el-col>
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
import ossApi from '@/api/oss'
export default {
  name: 'SettingIndexPage',
  // vue路由的钩子函数
  beforeRouteLeave (to, from, next) {
    if (JSON.stringify(this.userInfoCheck) !==
      JSON.stringify(this.userInfo)) {
      this.$confirm('离开页面 , 数据将不做保存, 请确认已经保存', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          next()
        })
        .catch(() => {
          // alert("router")
        })
    } else {
      next()
    }
  },
  layout: 'BaseLayout',
  data () {
    return {
      imageUrl: '',
      activeSubmitBtn: false,
      userInfo: {
        id: '',
        nickname: '',
        sex: null,
        birthday: '',
        avatar: '',
        sign: ''
      },
      userInfoCheck: {
        id: '',
        nickname: '',
        sex: null,
        birthday: '',
        avatar: '',
        sign: ''
      }
    }
  },
  created () {
    // 从 cookie 中获取用户信息
    this.userStr = jsCookie.get('NNNnote_userInfo')
    // 把字符串转换json对象(js对象)
    if (this.userStr) {
      this.userInfo = JSON.parse(this.userStr)
      this.userInfoCheck = JSON.parse(this.userStr)
    }
  },
  mounted () {
    // 校验用户信息是否被修改
    if (JSON.stringify(this.userInfoCheck) !==
      JSON.stringify(this.userInfo) ||
          this.imageUrl !== ''
    ) {
      window.onbeforeunload = function (e) { // 刷新与关闭时弹出提示
        e = e || window.event
        // 兼容IE8和Firefox 4之前的版本
        if (e) {
          e.returnValue = '关闭提示'
        }
        // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
        return '关闭提示'
      }
    }
  },
  methods: {
    // 提交修改的用户信息
    onSubmit () {
      // <-校验用户信息是否被修改
      // 是否上传了新的头像
      if (this.imageUrl !== '') {
        // 如果头像被改变，则提交修改
        this.userInfo.avatar = this.imageUrl
      }
      // 检测用户信息是否进行了修改
      if (JSON.stringify(this.userInfoCheck) ===
        JSON.stringify(this.userInfo)) {
        // 提示修改失败
        this.$message({
          type: 'warning',
          message: '请在修改后点击保存按钮'
        })
        return
      }
      // ->

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
                // 使用 window.logcation.href 跳转页面,可以刷新所有组件状态
                window.location.href = '/setting'
              })
          }
        })
    },

    // 检查头像文件格式与大小
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2
      this.file = file
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    // 上传头像
    uploadAvatar (params) {
      const data = new FormData()
      // 创建一个表单数据
      data.append('file', params.file)
      ossApi.uploadFile(data).then((response) => {
        if (response.data.code === 20000) {
          // 提示修改成功
          this.$message({
            type: 'success',
            message: '头像上传成功'
          })
          this.imageUrl = response.data.data.url
        } else {
          this.$message.error('头像上传失败')
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
