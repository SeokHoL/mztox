package com.example.mztox.exception;

/*
이 클래스는 사용자 정의 예외 클래스로
ForbiddenException은 주로 사용자가 접근 권한이 없는 자원에 접근하려고 할 때 던져질 수 있는 예외입니다.
*/
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        /*부모 클래스 (RuntimeException)에서 예외 메시지를 저장하고 관리하면,
        모든 자식 예외 클래스가 일관된 방식으로 메시지를 처리할 수 있습니다.
    모든 예외 클래스가 동일한 방식으로 예외 메시지를 처리하므로, 예외 처리 코드가 더 간단하고 일관됩니다.*/

        super(message); //RuntimeException 클래스의 인스턴스를 생성하면서 message를 부모 클래스에 전달합니다.
    }
}