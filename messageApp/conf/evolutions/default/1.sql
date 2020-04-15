# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table info (
  name                          varchar(255),
  password                      varchar(255)
);

create table note (
  note_id                       bigint auto_increment not null,
  note_name                     varchar(255),
  note_content                  varchar(255),
  date                          varchar(255),
  user_user_id                  bigint,
  constraint pk_note primary key (note_id)
);

create table password_modification (
  password                      varchar(255),
  confirmation_password         varchar(255),
  current_password              varchar(255)
);

create table reset (
  email                         varchar(255)
);

create table user (
  user_id                       bigint auto_increment not null,
  user_name                     varchar(255),
  user_password                 varchar(255),
  user_email                    varchar(255),
  constraint uq_user_user_name unique (user_name),
  constraint uq_user_user_email unique (user_email),
  constraint pk_user primary key (user_id)
);

create index ix_note_user_user_id on note (user_user_id);
alter table note add constraint fk_note_user_user_id foreign key (user_user_id) references user (user_id) on delete restrict on update restrict;


# --- !Downs

alter table note drop constraint if exists fk_note_user_user_id;
drop index if exists ix_note_user_user_id;

drop table if exists info;

drop table if exists note;

drop table if exists password_modification;

drop table if exists reset;

drop table if exists user;

