<template>
  <el-container style="min-height: calc(75vh);">
    <el-main>
      <el-row style="margin-top: calc(15vh);">
        <el-col :span="24" align="middle">
          <h4>
            订单提交成功，请您及时付款！订单号：{{ payObj?payObj.orderNo:'' }}
          </h4>
        </el-col>
      </el-row>

      <el-row style="margin-top: calc(5vh);">
        <el-col :span="24" align="middle">
          <!-- 使用 <qriously/> 标签需要引入 vue-qriously 插件-->
          <qriously :value="payObj?payObj.code_url:''" :size="200" />
        </el-col>
      </el-row>
      <el-row style="margin-top: calc(5vh);">
        <el-col :span="24" align="middle">
          <p>请使用微信扫一扫</p>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" align="middle">
          <p>扫描二维码支付</p>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import transactionApi from '@/api/transaction'

export default {
  name: 'PayIdPage',
  layout: 'BaseLayout',
  data () {
    return {
      timer: null, // 定时器名称
      initQCode: '',
      timer1: '',
      payObj: null
    }
  },
  created () {
    const data = { orderNo: this.$route.params.pid }
    transactionApi.generateWechatQR(data).then((response) => {
      this.payObj = response.data.data
    })
  },

  mounted () {
    // 在页面渲染之后执行
    // 每隔三秒，去查询一次支付状态
    this.timer1 = setInterval(() => {
      this.getPayStatus(this.payObj.orderNo)
    }, 3000)
  },
  methods: {
    // 查询支付状态的方法
    getPayStatus (payOrderNo) {
      const data = { orderNo: payOrderNo }
      transactionApi.getPayStatus(data).then((response) => {
        if (response.data.code === 20000) {
          // 如果支付成功，清除定时器
          clearInterval(this.timer1)
          this.$message({
            type: 'success',
            message: '支付成功!'
          })
          // 跳转到个人主页
          this.$router.push({ path: '/user' })
        }
      })
    }
  }
}
</script>
