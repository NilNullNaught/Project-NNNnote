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
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item>
          <el-col class="line" :span="5">
            <el-avatar size="large" style="width:80px;height:80px;block:inline" />
          </el-col>
          <el-col :span="8">
            <el-upload
              class="avatar-uploader"
              action="https://jsonplaceholder.typicode.com/posts/"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon" />
            </el-upload>
          </el-col>
        </el-form-item>

        <el-form-item label="昵称">
          <el-col class="line" :span="12">
            <el-input v-model="form.name" maxlength="10" show-word-limit />
          </el-col>
        </el-form-item>

        <el-form-item label="年龄">
          <el-col class="line" :span="12">
            <el-select v-model="form.region" placeholder="请选择年龄">
              <el-option label="区域一" value="shanghai" />
              <el-option label="区域二" value="beijing" />
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="form.type">
            <el-radio label="男" name="type" />
            <el-radio label="女" name="type" />
            <el-radio label="保密" name="type" />
          </el-radio-group>
        </el-form-item>

        <el-form-item label="签名">
          <el-input v-model="form.desc" style="height:100px" type="textarea" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">
            立即创建
          </el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'SettingIndexPage',
  layout: 'BaseLayout',

  data () {
    return {

      imageUrl: '',
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  created () {},
  methods: {
    onSubmit () {

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

    handleAvatarSuccess (res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
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
