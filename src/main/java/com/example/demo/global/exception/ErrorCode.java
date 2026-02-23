package com.example.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "USER-001", "이미 등록된 유저입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER-002", "해당 유저를 찾을 수 없습니다."),
    STORE_ALREADY_EXIST(HttpStatus.CONFLICT, "STORE-001", "이미 등록된 가게입니다."),
    STORE_NOT_FOUND(HttpStatus.BAD_REQUEST, "STORE-002", "해당 가게를 찾을 수 없습니다."),
    TABLE_ALREADY_EXIST(HttpStatus.CONFLICT, "TABLE-001", "이미 등록된 테이블 형식입니다."),
    TABLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TABLE-002", "해당 테이블을 찾을 수 없습니다."),
    DUPLICATE_TABLE_INPUT(HttpStatus.BAD_REQUEST, "TABLE-003", "동일한 테이블 형식이 중복 입력되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
