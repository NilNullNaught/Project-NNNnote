package cn.nilnullnaught.nnnnote.oss.utils;

import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.oss.config.AliyunOssConfig;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AliyunOssUtils {

    /**
     * 上传文件
     * @param file 需要上传的文件
     * @return 文件访问路径
     */
    public static String uploadFile(MultipartFile file) throws IOException {
        OSS ossClient = null;

        try (InputStream inputStream = file.getInputStream()) {// 获取上传文件输入流

            // 创建OSS实例。
            ossClient = new OSSClientBuilder().build(
                    AliyunOssConfig.END_POINT,
                    AliyunOssConfig.ACCESS_KEY_ID,
                    AliyunOssConfig.ACCESS_KEY_SECRET);

            //生成新的随机 FileName，防止文件名相同导致覆盖
            String fileName = getRandomFileName(file);

            // 把文件按照日期进行分类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath + "/" + fileName;

            //调用 OSS 方法实现上传
            ossClient.putObject(AliyunOssConfig.BUCKET_NAME, fileName, inputStream);

            //把上传之后文件路径返回,需要手动拼接
            //需要把上传到阿里云oss路径手动拼接出来
            return "https://" + AliyunOssConfig.BUCKET_NAME + "." + AliyunOssConfig.END_POINT + "/" + fileName;
        }finally {
            if (ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除文件
     * @param url 需要删除的文件地址
     */
    public static void deleteFile(String url) throws IOException{
            // 创建OSS实例
            OSS ossClient = new OSSClientBuilder().build(
                    AliyunOssConfig.END_POINT,
                    AliyunOssConfig.ACCESS_KEY_ID,
                    AliyunOssConfig.ACCESS_KEY_SECRET);

            //从完整的 url 中截取文件路径
            url = url.substring(url.lastIndexOf(".com/")+5);
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(AliyunOssConfig.BUCKET_NAME,url);
    }


    //生成随机文件名
    public static String getRandomFileName(MultipartFile file) {
        //修改文件名，防止文件重复
        String rawFileName = file.getOriginalFilename();
        if (rawFileName == null){
            throw new MyCustomException(20001,"文件名为空");
        }else {
            // 获取文件类型
            String fileType = rawFileName.substring(rawFileName.lastIndexOf("."));
            // 生成一个 UUID
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 生成新的文件名
            rawFileName = uuid + fileType;
            return rawFileName;
        }
    }
}
