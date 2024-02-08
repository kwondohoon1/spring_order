package com.encore.ordering.member.controller;

import com.encore.ordering.common.CommonResponseDto;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginReqDto;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.dto.MemberResponseDto;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.order.dto.OrderResDto;
import com.encore.ordering.order.service.OrderService;
import com.encore.ordering.securities.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final OrderService orderService;

    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResponseDto> memberCreate(@Valid @RequestBody MemberCreateReqDto memberCreateReqDto){
        Member member = memberService.create(memberCreateReqDto);
        return new ResponseEntity<>(new CommonResponseDto(HttpStatus.CREATED, "member successfully create", member.getId()), HttpStatus.CREATED);
        }

    @GetMapping("/member/myInfo")
    public MemberResponseDto findMyInfo(){
        return memberService.findMyInfo();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{id}/orders")
    public List<OrderResDto> findMember(@PathVariable Long id){
        return orderService.findByMember(id);
    }


    @GetMapping("/member/myorders")
    public List<OrderResDto> findMyOrders(){
        return orderService.findMyOrders();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public List<MemberResponseDto> members(){
        return memberService.findAll();

    }

    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponseDto> memberLogin(@Valid @RequestBody LoginReqDto loginReqDto){
        Member member = memberService.login(loginReqDto);

//        토큰 생성
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> member_info = new HashMap<>();
        member_info.put("token", jwtToken);
        return new ResponseEntity<>(new CommonResponseDto(HttpStatus.OK, "member successfully logined", member_info),HttpStatus.OK);

    }
}