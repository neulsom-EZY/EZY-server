package com.server.EZY.model.user;

import com.server.EZY.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserEntityTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired private UserRepository userRepo;


    @Test
    @DisplayName("UserEntity 컬럼 최대길이 제약조건 확인")
    void userEntity_최대길이검증(){

        //######## given ########//
        UserEntity userEntity = UserEntity.builder()
                .nickname("JsonWebTok")
                .password("JsonWebTok")
                .phoneNumber("01012345678")
                .permission(Permission.PERMISSION)
                .build();

        //######## when ########//
        // entity save 후 영속화 헤제
        userRepo.saveAndFlush(userEntity);
        em.detach(userEntity);

        Long userIdx = userEntity.getUserIdx();
        System.out.println(userIdx);
        UserEntity userEntitySave = userRepo.findById(userIdx).get();

        //######## then ########//
        assertThat(userEntity == userEntitySave);
    }

    @Test
    @DisplayName("UserEntity 최대길이_초과시_Exception 검증 (Exception 발생시 Test 성공)")
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
    @DisplayName("UserEntity 최대길이_미만시_Exception 검증 (Exception 발생시 Test 성공)")
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