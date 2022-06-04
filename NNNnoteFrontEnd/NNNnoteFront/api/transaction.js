import request from '@/utils/request'
const ApiName = '/transaction/transaction-member'

export default {
  // 创建订单
  createOrder () {
    return request({
      url: ApiName + '/createOrder',
      method: 'post'
    })
  },
  // 根据id获取订单
  getOrder (orderId) {
    return request({
      url: ApiName + '/getOrder',
      method: 'get',
      params: orderId
    })
  },
  // 生成微信支付二维码
  generateWechatQR (orderNo) {
    return request({
      url: ApiName + '/generateWechatQR',
      method: 'get',
      params: orderNo
    })
  },
  // 根据id获取订单支付状态
  getPayStatus (orderNo) {
    return request({
      url: ApiName + '/getPayStatus',
      method: 'get',
      params: orderNo
    })
  }
}
