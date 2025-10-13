package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.modules.sys.dao.RolePermissionRelationDao;
import com.supermap.modules.sys.entity.RolePermissionRelationEntity;
import com.supermap.modules.sys.service.RolePermissionRelationService;
import org.springframework.stereotype.Service;

@Service("rolePermissionRelationService")
public class RolePermissionRelationServiceImpl extends ServiceImpl<RolePermissionRelationDao, RolePermissionRelationEntity> implements RolePermissionRelationService {

}