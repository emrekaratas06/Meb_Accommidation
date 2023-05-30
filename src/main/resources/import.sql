ALTER TABLE guest_reservation DROP CONSTRAINT FKJ334AUX3646RG8JVFM9RRKP3J;
ALTER TABLE guest_reservation ADD CONSTRAINT FKJ334AUX3646RG8JVFM9RRKP3J FOREIGN KEY (GUEST_ID) REFERENCES guest (ID) ON DELETE CASCADE;


INSERT INTO teacher_home (id, province, district, name, address, email, phone_number) VALUES (1, 'Province 1', 'District 1', 'Teacher Home 1', 'Address 1', 'email@example.com', '05436245485');
INSERT INTO teacher_home (id, province, district, name, address, email, phone_number) VALUES (2, 'Province 2', 'District 2', 'Teacher Home 2', 'Address 2', 'email2@example.com', '05436245486');
INSERT INTO teacher_home (id, province, district, name, address, email, phone_number) VALUES (3, 'Province 3', 'District 3', 'Teacher Home 3', 'Address 3', 'email3@example.com', '05436245487');
INSERT INTO teacher_home (id, province, district, name, address, email, phone_number) VALUES (4, 'Province 1', 'District 2', 'Teacher Home 4', 'Address 12', 'email0102@example.com', '05436245488');
INSERT INTO teacher_home (id, province, district, name, address, email, phone_number) VALUES (5, 'Province 2', 'District 2', 'Teacher Home 5', 'Address 22', 'email0202@example.com', '05436245489');
INSERT INTO teacher_home (id, province, district, name, address, email, phone_number) VALUES (6, 'Province 4', 'District 1', 'Teacher Home 6', 'Address 4', 'email4@example.com', '05436245490');

INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (1, '101', '2023-05-21', '2023-05-30', TRUE, 2, 1, 50.00, 'DOUBLE','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (2, '102', '2023-05-21', '2023-05-30', TRUE, 1, 1, 40.00, 'SINGLE','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (3, '201', '2023-05-21', '2023-05-30', TRUE, 3, 2, 70.00, 'TRIPLE','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (4, '201', '2023-05-21', '2023-05-29', False, 3, 3, 200.00, 'SUİT','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (5, '205', '2023-05-21', '2023-05-30', False, 4, 3, 100.00, 'OTHER','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (6, '202', '2023-05-21', '2023-05-31', False, 2, 4, 320.00, 'TWINS','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (7, '201', '2023-05-21', '2023-05-31', TRUE, 1, 4, 180.00, 'SINGLE','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (8, '210', '2023-05-21', '2023-05-29', False, 3, 5, 400.00, 'SUİT','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (9, '205', '2023-05-21', '2023-05-30', False, 4, 6, 390.00, 'OTHER','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (10, '202', '2023-05-21', '2023-05-31', TRUE, 2, 5, 250.00, 'TWINS','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (11, '201', '2023-05-21', '2023-05-31', False, 1, 6, 180.00, 'SINGLE','EMPTY');
INSERT INTO room (id, room_number, dt_available_from, dt_available_to, reservation_switch, beds, teacher_home_id, cost_per_night, room_type,room_statuses) VALUES (12, '202', '2023-05-21', '2023-05-28', TRUE, 1, 6, 300.00, 'DOUBLE','EMPTY');

INSERT INTO guest (id, first_name, last_name, email, phone, guest_type,child,gender) VALUES (1, 'John', 'Doe', 'johndoe@example.com', '5551234567','MebPeople', FALSE,'MALE');
INSERT INTO guest (id, first_name, last_name, email, phone,guest_type, child,gender) VALUES (2, 'Jane', 'Doe', 'janedoe@example.com', '5551234568','StatePeople', FALSE,'FEMALE');
INSERT INTO guest (id, first_name, last_name, email, phone,guest_type, child,gender) VALUES (3, 'Alice', 'Smith', 'alicesmith@example.com', '5551234569','Person', TRUE,'FEMALE');
INSERT INTO guest (id, first_name, last_name, email, phone,guest_type, child,gender) VALUES (4, 'Ali', 'Smitci', 'alicesmith@cizz.com', '5551234570','MebPeople', FALSE,'MALE');
INSERT INTO guest (id, first_name, last_name, email, phone,guest_type, child,gender) VALUES (5, 'Ömer', 'Gezer', 'omergezer@cizz.com', '5551234571','StatePeople', TRUE,'MALE');
INSERT INTO guest (id, first_name, last_name, email, phone,guest_type, child,gender) VALUES (6, 'Haci', 'Smitci', 'hacismith@cizz.com', '5551234572','MebPeople', FALSE,'MALE');
INSERT INTO guest (id, first_name, last_name, email, phone,guest_type, child,gender) VALUES (7, 'Başkan', 'Gezer', 'baskangezer@cizz.com', '5551234573','StatePeople', TRUE,'MALE');

INSERT INTO reservation (id, info_price, room_id, dt_check_in, dt_check_out, teacher_home_id, start_date, end_date) VALUES (1, FALSE, 1, '10:00', '11:00', 1, '2023-04-21', '2023-04-25');
INSERT INTO reservation (id, info_price, room_id, dt_check_in, dt_check_out, teacher_home_id, start_date, end_date) VALUES (2, TRUE, 2, '11:00', '11:00', 1, '2023-04-21', '2023-04-22');
INSERT INTO reservation (id, info_price, room_id, dt_check_in, dt_check_out, teacher_home_id, start_date, end_date) VALUES (3, TRUE, 3, '10:30', '09:45', 2, '2023-04-21', '2023-04-24');
INSERT INTO reservation (id, info_price, room_id, dt_check_in, dt_check_out, teacher_home_id, start_date, end_date) VALUES (4, TRUE, 4, '12:00', '08:30', 3, '2023-04-22', '2023-04-26');
INSERT INTO reservation (id, info_price, room_id, dt_check_in, dt_check_out, teacher_home_id, start_date, end_date) VALUES (5, TRUE, 6, '10:30', '09:45', 4, '2023-05-21', '2023-05-24');
INSERT INTO reservation (id, info_price, room_id, dt_check_in, dt_check_out, teacher_home_id, start_date, end_date) VALUES (6, TRUE, 8, '12:00', '08:30', 5, '2023-05-28', '2023-06-02');


INSERT INTO employee (employee_id, employeetc_no, teacher_home_id, first_name, last_name, phone, employee_type, email) VALUES (1, '11111111111', 1, 'Emily', 'Johnson', '5559876543', 'EMPLOYEE', 'emilyjohnson@example.com');
INSERT INTO employee (employee_id, employeetc_no, teacher_home_id, first_name, last_name, phone, employee_type, email) VALUES (2, '22222222222', 1, 'Michael', 'Brown', '5559876544', 'MANAGER', 'michaelbrown@example.com');
INSERT INTO employee (employee_id, employeetc_no, teacher_home_id, first_name, last_name, phone, employee_type, email) VALUES (3, '33333333333', 2, 'Sarah', 'Taylor', '5559876545', 'MANAGER', 'sarahtaylor@example.com');
INSERT INTO employee (employee_id, employeetc_no, teacher_home_id, first_name, last_name, phone, employee_type, email) VALUES (4, '44444444444', 3, 'Farah', 'Maylor', '5559876546', 'OTHER', 'farahmaylor@example.com');

INSERT INTO guest_reservation (guest_id, reservation_id) VALUES (1, 1);
INSERT INTO guest_reservation (guest_id, reservation_id) VALUES (2, 1);
INSERT INTO guest_reservation (guest_id, reservation_id) VALUES (3, 2);
INSERT INTO guest_reservation (guest_id, reservation_id) VALUES (4, 3);
INSERT INTO guest_reservation (guest_id, reservation_id) VALUES (5, 4);
INSERT INTO guest_reservation (guest_id, reservation_id) VALUES (6, 5);
INSERT INTO guest_reservation (guest_id, reservation_id) VALUES (7, 6);