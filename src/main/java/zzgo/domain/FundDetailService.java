package zzgo.domain;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzgo.infra.entity.FundDetailEntity;
import zzgo.infra.entity.FundDetailSnapshotEntity;
import zzgo.infra.mysql.FundDetailRepo;
import zzgo.infra.mysql.FundDetailSnapshotRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-05 15:35
 */
@Service
@RequiredArgsConstructor
public class FundDetailService {

    private final FundDetailRepo repo;
    private final FundDetailSnapshotRepo snapshotRepo;

    @PostConstruct
    private void init() {
        delayAllFundDetail();
        doSnapshot();
    }

    private void delayAllFundDetail() {
        List<FundDetail> allDetail = getAll();
        LocalDate lastDate = allDetail.stream().map(FundDetail::getAddTime).max(LocalDate::compareTo).get();
        if (lastDate.isAfter(LocalDate.now())) {
            return;
        }

        List<FundDetail> nextDetail = getNextDetail(allDetail, lastDate);
        if (nextDetail.isEmpty()) {
            return;
        }

        repo.saveAll(nextDetail.stream().map(FundDetailEntity::of).toList());
    }

    private List<FundDetail> getNextDetail(List<FundDetail> allDetail, LocalDate lastDate) {
        List<FundDetail> lastDetail = allDetail.stream()
                .filter(fundDetail -> fundDetail.getAddTime().equals(lastDate))
                .filter(fundDetail -> !fundDetail.isReduction())
                .toList();
        for (FundDetail fundDetail : lastDetail) {
            fundDetail.delay2NextMonth();
        }
        return lastDetail;
    }

    private void doSnapshot() {
        LocalDateTime lastSnapshotTime = snapshotRepo.findAll().stream()
                .map(FundDetailSnapshotEntity::getSnapshotTime)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MIN);
        LocalDateTime now = LocalDateTime.now();
        int lastYear = lastSnapshotTime.getYear();
        int lastMonth = lastSnapshotTime.getMonthValue();
        int curYear = now.getYear();
        int curMonth = now.getMonthValue();
        if (lastYear < curYear) {
            //snapshot
            List<FundDetail> list = repo.findAllIncludeDeleted().stream().map(FundDetailEntity::toDomain).toList();
            snapshotRepo.saveAll(list.stream().map(FundDetailSnapshotEntity::of).toList());
        } else if (lastYear == curYear) {
            if (lastMonth < curMonth) {
                //snapshot
                List<FundDetail> list = repo.findAllIncludeDeleted().stream().map(FundDetailEntity::toDomain).toList();
                snapshotRepo.saveAll(list.stream().map(FundDetailSnapshotEntity::of).toList());
            }
        } else {
            throw new RuntimeException("含有未来fund detail????????");
        }
    }

    public void uploadFundDetail(FundDetail fundDetail) {
        FundDetailEntity fundDetailEntity = FundDetailEntity.of(fundDetail);
        repo.save(fundDetailEntity);
    }

    @Transactional
    public void uploadFundDetail(int id, FundDetail fundDetail) {
        FundDetail old = repo.findById(id).map(FundDetailEntity::toDomain).orElseThrow(() -> new RuntimeException(String.format("未找到fundDetail: {%s}", id)));

        fundDetail.copyOld(old);
        FundDetailEntity newDetail = FundDetailEntity.of(fundDetail);
        repo.save(newDetail);

        old.snapshot();
        repo.save(FundDetailEntity.of(old));
    }

    public List<FundDetail> getAll() {
        return repo.findAll().stream().map(FundDetailEntity::toDomain).filter(fundDetail -> !fundDetail.isSnapshot()).toList();
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

    public FundDetail get(int id) {
        return repo.findById(id).map(FundDetailEntity::toDomain).orElseThrow(() -> new RuntimeException("unknown fundDetail id{}" + id));
    }
}
