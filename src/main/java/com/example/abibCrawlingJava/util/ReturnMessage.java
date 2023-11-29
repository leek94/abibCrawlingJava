package com.example.abibCrawlingJava.util;

public enum ReturnMessage {
    CONNECTION("서버와의 연결에 실패 했습니다."),
    CHANGE_EVENT("이벤트 카테고리에 변화가 생겼습니다."),
    EVENT_INDEX("이벤트 크롤링 문제 발생"),
    INSERT_CATEGORY("카테고리가 추가 되었습니다"),
    CHANGE_CATEGORY("카테고리에 변화가 생겼습니다.\n");


    private final String message;

    ReturnMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}
