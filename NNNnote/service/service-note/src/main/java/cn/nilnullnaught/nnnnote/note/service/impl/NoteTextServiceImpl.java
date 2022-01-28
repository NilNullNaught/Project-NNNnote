package cn.nilnullnaught.nnnnote.note.service.impl;

import cn.nilnullnaught.nnnnote.entity.note.NoteText;
import cn.nilnullnaught.nnnnote.note.mapper.NoteTextMapper;
import cn.nilnullnaught.nnnnote.note.service.NoteTextService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 笔记正文表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Service
public class NoteTextServiceImpl extends ServiceImpl<NoteTextMapper, NoteText> implements NoteTextService {

}
