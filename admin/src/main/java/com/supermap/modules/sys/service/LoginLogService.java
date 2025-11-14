package com.supermap.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.modules.sys.entity.LoginLogEntity;
import com.supermap.modules.sys.dto.LoginLogDTO;
import com.supermap.modules.sys.dto.LoginLogSaveDTO;

/**
 * 登录日志
 *
 * @author gzw
 */
public interface LoginLogService extends IService<LoginLogEntity> {

    Page<LoginLogEntity> queryPage(LoginLogDTO dto);

    Long saveDTO(LoginLogSaveDTO dto);

    void updateDTOById(LoginLogSaveDTO dto);

    void updateByToken(LoginLogEntity loginLogEntity);

    LoginLogEntity getByToken(String token);

}

