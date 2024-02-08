package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.domain.Role;
import com.encore.ordering.member.dto.LoginReqDto;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.dto.MemberResponseDto;
import com.encore.ordering.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberCreateReqDto memberCreateReqDto) {
        if(memberRepository.findByEmail(memberCreateReqDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("중복 이메일 입니다.");
        }
        memberCreateReqDto.setPassword(passwordEncoder.encode(memberCreateReqDto.getPassword()));
        Member member = Member.toEntity(memberCreateReqDto);
        return memberRepository.save(member);
    }
    public MemberResponseDto findMyInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return MemberResponseDto.toMemberResponseDto(member);

    }

    public List<MemberResponseDto> findAll(){
        List<Member> members = memberRepository.findAll();
        return members.stream().map(m->MemberResponseDto.toMemberResponseDto(m)).collect(Collectors.toList());
    }

    public Member login(LoginReqDto loginReqDto) throws IllegalArgumentException {
//        email존재여부
        Member member = memberRepository.findByEmail(loginReqDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 입니다."));

        if (!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }
        return member;
    }
}
