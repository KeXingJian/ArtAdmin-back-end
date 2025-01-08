package com.kxj.artadmin.model;

import com.kxj.artadmin.enume.Status;
import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;


@Entity
public interface Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String uuid();

    @OneToOne
    @Nullable
    Picture picture();

    @ManyToOne
    @Nullable
    Artist artist();

    @Nullable
    String description();

    Status status();

    @ManyToMany
    List<Collection> collections();

    @ManyToMany(mappedBy = "videos")
    List<Dictionary> dictionaries();

    @LogicalDeleted("true")
    boolean deleted();

}
