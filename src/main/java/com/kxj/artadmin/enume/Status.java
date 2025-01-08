package com.kxj.artadmin.enume;

import org.babyfish.jimmer.sql.EnumItem;


public enum Status {
    @EnumItem(name = "0")
    WAIT_AUDIT,
    @EnumItem(name = "1")
    WAIT_PUBLIC,
    @EnumItem(name = "2")
    REPEAT,
    @EnumItem(name = "3")
    PUBLIC,
    @EnumItem(name = "4")
    SCRAP;


}
