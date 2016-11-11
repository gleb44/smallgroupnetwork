
    alter table slide 
        drop constraint fk_carousel_slide_attachment;

    drop table if exists admin cascade;

    drop table if exists attachment cascade;

    drop table if exists slide cascade;

    drop table if exists study cascade;

    drop sequence attachment_seq;

    drop sequence hibernate_sequence;

    drop sequence slide_seq;

    drop sequence study_seq;
