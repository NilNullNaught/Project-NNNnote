package cn.nilnullnaught.nnnnote.note.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NoteMultiMapper {

    /**
     * 初始化笔记
     * 向 note_info 和 note_text 中插入数据
     * @param ID
     * @param userID
     * @param title
     * @param text
     * @param date
     */
    int initializeNote(@Param("ID") String ID,
                        @Param("userID") String userID,
                        @Param("noteFolderId") String noteFolderId,
                        @Param("title") String title,
                        @Param("text") String text,
                        @Param("date") LocalDateTime date);

    /**
     * 删除草稿
     */
    void deleteDrafts(@Param("userID") String userID,
                      @Param("idList") List<String> ID);

}
