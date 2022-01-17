package cn.nilnullnaught.nnnnote.client.sms;

import cn.nilnullnaught.nnnnote.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-sms")
public interface SmsEmailClient {

    static final String basePath = "sms/email";

    @GetMapping(value = basePath+"/sendCode/{emailAddress}")
    public R sendCode(@PathVariable("emailAddress") String emailAddress);
}
