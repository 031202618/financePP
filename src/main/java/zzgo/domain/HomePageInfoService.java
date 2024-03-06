package zzgo.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.infra.entity.HomePageEntity;
import zzgo.infra.mysql.HomePageInfoRepo;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class HomePageInfoService {

    private final HomePageInfoRepo repo;

    public HomePageInfo get() {
        return repo.findAll().stream().max(Comparator.comparingInt(HomePageEntity::getId)).map(HomePageEntity::tran).orElse(null);
    }
}
