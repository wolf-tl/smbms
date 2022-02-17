package com.tl.smbms.service.roler;

import com.tl.smbms.pojo.Role;

import java.util.List;

/**
 * @author tl
 * 专注于角色业务
 */
public interface RolerService {
    //获取角色列表
    public List<Role> getRoleList();
}
