package zzgo.infra.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zzgo.infra.entity.FundDetailEntity;

/**
 * @author zhengw
 * @since 2024-03-05 15:54
 */
@Repository
public interface FundDetailRepo extends JpaRepository<FundDetailEntity, Integer> {
}
