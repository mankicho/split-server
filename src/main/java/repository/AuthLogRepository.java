package repository;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthLogRepository {
    private Connection con;


    public AuthLogRepository() {
        try {
            con = DatabaseConnection.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTodayUser() {
        int count = 0;
        String query = "select ifnull(p.memberId,b.memberId) as result from auth_log as l left outer join split_plan_log as p on l.a_plan_log_id = p.plan_log_id\n" +
                "left outer join split_plan_log_backup as b on l.a_plan_log_id = b.plan_log_id \n" +
                "where auth_date = date_format(now(),'%Y-%m-%d') group by result ";
//        String query = "select count(distinct p.memberId) from auth_log as l, split_plan_log as p \n" +
//                "where l.a_plan_log_id = p.plan_log_id AND auth_date = date_format(now(),'%Y-%m-%d') group by p.memberId;\n";
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1) == 0) {
                    continue;
                }
                count++;
            }
        } catch (SQLException e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

            return -1;
        }
        return count;
    }
}
