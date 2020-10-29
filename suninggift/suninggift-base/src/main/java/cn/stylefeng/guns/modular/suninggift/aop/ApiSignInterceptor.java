package cn.stylefeng.guns.modular.suninggift.aop;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Data
public class ApiSignInterceptor implements HandlerInterceptor {

    private static final String TOKEN_COOKIE = "token";

    private boolean isNotAuth = false;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {
        String token = req.getHeader("token");

        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

         if(StringUtils.isBlank(token)){
            //空就不过
            return false;
        }

        return true;

    }



    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith("/login") || requestURI.startsWith("/error")) {
            return ;
        }

    }



    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    public String getNotNullValue(String... str){
        for(String s : str){
            if(StringUtils.isNotBlank(s)){
                return s;
            }
        }
        return null;
    }
}