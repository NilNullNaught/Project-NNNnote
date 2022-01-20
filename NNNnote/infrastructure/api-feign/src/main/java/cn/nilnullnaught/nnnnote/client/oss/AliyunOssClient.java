package cn.nilnullnaught.nnnnote.client.oss;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.oss.vo.UrlVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("service-oss")
public interface AliyunOssClient {

    @PostMapping("/uploadFile")
    public R uploadFile(MultipartFile file);

    @PostMapping("/deleteFile")
    public R deleteFile(@RequestBody UrlVo UrlVo);
}
