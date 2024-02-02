package hello.hellospringboot.controller;

import hello.hellospringboot.service.LocalCacheService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * 홈 컨트롤러
 */
@Controller
public class HomeController {
    private final LocalCacheService localCacheService;

    public HomeController(LocalCacheService localCacheService) {
        this.localCacheService = localCacheService;
    }

    /*
    * home.html로 이동
    * */
    @GetMapping("/")
    public String home(Model model) {
        Long totCnt = (Long) localCacheService.getData("totCnt", "totCntRefresh"); // 총 건수 캐싱하기
        model.addAttribute("cnt", totCnt);
        return "home";
    }
}
