package com.example.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "USER-001", "이미 등록된 유저입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER-002", "해당 유저를 찾을 수 없습니다."),
    MISMATCH(HttpStatus.BAD_REQUEST, "USER-003", "입력값이 일치하지 않습니다."),
    POLICY_VIOLATION(HttpStatus.UNPROCESSABLE_CONTENT, "USER-004", "정책상 허용되지 않습니다."),
    STORE_ALREADY_EXIST(HttpStatus.CONFLICT, "STORE-001", "이미 등록된 가게입니다."),
    STORE_NOT_FOUND(HttpStatus.BAD_REQUEST, "STORE-002", "해당 가게를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESERVATION-001", "해당 예약을 찾을 수 없습니다."),
    RESERVATION_UNAVAILABLE_TIME(HttpStatus.BAD_REQUEST, "RESERVATION-002", "해당 시간은 예약 불가합니다."),
    RESERVATION_FULL_TIME(HttpStatus.CONFLICT, "RESERVATION-003", "해당 시간은 예약이 마감되었습니다."),
    TABLE_ALREADY_EXIST(HttpStatus.CONFLICT, "TABLE-001", "이미 등록된 테이블 형식입니다."),
    TABLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TABLE-002", "해당 테이블을 찾을 수 없습니다."),
    DUPLICATE_TABLE_INPUT(HttpStatus.BAD_REQUEST, "TABLE-003", "동일한 테이블 형식이 중복 입력되었습니다."),
    TABLE_DELETE_RESERVATION_EXIST(HttpStatus.CONFLICT, "TABLE-004", "해당 테이블은 삭제할 수 없습니다."),
    TABLE_UPDATE_RESERVATION_EXIST(HttpStatus.CONFLICT, "TABLE-005", "해당 테이블의 수량을 변경할 수 없습니다."),
    MENU_ALREADY_EXIST(HttpStatus.CONFLICT, "MENU-001", "이미 등록된 메뉴입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-001", "권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
