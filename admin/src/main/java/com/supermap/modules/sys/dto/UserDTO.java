package com.supermap.modules.sys.dto;

import com.supermap.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * 用户表
 *
 * @author gzw
 */
@EqualsAndHashCode(callSuper = true)
@Schema(title = "用户表")
@Data
public class UserDTO extends PageParam {

	@Schema(title = "开始时间")
	private Timestamp startTime;

	@Schema(title = "结束时间")
	private Timestamp endTime;

	@Schema(title = "用户id")
	private Long userId;

	@Schema(title = "用户名")
	private String username;

	@Schema(title = "密码")
	private String password;

	@Schema(title = "昵称")
	private String nickname;

}
