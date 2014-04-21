INSERT INTO grade(name) VALUES('小班');
INSERT INTO grade(name) VALUES('小班');
INSERT INTO grade(name) VALUES('中班');
INSERT INTO grade(name) VALUES('大班');

INSERT INTO job_title(name) VALUES('普通教师');
INSERT INTO job_title(name) VALUES('高级教师');
INSERT INTO job_title(name) VALUES('特级教师');

INSERT INTO student(name, gender, grade, address, avatar) VALUES('Alex', 0, 1, '西湖区留下镇', 'child.png');

INSERT INTO student(name, gender, grade, address, avatar) VALUES('Ye', 0, 2, '下城区', '/files/child.png');
INSERT INTO student(name, gender, grade, address, avatar) VALUES('Allen', 0, 3, '上城区', '/files/child.png');


INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234560', 'Alex0', '爸爸', '12345678901', 'parent.png', '1');
INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234561', 'Alex1', '妈妈', '12345678901', 'parent.png', '1');
INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234562', 'Alex2', '爷爷', '12345678901', 'parent.png', '1');
INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234563', 'Alex3', '奶奶', '12345678901', 'parent.png', '1');
INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234564', 'Alex4', '外婆', '12345678901', 'parent.png', '1');


INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234565', 'Ye0', '爸爸', '12345678901', 'parent.png', '2');
INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234566', 'Ye1', '妈妈', '12345678901', 'parent.png', '2');
INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234567', 'Ye2', '外公', '12345678901', 'parent.png', '2');

INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('1234568', 'Allen0', '妈妈', '12345678901', 'parent.png', '3');

INSERT INTO parent(card_id, name, role, phone, avatar, student) VALUES('02303030383834363438360D0A03', '程程', '妈妈', '12345678901', 'parent.png', '3');
