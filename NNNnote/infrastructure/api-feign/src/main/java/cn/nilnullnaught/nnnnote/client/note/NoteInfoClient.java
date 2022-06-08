package cn.nilnullnaught.nnnnote.client.note;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteComment;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service-note",contextId = "note-info")
public interface NoteInfoClient {

    @GetMapping("/note/note-info/getNotes/{page}/{limit}/{condition}")
     R getNotes(@RequestHeader("token") String token,
                      @PathVariable long page,
                      @PathVariable long limit,
                      @PathVariable String condition);

    @PostMapping("/note/note-info/getNotesByNoteIdList")
     List<NoteInfo> getNotesByNoteIdList(@RequestBody List<String> noteIdList);

}
