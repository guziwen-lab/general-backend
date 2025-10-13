package com.supermap.common.valid.validator;

import cn.hutool.core.util.IdcardUtil;
import com.supermap.common.util.StringUtils;
import com.supermap.common.valid.annotation.IdentityCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 身份证号校验处理类
 *
 * @author gzw
 */
public class IdentityCodeValidator implements ConstraintValidator<IdentityCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value))
            return true;
        return IdcardUtil.isValidCard(value);
    }

    @Override
    public void initialize(IdentityCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

}
