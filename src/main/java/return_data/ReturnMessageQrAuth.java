package return_data;

public class ReturnMessageQrAuth {
    private int authenticate;
    private String message;
    private String name;
    private String code;


    public ReturnMessageQrAuth(int authenticate, String name, String code, String message) {
        this.name = name;
        this.code = code;
        this.authenticate = authenticate;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getAuthenticate() {
        return authenticate;
    }

    public String getMessage() {
        return message;
    }

    public void setAuthenticate(int authenticate) {
        this.authenticate = authenticate;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
