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
                         @RequestParam(value = "noteId") String noteId,
                         @RequestParam(value = "cfolderId") String cfolderId){
        String userId = JwtUtils.getIdByJwtToken(token);

        noteUserCollectionService.noteCollect(noteId,userId,cfolderId);
        return R.ok();
    }



    @ApiOperation("获取收藏了指定笔记的用户收藏夹列表（因为笔记可以被多个收藏夹收藏，所以结果返回一个 cfolderId 列表）")
    @GetMapping("getCfolderIds")
    public R getCfolderIds(@RequestHeader("token") String token,
                           @RequestParam(value = "noteId") String noteId){
        String userId = JwtUtils.getIdByJwtToken(token);

        var  data=noteUserCollectionService.getCfolderIds(userId,noteId);
        return R.ok().data("data",data);
    }
}
