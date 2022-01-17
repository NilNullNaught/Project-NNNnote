package cn.nilnullnaught.nnnnote.sms.service.impl;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.common.utils.RandomUtil;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.sms.config.EmailSmsConfig;
import cn.nilnullnaught.nnnnote.sms.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 向用户邮箱发送验证码邮件
     * @param emailAddress :用户的邮件地址
     * @return :发送是否成功
     */
    @Override
    public void sendCode(String emailAddress)  {
        log.info(LocalDateTime.now().toString());
        //判断是否已经发送邮件
        String code = redisTemplate.opsForValue().get(emailAddress);
        if(!StringUtils.isEmpty(code)) throw new MyCustomException(20001,"邮件已发送，尚未过期");

        //发送邮件
        try {
            //生成六位随机验证码
            code = RandomUtil.getSixBitRandom();
            //在 Redis 中存入六位随机验证码，并设置过期时间为 1 分钟
            redisTemplate.opsForValue().set(emailAddress, code,1, TimeUnit.MINUTES);

            HtmlEmail email = new HtmlEmail();
            email.setHostName(EmailSmsConfig.HOST_NAME);
            email.setCharset(EmailSmsConfig.CHARSET);
            email.setFrom(EmailSmsConfig.EMAILADDRESS,EmailSmsConfig.SENDER);
            email.setAuthentication(EmailSmsConfig.EMAILADDRESS,EmailSmsConfig.AUTHENTICATION);

            //设置收件人
            email.addTo(emailAddress);
            //设置发送主题
            email.setSubject("注册验证码");
            //设置发送内容,即验证码
            email.setMsg(code);
            //进行发送
            email.send();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送失败");
            throw new MyCustomException(20001,"邮件发送失败");
        }

    }

}
