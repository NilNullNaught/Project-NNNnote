package cn.nilnullnaught.nnnnote.user.service.impl;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.encryptUtils.MyEncryptUtils;
import cn.nilnullnaught.nnnnote.entity.user.UserCheck;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.mapper.UserCheckMapper;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import cn.nilnullnaught.nnnnote.user.vo.RegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * <p>
 * 用户登录验证表 服务实现类
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Service
@Slf4j
public class UserCheckServiceImpl extends ServiceImpl<UserCheckMapper, UserCheck> implements UserCheckService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    private static BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    /**
     * 检查该手机号是否已被注册
     * @param mobile
     * @return
     */
    @Override
    public Boolean checkMobile(String mobile) {

        QueryWrapper<UserCheck> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile",mobile);
        UserCheck userCheck =  getOne(queryWrapper);

        if (userCheck == null) return false;
        return true;
    }

    /**
     * 检查该邮箱地址是否已被注册
     * @param email
     * @return
     */
    @Override
    public Boolean checkEmail(String email) {
        QueryWrapper<UserCheck> queryWrapper = new QueryWrapper();
        queryWrapper.eq("email",email);
        UserCheck userCheck =  getOne(queryWrapper);

        if (userCheck == null) return false;
        return true;
    }

    /**
     * 注册校验、密码加密，最后将数据写入数据库
     * @param registerVo
     */
    @Override
    @Transactional
    public void userRegister(RegisterVo registerVo)  {
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String email = registerVo.getEmail();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //校验参数
        if (StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(email) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new MyCustomException(20001,"注册信息中包含空值");
        }

        //校验验证码
        log.info("email = "+email);
        if(!code.equals(redisTemplate.opsForValue().get(email))){
            throw new MyCustomException(20001,"验证码错误");
        }

        //判断邮箱是否已被注册
        QueryWrapper<UserCheck> queryWrapper = new QueryWrapper();
        queryWrapper.eq("email",email);
        if (getOne(queryWrapper) != null) throw new MyCustomException(20001,"邮箱已被注册");

        //生成 UUID
        String IdWorkerID = IdWorker.get32UUID();

        //密码加密
        password = MyEncryptUtils.encodeByBCrypt(password);

        //将数据添加到数据库中
        baseMapper.userRegister(IdWorkerID,nickname,email,password, LocalDateTime.now());
    }

    /**
     * 根据用户邮箱和密码进行登录校验，并生成 token
     * @param userCheck
     */
    @Override
    public String userLogin(UserCheck userCheck) {
        //获取登录邮箱和密码
        String email = userCheck.getEmail();
        String password = userCheck.getPassword();

        //邮箱和密码非空判断
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new MyCustomException(20001,"邮箱或密码为空");
        }

        //查询用户 UserCheck 信息
        QueryWrapper<UserCheck> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        UserCheck _userCheck = baseMapper.selectOne(wrapper);
        if (_userCheck == null){
            throw new MyCustomException(20001,"该邮箱尚未注册");
        }

        //查询账号是否被禁用
        if(_userCheck.getIsDisabled()){
            throw new MyCustomException(20001,"该用户被禁用");
        }

        //登录成功，生成token字符串
        String jwtToken = JwtUtils.getJwtToken(_userCheck.getId());
        return jwtToken;
    }

}
