package cn.nilnullnaught.nnnnote.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 密码的处理方法类型
 * </p>
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }

    /**
     * @param strength
     *            the log rounds to use, between 4 and 31
     */
    public DefaultPasswordEncoder(int strength) {

    }

    /**
     * 密码加密
     * @param rawPassword
     * @return
     */
    public String encode(CharSequence rawPassword) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(rawPassword.toString());
        return encodePassword;
    }

    /**
     * 密码验证
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.matches(rawPassword,encodedPassword);
    }
}
