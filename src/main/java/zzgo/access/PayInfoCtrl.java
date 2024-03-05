package zzgo.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zzgo.app.PayInfoService;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;

import java.time.LocalDate;

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
}
