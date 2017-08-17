create table i18n_message(
    id bigint not null,
    `key` varchar(255) not null,
    description varchar(255) not null,
    locale   varchar(255) not null,
    message varchar(20000) not null
) ENGINE=InnoDB;

alter table i18n_message
  add constraint pk_i18n_message primary key (id, `key`, locale);

alter table i18n_message
  add constraint u_i18n_message unique (`key`, locale);