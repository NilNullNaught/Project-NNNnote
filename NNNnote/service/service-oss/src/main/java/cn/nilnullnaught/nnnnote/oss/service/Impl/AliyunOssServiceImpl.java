package cn.nilnullnaught.nnnnote.oss.service.Impl;

import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.oss.config.AliyunOssConfig;
import cn.nilnullnaught.nnnnote.oss.utils.AliyunOssUtils;
import cn.nilnullnaught.nnnnote.oss.service.AliyunOssService;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AliyunOssServiceImpl implements AliyunOssService {

    /**
     * 上传文件
     *
     * @param file 上传文件
     * @return 文件上传后的访问地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return AliyunOssUtils.uploadFile(file);
        }catch (Exception e){
            throw new MyCustomException(20001,"上传文件失败");
        }
    }


    /**
     * 暂存文件
     * @param file
     * @return
     */
    @Override
    public String uploadFileTemporary(MultipartFile file) {
        try {
            String path = new DateTime().toString("yyyy/MM/dd");
            path = "temporary/"+path;
            return AliyunOssUtils.uploadFile(file,path);
        }catch (Exception e){
            throw new MyCustomException(20001,"上传文件失败");
        }
    }

    /**
     *  改变文件存储位置，但是注意，实际上旧文件并没有被删除，而是在新的位置生成了副本
     *  用于将文件从暂存文件夹转移
     * @param tempUrl
     * @param newUrl
     * @param oldUrl 需要删除的 url，如果文件发生迭代（比如用户头像更新）则删除过去的文件
     */
    @Override
    public void alterFileLocation(String tempUrl,String newUrl,String oldUrl) {
        try{
            tempUrl = tempUrl.substring(tempUrl.lastIndexOf(".com/")+5);
            newUrl = newUrl.substring(newUrl.lastIndexOf(".com/")+5);
            AliyunOssUtils.copyFile(tempUrl,newUrl);
            if (!StringUtils.isEmpty(oldUrl) && oldUrl != "EmptyUrl"){
                AliyunOssUtils.deleteFile(oldUrl);
            }
        }catch (Exception e){
            throw new MyCustomException(20001,"修改文件位置失败");
        }
    }

    /**
     * 根据 URL　删除单个文件
     * @param url 被删除文件的 url
     */
    @Override
    public void deleteFile(String url) {
        try {
            AliyunOssUtils.deleteFile(url);
        }catch (Exception e){
            e.printStackTrace();
            throw new MyCustomException(20001,"删除文件失败");
        }
    }



}
