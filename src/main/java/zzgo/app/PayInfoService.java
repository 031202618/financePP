package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;

import java.time.LocalDate;

/**
 * @author zhengw
 * @since 2024-03-05 14:48
 */
@Service
@RequiredArgsConstructor
public class PayInfoService {

    private final FundDetailService fundDetailService;

    public void uploadFundDetail(CategoryEnum category, LocalDate addTime, Money amount) {
        FundDetail fundDetail = FundDetail.of(category, addTime, amount);
        fundDetailService.uploadFundDetail(fundDetail);
    }
}
