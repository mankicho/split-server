package schedule;

import database.DatabaseConnection;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PlanetVisitorInitJob implements Job {
    private Connection con;

    public PlanetVisitorInitJob() {
        try {
            con = DatabaseConnection.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("every today visitor inits to 0");
        String query = "UPDATE split_planet set today_visitor = 0";
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
