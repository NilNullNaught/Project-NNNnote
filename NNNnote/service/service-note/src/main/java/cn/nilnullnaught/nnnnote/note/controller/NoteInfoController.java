package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.note.service.NoteInfoService;
import cn.nilnullnaught.nnnnote.note.vo.SaveNoteVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 笔记信息表 前端控制器
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@RestController
@RequestMapping("/note/note-info")
public class NoteInfoController {

    @Autowired
    private NoteInfoService noteInfoService;


    @ApiOperation("笔记初始化")
    @PostMapping("/initializeNote/{nFolderId}")
    public R initializeNote(HttpServletRequest request, @PathVariable(required = false) String nFolderId) {
        String ID = JwtUtils.getIdByJwtToken(request);
        String noteID = noteInfoService.initializeNote(ID, nFolderId);
        return R.ok().data("data", noteID);
    }

    @ApiOperation("查询笔记信息（编辑）")
    @GetMapping("/getNoteInfoToEdit/{noteId}")
    public R getNoteInfoToEdit(@PathVariable String noteId) {
        Map result = noteInfoService.getNoteInfoToEdit(noteId);
        return R.ok().data("data", result);
    }

    @ApiOperation("查询笔记信息（阅读）")
    @GetMapping("/getNoteInfoToRead/{noteId}")
    public R getNoteInfoToRead(@PathVariable String noteId) {
        Map result = noteInfoService.getNoteInfoToRead(noteId);
        return R.ok().data("data", result);
    }


    @ApiOperation("笔记保存")
    @PostMapping("/saveNote")
    public R saveNote(@RequestBody SaveNoteVo saveNoteVo) {
        noteInfoService.saveNote(saveNoteVo);
        return R.ok();
    }

    @ApiOperation("笔记自动保存")
    @PostMapping("/autoSaveNote")
    public R autoSaveNote(@RequestBody SaveNoteVo saveNoteVo) {
        noteInfoService.autoSaveNote(saveNoteVo);
        return R.ok();
    }

    @ApiOperation(value = "分页条件查询笔记")
    @PostMapping("getNotes")
    public R getNotes(
            @RequestHeader("token") String token,
            @RequestParam(value = "noteFolderId", required = false) String noteFolderId,
            @RequestParam("page") long page,
            @RequestParam("limit") long limit,
            @RequestParam(value = "condition", required = false) String condition) {

        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> map = noteInfoService.getNotes(userId,noteFolderId, page, limit, condition);
        return R.ok().data(map);
    }

    @ApiOperation(value = "批量删除笔记",notes = "暂时删除，可以从回收站中找回")
    @DeleteMapping("deleteNotes")
    public R deleteNotes(@RequestBody List<String> nFolderList) {
        noteInfoService.deleteNotes(nFolderList);
        return R.ok();
    }

}
