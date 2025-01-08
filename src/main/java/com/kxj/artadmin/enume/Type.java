package com.kxj.artadmin.enume;

import org.babyfish.jimmer.sql.EnumItem;

public enum Type {
    @EnumItem(name = "M")
    MIRROR("mirror"),
    @EnumItem(name = "T")
    TEACHER("teacher"),
    @EnumItem(name = "A")
    AVATAR("avatar"),
    @EnumItem(name = "C")
    COVER("cover"),;
    private final String path;
    public String getPath() {
        return path;
    }

    Type(String path) {
        this.path = path;
    }
}
