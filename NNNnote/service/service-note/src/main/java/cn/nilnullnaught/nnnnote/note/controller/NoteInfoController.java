package cn.nilnullnaught.nnnnote.note.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.note.service.NoteInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    @PostMapping("initializeNote")
    public R initializeNote(HttpServletRequest request){
        String ID = JwtUtils.getIdByJwtToken(request);
        NoteInfo noteInfo = noteInfoService.initializeNote(ID);
        return R.ok().data("data",noteInfo);
    }
}
