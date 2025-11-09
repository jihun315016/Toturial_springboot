package com.example.basic.service;

import com.example.basic.dto.MemberRequest;
import com.example.basic.dto.MemberResponse;
import com.example.basic.entity.Member;
import com.example.basic.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // // final 필드에 대한 생성자 자동 주입
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
public class MemberService {
    private final MemberRepository memberRepository;

    // C: 회원 생성 (쓰기 작업이므로 @Transactional 필요)
    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        // 비즈니스 로직: username 중복 체크 등
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        Member member = request.toEntity(request);
        Member savedMember = memberRepository.save(member);
        return new MemberResponse(savedMember);
    }

    // R: 단일 회원 조회
    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + id));
        return new MemberResponse(member);
    }

    // R: 전체 회원 조회
    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    // U: 회원 정보 수정 (쓰기 작업이므로 @Transactional 필요)
    @Transactional
    public MemberResponse updateMemberEmail(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + id));

        // Entity 내부의 비즈니스 로직 메서드를 사용
        member.updateEmail(request.getEmail());

        // @Transactional에 의해 변경 감지(Dirty Checking)가 동작하여 save()를 명시적으로 호출하지 않아도 DB에 반영됩니다.
        return new MemberResponse(member);
    }

    // D: 회원 삭제 (쓰기 작업이므로 @Transactional 필요)
    @Transactional
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다: " + id);
        }
        memberRepository.deleteById(id);
    }
}
