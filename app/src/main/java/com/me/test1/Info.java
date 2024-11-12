package com.me.test1;

public class Info {
    private static String name = null;
    private static String email = null;
    private static Long id = null;
    private static String avatar = null;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Info.name = name;
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

    public static String getAvatar() {
        return avatar;
    }

    public static void setAvatar(String avatar) {
        Info.avatar = avatar;
    }
}
