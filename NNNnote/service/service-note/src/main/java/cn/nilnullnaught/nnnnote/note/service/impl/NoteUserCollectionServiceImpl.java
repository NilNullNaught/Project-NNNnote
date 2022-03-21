package cn.nilnullnaught.nnnnote.note.service.impl;

import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.note.mapper.NoteUserCollectionMapper;
import cn.nilnullnaught.nnnnote.note.service.NoteUserCollectionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * 笔记收藏与取消
     * @param noteId
     * @param userId
     */
    @Override
    public void noteCollect(String noteId, String userId,String cfolderId) {

        // region <- 指定的收藏夹中是否包含该笔记 ->
        var qw = new QueryWrapper<NoteUserCollection>();
        qw.eq("note_id",noteId);
        qw.eq("user_id",userId);
        qw.eq("collection_folder_id",cfolderId);
        var check= baseMapper.selectList(qw);
        // endregion


        if (check.isEmpty()){
            // 指定收藏夹中没有该笔记，收藏笔记
            var noteUserCollection = new NoteUserCollection();
            noteUserCollection.setNoteId(noteId);
            noteUserCollection.setUserId(userId);
            noteUserCollection.setCollectionFolderId(cfolderId);
            baseMapper.insert(noteUserCollection);
        }else {
            // 指定收藏夹中存在该笔记，取消收藏
            baseMapper.delete(qw);
        }

    }

    /**
     * 获取收藏了指定笔记的用户收藏夹列表
     * @param userId
     * @param noteId
     * @return
     */
    @Override
    public List<String> getCfolderIds(String userId, String noteId) {

        var qw = new QueryWrapper<NoteUserCollection>();
        qw.eq("note_id",noteId);
        qw.eq("user_id",userId);
        var list= baseMapper.selectList(qw);

        var result = list.stream().map(item -> item.getCollectionFolderId()).collect(Collectors.toList());

        return result;
    }

}
