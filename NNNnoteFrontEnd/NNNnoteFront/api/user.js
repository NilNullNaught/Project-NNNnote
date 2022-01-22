import request from '@/utils/request'

const ApiName = '/user/user-info'
export default {
  // 获取用户信息
  getUserInfo () {
    return request({
      url: `${ApiName}/getUserInfo`,
      method: 'get'
    })
  },
  // 修改用户信息
  alterUserInfo (form) {
    return request({
      url: `${ApiName}/alterUserInfo`,
      method: 'post',
      data: form
    })
  }
}
