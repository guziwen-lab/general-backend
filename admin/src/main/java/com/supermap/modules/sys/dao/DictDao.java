package com.supermap.modules.sys.dao;

import com.supermap.modules.sys.entity.DictEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表
 * 
 * @author gzw
 */
@Mapper
public interface DictDao extends BaseMapper<DictEntity> {
	
}
