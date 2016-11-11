
    create table admin (
        id int8 not null,
        email varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        password varchar(255),
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

    alter table admin 
        add constraint uk_admin_email  unique (email);

    alter table slide 
        add constraint fk_carousel_slide_attachment 
        foreign key (id_attachment) 
        references attachment;

    create sequence attachment_seq start 1 increment 1;

    create sequence hibernate_sequence;

    create sequence slide_seq start 1 increment 1;

    create sequence study_seq start 1 increment 1;
