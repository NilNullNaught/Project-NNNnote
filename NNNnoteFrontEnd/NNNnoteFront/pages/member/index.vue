<template>
  <el-container style="min-height: calc(75vh);">
    <el-main>
      <el-row>
        <el-col :span="24" align="middle">
          <h2>
            会员权益
          </h2>
        </el-col>
      </el-row>
      <el-row style="margin-top:20px">
        <el-col :span="18" :offset="6">
          1. 解除笔记上限
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18" :offset="6">
          2. 暂无
        </el-col>
      </el-row>
      <el-row style="margin-top: calc(20vh);">
        <el-col :span="24" align="middle">
          <el-button type="success" round @click="createOrder">
            确认开通
          </el-button>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import transaction from '@/api/transaction'
export default {
  name: 'MemberIndexPage',
  layout: 'BaseLayout',
  methods: {
    createOrder () {
      transaction.createOrder().then((response) => {
        if (response.data.success) {
          // 订单创建成功，跳转到订单页面
          this.$router.push({ path: '/order/' + response.data.data.data })
        }
      })
    }
  }
}
</script>
<style scoped>

</style>
