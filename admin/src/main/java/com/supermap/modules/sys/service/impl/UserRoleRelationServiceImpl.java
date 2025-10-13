package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.modules.sys.dao.UserRoleRelationDao;
import com.supermap.modules.sys.entity.UserRoleRelationEntity;
import com.supermap.modules.sys.service.UserRoleRelationService;
import org.springframework.stereotype.Service;

@Service("userRoleRelationService")
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationDao, UserRoleRelationEntity> implements UserRoleRelationService {

}