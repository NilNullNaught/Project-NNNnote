package cn.nilnullnaught.nnnnote.user.service.impl;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.encryptUtils.MyEncryptUtils;
import cn.nilnullnaught.nnnnote.entity.user.UserCheck;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.mapper.UserCheckMapper;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import cn.nilnullnaught.nnnnote.user.vo.LoginVo;
import cn.nilnullnaught.nnnnote.user.vo.RegisterVo;
import cn.nilnullnaught.nnnnote.user.vo.ResetPasswordVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.EncryptUtils;
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
     * 校验注册信息、密码加密，最后将数据写入数据库
     * @param registerVo
     */
    @Override
    @Transactional
    public void userRegister(RegisterVo registerVo)  {
        //获取注册信息
        String nickname = registerVo.getNickname();
        String email = registerVo.getEmail();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //校验注册信息
        if (StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(email) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new MyCustomException(20001,"注册信息中包含空值");
        }

        //校验验证码
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
     * @param loginVo
     */
    @Override
    public String userLogin(LoginVo loginVo) {
        //获取登录邮箱和密码
        String email = loginVo.getEmail();
        String password = loginVo.getPassword();
        Boolean rememberMe = loginVo.getRememberMe();
        //邮箱和密码非空判断
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new MyCustomException(20001,"邮箱或密码为空");
        }

        //判断邮箱是否被注册
        QueryWrapper<UserCheck> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        UserCheck userCheck = baseMapper.selectOne(wrapper);
        if (userCheck == null){
            //邮箱未被注册
            throw new MyCustomException(20001,"该邮箱尚未注册");
        }

        //验证密码是否正确
        if (!MyEncryptUtils.checkByBCrypt(password,userCheck.getPassword())){
            throw new MyCustomException(20001,"密码错误");
        }

        //查询账号是否被禁用
        if(userCheck.getIsDisabled()){
            throw new MyCustomException(20001,"该用户被禁用");
        }

        //登录成功，生成token字符串
        String jwtToken = JwtUtils.getJwtToken(userCheck.getId(),rememberMe);
        return jwtToken;
    }

    /**
     * 重置用户密码
     * @param resetPasswordVo
     */
    @Override
    @Transactional
    public void restPassword(ResetPasswordVo resetPasswordVo) {
        //获取用户注册信息
        String email=resetPasswordVo.getEmail();
        String password=resetPasswordVo.getPassword();
        String code = resetPasswordVo.getCode();

        //参数校验
        if(StringUtils.isEmpty(email)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)
        ){
            throw new MyCustomException(20001,"参数校验失败");
        }

         //校验验证码
        if(!code.equals(redisTemplate.opsForValue().get(email))){
            throw new MyCustomException(20001,"验证码错误");
        }

        //判断邮箱是否被注册
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

        //为新密码进行加密
        String passwordEncrypt = MyEncryptUtils.encodeByBCrypt(password);

        //修改密码
        _userCheck.setPassword(passwordEncrypt);
        if (baseMapper.updateById(_userCheck) != 1){
            throw new MyCustomException(20001,"密码修改失败");
        }
    }

}
