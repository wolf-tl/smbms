package com.tl.smbms.filter;

import com.tl.smbms.pojo.User;
import com.tl.smbms.utils.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tl
 * 过滤没有门票的用户
 * req.getContextPath()表示webapp路径
 */
public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req =(HttpServletRequest) request;
        HttpServletResponse resp=(HttpServletResponse) response;
        User user=(User)req.getSession().getAttribute(Constants.USER_SESSION);
        if(user==null){
            resp.sendRedirect(req.getContextPath()+"/error.jsp");
        }
        else {
            chain.doFilter(request,response);
        }
    }

    public void destroy() {

    }
}
