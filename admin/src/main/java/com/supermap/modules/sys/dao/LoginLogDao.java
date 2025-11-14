package com.supermap.modules.sys.dao;

import com.supermap.modules.sys.entity.LoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志
 * 
 * @author gzw
 */
@Mapper
public interface LoginLogDao extends BaseMapper<LoginLogEntity> {
	
}
