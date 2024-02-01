package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.domain.Role;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(MemberCreateReqDto memberCreateReqDto){
        Address address = new Address(memberCreateReqDto.getCity(),
                memberCreateReqDto.getStreet(),
                memberCreateReqDto.getZipcode());
        Member member = Member.builder()
                .name(memberCreateReqDto.getName())
                .email(memberCreateReqDto.getEmail())
                .password(memberCreateReqDto.getPassword())
                .address(address)
                .role(Role.USER)
                .build();
        return memberRepository.save(member);
    }
}
