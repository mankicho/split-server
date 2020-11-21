package return_data;

public class ReturnMessageForPlanInsert {
    private int planLogId;
    private String message;

    public ReturnMessageForPlanInsert() {
    }

    public ReturnMessageForPlanInsert(int planLogId, String message) {
        this.planLogId = planLogId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getPlanLogId() {
        return planLogId;
    }


    public void setPlanLogId(int planLogId) {
        this.planLogId = planLogId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
