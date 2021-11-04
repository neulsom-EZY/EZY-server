package com.server.EZY.model.member.service.message;

/**
 * 문자 서비스 로직 선언부
 *
 * @version 1.0.0
 * @author 배태현
 */
public interface MessageService {

    void sendAuthKeyMessage(String phoneNumber, String authKey);

    void sendUsernameMessage(String phoneNumber, String username);
}
