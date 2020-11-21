package domain.plan;

public enum PlanProgress {
    ONGOING, SUCCESS, FAIL, FINISH;

    public static PlanProgress getInstance(int value) {
        switch (value) {
            case 0:
                return PlanProgress.ONGOING;
            case 1:
                return PlanProgress.SUCCESS;
            case 2:
                return PlanProgress.FINISH;
            default:
                return PlanProgress.FAIL;
        }
    }
}
