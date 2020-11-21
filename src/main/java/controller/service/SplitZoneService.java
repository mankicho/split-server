package controller.service;

import domain.SplitZone;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.SplitZoneRepository;
import return_data.ReturnSplitZoneDataAfterQrAuth;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = "/split")
public class SplitZoneService {
    private final SplitZoneRepository splitZoneRepository;

    public SplitZoneService() {
        splitZoneRepository = new SplitZoneRepository();
    }

    @GetMapping(value = "/get.do")
    public List<SplitZone> getAllList() {
        return splitZoneRepository.getSplitZones();
    }

    @GetMapping(value = "/get/detail")
    public ReturnSplitZoneDataAfterQrAuth getDetail(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("planet_id"));
        return splitZoneRepository.getSplitZoneNameAndCode(id);
    }
}
