package cn.nilnullnaught.nnnnote.oss.service.Impl;

import cn.nilnullnaught.nnnnote.entity.oss.AliyunOssResource;
import cn.nilnullnaught.nnnnote.entity.oss.vo.ResourceManagerVo;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.oss.mapper.AliyunOssResourceMapper;
import cn.nilnullnaught.nnnnote.oss.utils.AliyunOssUtils;
import cn.nilnullnaught.nnnnote.oss.service.AliyunOssService;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class AliyunOssServiceImpl extends ServiceImpl<AliyunOssResourceMapper, AliyunOssResource> implements AliyunOssService {

    /**
     * 上传文件
     *
     * @param file 上传文件
     * @return 文件上传后的访问地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String uuid = IdWorker.get32UUID();
            String src = AliyunOssUtils.uploadFile(uuid, file);
            AliyunOssResource resource = new AliyunOssResource();
            resource.setId(uuid);
            resource.setSrc(src);
            baseMapper.insert(resource);
            return src;
        } catch (Exception e) {
            throw new MyCustomException(20001, "上传文件失败");
        }
    }


    /**
     * 暂存文件
     *
     * @param file
     * @return
     */
    @Override
    public String uploadFileTemporary(MultipartFile file) {
        try {
            String path = new DateTime().toString("yyyy/MM/dd");
            path = "temporary/" + path;
            return AliyunOssUtils.uploadFile(file, path);
        } catch (Exception e) {
            throw new MyCustomException(20001, "上传文件失败");
        }
    }

    /**
     * 改变文件存储位置，但是注意，实际上旧文件并没有被删除，而是在新的位置生成了副本
     * 用于将文件从暂存文件夹转移
     *
     * @param tempUrl
     * @param newUrl
     * @param oldUrl  需要删除的 url，如果文件发生迭代（比如用户头像更新）则删除过去的文件
     */
    @Override
    public void alterFileLocation(String tempUrl, String newUrl, String oldUrl) {
        try {
            tempUrl = tempUrl.substring(tempUrl.lastIndexOf(".com/") + 5);
            newUrl = newUrl.substring(newUrl.lastIndexOf(".com/") + 5);
            AliyunOssUtils.copyFile(tempUrl, newUrl);
            if (!StringUtils.isEmpty(oldUrl) && oldUrl != "EmptyUrl") {
                AliyunOssUtils.deleteFile(oldUrl);
            }
        } catch (Exception e) {
            throw new MyCustomException(20001, "修改文件位置失败");
        }
    }

    /**
     * 根据 URL　删除单个文件
     *
     * @param url 被删除文件的 url
     */
    @Override
    public void deleteFile(String url) {
        try {
            AliyunOssUtils.deleteFile(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyCustomException(20001, "删除文件失败");
        }
    }

    /**
     * 管理文件状态
     *
     * @param resourceManagerVo
     */
    @Override
    @Transactional
    public void manageResource(ResourceManagerVo resourceManagerVo) {

        Integer type = resourceManagerVo.getType();
        String BelongId = resourceManagerVo.getBelongId();
        List ResourceUrl = resourceManagerVo.getResourceUrlList();

        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(BelongId) || ResourceUrl.isEmpty()) {
            throw new MyCustomException(20001, "请输入完整的文件管理信息");
        }

        UpdateWrapper<AliyunOssResource> updateWrapperI = new UpdateWrapper<>();
        updateWrapperI.eq("type", type);
        updateWrapperI.eq("belong_id", BelongId);
        updateWrapperI.set("in_use", false);
        baseMapper.update(new AliyunOssResource(), updateWrapperI);


        UpdateWrapper<AliyunOssResource> updateWrapperII = new UpdateWrapper<>();

        for (String url : resourceManagerVo.getResourceUrlList()) {
            //获取 32 位 uuid
            String uuid = url.substring(url.lastIndexOf(".") - 32, url.lastIndexOf("."));
            updateWrapperII.eq("id", uuid).or();
        }

        updateWrapperII.set("type", type);
        updateWrapperII.set("belong_id", BelongId);
        updateWrapperII.set("in_use", true);
        int result = baseMapper.update(new AliyunOssResource(), updateWrapperII);
        if (result != ResourceUrl.size()) {
            throw new MyCustomException(20001, "文件状态更新失败");
        }

    }


}
