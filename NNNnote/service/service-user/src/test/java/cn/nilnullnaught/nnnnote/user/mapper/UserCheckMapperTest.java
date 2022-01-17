package cn.nilnullnaught.nnnnote.user.mapper;

import cn.nilnullnaught.nnnnote.common.utils.encryptUtils.MyEncryptUtils;
import cn.nilnullnaught.nnnnote.user.BaseTest;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UserCheckMapperTest extends BaseTest {
    @Autowired
    private UserCheckMapper userCheckMapper;


    @Test
    //@Transactional
    public void userRegister(){
        String UUID = IdWorker.get32UUID();
        String nickName = "TEST";
        String Email = "xxxxx@xxx.com";
        String Password = "**********";
        Password = MyEncryptUtils.encodeByBCrypt(Password);

        LocalDateTime date = LocalDateTime.now();
        userCheckMapper.userRegister(UUID,nickName,Email,Password,date);
    }

}
