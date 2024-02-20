package Filters;

public class UsernameHolder {
    private static final ThreadLocal<String> userNameLocal = new ThreadLocal<>();

    public static void setUserName(String userName){
        userNameLocal.set(userName);
    }

    public static String getUserName(){
        return userNameLocal.get();
    }

    public static void clearUserName(){
        userNameLocal.remove();
    }
}
