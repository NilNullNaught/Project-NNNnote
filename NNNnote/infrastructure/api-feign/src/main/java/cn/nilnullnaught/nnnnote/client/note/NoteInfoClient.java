package cn.nilnullnaught.nnnnote.client.note;

import cn.nilnullnaught.nnnnote.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("service-note")
public interface NoteInfoClient {

    @GetMapping("/note/note-info/getNotes/{page}/{limit}/{condition}")
    public R getNotes(@RequestHeader("token") String token,
                      @PathVariable long page,
                      @PathVariable long limit,
                      @PathVariable String condition);
}