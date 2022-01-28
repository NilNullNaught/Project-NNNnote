package cn.nilnullnaught.nnnnote.note.mapper;

import cn.nilnullnaught.nnnnote.entity.note.NoteComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 笔记评论表 Mapper 接口
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Mapper
public interface NoteCommentMapper extends BaseMapper<NoteComment> {

}
