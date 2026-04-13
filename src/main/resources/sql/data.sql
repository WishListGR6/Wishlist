use WishDB;

Set foreign_key_checks = 0;

truncate table wishlist;
truncate table wish;
truncate table event;
truncate table event_wish;
truncate table access_token;

set foreign_key_checks = 1;

start transaction;

insert into wishlist(listID, list_name, ownerPW, guestPW) values
    ('abcd1234', 'Sample list', 'o1234', 'g1234');

insert into access_token(token, listID) values
    ('access12', 'abcd1234');

insert into wish(listID, wish_name, description, product_url, comments, price) values
    ('abcd1234', 'Sample wish', 'description', 'URL',
     'sample comments', 9.95);

insert into event(listID, event_name, event_date) values
    ('abcd1234', 'Sample event', str_to_date('24-12-2026', '%d-%m-%Y'));

insert into event_wish(eventID, wishID)
select e.eventID, w.wishID
from(
    select eventID from event where event_name = 'Sample event'
) as e
cross join (
    select wishID from wish where wish_name = 'Sample wish'
) as w;

commit;