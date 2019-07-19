package com.miaoshaproject;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


//    @Test
//    public void bCryptPasswordEncoderTest(){
//        System.out.println(new BCryptPasswordEncoder().matches("$2a$10$5qRnHGOgS/pELuIdLjSSHuHltNA6hl6im.t08MngSrmYFl.IeJlmu","$2a$10$I1LaqUFoROCSjl7nvatdUej9iN5bCi9iA9sD24q1gApd7Lb0s5Zwe"));
//    }


    public static void main(String[] args){
        String password = "123";
        BCryptPasswordEncoder encoder1 = new BCryptPasswordEncoder();
        String encrptPassword1 = encoder1.encode(password.trim());
        BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
        String encrptPassword2 = encoder1.encode(password.trim());
        System.out.println(encrptPassword1+"====="+encrptPassword2);
        System.out.println(encoder1.matches("123",encrptPassword2));
    }
}
