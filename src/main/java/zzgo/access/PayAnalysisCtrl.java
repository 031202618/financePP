package zzgo.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zzgo.app.PayAnalysisService;
import zzgo.app.vo.PayAnalysisVO;

@Controller
@RequestMapping("/payAnalysis")
@RequiredArgsConstructor
public class PayAnalysisCtrl {

    private final PayAnalysisService service;

    @GetMapping("")
    public String getPage(){
        return "fundAnalysis";
    }

    @GetMapping("/info")
    @ResponseBody
    public PayAnalysisVO getPayAnalysisVo(int year, int month){
        return service.getPayAnalysisVo(year, month);
    }

}
