package com.example.UA.Validation;

import com.example.UA.repository.AccountRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueValidator implements ConstraintValidator<Unique, String> {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public void initialize(Unique annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //valueには、アノテーションを付与したフィールドの項目値が設定される
        //value = account　のこと
        //Repositoryでaccountを引数にDBを検索し、データが見つからなければ一意とする
        if (accountRepository.findByAccount(value) == null) {
            return true;
        }
        return false;
    }
}