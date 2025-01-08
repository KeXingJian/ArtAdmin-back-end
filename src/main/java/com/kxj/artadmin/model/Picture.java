package com.kxj.artadmin.model;

import com.kxj.artadmin.enume.Type;
import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

@Entity
public interface Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    @Nullable
    String path();
    @Nullable
    Type type();

    @OneToOne(mappedBy = "picture")
    @Nullable
    Artist artist();

    @LogicalDeleted("true")
    boolean deleted();


}
