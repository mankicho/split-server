package controller.service;

import domain.plan.PlanDomain;
import domain.plan.UserPlan;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.PlanRepository;
import return_data.ReturnMessageForPlanDelete;
import return_data.ReturnMessageForPlanInsert;
import return_data.ReturnMessageQrAuth;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/plan")
@Log4j
public class PlanService {

    private final PlanRepository planRepository;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public PlanService() {
        planRepository = new PlanRepository();
    }

    @RequestMapping(value = "/qr.auth")
    public ReturnMessageQrAuth qrAuth(HttpServletRequest request) {
        String planLogId = request.getHeader("plan_log_id");
        String planetId = request.getParameter("planet_id");

        // 스케줄ID

        int flag = 0; // 일반적인 인증
        int pLogId;
        int pNetId;
        try {
            pLogId = Integer.parseInt(planLogId);
            pNetId = Integer.parseInt(planetId);
            Calendar now = Calendar.getInstance();
            Calendar after = Calendar.getInstance();
            now.setTime(new Date());
            after.setTime(new Date(new Date().getTime() + 61 * 60 * 1000));

            if (now.get(Calendar.DAY_OF_MONTH) != after.get(Calendar.DAY_OF_MONTH)) {
                flag = 1; // 오후 11시 이후의 인증 --> 날짜가 바뀌면서 시간비교를 다르게 해야함
            }
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " --> " + request.getRemoteAddr() + " : " + e.getLocalizedMessage());
            return new ReturnMessageQrAuth(-4, "", "", "유효하지 않은 접근입니다.");
        }

        return planRepository.qrAuth(pLogId, pNetId, flag);
    }

    @RequestMapping(value = "/insert.do")
    public ReturnMessageForPlanInsert insertDo(HttpServletRequest request) {
        log.info(dateFormat.format(new Date()) + " : " + request.getRemoteAddr() + " insert.do");

        String memberId = request.getHeader("member_id");
        String planId = request.getHeader("plan_id");
        String startDate = request.getHeader("startDate");
        String endDate = request.getHeader("endDate");
        String setTime = request.getHeader("setTime");
        setTime = setTime + ":00";

        String today = dateFormat.format(new Date());
        try {
            if (dateFormat.parse(today).compareTo(dateFormat.parse(startDate)) > 0) {
                return new ReturnMessageForPlanInsert(-700, "플랜 예약은 오늘날짜 이후만 가능합니다");
            }
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " : " + request.getRemoteAddr() + " 오늘 날짜 이후의 플랜만 예약 가능");
        }
        if (memberId == null || planId == null || startDate == null || endDate == null) {
            return new ReturnMessageForPlanInsert(-600, "보내지않은 헤더값이 존재합니다");
        }

        int mId;
        int pId;
        int flag;
        Date date;
        try {
            mId = Integer.parseInt(memberId);
            pId = Integer.parseInt(planId);
            date = timeFormat.parse(setTime);
            Calendar plusCal = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Calendar minusCal = Calendar.getInstance();
            Date tmp = new Date(date.getTime() + 3600 * 2 * 1000); // setTime + 2시간이 다음날로 넘어가는경우
            Date tmp2 = new Date(date.getTime() - 3600 * 2 * 1000); // setTime - 2시간이 전날로 넘어가는경우
            plusCal.setTime(tmp);
            cal.setTime(date);
            minusCal.setTime(tmp2);
            if (plusCal.get(Calendar.DAY_OF_MONTH) != cal.get(Calendar.DAY_OF_MONTH)) {
                flag = 1;
            } else if (minusCal.get(Calendar.DAY_OF_MONTH) != cal.get(Calendar.DAY_OF_MONTH)) {
                flag = -1;
            } else {
                flag = 0;
            }
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " : " + e.getLocalizedMessage());
            return new ReturnMessageForPlanInsert(-1, "please enter number for member_id, plan_id");
        }


        return planRepository.insert(mId, pId, startDate, endDate, setTime, flag);
    }

    @RequestMapping(value = "/get.do")
    public List<PlanDomain> getPlans() {
        return planRepository.getPlanDomains();
    }

    @RequestMapping(value = "/delete.do")
    public ReturnMessageForPlanDelete deletePlan(HttpServletRequest request) {

        return planRepository.deletePlan(request.getIntHeader("plan_log_id"));
    }

}
