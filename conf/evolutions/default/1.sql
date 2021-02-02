# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table autor (
  id                            bigint auto_increment not null,
  nombre                        varchar(255),
  apellidos                     varchar(255),
  ciudad_natal                  varchar(255),
  fecha_creacion                timestamp not null,
  ultima_actualizacion          timestamp not null,
  constraint uq_autor_nombre unique (nombre),
  constraint pk_autor primary key (id)
);

create table ingrediente (
  id                            bigint auto_increment not null,
  nombre                        varchar(255),
  familia                       varchar(255),
  fecha_creacion                timestamp not null,
  ultima_actualizacion          timestamp not null,
  constraint uq_ingrediente_nombre unique (nombre),
  constraint pk_ingrediente primary key (id)
);

create table receta (
  id                            bigint auto_increment not null,
  nombre                        varchar(255),
  descripcion                   varchar(255),
  tipo_id                       bigint,
  autor_id                      bigint,
  fecha_creacion                timestamp not null,
  ultima_actualizacion          timestamp not null,
  constraint uq_receta_nombre unique (nombre),
  constraint uq_receta_tipo_id unique (tipo_id),
  constraint pk_receta primary key (id)
);

create table receta_ingrediente (
  receta_id                     bigint not null,
  ingrediente_id                bigint not null,
  constraint pk_receta_ingrediente primary key (receta_id,ingrediente_id)
);

create table tipo (
  id                            bigint auto_increment not null,
  nombre_tipo                   varchar(255),
  descripcion                   varchar(255),
  constraint uq_tipo_nombre_tipo unique (nombre_tipo),
  constraint pk_tipo primary key (id)
);

alter table receta add constraint fk_receta_tipo_id foreign key (tipo_id) references tipo (id) on delete restrict on update restrict;

create index ix_receta_autor_id on receta (autor_id);
alter table receta add constraint fk_receta_autor_id foreign key (autor_id) references autor (id) on delete restrict on update restrict;

create index ix_receta_ingrediente_receta on receta_ingrediente (receta_id);
alter table receta_ingrediente add constraint fk_receta_ingrediente_receta foreign key (receta_id) references receta (id) on delete restrict on update restrict;

create index ix_receta_ingrediente_ingrediente on receta_ingrediente (ingrediente_id);
alter table receta_ingrediente add constraint fk_receta_ingrediente_ingrediente foreign key (ingrediente_id) references ingrediente (id) on delete restrict on update restrict;


# --- !Downs

alter table receta drop constraint if exists fk_receta_tipo_id;

alter table receta drop constraint if exists fk_receta_autor_id;
drop index if exists ix_receta_autor_id;

alter table receta_ingrediente drop constraint if exists fk_receta_ingrediente_receta;
drop index if exists ix_receta_ingrediente_receta;

alter table receta_ingrediente drop constraint if exists fk_receta_ingrediente_ingrediente;
drop index if exists ix_receta_ingrediente_ingrediente;

drop table if exists autor;

drop table if exists ingrediente;

drop table if exists receta;

drop table if exists receta_ingrediente;

drop table if exists tipo;

