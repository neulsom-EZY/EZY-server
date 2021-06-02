package com.server.EZY.model.user;

import com.server.EZY.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Transactional
class UserEntityTest {

    @Autowired private UserRepository userRepo;

    @Test
    void userEntity_최대길이검증(){

        //given
        UserEntity user = UserEntity.builder()
                .nickname("JsonWebTok")
                .password("JsonWebTok")
                .phoneNumber("01012345678")
                .permission(Permission.PERMISSION)
                .build();

        assertThat(userRepo.save(user));

        //when
        String nickname = user.getNickname();
        String password = user.getPassword();
        String phoneNumber = user.getPhoneNumber();
        Permission permission = user.getPermission();

        //then
        assertThat(nickname.length()).isEqualTo(10);
        assertThat(password.length()).isEqualTo(10);
        assertThat(phoneNumber.length()).isEqualTo(11);
        assertThat(permission).isEqualTo(Permission.PERMISSION);
    }

    @Test
    void userEntity_최대길이_초과_Exception_검증() throws Exception {
        try{
            UserEntity user = UserEntity.builder()
                    .nickname("JsonWebTok1")
                    .password("JsonWebTok2")
                    .phoneNumber("010123456783")
                    .permission(Permission.PERMISSION)
                    .build();

            userRepo.save(user);
        }catch(ConstraintViolationException e){
            printException(e);
            return;
        }
        throw new Exception(); // ConstraintViolationException exception 이 발생되지 않으면 테스트 코드가 실패한다.
    }

    @Test
    void userEntity_최소길이_미만_Exception_검증() throws Exception {
        try{
            UserEntity user = UserEntity.builder()
                    .nickname("")
                    .password("123")
                    .phoneNumber("0103629383")
                    .permission(Permission.PERMISSION)
                    .build();

            userRepo.save(user);
        }catch(ConstraintViolationException e){
            printException(e);
            return;
        }
        throw new Exception(); // ConstraintViolationException exception 이 발생되지 않으면 테스트 코드가 실패한다.
    }

    void printException(Exception e){
        System.out.println("################################################################### Exception msg ###################################################################\n");
        e.printStackTrace();
        System.out.println("################################################################### Exception done ###################################################################\n");
    }

}