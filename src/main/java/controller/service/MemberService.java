package controller.service;

import domain.plan.UserPlan;
import lombok.extern.log4j.Log4j;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import repository.MemberRepository;
import return_data.ReturnMessageForMemberInsert;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/member")
@Log4j
public class MemberService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MemberRepository repository = new MemberRepository();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping(value = "/insert.do")
    public ReturnMessageForMemberInsert insert(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }


        String pNum = request.getHeader("pNum");
        String mail = request.getHeader("mail");

        String nick = request.getParameter("nick");
        log.info(dateFormat.format(new Date()) + " --> " + request.getRemoteAddr() + " : request\n" +
                "header = " + pNum + "," + mail + "," + nick);
        if (pNum == null) {
            return new ReturnMessageForMemberInsert(-1, "pNum is null");
        }


        if (!pNum.matches("^[0-9]+$")) {
            return new ReturnMessageForMemberInsert(-1, "please enter pNum only number");
        }

        if (nick == null) {
            return new ReturnMessageForMemberInsert(-1, "nName is null");
        }
//
        if (!(nick.length() >= 2 && nick.length() <= 8)) {
            return new ReturnMessageForMemberInsert(-1, "please enter nick length between 2 and 8");
        }

        if (mail == null) {
            mail = "";
        }
        return repository.insert(pNum, nick, mail);

    }

    @RequestMapping(value = "/getMy.do")
    public List<UserPlan> getMyPlans(HttpServletRequest request) {
        String id = request.getHeader("memberId");
        int memberId;
        try {
            memberId = Integer.parseInt(id);
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " --> " + request.getRemoteAddr() + " : " + e.getLocalizedMessage());

            return new ArrayList<>();
        }
        return repository.getMyPlans(memberId);
    }

    @RequestMapping(value = "/getMyAll.do")
    public List<UserPlan> getMyAllPlans(HttpServletRequest request) {
        String id = request.getHeader("memberId");
        String startDate = request.getHeader("startDate");
        String endDate = request.getHeader("endDate");

        int memberId = -1;
        try {
            memberId = Integer.parseInt(id);
            format.parse(startDate);
            format.parse(endDate);
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " --> " + request.getRemoteAddr() + " : " + e.getLocalizedMessage());
            return new ArrayList<>();
        }
        return repository.getAllMyPlans(memberId, startDate, endDate);
    }

    @RequestMapping(value = "/getMy.group")
    public List<UserPlan> getMyGroupByDayPlans(HttpServletRequest request) {
        String id = request.getHeader("memberId");

        int memberId = -1;

        try {
            memberId = Integer.parseInt(id);
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " --> " + request.getRemoteAddr() + " : " + e.getLocalizedMessage());
            return new ArrayList<>();
        }
        return repository.getMyGroupedByDayPlans(memberId);
    }


    @PostMapping(value = "/auto.login")
    public boolean autoLogin(HttpServletRequest request) {
        String pNum = request.getParameter("pNum");

        return repository.isExistsMember(pNum);
    }

    @RequestMapping(value = "/check.nick")
    public boolean checkNick(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String nickName = request.getParameter("nick");
        return repository.isExistsNick(nickName);
    }

    @RequestMapping(value = "/check.member")
    public boolean isExistsMember(HttpServletRequest request) {
        String phoneNumber = request.getHeader("pNum");
        return repository.isExistsMember(phoneNumber);
    }
}
