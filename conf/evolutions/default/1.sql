# User schema

# --- !Ups

create table user (
  id integer not null primary key,
  email varchar(255) not null,
  name varchar(255) not null,
  password varchar(255) not null
);

create sequence user_seq;

# --- !Downs

drop table if exists user;
drop sequence if exists user_seq;
