package domain.plan;


public class UserPlan {

    private int planLogId;
    private String planName;
    private PlanType type;
    private String startDate;
    private String endDate;
    private String setTime;
    private int needAuthNum;
    private int nowAuthNum;
    private PlanProgress planProgress;

    public UserPlan(int planLogId, String planName, PlanType type,
                    String startDate, String endDate, String setTime, int needAuthNum, int nowAuthNum, PlanProgress planProgress) {
        this.planLogId = planLogId;
        this.planName = planName;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.setTime = setTime;
        this.needAuthNum = needAuthNum;
        this.nowAuthNum = nowAuthNum;
        this.planProgress = planProgress;
    }

    public int getPlanLogId() {
        return planLogId;
    }

    public String getPlanName() {
        return planName;
    }

    public PlanType getType() {
        return type;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getSetTime() {
        return setTime;
    }

    public int getNeedAuthNum() {
        return needAuthNum;
    }

    public int getNowAuthNum() {
        return nowAuthNum;
    }

    public PlanProgress getPlanProgress() {
        return planProgress;
    }
}
