import request from '@/utils/request'

const ApiName = '/user/user-message'
export default {
  getTestData () {
    return request({
      url: `${ApiName}/getallUser`,
      method: 'get'
    })
  }
}
