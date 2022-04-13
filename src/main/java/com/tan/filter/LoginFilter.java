package com.tan.filter;

/**
 * @author TanS
 * @date 2022/4/12
 */

import com.alibaba.fastjson.JSON;
import com.tan.utils.BaseContext;
import com.tan.utils.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器
 */
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String uri = httpServletRequest.getRequestURI();

        String[] strings = new String[]{
                "/employee/login",
                "/backend/images/**",
                "/backend/page/login/**",
                "/backend/js/**",
                "/backend/api/**",
                "/backend/styles/**",
                "/backend/plugins/**",
                "/front/images/**",
                "/front/js/**",
                "/front/api/**",
                "/front/styles/**",
                "/front/fonts/**",
                "/swagger-ui.html"
        };
        boolean check = this.check(strings, uri);

        if (check) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return ;
        }

        if (httpServletRequest.getSession().getAttribute("employee")!=null){

            Long id = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.set(id);
            chain.doFilter(httpServletRequest, httpServletResponse);
            return ;
        }

        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 检查路径
     *
     * @param uris
     * @param uri
     * @return
     */
    private boolean check(String[] uris, String uri) {
        for (String s : uris) {
            boolean match = PATH_MATCHER.match(s, uri);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
