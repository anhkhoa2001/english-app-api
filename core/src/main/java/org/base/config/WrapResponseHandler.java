package org.base.config;

import org.base.dto.common.MessageResponseDTO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = {"org.base", "org.example"})
@EnableWebMvc
public class WrapResponseHandler extends HandlerInterceptorAdapter implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                            ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) serverHttpRequest;
        if (!(o instanceof MessageResponseDTO) && (methodParameter.getMethodAnnotation(IgnoreWrapResponse.class) != null ||
                        (methodParameter.getDeclaringClass().getAnnotation(EnableWrapResponse.class) == null))) {
            return o;
        }

        MessageResponseDTO msg = new MessageResponseDTO().build(o);
        msg.setPath(servletServerRequest.getServletRequest().getRequestURI());

        return msg;
    }
}
