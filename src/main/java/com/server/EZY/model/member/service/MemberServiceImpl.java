package com.server.EZY.model.member.service;

import com.server.EZY.exception.authentication_number.exception.InvalidAuthenticationNumberException;
import com.server.EZY.exception.user.exception.MemberAlreadyExistException;
import com.server.EZY.exception.user.exception.MemberInformationCheckAgainException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.exception.user.exception.NotCorrectPasswordException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.member.service.message.MessageService;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.CurrentUserUtil;
import com.server.EZY.util.KeyUtil;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 회원 관련 서비스 로직 구현부
 *
 * @version 1.0.0
 * @author 배태현
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final KeyUtil keyUtil;
    private final CurrentUserUtil currentUserUtil;
    private final MessageService messageService;

    private final long KEY_EXPIRATION_TIME = 1000L * 60 * 30; //3분

    private long REDIS_EXPIRATION_TIME = JwtTokenProvider.REFRESH_TOKEN_VALIDATION_TIME; //6개월

    /**
     * 이미 가입된 username인지 체크해주는 서비스로직
     *
     * @param username username
     * @return 이미 가입된 username이라면 true반환 / 이미 가입된 username이 아니라면 false반환
     * @author 배태현
     */
    @Override
    public boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    /**
     * 이미 가입된 phoneNumber인지 체크해주는 서비스로직
     *
     * @param phoneNumber phoneNumber
     * @return 이미 가입된 phoneNumber라면 true반환 / 이미 가입된 phoneNumber가 아니라면 false반환
     * @author 배태현
     */
    @Override
    public boolean isExistPhoneNumber(String phoneNumber) {
        return memberRepository.existsByPhoneNumber(phoneNumber);
    }

    /**
     * 회원가입 서비스 로직
     *
     * @param memberDto memberDto(username, password, phoneNumber, fcmToken)
     * @return save가 완료되면 test코드를 위한 memberEntity를 반환합니다.
     * @exception MemberAlreadyExistException 이미 존재하는 정보의 회원이라면
     * @author 배태현
     */
    @Override
    public MemberEntity signup(MemberDto memberDto) {
        try {
            if(!memberRepository.existsByUsernameAndPhoneNumber(memberDto.getUsername(), memberDto.getPhoneNumber())) {
                memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

                return memberRepository.save(memberDto.toEntity());
            } else throw new MemberAlreadyExistException();

        } catch (DataIntegrityViolationException e) {
            throw new MemberAlreadyExistException();
        }
    }

    /**
     * 로그인 서비스 로직
     *
     * @param loginDto loginDto(username, password)
     * @exception MemberNotFoundException username으로 member를 찾을 수 없을 때
     * @exception NotCorrectPasswordException 비밀번호가 일치하지 않을 때
     * @return Map<String ,String> (username, accessToken, refreshToken) 반환
     * @author 배태현
     */
    @Override
    @Transactional
    public Map<String, String> signin(AuthDto loginDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(loginDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        boolean passwordCheck = passwordEncoder.matches(loginDto.getPassword(), memberEntity.getPassword());
        if (!passwordCheck) throw new NotCorrectPasswordException();

        String accessToken = jwtTokenProvider.createToken(memberEntity.getUsername(), memberEntity.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisUtil.deleteData(memberEntity.getUsername()); // accessToken이 만료되지않아도 로그인 할 때 refreshToken도 초기화해서 다시 생성 후 redis에 저장한다.
        redisUtil.setDataExpire(memberEntity.getUsername(), refreshToken, REDIS_EXPIRATION_TIME);

        Map<String ,String> map = new HashMap<>();
        map.put("username", loginDto.getUsername());
        map.put("accessToken", "Bearer " + accessToken); // accessToken 반환
        map.put("refreshToken", "Bearer " + refreshToken); // refreshToken 반환

        return map;
    }

    /**
     * 로그아웃 서비스 로직 <br>
     * (redis에 있는 refreshToken을 지워준다) (Client는 accessToken을 지워준다)
     *
     * @param nickname nickname
     * @author 배태현
     */
    @Override
    public void logout(String nickname) {
        SecurityContextHolder.clearContext();
        redisUtil.deleteData(nickname);
    }

    /**
     * 전화번호로 인증번호를 보내는 서비스 로직
     *
     * @param phoneNumber phoneNumber
     * @author 배태현
     */
    @Override
    public void sendAuthKey(String phoneNumber) {
        String authKey = keyUtil.getKey(4);
        redisUtil.setDataExpire(authKey, authKey, KEY_EXPIRATION_TIME);

        messageService.sendAuthKeyMessage(phoneNumber, authKey);
    }

    /**
     * 사용자가 문자로 받은 인증번호를 검증하는 서비스 로직
     *
     * @param key authKey
     * @exception InvalidAuthenticationNumberException 인증번호가 일치하지 않을 때
     * @return test코드 작성을 위한 key 반환
     * @author 배태현
     */
    @Override
    public String validAuthKey(String key) {
        if (key.equals(redisUtil.getData(key))) {
            redisUtil.deleteData(key);
            return key;
        } else {
            throw new InvalidAuthenticationNumberException();
        }
    }

    /**
     * 비밀번호를 변경하기 전 정보를 받고 인증번호를 전송하는 서비스 로직
     *
     * @param memberAuthKeySendInfoDto memberAuthKeySendInfoDto(username, phoneNumber)
     * @exception MemberNotFoundException 회원을 찾을 수 없을 때
     * @exception MemberInformationCheckAgainException 회원에 대한 phoneNumber가 일치하지 않을 때
     * @author 배태현
     */
    @Override
    public void sendAuthKeyByMemberInfo(MemberAuthKeySendInfoDto memberAuthKeySendInfoDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(memberAuthKeySendInfoDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        if (memberEntity.getPhoneNumber().equals(memberAuthKeySendInfoDto.getPhoneNumber())) {
            sendAuthKeyToChangePassword(memberAuthKeySendInfoDto.getPhoneNumber()); //비밀번호 변경용 인증번호 메세지 전송
        } else {
            throw new MemberInformationCheckAgainException();
        }
    }

    /**
     * 비밀번호를 변경하기 위해 인증번호를 보내는 메서드
     *
     * @param phoneNumber phoneNumber
     * @exception MemberNotFoundException 회원을 찾을 수 없을 때
     * @author 배태현
     */
    private void sendAuthKeyToChangePassword(String phoneNumber) {
        MemberEntity findMember = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MemberNotFoundException());

        String authKey = keyUtil.getKey(4);
        redisUtil.setDataExpire(findMember.getUsername(), authKey, KEY_EXPIRATION_TIME);

        messageService.sendAuthKeyMessage(phoneNumber, authKey);
    }

    /**
     * 인증번호, 새로운 비밀번호를 받아 인증번호가 일치할 시 비밀번호가 변경되는 서비스로직
     *
     * @param passwordChangeDto passwordChangeDto(key, username, newPassword)
     * @exception InvalidAuthenticationNumberException 인증번호가 일치하지 않을 때
     * @author 배태현
     */
    @Override
    @Transactional
    public void changePassword(PasswordChangeDto passwordChangeDto) {
        String authKey = passwordChangeDto.getKey();
        String redisAuthKey = redisUtil.getData(passwordChangeDto.getUsername());

        if (authKey.equals(redisAuthKey)) {
            MemberEntity findMember = memberRepository.findByUsername(passwordChangeDto.getUsername());
            findMember.updatePassword(
                    passwordEncoder.encode(passwordChangeDto.getNewPassword())
            );
            redisUtil.deleteData(passwordChangeDto.getUsername());
        } else {
            throw new InvalidAuthenticationNumberException();
        }
    }

    /**
     * 문자로 username을 보내는 서비스로직
     *
     * @param phoneNumber phoneNumber
     * @exception MemberNotFoundException 회원을 찾을 수 없을 때
     * @author 배태현
     */
    @Override
    public void findUsernameByPhoneNumber(String phoneNumber) {
        MemberEntity memberEntity = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MemberNotFoundException());

        String username = memberEntity.getUsername().substring(1);

        messageService.sendUsernameMessage(phoneNumber, username);
    }

    /**
     * username을 변경하는 서비스 로직
     *
     * @param usernameDto usernameChangeDto(username, newUsername)
     * @author 배태현
     */
    @Override
    @Transactional
    public void changeUsername(UsernameDto usernameDto) {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();

        currentUser.updateUsername(usernameDto.getUsername());
    }

    /**
     * 전화번호를 변경하는 서비스 로직
     *
     * @param phoneNumberChangeDto phoneNumberChangeDto(newPhoneNumber)
     * @author 배태현
     */
    @Override
    @Transactional
    public void changePhoneNumber(PhoneNumberChangeDto phoneNumberChangeDto) {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();

        currentUser.updatePhoneNumber(phoneNumberChangeDto.getNewPhoneNumber());
    }

    /**
     * 회원탈퇴 서비스 로직
     *
     * @param deleteUserDto deleteUserDto(username, password)
     * @exception MemberNotFoundException 회원을 찾을 수 없을 때
     * @exception NotCorrectPasswordException 올바르지 않는 비밀번호일 시
     * @author 배태현
     */
    @Override
    public void deleteUser(AuthDto deleteUserDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(deleteUserDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        if (passwordEncoder.matches(deleteUserDto.getPassword(), memberEntity.getPassword())) {
            memberRepository.deleteById(memberEntity.getMemberIdx());
        } else throw new NotCorrectPasswordException();
    }

    /**
     * fcmToken 변경 서비스로직
     *
     * @param fcmTokenDto fcmTokenDto(fcmToken)
     * @author 배태현
     */
    @Override
    @Transactional
    public void updateFcmToken(FcmTokenDto fcmTokenDto) {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();

        currentUser.updateFcmToken(fcmTokenDto.getFcmToken());
    }

    /**
     * 키워드를 포함하고 있는 닉네임을 검색할 수 있는 메소드.
     *
     * @param keyword
     * @return List<UsernameResponseDto>
     * @author 전지환
     */
    @Override
    public List<UsernameResponseDto> searchUser(String keyword) {
        List<UsernameResponseDto> usernameList = memberRepository.searchUsernameKeywordBased(keyword);
        if (usernameList.isEmpty()) throw new UsernameNotFoundException("해당 keyword로 존재하는 username이 존재하지 않습니다.");
        else return usernameList;
    }
}
