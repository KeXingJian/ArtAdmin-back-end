package com.kxj.artadmin.model;

import org.babyfish.jimmer.sql.*;

import java.util.List;


@Entity
public interface Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String name();

    @OneToMany(mappedBy = "role")
    List<ArtUser> artUsers();

    @LogicalDeleted("true")
    boolean deleted();
}
