package com.tl.smbms.service.user;


import com.tl.smbms.dao.BaseDao;
import com.tl.smbms.dao.user.UserDao;
import com.tl.smbms.dao.user.UserDaoImplement;
import com.tl.smbms.pojo.Role;
import com.tl.smbms.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author tl
 * 面向接口编程
 * 业务层用户登录
 * 业务层调用Dao层
 */
public class UserServiceImplement implements UserService {
    //业务层需要使用Dao，所以直接将Dao作为一个成员变量来使用
    private UserDao userDao;

    //通过构造器创建UserDao接口的实现类对象userDao
    public UserServiceImplement() {
        userDao = new UserDaoImplement();
    }

    //获取用户信息
    public User login(String userCode, String userPassword) {
        //获取数据库连接对象
        Connection conn = BaseDao.getConnection();
        //通过业务层调用Dao层
        //调用userDao中的获取用户信息的方法
        User user = userDao.getLoginUserInfo(conn, userCode);
        //关闭资源
        BaseDao.close(conn, null, null);

        return user;
    }
    //修改密码
    public boolean updatePwd(String newPwd, int id) {
        Connection conn = null;
        int rs = 0;
        boolean flag = false;

        conn = BaseDao.getConnection();//获取数据库连接对象
        //通过业务层调用Dao层
        if (userDao.updatePwd(conn, newPwd, id) > 0) {//数据库修改成功
            flag = true;
        }
        BaseDao.close(conn, null, null);

        return flag;
    }
    //显示用户总数
    public int getUserCount(String userName, int userRole) {
        Connection conn = null;
        int rs = 0;

        try {
            conn = BaseDao.getConnection();//获取数据库连接对象
            rs = userDao.getUserCount(conn, userName, userRole);//业务层调用Dao层获取业务结果
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.close(conn, null, null);//关闭资源
        }
        return rs;//业务层将业务结果返回给servlet

    }
    //显示用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection conn = null;
        List<User> userList = null;
        try {
            conn = BaseDao.getConnection();//获取连接
            //业务层调用Dao层获取业务结果
            userList = userDao.getUserList(conn, userName, userRole, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.close(conn, null, null);//关闭资源
        }
        return userList;//业务层将业务结果返回给servlet
    }
    //增加用户

    //删除用户

}

