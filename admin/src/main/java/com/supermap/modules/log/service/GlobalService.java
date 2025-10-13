package com.supermap.modules.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.modules.log.entity.GlobalEntity;
import com.supermap.modules.log.dto.GlobalDTO;
import com.supermap.modules.log.dto.GlobalSaveDTO;

/**
 * 全局日志表
 *
 * @author gzw
 */
public interface GlobalService extends IService<GlobalEntity> {

    Page<GlobalEntity> queryPage(GlobalDTO dto);

    Long saveDTO(GlobalSaveDTO dto);

    void updateDTOById(GlobalSaveDTO dto);

}

