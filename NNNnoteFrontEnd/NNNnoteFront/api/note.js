import request from '@/utils/request'

const ApiName = '/note/note-info'
const ApiName2 = '/note/note-user-collection'
const ApiName3 = '/note/note-comment'
export default {
  // 笔记初始化
  initializeNote (nFolderId) {
    return request({
      url: `${ApiName}/initializeNote/${nFolderId}`,
      method: 'post'
    })
  },
  // 获取笔记信息（编辑）
  getNoteInfoToEdit (noteId) {
    return request({
      url: `${ApiName}/getNoteInfoToEdit/${noteId}`,
      method: 'get'
    })
  },
  // 获取笔记信息（查看）
  getNoteInfoToRead (noteId) {
    return request({
      url: `${ApiName}/getNoteInfoToRead/${noteId}`,
      method: 'get'
    })
  },
  // 保存笔记
  saveNote (saveNote) {
    return request({
      url: `${ApiName}/saveNote`,
      method: 'post',
      data: saveNote
    })
  },
  // 自动保存笔记
  autoSaveNote (saveNote) {
    return request({
      url: `${ApiName}/autoSaveNote`,
      method: 'post',
      data: saveNote
    })
  },
  // 分页条件查询笔记
  getNotes (formdata) {
    return request({
      url: `${ApiName}/getNotes`,
      method: 'post',
      data: formdata
    })
  },
  // 批量删除笔记
  deleteNotes (list) {
    return request({
      url: `${ApiName}/deleteNotes`,
      method: 'delete',
      data: list
    })
  },
  // 分页查询草稿
  getDraftList (formdata) {
    return request({
      url: `${ApiName}/getDraftList`,
      method: 'post',
      data: formdata
    })
  },
  // 批量删除草稿
  deleteDrafts (list) {
    return request({
      url: `${ApiName}/deleteDrafts`,
      method: 'delete',
      data: list
    })
  },
  // 分页查询被逻辑删除的笔记
  getLogicDeletedNoteList (formdata) {
    return request({
      url: `${ApiName}/getLogicDeletedNoteList`,
      method: 'post',
      data: formdata
    })
  },
  // 还原被逻辑删除的笔记
  restoreDeletedNote (formdata) {
    return request({
      url: `${ApiName}/restoreDeletedNote`,
      method: 'post',
      data: formdata
    })
  },
  // 删除回收站中的笔记
  deleteDeletedNotes (list) {
    return request({
      url: `${ApiName}/deleteDeletedNotes`,
      method: 'delete',
      data: list
    })
  },
  // 查询笔记相关数据（回收站数量，草稿数量，笔记总数）
  getCountOfNoteInfo () {
    return request({
      url: `${ApiName}/getCountOfNoteInfo`,
      method: 'get'
    })
  },
  // 搜索笔记（分页，可以附带条件）
  searchNoteList (data) {
    return request({
      url: `${ApiName}/searchNoteList`,
      method: 'get',
      params: data
    })
  },
  // 判断用户有没有对该笔记进行过点赞
  userLikeNote (data) {
    return request({
      url: `${ApiName}/userLikeNote`,
      method: 'get',
      params: data
    })
  },
  // 笔记点赞与取消
  noteLike (dataform) {
    return request({
      url: `${ApiName}/noteLike`,
      method: 'post',
      data: dataform
    })
  },
  // 查询文章的点赞数（用户点赞或取消点赞后，更新笔记页展示的信息）
  getNoteLikeCount (noteId) {
    return request({
      url: `${ApiName}/getNoteLikeCount/${noteId}`,
      method: 'get'
    })
  },
  // 获取收藏了指定笔记的用户收藏夹列表
  getCfolderIds (param) {
    return request({
      url: `${ApiName2}/getCfolderIds`,
      method: 'get',
      params: param
    })
  },
  // 笔记收藏与取消
  noteCollect (dataform) {
    return request({
      url: `${ApiName2}/noteCollect`,
      method: 'post',
      data: dataform
    })
  },
  // 查询笔记的收藏数（用户收藏或取消收藏后，更新笔记页展示的信息）
  getNoteCollectCount (noteId) {
    return request({
      url: `${ApiName}/getNoteCollectCount/${noteId}`,
      method: 'get'
    })
  },
  // 更新笔记的收藏数
  updateNoteCollectionCount (dataform) {
    return request({
      url: `${ApiName}/updateNoteCollectionCount`,
      method: 'post',
      data: dataform
    })
  },
  // 分页查询收藏夹下的笔记
  getNoteInCfolderPaging (param) {
    return request({
      url: `${ApiName2}/getNoteInCfolderPaging`,
      method: 'get',
      params: param
    })
  },
  // 批量取消收藏
  cancelCollectNote (param, list) {
    return request({
      url: `${ApiName2}/cancelCollectNote`,
      method: 'delete',
      params: param,
      data: list
    })
  },
  // 发布评论
  postComment (dataform) {
    return request({
      url: `${ApiName3}/postComment`,
      method: 'post',
      data: dataform
    })
  },
  // 获取评论
  getComments (param) {
    return request({
      url: `${ApiName3}/getComments`,
      method: 'get',
      params: param
    })
  },
  // 获取评论下的所有回复
  getReplies (param) {
    return request({
      url: `${ApiName3}/getReplies`,
      method: 'get',
      params: param
    })
  },
  // 评论点赞与取消
  likeComment (dataform) {
    return request({
      url: `${ApiName3}/likeComment`,
      method: 'post',
      data: dataform
    })
  } // 笔记点赞与取消

}
