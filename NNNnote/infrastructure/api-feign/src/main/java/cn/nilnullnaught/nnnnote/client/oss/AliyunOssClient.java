package cn.nilnullnaught.nnnnote.client.oss;

import cn.nilnullnaught.nnnnote.common.utils.R;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("service-oss")
public interface AliyunOssClient {

    @PostMapping("/uploadFile")
    public R uploadFile(MultipartFile file);

    @PostMapping("/deleteFile")
    public R deleteFile(@RequestHeader("url")String url);

    @PostMapping(value = "/oss/aliyun-oss/upDateFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R upDateFile(@RequestHeader("url")String url,@RequestPart("file")MultipartFile file);
}


