package com.example.UA.Validation;

import org.apache.commons.lang3.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckBlankValidator implements ConstraintValidator<CheckBlank, String> {
    @Override
    public void initialize(CheckBlank annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //valueには、アノテーションを付与したフィールドの項目値が設定される
        //valueが空白値か、全て全角文字の場合はfalse返すチェック処理を行う
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return true;
    }
}
