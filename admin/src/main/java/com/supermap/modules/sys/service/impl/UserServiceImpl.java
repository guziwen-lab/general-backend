package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.common.util.BeanUtils;
import com.supermap.common.util.StringUtils;
import com.supermap.modules.sys.dao.UserDao;
import com.supermap.modules.sys.dto.UserDTO;
import com.supermap.modules.sys.dto.UserSaveDTO;
import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.modules.sys.service.UserService;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import com.supermap.shiro.encoder.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service("userService")
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserEntity> queryPage(UserDTO dto) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>(UserEntity.class);
        wrapper.like(StringUtils.isNotEmpty(dto.getUsername()), UserEntity::getUsername, dto.getUsername());
        wrapper.ge(dto.getStartTime() != null, UserEntity::getCreateTime, dto.getStartTime());
        wrapper.le(dto.getEndTime() != null, UserEntity::getCreateTime, dto.getEndTime());
        return page(dto.page(), wrapper);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<>(UserEntity.class).eq(UserEntity::getUsername, username));
    }

    @Override
    public void saveDTO(UserSaveDTO dto) {
        UserEntity username = getByUsername(dto.getUsername());
        if (username != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(dto, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        save(userEntity);
    }

    @Override
    public void updateLoginUser(UserSaveDTO dto) {
        UserEntity userEntity = new UserEntity();

        LoginUser loginUser = LoginUserContextHandler.getLoginUser();
        userEntity.setUserId(loginUser.getUserId());
        if (StringUtils.isNotBlank(dto.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        updateById(userEntity);
    }

    @Override
    public void reset(String username) {
        UserEntity user = getByUsername(username);
        if (user == null)
            throw new IllegalArgumentException("用户不存在");

        UserEntity update = new UserEntity();
        update.setUserId(user.getUserId());
        update.setPassword("123456");
        update.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        updateById(update);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetAll() {
        List<UserEntity> updateList = new ArrayList<>();

        List<UserEntity> list = list();
        for (UserEntity userEntity : list) {
            UserEntity update = new UserEntity();
            update.setUserId(userEntity.getUserId());
            update.setPassword("123456");
            update.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            updateList.add(update);
        }

        updateBatchById(updateList);
    }

}