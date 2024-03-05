package zzgo.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.infra.entity.FundDetailEntity;
import zzgo.infra.mysql.FundDetailRepo;

/**
 * @author zhengw
 * @since 2024-03-05 15:35
 */
@Service
@RequiredArgsConstructor
public class FundDetailService {

    private final FundDetailRepo repo;

    public void uploadFundDetail(FundDetail fundDetail) {
        FundDetailEntity fundDetailEntity = FundDetailEntity.of(fundDetail);
        repo.save(fundDetailEntity);
    }
}
