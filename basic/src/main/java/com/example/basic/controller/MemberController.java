package com.example.basic.controller;

import com.example.basic.dto.MemberRequest;
import com.example.basic.dto.MemberResponse;
import com.example.basic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // C: 회원 생성 (POST /api/members)
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // R: 단일 회원 조회 (GET /api/members/{id})
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        MemberResponse response = memberService.findMember(id);
        return ResponseEntity.ok(response);
    }

    // R: 전체 회원 조회 (GET /api/members)
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> responseList = memberService.findAllMembers();
        return ResponseEntity.ok(responseList);
    }

    // U: 회원 정보 수정 (PUT /api/members/{id})
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest request) {
        MemberResponse response = memberService.updateMemberEmail(id, request);
        return ResponseEntity.ok(response);
    }

    // D: 회원 삭제 (DELETE /api/members/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
