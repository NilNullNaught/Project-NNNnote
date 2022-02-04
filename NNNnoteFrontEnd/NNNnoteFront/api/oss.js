import request from '@/utils/request'

const ApiName = '/oss/aliyun-oss'
export default {

  // 上传文件
  uploadFile (file) {
    return request({
      url: `${ApiName}/uploadFile`,
      method: 'post',
      data: file,
      headers: {
        'content-type': 'multipart/form-data; boundary=<calculated when request is sent>'
      }
    })
  },
  // 文件状态更新
  manageResource (file) {
    return request({
      url: `${ApiName}/managerResource`,
      method: 'post',
      data: file
    })
  },
  // 获取该 ID 正在使用的所有指定类型的文件
  getResourceByBelongId (ID, type) {
    return request({
      url: `${ApiName}/getResourceByBelongId/${ID}/${type}`,
      method: 'get'
    })
  },
  // 上传文件到临时目录
  uploadFileTemporary (file) {
    return request({
      url: `${ApiName}/uploadFileTemporary`,
      method: 'post',
      data: file,
      headers: {
        'content-type': 'multipart/form-data; boundary=<calculated when request is sent>'
      }
    })
  }

}
