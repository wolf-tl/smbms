package com.tl.smbms.dao.user;

import com.tl.smbms.pojo.Role;
import com.tl.smbms.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author tl
 * 编写Dao层用户登陆的接口
 * 面向接口编程
 * 有利于整体框架的构建
 */
public interface UserDao {
    //登录
    public User getLoginUserInfo(Connection connection, String userCode);
    //更新密码
    public int updatePwd(Connection conn,String newPwd,int id);
    //获取数据库用户总数
    public int getUserCount(Connection conn, String userName, int userRole) throws SQLException;
    /**
     * 获取用户列表
     * @param conn：数据库连接对象
     * ===========后面两个参数用于条件查询用户数据
     * @param userName：按照用户名查找
     * @param userRole：按照角色名称查找
     * ===========后面两个参数用于对按照上面条件查询出来的结果进行分页处理
     * @param currentPageNo：翻到第多少页
     * @param pageSize：每一页多少条数据
     * @return：返回满足条件的user对象集合
     */
    public List<User> getUserList(Connection conn, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

}
