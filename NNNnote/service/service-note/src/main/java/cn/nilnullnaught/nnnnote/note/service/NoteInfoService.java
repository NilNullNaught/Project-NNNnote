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

    String initializeNote(String userID);

    void saveNote(SaveNoteVo saveNoteVo);

    Map noteInfo(String noteId);
}
