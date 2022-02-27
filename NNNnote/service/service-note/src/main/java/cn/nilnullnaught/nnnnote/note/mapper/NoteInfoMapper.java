package cn.nilnullnaught.nnnnote.note.mapper;

import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 查询被逻辑删除的笔记总数
     * @param userID
     * @return
     */
    Integer getLogicDeletedNoteCount(@Param("userID") String userID);

    /**
     * 分页查询被逻辑删除的笔记
     * @param userID
     * @param orderByColumn
     * @param isAsc
     * @param limit
     * @param offset
     * @return
     */
    List<NoteInfo> getLogicDeletedNoteList(@Param("userID") String userID,
                                           @Param("orderByColumn") String orderByColumn,
                                           @Param("isAsc") Boolean isAsc,
                                           @Param("limit") Long limit,
                                           @Param("offset") Long offset);
}
