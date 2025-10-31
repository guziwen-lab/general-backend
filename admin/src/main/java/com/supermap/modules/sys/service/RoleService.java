package com.supermap.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supermap.modules.sys.dto.RoleDTO;
import com.supermap.modules.sys.dto.RoleSaveDTO;
import com.supermap.modules.sys.entity.RoleEntity;

import java.util.List;
import java.util.Set;

/**
 * 角色表
 *
 * @author gzw
 */
public interface RoleService extends IService<RoleEntity> {

    Page<RoleEntity> queryPage(RoleDTO dto);

    Set<String> getRoleNamesByUserId(Long userId);

    void saveDTO(RoleSaveDTO dto);

    void updateDTO(RoleSaveDTO dto);

    List<RoleEntity> all();

}

