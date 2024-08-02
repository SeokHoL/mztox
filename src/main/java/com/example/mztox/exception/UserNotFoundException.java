package com.example.mztox.exception;

/*
UserNotFoundException은 주로 애플리케이션에서 특정 사용자를 찾을 수 없을 때 발생하는 예외를 나타내기 위해 사용됩니다.
*/
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}