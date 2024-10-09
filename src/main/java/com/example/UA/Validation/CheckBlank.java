package com.example.UA.Validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Targetはアノテーションを適用する場所を示す。今回はフィールドに適用。
@Target({ElementType.FIELD})
// @Retentionはアノテーションを保持する範囲。今回はアノテーションを実行時も保持する。
@Retention(RetentionPolicy.RUNTIME)
// @Constraintは制約(チェック)したい具体的なロジックを記述したクラスを指定。
@Constraint(validatedBy={CheckBlankValidator.class})
public @interface CheckBlank {
    String message() default "";
    //特定のバリデーショングループがカスタマイズできるような設定
    Class<?>[] groups() default {};
    //チェック対象のオブジェクトになんらかのメタ情報を与えるためだけの宣言
    Class<? extends Payload>[] payload() default {};
}