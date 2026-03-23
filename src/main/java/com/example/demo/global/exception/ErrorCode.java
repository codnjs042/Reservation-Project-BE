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
    SCHEDULE_LOCKED(HttpStatus.BAD_REQUEST, "SCHEDULE-001", "해당 시간대에 예약이 배정되어 스케줄 관리가 제한됩니다."),
    INVALID_SCHEDULE_TIME(HttpStatus.BAD_REQUEST, "SCHEDULE-002", "유효하지 않은 입력입니다. 시간대는 서로 겹치게 설정할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESERVATION-001", "해당 예약을 찾을 수 없습니다."),
    RESERVATION_GROUP_LIMIT(HttpStatus.BAD_REQUEST, "RESERVATION-002", "해당 인원은 전화로 문의해주세요."),
    RESERVATION_UNAVAILABLE_TIME(HttpStatus.BAD_REQUEST, "RESERVATION-003", "해당 시간은 예약 불가합니다."),
    RESERVATION_FULL_TIME(HttpStatus.CONFLICT, "RESERVATION-004", "해당 시간은 예약이 마감되었습니다."),
    INVALID_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "RESERVATION-005", "현재는 예약 상태 변경이 불가능합니다."),
    TABLE_ALREADY_EXIST(HttpStatus.CONFLICT, "TABLE-001", "이미 등록된 테이블 형식입니다."),
    TABLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TABLE-002", "해당 테이블을 찾을 수 없습니다."),
    DUPLICATE_TABLE_INPUT(HttpStatus.BAD_REQUEST, "TABLE-003", "동일한 테이블 형식이 중복 입력되었습니다."),
    TABLE_LOCKED(HttpStatus.CONFLICT, "TABLE-004", "해당 테이블에 예약이 배정되어 테이블 관리가 제한됩니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-002", "권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
