import request from '@/utils/request'

const ApiName = '/user/user-info'
export default {
  getUserInfo () {
    return request({
      url: `${ApiName}/getUserInfo`,
      method: 'get'
    })
  }
}
