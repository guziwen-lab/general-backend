package com.supermap.modules.api.dto;

import com.supermap.common.valid.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * api测试
 *
 * @author gzw
 */
@Schema(title = "api测试")
@Data
public class TestSaveDTO {

	@NotNull(groups = Update.class)
	@Schema(title = "id")
	private Long id;

	@Schema(title = "名称")
	private String name;

	@Schema(title = "对象")
	private Object obj;

	@Schema(title = "对象数组")
	private Object objArray;

}
