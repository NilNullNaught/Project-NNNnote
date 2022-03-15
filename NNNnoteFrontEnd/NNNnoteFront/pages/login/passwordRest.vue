<template>
  <div class="main">
    <div class="sign-up-container">
      <div class="title">
        <span style="font-weight:700;font-size:18px;color:green;">重新设置密码</span>
      </div>
      <!-- 开始 -- 表单 --------------------------------------------------------------------------------------------------------------------------------->

      <el-form ref="userForm" :model="params">
        <el-form-item
          prop="mobile"
          :rules="[
            { validator: checkEmail, trigger: ['blur'] },
          ]"
        >
          <div>
            <el-input v-model="params.email" type="text" placeholder="邮箱地址">
              <i slot="prefix" class="el-input__icon el-icon-user-solid" />
            </el-input>
          </div>
        </el-form-item>
        <el-form-item
          prop="code"
          :rules="[
            { required: true, message: '请输入验证码', trigger: 'blur' },
            { validator: checkCodeFormat, trigger: ['blur', 'change'] },
          ]"
        >
          <el-input v-model="params.code" type="text" placeholder="请输入验证码">
            <i slot="prefix" class="el-input__icon el-icon-s-promotion" />
            <el-button slot="append" :disabled="activeSendCodeBtn" @click="sendCode()">
              {{ codeTest }}
            </el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input v-model="params.password" type="password" placeholder="请输入密码">
              <i slot="prefix" class="el-input__icon el-icon-lock" />
            </el-input>
          </div>
        </el-form-item>
        <el-form-item prop="checkPasswordVal" :rules="[{validator: checkPassword, trigger: 'blur' }]">
          <div>
            <el-input v-model="checkPasswordVal" type="password" placeholder="请再次输入密码">
              <i slot="prefix" class="el-input__icon el-icon-lock" />
            </el-input>
          </div>
        </el-form-item>
        <div class="btn">
          <input type="button" class="sign-in-button" value="重置" @click="submitRestPassword()">
        </div>
      </el-form>
      <!-- 结束 -- 表单 --------------------------------------------------------------------------------------------------------------------------------->
    </div>
  </div>
</template>

<script>
import '~/assets/css/loginCss/sign.css'
import registerApi from '@/api/register'
import loginApi from '@/api/login'

export default {
  name: 'PasswordRestPage',
  layout: 'SignLayout',

  data () {
    return {
      // 封装登录手机号和密码对象
      params: {
        email: '',
        code: '', // 验证码
        password: ''
      },
      checkPasswordVal: '',
      second: 60, // 倒计时长度
      codeTest: '获取验证码',
      activeSendCodeBtn: false
    }
  },
  methods: {
    // 验证邮箱的合法性
    checkEmail (rule, value, callback) {
      if (this.params.email === '') {
        return callback(new Error('请输入邮箱地址'))
      }
      if (!(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(this.params.email))) {
        return callback(new Error('邮箱地址格式不正确'))
      }
    },
    // 验证再次输入的密码
    checkPassword (rule, value, callback) {
      if (this.checkPasswordVal === '') {
        return callback(new Error('请再次输入密码'))
      }
      if (this.params.password !== this.checkPasswordVal) {
        return callback(new Error('两次输入的密码不一致'))
      }
    },

    // 校验验证码格式
    checkCodeFormat (rule, value, callback) {
      if (!(/^[0-9]{6}$/.test(value))) {
        return callback(new Error('验证码应为六位数字'))
      }
      return callback()
    },

    // 发送验证码
    sendCode () {
      this.activeSendCodeBtn = true
      registerApi.sendCode(this.params.email)

      // 设置倒计时
      const result = setInterval(() => {
        --this.second
        this.codeTest = this.second
        if (this.second < 1) {
          clearInterval(result)
          this.activeSendCodeBtn = false
          this.second = 60
          this.codeTest = '获取验证码'
        }
      }, 1000)
    },

    // 发送重置密码请求
    submitRestPassword () {
      // 校验表单数据
      if (this.params.email === '' ||
       this.params.password === '' ||
       this.params.code === '' ||
       this.checkPasswordVal === ''
      ) {
        this.$message({
          type: 'warning',
          message: '请填写邮箱地址、密码与验证码'
        })
        return
      }

      loginApi.resetPassword(this.params)
        .then((response) => {
          if (response.data.code === 20000) {
            // 提示密码修改成功
            this.$message({
              type: 'success',
              message: '密码修改成功'
            })

            // 跳转至登录页
            this.$router.push({ path: '/login' })
          } else {
            // 提示密码修改
            this.$message.error('密码修改失败')
          }
        })
    }
  }
}
</script>
