package com.supermap.modules.sys.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 用户表
 *
 * @author gzw
 */
@Schema(title = "用户表")
@Data
public class UserVO {

	/**
	 * token
	 */
	@Schema(title = "token")
	private String token;

	@Schema(title = "用户id")
	private Long userId;

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
