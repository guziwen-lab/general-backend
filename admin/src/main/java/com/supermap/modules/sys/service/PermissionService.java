package com.supermap.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supermap.modules.sys.dto.PermissionDTO;
import com.supermap.modules.sys.dto.PermissionSaveDTO;
import com.supermap.modules.sys.entity.PermissionEntity;

import java.util.List;
import java.util.Set;

/**
 * 权限表
 *
 * @author gzw
 */
public interface PermissionService extends IService<PermissionEntity> {

    Page<PermissionEntity> queryPage(PermissionDTO dto);

    List<PermissionEntity> getByRoleId(Long roleId);

    Set<String> getPermsKeysByUserId(Long userId);

    Long saveDTO(PermissionSaveDTO dto);

    void updateDTOById(PermissionSaveDTO dto);

}

