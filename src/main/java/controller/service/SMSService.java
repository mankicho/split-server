package controller.service;

import database.DatabaseConnection;
import domain.SMS;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Random;

@RestController
@RequestMapping(value = "/sms")
public class SMSService {

//    private File file = new File("C:\\split\\key.txt");
    private Connection con;

//    private String[] keys = new String[2];

    public SMSService() {
        try {
            con = DatabaseConnection.get();
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line;
//            StringBuilder sb = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                sb.append(line).append(",");
//            }
//            sb.deleteCharAt(sb.length() - 1);
//
//            keys = sb.toString().split(",");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/receive.do")
    public SMS sendSMS(HttpServletRequest request) {
        String pNum = request.getHeader("pNum");
        if (pNum == null) {

            System.out.println("null");
            return new SMS(-1, "", " pNum is null ");
        }

        if (!pNum.matches("^[0-9]+$")) {
            System.out.println("match");
            return new SMS(-1, "", "please enter the pNum {" + pNum + "}");

        }
        if (pNum.length() != 11) {
            System.out.println("length");
            return new SMS(-1, "", "please enter the pNum 11 length {" + pNum.length() + "}");
        }
        try {
            URL url = new URL("https://api-sens.ncloud.com/v1/sms/services/ncp:sms:kr:261072792575:split/messages");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-NCP-auth-key", "Bqm3WYwbuiI7ZaxJQs9H");
            connection.setRequestProperty("X-NCP-service-secret", "195c1c4e4b3b4b879b2a5b112d3b24e9");
            String msg = new Random().nextInt(8999) + 1000 + "";
            String[] str = new String[]{pNum};

            JSONObject object = new JSONObject();
            object.put("content", "[split 회원가입]\n인증번호 [" + msg + "] 를 입력해주세요");
            object.put("type", "SMS");
            object.put("to", Arrays.stream(str).toArray());
            object.put("from", "01036198087");

            OutputStream os = connection.getOutputStream();
            os.write(object.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();


            int status = connection.getResponseCode();
            String query = "select memberId,nickname from split_member where phone_number = ?";

            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setString(1, pNum);
            ResultSet rs = pstmt.executeQuery();

            int mId = -1;
            String nickName = "";
            if (rs.next()) {
                mId = rs.getInt(1);
                nickName = rs.getString(2);
            }
//
            if (status == 202) {
                return new SMS(mId, nickName, msg);
            } else {
                new SMS(-1, "", "fail");
            }
        } catch (Exception e) {
            System.out.println(getClass().getName() + " : " + e.getMessage());

            return new SMS();
        }
        return new SMS();
    }
}
