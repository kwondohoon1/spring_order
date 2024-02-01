package com.encore.ordering.member.controller;

import com.encore.ordering.common.ResponseDto;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member/create")
    public ResponseEntity<ResponseDto> memberCreate(@Valid @RequestBody MemberCreateReqDto memberCreateReqDto){
        Member member = memberService.create(memberCreateReqDto);
        return new ResponseEntity<>(new ResponseDto(HttpStatus.CREATED, "member successfully create", member.getId()), HttpStatus.CREATED);

    }
}
