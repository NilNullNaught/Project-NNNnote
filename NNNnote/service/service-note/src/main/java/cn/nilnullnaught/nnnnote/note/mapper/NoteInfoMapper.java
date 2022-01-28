package cn.nilnullnaught.nnnnote.note.mapper;

import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 笔记信息表 Mapper 接口
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Mapper
public interface NoteInfoMapper extends BaseMapper<NoteInfo> {
    void initializeNote(@Param("ID") String ID,
                        @Param("userID") String userID,
                        @Param("title") String title,
                        @Param("text") String text,
                        @Param("date") LocalDateTime date);

}
