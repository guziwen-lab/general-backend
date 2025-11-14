package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supermap.common.util.BeanUtils;
import com.supermap.common.util.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.supermap.modules.sys.dao.LoginLogDao;
import com.supermap.modules.sys.entity.LoginLogEntity;
import com.supermap.modules.sys.service.LoginLogService;
import com.supermap.modules.sys.dto.LoginLogDTO;
import com.supermap.modules.sys.dto.LoginLogSaveDTO;

@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogDao, LoginLogEntity> implements LoginLogService {

    @Override
    public Page<LoginLogEntity> queryPage(LoginLogDTO dto) {
        LambdaQueryWrapper<LoginLogEntity> wrapper = new LambdaQueryWrapper<>();
        return page(dto.page(), wrapper);
    }

    @Override
    public Long saveDTO(LoginLogSaveDTO dto) {
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        BeanUtils.copyProperties(dto, loginLogEntity);
        save(loginLogEntity);
        return loginLogEntity.getId();
    }

    @Override
    public void updateDTOById(LoginLogSaveDTO dto) {
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        BeanUtils.copyProperties(dto, loginLogEntity);
        updateById(loginLogEntity);
    }

    @Override
    public void updateByToken(LoginLogEntity loginLogEntity) {
        String token = loginLogEntity.getToken();
        if (StringUtils.isEmpty(token))
            throw new RuntimeException("token不能为空");

        update(loginLogEntity, new LambdaQueryWrapper<LoginLogEntity>().eq(LoginLogEntity::getToken, token));
    }

    @Override
    public LoginLogEntity getByToken(String token) {
        return getOne(new LambdaQueryWrapper<LoginLogEntity>().eq(LoginLogEntity::getToken, token));
    }

}