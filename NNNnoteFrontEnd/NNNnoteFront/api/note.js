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
  }
}
