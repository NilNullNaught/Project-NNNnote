package cn.nilnullnaught.nnnnote.note.service;

import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.note.vo.SaveNoteVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 笔记信息表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
public interface NoteInfoService extends IService<NoteInfo> {

    String initializeNote(String userId,String nFolderId);

    void saveNote(SaveNoteVo saveNoteVo,String userId);

    Map getNoteInfoToEdit(String noteId);

    Map getNoteInfoToRead(String noteId);

    Map<String, Object> getNotes(String userId,String noteFolderId, long page, long limit, String condition);

    void deleteNotes(String userId,List<String> idList);

    void autoSaveNote(SaveNoteVo saveNoteVo,String userId);


    Map<String, Object> getDraftList(String userId, long page, long limit);

    void deleteDrafts(String userId,List<String> idList);

    Map<String, Object> getLogicDeletedNoteList(String userId, long page, long limit);

    void restoreDeletedNote(String userId,String token, List<String> idList);

    void deleteDeletedNotes(String userId, List<String> idList);

    void deleteDeletedNotesScheduledTask();

    Map<String, Object> getCountOfNoteInfo(String userId);

    Map<String,Object> searchNoteList(String criteria, String sortField, Integer page, Integer limit);

    void noteLike(String userId, String noteId);

    Boolean userLikeNote(String userId, String noteId);

    Map<String, Object> getPublicNotes(String userId,String criteria, String sortField,Integer page, Integer limit);
}
