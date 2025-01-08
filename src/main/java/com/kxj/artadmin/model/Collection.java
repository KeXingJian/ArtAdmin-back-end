package com.kxj.artadmin.model;

import org.jetbrains.annotations.Nullable;
import org.babyfish.jimmer.sql.*;

import java.util.List;

@Entity
public interface Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    String name();

    boolean common();

    @ManyToOne
    @Nullable
    ArtUser artUser();

    @Nullable
    @OneToOne
    Picture picture();

    @ManyToMany(mappedBy = "collections")
    List<Video> videos();

    @LogicalDeleted("true")
    boolean deleted();

}
