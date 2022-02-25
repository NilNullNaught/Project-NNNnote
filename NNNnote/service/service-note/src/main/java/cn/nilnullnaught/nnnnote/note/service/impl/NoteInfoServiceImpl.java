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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;
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
     * @param userID
     * @param nFolderId
     * @return
     */
    @Override
    @Transactional
    public String initializeNote(String userID, String nFolderId) {
        String noteID = IdWorker.get32UUID();
        noteMultiMapper.initializeNote(noteID, userID, nFolderId, "", "", LocalDateTime.now());

        if (baseMapper.selectById(noteID) != null) {
            // 更新用户文件夹中的笔记数量
            QueryWrapper<NoteInfo> queryWrapper = new QueryWrapper<NoteInfo>();
            queryWrapper.eq("note_folder_id", nFolderId);
            Long count = baseMapper.selectCount(queryWrapper);
            HashMap<String, Long> map = new HashMap<>();
            map.put(nFolderId, count);
            R result = userNfolderClient.alterUserNfolderNoteCount(map);
            if (result.getCode() != 20000) {
                throw new MyCustomException(20001, "更新笔记数量失败");
            }

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

        // <- 更新图片信息
        // 如果不包含图片，直接跳过这一步
        if (saveNoteVo.getResourceUrlList() != null) {
            ResourceManagerVo resourceManagerVo = new ResourceManagerVo();
            resourceManagerVo.setBelongId(saveNoteVo.getId());
            resourceManagerVo.setResourceUrlList(saveNoteVo.getResourceUrlList());
            resourceManagerVo.setType(1);
            R r = aliyunOssClient.manageResource(resourceManagerVo);
            if (r.getCode() != 20000) {
                throw new MyCustomException(20001, r.getMessage());
            }
        }
        // ->

        // <- 更新 note_info
        NoteInfo noteInfo = baseMapper.selectById(saveNoteVo.getId());
        noteInfo.setTitle(saveNoteVo.getTitle());
        noteInfo.setStatus(saveNoteVo.getStatus());

        // 更新用户文件夹中的笔记数量
        if (noteInfo.getNoteFolderId() != saveNoteVo.getNoteFolderId()) {

            QueryWrapper<NoteInfo> queryWrapper1 = new QueryWrapper<NoteInfo>();
            queryWrapper1.eq("note_folder_id", noteInfo.getNoteFolderId());
            Long oldNoteFolderCount = baseMapper.selectCount(queryWrapper1);

            QueryWrapper<NoteInfo> queryWrapper2 = new QueryWrapper<NoteInfo>();
            queryWrapper2.eq("note_folder_id", saveNoteVo.getNoteFolderId());
            Long newNoteFolderCount = baseMapper.selectCount(queryWrapper2);

            HashMap<String, Long> map = new HashMap<>();
            map.put(noteInfo.getNoteFolderId(), oldNoteFolderCount - 1);
            map.put(saveNoteVo.getNoteFolderId(), newNoteFolderCount + 1);
            R result = userNfolderClient.alterUserNfolderNoteCount(map);
            if (result.getCode() != 20000) {
                throw new MyCustomException(20001, "更新失败");
            }
            noteInfo.setNoteFolderId(saveNoteVo.getNoteFolderId());
        }
        baseMapper.updateById(noteInfo);
        // ->

        // <- 更新 note_text
        UpdateWrapper noteTextUW = new UpdateWrapper<NoteText>();
        noteTextUW.eq("id", saveNoteVo.getId());
        noteTextUW.set("text", saveNoteVo.getText());
        noteTextMapper.update(new NoteText(), noteTextUW);
        // ->
    }

    /**
     * 自动保存笔记
     * @param saveNoteVo
     */
    @Override
    public void autoSaveNote(SaveNoteVo saveNoteVo) {
        // <- 更新图片信息，如果不包含图片，直接跳过这一步
        if (saveNoteVo.getResourceUrlList() != null) {
            ResourceManagerVo resourceManagerVo = new ResourceManagerVo();
            resourceManagerVo.setBelongId(saveNoteVo.getId());
            resourceManagerVo.setResourceUrlList(saveNoteVo.getResourceUrlList());
            resourceManagerVo.setType(1);
            R r = aliyunOssClient.manageResource(resourceManagerVo);
            if (r.getCode() != 20000) {
                throw new MyCustomException(20001, r.getMessage());
            }
        }
        // ->

        // <- 更新 note_info
        NoteInfo noteInfo = baseMapper.selectById(saveNoteVo.getId());
        noteInfo.setTitle(saveNoteVo.getTitle());
        baseMapper.updateById(noteInfo);
        // ->

        // <- 更新 note_text
        UpdateWrapper noteTextUW = new UpdateWrapper<NoteText>();
        noteTextUW.eq("id", saveNoteVo.getId());
        noteTextUW.set("text", saveNoteVo.getText());
        noteTextMapper.update(new NoteText(), noteTextUW);
        // ->
    }
    /**
     * 获取笔记信息（编辑）
     *
     * @param noteId
     * @return
     */
    @Override
    public Map getNoteInfoToEdit(String noteId) {
        NoteInfo noteInfo = baseMapper.selectById(noteId);
        NoteText noteText = noteTextMapper.selectById(noteId);
        R r = userNfolderClient.getUserNfolderList(noteInfo.getUserId());

        Map<String, Object> result = new HashMap();
        if (r.getCode() == 20000) {
            result.put("NfolderList", r.getData().get("data"));
        } else {
            throw new MyCustomException(20001, "获取失败");
        }

        result.put("noteInfo", noteInfo);
        result.put("noteText", noteText);
        return result;
    }

    /**
     * 获取笔记信息（阅读）
     *
     * @param noteId
     * @return
     */
    @Override
    public Map getNoteInfoToRead(String noteId) {
        NoteInfo noteInfo = baseMapper.selectById(noteId);
        NoteText noteText = noteTextMapper.selectById(noteId);
        Map<String, Object> result = new HashMap();
        result.put("noteInfo", noteInfo);
        result.put("noteText", noteText);
        return result;
    }


    //TODO 使用 elasticsearch 实现
    /**
     * 分页条件查询笔记
     * @param userId
     * @param noteFolderId
     * @param page
     * @param limit
     * @param condition
     * @return
     */
    @Override
    public Map<String, Object> getNotes(String userId,String noteFolderId, long page, long limit, String condition) {

        QueryWrapper<NoteInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");
        queryWrapper.eq("user_id", userId);

        // 查询指定文件夹中的笔记
        if (!StringUtils.isEmpty(noteFolderId)){
            queryWrapper.eq("note_folder_id",noteFolderId);
        }

        // 附加查询条件
        if (!StringUtils.isEmpty(condition)){
            queryWrapper.like("title", condition);
        }
        Page<NoteInfo> noteInfoPage = new Page<>(page, limit);
        baseMapper.selectPage(noteInfoPage, queryWrapper);

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", noteInfoPage.getRecords());
        map.put("current", noteInfoPage.getCurrent());
        map.put("pages", noteInfoPage.getPages());
        map.put("size", noteInfoPage.getSize());
        map.put("total", noteInfoPage.getTotal());
        map.put("hasNext", noteInfoPage.hasNext());
        map.put("hasPrevious", noteInfoPage.hasPrevious());
        return map;
    }

    /**
     * 批量删除
     * @param nFolderList
     */
    @Override
    public void deleteNotes(List<String> nFolderList) {

    }




}
