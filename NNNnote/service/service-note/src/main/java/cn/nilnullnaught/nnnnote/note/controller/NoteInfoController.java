package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;

import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.note.service.NoteInfoService;

import cn.nilnullnaught.nnnnote.note.service.NoteUserCollectionService;
import cn.nilnullnaught.nnnnote.note.vo.SaveNoteVo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private NoteUserCollectionService noteUserCollectionService;

    @ApiOperation("笔记初始化")
    @PostMapping("/initializeNote/{nFolderId}")
    public R initializeNote(@RequestHeader("token") String token, @PathVariable(required = false) String nFolderId) {
        String userId = JwtUtils.getIdByJwtToken(token);
        String noteID = noteInfoService.initializeNote(userId, nFolderId);
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
    public R saveNote(@RequestHeader("token") String token, @RequestBody SaveNoteVo saveNoteVo) {
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.saveNote(saveNoteVo, userId);
        return R.ok();
    }

    @ApiOperation("笔记自动保存")
    @PostMapping("/autoSaveNote")
    public R autoSaveNote(@RequestHeader("token") String token, @RequestBody SaveNoteVo saveNoteVo) {
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.autoSaveNote(saveNoteVo, userId);
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

    @ApiOperation(value = "还原被逻辑删除的笔记", notes = "还原时会对笔记所属的笔记文件夹进行检查，如果文件夹已经被删除，则还原到默认文件夹中")
    @PostMapping("/restoreDeletedNote")
    public R restoreDeletedNote(@RequestHeader("token") String token, @RequestBody List<String> idList) {
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.restoreDeletedNote(userId, token, idList);
        return R.ok();
    }

    @ApiOperation(value = "删除回收站中的笔记")
    @DeleteMapping("/deleteDeletedNotes")
    public R deleteDeletedNotes(@RequestHeader("token") String token, @RequestBody List<String> idList) {
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.deleteDeletedNotes(userId, idList);
        return R.ok();
    }

    @ApiOperation(value = "查询笔记相关数据（回收站数量，草稿数量，笔记总数）")
    @GetMapping("/getCountOfNoteInfo")
    public R getCountOfNoteInfo(@RequestHeader("token") String token) {
        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> map = noteInfoService.getCountOfNoteInfo(userId);
        return R.ok().data(map);
    }

    @ApiOperation(value = "通过 ID 查询笔记数量（只包含笔记总数，不包括回收站数量和草稿数量）")
    @GetMapping("/getCountOfNoteInfoById")
    public R getCountOfNoteInfoById(@RequestParam("userId") String userId) {
        Map<String, Object> map = noteInfoService.getCountOfNoteInfo(userId);
        return R.ok().data("data",map.get("noteCount"));
    }

    @ApiOperation("分页搜索已公开的笔记，通过 ElasticSearch 实现")
    @GetMapping("/searchNoteList")
    public R searchNoteList(@RequestParam(value = "criteria", required = false) String criteria,
                            @RequestParam(value = "sortField", required = false) String sortField,
                            @RequestParam("page") Integer page,
                            @RequestParam("limit") Integer limit) {

        var result = noteInfoService.searchNoteList(criteria, sortField, page, limit);
        return R.ok().data(result);

    }

    @ApiOperation("笔记点赞与取消")
    @PostMapping("/noteLike")
    public R noteLike(
            @RequestHeader("token") String token,
            @RequestParam(value = "noteId") String noteId) {
        String userId = JwtUtils.getIdByJwtToken(token);
        noteInfoService.noteLike(userId, noteId);
        return R.ok();
    }

    @ApiOperation("判断用户有没有对该笔记进行过点赞")
    @GetMapping("/userLikeNote")
    public R userLikeNote(
            @RequestHeader("token") String token,
            @RequestParam(value = "noteId") String noteId) {
        String userId = JwtUtils.getIdByJwtToken(token);
        Boolean result = noteInfoService.userLikeNote(userId, noteId);
        return R.ok().data("data", result);
    }

    @ApiOperation("查询笔记的点赞数（用户点赞或取消点赞后，更新笔记页展示的信息）")
    @GetMapping("/getNoteLikeCount/{noteId}")
    public R getNoteLikeCount(@PathVariable("noteId") String noteId) {
        var qw = new QueryWrapper<NoteInfo>();
        qw.select("likes");
        qw.eq("id", noteId);
        var result = noteInfoService.getOne(qw).getLikes();
        return R.ok().data("data", result);
    }

    @ApiOperation("查询笔记的收藏数")
    @GetMapping("/getNoteCollectCount/{noteId}")
    public R getNoteCollectCount(@PathVariable("noteId") String noteId) {
        var qw = new QueryWrapper<NoteInfo>();
        qw.select("collection_count");
        qw.eq("id", noteId);
        var result = noteInfoService.getOne(qw).getCollectionCount();
        return R.ok().data("data", result);
    }

    @ApiOperation(value = "更新笔记的收藏数",notes = "笔记收藏和取消收藏时触发，一位用户如果在多个收藏夹中收藏了同一篇笔记，则以一次计算")
    @PostMapping("/updateNoteCollectionCount")
    @Transactional
    public R updateNoteCollectionCount(@RequestBody List<String> noteIdList) {
        // region <- 更新笔记的收藏数量（每个用户只计算一次） ->

        for (var noteId : noteIdList){
            var qw = new QueryWrapper<NoteUserCollection>();
            qw.in("note_id", noteIdList);
            qw.select("DISTINCT user_id");

            var count = noteUserCollectionService.count(qw);

            var uw = new UpdateWrapper<NoteInfo>();
            uw.eq("id", noteId);
            uw.set("collection_count", count);
            noteInfoService.update(uw.getEntity(), uw);
        }

        // endregion
        return R.ok();

    }

    @ApiOperation(value = "查询指定用户公开的所有笔记")
    @GetMapping("/getPublicNotes")
    public R getPublicNotes(@RequestParam("userId")String userId,
                            @RequestParam(value = "criteria", required = false) String criteria,
                            @RequestParam(value = "sortField", required = false) String sortField,
                            @RequestParam("page") Integer page,
                            @RequestParam("limit") Integer limit){

        var result= noteInfoService.getPublicNotes(userId,criteria,sortField,page,limit);

        return R.ok().data(result);
    }
}
