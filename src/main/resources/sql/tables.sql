create table art_user
(
    id         int auto_increment
        primary key,
    picture_id int                  null,
    name       varchar(255)         not null,
    account    varchar(255)         not null,
    password   varchar(255)         not null,
    role_id    int        default 3 null,
    status     tinyint(1) default 0 not null,
    deleted    tinyint(1) default 0 not null,
    stop       tinyint(1)           not null,
    constraint account
        unique (account),
    constraint id
        unique (id),
    constraint user_picture_id_fk
        foreign key (picture_id) references picture (id),
    constraint user_role_id_fk
        foreign key (role_id) references role (id)
);
create table artist
(
    id          int auto_increment
        primary key,
    name        varchar(255)                null,
    picture_id  int                         null,
    description varchar(255) default '暂无' null,
    deleted     tinyint(1)   default 0      not null,
    constraint artist_index_0
        unique (id),
    constraint artist_index_1
        unique (name),
    constraint id
        unique (id),
    constraint name
        unique (name),
    constraint artist_picture_id_fk
        foreign key (picture_id) references picture (id)
)
    comment '艺术家表';
create table collection
(
    id          int auto_increment
        primary key,
    name        varchar(255)         not null,
    common      tinyint(1)           not null,
    art_user_id int                  null,
    picture_id  int                  null,
    deleted     tinyint(1) default 0 not null,
    constraint id
        unique (id),
    constraint collection_art_user_id_fk
        foreign key (art_user_id) references art_user (id),
    constraint collection_picture_id_fk
        foreign key (picture_id) references picture (id)
);
create table dictionary
(
    id          int auto_increment
        primary key,
    parent_id   int                  null,
    description varchar(255)         not null,
    deleted     tinyint(1) default 0 not null,
    constraint `desc`
        unique (description),
    constraint id
        unique (id),
    constraint dictionary_dictionary_id_fk
        foreign key (parent_id) references dictionary (id)
);

create table picture
(
    id      int auto_increment
        primary key,
    path    varchar(255)              null,
    deleted tinyint(1) default 0      not null,
    type    enum ('M', 'T', 'A', 'C') null,
    constraint id
        unique (id),
    constraint picture_index_0
        unique (id)
);

create table mirror
(
    id          int auto_increment
        primary key,
    picture_id  int                         not null,
    description varchar(255) default '暂无' null,
    deleted     tinyint(1)   default 0      not null,
    constraint id
        unique (id),
    constraint mirror_picture_id_fk
        foreign key (picture_id) references picture (id)
);

create index picture_index_1
    on picture (path);

create table role
(
    id      int auto_increment
        primary key,
    name    varchar(255)         not null,
    deleted tinyint(1) default 0 not null,
    constraint id
        unique (id)
);

create table video
(
    id          int auto_increment
        primary key,
    uuid        varchar(255)                   not null,
    picture_id  int                            null,
    artist_id   int                            null,
    description varchar(255) default '暂无'    null,
    deleted     tinyint(1)   default 0         not null,
    status      enum ('0', '1', '2', '3', '4') not null,
    constraint id
        unique (id),
    constraint uuid
        unique (uuid),
    constraint video_index_0
        unique (id),
    constraint video_index_1
        unique (uuid),
    constraint video_artist_id_fk
        foreign key (artist_id) references artist (id),
    constraint video_picture_id_fk
        foreign key (picture_id) references picture (id)
);

create table dictionary_video_mapping
(
    video_id      int not null,
    dictionary_id int not null,
    primary key (video_id, dictionary_id),
    constraint video_dictionary_mapping_dictionary_id_fk
        foreign key (dictionary_id) references dictionary (id),
    constraint video_dictionary_mapping_video_id_fk
        foreign key (video_id) references video (id)
);

create table video_collection_mapping
(
    collection_id int not null,
    video_id      int not null,
    primary key (collection_id, video_id),
    constraint video_collection_mapping_collection_id_fk
        foreign key (collection_id) references collection (id),
    constraint video_collection_mapping_video_id_fk
        foreign key (video_id) references video (id)
);



