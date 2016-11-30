
    alter table account 
        drop constraint FKm2dkadej0ilfblesdl8521ad9;

    alter table admin_access 
        drop constraint FK23a02nljg3yjbsi8wu7354xts;

    alter table profile 
        drop constraint FKpjg40inv1v6gtxger2143khdk;

    alter table slide 
        drop constraint fk_carousel_slide_attachment;

    alter table user_account 
        drop constraint fk_user_admin_access;

    alter table user_account 
        drop constraint fk_user_attachment;

    alter table user_account 
        drop constraint fk_user_profile;

    drop table if exists account cascade;

    drop table if exists admin_access cascade;

    drop table if exists attachment cascade;

    drop table if exists profile cascade;

    drop table if exists slide cascade;

    drop table if exists user_account cascade;

    drop sequence if exists attachment_seq;

    drop sequence if exists slide_seq;

    drop sequence if exists user_seq;
