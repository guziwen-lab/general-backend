package com.supermap.shiro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gzw
 */
@Data
public class LoginUserWithoutLong {

    // 权限信息的集合
    private Set<String> permissions = new HashSet<>();

    // 角色信息的集合
    private Set<String> roles = new HashSet<>();

    @Schema(title = "用户id")
    private String userId;

    /**
     * token
     */
    @Schema(title = "token")
    private String token;

    @Schema(title = "用户名")
    private String username;

    @JsonIgnore
    @Schema(title = "密码")
    private String password;

    @Schema(title = "创建时间")
    private Timestamp createTime;

    @Schema(title = "更新时间")
    private Timestamp updateTime;

    @Schema(title = "行政区划代码")
    private String xzqdm;

    @Schema(title = "昵称")
    private String nickname;

}
