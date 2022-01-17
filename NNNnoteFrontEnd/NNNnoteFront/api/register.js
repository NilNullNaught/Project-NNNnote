import request from '@/utils/request'

const ApiName = '/user/user-check'
export default {
  // 检查邮箱地址是否被注册
  checkEmail (email) {
    return request({
      url: `${ApiName}/checkEmail/${email}`,
      method: 'get'
    })
  },
  // 发送验证码
  sendCode (email) {
    return request({
      url: `${ApiName}/sendCode/${email}`,
      method: 'get'
    })
  },
  register (formItem) {
    return request({
      url: `${ApiName}/register`,
      method: 'post',
      data: formItem
    })
  }

}
