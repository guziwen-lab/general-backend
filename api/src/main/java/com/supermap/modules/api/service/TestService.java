package com.supermap.modules.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.modules.api.entity.TestEntity;
import com.supermap.modules.api.dto.TestDTO;
import com.supermap.modules.api.dto.TestSaveDTO;

/**
 * api测试
 *
 * @author gzw
 */
public interface TestService extends IService<TestEntity> {

    Page<TestEntity> queryPage(TestDTO dto);

    Long saveDTO(TestSaveDTO dto);

    void updateDTOById(TestSaveDTO dto);

}

