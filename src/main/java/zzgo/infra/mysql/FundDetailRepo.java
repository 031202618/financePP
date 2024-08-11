package zzgo.infra.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zzgo.infra.entity.FundDetailEntity;

import java.util.List;

/**
 * @author zhengw
 * @since 2024-03-05 15:54
 */
@Repository
public interface FundDetailRepo extends JpaRepository<FundDetailEntity, Integer> {
    @Query(value = "SELECT * FROM fund_detail", nativeQuery = true)
    List<FundDetailEntity> findAllIncludeDeleted();
}
