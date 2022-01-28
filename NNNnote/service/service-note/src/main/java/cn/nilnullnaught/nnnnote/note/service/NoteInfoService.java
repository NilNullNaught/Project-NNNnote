package cn.nilnullnaught.nnnnote.note.service;

import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 笔记信息表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
public interface NoteInfoService extends IService<NoteInfo> {

    NoteInfo initializeNote(String id);
}
