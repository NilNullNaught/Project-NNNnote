package cn.nilnullnaught.nnnnote.user.controller.WeChat;

import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import cn.nilnullnaught.nnnnote.user.service.UserInfoService;
import cn.nilnullnaught.nnnnote.user.util.ConstantWxUtils;
import cn.nilnullnaught.nnnnote.user.util.HttpClientUtils;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller  //只是请求地址，不需要返回数据(如果使用的是 @RestController，genQrConnect() 将无法跳转)
@RequestMapping("user/user-wechat")
public class WechatController {

    @Autowired
    private UserCheckService userCheckService;



    @ApiOperation(value = "生成微信扫描二维码")
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        //固定地址，后面拼接参数
        //String url = "https://open.weixin.qq.com/" +
        //"connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";

        // 微信开放平台授权baseUrl  %s相当于?代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (Exception e) {
            throw new MyCustomException(20001, e.getMessage());
        }
        // 防止csrf攻击（跨站请求伪造攻击）
        // String state = IdWorker.get32UUID();//一般情况下会使用一个随机数
        String state = "atguigu";

        //设置%s里面值
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                state
        );

        //重定向到请求微信地址里面
        return "redirect:" + qrcodeUrl;
    }

    @ApiOperation(value = "微信登录回调，获取扫描人信息，添加数据")
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            //1 获取code值，临时票据，类似于验证码
            //2 拿着code请求 微信固定的地址，得到两个值 accsess_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数 ：id  秘钥 和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //请求这个拼接好的地址，得到返回两个值 accsess_token 和 openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串获取出来两个值 accsess_token 和 openid
            //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
            //使用json转换工具 Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openidWx = (String) mapAccessToken.get("openid");

            //把扫描人信息添加数据库里面
            //判断数据表里面是否存在相同微信信息，根据openid判断
            var userId = userCheckService.checkByOpenidWx(openidWx);
            if (userId == null) {//userCheck，表没有相同微信数据，进行添加

                //3 拿着得到accsess_token 和 openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openidWx
                );
                //发送请求
                String wxUserInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userinfo字符串扫描人信息
                HashMap wxUserInfoMap = gson.fromJson(wxUserInfo, HashMap.class);
                String nickname = (String) wxUserInfoMap.get("nickname");//昵称
                String headimgURL = (String) wxUserInfoMap.get("headimgurl");//头像

                // 向数据库中写入注册信息
                userId = userCheckService.wechatRegister(nickname,headimgURL,openidWx);
            }

            //使用jwt根据 user 对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(userId, false);
            //最后：返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token=" + jwtToken;
        } catch (Exception e) {
            throw new MyCustomException(20001, "登录失败");
        }
    }
}
