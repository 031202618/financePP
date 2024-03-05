package zzgo.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zzgo.app.PayInfoService;
import zzgo.domain.FundDetail;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payInfo")
@RequiredArgsConstructor
public class PayInfoCtrl {

    private final PayInfoService payInfoService;

    @PostMapping("/fundDetail")
    public void uploadFundDetail(
            @RequestParam("category") CategoryEnum category,
            @RequestParam("addTime") LocalDate addTime,
            @RequestParam("amount") Money amount){
        payInfoService.uploadFundDetail(category, addTime, amount);
    }

    @GetMapping("/fundDetails")
    public List<FundDetail> listFundDetails(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer quarter,
            @RequestParam(required = false) Integer month
    ){
        return payInfoService.listFundDetails(year, quarter, month);
    }

    @DeleteMapping("/fundDetail")
    public void deleteFundDetail(@RequestParam int id){
        payInfoService.deleteFundDetail(id);
    }
}
