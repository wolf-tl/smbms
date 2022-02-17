package com.tl.smbms.dao.roler;

import com.tl.smbms.dao.BaseDao;
import com.tl.smbms.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tl
 * 专注于角色Dao
 */
public class RolerDaoImplement implements RolerDao{
    public List<Role> getRoleList(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Role> list = null;//存储角色对象的集合

        if (conn!=null){
            list = new ArrayList<Role>();
            String sql = "SELECT * FROM smbms_role";//直接写死，不用参数
            Object[] params = {};
            rs = BaseDao.execute(conn,sql,params,pstmt,rs);
            while (rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                list.add(role);
            }
            BaseDao.close(null,pstmt,rs);
        }
        return list;
    }
}
