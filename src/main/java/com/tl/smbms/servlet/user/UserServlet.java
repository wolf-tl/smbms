package com.tl.smbms.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.tl.smbms.pojo.Role;
import com.tl.smbms.pojo.User;
import com.tl.smbms.service.roler.RolerServiceImplement;
import com.tl.smbms.service.user.UserService;
import com.tl.smbms.service.user.UserServiceImplement;
import com.tl.smbms.utils.Constants;
import com.tl.smbms.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tl
 * UserServlet 用于控制修改密码的业务
 * 通过封装方法来复用Servlet
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        //验证旧密码
        if(method.equals("pwdmodify")&&method!=null){
            pwdModify(req,resp);
        }
        //保存新密码
        if(method.equals("savepwd")&&method!=null){
            //通过获取前端隐藏域传过来的方法的名称来判断应该调用我们封装的哪个方法
            //从而实现了Servlet的复用
            updatePwd(req,resp);
        }
        if(method.equals("query")&&method!=null){
            query(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        boolean flag = false;

        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        //获取到了这个用户对象且获取到的新密码不为空
        if (user!=null && newpassword!=null){
            //获取业务层对象
            UserService userService = new UserServiceImplement();
            //servlet调用业务层
            flag = userService.updatePwd(newpassword,user.getId());
            if (flag){//修改成功
                req.setAttribute("message","密码修改成功！请使用新密码重新登陆");
                //移除用户的session(门票)，利用过滤器阻止用户再进行操作,直接跳转error.jsp页面
                req.getSession().removeAttribute(Constants.USER_SESSION);
//                resp.sendRedirect(req.getContextPath()+"/error.jsp");
            }else{
                req.setAttribute("message","密码修改失败");
            }
        }else {//session过期的话，点击保存后会直接跳转到error.jsp；所以能走到这里
            //一定是有session的，即user!=null
            req.setAttribute("message","密码设置有误，请重新输入！");
        }
        //无论是修改成功还是失败，都重定向到密码修改页面，就是在刷新页面，否则我们设置在req中的message属性不会被前端读到
        resp.sendRedirect("/smbms/jsp/pwdmodify.jsp");
        //转发并不会跳出页面因为转发规避了过滤器
//        req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);

    }

    //验证旧密码
    //直接与session中的user对象的密码进行对比即可，不用再去查数据库
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object user = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        //使用Map集合存储返回给前端的数据
        Map<String, String> resultMap = new HashMap<String, String>();

        if (user == null) {//session中的user对象失效了
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {//输入的密码为空
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User) user).getUserPassword();
            if (userPassword.equals(oldpassword)) {//输入的密码匹配
                resultMap.put("result", "true");
            } else {//输入的密码不匹配
                resultMap.put("result", "false");
            }
        }

        //将Map集合中的数据转为JSON格式传输给前端
        try {
            //设置返回数据是一个JSON，这样浏览器拿到这个数据之后就会按照JSON的数据格式来解析它
            resp.setContentType("application/JSON");
            PrintWriter writer = resp.getWriter();//获取输出流
            //使用阿里巴巴提供的一个JSON工具类，直接将其他数据格式的数据转为JSON数据格式，然后直接将其写出去
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();//刷新缓冲区
            writer.close();//关闭资源
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //按照用户名/职位名称查询用户列表或整表查询
    //【重点&难点】
    public void query(HttpServletRequest req, HttpServletResponse resp){
        //1、从前端获取数据
        String queryname = req.getParameter("queryname");
        String queryUserRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");//通过隐藏域进行的提交，默认 = 1

        int UserRole = 0;//我们先让UserRole = 0，因为从前端接收到的queryUserRole可能就是一个NULL，此时我们就需要将其指定为0
        int pageSize = 5;//这个数字最好是写在配置文件中，这样以后想要修改一页上面显示的行数，我们就不用再从新编译代码和测试了
        int currentPageNo = 1;//先给当前页设置一个默认的值

        //2、通过判断参数决定哪些请求需要处理
        if (queryname == null){
            queryname = "";//如果前端没有按照用户名查询，我们就将用户名设置""
        }
        if (queryUserRole!=null && queryUserRole!=""){
            UserRole = Integer.parseInt(queryUserRole);//当前端传过来的queryUserRole有数据的时候我们才对其进行解析
        }
        if (pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //3、为了实现分页，需要使用工具类PageSupport并传入总用户数，计算出totalPageCount
        //参数totalCount由getUserCount得出；pageSize是固定死了的；currentPageNo默认设为1
        UserService userService = new UserServiceImplement();
        //得到用户数
        int totalCount = userService.getUserCount(queryname,UserRole);
        //使用最开始导入的工具类
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);//设置一页多少行数据
        pageSupport.setTotalCount(totalCount);//设置总页数
        pageSupport.setCurrentPageNo(currentPageNo);//设置当前页数


        int totalPageCount = 0;
        totalPageCount = pageSupport.getTotalPageCount();

        //4、控制翻页
        if (currentPageNo<1){//在第一页的时候还想点击上一页
            currentPageNo = 1;
        }else if(currentPageNo>pageSupport.getTotalPageCount()) {//在第最后一页的时候还想点击下一页
            currentPageNo = totalPageCount;
        }

        //5、用户列表展示
        List<User> userList = userService.getUserList(queryname,UserRole,currentPageNo,pageSize);
        //将集合返回给前端进行解析显示
        req.setAttribute("userList",userList);

        //6、角色列表展示
        List<Role> roleList = new RolerServiceImplement().getRoleList();
        req.setAttribute("roleList",roleList);

        //7、将参数返回前端
        req.setAttribute("queryUserName",queryname);
        req.setAttribute("queryUserRole",queryUserRole);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);

        //8、转发/重定向刷新页面
        try {
            System.out.println("=======================进入到servlet，且调用method = query");
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

