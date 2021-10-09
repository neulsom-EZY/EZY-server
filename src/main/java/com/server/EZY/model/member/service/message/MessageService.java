package com.server.EZY.model.member.service.message;

public interface MessageService {

    void sendAuthKeyMessage(String phoneNumber, String authKey);

    void sendUsernameMessage(String phoneNumber, String username);
}
