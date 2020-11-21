package domain.plan;

public enum PlanType {
    CHALLENGE, EVENT,DEFAULT;

    public static PlanType getPlanType(int value) {
        switch (value) {
            case 0:
                return PlanType.CHALLENGE;
            case 1:
                return PlanType.EVENT;
            default:
                return PlanType.DEFAULT;
        }
    }
}
