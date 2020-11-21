package domain;

public class SplitZone {
    private int planetId;
    private String planetCode;
    private String planetName;
    private String address;

    private String startTime;
    private String endTime;
    private double lat;
    private double lng;
    private int allVisit;
    private int todayVisit;
    private String menuList;
    private String phoneNumber;
    private String cafeInImage;
    private String cafeOutImage;
    private String notify;
    private String holiday;

    private String specialStartTime;
    private String specialEndTime;

    public SplitZone(int planetId, String planetCode, String planetName,
                     String address, String startTime, String endTime, double lat,
                     double lng, int allVisit, int todayVisit, String menuList,
                     String phoneNumber, String cafeInImage
            , String cafeOutImage, String notify, String holiday, String specialStartTime, String specialEndTime) {
        this.planetId = planetId;
        this.planetCode = planetCode;
        this.planetName = planetName;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lng = lng;
        this.allVisit = allVisit;
        this.todayVisit = todayVisit;
        this.menuList = menuList;
        this.phoneNumber = phoneNumber;
        this.cafeInImage = cafeInImage;
        this.cafeOutImage = cafeOutImage;
        this.notify = notify;
        this.holiday = holiday;
        this.specialStartTime = specialStartTime;
        this.specialEndTime = specialEndTime;
    }

    public int getPlanetId() {
        return planetId;
    }

    public String getPlanetCode() {
        return planetCode;
    }

    public String getPlanetName() {
        return planetName;
    }

    public String getAddress() {
        return address;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getAllVisit() {
        return allVisit;
    }

    public int getTodayVisit() {
        return todayVisit;
    }

    public String getMenuList() {
        return menuList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCafeInImage() {
        return cafeInImage;
    }

    public String getCafeOutImage() {
        return cafeOutImage;
    }

    public String getNotify() {
        return notify;
    }

    public String getHoliday() {
        return holiday;
    }

    public String getSpecialStartTime() {
        return specialStartTime;
    }

    public String getSpecialEndTime() {
        return specialEndTime;
    }
}
