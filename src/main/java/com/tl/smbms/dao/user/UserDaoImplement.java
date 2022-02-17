package com.tl.smbms.dao.user;

import com.mysql.jdbc.StringUtils;
import com.tl.smbms.dao.BaseDao;
import com.tl.smbms.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tl
 * Dao层用户登陆的接口实现类
 * 有利于专注于实现
 */
public class UserDaoImplement implements UserDao{
    //用户登录
    public User getLoginUserInfo(Connection connection, String userCode) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        User user = null;
        if (connection!=null){
            String sql = "SELECT * FROM smbms_user WHERE userCode = ?";
            Object[] params = {userCode};
            //调用项目搭建阶段准备的公共查询方法
            rs = BaseDao.execute(connection,sql,params,preparedStatement,rs);
            try {
                while (rs.next()){
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));
                }

                //关闭资源
                //因为数据库的连接可能不只是这一个操作，
                // 所以我们不应该做完一件事就把数据库连接对象销毁，所以conn处传的null
                BaseDao.close(null,preparedStatement,rs);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return user;
    }
    //修改密码
    public int updatePwd(Connection conn, String newPwd, int id) {
        PreparedStatement preparedStatement = null;
        int rs = 0;
        User user = null;
        if (conn!=null){
            String sql = "UPDATE smbms_user SET userPassword = ? WHERE id = ?";
            Object[] param = {newPwd,id};//按照sql语句的占位符的顺序来传递数据，使用的时候需要注意
            rs = BaseDao.execute(conn,sql,param,preparedStatement);
            //把这次使用的sql语句发送器关掉，连接不要关，service还可能有其他用
            BaseDao.close(null,preparedStatement,null);
        }
        return rs;
    }
    //根据用户名/角色名获取用户总数
    public int getUserCount(Connection conn, String userName, int userRole) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        if (conn!=null){
            //使用字符串缓冲区，这样就可以动态的在sql后面追加AND条件了
            StringBuffer sql = new StringBuffer();
            //基本的联表查询SQL语句
            sql.append("SELECT COUNT(1) COUNT FROM smbms_user u,smbms_role r WHERE u.userRole = r.id");
            //创建一个list来存储我们要拼接的筛选条件，由于我们不能限制传入的参数的数据类型，所以泛型指定为object
            List<Object> list = new ArrayList<Object>();
            //判断传入的用户名是不是空，如果不是空表明前端指定了按照姓名查询的参数
            if (!StringUtils.isNullOrEmpty(userName)){
                //AND前需要有空格
                sql.append(" AND userName like ?");
                list.add("%"+userName+"%");
            }
            if (userRole>0){
                sql.append(" AND userRole = ?");
                list.add(userRole);
            }
            //获得BaseDao中为pstmt对象设置参数的参数列表
            Object[] params = list.toArray();
            //调用查询定义好的查询方法
            rs = BaseDao.execute(conn,sql.toString(), params, pstmt, rs);
            if (rs.next()){//获取查询结果
                //COUNT是在SQL语句中为查询结果取的别名
                count = rs.getInt("COUNT");
            }
            BaseDao.close(null,pstmt,rs);//关闭资源
        }
        return count;
    }
    //获取满足条件的用户列表
    public List<User> getUserList(Connection conn, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> userList = null;
        if (conn!=null){
            userList = new ArrayList<User>();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT u.*,r.roleName as userRoleName FROM smbms_user u,smbms_role r WHERE u.userRole = r.id");
            List<Object> temp = new ArrayList<Object>();
            if (userName!=null){
                sql.append(" AND u.userName LIKE ?");
                temp.add("%"+userName+"%");
            }
            if (userRole>0){
                sql.append(" AND u.userRole = ?");
                temp.add(userRole);
            }
            sql.append(" ORDER BY u.creationDate DESC LIMIT ?,?");//在sql最后追加一个排序和分页
            //减一的原因就是MYSQL分页的index从0开始
            currentPageNo = (currentPageNo-1)*pageSize;
            temp.add(currentPageNo);//从哪一个下标开始
            temp.add(pageSize);//从currentPageNo连续取几个
            Object[] params = temp.toArray();
            rs = BaseDao.execute(conn,sql.toString(),params,pstmt,rs);
            while (rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                //这个属性是在POJO中新加入的，数据表中没有
                _user.setUserRoleName(rs.getString("userRoleName"));
                //通过userRole能够查询多个人的信息
                userList.add(_user);
            }
            BaseDao.close(null,pstmt,rs);
        }
        return userList;
    }
    //增加用户

    //删除用户

}
