package util;

import domain.plan.UserPlan;

import java.util.ArrayList;
import java.util.List;

public class ReturnValueUtil {

    public static String returnStr() {
        return "please send data to GET method";
    }

    public static List<UserPlan> returnList() {
        return new ArrayList<>();
    }
}
