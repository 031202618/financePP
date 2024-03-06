package zzgo.access;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzgo.app.HomePageService;
import zzgo.domain.HomePageInfo;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomePageController {

    private final HomePageService homePageService;

    @GetMapping("/info")
    public HomePageInfo getHomePageInfo(){
        return homePageService.getHomePageInfo();
    }

}
