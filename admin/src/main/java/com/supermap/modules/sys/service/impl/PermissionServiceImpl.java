package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.common.util.BeanUtils;
import com.supermap.modules.sys.dao.PermissionDao;
import com.supermap.modules.sys.dto.PermissionDTO;
import com.supermap.modules.sys.dto.PermissionSaveDTO;
import com.supermap.modules.sys.entity.PermissionEntity;
import com.supermap.modules.sys.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, PermissionEntity> implements PermissionService {

    @Override
    public Page<PermissionEntity> queryPage(PermissionDTO dto) {
        LambdaQueryWrapper<PermissionEntity> wrapper = new LambdaQueryWrapper<>(PermissionEntity.class);
        return page(dto.page(), wrapper);
    }

    @Override
    public List<PermissionEntity> getByRoleId(Long roleId) {
        return baseMapper.getByRoleId(roleId);
    }

    @Override
    public Set<PermissionEntity> getByUserId(Long userId) {
        return baseMapper.getByUserId(userId);
    }

    @Override
    public Long saveDTO(PermissionSaveDTO dto) {
        PermissionEntity exists = getOne(new LambdaQueryWrapper<PermissionEntity>()
                .eq(PermissionEntity::getPermsKey, dto.getPermsKey()));
        if (exists != null) {
            throw new RuntimeException("权限key已存在");
        }

        PermissionEntity permissionEntity = new PermissionEntity();
        BeanUtils.copyProperties(dto, permissionEntity);
        save(permissionEntity);
        return permissionEntity.getPermissionId();
    }

    @Override
    public void updateDTOById(PermissionSaveDTO dto) {
        PermissionEntity permissionEntity = new PermissionEntity();
        BeanUtils.copyProperties(dto, permissionEntity);
        updateById(permissionEntity);
    }

}