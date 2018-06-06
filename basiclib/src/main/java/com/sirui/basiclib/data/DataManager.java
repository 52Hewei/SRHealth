package com.sirui.basiclib.data;

import com.sirui.basiclib.data.bean.User;

/**
 * Created by xiepc on 2018/4/9 18:59
 */

public class DataManager {

    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
