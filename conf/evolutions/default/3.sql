# --- !Ups
create sequence record_req;
create table record (
  id integer not null primary key,
  memo varchar(255) not null,
  created timestamp not null default current_timestamp
);

# --- !Downs

drop sequence if exists record_req;
drop table if exists record;
