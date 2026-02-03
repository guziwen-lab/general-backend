package com.supermap.shiro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.modules.sys.entity.DepartmentEntity;
import com.supermap.modules.sys.entity.PermissionEntity;
import com.supermap.modules.sys.entity.RoleEntity;
import com.supermap.modules.sys.entity.UserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gzw
 */
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LoginUser extends UserEntity implements AuthorizationInfo {

    @Getter
    private String token;

    @Getter
    @JsonIgnore
    private String password;

    private Set<String> stringPermissions = new HashSet<>();

    @Getter
    private Set<String> permissionNames = new HashSet<>();

    @Getter
    private Collection<PermissionEntity> permissionEntities = new HashSet<>();

    @JsonIgnore
    private Set<Permission> objectPermissions = new HashSet<>();

    private Set<String> roles = new HashSet<>();

    @Getter
    private Collection<RoleEntity> roleEntities = new HashSet<>();

    @Getter
    private Set<String> buttons = new HashSet<>();

    @Getter
    private Set<String> departments = new HashSet<>();

    @Getter
    private Collection<DepartmentEntity> departmentEntities = new HashSet<>();

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public Set<String> getStringPermissions() {
        return stringPermissions;
    }

    @Override
    public Set<Permission> getObjectPermissions() {
        return objectPermissions;
    }

}
