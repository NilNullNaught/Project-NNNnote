import request from '@/utils/request'

const ApiName = '/user/user-info'
const ApiName2 = '/user/user-check'
const ApiName3 = '/user/user-nfolder'
const ApiName4 = '/user/user-cfolder'
const ApiName5 = '/user/user-member'
const ApiName6 = '/user/user-follow'
const ApiName7 = '/user/user-dynamic'

export default {
  // 获取用户信息
  getUserInfo () {
    return request({
      url: `${ApiName}/getUserInfo`,
      method: 'get'
    })
  },
  // 获取用户信息
  getUserInfoById (id) {
    return request({
      url: `${ApiName}/getUserInfoById/${id}`,
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
  // 根据文件夹 ID 查询获取笔记文件夹
  getNotefolderBynFolderId (noteFolderID) {
    return request({
      url: `${ApiName3}/getNotefolderBynFolderId/${noteFolderID}`,
      method: 'get'
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
  // 批量删除笔记文件夹
  deleteUserNFolder (list) {
    return request({
      url: `${ApiName3}/deleteUserNFolder`,
      method: 'delete',
      data: list
    })
  },
  // 根据文件夹 Id 获取笔记文件夹标题
  getNoteFolderNameByFolderId (list) {
    return request({
      url: `${ApiName3}/getNoteFolderNameByFolderId`,
      method: 'post',
      data: list
    })
  },
  // 通过token获取用户的收藏夹
  getUserCfolders () {
    return request({
      url: `${ApiName4}/getUserCfolders`,
      method: 'get'
    })
  },
  // 根据用户 id 创建新的收藏夹，文件夹描述可以为空
  addUserCfolder (dataform) {
    return request({
      url: `${ApiName4}/addUserCfolder`,
      method: 'post',
      data: dataform
    })
  },
  // 分页查询笔记收藏夹列表
  getUserCfolderPaging (param) {
    return request({
      url: `${ApiName4}/getUserCfolderPaging`,
      method: 'get',
      params: param
    })
  },
  // 删除收藏夹
  deleteCFolders (list) {
    return request({
      url: `${ApiName4}/deleteCFolders`,
      method: 'delete',
      data: list
    })
  },
  // 修改收藏夹
  updateUserCfolder (formdata) {
    return request({
      url: `${ApiName4}/updateUserCfolder`,
      method: 'post',
      data: formdata
    })
  },
  // 获取收藏夹信息
  getUserCfolderById (param) {
    return request({
      url: `${ApiName4}/getUserCfolderById`,
      method: 'get',
      params: param
    })
  },
  // 获取用户会员信息
  getUserMember () {
    return request({
      url: `${ApiName5}/getUserMember`,
      method: 'get'
    })
  },
  // 通过 ID 获取用户会员信息
  getUserMemberById (userId) {
    return request({
      url: `${ApiName5}/getUserMemberById`,
      method: 'get',
      params: userId
    })
  },
  // 关注与取消关注
  followAndCancel (followUserID) {
    return request({
      url: `${ApiName6}/followAndCancel`,
      method: 'post',
      data: followUserID
    })
  },
  // 查询是否关注该用户
  isFollowed (followUserID) {
    return request({
      url: `${ApiName6}/isFollowed`,
      method: 'get',
      params: followUserID
    })
  },
  // 查询关注列表
  getFollowList () {
    return request({
      url: `${ApiName6}/getFollowList`,
      method: 'get'
    })
  },
  // 查询当前用户关注用户的动态（包括自己）
  getFollowUserDynamic (param) {
    return request({
      url: `${ApiName7}/getFollowUserDynamic`,
      method: 'get',
      params: param
    })
  },
  // 查询指定用户的所有动态
  getDynamicByUserId (param) {
    return request({
      url: `${ApiName7}/getDynamicByUserId`,
      method: 'get',
      params: param
    })
  }
}
