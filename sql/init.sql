create table if not exists apartment_rental.apartment
(
	id serial not null,
	latitude varchar(20) not null,
	longitude varchar(20) not null,
	realtor_id integer not null,
	name varchar(100) not null,
	room_count integer not null,
	price numeric(18,2) not null,
	floor_area numeric(18,2) not null,
	description varchar(1000) default '{}'::json not null,
	created_at timestamp default LOCALTIMESTAMP not null,
	updated_at timestamp default LOCALTIMESTAMP not null
);

alter table apartment_rental.apartment owner to postgres;

create table apartment_rental.app_user
(
	id serial not null,
	name varchar(100) not null,
	email varchar(200) not null,
	password varchar(200),
	role varchar(10) not null,
	created_at timestamp default LOCALTIMESTAMP,
	updated_at timestamp default LOCALTIMESTAMP,
	status varchar(20) not null,
	is_suspended boolean default false not null,
	verification_param varchar(100) not null
);

alter table apartment_rental.app_user owner to postgres;

create unique index app_user_email_uindex
	on apartment_rental.app_user (email);

create unique index app_user_verification_param_uindex
	on apartment_rental.app_user (verification_param);



