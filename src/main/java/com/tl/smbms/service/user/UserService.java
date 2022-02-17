package com.tl.smbms.service.user;

import com.tl.smbms.pojo.Role;
import com.tl.smbms.pojo.User;

import java.util.List;

/**
 * @author tl
 * 编写业务层接口
 * 业务层用户登录
 */
public interface UserService {
    /**
     *
     * @param userCode      账号
     * @param userPassword  密码
     * @return
     */
    public User login(String userCode, String userPassword);
    /**
     * 根据用户ID修改用户密码
     * @param newPwd：新密码
     * @param id：用户ID
     * @return
     */
    public boolean updatePwd(String newPwd, int id);
    /**
     * 获取用户总数
     * @param userName：按照用户姓名查，查到的用户总数
     * @param userRole：按照用户角色查，查到的用户总数
     * @return：返沪对应查询条件查到的用户总数
     */
    public int getUserCount(String userName, int userRole) ;
    /**
     * 根据用户名/用户角色名称来查询数据，返回一个User对象集合，而currentPageNo+pageSize用于前端做分页操作
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return：满足条件+limit的User对象集合
     */
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

}
