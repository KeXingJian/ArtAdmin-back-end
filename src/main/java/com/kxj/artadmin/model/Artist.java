package com.kxj.artadmin.model;


import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Entity
public interface Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    @Nullable
    String name();

    @OneToOne
    @Nullable
    Picture picture();

    @Nullable
    String description();

    @OneToMany(mappedBy = "artist")
    List<Video> videos();

    @LogicalDeleted("true")
    boolean deleted();

}
