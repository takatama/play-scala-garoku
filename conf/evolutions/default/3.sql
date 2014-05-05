# --- !Ups

create sequence record_seq;

create table record (
  id integer not null primary key,
  memo varchar(255) not null,
  created timestamp not null default current_timestamp
);

create sequence image_seq;

create table image (
  id integer not null primary key,
  content_type varchar(255) not null,
  path varchar(255) not null,
  created timestamp not null default current_timestamp,
);

# --- !Downs

drop sequence if exists record_seq;
drop table if exists record;

drop sequence if exists image_seq;
drop table if exists image;
