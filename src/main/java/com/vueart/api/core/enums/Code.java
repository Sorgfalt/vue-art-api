package com.vueart.api.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Code {

    public enum YN {
        Y,
        N
    }

    public enum SocialLoginType {
        GOOGLE, KAKAO, NAVER
    }

    public enum ReservationStatus {
        CONFIRMED,
        CANCELLED
    }

    public enum BusinessRegisterType {
        VALID("01"),
        INVALID("02");

        private final String code;

        BusinessRegisterType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static BusinessRegisterType fromCode(String code) {
            for (BusinessRegisterType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown code: " + code);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        USER("ROLE_USER");

        private final String key;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum ApiResponseCode {
        SUCCESS(200, "S001", "success"),
        SUCCESS_SIGN_UP(201, "S002", "회원 가입이 완료되었습니다."),
        SUCCESS_SIGN_IN(200, "S003", "로그인되었습니다."),
        CREATED_EXHIBITION_INFO(201, "S004", "전시회 정보가 등록되었습니다."),
        CREATED_TICKET(201, "S005", "티켓 정보가 등록되었습니다."),
        UPDATED_EXHIBITION_INFO(201, "S005", "전시회 정보가 수정되었습니다."),
        ;
        private int status;
        private final String code;
        private final String message;

        ApiResponseCode(final int status, final String code, final String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return this.message;
        }

        public static String getCode(String code) {
            for (ApiResponseCode responseCode : ApiResponseCode.values()) {
                if (code.equals(responseCode.code)) {
                    return responseCode.getMessage();
                }
            }
            return null;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum ErrorCode {
        // Common
        BAD_REQUEST(400, "E000", "잘못된 요청 입니다."),
        INVALID_INPUT_VALUE(400, "E001", "입력 값이 잘못 되었습니다."),
        METHOD_NOT_ALLOWED(405, "E002", "허용되지 않은 요청 방식 입니다."),
        INTERNAL_SERVER_ERROR(500, "E003", "서버에서 발생한 오류입니다. 운영자에게 신고부탁드립니다."),
        INVALID_TYPE_VALUE(400, "E004", "잘못된 형식의 갑을 입력하였습니다."),
        REQUIRED_PARAMETER_IS_MISSING(400, "E005", "필수 parameter가 없습니다.다시 확인해주세요."),

        // Sign
        INVALID_JWT_SIGNATURE(401, "E006", "잘못된 JWT 서명입니다."),
        INVALID_JWT_TOKEN(401, "E007", "유효하지 않은 토큰입니다."),
        EXPIRED_JWT_TOKEN(401, "E008", "토큰 기한 만료"),
        NOT_SUPPORTED_JWT_TOKEN(401, "E009", "지원하지 않는 토큰입니다."),
        UNAUTHORIZED(401, "E010", "권한이 없습니다. 로그인이 필요합니다."),
        FORBIDDEN(403, "E011", "접근 권한이 없습니다."),
        ALREADY_REGISTERED_USER(400, "E012", "이미 가입이 되어 있습니다."),
        NOT_REGISTERED_USER(404, "E013", "가입된 회원이 아닙니다. 회원 가입을 먼저 진행해주세요."),
        NOT_FOUND_USER_ID(404, "E014", "해당 아이디를 찾을 수 없습니다."),
        CURRENT_PASSWORD_REQUIRED(404, "E014", "이전 비밀번호는 필수 입력 항목입니다."),
        NEW_PASSWORD_REQUIRED(404, "E015", "새 비밀번호는 필수 입력 항목입니다."),
        CONFIRM_PASSWORD_REQUIRED(404, "E016", "새 비밀번호 확인은 필수 입력 항목입니다."),
        PASSWORD_CONFIRMATION_MISMATCH(404, "E017", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다."),
        CURRENT_PASSWORD_INCORRECT(404, "E018", "입력한 이전 비밀번호가 현재 비밀번호와 일치하지 않습니다."),
        NEW_PASSWORD_SAME_AS_OLD(404, "E019", "새 비밀번호는 이전 비밀번호와 다르게 설정해야 합니다."),
        INVALID_PASSWORD_FORMAT(404, "E020", "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다."),
        INVALID_PASSWORD(404, "E021", "비밀번호가 일치하지 않습니다."),

        // Exception (공통, 메시지 부분은 예외 생성 시 메시지를 받아서 처리)
        COMMON_KEY_NOT_FOUND_EXCEPTION(400, "E401", "요청 바디의 키값 확인 필요 (존재하지 않는 데이터)"),
        COMMON_NOT_FOUND_EXCEPTION(400, "E404", null),
        COMMON_BAD_REQUEST_EXCEPTION(400, "E400", null),

        // Exception (Database, Spring Data)
        SQL_TIME_OUT_EXCEPTION(500, "E500", "DB 연결 요청 시간 초과."),
        SQL_EXCEPTION(500, "E501", "DB 연결 관련 예외 발생."),
        DATA_ACCESS_EXCEPTION(500, "E502", "DB 관련 예외 발생."),
        CSV_PARSING_ERROR(500, "E03", "CSV 파싱 에러"),

        // Category
        ALREADY_REGISTERED_CATEGORY(409, "CATEGORY_ALREADY_EXISTS", "이미 등록된 카테고리입니다."),
        NOT_FOUND_CATEGORY(404, "CATEGORY_404", "카테고리를 찾을 수 없습니다."),

        // Favorite Category
        NOT_FOUND_FAVORITE_CATEGORY(404, "FAVORITE_CATEGORY_404", "즐겨찾기 항목을 찾을 수 없습니다."),
        ALREADY_DELETED_FAVORITE(404, "FAVORITE_CATEGORY_ALREADY_DELETED", "이미 삭제된 즐겨찾기입니다."),
        ALREADY_REGISTERED_FAVORITE(409, "FAVORITE_CATEGORY_ALREADY_EXISTS", "이미 등록된 즐겨찾기입니다."),

        // Subscription
        NOT_FOUND_SUBSCRIBER_ID(404, "SUBSCRIBER_404", "해당 구독자를 찾을 수 없습니다."),
        NOT_FOUND_ORGANIZER_ID(404, "ORGANIZER_404", "해당 주최자를 찾을 수 없습니다."),
        NOT_FOUND_SUBSCRIPTION(404, "SUBSCRIPTION_404", "해당 구독을 찾을 수 없습니다."),
        ALREADY_REGISTERED_SUBSCRIPTION(409, "SUBSCRIPTION_ALREADY_EXISTS", "이미 등록된 구독입니다."),

        // Notification
        NOT_FOUND_NOTIFICATION(404, "NOTIFICATION_404", "알림을 찾을 수 없습니다."),

        // Ticket
        INSUFFICIENT_TICKET_QUANTITY(404, "TICKET_500", "잔여수량이 부족합니다."),

        // Exhibition
        NOT_FOUND_EXHIBITION(404, "EXHIBITION_404", "전시회를 찾을 수 없습니다.");

        private int status;
        private final String code;
        private final String message;

        ErrorCode(final int status, final String code, final String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return this.message;
        }

        public static String getCode(String code) {
            for (ErrorCode errorCode : ErrorCode.values()) {
                if (code.equals(errorCode.code)) {
                    return errorCode.getMessage();
                }
            }
            return null;
        }
    }
}
