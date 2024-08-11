package zzgo.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zzgo.app.PayInfoService;
import zzgo.domain.FundDetail;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payInfo")
@RequiredArgsConstructor
public class PayInfoCtrl {

    private final PayInfoService payInfoService;

    @PostMapping("/fundDetail")
    @ResponseBody
    public void uploadFundDetail(
            @RequestParam("category") int category,
            @RequestParam("addTime") LocalDate addTime,
            @RequestParam("amount") String amount,
            @RequestParam("comment") String comment) {
        payInfoService.uploadFundDetail(category, addTime, amount, comment);
    }

    @PostMapping("/fundDetail/{id}")
    @ResponseBody
    public void uploadFundDetail(
            @PathVariable("id") int id,
            @RequestParam("category") int category,
            @RequestParam("addTime") LocalDate addTime,
            @RequestParam("amount") String amount,
            @RequestParam("comment") String comment) {
        payInfoService.uploadFundDetail(id, category, addTime, amount, comment);
    }

    @GetMapping("/fundDetails")
    public String listFundDetails(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer quarter,
            @RequestParam(required = false) Integer month,
            Model model
    ) {
        List<FundDetail> fundDetails = payInfoService.listFundDetails(year, quarter, month);
        model.addAttribute("fundDetails", fundDetails);
        return "fundDetail";
    }

    @DeleteMapping("/fundDetail")
    @ResponseBody
    public void deleteFundDetail(@RequestParam int id) {
        payInfoService.deleteFundDetail(id);
    }
}
