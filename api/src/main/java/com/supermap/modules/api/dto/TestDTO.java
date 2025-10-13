package com.supermap.modules.api.dto;

import java.sql.Timestamp;
import com.supermap.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * api测试
 *
 * @author gzw
 */
@EqualsAndHashCode(callSuper = true)
@Schema(title = "api测试")
@Data
public class TestDTO extends PageParam {

	@Schema(title = "开始时间")
	private Timestamp startTime;

	@Schema(title = "结束时间")
	private Timestamp endTime;

	@Schema(title = "id")
	private Long id;

	@Schema(title = "名称")
	private String name;

	@Schema(title = "对象")
	private Object obj;

	@Schema(title = "对象数组")
	private Object objArray;

}
