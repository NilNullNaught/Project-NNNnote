import request from '@/utils/request'

const ApiName = '/user/user-check'
export default {
  login (formItem) {
    return request({
      url: `${ApiName}/login`,
      method: 'post',
      data: formItem
    })
  }

}
