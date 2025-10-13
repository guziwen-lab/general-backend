package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.modules.sys.dao.RoleDao;
import com.supermap.modules.sys.dto.RoleDTO;
import com.supermap.modules.sys.entity.RoleEntity;
import com.supermap.modules.sys.service.RoleService;
import org.springframework.stereotype.Service;

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

}