import axios from 'axios'
import jsCookie from 'js-cookie'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:10001', // api的base_url
  timeout: 20000 // 请求超时时间
})

// 请求拦截器，如果用户已经登录，那么在每次请求的请求头上添加token信息
service.interceptors.request.use(
  (config) => {
    if (jsCookie.get('NNNnote_token')) {
      config.headers.token = jsCookie.get('NNNnote_token')
    }
    return config
  },
  (err) => {
    return Promise.reject(err)
  })

// 失败——无法获取 store
// import noteApi from '@/api/note'
// const intercepteURL = [
//   '/note/note-info/initializeNote',
//   '/note/note-info/restoreDeletedNote',
//   '/note/note-info/deleteNotes',
//   '/note/note-info/deleteDrafts',
//   '/note/note-info/deleteDeletedNotes']

// // 响应拦截器，拦截对笔记进行新增或删除的操作，查询笔记数量
// service.interceptors.response.use(
//   (response) => {
//     if (response.data.code === 20000) {
//       if (intercepteURL.includes(response.config.url)) {
//         noteApi.getCountOfNoteInfo().then((response) => {
//           if (response.data.code === 20000) {
//             $nuxt.$store.commit('userData/updateDataCount', response.data.data)
//           }
//         })
//       }
//     }
//     return response
//   },
//   (error) => {
//     return Promise.reject(error.response) // 返回接口返回的错误信息
//   })

export default service
