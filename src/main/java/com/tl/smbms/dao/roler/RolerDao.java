package com.tl.smbms.dao.roler;

import com.tl.smbms.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author tl
 * 专注于角色Dao
 */
public interface RolerDao {
    public List<Role> getRoleList(Connection conn) throws SQLException;
}
