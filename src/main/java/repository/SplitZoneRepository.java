package repository;

import database.DatabaseConnection;
import domain.SplitZone;
import return_data.ReturnSplitZoneDataAfterQrAuth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SplitZoneRepository {
    private Connection con;

    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    public SplitZoneRepository() {
        try {
            con = DatabaseConnection.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SplitZone> getSplitZones() {
        String query = "SELECT * FROM split_planet";
        List<SplitZone> splitZones = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int planetId = rs.getInt(1);
                String planetCode = rs.getString(2);
                String planetName = rs.getString(3);
                String doroName = rs.getString(4);
                String startOperateTime = format.format(rs.getTime(5));
                String endOperateTime = format.format(rs.getTime(6));
                double lat = rs.getDouble(7);
                double lng = rs.getDouble(8);
                int allVisitor = rs.getInt(9);
                int todayVisitor = rs.getInt(10);
                String menuList = rs.getString(11);
                String phoneNumber = rs.getString(12);
                String cafeInImage = rs.getString(13);
                String cafeOutImage = rs.getString(14);
                String notify = rs.getString(15);
                String holiday = rs.getString(16);
                Time sTime = rs.getTime(17);
                Time eTime = rs.getTime(18);
                String specialStartTime;
                String specialEndTime;

                if (sTime == null) {
                    specialStartTime = "";
                } else {
                    specialStartTime = format.format(sTime);
                }

                if (eTime == null) {
                    specialEndTime = "";
                } else {
                    specialEndTime = format.format(eTime);
                }
                splitZones.add(new SplitZone(planetId, planetCode, planetName, doroName, startOperateTime
                        , endOperateTime, lat, lng, allVisitor, todayVisitor, menuList, phoneNumber
                        , cafeInImage, cafeOutImage, notify, holiday, specialStartTime, specialEndTime));
            }
        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());
        }
        return splitZones;
    }

    public ReturnSplitZoneDataAfterQrAuth getSplitZoneNameAndCode(int planet_id) {
        String query = "select planet_name,planet_code from split_planet where planet_id = ?";

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, planet_id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ReturnSplitZoneDataAfterQrAuth(rs.getString(1), rs.getString(2));
            }
        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

        }
        return new ReturnSplitZoneDataAfterQrAuth("error", "error");

    }
}