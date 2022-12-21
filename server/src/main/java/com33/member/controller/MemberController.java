package com33.member.controller;
import com33.member.dto.MemberDto;
import com33.member.entity.Member;
import com33.member.mapper.MemberMapper;
import com33.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/members")
public class MemberController {
    private final Map<Long, Map<String, Object>> members = new HashMap<>();
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() { //더미 데이터
        Member member = new Member();
        long memberId = 1L;
        member.setMember_id(memberId);
        member.setPw("1234");
        member.setName("홍길동");
        member.setGender("m");
        member.setAge(27);
        member.setEmail("hgd@gmail.com");

        memberService.createMember(member);
    }

    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody){
        Member member = memberService.createMember(mapper.memberPostToMember(requestBody));

        return ResponseEntity.ok(mapper.memberToMemberResponse(member));
    }
    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @Valid @RequestBody MemberDto.Patch requestBody){
        requestBody.setMember_id(memberId);
        Member member = memberService.updateMember(mapper.memberPatchToMember(requestBody));

        return ResponseEntity.ok(mapper.memberToMemberResponse(member));
    }
    @GetMapping
    public ResponseEntity getMembers() {
        return ResponseEntity.ok(mapper.membersToMemberResponses(memberService.findMembers()));
    }
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(
            @PathVariable("member-id") @Positive long memberId) {
        return ResponseEntity.ok(mapper.memberToMemberResponse(memberService.findMember(memberId)));
    }
    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(
            @PathVariable("member-id") @Positive long memberId){
        memberService.deleteMember(memberId);

        return ResponseEntity.ok().build();
    }
}