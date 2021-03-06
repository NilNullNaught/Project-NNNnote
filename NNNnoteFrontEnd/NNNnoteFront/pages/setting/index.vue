<template>
  <el-container style="padding:20px">
    <!-- 侧边栏 ----------------------------------------------------------------------------------------------------------------------------------------->
    <el-aside width="200px">
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
      <el-form ref="userInfo" :model="newUserInfo" label-width="80px">
        <el-form-item>
          <el-col class="line" :span="6">
            <el-avatar
              size="large"
              :src="newUserInfo.avatar"
              style="width:80px;height:80px;block:inline;"
              @error="true"
            >
              <img src="@/static/img/unsetAvatar.png">
            </el-avatar>
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
          <el-col class="line" :span="14">
            <el-input v-model="newUserInfo.nickname" maxlength="10" show-word-limit />
          </el-col>
        </el-form-item>

        <el-form-item prop="birthday" label="生日">
          <el-col class="line" :span="14">
            <el-date-picker
              v-model="newUserInfo.birthday"
              type="date"
              placeholder="选择日期"
              style="width: 60%;"
              value-format="yyyy-MM-ddThh:mm:ss.sssZ"
            />
          </el-col>
        </el-form-item>

        <el-form-item prop="sex" label="性别">
          <el-radio-group v-model="newUserInfo.sex">
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
              v-model="newUserInfo.sign"
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
import userApi from '@/api/user'
import ossApi from '@/api/oss'
export default {
  name: 'SettingIndexPage',
  // vue路由的钩子函数
  beforeRouteLeave (to, from, next) {
    if (JSON.stringify(this.newUserInfo) !==
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
      newUserInfo: {
        id: '',
        nickname: '',
        sex: '',
        birthday: '',
        avatar: '',
        sign: ''
      }
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
  watch: {
    userInfo: {
      handler () {
        this.newUserInfo = JSON.parse(JSON.stringify(this.$store.state.userData.userInfo))
      },
      immediate: true,
      deep: true
    }
  },
  created () {

  },

  methods: {
    // 提交修改的用户信息
    onSubmit () {
      // <-校验用户信息是否被修改
      // 是否上传了新的头像
      if (this.imageUrl !== '') {
        // 如果头像被改变，则提交修改
        this.newUserInfo.avatar = this.imageUrl
      }
      // 检测用户信息是否进行了修改
      if (JSON.stringify(this.newUserInfo) ===
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

      userApi.alterUserInfo(this.newUserInfo)
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
                const data = response.data.data.data
                this.newUserInfo = response.data.data.data
                this.$store.dispatch('userData/updateUserInfo', { data })
              })
          }
        })
    },

    // 检查头像文件格式与大小
    beforeAvatarUpload (file) {
      const isJPG = (file.type === 'image/jpeg' || file.type === 'image/png')
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
<style scoped>
.el-aside{
  border-right: solid 1px #e6e6e6;
}
.el-menu{
  border: 0px;
}

/* 上传头像组件 CSS*/
  .avatar-uploader ::v-deep .el-upload {
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
