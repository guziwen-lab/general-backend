package com.supermap.shiro;

import com.supermap.modules.sys.vo.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author gzw
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class LoginUser extends UserVO {

    private Set<String> permissions = new HashSet<>();

    private Set<String> roles = new HashSet<>();

    private Set<String> routes = new HashSet<>();

    private Set<String> buttons = new HashSet<>();

}
