package cn.nilnullnaught.nnnnote.note.service.impl;

import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.note.mapper.NoteUserCollectionMapper;
import cn.nilnullnaught.nnnnote.note.service.NoteUserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    public void noteCollect(String noteId, String userId) {

    }
}
