package com.supermap.modules.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Timestamp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 全局日志表
 *
 * @author gzw
 */
@Schema(title = "全局日志表")
@Data
@TableName("log_global")
public class GlobalEntity {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@Schema(title = "主键")
	private Long id;

	@Schema(title = "类名")
	private String className;

	@Schema(title = "方法名")
	private String methodName;

	@Schema(title = "参数")
	private String param;

	@Schema(title = "结果")
	private String result;

	@Schema(title = "耗时")
	private Long take;

	@Schema(title = "错误信息")
	private String errorMsg;

	@Schema(title = "异常")
	private String exception;

	@Schema(title = "请求用户")
	private Long requestUser;

	@Schema(title = "创建时间")
	private Timestamp createTime;

}
