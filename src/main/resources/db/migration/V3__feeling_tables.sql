INSERT INTO "user" (name, psw, active, last_time_of_activity)
VALUES ('yana', '1234', false, '2021-12-16 11:36:36.659560');
INSERT INTO "user" (name, psw, active, last_time_of_activity)
VALUES ('lesha', '1234', false, '2021-12-16 13:36:36.659560');
INSERT INTO "user" (name, psw, active, last_time_of_activity)
VALUES ('danya', '1234', false, '2021-12-16 16:36:36.659560');

INSERT INTO main_theme(theme_name)
VALUES ('theme_1');
INSERT INTO main_theme(theme_name)
VALUES ('theme_2');
INSERT INTO main_theme(theme_name)
VALUES ('theme_3');
INSERT INTO main_theme(theme_name)
VALUES ('theme_4');
INSERT INTO main_theme(theme_name)
VALUES ('theme_5');

INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_1.1', 1);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_1.2', 1);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_1.3', 1);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_1.4', 1);

INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_2.1', 2);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_2.2', 2);

INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_3.1', 3);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_3.2', 3);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_3.3', 3);

INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_4.1', 4);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_4.2', 4);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_4.3', 4);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_4.4', 4);
INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_4.5', 4);

INSERT INTO sub_theme(theme_name, main_theme_id)
VALUES ('sub_theme_5.1', 5);