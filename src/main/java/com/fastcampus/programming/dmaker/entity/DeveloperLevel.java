package com.fastcampus.programming.dmaker.entity;

import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;

@Getter
@AllArgsConstructor
public enum DeveloperLevel {
    NEW("신입 개발자", years -> years == 0),
    JUNIOR("주니어 개발자", years -> years <= MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자", years -> years > MAX_JUNIOR_EXPERIENCE_YEARS && years < MIN_SENIOR_EXPERIENCE_YEARS),
    SENIOR("시니어 개발자", years -> years >= MIN_SENIOR_EXPERIENCE_YEARS);

    private final String description;
    private final Function<Integer, Boolean> validateExperienceYears;

    public void validateExperienceYears(Integer years) {
        if (!validateExperienceYears.apply(years)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

}
