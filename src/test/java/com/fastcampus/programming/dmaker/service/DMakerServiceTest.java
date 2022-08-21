package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.constant.DMakerConstant;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.entity.DeveloperLevel;
import com.fastcampus.programming.dmaker.entity.DeveloperSkillType;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith({MockitoExtension.class})
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(DeveloperLevel.SENIOR)
            .developerSkillType(DeveloperSkillType.BACK_END)
            .experienceYears(12)
            .memberId("memberId")
            .name("name")
            .age(32)
            .build();

    private CreateDeveloper.Request getCreateRequest(DeveloperLevel developerLevel,
                                                     DeveloperSkillType developerSkillType,
                                                     Integer experienceYears) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .memberId("memberId")
                .name("name")
                .age(32)
                .build();
    }

    @Test
    public void testSomething() {
        // given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        // when
        DeveloperDetailDto developerDetailDto = dMakerService.getDeveloperDetail("memberId");

        // then
        assertEquals(DeveloperLevel.SENIOR, developerDetailDto.getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, developerDetailDto.getDeveloperSkillType());
        assertEquals(12, developerDetailDto.getExperienceYears());
    }


    @Test
    void createDeveloperTest_success() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        // when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(
                getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS));

        // then
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();
        assertEquals(developer.getDeveloperLevel(), savedDeveloper.getDeveloperLevel());
        assertEquals(developer.getDeveloperSkillType(), savedDeveloper.getDeveloperSkillType());
        assertEquals(developer.getExperienceYears(), savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        // when
        // then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END,
                                DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS)));


        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

    @Test
    void createDeveloperTest_fail_low_senior() {
        // when
        // then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END,
                                DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS - 1)));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());
    }

    @Test
    void createDeveloperTest_fail_with_unmatched_level() {
        // when
        // then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(DeveloperLevel.JUNIOR, DeveloperSkillType.FRONT_END,
                                DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS + 1)));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());


        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(DeveloperLevel.JUNGNIOR, DeveloperSkillType.FRONT_END,
                                DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS + 1)));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END,
                                DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS - 1)));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());
    }

}