package com.tl.smbms.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author tl
 * 字符编码过滤器
 */
public class Filter_ implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        chain.doFilter(request,response);
    }

    public void destroy() {

    }
}
