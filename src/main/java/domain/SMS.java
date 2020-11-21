package domain;

public class SMS {
    private int id;
    private String nick;
    private String authNumber;

    public SMS() {
    }

    public SMS(int id, String nick, String authNumber) {
        this.id = id;
        this.nick = nick;
        this.authNumber = authNumber;
    }

    public String getNick() {
        return nick;
    }

    public int getId() {
        return id;
    }


    public String getAuthNumber() {
        return authNumber;
    }
}
