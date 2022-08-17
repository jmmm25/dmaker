package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.*;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(@PathVariable final String memberId) {
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(@Valid @RequestBody CreateDeveloper.Request request) {
        log.info("request : {}", request);
        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/developers/{memberId}")
    public DeveloperDetailDto editDeveloper(@PathVariable final String memberId,
                                            @Valid @RequestBody EditDeveloper.Request request) {
        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developers/{memberId}")
    public DeveloperDetailDto deleteDeveloper(@PathVariable final String memberId) {
        return dMakerService.deleteDeveloper(memberId);
    }

    // 예전에는 Controller 자체에서 Exception Handler를 구성
    // 요즘에는 RestController 전체의 @RestControllerAdvice를 구성
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(DMakerException.class)
//    public DMakerResponse handleException(DMakerException e,
//                                          HttpServletRequest request) {
//        log.error("errorCode : {}, url : {}, message: {}",
//                e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());
//        return DMakerResponse.builder()
//                .errorCode(e.getDMakerErrorCode())
//                .errorMessage(e.getDetailMessage())
//                .build();
//    }

}
