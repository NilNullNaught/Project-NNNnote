package cn.nilnullnaught.nnnnote.note.service;

import cn.nilnullnaught.nnnnote.entity.note.NoteComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 笔记评论表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
public interface NoteCommentService extends IService<NoteComment> {

    Map<String, Object> getComments(String noteId, long current, long limit, String sortCondition);

    void postComment(NoteComment noteComment);

    List<NoteComment> getReplies(String replyCommentId);

    Map<String,Object> likeComment(String userId, String commentId);
}
