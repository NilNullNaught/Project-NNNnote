package cn.nilnullnaught.nnnnote.note.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

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
    void initializeNote(@Param("ID") String ID,
                        @Param("userID") String userID,
                        @Param("noteFolderId") String noteFolderId,
                        @Param("title") String title,
                        @Param("text") String text,
                        @Param("date") LocalDateTime date);
}
