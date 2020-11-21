package domain;


import java.util.List;

public class Member {
    private int memberId;
    private String phoneNumber;
    private String nickName;
    private String email;
    private int authNum;
    private int point;

    private String authNumber;

    public Member(int memberId, String phoneNumber, String nickName, String email,
                  int authNum, int point, String authNumber) {
        this.memberId = memberId;
        this.authNumber = authNumber;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.email = email;
        this.authNum = authNum;
        this.point = point;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public int getAuthNum() {
        return authNum;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getPoint() {
        return point;
    }

    public String getAuthNumber() {
        return authNumber;
    }
}
