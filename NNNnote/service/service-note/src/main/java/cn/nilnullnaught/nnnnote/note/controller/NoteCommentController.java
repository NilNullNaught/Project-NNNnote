package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteComment;
import cn.nilnullnaught.nnnnote.note.service.NoteCommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 笔记评论表 前端控制器
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@RestController
@RequestMapping("/note/note-comment")
public class NoteCommentController {

    @Autowired
    private NoteCommentService noteCommentService;

    // TODO
    // 需求：用户修改头像和昵称后，需要进行同步
    // 解决方案一：
    //  思路：通过定时任务更新
    //  缺点：更新时涉及的数据量庞大
    // 解决方案二：
    //  思路：不存储 avatar 和 nickname，查询时通过 userId 从 user 服务中获取相关数据
    //  缺点：查询更复杂耗时
    @ApiOperation(value = "发布评论", notes = "在笔记下发布评论，也可以回复他人的评论")
    @PostMapping("/postComment")
    public R postComment(
            @RequestHeader("token") String token,
            @RequestParam("noteId") String noteId,
            @RequestParam("nickname") String nickname,
            @RequestParam("avatar") String avatar,
            @RequestParam("content") String content,
            @RequestParam(value = "replyCommentId", required = false) String replyCommentId,
            @RequestParam(value = "replyNickname", required = false) String replyNickname,
            @RequestParam(value = "replyUserId", required = false) String replyUserId) {
        String userId = JwtUtils.getIdByJwtToken(token);

        var noteComment = new NoteComment();
        noteComment.setNoteId(noteId);
        noteComment.setUserId(userId);
        noteComment.setNickname(nickname);
        noteComment.setAvatar(avatar);
        noteComment.setContent(content);
        // 评论回复
        if (replyCommentId != null) {
            noteComment.setReplyCommentId(replyCommentId);

            // 在评论回复中进行回复
            if (replyNickname != null && replyUserId != null) {
                noteComment.setReplyNickname(replyNickname);
                noteComment.setReplyUserId(replyUserId);
            }
        }

        noteCommentService.postComment(noteComment);

        return R.ok();
    }

    @ApiOperation(value = "获取评论", notes = "分页查询指定文章下的评论。如果评论包含三条以上回复，则只查询三条回复")
    @GetMapping("getComments")
    public R getComments(@RequestParam("noteId") String noteId,
                         @RequestParam("current") long current,
                         @RequestParam("limit") long limit,
                         @RequestParam("sortCondition") String sortCondition) {
        var result = noteCommentService.getComments(noteId, current, limit, sortCondition);
        return R.ok().data(result);
    }

    @ApiOperation(value = "获取评论下的所有回复")
    @GetMapping("getReplies")
    public R getReplies(@RequestParam("replyCommentId") String replyCommentId) {
        var data = noteCommentService.getReplies(replyCommentId);
        return R.ok().data("data", data);
    }


    // TODO 如何在页面进入时，对已经点过赞的评论进行处理？
    @ApiOperation(value = "评论点赞与取消")
    @PostMapping("likeComment")
    public R likeComment(@RequestHeader("token") String token,
                         @RequestParam(value = "commentId") String commentId) {
        String userId = JwtUtils.getIdByJwtToken(token);
        var result = noteCommentService.likeComment(userId, commentId);
        return R.ok().data(result);
    }


}
