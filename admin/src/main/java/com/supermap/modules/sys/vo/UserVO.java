package com.supermap.modules.sys.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.modules.sys.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * 用户表VO
 *
 * @author gzw
 */
@EqualsAndHashCode(callSuper = true)
@Schema(title = "用户表VO")
@Data
public class UserVO extends UserEntity {

	/**
	 * token
	 */
	@Schema(title = "token")
	private String token;

	@JsonIgnore
	@Schema(title = "密码")
	private String password;

}
