package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.domain.FundDetail;
import zzgo.domain.FundDetailService;
import zzgo.domain.enums.CategoryEnum;
import zzgo.domain.util.Money;
import zzgo.domain.util.Time;
import zzgo.infra.entity.FundDetailEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public List<FundDetail> listFundDetails(Integer year, Integer quarter, Integer month) {
        return fundDetailService.getAll().stream()
                .sorted(Comparator.comparing(FundDetail::getAddTime))
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
