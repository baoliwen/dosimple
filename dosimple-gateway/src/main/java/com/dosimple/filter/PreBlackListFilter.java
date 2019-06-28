package com.dosimple.filter;


import com.dosimple.config.RedisKeys;
import com.dosimple.common.constant.ApiResult;
import com.dosimple.common.constant.ResponseCode;
import com.dosimple.common.util.GsonHelper;
import com.dosimple.common.util.RedisUtil;
import com.dosimple.common.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @date: 2018/8/8 0008 下午 5:41
 */
@Slf4j
@Component
@AllArgsConstructor
public class PreBlackListFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String ip = WebUtils.getIP(headers);
        if (StringUtils.isBlank(ip) && null != exchange.getRequest().getRemoteAddress()) {
            ip = exchange.getRequest().getRemoteAddress().getHostName();
        }
        if(RedisUtil.get(RedisKeys.BLACK_IP_LIST.build(ip)) != null){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
            try {
                ApiResult apiResult = new ApiResult(ResponseCode.DISABLE_USER);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(GsonHelper.toJson(apiResult).getBytes())));
            } catch (Exception e1) {
                log.error("返回结果异常", e1);
            }
            ApiResult apiResult = new ApiResult(ResponseCode.INTERNAL_SERVER_ERROR);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(GsonHelper.toJson(apiResult).getBytes())));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
