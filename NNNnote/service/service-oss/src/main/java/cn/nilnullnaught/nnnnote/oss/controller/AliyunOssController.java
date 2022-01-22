package cn.nilnullnaught.nnnnote.oss.controller;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.oss.service.AliyunOssService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oss/aliyun-oss")
public class AliyunOssController {

    @Autowired
    private AliyunOssService aliyunOssService;

    @ApiOperation("上传文件")
    @PostMapping("/uploadFile")
    public R uploadFile(MultipartFile file) {

        //返回上传到oss的路径
        String url = aliyunOssService.uploadFile(file);
        return R.ok().data("url",url);
    }


    @ApiOperation("删除文件（注意：路径被封装在请求头的 url 属性中！）")
    @PostMapping("/deleteFile")
    public R deleteFile(@RequestHeader("url")String url) {
        aliyunOssService.deleteFile(url);
        return R.ok().message("删除成功");
    }

    @ApiOperation("更新文件")
    @PostMapping("/upDateFile")
    public R upDateFile(@RequestHeader("url")String url,@RequestPart("file")MultipartFile file) {

        //String url = aliyunOssService.updateFile(url,file);
        return R.ok().message(file.getOriginalFilename()).data("url",url);
    }
}
