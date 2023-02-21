package com.security.jvm;

import lombok.Data;

/**
 * @author FUHONGXING
 */
@Data
public class User {
    private int uid;
    private String info;

    public User() {
    }

    public User(int uid, String info) {
        this.uid = uid;
        this.info = info;
    }

}
