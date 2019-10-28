create table boards
(
    board_id bigint auto_increment
        primary key,
    name     varchar(255) not null,
    user_id  bigint       null
)
    engine = MyISAM;

create index FK7kt8hby5livgmjj15f79e9t6v
    on boards (user_id);

create table boards_columns
(
    board_board_id       bigint not null,
    columns_task_list_id bigint not null,
    constraint UK_obtubinx3mg76lfk2b0okmmo0
        unique (columns_task_list_id)
)
    engine = MyISAM;

create index FK3ss48sarsks5fne4bgb2aqp92
    on boards_columns (board_board_id);

create table boards_members
(
    board_board_id  bigint not null,
    members_user_id bigint not null,
    constraint UK_s05aaekamp0p276n593jmimrq
        unique (members_user_id)
)
    engine = MyISAM;

create index FKf95jrgv9eh8fx0s6vbnv4t42y
    on boards_members (board_board_id);

create table comments
(
    comment_id bigint auto_increment
        primary key,
    content    varchar(255) not null,
    created_at datetime     not null,
    user_id    bigint       null,
    task_id    bigint       null
)
    engine = MyISAM;

create index FK8omq0tc18jd43bu5tjh6jvraq
    on comments (user_id);

create index FKi7pp0331nbiwd2844kg78kfwb
    on comments (task_id);

create table invitations
(
    invitation_id bigint auto_increment
        primary key,
    state         varchar(255) null,
    board_id      bigint       null,
    recipient     bigint       null,
    sender        bigint       null
)
    engine = MyISAM;

create index FKasfymxvqe67y4gcu16swe261i
    on invitations (recipient);

create index FKjw2cl0uqml103g752v8qwsh1r
    on invitations (sender);

create index FKqeo92gctew3ctmq2so2by8pbs
    on invitations (board_id);

create table tasklists
(
    task_list_id bigint auto_increment
        primary key,
    name         varchar(255) not null,
    position     int          not null,
    board_id     bigint       null
)
    engine = MyISAM;

create index FK4d603l7djdshp1n6xrfpttk2v
    on tasklists (board_id);

create table tasks
(
    task_id      bigint auto_increment
        primary key,
    description  varchar(255) not null,
    user_id      bigint       null,
    task_list_id bigint       null
)
    engine = MyISAM;

create index FK5410fi15rwc78gggkdwy5sfs4
    on tasks (task_list_id);

create index FK6s1ob9k4ihi75xbxe2w0ylsdh
    on tasks (user_id);

create table users
(
    user_id   bigint auto_increment
        primary key,
    logged_id bit          not null,
    login     varchar(255) not null,
    password  varchar(255) not null,
    constraint UK_ow0gan20590jrb00upg3va2fn
        unique (login)
)
    engine = MyISAM;

