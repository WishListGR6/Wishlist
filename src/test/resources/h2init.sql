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
    event_name varchar(100),
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

insert into wishlist(listID, list_name, ownerPW, guestPW) values
    ('abcd1234', 'Sample list', 'o1234', 'g1234');

insert into access_token(token, listID) values
    ('access12', 'abcd1234');

insert into wish(listID, wish_name, description, product_url, comments, price, isReserved) values
    ('abcd1234', 'Sample wish', 'description', 'URL',
     'sample comments', 9.95, false);

insert into wish(listID, wish_name, description, product_url, comments, price, isReserved) values
    ('abcd1234', 'Sample wish 2', 'description 2', 'URL 2',
     'sample comments 2', 14.95, false);

insert into wish(listID, wish_name, description, product_url, comments, price, isReserved) values
    ('abcd1234', 'Sample wish 3', 'description 3', 'URL 3',
     'sample comments 3', 99.95, false);

insert into event(listID, event_name, event_date) values
    ('abcd1234', 'No event', '2050-01-01');

insert into event(listID, event_name, event_date) values
    ('abcd1234', 'Sample event', '2026-12-24');

insert into event(listID, event_name, event_date) values
    ('abcd1234', 'Sample event 2', '2027-05-13');


insert into event_wish(eventID, wishID)
select e.eventID, w.wishID
from(
select eventID from event where event_name = 'Sample event') as e
    cross join (
    select wishID from wish where wish_name = 'Sample wish') as w;

insert into event_wish(eventID, wishID)
select e.eventID, w.wishID
from(
        select eventID from event where event_name = 'No event'
    ) as e
        cross join (
    select wishID from wish where wish_name = 'Sample wish 2'
) as w;

insert into event_wish(eventID, wishID)
select e.eventID, w.wishID
from(
        select eventID from event where event_name in ('Sample event', 'Sample event 2')
    ) as e
        cross join (
    select wishID from wish where wish_name = 'Sample wish 3'
) as w;
