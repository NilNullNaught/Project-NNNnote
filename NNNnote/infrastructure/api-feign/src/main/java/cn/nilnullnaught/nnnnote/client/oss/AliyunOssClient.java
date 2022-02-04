package cn.nilnullnaught.nnnnote.client.oss;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.oss.vo.ResourceManagerVo;
import feign.Headers;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/oss/aliyun-oss/alterFileLocation")
    public R alterFileLocation(@RequestHeader("tempUrl")String tempUrl,@RequestHeader("newUrl")String newUrl,@RequestHeader("oldUrl")String oldUrl);

    @PostMapping("/oss/aliyun-oss/managerResource")
    public R manageResource(@RequestBody ResourceManagerVo resourceManagerVo);

    @GetMapping("/oss/aliyun-oss/getResourceByBelongId/{belongId}/{type}")
    public R getResourceByBelongId(@PathVariable("belongId") String belongId,@PathVariable("type") Integer type);
}


