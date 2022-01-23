import request from '@/utils/request'

const ApiName = '/oss/aliyun-oss'
export default {
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
