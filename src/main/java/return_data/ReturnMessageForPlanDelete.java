package return_data;

public class ReturnMessageForPlanDelete {
    private boolean success;
    private String message;

    public ReturnMessageForPlanDelete(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
