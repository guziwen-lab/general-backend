package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.common.constant.MenuType;
import com.supermap.common.util.BeanUtils;
import com.supermap.common.util.StringUtils;
import com.supermap.modules.sys.dao.UserDao;
import com.supermap.modules.sys.dto.UserDTO;
import com.supermap.modules.sys.dto.UserSaveDTO;
import com.supermap.modules.sys.entity.PermissionEntity;
import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.modules.sys.service.PermissionService;
import com.supermap.modules.sys.service.UserService;
import com.supermap.modules.sys.vo.UserVO;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import com.supermap.shiro.encoder.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final PermissionService permissionService;

    private final RoleServiceImpl roleService;

    @Override
    public Page<UserVO> queryPage(UserDTO dto) {
        return baseMapper.page(dto.page(), dto);
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
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userEntity.setCreateTime(now);
        userEntity.setUpdateTime(now);
        BeanUtils.copyProperties(dto, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        try {
            save(userEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("用户名已存在");
        }
    }

    @Override
    public void updateDTO(UserSaveDTO dto) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(dto, userEntity);
        if (StringUtils.isNotBlank(dto.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            userEntity.setPassword(null);
        }
        userEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        try {
            updateById(userEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 更新登录缓存
        LoginUser loginUser = LoginUserContextHandler.getLoginUser();
        userEntity = getById(dto.getUserId());
        LoginUser loginUserPermsInfo = getLoginUserPermsInfo(userEntity.getUserId());
        BeanUtils.copyProperties(userEntity, loginUserPermsInfo);
        loginUserPermsInfo.setToken(loginUser.getToken());
        LoginUserContextHandler.refreshLoginUser(loginUserPermsInfo);
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

    @Override
    public LoginUser getLoginUserPermsInfo(Long userId) {
        LoginUser loginUser = new LoginUser();

        Set<String> roleNames = roleService.getRoleNamesByUserId(userId);
        loginUser.setRoles(roleNames);

        Set<PermissionEntity> permissionEntities = permissionService.getByUserId(userId);
        loginUser.setPermissions(permissionEntities.stream()
                .map(PermissionEntity::getPermsKey)
                .collect(Collectors.toSet()));
        loginUser.setRoutes(permissionEntities.stream()
                .filter(permissionEntity -> Objects.equals(permissionEntity.getType(), MenuType.PAGE))
                .map(PermissionEntity::getPath)
                .collect(Collectors.toSet()));
        loginUser.setButtons(permissionEntities.stream()
                .filter(permissionEntity -> Objects.equals(permissionEntity.getType(), MenuType.BUTTON))
                .map(PermissionEntity::getPermsKey)
                .collect(Collectors.toSet()));

        return loginUser;
    }

}