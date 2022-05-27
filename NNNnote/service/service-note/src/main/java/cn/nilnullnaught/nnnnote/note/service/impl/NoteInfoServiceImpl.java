package cn.nilnullnaught.nnnnote.note.service.impl;

import cn.nilnullnaught.nnnnote.client.oss.AliyunOssClient;
import cn.nilnullnaught.nnnnote.client.user.UserInfoClient;
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
import cn.nilnullnaught.nnnnote.note.util.MyElasticsearchRestTemplate;
import cn.nilnullnaught.nnnnote.note.vo.SaveNoteVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    // region <- 成员变量 ->
    @Autowired
    private NoteMultiMapper noteMultiMapper;

    @Autowired
    private NoteTextMapper noteTextMapper;

    @Autowired
    private AliyunOssClient aliyunOssClient;

    @Autowired
    private UserInfoClient userInfoClient;

    @Autowired
    private UserNfolderClient userNfolderClient;

    @Autowired
    private MyElasticsearchRestTemplate myElasticsearchRestTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    // endregion


    /**
     * 初始化笔记，创建草稿
     *
     * @param userId
     * @param nFolderId
     * @return
     */
    @Override
    @Transactional
    public String initializeNote(String userId, String nFolderId) {
        String noteId = IdWorker.get32UUID();
        noteMultiMapper.initializeNote(noteId, userId, nFolderId, "", "", LocalDateTime.now());

        if (baseMapper.selectById(noteId) != null) {
            this.updateNoteCountInNoteFolder(Arrays.asList(nFolderId));
            return noteId;
        } else {
            throw new MyCustomException(20001, "创建失败");
        }

    }

    /**
     * 保存笔记
     *
     * @param saveNoteVo
     * @param userId
     */
    @Override
    @Transactional
    public void saveNote(SaveNoteVo saveNoteVo, String userId) {
        // region <- 用户校验 ->
        var realUserId = baseMapper.selectById(saveNoteVo.getId()).getUserId();

        if (!realUserId.equals(userId)) {
            throw new MyCustomException(20001, "违规操作");
        }
        // endregion


        // region <- 更新笔记内的图片信息（如果笔记不包含图片，直接跳过这一步） ->
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
        // endregion


        // region <- 更新 note_info 与相关信息 ->

        // 1.获取旧的笔记信息
        var oldNoteInfo = baseMapper.selectById(saveNoteVo.getId());

        // 2.提取旧文件夹 ID 与旧封面 URL
        var oldFolder = oldNoteInfo.getNoteFolderId();
        if (oldFolder == null) oldFolder = "";
        var oldCover = oldNoteInfo.getCover();
        if (oldCover == null) oldCover = "";

        // 3.提取新文件夹 ID 与旧封面 URL
        var newFolder = saveNoteVo.getNoteFolderId();
        if (newFolder == null) newFolder = "";
        var newCover = saveNoteVo.getCover();
        if (newCover == null) newCover = "";

        // 4.更新 noteInfo 信息
        NoteInfo noteInfo = new NoteInfo();
        noteInfo.setId(saveNoteVo.getId());
        noteInfo.setTitle(saveNoteVo.getTitle());
        noteInfo.setPreview(saveNoteVo.getPreview());
        noteInfo.setLength(saveNoteVo.getLength());
        noteInfo.setStatus(saveNoteVo.getStatus());

        noteInfo.setCover(newCover);
        noteInfo.setNoteFolderId(newFolder);

        baseMapper.updateById(noteInfo);

        // 5.更新用户文件夹中的笔记数量，必须在更新 noteInfo 信息之后进行
        if (!oldFolder.equals(newFolder)) {
            this.updateNoteCountInNoteFolder(Arrays.asList(oldFolder, newFolder));
        }

        // 6.更新封面状态
        if (!oldCover.equals(newCover)) {
            ResourceManagerVo vo = new ResourceManagerVo();
            vo.setType(2);
            vo.setBelongId(saveNoteVo.getId());
            vo.setResourceUrlList(Arrays.asList(newCover));
            aliyunOssClient.manageResource(vo);
        }
        // endregion


        // region <- 更新 note_text ->
        NoteText noteText = new NoteText();
        noteText.setId(saveNoteVo.getId());
        noteText.setText(saveNoteVo.getText());
        noteTextMapper.updateById(noteText);
        // endregion
    }

    /**
     * 自动保存笔记
     *
     * @param saveNoteVo
     */
    @Override
    @Transactional
    public void autoSaveNote(SaveNoteVo saveNoteVo, String userId) {

        // region <- 用户校验 ->
        var realUserId = baseMapper.selectById(saveNoteVo.getId()).getUserId();

        if (!realUserId.equals(userId)) {
            throw new MyCustomException(20001, "违规操作");
        }
        // endregion

        // region <- 更新图片信息，如果不包含图片，直接跳过这一步 ->
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
        // endregion

        // region <- 更新 note_info ->
        NoteInfo noteInfo = new NoteInfo();
        noteInfo.setId(saveNoteVo.getId());
        noteInfo.setTitle(saveNoteVo.getTitle());
        noteInfo.setPreview(saveNoteVo.getPreview());
        noteInfo.setLength(saveNoteVo.getLength());
        baseMapper.updateById(noteInfo);
        // endregion

        // region <- 更新 note_text ->
        NoteText noteText = new NoteText();
        noteText.setId(saveNoteVo.getId());
        noteText.setText(saveNoteVo.getText());
        noteTextMapper.updateById(noteText);
        // endregion
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

        // region <- 确认数据属于该用户 ->
        QueryWrapper<NoteInfo> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);//确认用户
        qw.eq("status", 0);//草稿状态
        qw.eq("is_deleted", 0);//未被逻辑删除
        qw.in("id", idList);
        qw.select("id", "note_folder_id");
        List<NoteInfo> noteList = baseMapper.selectList(qw);
        if (noteList == null || noteList.size() ==0) throw new MyCustomException(20001, "删除失败");
        // endregion

        // region <- 执行删除 ->
        List<String> _idList = noteList.stream().map(NoteInfo::getId).collect(Collectors.toList());
        noteMultiMapper.deleteDrafts(_idList);
        // endregion


        // region <- 更新文件夹笔记数量 ->
        List<String> noteFolderIdList = noteList.stream().map(NoteInfo::getNoteFolderId).collect(Collectors.toList());
        this.updateNoteCountInNoteFolder(noteFolderIdList);
        // endregion
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

        // region <- 确认数据属于该用户 ->
        var noteInfoList = baseMapper.getLogicDeletedNoteList(userId, idList);
        var _idList = noteInfoList.stream().map(NoteInfo::getId).collect(Collectors.toList());
        if (_idList == null || _idList.size() ==0) throw new MyCustomException(20001, "删除失败");
        // endregion

        // region <- 验证文件夹是否被删除 ->
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
        // endregion

        // region <- 还原被删除的笔记 ->

        baseMapper.restoreDeletedNote(_idList);

        //如果文件夹已经被删除，则还原到默认文件夹
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
        // endregion

        // region <- 更新文件夹中笔记的数量 ->
        this.updateNoteCountInNoteFolder(noteFolderIdList);
        // endregion
    }

    /**
     * 删除回收站中的笔记
     *
     * @param userId
     * @param idList
     */
    @Override
    @Transactional
    public void deleteDeletedNotes(String userId, List<String> idList) {

        // region <- 确认数据属于该用户 ->
        var noteInfoList = baseMapper.getLogicDeletedNoteList(userId, idList);
        var _idList = noteInfoList.stream().map(NoteInfo::getId).collect(Collectors.toList());
        if (_idList == null || _idList.size() ==0) throw new MyCustomException(20001, "删除失败");
        // endregion

        // region <- 完成删除 ->
        noteMultiMapper.deleteDeletedNotes(_idList);
        // endregion
    }

    /**
     * 删除回收站中已经保存超过时间的笔记
     */
    @Override
    @Transactional
    public void deleteDeletedNotesScheduledTask(){
        // region <- 查询符合条件的笔记ID ->
        var time = LocalDateTime.now();
        time = time.minusDays(7);

        var idList = baseMapper.getDeletedNoteIdScheduledTask(time);
        // endregion

        // region <- 删除笔记 ->
        noteMultiMapper.deleteDeletedNotes(idList);
        // endregion
    }

    @Override
    public Map<String, Object> getCountOfNoteInfo(String userId) {

        var qw1 = new QueryWrapper<NoteInfo>();
        qw1.eq("user_id", userId);
        var noteCount = baseMapper.selectCount(qw1);

        var qw2 = new QueryWrapper<NoteInfo>();
        qw2.eq("user_id", userId);
        qw2.eq("status", 0);
        var draftCount = baseMapper.selectCount(qw2);

        var deletedCount = baseMapper.getCountOfDeletedNote(userId);

        var map = new HashMap<String, Object>();
        map.put("noteCount", noteCount);
        map.put("draftCount", draftCount);
        map.put("deletedCount", deletedCount);

        return map;
    }

    /**
     * 分页搜索已公开的笔记
     *
     * @param criteria
     * @param sortField
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> searchNoteList(String criteria, String sortField, Integer page, Integer limit) {
        try {
            // region <- 通过 ElasticSearch 搜索笔记 ->
            if (criteria == null) criteria = "";
            if (sortField == null) sortField = "";
            // noteList() 返回的是一个由 Total 和 List 组成的 HashMap
            var result = myElasticsearchRestTemplate.noteList(criteria, sortField, page, limit);
            // endregion

            // region <- 获取笔记对应用户的头像和昵称 ->
            var list = (List<NoteInfo>) result.get("data");
            var idList = list.stream().map(object -> object.getUserId()).collect(Collectors.toSet());
            var response = userInfoClient.getUserAvatarAndNickNameByIdList(idList);
            var avatarAndNickname = response.getData().get("data");
            result.put("avatarAndNickname", avatarAndNickname);
            // endregion

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyCustomException(20001, "搜索失败");
        }
    }

    /**
     * 笔记点赞与取消
     * @param userId
     * @param noteId
     */
    @Override
    public void noteLike(String userId, String noteId) {
        // region <- 判断 redis 中是否存在 noteId 对应的键，如果不存在则创建 ->
        var keyExist= redisTemplate.hasKey(noteId);

        if (keyExist){
            Boolean exist = redisTemplate.boundSetOps(noteId).isMember(userId);
        }else{
            var setKey = redisTemplate.boundSetOps("setKey");
            redisTemplate.expire(noteId,1, TimeUnit.DAYS);
        }
        // endregion

        // region <- 判断用户是否已经对该笔记点过赞。并执行相应操作 ->
        Boolean exist = redisTemplate.boundSetOps(noteId).isMember(userId);

        var uw = new UpdateWrapper<NoteInfo>();
        uw.eq("id",noteId);
        if (exist){
            // 用户已经点过赞，取消赞
            uw.setSql("likes = likes-1");
            baseMapper.update(uw.getEntity(), uw);
            redisTemplate.boundSetOps(noteId).remove(userId);
        }else {
            // 用户没有点过赞，赞数加一
            uw.setSql("likes = likes+1");
            baseMapper.update(uw.getEntity(), uw);
            redisTemplate.boundSetOps(noteId).add(userId);

        }
        // endregion

    }

    /**
     * 判断用户有没有对该笔记进行过点赞
     * @param userId
     * @param noteId
     */
    @Override
    public Boolean userLikeNote(String userId, String noteId) {
        return redisTemplate.boundSetOps(noteId).isMember(userId);
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
