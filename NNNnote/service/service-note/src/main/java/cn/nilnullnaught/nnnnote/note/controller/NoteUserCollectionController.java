package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.client.user.UserCfolderClient;
import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.note.service.NoteCommentService;
import cn.nilnullnaught.nnnnote.note.service.NoteUserCollectionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private UserCfolderClient userCfolderClient;

    @ApiOperation("笔记收藏与取消")
    @PostMapping("noteCollect")
    public R noteCollect(@RequestHeader("token") String token,
                         @RequestParam(value = "noteId") String noteId,
                         @RequestParam(value = "cfolderId") String cfolderId) {
        String userId = JwtUtils.getIdByJwtToken(token);

        noteUserCollectionService.noteCollect(noteId, userId, cfolderId);
        return R.ok();
    }


    @ApiOperation("获取收藏了指定笔记的用户收藏夹列表（因为笔记可以被多个收藏夹收藏，所以结果返回一个 cfolderId 列表）")
    @GetMapping("getCfolderIds")
    public R getCfolderIds(@RequestHeader("token") String token,
                           @RequestParam(value = "noteId") String noteId) {
        String userId = JwtUtils.getIdByJwtToken(token);

        var data = noteUserCollectionService.getCfolderIds(userId, noteId);
        return R.ok().data("data", data);
    }

    @ApiOperation(value = "分页查询收藏夹下的笔记", notes = "根据 token 中的信息分页查询指定收藏夹下的笔记")
    @GetMapping("getNoteInCfolderPaging")
    public R getNoteInCfolderPaging(@RequestHeader("token") String token,
                                    @RequestParam("cfolderId") String cfolderId,
                                    @RequestParam("page") Integer page,
                                    @RequestParam("limit") Integer limit) {
        String userId = JwtUtils.getIdByJwtToken(token);

        var result = noteUserCollectionService.getNoteInCfolderPaging(userId, cfolderId, page, limit);
        return R.ok().data(result);
    }

    @ApiOperation("批量取消收藏")
    @DeleteMapping("cancelCollectNote")
    @Transactional
    public R cancelCollectNote(@RequestHeader("token") String token,
                               @RequestParam("cfolderId")String cfolderId,
                               @RequestBody List<String> noteIdList) {

        // region <- 数据校验 ->

        var qw = new QueryWrapper<NoteUserCollection>();
        String userId = JwtUtils.getIdByJwtToken(token);
        qw.eq("user_id",userId);
        qw.eq("collection_folder_id",cfolderId);
        qw.in("note_id",noteIdList);
        qw.select("id");
        var _idlist = noteUserCollectionService.listObjs(qw);

        // endregion

        // region <- 删除收藏记录 ->

        if (_idlist.isEmpty()) throw new MyCustomException(20001,"错误");

        noteUserCollectionService.removeByIds(_idlist);
        // endregion

        // region <- 更新笔记收藏夹中的笔记数量 ->

        var qw2 = new QueryWrapper<NoteUserCollection>();
        qw2.eq("collection_folder_id", cfolderId);
        var count = noteUserCollectionService.count(qw2);
        userCfolderClient.updateCfolderNoteCount(count, cfolderId);

        // endregion

        return R.ok();
    }
}
