# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table info (
  name                          varchar(255),
  password                      varchar(255)
);

create table note (
  note_id                       bigserial not null,
  note_name                     varchar(255),
  note_content                  varchar(255),
  date_                         varchar(255),
  user_user_                    bigint,
  constraint uq_note_note_name unique (note_name),
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

create table user_ (
  user_                         bigserial not null,
  user_name                     varchar(255),
  user_password                 varchar(255),
  user_email                    varchar(255),
  constraint uq_user__user_name unique (user_name),
  constraint uq_user__user_email unique (user_email),
  constraint pk_user_ primary key (user_)
);

create index ix_note_user_user_ on note (user_user_);
alter table note add constraint fk_note_user_user_ foreign key (user_user_) references user_ (user_) on delete restrict on update restrict;


# --- !Downs

alter table if exists note drop constraint if exists fk_note_user_user_;
drop index if exists ix_note_user_user_;

drop table if exists info cascade;

drop table if exists note cascade;

drop table if exists password_modification cascade;

drop table if exists reset cascade;

drop table if exists user_ cascade;

