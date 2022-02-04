package cn.nilnullnaught.nnnnote.oss.service;

import cn.nilnullnaught.nnnnote.entity.oss.AliyunOssResource;
import cn.nilnullnaught.nnnnote.entity.oss.vo.ResourceManagerVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

public interface AliyunOssService extends IService<AliyunOssResource> {

    String uploadFile(MultipartFile file);

    String uploadFileTemporary(MultipartFile file);

    void alterFileLocation(String tempUrl,String newUrl,String oldUrl);

    void deleteFile(String URL);

    void manageResource(ResourceManagerVo resourceManagerVo);
}
