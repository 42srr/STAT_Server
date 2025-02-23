
/*clear all record*/
delete from reservation;
delete from study_room;
delete from study_group;

/*create study group dummy record*/
insert into study_group (study_group_id, group_name)
values (1, 'test1');
insert into study_group (study_group_id, group_name)
values (2, 'test2');
insert into study_group (study_group_id, group_name)
values (3, 'test3');

/*create study room dummy record*/
insert into study_room (study_room_id, name, img, open_time, close_time, is_open24hour, can_drink, can_eat, can_use_tool)
values (1, 'test_room_1', 'www.url.com', '09:00:00', '18:00:00', 1, 1, 1, 1);
insert into study_room (study_room_id, name, img, open_time, close_time, is_open24hour, can_drink, can_eat, can_use_tool)
values (2, 'test_room_2', 'www.url.com', '09:00:00', '18:00:00', 1, 1, 1, 1);
/*create reservation dummy data*/

-- 16 - 18 시 예약
insert into reservation (reservation_id, study_group_id, study_room_id, start_date_time, end_date_time)
values (-1, 2, 1,'2025-02-10 16:00:00','2025-02-10 18:00:00');

-- 10 - 12 시 예약
insert into reservation (reservation_id, study_group_id, study_room_id, start_date_time, end_date_time)
values (-2, 2, 1,'2025-02-10 10:00:00','2025-02-10 12:00:00');

insert into reservation (reservation_id, study_group_id, study_room_id, start_date_time, end_date_time)
values (-3, 1, 1,'2025-02-10 9:00:00','2025-02-10 10:00:00');
