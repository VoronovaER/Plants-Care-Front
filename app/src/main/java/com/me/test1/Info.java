package com.me.test1;

public class Info {
    private static String password = null;
    private static String email = null;
    private static Long id = null;

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Info.password = password;
    }
    public static String getEmail() {
        return email;
    }
    public static void setEmail(String email) {
        Info.email = email;
    }

    public static Long getId() {
        return id;
    }
    public static void setId(Long id) {
        Info.id = id;
    }
}
