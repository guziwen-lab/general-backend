package com.supermap.shiro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.modules.sys.vo.UserVO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author gzw
 */
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LoginUser extends UserVO implements AuthorizationInfo {

    private Set<String> stringPermissions = new HashSet<>();

    @JsonIgnore
    private Set<Permission> objectPermissions = new HashSet<>();

    private Set<String> roles = new HashSet<>();

    @Getter
    private Set<String> routes = new HashSet<>();

    @Getter
    private Set<String> buttons = new HashSet<>();

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
