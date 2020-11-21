package repository;


import database.DatabaseConnection;
import domain.plan.PlanDomain;
import domain.plan.PlanType;
import domain.plan.UserPlan;
import lombok.extern.log4j.Log4j;
import return_data.ReturnMessageForPlanDelete;
import return_data.ReturnMessageForPlanInsert;
import return_data.ReturnMessageQrAuth;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// plan data repository
@Log4j
public class PlanRepository {
    private Connection con;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PlanRepository() {
        try {
            con = DatabaseConnection.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReturnMessageQrAuth qrAuth(int planLogId, int planetId, int flag) {
        System.out.println("flag =  " + flag);
        // todo 1. 행성 정보 가져오기
        String getPlanetQuery = "select planet_code, planet_name from split_planet where planet_id = ?";
        String name = "";
        String code = "";
        try {
            PreparedStatement pstmt = con.prepareStatement(getPlanetQuery);
            pstmt.setInt(1, planetId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                code = rs.getString(1);
                name = rs.getString(2);
            }
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " : " + e.getLocalizedMessage());
            return new ReturnMessageQrAuth(-500, name, code, "서버 에러입니다.");

        }
        // todo 2. plan 이 존재 하는지?
        String query = "SELECT plan_log_id from split_plan_log where plan_log_id = ?";
        boolean isExistPlan = false;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setInt(1, planLogId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isExistPlan = true;
            }
        } catch (SQLException e) {
            log.info(dateFormat.format(new Date()) + " : " + e.getLocalizedMessage());
            return new ReturnMessageQrAuth(-500, name, code, "유효하지 않은 접근입니다.");

        }

        int result = -1;
        if (isExistPlan) { // 존재하는 플랜이면
            // todo 2.1 현재 시간이 인증 할 수 있는 시간대인가 (오후 11시 이후)
            if (flag == 1) {
                String timeAfterMidnight = "select IF((p.setTime) " +
                        ">= time_format(date_add(now(),interval 61 minute),'%H:%i:%s'),'-1','1')" +
                        "from split_plan_log as p where plan_log_id = ?";
                try {
                    PreparedStatement pstmt = con.prepareStatement(timeAfterMidnight);
                    pstmt.setInt(1, planLogId);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        result = rs.getInt(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ReturnMessageQrAuth(-500, "", "", "서버에러");
                }
            } else {
//            // todo 2.2 현재 시간이 인증 할 수 있는 시간대인가 (오후 11시 이전)
                String timeBeforeMidnight = "select IF(p.setTime < " +
                        "time_format(date_add(now(), interval 61 minute),'%H:%i:%s')" +
                        "and p.setTime >= time_format(now(),'%H:%i:%s'),'1','-1')\n" +
                        "          as result\n" +
                        "from split_plan_log as p where p.plan_log_id = ?";
                try {
                    PreparedStatement pstmt = con.prepareStatement(timeBeforeMidnight);
                    pstmt.setInt(1, planLogId);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        result = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new ReturnMessageQrAuth(-500, "", "", "서버에러");
                }
            }

//             인증이 가능한 시간대라면
            if (result == 1) {
                // todo 4. 인증 가능하면 인증내역 테이블에 레코드 삽입
                String query4 = "insert into auth_log(a_plan_log_id,a_planet_id,auth_date,auth_time)" +
                        " values(?,?,date_format(now(),'%Y-%m-%d'),time_format(now(),'%H:%i:%s'))";
                try {
                    PreparedStatement pstmt = con.prepareStatement(query4);
                    pstmt.setInt(1, planLogId);
                    pstmt.setInt(2, planetId);

                    pstmt.execute();

                    return new ReturnMessageQrAuth(1, name, code, "플랜 인증을 성공했습니다");
                } catch (SQLIntegrityConstraintViolationException e) {
                    log.info(dateTimeFormat.format(new Date()) + " " + getClass().getName() + " : " + e.getMessage());
                    return new ReturnMessageQrAuth(-300, name, code, "이미 인증한 플랜입니다.");

                } catch (SQLException e1) {
                    log.info(dateTimeFormat.format(new Date()) + " " + getClass().getName() + " : " + e1.getMessage());
                    return new ReturnMessageQrAuth(-500, name, code, "서버 에러입니다.");

                }
            } else {
                return new ReturnMessageQrAuth(-200, name, code, "인증 가능한 시간이 아닙니다.");
            }
        } else {
            return new ReturnMessageQrAuth(-400, "", "", "존재하지 않는 플랜입니다");
        }
    }


    public ReturnMessageForPlanInsert insert(int memberId, int planId, String startDate,
                                             String endDate, String setTime, int flag) {
        if (flag != 0) {
            String subTime;
            String criTime;
            switch (setTime) {
                case "22:00:00":
                    subTime = "22:00:00";
                    criTime = "23:59:59";
                    break;
                case "22:30:00":
                    subTime = "00:30:00";
                    criTime = "00:00:00";
                    break;
                case "23:00:00":
                    subTime = "01:00:00";
                    criTime = "00:00:00";
                    break;
                case "23:30:00":
                    subTime = "01:30:00";
                    criTime = "00:00:00";
                    break;
                case "00:00:00":
                    subTime = "22:00:00";
                    criTime = "23:59:59";
                    break;
                case "00:30:00":
                    subTime = "22:30:00";
                    criTime = "23:59:59";
                    break;
                case "01:00:00":
                    subTime = "23:00:00";
                    criTime = "23:59:59";
                    break;
                default:
                    subTime = "23:30:00";
                    criTime = "23:59:59";
                    break;
            }
////             todo special
            String query = "SELECT IF(setTime between time_format('" + criTime + "','%H:%i:%s') and " +
                    "time_format('" + subTime + "','%H:%i:%s'),1,-1)" +
                    "from split_plan_log " +
                    "where memberId = ? and date_format(?,'%Y-%m-%d') <= endDate" +
                    "  and date_format(?,'%Y-%m-%d') >= startDate";

            try {
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setInt(1, memberId);
                pstmt.setString(2, startDate);
                pstmt.setString(3, endDate);
//
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    if (rs.getInt(1) == 1) {
                        return new ReturnMessageForPlanInsert(-1, "2시간 이내에 인증해야 하는 플랜이 존재합니다.");
                    }
                }
            } catch (Exception e) {
                log.info(dateFormat.format(new Date()) + " : " + e.getLocalizedMessage());
                return new ReturnMessageForPlanInsert(-500, "server error");
            }
        }
////         todo 1. 유저가 신청한 플랜 기간중 2시간 이내의 플랜이 존재하는가
        String query = "\n" +
                "\n" +
                "select " +
                "" +
                "       " +
                "IF((time_to_sec(abs(timediff(setTime,time_format(?,'%H:%i:%s')))) / 3600)  < 2, '-1', '1') as result\n" +
                "" +
                "       from split_plan_log where memberId = ? and date_format(?,'%Y-%m-%d') <= endDate\n" +
                "                                                                               and date_format(?,'%Y-%m-%d') >= startDate";

        int result = 0;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, setTime);
            pstmt.setInt(2, memberId);
            pstmt.setString(3, startDate);
            pstmt.setString(4, endDate);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1) == -1) {
                    result = -1;
                    break;
                } else {
                    result = 1;
                }
            }
        } catch (Exception e) {
            log.info(dateFormat.format(new Date()) + " : " + e.getLocalizedMessage());
            return new ReturnMessageForPlanInsert(-500, "server error");
        }
