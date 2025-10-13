package com.supermap.modules.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.common.util.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.supermap.modules.log.dao.GlobalDao;
import com.supermap.modules.log.entity.GlobalEntity;
import com.supermap.modules.log.service.GlobalService;
import com.supermap.modules.log.dto.GlobalDTO;
import com.supermap.modules.log.dto.GlobalSaveDTO;

@Service("globalService")
public class GlobalServiceImpl extends ServiceImpl<GlobalDao, GlobalEntity> implements GlobalService {

    @Override
    public Page<GlobalEntity> queryPage(GlobalDTO dto) {
        LambdaQueryWrapper<GlobalEntity> wrapper = new LambdaQueryWrapper<>();
        return page(dto.page(), wrapper);
    }

    @Override
    public Long saveDTO(GlobalSaveDTO dto) {
        GlobalEntity globalEntity = new GlobalEntity();
        BeanUtils.copyProperties(dto, globalEntity);
        save(globalEntity);
        return globalEntity.getId();
    }

    @Override
    public void updateDTOById(GlobalSaveDTO dto) {
        GlobalEntity globalEntity = new GlobalEntity();
        BeanUtils.copyProperties(dto, globalEntity);
        updateById(globalEntity);
    }

}