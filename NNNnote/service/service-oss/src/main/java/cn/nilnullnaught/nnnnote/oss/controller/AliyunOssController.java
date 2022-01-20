package cn.nilnullnaught.nnnnote.oss.controller;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.oss.service.AliyunOssService;
import cn.nilnullnaught.nnnnote.entity.oss.vo.UrlVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss/aliyun-oss")
public class AliyunOssController {

    @Autowired
    private AliyunOssService aliyunOssService;

    @ApiOperation("上传文件")
    @PostMapping("/uploadFile")
    public R uploadFile(MultipartFile file) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        String url = aliyunOssService.uploadFile(file);
        return R.ok().data("url",url);
    }


    @ApiOperation("删除文件")
    @PostMapping("/deleteFile")
    public R deleteFile(@RequestBody UrlVo UrlVo) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        aliyunOssService.deleteFile(UrlVo.getUrl());
        return R.ok().message("删除成功");
    }

}
