package hello.hellospringboot.controller;

import hello.hellospringboot.domain.Member;
import hello.hellospringboot.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
/*
 * 회원 컨트롤러
 */
@Controller
public class MemberController {
    private final MemberService memberService;
    //생성자에 @Autowired 가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다. - DI
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    /*
     * 회원 등록 폼
     */
    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }
    /*
     * 회원 등록
     */
    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);
        return "redirect:/";
    }
    /*
     * 회원 목록 조회
     */
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
