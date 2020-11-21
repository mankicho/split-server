package return_data;

public class HomeView {
    private int allUsersToday;
    private int allUsersAtMoment;
    private int authUsersAtMoment;

    public HomeView(int allUsersToday, int allUsersAtMoment, int authUsersAtMoment) {
        this.allUsersToday = allUsersToday;
        this.allUsersAtMoment = allUsersAtMoment;
        this.authUsersAtMoment = authUsersAtMoment;
    }

    public int getAllUsersToday() {
        return allUsersToday;
    }

    public int getAllUsersAtMoment() {
        return allUsersAtMoment;
    }

    public int getAuthUsersAtMoment() {
        return authUsersAtMoment;
    }
}
