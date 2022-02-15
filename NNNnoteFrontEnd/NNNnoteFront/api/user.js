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
  // 修改笔记文件夹信息
  alterUserNfolder (formdata) {
    return request({
      url: `${ApiName3}/alterUserNfolder`,
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
  },
  // 分页查询 笔记文件夹列表
  getUserNfolderPage (page, limit) {
    return request({
      url: `${ApiName3}/getUserNfolder/${page}/${limit}`,
      method: 'get'
    })
  },
  // 分页条件查询 笔记与笔记文件夹
  getNfolderANDNote (page, limit, condition) {
    return request({
      url: `${ApiName3}/getNfolderANDNote/${page}/${limit}/${condition}`,
      method: 'get'
    })
  },
  // 批量删除
  deleteUserNFolder (list) {
    return request({
      url: `${ApiName3}/deleteUserNFolder`,
      method: 'delete',
      data: list
    })
  }
}
