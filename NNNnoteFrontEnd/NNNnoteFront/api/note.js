import request from '@/utils/request'

const ApiName = '/note/note-info'
export default {
  // 笔记初始化
  initializeNote () {
    return request({
      url: `${ApiName}/initializeNote`,
      method: 'post'
    })
  },
  // 获取笔记信息
  getNoteInfo (noteId) {
    return request({
      url: `${ApiName}/getNoteInfo/${noteId}`,
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
  }
}
