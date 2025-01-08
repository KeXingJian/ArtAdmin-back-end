package com.kxj.artadmin.model;


import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Entity
public interface Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String description();

    @ManyToOne
    @Nullable
    Dictionary parent();

    @OneToMany(mappedBy = "parent")
    List<Dictionary> childDictionaries();

    @ManyToMany
    List<Video> videos();

    @LogicalDeleted("true")
    boolean deleted();
}
