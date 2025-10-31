package com.supermap.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supermap.modules.sys.entity.UserRoleRelationEntity;

/**
 * 用户角色关系表
 *
 * @author gzw
 */
public interface UserRoleRelationService extends IService<UserRoleRelationEntity> {

    void removeByUserId(Long userId);

}

