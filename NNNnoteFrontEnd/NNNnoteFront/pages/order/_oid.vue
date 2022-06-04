<template>
  <el-container style="min-height: calc(75vh);">
    <el-main>
      <el-row style="margin-top: calc(15vh);">
        <el-col :span="24" align="middle">
          <h2>
            订单确认
          </h2>
        </el-col>
      </el-row>

      <el-row style="margin-top: calc(5vh);">
        <el-col :span="24" align="middle">
          <p>金额 ￥<strong>{{ order ? order.totalFee : "" }}</strong></p>
        </el-col>
      </el-row>

      <el-row style="margin-top: calc(5vh);">
        <el-col :span="24" align="middle">
          <label>
            <p class="on">
              <input type="checkbox" checked="checked">我已阅读并同意<a href="javascript:" target="_blank">《协议》</a>
            </p>
          </label>
        </el-col>
      </el-row>

      <el-row style="margin-top: calc(5vh);">
        <el-col :span="24" align="middle">
          <el-button type="success" round @click="createOrder">
            <i class="alibaba_icons_weixin" /> 微信支付
          </el-button>
          <el-button round @click="$router.push({ path: `/user` })">
            取消
          </el-button>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import transactionApi from '@/api/transaction'
export default {
  name: 'OrderIdPage',
  layout: 'BaseLayout',
  // 根据订单id获取订单信息
  asyncData ({ params, error }) {
    const data = { orderId: params.oid }
    return transactionApi.getOrder(data).then((response) => {
      return {
        order: response.data.data.data
      }
    })
  },
  methods: {
    // 点击微信支付，跳转到支付页面
    createOrder () {
      this.$router.push({ path: '/pay/' + this.order.orderNo })
    }
  }
}
</script>
<style scoped>

</style>
