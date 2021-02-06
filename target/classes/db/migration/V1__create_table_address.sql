create table address (
	id bigint not null auto_increment,
	street_Name varchar(80) not null,
    number varchar(6) not null,
    complement varchar(20),
    neighbourhood varchar(80) not null,
    city varchar(80) not null,
    state varchar(80) not null,
    country varchar(80) not null, 
    zipcode varchar(20) not null,
    latitude DECIMAL(12,8) not null,
    longitude DECIMAL(12,8) not null,
    primary key (id)
);