import axios from 'axios'
import jsCookie from 'js-cookie'
// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:10001', // api的base_url
  timeout: 20000 // 请求超时时间
})

// 如果用户已经登录，那么在每次请求的请求头上添加token信息
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

export default service
