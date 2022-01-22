import request from '@/utils/request'

const ApiName = '/user/user-check'
export default {
  // 登录
  login (form) {
    return request({
      url: `${ApiName}/login`,
      method: 'post',
      data: form
    })
  },
  // 密码重置
  resetPassword (form) {
    return request({
      url: `${ApiName}/resetPassword`,
      method: 'post',
      data: form
    })
  }

}
