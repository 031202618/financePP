package zzgo.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhengw
 * @since 2025-01-12 17:33
 */
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionCtrl {
    @GetMapping("")
    public String getPage() {
        return "question";
    }
}
