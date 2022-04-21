package cn.nilnullnaught.nnnnote.note.service.impl;

import cn.nilnullnaught.nnnnote.client.user.UserCfolderClient;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.note.mapper.NoteInfoMapper;
import cn.nilnullnaught.nnnnote.note.mapper.NoteUserCollectionMapper;
import cn.nilnullnaught.nnnnote.note.service.NoteUserCollectionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 笔记—收藏夹关系表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Service
public class NoteUserCollectionServiceImpl extends ServiceImpl<NoteUserCollectionMapper, NoteUserCollection> implements NoteUserCollectionService {

    @Autowired
    private UserCfolderClient userCfolderClient;

    @Autowired
    private NoteInfoMapper noteInfoMapper;

    /**
     * 笔记收藏与取消
     *
     * @param noteId
     * @param userId
     */
    @Override
    @Transactional
    public void noteCollect(String noteId, String userId, String cfolderId) {

        // region <- 指定的收藏夹中是否包含该笔记 ->
        var qw = new QueryWrapper<NoteUserCollection>();
        qw.eq("note_id", noteId);
        qw.eq("user_id", userId);
        qw.eq("collection_folder_id", cfolderId);
        var check = baseMapper.selectList(qw);
        // endregion


        if (check.isEmpty()) {
            // 指定收藏夹中没有该笔记，收藏笔记
            var noteUserCollection = new NoteUserCollection();
            noteUserCollection.setNoteId(noteId);
            noteUserCollection.setUserId(userId);
            noteUserCollection.setCollectionFolderId(cfolderId);
            baseMapper.insert(noteUserCollection);
        } else {
            // 指定收藏夹中存在该笔记，取消收藏
            baseMapper.delete(qw);
        }

        // 更新笔记收藏夹中的笔记数量
        var qw2 = new QueryWrapper<NoteUserCollection>();
        qw2.eq("collection_folder_id", cfolderId);
        var count = baseMapper.selectCount(qw2);
        userCfolderClient.updateCfolderNoteCount(count, cfolderId);
    }

    /**
     * 获取收藏了指定笔记的用户收藏夹列表
     *
     * @param userId
     * @param noteId
     * @return
     */
    @Override
    public List<String> getCfolderIds(String userId, String noteId) {

        var qw = new QueryWrapper<NoteUserCollection>();
        qw.eq("note_id", noteId);
        qw.eq("user_id", userId);
        var list = baseMapper.selectList(qw);

        var result = list.stream().map(item -> item.getCollectionFolderId()).collect(Collectors.toList());

        return result;
    }

    /**
     * 分页查询收藏夹下的笔记
     *
     * @param userId
     * @param cfolderId
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> getNoteInCfolderPaging(String userId, String cfolderId, Integer page, Integer limit) {

        // region <- 获取收藏夹指定下的笔记 Id ->

        var qw = new QueryWrapper<NoteUserCollection>();
        qw.eq("user_id", userId);
        qw.eq("collection_folder_id", cfolderId);

        var noteUserCollectionPage = new Page<NoteUserCollection>(page, limit);
        baseMapper.selectPage(noteUserCollectionPage, qw);

        var list = noteUserCollectionPage.getRecords();
        var noteIdList = list.stream().map(item -> item.getNoteId()).collect(Collectors.toList());
        if (noteIdList.isEmpty()) return null;
        // endregion

        // region <- 根据笔记 Id 获取 NoteInfo ->

        var qw2 = new QueryWrapper<NoteInfo>();

        qw2.in("id", noteIdList);

        var items = noteInfoMapper.selectList(qw2);

        //把分页数据提取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", items);
        map.put("current", noteUserCollectionPage.getCurrent());
        map.put("pages", noteUserCollectionPage.getPages());
        map.put("size", noteUserCollectionPage.getSize());
        map.put("total", noteUserCollectionPage.getTotal());
        map.put("hasNext", noteUserCollectionPage.hasNext());
        map.put("hasPrevious", noteUserCollectionPage.hasPrevious());

        return map;
        // endregion
    }

}
