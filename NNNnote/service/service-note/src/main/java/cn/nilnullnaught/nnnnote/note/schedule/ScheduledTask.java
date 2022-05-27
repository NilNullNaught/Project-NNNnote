package cn.nilnullnaught.nnnnote.note.schedule;

import cn.nilnullnaught.nnnnote.note.service.NoteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScheduledTask {

    @Autowired
    private NoteInfoService noteInfoService;

//    /**
//     * 测试
//     * 每五秒执行一次
//     */
//    @Scheduled(cron = "0/5 * * * * ? ")
//    public void task1() {
//        System.out.println("*********++++++++++++*****执行了");
//    }

    /**
     * 每天凌晨一点删除回收站中过期的数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void DeleteExpiredNoteInRecycleBin(){
        noteInfoService.deleteDeletedNotesScheduledTask();
    }

}
