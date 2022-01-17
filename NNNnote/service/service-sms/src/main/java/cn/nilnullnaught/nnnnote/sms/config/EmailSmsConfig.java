package cn.nilnullnaught.nnnnote.sms.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSmsConfig implements InitializingBean {

    @Value("${email.hostname}")
    private String HostName;

    @Value("${email.charset}")
    private String Charset;

    @Value("${email.emailaddress}")
    private String EmailAddress;

    @Value("${email.sender}")
    private String Sender;

    @Value("${email.authentication}")
    private String Authentication;


    public static String HOST_NAME;
    public static String CHARSET;
    public static String EMAILADDRESS;
    public static String SENDER;
    public static String AUTHENTICATION;

    @Override
    public void afterPropertiesSet() throws Exception {
        HOST_NAME = HostName;
        CHARSET = Charset;
        EMAILADDRESS = EmailAddress;
        SENDER = Sender;
        AUTHENTICATION = Authentication;
    }


}
