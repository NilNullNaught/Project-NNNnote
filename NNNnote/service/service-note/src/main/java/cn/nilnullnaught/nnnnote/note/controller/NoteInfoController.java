package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.note.service.NoteInfoService;
import cn.nilnullnaught.nnnnote.note.vo.SaveNoteVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @PostMapping("/initializeNote")
    public R initializeNote(HttpServletRequest request){
        String ID = JwtUtils.getIdByJwtToken(request);
        String noteID = noteInfoService.initializeNote(ID);
        return R.ok().data("data",noteID);
    }

    @ApiOperation("查询笔记信息")
    @GetMapping("/getNoteInfo/{noteId}")
    public R getNoteInfo(@PathVariable String noteId){
        Map result = noteInfoService.noteInfo(noteId);
        return R.ok().data("data",result);
    }


    @ApiOperation("笔记保存")
    @PostMapping("/saveNote")
    public R saveNote(@RequestBody SaveNoteVo saveNoteVo){
        noteInfoService.saveNote(saveNoteVo);
        return R.ok();
    }
}
