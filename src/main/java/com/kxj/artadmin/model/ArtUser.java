package com.kxj.artadmin.model;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Entity
public interface ArtUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    String name();

    @Key
    String account();

    String password();

    @ManyToOne
    @Nullable
    Role role();

    boolean status();

    boolean stop();

    @LogicalDeleted("true")
    boolean deleted();

    @OneToOne
    @Nullable
    Picture picture();

    @OneToMany(mappedBy = "artUser")
    List<Collection> collections();

}
