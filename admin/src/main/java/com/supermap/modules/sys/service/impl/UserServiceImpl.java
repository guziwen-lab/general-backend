package com.supermap.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supermap.common.constant.MenuType;
import com.supermap.common.util.BeanUtils;
import com.supermap.common.util.CollectionUtils;
import com.supermap.common.util.StringUtils;
import com.supermap.modules.sys.dao.UserDao;
import com.supermap.modules.sys.dto.UserDTO;
import com.supermap.modules.sys.dto.UserSaveDTO;
import com.supermap.modules.sys.entity.PermissionEntity;
import com.supermap.modules.sys.entity.RoleEntity;
import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.modules.sys.entity.UserRoleRelationEntity;
import com.supermap.modules.sys.service.FileService;
import com.supermap.modules.sys.service.PermissionService;
import com.supermap.modules.sys.service.UserRoleRelationService;
import com.supermap.modules.sys.service.UserService;
import com.supermap.modules.sys.vo.UserVO;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import com.supermap.shiro.encoder.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service("userService")
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final PermissionService permissionService;

    private final RoleServiceImpl roleService;

    private final UserRoleRelationService userRoleRelationService;

    private final FileService fileService;

    @Override
    public Page<UserVO> queryPage(UserDTO dto) {
        return baseMapper.page(dto.page(), dto);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<>(UserEntity.class).eq(UserEntity::getUsername, username));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveDTO(UserSaveDTO dto) {
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

        saveRoleByUserId(userEntity.getUserId(), dto.getRoleIds());

        if (userEntity.getAvatar() != null)
            fileService.increaseRefCount(userEntity.getAvatar());

        return userEntity.getUserId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDTO(UserSaveDTO dto) {
        UserEntity user = getById(dto.getUserId());
        if (user == null)
            throw new IllegalArgumentException("用户不存在");

        UserEntity update = new UserEntity();
        BeanUtils.copyProperties(dto, update);
        if (StringUtils.isNotBlank(dto.getPassword())) {
            update.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            update.setPassword(null);
        }
        update.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        try {
            updateById(update);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("用户名已存在");
        }

        saveRoleByUserId(dto.getUserId(), dto.getRoleIds());

        if (update.getAvatar() != null && !Objects.equals(user.getAvatar(), update.getAvatar())) {
            fileService.increaseRefCount(update.getAvatar());
            fileService.decreaseRefCount(user.getAvatar());
        }
    }

    private void saveRoleByUserId(Long userId, List<Long> roleIds) {
        userRoleRelationService.removeByUserId(userId);

        if (CollectionUtils.isEmpty(roleIds))
            return;

        long count = roleService.count(new LambdaQueryWrapper<RoleEntity>()
                .in(RoleEntity::getRoleId, roleIds));
        if (count != roleIds.size())
            throw new IllegalArgumentException("角色不存在");

        List<UserRoleRelationEntity> relationEntities = roleIds.stream()
                .map(item -> {
                    UserRoleRelationEntity userRoleRelationEntity = new UserRoleRelationEntity();
                    userRoleRelationEntity.setUserId(userId);
                    userRoleRelationEntity.setRoleId(item);
                    return userRoleRelationEntity;
                }).toList();
        userRoleRelationService.saveBatch(relationEntities);
    }

    public LoginUser refreshLoginUser(Long userId) {
        LoginUser loginUser = LoginUserContextHandler.getLoginUser();

        UserEntity userEntity = getById(userId);
        BeanUtils.copyProperties(userEntity, loginUser);
        setLoginUserPermsInfo(loginUser);

        LoginUserContextHandler.refreshLoginUser(loginUser);

        return loginUser;
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
    public void setLoginUserPermsInfo(LoginUser loginUser) {
        Set<String> roleNames = roleService.getRoleNamesByUserId(loginUser.getUserId());
        loginUser.setRoles(roleNames);

        Set<PermissionEntity> permissionEntities = permissionService.getByUserId(loginUser.getUserId());
        for (PermissionEntity pe : permissionEntities) {
            Integer type = pe.getType();

            if (StringUtils.isNotBlank(pe.getPermsKey())) {
                String permsKey = pe.getPermsKey().trim();

                loginUser.getStringPermissions().add(permsKey);
                // 使用 WildcardPermission；若 permsKey 格式不合法
                try {
                    loginUser.getObjectPermissions().add(new WildcardPermission(permsKey));
                } catch (Exception ignored) {
                }

                if (type != null && type == MenuType.BUTTON) {
                    loginUser.getButtons().add(permsKey);
                }
            }

            if (type != null && type == MenuType.CATALOG) {
                loginUser.getRoutes().add(pe.getUrl().trim());
            }
        }
    }

    @Override
    public UserVO getUserVO(Long userId) {
        UserVO userVO = new UserVO();
        UserEntity userEntity = getById(userId);
        BeanUtils.copyProperties(userEntity, userVO);

        List<RoleEntity> roles = roleService.getByUserId(userId);
        userVO.setRoleEntities(roles);

        return userVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Long> userIds) {
        removeByIds(userIds);
        userRoleRelationService.removeByUserIds(userIds);
    }

}