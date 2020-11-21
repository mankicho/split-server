package schedule;

import database.DatabaseConnection;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdatePlanJob implements Job {
    private Connection con;

    public UpdatePlanJob() {
        try {
            con = DatabaseConnection.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("plan update do");
        // todo 1. 남은 인증 가능한 날짜와 인증 성공 회수를 고려해 챌린지의 성공 가능성 여부를 판별
        String updatePlan ="update split_plan_log as l inner join plan_going as p on l.plan_log_id = p.plan_log_id\n" +
                "set success_auth =\n" +
                "        case when (ceil(p.need_auth_num * 0.7) - (datediff(l.endDate,date_format(now(),'%Y-%m-%d'))+1) -\n" +
                "                   p.now_auth_num) > 0 then '-1'\n" +
                "             when p.now_auth_num >= ceil(p.need_auth_num * 0.7) then '2'\n" +
                "             else '0'\n" +
                "        end" +
                " where datediff(l.endDate,date_format(now(),'%Y-%m-%d')) >=0";

        try {
            PreparedStatement pstmt = con.prepareStatement(updatePlan);
            pstmt.execute();
            // todo 2.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
