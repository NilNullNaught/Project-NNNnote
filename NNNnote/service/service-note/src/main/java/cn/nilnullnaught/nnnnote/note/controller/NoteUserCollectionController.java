package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.note.service.NoteCommentService;
import cn.nilnullnaught.nnnnote.note.service.NoteUserCollectionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 笔记—收藏夹关系表 前端控制器
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@RestController
@RequestMapping("/note/note-user-collection")
public class NoteUserCollectionController {

    @Autowired
    private NoteUserCollectionService noteUserCollectionService;

    @ApiOperation("笔记收藏与取消")
    @PostMapping("noteCollect")
    public R noteCollect(@RequestHeader("token") String token,
                         @RequestParam(value = "noteId") String noteId){
        String userId = JwtUtils.getIdByJwtToken(token);

        noteUserCollectionService.noteCollect(noteId,userId);
        return R.ok();
    }

}
