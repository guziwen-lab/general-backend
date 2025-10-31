package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.common.util.BeanUtils;
import com.supermap.modules.sys.dao.RoleDao;
import com.supermap.modules.sys.dto.RoleDTO;
import com.supermap.modules.sys.dto.RoleSaveDTO;
import com.supermap.modules.sys.entity.RoleEntity;
import com.supermap.modules.sys.service.RoleService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    @Override
    public Page<RoleEntity> queryPage(RoleDTO dto) {
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>(RoleEntity.class);
        return page(dto.page(), wrapper);
    }

    @Override
    public Set<String> getRoleNamesByUserId(Long userId) {
        return baseMapper.getRolesByUserId(userId);
    }

    @Override
    public void saveDTO(RoleSaveDTO dto) {
        RoleEntity roleName = getByRoleName(dto.getRoleName());
        if (roleName != null) {
            throw new IllegalArgumentException("角色名已存在");
        }

        RoleEntity roleEntity = new RoleEntity();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        roleEntity.setCreateTime(now);
        roleEntity.setUpdateTime(now);
        BeanUtils.copyProperties(dto, roleEntity);

        try {
            save(roleEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("角色名已存在");
        }
    }

    @Override
    public void updateDTO(RoleSaveDTO dto) {
        RoleEntity roleEntity = new RoleEntity();
        BeanUtils.copyProperties(dto, roleEntity);
        roleEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        updateById(roleEntity);
    }

    @Override
    public List<RoleEntity> all() {
        return list(new LambdaQueryWrapper<>(RoleEntity.class)
                .orderByDesc(RoleEntity::getCreateTime));
    }

    private RoleEntity getByRoleName(String roleName) {
        return getOne(new LambdaQueryWrapper<>(RoleEntity.class)
                .eq(RoleEntity::getRoleName, roleName));
    }

}