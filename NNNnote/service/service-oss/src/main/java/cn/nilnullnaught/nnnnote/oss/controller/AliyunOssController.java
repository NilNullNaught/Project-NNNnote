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

    @ApiOperation("暂存文件")
    @PostMapping("/uploadFileTemporary")
    public R uploadFileTemporary(MultipartFile file){
        //返回上传到oss的路径
        String url = aliyunOssService.uploadFileTemporary(file);
        return R.ok().data("url",url);
    }

    @ApiOperation("修改文件存储位置")
    @PostMapping("/alterFileLocation")
    public R alterFileLocation(@RequestHeader("tempUrl")String tempUrl,@RequestHeader("newUrl")String newUrl,@RequestHeader("oldUrl")String oldUrl){
        //返回上传到oss的路径
        aliyunOssService.alterFileLocation(tempUrl,newUrl,oldUrl);
        return R.ok();
    }

    @ApiOperation("删除单个文件（注意：路径被封装在请求头的 url 属性中！）")
    @PostMapping("/deleteFile")
    public R deleteFile(@RequestHeader("url")String url) {
        aliyunOssService.deleteFile(url);
        return R.ok().message("删除成功");
    }

}
