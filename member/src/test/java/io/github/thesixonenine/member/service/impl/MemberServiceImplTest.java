package io.github.thesixonenine.member.service.impl;

import io.github.thesixonenine.member.entity.MemberEntity;
import io.github.thesixonenine.member.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void testList(){
        List<MemberEntity> list = memberService.list();
    }
}