////         todo 2. 있으면 불가능을 리턴한다
////
        if (result == -1) {
            return new ReturnMessageForPlanInsert(-1, "2시간 이내에 인증해야 하는 플랜이 존재합니다.");
        } else {
//             todo 3. 없으면 하루 보유 플랜이 3개 미만인지 확인한다.
            String numberOfPlanQuery = "call checkPlans(?,?,?)";

            int numOfPlan = 0;
            try {
                PreparedStatement pstmt = con.prepareStatement(numberOfPlanQuery);
//
                pstmt.setInt(1, memberId);
                pstmt.setString(2, startDate);
                pstmt.setString(3, endDate);

                ResultSet rs = pstmt.executeQuery();
//
                if (rs.next()) {
                    numOfPlan = rs.getInt(1);
                }
            } catch (Exception e) {
                log.info(dateFormat.format(new Date()) + " : " + e.getLocalizedMessage());
                return new ReturnMessageForPlanInsert(-500, "server error");
            }
            // todo 4. 3개 미만이라면 insert 하고 성공을 리턴한다

            if (numOfPlan < 3) {
                String insertQuery = "insert into split_plan_log (memberId,plan_id,startDate," +
                        "endDate,setTime,success_auth,insert_time)" +
                        " values(?,?," +
                        "str_to_date(?,'%Y-%m-%d'),str_to_date(?,'%Y-%m-%d'),str_to_date(?,'%H:%i:%s')," +
                        "0,date_format(now(),'%Y-%m-%d %T'))";
                try {
                    PreparedStatement pstmt = con.prepareStatement(insertQuery);
                    pstmt.setInt(1, memberId);
                    pstmt.setInt(2, planId);
                    pstmt.setString(3, startDate);
                    pstmt.setString(4, endDate);
                    pstmt.setString(5, setTime);

                    pstmt.execute();

                    return new ReturnMessageForPlanInsert(1, "플랜 등록을 성공했습니다");

                } catch (SQLIntegrityConstraintViolationException e) {
                    log.info(dateFormat.format(new Date()) + " : " + e.getLocalizedMessage());
                    return new ReturnMessageForPlanInsert(-100, "중복 플랜이 존재합니다");
                } catch (Exception e2) {
                    log.info(dateFormat.format(new Date()) + " : " + e2.getLocalizedMessage());
                    return new ReturnMessageForPlanInsert(-500, "server error");
                }
            } else {
                return new ReturnMessageForPlanInsert(-2, "같은 기간에 인증해야 하는 플랜이 3개 이상 존재합니다");
            }

        }
    }

    public List<PlanDomain> getPlanDomains() {
        String query = "select p.plan_id,p.plan_name,p.plan_type,p.need_auth_num,p.img_path,count(*) from split_plan as p left outer join split_plan_log\n" +
                "as l on l.plan_id = p.plan_id group by p.plan_id";
        List<PlanDomain> result = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int type = rs.getInt(3);
                int needNum = rs.getInt(4);
                String imgPath = rs.getString(5);
                int accNum = rs.getInt(6);
                result.add(new PlanDomain(id, name, PlanType.getPlanType(type), needNum, imgPath, accNum));
            }

        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

        }
        return result;
    }

    public int[] getHomeViewData() {
        String query = "SELECT count(*),success_auth from split_plan_log as l, split_plan as p where l.plan_id = p.plan_id\n" +
                "ANd success_auth != -1 and setTime between str_to_date(?,'%H:%i:%s')" +
                "AND str_to_date(?,'%H:%i:%s') AND str_to_date(?,'%Y-%m-%d') between startDate and endDate" +
                " group by success_auth";

        try {
            java.util.Date start = new java.util.Date();
            java.util.Date end = new java.util.Date(start.getTime() + 1800 * 1000);

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, timeFormat.format(start));
            pstmt.setString(2, timeFormat.format(end));
            pstmt.setString(3, dateFormat.format(start));
            ResultSet rs = pstmt.executeQuery();

            int[] users = new int[2];
            while (rs.next()) {
                int num = rs.getInt(1);
                int type = rs.getInt(2);

                if (type == 1) {
                    users[1] += num;
                }

                users[0] += num;
            }
            return users;
        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

            return new int[]{};
        }
    }

    public ReturnMessageForPlanDelete deletePlan(int planLogId) {
        String deletePlan = "delete from plan_going where plan_log_id = ?";

        try {
            PreparedStatement pstmt = con.prepareStatement(deletePlan);
            pstmt.setInt(1, planLogId);

            int row = pstmt.executeUpdate();


            if (row > 0) {
                return new ReturnMessageForPlanDelete(true, "플랜을 성공적으로 삭제했습니다.");
            }
            return new ReturnMessageForPlanDelete(false, "존재하지 않는 플랜입니다.");
        } catch (SQLException e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

            return new ReturnMessageForPlanDelete(false, "서버 에러");
        }
    }


}
