package cn.nilnullnaught.nnnnote.note.mapper;

import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 笔记—收藏夹关系表 Mapper 接口
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Mapper
public interface NoteUserCollectionMapper extends BaseMapper<NoteUserCollection> {

}
