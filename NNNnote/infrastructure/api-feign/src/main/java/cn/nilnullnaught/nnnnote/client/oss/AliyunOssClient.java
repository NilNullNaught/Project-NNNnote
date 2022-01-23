package cn.nilnullnaught.nnnnote.client.oss;

import cn.nilnullnaught.nnnnote.common.utils.R;
import feign.Headers;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("service-oss")
public interface AliyunOssClient {


    /**
     * 该方法仅作为使用 OpenFeign 传输文件的示例，不要使用该方法传输文件
     * @param url
     * @param file
     * @return
     */
    @Deprecated
    @PostMapping(value = "/oss/aliyun-oss/upDateFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R upDateFile(@RequestHeader("url")String url,@RequestPart("file")MultipartFile file);

    @ApiOperation("修改文件存储位置")
    @PostMapping("/oss/aliyun-oss/alterFileLocation")
    public R alterFileLocation(@RequestHeader("tempUrl")String tempUrl,@RequestHeader("newUrl")String newUrl,@RequestHeader("oldUrl")String oldUrl);
}


