
    alter table slide 
        drop constraint fk_carousel_slide_attachment;

    drop table if exists admin cascade;

    drop table if exists attachment cascade;

    drop table if exists mega_test cascade;

    drop table if exists slide cascade;

    drop table if exists study cascade;

    drop sequence if exists attachment_seq;

    drop sequence if exists hibernate_sequence;

    drop sequence if exists slide_seq;

    drop sequence if exists study_seq;
