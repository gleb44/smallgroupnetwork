create sequence attachment_seq start 1 increment 1;
create sequence slide_seq start 1 increment 1;
create sequence study_seq start 1 increment 1;
create sequence user_seq start 1 increment 1;

    create table account (
        id int8 not null,
        login varchar(255),
        password varchar(255),
        primary key (id)
    );

    create table admin_access (
        id int8 not null,
        admin_role varchar(50),
        primary key (id)
    );

    create table attachment (
        id int8 not null,
        content_type varchar(255),
        description varchar(255),
        duration int8,
        file_name varchar(255),
        uploaded timestamp,
        primary key (id)
    );

    create table profile (
        id int8 not null,
        address varchar(255),
        birth_date timestamp,
        city varchar(255),
        country varchar(255),
        postal_code varchar(255),
        state varchar(255),
        primary key (id)
    );

    create table slide (
        id int8 not null,
        index_number int4 not null,
        link varchar(255),
        id_attachment int8,
        primary key (id)
    );

    create table study (
        id int8 not null,
        created timestamp not null,
        description varchar(5120),
        link varchar(255) not null,
        speaker varchar(255),
        start_min int4 not null,
        start_sec int4 not null,
        title varchar(255) not null,
        views_count int4 not null,
        primary key (id)
    );

    create table user_account (
        id int8 not null,
        email varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        id_admin_access int8,
        id_avatar int8,
        id_profile int8,
        primary key (id)
    );

    alter table account 
        add constraint uk_account_login unique (login);

    alter table user_account 
        add constraint uk_admin_email unique (email);

    alter table account 
        add constraint FKm2dkadej0ilfblesdl8521ad9 
        foreign key (id) 
        references user_account;

    alter table admin_access 
        add constraint FK23a02nljg3yjbsi8wu7354xts 
        foreign key (id) 
        references user_account;

    alter table profile 
        add constraint FKpjg40inv1v6gtxger2143khdk 
        foreign key (id) 
        references user_account 
        on delete cascade;

    alter table slide 
        add constraint fk_carousel_slide_attachment 
        foreign key (id_attachment) 
        references attachment;

    alter table user_account 
        add constraint fk_user_admin_access 
        foreign key (id_admin_access) 
        references admin_access;

    alter table user_account 
        add constraint fk_user_attachment 
        foreign key (id_avatar) 
        references attachment;

    alter table user_account 
        add constraint fk_user_profile 
        foreign key (id_profile) 
        references profile;
