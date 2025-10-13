package com.supermap.modules.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.common.util.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.supermap.modules.api.dao.TestDao;
import com.supermap.modules.api.entity.TestEntity;
import com.supermap.modules.api.service.TestService;
import com.supermap.modules.api.dto.TestDTO;
import com.supermap.modules.api.dto.TestSaveDTO;

@Service("testService")
public class TestServiceImpl extends ServiceImpl<TestDao, TestEntity> implements TestService {

    @Override
    public Page<TestEntity> queryPage(TestDTO dto) {
        LambdaQueryWrapper<TestEntity> wrapper = new LambdaQueryWrapper<>();
        return page(dto.page(), wrapper);
    }

    @Override
    public Long saveDTO(TestSaveDTO dto) {
        TestEntity testEntity = new TestEntity();
        BeanUtils.copyProperties(dto, testEntity);
        save(testEntity);
        return testEntity.getId();
    }

    @Override
    public void updateDTOById(TestSaveDTO dto) {
        TestEntity testEntity = new TestEntity();
        BeanUtils.copyProperties(dto, testEntity);
        updateById(testEntity);
    }

}