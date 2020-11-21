package domain.plan;

public class PlanDomain {
    private int id;
    private String name;
    private PlanType type;
    private int need;
    private String imgPath;

    private int accNumber;

    public PlanDomain(int id, String name, PlanType type, int need, String imgPath, int accNumber) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.need = need;
        this.imgPath = imgPath;
        this.accNumber = accNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlanType getType() {
        return type;
    }

    public int getNeed() {
        return need;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getAccNumber() {
        return accNumber;
    }
}