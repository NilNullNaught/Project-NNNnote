package cn.nilnullnaught.nnnnote.oss.service;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

public interface AliyunOssService {

    String uploadFile(MultipartFile file);

    String uploadFileTemporary(MultipartFile file);

    void alterFileLocation(String tempUrl,String newUrl,String oldUrl);

    void deleteFile(String URL);


}
