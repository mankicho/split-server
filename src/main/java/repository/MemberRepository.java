package repository;

import domain.plan.PlanProgress;
import return_data.ReturnMessageForMemberInsert;
import database.DatabaseConnection;
import domain.plan.UserPlan;
import domain.plan.PlanType;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
    private Connection con;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public MemberRepository() {
        try {
            con = DatabaseConnection.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExistsMember(String pNum) {
        String query = "select if((select phone_number from split_member where phone_number = ?),1,0)";

        try {
            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setString(1, pNum);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ReturnMessageForMemberInsert insert(String pName, String nName, String email) {
        try {
            String query = "INSERT INTO split_member(phone_number,nickname,email,auth_num,point) values(?,?,?,0,0)";
            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setString(1, pName);
            pstmt.setString(2, nName);
            pstmt.setString(3, email);

            int row = pstmt.executeUpdate();
            if (row > 0) {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select memberId from split_member order by memberId desc limit 1");

                if (rs.next()) {
                    return new ReturnMessageForMemberInsert(rs.getInt(1), "insert success");
                }
//                return "insert success";
            }
        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());
            return new ReturnMessageForMemberInsert(-1, "duplicate entry {" + pName + "} or {" + nName + "}");

        }
        return new ReturnMessageForMemberInsert(-1, "server error");
    }

    public List<UserPlan> getMyPlans(int memberId) {
        String query = "select l.plan_log_id, p.plan_name,p.plan_type, l.startDate, l.endDate, l.setTime, g.need_auth_num,\n" +
                "       g.now_auth_num, case when ceil(g.need_auth_num * 0.7) - g.now_auth_num -  (datediff(l.endDate, date_format(now(), '%Y-%m-%d')) + 1) > 0 THEN -1\n" +
                "                            when g.now_auth_num >= ceil(g.need_auth_num * 0.7) THEN 1\n" +
                "                            else 0 end as result\n" +
                "from split_plan_log as l, split_plan as p, plan_going as g where l.memberId = ?\n" +
                "                                                            AND l.plan_id = p.plan_id\n" +
                "                                                             and l.plan_log_id = g.plan_log_id\n" +
                "order by l.setTime asc";

        List<UserPlan> result = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, memberId);
//            pstmt.setString(2, wantDate);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int plan_log_id = rs.getInt(1);
                String planName = rs.getString(2);
                int planType = rs.getInt(3);
                Date startDate = rs.getDate(4);
                Date endDate = rs.getDate(5);
                String setTime = timeFormat.format(rs.getTime(6));
                int needAuth = rs.getInt(7);
                int nowAuth = rs.getInt(8);
                PlanProgress planProgress = PlanProgress.getInstance(rs.getInt(9));

                result.add(new UserPlan(plan_log_id, planName, PlanType.getPlanType(planType),
                        startDate.toString(), endDate.toString(), setTime, needAuth, nowAuth, planProgress));
            }

        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

        }
        return result;
    }

    public List<UserPlan> getMyGroupedByDayPlans(int memberId) {
        List<UserPlan> result = new ArrayList<>();
        String query = "select l.plan_log_id, p.plan_name,p.plan_type, l.startDate, l.endDate, l.setTime, g.need_auth_num,\n" +
                "       g.now_auth_num, case when ceil(g.need_auth_num * 0.7) - g.now_auth_num -  (datediff(l.endDate, date_format(now(), '%Y-%m-%d')) + 1) > 0 THEN -1\n" +
                "                            when g.now_auth_num >= ceil(g.need_auth_num * 0.7) THEN 1\n" +
                "                            else 0 end as result\n" +
                "from split_plan_log as l, split_plan as p, plan_going as g where l.memberId = ?\n" +
                "                                                            AND l.plan_id = p.plan_id and date_format(now(),'%Y-%m-%d')" +
                "between l.startDate and l.endDate " +
                "                                                             and l.plan_log_id = g.plan_log_id\n" +
                "order by l.setTime asc";
        try {
            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setInt(1, memberId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int plan_log_id = rs.getInt(1);
                String planName = rs.getString(2);
                int planType = rs.getInt(3);
                Date startDate = rs.getDate(4);
                Date endDate = rs.getDate(5);
                String setTime = timeFormat.format(rs.getTime(6));
                int needAuth = rs.getInt(7);
                int nowAuth = rs.getInt(8);
                PlanProgress planProgress = PlanProgress.getInstance(rs.getInt(9));

                result.add(new UserPlan(plan_log_id, planName, PlanType.getPlanType(planType),
                        startDate.toString(), endDate.toString(), setTime, needAuth, nowAuth, planProgress));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isExistsNick(String nick) {
        String query = "select if((select count(*) from split_member where nickname = ?),1,0)";

        try {

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, nick);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true; // nickname 이 중복이면
                } else {
                    return false; // 중복이 아니면
                }
            }
        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

        }
        return false;
    }

    public List<UserPlan> getAllMyPlans(int memberId, String startDate, String endDate) {
        String query = "select l.plan_log_id,p.plan_name,p.plan_type,l.startDate,l.endDate,l.setTime,\n" +
                "       g.need_auth_num,g.now_auth_num,\n" +
                "       case when ceil(g.need_auth_num * 0.7) - g.now_auth_num -  (datediff(l.endDate, date_format(now(), '%Y-%m-%d')) + 1) > 0 THEN -1\n" +
                "             when g.now_auth_num >= ceil(g.need_auth_num * 0.7) THEN 1\n" +
                "            else 0 end as result\n" +
                "       from split_plan_log as l,split_plan as p, plan_going as g\n" +
                "where l.plan_id = p.plan_id and g.plan_log_id = l.plan_log_id\n" +
                "and l.memberId = ?";
        List<UserPlan> result = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setInt(1, memberId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int planLogId = rs.getInt(1);
                String planName = rs.getString(2);
                PlanType type = PlanType.getPlanType(rs.getInt(3));
                String sDate = format.format(rs.getDate(4));
                String eDate = format.format(rs.getDate(5));
                String time = timeFormat.format(rs.getTime(6));
                int need = rs.getInt(7);
                int now = rs.getInt(8);
                PlanProgress planProgress = PlanProgress.getInstance(rs.getInt(9));

                result.add(new UserPlan(planLogId, planName, type, sDate, eDate, time, need, now, planProgress));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
