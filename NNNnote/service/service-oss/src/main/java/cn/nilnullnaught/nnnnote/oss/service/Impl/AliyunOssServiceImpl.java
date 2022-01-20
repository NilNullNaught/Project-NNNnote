package cn.nilnullnaught.nnnnote.oss.service.Impl;

import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.oss.utils.AliyunOssUtils;
import cn.nilnullnaught.nnnnote.oss.service.AliyunOssService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AliyunOssServiceImpl implements AliyunOssService {

    /**
     * 上传文件
     *
     * @param file 上传文件
     * @return 文件上传后的访问地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return AliyunOssUtils.uploadFile(file);
        }catch (Exception e){
            throw new MyCustomException(20001,"上传文件失败");
        }
    }

    /**
     * 根据 URL　删除单个文件
     * @param url 被删除文件的 url
     */
    @Override
    public void deleteFile(String url) {
        try {
            AliyunOssUtils.deleteFile(url);
        }catch (Exception e){
            e.printStackTrace();
            throw new MyCustomException(20001,"删除文件失败");
        }
    }

}
