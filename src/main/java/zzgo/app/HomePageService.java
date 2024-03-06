package zzgo.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzgo.domain.HomePageInfo;
import zzgo.domain.HomePageInfoService;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final HomePageInfoService homePageInfoService;

    public HomePageInfo getHomePageInfo() {
        return homePageInfoService.get();
    }
}
