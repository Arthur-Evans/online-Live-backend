create table comment_log
(
    commentId  int auto_increment
        primary key,
    userName   varchar(50)  null,
    comment    varchar(100) null,
    roomLogId  int          null,
    createTime datetime     null
);

create index comment_log_room_log_id_fk
    on comment_log (roomLogId);

create index comment_log_user_info_uuid_fk
    on comment_log (userName);

create table partition_info
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    constraint id
        unique (id)
);

create table present_info
(
    id            int auto_increment
        primary key,
    name          varchar(50)   not null,
    value         int default 0 not null,
    presentAvatar mediumblob    null,
    illustration  varchar(50)   null comment '礼物详细说明'
);

create table present_log
(
    id        int auto_increment
        primary key,
    uuid      varchar(50)                        not null comment '谁送的',
    roomlogId int                                null comment '送给哪场直播',
    presentId int                                null comment '送的是啥',
    sendTime  datetime default CURRENT_TIMESTAMP null comment '啥时候送的',
    constraint present_log_id_uindex
        unique (id)
);

create index present_log_present_info_id_fk
    on present_log (presentId);

create index present_log_room_log_id_fk
    on present_log (roomlogId);

create index present_log_user_info_uuid_fk
    on present_log (uuid);

create table room_info
(
    liveId      int auto_increment comment '直播间号'
        primary key,
    roomName    varchar(50) null comment '名字',
    uuid        varchar(50) not null comment '拥有者的uuid',
    partitionId int         not null comment '分区',
    profile     varchar(50) null comment '简介',
    roomAvatar  blob        null
);

create index room_info_partition_info_id_fk
    on room_info (partitionId);

create index room_info_user_info_uuid_fk
    on room_info (uuid);

create table room_log
(
    id                 int auto_increment
        primary key,
    createTime         datetime default CURRENT_TIMESTAMP null,
    roomForeignId      int                                null comment '外键，关联Room_info的那个房间推流号',
    profile            varchar(100)                       null comment '简介',
    name               varchar(50)                        null comment '房间名',
    stopTime           datetime                           null,
    totalStar          int      default 0                 null,
    totalPresentValues int      default 0                 null,
    partitionId        int                                null comment '分区',
    roomAvatar         mediumblob                         null,
    constraint id
        unique (id)
);

create index room_log_partition_info_id_fk
    on room_log (partitionId);

create index room_log_room_info_liveId_fk
    on room_log (roomForeignId);

create table user_info
(
    uuid           varchar(50)                not null
        primary key,
    userAccount    varchar(30)                not null,
    userPassword   varchar(50)                not null,
    userName       varchar(20) default 'momo' not null,
    userSex        tinyint(1)  default 0      null,
    userAge        int         default 0      not null,
    userAvatar     blob                       null,
    userEmail      varchar(50)                null,
    userPrivilege  int         default 0      null,
    userCreateTime datetime                   not null,
    userUpdateTime datetime                   null,
    isDelete       tinyint(1)  default 0      null,
    userSignature  varchar(50)                null
);

create table follower_info
(
    id        int auto_increment
        primary key,
    following varchar(50) null comment '关注者',
    followed  varchar(50) null comment '被关注者',
    constraint follower_info_id_uindex
        unique (id),
    constraint follower_info_user_info_uuid_fk
        foreign key (followed) references user_info (uuid),
    constraint follower_info_user_info_uuid_fk_2
        foreign key (following) references user_info (uuid)
);

create table user_view_log
(
    id            int auto_increment
        primary key,
    uuid          varchar(50)   null,
    startViewTime datetime      null,
    endViewTime   datetime      null,
    stayTime      int default 0 null comment '单位：秒',
    room_log_id   int           null comment '访问的哪场直播',
    constraint user_view_log_user_info_uuid_fk
        foreign key (uuid) references user_info (uuid)
)
    comment '用户浏览日志';

create index user_view_log_room_log_id_fk
    on user_view_log (room_log_id);


