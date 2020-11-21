package controller;

import lombok.extern.log4j.Log4j;
import return_data.HomeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.AuthLogRepository;
import repository.PlanRepository;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/home")
@Log4j
public class HomeController {

    private final AuthLogRepository authLogRepository;
    private final PlanRepository planRepository;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HomeController() {
        authLogRepository = new AuthLogRepository();
        planRepository = new PlanRepository();
    }


    @GetMapping(value = "/today/get.home")
    public HomeView getHomeViewData(HttpServletRequest request) {
        log.info(dateFormat.format(new Date()) + " --> " + request.getRemoteAddr() + " : request");
        int allUser = authLogRepository.getTodayUser();
        int[] usersData = planRepository.getHomeViewData();
        int allUsersAtMoment = usersData[0];
        int authUsersAtMoment = usersData[1];
        return new HomeView(allUser, allUsersAtMoment, authUsersAtMoment);
    }
}
