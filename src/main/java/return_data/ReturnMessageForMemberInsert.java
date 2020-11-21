package return_data;

import java.net.HttpURLConnection;

public class ReturnMessageForMemberInsert {
    private int mId;
    private String message;

    public ReturnMessageForMemberInsert(int mId, String message) {
        this.mId = mId;
        this.message = message;
    }

    public int getmId() {
        return mId;
    }

    public String getMessage() {
        return message;
    }
}
