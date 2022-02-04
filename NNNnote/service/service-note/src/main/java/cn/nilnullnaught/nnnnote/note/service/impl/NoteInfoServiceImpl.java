package cn.nilnullnaught.nnnnote.note.service.impl;

import cn.nilnullnaught.nnnnote.client.oss.AliyunOssClient;
import cn.nilnullnaught.nnnnote.client.user.UserNfolderClient;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.note.NoteText;
import cn.nilnullnaught.nnnnote.entity.oss.vo.ResourceManagerVo;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.note.mapper.NoteInfoMapper;
import cn.nilnullnaught.nnnnote.note.mapper.NoteMultiMapper;
import cn.nilnullnaught.nnnnote.note.mapper.NoteTextMapper;
import cn.nilnullnaught.nnnnote.note.service.NoteInfoService;
import cn.nilnullnaught.nnnnote.note.vo.SaveNoteVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 笔记信息表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Service
public class NoteInfoServiceImpl extends ServiceImpl<NoteInfoMapper, NoteInfo> implements NoteInfoService {

    @Autowired
    private NoteMultiMapper noteMultiMapper;

    @Autowired
    private NoteTextMapper noteTextMapper;

    @Autowired
    private AliyunOssClient aliyunOssClient;

    @Autowired
    private UserNfolderClient userNfolderClient;

    /**
     * 初始化笔记，创建草稿
     *
     * @param userID
     * @return
     */
    @Override
    @Transactional
    public String initializeNote(String userID) {
        String noteID = IdWorker.get32UUID();
        noteMultiMapper.initializeNote(noteID, userID, "", "", LocalDateTime.now());
        if (baseMapper.selectById(noteID) != null) {
            return noteID;
        } else {
            throw new MyCustomException(20001, "创建失败");
        }

    }

    /**
     * 保存笔记
     *
     * @param saveNoteVo
     */
    @Override
    @Transactional
    public void saveNote(SaveNoteVo saveNoteVo) {


        //如果不包含图片，直接跳过
        if (!saveNoteVo.getResourceUrlList().isEmpty()) {
            ResourceManagerVo resourceManagerVo = new ResourceManagerVo();
            resourceManagerVo.setBelongId(saveNoteVo.getId());
            resourceManagerVo.setResourceUrlList(saveNoteVo.getResourceUrlList());
            resourceManagerVo.setType(1);
            R r = aliyunOssClient.manageResource(resourceManagerVo);
            if (r.getCode() != 20000) {
                throw new MyCustomException(20001, r.getMessage());
            }
        }

        UpdateWrapper noteInfoUW = new UpdateWrapper<NoteInfo>();
        noteInfoUW.eq("id", saveNoteVo.getId());
        noteInfoUW.set("title", saveNoteVo.getTitle());
        noteInfoUW.set("status", saveNoteVo.getStatus());

        UpdateWrapper noteTextUW = new UpdateWrapper<NoteText>();
        noteTextUW.eq("id", saveNoteVo.getId());
        noteTextUW.set("text", saveNoteVo.getText());

        baseMapper.update(new NoteInfo(), noteInfoUW);
        noteTextMapper.update(new NoteText(), noteTextUW);
    }

    /**
     * 获取笔记信息
     *
     * @param noteId
     * @return
     */
    @Override
    public Map noteInfo(String noteId) {
        NoteInfo noteInfo = baseMapper.selectById(noteId);
        NoteText noteText = noteTextMapper.selectById(noteId);
        R r = userNfolderClient.getUserNfolderList(noteInfo.getUserId());

        Map<String, Object> result = new HashMap();
        if (r.getCode() == 20000){
            result.put("NfolderList",r.getData().get("data"));
        }else {
            throw new MyCustomException(20001,"获取失败");
        }

        result.put("noteInfo", noteInfo);
        result.put("noteText", noteText);
        return result;
    }
}
