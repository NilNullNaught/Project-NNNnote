package cn.nilnullnaught.nnnnote.filter;

import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class FrontLoginGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 获取 token
        List<String> tokenList = request.getHeaders().get("token");

        // 放行不需要登录即可访问的接口
        //user-check 中的多数接口不需要登录即可访问
        if (antPathMatcher.match("/user/user-check/**", path)
                && !antPathMatcher.match("/user/user-check/alterUserEmail", path)
                && !antPathMatcher.match("/user/user-check/alterUserSafeInfo", path)
        ) {
            return chain.filter(exchange);
        }

        if (tokenList == null) {
            //请求体中不包含 token
            ServerHttpResponse response = exchange.getResponse();
            return MyResponse(response, 28004, "用户未登录");
        } else if (!JwtUtils.checkToken(tokenList.get(0))) {
            //请求体中包含 token 已经失效
            ServerHttpResponse response = exchange.getResponse();
            return MyResponse(response, 28004, "用户 token 已过期");
        }

        return chain.filter(exchange);
    }

    /**
     * 自定义响应信息
     *
     * @param response
     * @param code     响应状态码
     * @param data     错误描述
     * @return
     */
    private Mono<Void> MyResponse(ServerHttpResponse response, Integer code, String data) {
        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", code);
        message.addProperty("data", data);
        byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        // 设置 HTTP 返回状态码为 401（授权失败）
        //response.setStatusCode(HttpStatus.UNAUTHORIZED);//由于前端要对响应结果进行处理，所以此处不进行设置
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
