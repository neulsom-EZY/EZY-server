package com.server.EZY.model.member.service.message;

import com.server.EZY.exception.authentication_number.exception.AuthenticationNumberTransferFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.Coolsms;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${sms.api.apikey}")
    private String apiKey;

    @Value("${sms.api.apiSecret}")
    private String apiSecret;

    /**
     * 메세지로 인증번호를 보내는 서비스로직
     *
     * @param phoneNumber phoneNumber
     * @param authKey authKey
     * @author 배태현
     */
    @Override
    public void sendAuthKeyMessage(String phoneNumber, String authKey) {
        Message coolsms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", phoneNumber);
        params.put("from", "07080283503");
        params.put("type", "SMS");
        params.put("text", "[EZY] 인증번호 "+ authKey +" 를 입력하세요.");
        params.put("app_version", "1.0.0");

        sendSms(coolsms, params);
    }

    /**
     * 메세지로 username을 보내는 서비스로직
     *
     * @param phoneNumber phoneNumber
     * @param username username
     * @author 배태현
     */
    @Override
    public void sendUsernameMessage(String phoneNumber, String username) {
        Message coolsms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", phoneNumber);
        params.put("from", "07080283503");
        params.put("type", "SMS");
        params.put("text", "[EZY] 회원님의 닉네임은 '"+ username +"' 입니다.");
        params.put("app_version", "1.0.0");

        sendSms(coolsms, params);
    }

    /**
     * 실질적으로 SMS를 전송하는 로직
     *
     * @param coolsms coolsms
     * @param params HashMap<String, String> params (사용자의 전화번호 등이 담긴 정보)
     * @author 배태현
     */
    private void sendSms(Message coolsms, HashMap<String, String> params) {
        try {
            JSONObject obj = coolsms.send(params);
            log.debug(obj.toString());

            if (obj.get("success_count").toString().equals("0")) {
                throw new AuthenticationNumberTransferFailedException();
            }

        } catch (CoolsmsException e) {
            log.debug(e.getMessage());
            log.debug(String.valueOf(e.getCode()));
        }
    }
}
