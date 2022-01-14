import request from '@/utils/request'

const ApiName = '/user/user-message'
export default {
  // 查询前两条banner数据
  getTestData () {
    return request({
      url: `${ApiName}/getallUser`,
      method: 'get'
    })
  }
}
