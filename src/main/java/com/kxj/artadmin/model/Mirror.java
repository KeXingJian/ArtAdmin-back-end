package com.kxj.artadmin.model;


import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

@Entity
public interface Mirror {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @OneToOne
    Picture picture();

    @Nullable
    String description();

    @LogicalDeleted("true")
    boolean deleted();

}
