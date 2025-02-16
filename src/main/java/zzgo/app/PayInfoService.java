package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;
import zzgo.domain.util.Time;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-05 14:48
 */
@Service
@RequiredArgsConstructor
public class PayInfoService {

    private final FundDetailService fundDetailService;

    public void uploadFundDetail(int category, LocalDate addTime, String amount, String comment) {
        FundDetail fundDetail = FundDetail.of(CategoryEnum.of(category), addTime, Money.of(amount), comment, 0);
        fundDetailService.uploadFundDetail(fundDetail);
    }

    public void uploadFundDetail(int id, int category, LocalDate addTime, String amount, String comment) {
        FundDetail fundDetail = FundDetail.of(CategoryEnum.of(category), addTime, Money.of(amount), comment, 0);
        fundDetailService.uploadFundDetail(id, fundDetail);
    }

    public List<FundDetail> listFundDetails(Integer year, Integer quarter, Integer month) {
        return fundDetailService.getAll().stream()
                .sorted((x, y) -> y.getId() - x.getId())
                .filter(fundDetail -> {
                    LocalDate addTime = fundDetail.getAddTime();
                    return (year == null || addTime.getYear() == year) &&
                            (quarter == null || Time.month2Quarter(addTime.getMonthValue()) == quarter) &&
                            (month == null || month == addTime.getMonthValue());
                })
                .toList();
    }

    public void deleteFundDetail(int id) {
        fundDetailService.delete(id);
    }
}
