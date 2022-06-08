package cn.nilnullnaught.nnnnote.client.note;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteComment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "service-note",contextId = "note-comment")
public interface NoteCommentClient {

    @PostMapping("/note/note-comment/getCommentListById")
    List<NoteComment> getCommentListById(@RequestBody List<String> idList);
}
