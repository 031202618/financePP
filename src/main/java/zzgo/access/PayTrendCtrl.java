package zzgo.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zzgo.app.PayTrendService;
import zzgo.app.vo.PayTrendVO;

import java.time.YearMonth;

@Controller
@RequestMapping("/payTrend")
@RequiredArgsConstructor
public class PayTrendCtrl {

    private final PayTrendService service;

    @GetMapping("")
    public String getPage() {
        return "fundTrend";
    }

    @GetMapping("/info")
    @ResponseBody
    public PayTrendVO getPayAnalysisVo(@RequestParam YearMonth startTime, @RequestParam YearMonth endTime) {
        return service.getPayAnalysisVo(startTime, endTime);
    }

}
