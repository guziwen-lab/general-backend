package com.supermap.modules.api.vo;

import com.supermap.modules.api.entity.TestEntity;
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
public class TestVO extends TestEntity {

}
