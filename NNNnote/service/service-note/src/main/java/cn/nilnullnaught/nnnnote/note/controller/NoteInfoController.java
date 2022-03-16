package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.note.service.NoteInfoService;
import cn.nilnullnaught.nnnnote.note.util.MyElasticsearchRestTemplate;
import cn.nilnullnaught.nnnnote.note.vo.SaveNoteVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Autowired
    private MyElasticsearchRestTemplate myElasticsearchRestTemplate;

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
        return R.ok().data(result);
    }

    @ApiOperation("查询笔记信息（阅读）")
    @GetMapping("/getNoteInfoToRead/{noteId}")
    public R getNoteInfoToRead(@PathVariable String noteId) {
        Map result = noteInfoService.getNoteInfoToRead(noteId);
        return R.ok().data(result);
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
    @PostMapping("/getNotes")
    public R getNotes(
            @RequestHeader("token") String token,
            @RequestParam(value = "noteFolderId", required = false) String noteFolderId,
            @RequestParam("page") long page,
            @RequestParam("limit") long limit,
            @RequestParam(value = "condition", required = false) String condition) {

        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> map = noteInfoService.getNotes(userId, noteFolderId, page, limit, condition);
        return R.ok().data(map);
    }

    @ApiOperation(value = "批量删除笔记", notes = "暂时删除，可以从回收站中找回")
    @DeleteMapping("/deleteNotes")
    public R deleteNotes(@RequestHeader("token") String token, @RequestBody List<String> idList) {
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.deleteNotes(userId, idList);
        return R.ok();
    }

    @ApiOperation(value = "分页查询草稿")
    @PostMapping("/getDraftList")
    public R getDraftList(
            @RequestHeader("token") String token,
            @RequestParam("page") long page,
            @RequestParam("limit") long limit) {
        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> resultList = noteInfoService.getDraftList(userId, page, limit);
        return R.ok().data(resultList);
    }

    @ApiOperation(value = "批量删除草稿", notes = "彻底删除，不可找回")
    @DeleteMapping("/deleteDrafts")
    public R deleteDrafts(@RequestHeader("token") String token, @RequestBody List<String> idList) {
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.deleteDrafts(userId, idList);
        return R.ok();
    }

    @ApiOperation(value = "分页查询被逻辑删除的笔记", notes = "")
    @PostMapping("/getLogicDeletedNoteList")
    public R getLogicDeletedNoteList(@RequestHeader("token") String token,
                                  @RequestParam("page") long page,
                                  @RequestParam("limit") long limit) {

        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> resultList = noteInfoService.getLogicDeletedNoteList(userId, page, limit);
        return R.ok().data(resultList);
    }

    @ApiOperation(value = "还原被逻辑删除的笔记",notes = "还原时会对笔记所属的笔记文件夹进行检查，如果文件夹已经被删除，则还原到默认文件夹中")
    @PostMapping("/restoreDeletedNote")
    public R restoreDeletedNote(@RequestHeader("token") String token,@RequestBody List<String> idList){
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.restoreDeletedNote(userId, token, idList);
        return R.ok();
    }

    @ApiOperation(value = "删除回收站中的笔记")
    @DeleteMapping("/deleteDeletedNotes")
    public R deleteDeletedNotes(@RequestHeader("token")String token,@RequestBody List<String> idList){
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.deleteDeletedNotes(userId,idList);
        return R.ok();
    }

    @ApiOperation(value = "查询笔记相关数据（回收站数量，草稿数量，笔记总数）")
    @GetMapping("/getCountOfNoteInfo")
    public R getCountOfNoteInfo(@RequestHeader("token") String token) {
        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> map= noteInfoService.getCountOfNoteInfo(userId);
        return R.ok().data(map);
    }

    @ApiOperation("分页搜索已公开的笔记,通过 ElasticSearch 实现")
    @GetMapping("/searchNoteList")
    public R searchNoteList(@RequestParam(value = "condition",required = false)String condition,
                            @RequestParam("sortField")String sortField,
                            @RequestParam("page")Integer page,
                            @RequestParam("limit")Integer limit)  {

        try {
            if (condition == null) condition ="";
            var result = myElasticsearchRestTemplate.noteList(condition,sortField,page,limit);
            return R.ok().data("data",result);
        }catch (Exception e){
            e.printStackTrace();
            throw new MyCustomException(20001,"搜索失败");
        }
    }

}
