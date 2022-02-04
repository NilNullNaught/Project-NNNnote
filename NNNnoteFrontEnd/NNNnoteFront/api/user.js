import request from '@/utils/request'

const ApiName = '/user/user-info'
const ApiName2 = '/user/user-check'
const ApiName3 = '/user/user-nfolder'

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
  },
  // 获取用户安全信息
  alterUserSafeInfo () {
    return request({
      url: `${ApiName2}/alterUserSafeInfo`,
      method: 'get'
    })
  },
  // 修改用户邮箱地址
  alterUserEmail (email) {
    return request({
      url: `${ApiName2}/alterUserEmail`,
      method: 'post',
      data: email
    })
  },
  // 创建笔记文件夹
  addUserNfolder (formdata) {
    return request({
      url: `${ApiName3}/addUserNfolder`,
      method: 'post',
      data: formdata
    })
  },
  // 获取笔记文件夹列表
  getUserNfolder () {
    return request({
      url: `${ApiName3}/getUserNfolder`,
      method: 'get'
    })
  }
}
