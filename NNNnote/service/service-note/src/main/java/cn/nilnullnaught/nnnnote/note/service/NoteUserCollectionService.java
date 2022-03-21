package cn.nilnullnaught.nnnnote.note.service;

import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 笔记—收藏夹关系表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
public interface NoteUserCollectionService extends IService<NoteUserCollection> {

    void noteCollect(String noteId, String userId,String cfolderId);

    List<String> getCfolderIds(String userId, String noteId);

}
