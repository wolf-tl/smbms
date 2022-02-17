package com.tl.smbms.servlet.user;

import com.tl.smbms.pojo.User;
import com.tl.smbms.service.user.UserService;
import com.tl.smbms.service.user.UserServiceImplement;
import com.tl.smbms.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tl
 * Servlet: 控制层调用业务层
 */
public class LoginServlet extends HttpServlet {
    private UserService userService=null;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1、获取前端传过来的用户名+密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        //2、调用业务层，通过账户（账户是唯一的）在数据库中查询，并判断账户密码的正确性
        this.userService = new UserServiceImplement();
        User user = userService.login(userCode,userPassword);//已经把用户查到了

        //3、判断返回是否为null
        if (user!=null&&user.getUserPassword().equals(userPassword)){//账号+密码正确
            //将用户的信息存入session中  相当于发门票
                req.getSession().setAttribute(Constants.USER_SESSION,user);
                //跳转内部主页
                resp.sendRedirect("/smbms/jsp/frame.jsp");

        }else{//账号+密码不正确
            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }


    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
