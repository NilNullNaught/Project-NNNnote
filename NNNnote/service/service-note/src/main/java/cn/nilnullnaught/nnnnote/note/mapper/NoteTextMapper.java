package cn.nilnullnaught.nnnnote.note.mapper;

import cn.nilnullnaught.nnnnote.entity.note.NoteText;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 笔记正文表 Mapper 接口
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Mapper
public interface NoteTextMapper extends BaseMapper<NoteText> {

}
