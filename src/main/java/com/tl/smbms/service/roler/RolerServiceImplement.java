package com.tl.smbms.service.roler;

import com.tl.smbms.dao.BaseDao;
import com.tl.smbms.dao.roler.RolerDao;
import com.tl.smbms.dao.roler.RolerDaoImplement;
import com.tl.smbms.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author tl
 * 专注于角色业务
 */
public class RolerServiceImplement implements RolerService {
    private RolerDao rolerDao = null;

    public RolerServiceImplement() {
        rolerDao = new RolerDaoImplement();
    }

    public List<Role> getRoleList() {
        Connection conn = null;//获取连接
        List<Role> roleList = null;
        try {
            conn = BaseDao.getConnection();
            roleList = rolerDao.getRoleList(conn);//服务层调用Dao层方法
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.close(conn, null, null);//关闭连接
        }
        return roleList;
    }

    @Test
    public void test() {
        RolerService rolerService = new RolerServiceImplement();
        List<Role> list = rolerService.getRoleList();
        for (Role role : list) {
            System.out.println(role.getId() + "\t" + role.getRoleName() + "\t" + role.getRoleCode());
        }
    }
}
