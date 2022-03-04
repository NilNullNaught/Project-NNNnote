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
import java.util.*;
import java.util.stream.Collectors;

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
     * @param nFolderId
     * @return
     */
    @Override
    @Transactional
    public String initializeNote(String userID, String nFolderId) {
        String noteID = IdWorker.get32UUID();
        noteMultiMapper.initializeNote(noteID, userID, nFolderId, "", "", LocalDateTime.now());

        if (baseMapper.selectById(noteID) != null) {
            this.updateNoteCountInNoteFolder(Arrays.asList(nFolderId));
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
        // 获取旧的笔记文件夹
        String oldFolder = baseMapper.selectById(saveNoteVo.getId()).getNoteFolderId();
        String newFolder = saveNoteVo.getNoteFolderId();

        NoteInfo noteInfo = new NoteInfo();
        noteInfo.setId(saveNoteVo.getId());
        noteInfo.setTitle(saveNoteVo.getTitle());
        noteInfo.setPreview(saveNoteVo.getPreview());
        noteInfo.setStatus(saveNoteVo.getStatus());
        noteInfo.setNoteFolderId(newFolder);
        baseMapper.updateById(noteInfo);
        // ->

        // 更新用户文件夹中的笔记数量, （前提条件，笔记的文件夹发生了变更）
        if (oldFolder != newFolder) {
            this.updateNoteCountInNoteFolder(Arrays.asList(oldFolder, newFolder));
        }


        // <- 更新 note_text
        NoteText noteText = new NoteText();
        noteText.setId(saveNoteVo.getId());
        noteText.setText(saveNoteVo.getText());
        noteTextMapper.updateById(noteText);
        // ->
    }

    /**
     * 自动保存笔记
     *
     * @param saveNoteVo
     */
    @Override
    @Transactional
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
        NoteInfo noteInfo = new NoteInfo();
        noteInfo.setId(saveNoteVo.getId());
        noteInfo.setTitle(saveNoteVo.getTitle());
        noteInfo.setPreview(saveNoteVo.getPreview());
        baseMapper.updateById(noteInfo);
        // ->

        // <- 更新 note_text
        NoteText noteText = new NoteText();
        noteText.setId(saveNoteVo.getId());
        noteText.setText(saveNoteVo.getText());
        noteTextMapper.updateById(noteText);
        // ->
    }

    /**
     * 分页查询草稿列表
     *
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> getDraftList(String userId, long page, long limit) {
        QueryWrapper<NoteInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("gmt_create");
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 0);

        Page<NoteInfo> noteInfoPage = new Page<>(page, limit);
        baseMapper.selectPage(noteInfoPage, queryWrapper);

        //把分页数据提取出来，放到map集合
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
     * 彻底删除草稿
     *
     * @param userId
     * @param idList
     */
    @Override
    @Transactional
    public void deleteDrafts(String userId, List<String> idList) {

        // <- 验证笔记 id
        QueryWrapper<NoteInfo> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);//确认用户
        qw.eq("status", 0);//草稿状态
        qw.eq("is_deleted", 0);//未被逻辑删除
        qw.in("id", idList);
        qw.select("id", "note_folder_id");
        List<NoteInfo> noteList = baseMapper.selectList(qw);
        // ->

        // 执行删除
        List<String> _idList = noteList.stream().map(NoteInfo::getId).collect(Collectors.toList());
        noteMultiMapper.deleteDrafts(_idList);

        //更新文件夹笔记数量
        List<String> noteFolderIdList = noteList.stream().map(NoteInfo::getNoteFolderId).collect(Collectors.toList());
        this.updateNoteCountInNoteFolder(noteFolderIdList);
    }

    /**
     * 分页查询被逻辑删除的笔记
     *
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> getLogicDeletedNoteList(String userId, long page, long limit) {

        Integer total = baseMapper.getLogicDeletedNoteCount(userId);

        Long offset = limit * (page - 1);
        List<NoteInfo> resultList = baseMapper.getLogicDeletedNoteListPaging(userId, "gmt_modified", true, limit, offset);


        //把分页数据提取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", resultList);
        map.put("total", total);

        return map;
    }

    /**
     * 还原被逻辑删除的笔记
     *
     * @param userId
     * @param token
     * @param idList
     */
    @Override
    @Transactional
    public void restoreDeletedNote(String userId, String token, List<String> idList) {
        // <- 1.验证 idList 的合法性
        var noteInfoList = baseMapper.getLogicDeletedNoteList(userId, idList);
        var _idList = noteInfoList.stream().map(NoteInfo::getId).collect(Collectors.toList());
        if (_idList.size() == 0) return;
        // ->

        // <- 2.验证文件夹是否被删除
        var noteFolderIdList = new ArrayList<>(
                noteInfoList.stream().map(NoteInfo::getNoteFolderId).collect(Collectors.toSet()));
        var r = userNfolderClient.getNoteFolderNameByFolderId(token, noteFolderIdList);
        var deletedFolderList = new ArrayList<String>();
        if (r.getCode() == 20000) {
            Map<String, String> responseData = (Map<String, String>) r.getData().get("data");
            if (!responseData.isEmpty()) {
                responseData.forEach((k, v) -> {
                    if (v == null) {
                        deletedFolderList.add(k);
                    }
                });
            }
        }
        // ->

        // <- 3.还原被删除的笔记
        baseMapper.restoreDeletedNote(_idList);
        // ->

        // <- 4.如果文件夹已经被删除，则还原到默认文件夹
        if (!deletedFolderList.isEmpty()) {
            var uw2 = new UpdateWrapper<NoteInfo>();
            uw2.set("note_folder_id", userId);
            uw2.in("note_folder_id", deletedFolderList);
            uw2.in("id", _idList);
            baseMapper.update(new NoteInfo(), uw2);
            noteFolderIdList.removeAll(deletedFolderList);
            if (!noteFolderIdList.contains(userId)) {
                noteFolderIdList.add(userId);
            }
        }
        // ->

        // <- 5.更新文件夹中笔记的数量
        this.updateNoteCountInNoteFolder(noteFolderIdList);
        // ->
    }

    /**
     * 删除回收站中的笔记
     * @param userId
     * @param idList
     */
    @Override
    @Transactional
    public void deleteDeletedNotes(String userId, List<String> idList) {
        // <- 1.验证 idList 的合法性
        var noteInfoList = baseMapper.getLogicDeletedNoteList(userId, idList);
        var _idList = noteInfoList.stream().map(NoteInfo::getId).collect(Collectors.toList());
        if (_idList.size() == 0) return;
        // ->

        // <- 2.完成删除
        noteMultiMapper.deleteDeletedNotes(_idList);
        // ->
    }

    @Override
    public Map<String, Object> getCountOfNoteInfo(String userId) {

        var qw1 =new QueryWrapper<NoteInfo>();
        qw1.eq("user_id",userId);
        var noteCount = baseMapper.selectCount(qw1);

        var qw2 = new QueryWrapper<NoteInfo>();
        qw2.eq("user_id",userId);
        qw2.eq("status",0);
        var draftCount = baseMapper.selectCount(qw2);

        var deletedCount = baseMapper.getCountOfDeletedNote(userId);

        var map = new HashMap<String,Object>();
        map.put("noteCount",noteCount);
        map.put("draftCount",draftCount);
        map.put("deletedCount",deletedCount);

        return map;
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
     *
     * @param userId
     * @param noteFolderId
     * @param page
     * @param limit
     * @param condition
     * @return
     */
    @Override
    public Map<String, Object> getNotes(String userId, String noteFolderId, long page, long limit, String condition) {

        QueryWrapper<NoteInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("gmt_create");
        queryWrapper.eq("user_id", userId);

        // 查询指定文件夹中的笔记
        if (!StringUtils.isEmpty(noteFolderId)) {
            queryWrapper.eq("note_folder_id", noteFolderId);
        }

        // 附加查询条件
        if (!StringUtils.isEmpty(condition)) {
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
     *
     * @param userId
     * @param idList
     */
    @Override
    public void deleteNotes(String userId, List<String> idList) {
        QueryWrapper<NoteInfo> qw1 = new QueryWrapper();
        qw1.eq("user_id", userId);
        qw1.in("id", idList);
        qw1.select("note_folder_id");
        List<NoteInfo> noteFolderList = baseMapper.selectList(qw1);

        UpdateWrapper<NoteInfo> qw2 = new UpdateWrapper();
        qw2.eq("user_id", userId);
        qw2.in("id", idList);
        baseMapper.delete(qw2);

        // <- 更新用户文件夹中的笔记数量
        List<String> noteFolderIdList = noteFolderList.stream().map(NoteInfo::getNoteFolderId).collect(Collectors.toList());
        this.updateNoteCountInNoteFolder(noteFolderIdList);
        // ->
    }

    /**
     * 【公共方法】 —— 更新用户文件夹中的笔记数量
     *
     * @param noteFolderIdList
     */
    private void updateNoteCountInNoteFolder(List<String> noteFolderIdList) {
        if (noteFolderIdList.isEmpty()) {
            return;
        }

        HashMap<String, Long> map = new HashMap<>();

        // 查询文件夹中笔记数量
        for (String noteFolderId : noteFolderIdList) {
            QueryWrapper<NoteInfo> qw = new QueryWrapper<NoteInfo>();
            qw.eq("note_folder_id", noteFolderId);
            qw.eq("is_deleted", 0);
            qw.select("id");
            Long count = baseMapper.selectCount(qw);
            map.put(noteFolderId, count);
        }

        R result = userNfolderClient.alterUserNfolderNoteCount(map);
        if (result.getCode() != 20000) {
            throw new MyCustomException(20001, "更新笔记数量失败");
        }
    }

}
