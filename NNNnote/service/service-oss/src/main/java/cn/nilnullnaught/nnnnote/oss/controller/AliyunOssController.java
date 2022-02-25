package cn.nilnullnaught.nnnnote.oss.controller;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.oss.AliyunOssResource;
import cn.nilnullnaught.nnnnote.entity.oss.vo.ResourceManagerVo;
import cn.nilnullnaught.nnnnote.oss.service.AliyunOssService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @ApiOperation("文件状态管理")
    @PostMapping("/managerResource")
    public R manageResource(@RequestBody ResourceManagerVo resourceManagerVo){
        aliyunOssService.manageResource(resourceManagerVo);
        return R.ok();
    }

    @ApiOperation(value = "获取该使用者正在使用的所有文件URL",notes = "使用者，可以是用户（文件此时是用户的头像），也可以是笔记（文件此时是笔记中的所有图片）")
    @GetMapping("/getResourceByBelongId/{belongId}/{type}")
    public R getResourceByBelongId(@PathVariable("belongId") String belongId,@PathVariable("type") Integer type){

        QueryWrapper<AliyunOssResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("belong_id",belongId);
        queryWrapper.eq("type",type);
        queryWrapper.eq("in_use",true);
        queryWrapper.select("src");

        List<String> list =new ArrayList<>();

        for(AliyunOssResource it : aliyunOssService.list(queryWrapper)){
            list.add(it.getSrc());
        }
        return R.ok().data("data",list);
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
