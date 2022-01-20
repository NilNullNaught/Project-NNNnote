package cn.nilnullnaught.nnnnote.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface AliyunOssService {

    String uploadFile(MultipartFile file);

    void deleteFile(String URL);


}
