import request from '@/utils/request'

const ApiName = '/note/note-info'
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
  }
}
