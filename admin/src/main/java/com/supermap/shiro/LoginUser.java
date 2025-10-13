package com.supermap.shiro;

import com.supermap.common.util.BeanUtils;
import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.modules.sys.vo.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gzw
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class LoginUser extends UserVO {

    // 权限信息的集合
    private Set<String> permissions = new HashSet<>();

    // 角色信息的集合
    private Set<String> roles = new HashSet<>();

    public LoginUser(@Nullable Set<String> permissions, @Nullable Set<String> roles) {
        if (permissions != null)
            this.permissions.addAll(permissions);
        if (roles != null)
            this.roles.addAll(roles);
    }

    public LoginUser(UserEntity userEntity, @Nullable Set<String> permissions, @Nullable Set<String> roles) {
        BeanUtils.copyProperties(userEntity, this);
        if (permissions != null)
            this.permissions.addAll(permissions);
        if (roles != null)
            this.roles.addAll(roles);
    }

    public void addPermission(String... permission) {
        this.permissions.addAll(Arrays.asList(permission));
    }

    public void addRoles(String... roles) {
        this.roles.addAll(Arrays.asList(roles));
    }

}
