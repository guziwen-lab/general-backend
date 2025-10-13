package com.supermap.modules.api.dao;

import com.supermap.modules.api.entity.TestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * api测试
 * 
 * @author gzw
 */
@Mapper
public interface TestDao extends BaseMapper<TestEntity> {
	
}
