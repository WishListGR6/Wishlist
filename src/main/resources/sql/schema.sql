CREATE DATABASE IF NOT exists WishDB
       character set utf8mb4;

USE WishDB;

drop table if exists event_wish;
drop table if exists access_token;
drop table if exists event;
drop table if exists wish;
drop table if exists wishlist;


create table if not exists wishlist(
    listID varchar(8) primary key,
    list_name varchar(100) not null,
    ownerPW varchar(100) not null,
    guestPW varchar(100)
    );

create table if not exists access_token (
    token varchar(8) primary key,
    listID varchar(8) not null,
    foreign key(listID) references wishlist (listID) on delete cascade
);

create table if not exists event (
    eventID int auto_increment primary key,
    listID varchar(8) not null,
    foreign key(listID) references wishlist (listID) on delete cascade,
    event_name varchar(1000),
    event_date date not null
);

create table if not exists wish (
    wishID int auto_increment primary key,
    listID varchar(8) not null,
    foreign key(listID) references wishlist (listID) on delete cascade,
    wish_name varchar(100) not null,
    description varchar(1000),
    product_url varchar(1000),
    comments varchar(1000),
    price double,
    isReserved boolean not null,
    image mediumblob
);

create table if not exists event_wish (
    eventID int not null,
    wishID int not null,
    primary key (wishID, eventID),
    foreign key (wishID) references wish (wishID) on delete restrict,
    foreign key (eventID) references event (eventID) on delete restrict
);