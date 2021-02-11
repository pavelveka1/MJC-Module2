-- use gift_db;
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Поeлет на дельтоплане','Полеты на мотодельтаплане дарят кристально чистый заряд адреналина',1000,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Дайegeвинг с ьфинами','Сертификат на дайвинг с дельфинами — это уникальная возможность пообщаться с удивительными морскими жителями в их естественной среде. Погладить афалинов, поиграть с ними и даже услышать, как они общаются друг с другом, можно в бассейне дельфинария «Немо».',300,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Полеdsvт за штурвалом авижера Diamond','Вы всю жизнь боялись самолетов и предпочитаете путешествовать поездом? У вас есть по-настоящему уникальная возможность попробовать себя в качестве капитана воздушного судна и убедиться в безопасности современной техники. ',500,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Экстреegeмальные пи на джипе-монстре','Забудьте, все, что вы знали до этого о джипах. От брутальной внешности джип-монстра «Дождик» даже у видавших виды экстремалов изумленно откроется рот. Никаких сомнений в том, что этот «агрегат» с легкостью справится с любыми препятствиями, возникнуть просто не может.',100,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Массаж всего тела, спины или лица','Массаж — это самый эффективный, физиологичный и приятный способ сохранить здоровье, молодость и красоту! С помощью массажа можно избавиться от боли, усталости, стресса, эмоционального напряжения',200,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Базefeовыс «Рисование карандашом»','Специально для вас школа рисования «Все Малевичи» создала курс, который позволяет всего за 6 занятий включить у вас так называемое «художественное видение».',100,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Карefeтингухэтажной трассе','Подарочный сертификат на катание в обновленном клубе Минска «Картленд». Он находится в новом, чистом, сухом помещении. Но даже тут уже прочно обосновался запах жженой резины и бензина. Именно он привлекает многих любителей скорости',500,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Онлайefeн макласс по рисованию на выбор','Научиться рисовать и освоить новые техники поможет онлайн мастер-класс от от школы рисования «Все Малевичи»',100,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Обучиfeeтесь амическому и эстрадному вокалу','За один месяц вы освоите базовые теоретические знания и получите навыки профессионального исполнения, которые помогут вам развиваться в дальнейшем.',400,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');
INSERT INTO `gift_certificates` (`name`,`description`,`price`,`duration`,`create_date`,`last_update_date`) VALUES ('Мастefeер-класс «Любимые напитки»','Посетите мастер-класс от школы «БаристаБел» и узнайте много интересного и нового о любимом напитке!',200,30, '2020-09-15 16:06:00', '2020-10-15 16:06:00');


INSERT INTO `tags` (`name`) VALUES ('Активный отдых');
INSERT INTO `tags` (`name`) VALUES ('Спорт');
INSERT INTO `tags` (`name`) VALUES ('Туризм');
INSERT INTO `tags` (`name`) VALUES ('Развлечения');
INSERT INTO `tags` (`name`) VALUES ('Обучение');
INSERT INTO `tags` (`name`) VALUES ('Авто');
INSERT INTO `tags` (`name`) VALUES ('Услуги салонов красоты');
INSERT INTO `tags` (`name`) VALUES ('Мастер-класс');
INSERT INTO `tags` (`name`) VALUES ('Романтика');
INSERT INTO `tags` (`name`) VALUES ('Искусство');
INSERT INTO `tags` (`name`) VALUES ('Музыка');
INSERT INTO `tags` (`name`) VALUES ('Пение');
INSERT INTO `tags` (`name`) VALUES ('еда и напитки');
INSERT INTO `tags` (`name`) VALUES ('приготовление еды и напитков');
INSERT INTO `tags` (`name`) VALUES ('авиация');

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (1,1);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (1,2);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (1,3);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (1,4);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (1,9);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (1,15);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (2,1);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (2,2);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (2,3);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (2,4);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (3,4);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (3,5);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (3,6);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (3,15);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (4,1);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (4,2);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (4,3);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (4,4);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (4,6);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (5,7);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (5,8);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (5,4);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (6,5);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (6,8);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (6,10);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (7,1);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (7,2);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (7,3);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (7,4);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (7,6);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (8,5);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (8,8);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (8,10);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (9,5);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (9,8);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (9,11);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (9,12);

INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (10,5);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (10,8);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (10,13);
INSERT INTO `gift_certificates_has_tags` (`gift_certificates_id`,`tags_id`) VALUES (10,14);