insert into "user"(name, pass) values('student', 'student');
insert into "user"(name, pass) values('user1', 'user1');
insert into "user"(name, pass) values('user2', 'user2');
insert into "user"(name, pass) values('user3', 'user3');
insert into "user"(name, pass) values('user4', 'user4');

insert into filelist(name, user_id) values('default', 1);
insert into filelist(name, user_id) values('source', 1);

insert into filediscr(name, size, copy, state, filelist_id) values('file.txt', 352, 1, 0, 1);
insert into filediscr(name, size, copy, state, filelist_id) values('photo.jpg', 54352, 1, 0, 1);
insert into filediscr(name, size, copy, state, filelist_id) values('test.file', 52, 1, 0, 1);


insert into filediscr(name, size, copy, state, filelist_id) values('main.cpp', 5432, 1, 0, 2);
insert into filediscr(name, size, copy, state, filelist_id) values('server.java', 5432, 1, 0, 2);

insert into filediscr(name, size, copy, state, filelist_id) values('test.file', 67, 3, 0, 29);
48
insert into filepart(num, size, filediscr_id, storage_id) values(0, 30, 48, 1); 
insert into filepart(num, size, filediscr_id, storage_id) values(0, 30, 48, 3); 

insert into filepart(num, size, filediscr_id, storage_id) values(1, 30, 48, 1); 
insert into filepart(num, size, filediscr_id, storage_id) values(1, 30, 48, 3); 

insert into filepart(num, size, filediscr_id, storage_id) values(2, 7, 48, 1); 
insert into filepart(num, size, filediscr_id, storage_id) values(2, 7, 48, 3); 



insert into storage(hostname, port, state) values('localhost', 9000, 0);
insert into storage(hostname, port, state) values('localhost', 9001, 0);

insert into filepart(num, size, filediscr_id, storage_id) values(0, 52, 5, 1); 

insert into filepart(num, size, filediscr_id, storage_id) values(0, 1713632, 2, 1); 
insert into filepart(num, size, filediscr_id, storage_id) values(0, 426, 3, 1); 
insert into filepart(num, size, filediscr_id, storage_id) values(0, 3678, 4, 1); 
insert into filepart(num, size, filediscr_id, storage_id) values(0, 3167, 1, 1); 


-- get all filelist
select fl.id, fl.name from filelist as fl
join "user" as u
on u.id=fl.user_id and u.name='student';

-- search files for user 'student'
select fd.id, fd.name, size, copy, fl.id, fl.name from filediscr as fd
join filelist as fl
on fl.id=fd.filelist_id and fd.name LIKE '%j%'
join "user" as us
on us.id=fl.user_id and us.name='student';

-- get fileparts of file and user
SELECT fd.id, fd.size, fp.id, fp.num, fp.size, st.hostname, st.port FROM filediscr AS fd
JOIN filelist AS fl
ON fl.id=fd.filelist_id AND fd.id = 5
JOIN "user" AS us
ON us.id=fl.user_id AND us.name='student'
JOIN filepart AS fp
ON fd.id=fp.filediscr_id
JOIN storage AS st
ON st.id=fp.storage_id
ORDER BY fp.num;


-- create new filelist
insert into filelist(name, user_id) values('list', 1);


-- create new filediscr
INSERT into filediscr(name, size, copy, state, filelist_id)
values('test.upload', 33, 1, 1, 2);

-- create fileparts
INSERT into filepart(num, size, filediscr_id, storage_id)
values();



-- remove filepart for file
SELECT fp.id from filepart as fp
JOIN filediscr as fd
on fd.id=3 and fd.id=fp.filediscr_id;

-- remove all fileparts for filediscr.id by user.id=#
DELETE from filepart WHERE id in (SELECT fp.id from filepart as fp
JOIN filediscr as fd
on fd.id=2 and fd.id=fp.filediscr_id
JOIN filelist as fl
on fl.id = fd.filelist_id and fl.user_id=1);
-- remove all fileparts from filelist.id=# by user.id=#
DELETE from filepart WHERE id in (SELECT fp.id from filepart as fp
JOIN filediscr as fd
on fd.filelist_id=2 and fd.id=fp.filediscr_id
JOIN filelist as fl
on fl.id = fd.filelist_id and fl.user_id=1);

-- remove all file from filelist.id
DELETE from filediscr WHERE id in (
SELECT fd.id from filediscr as fd
JOIN filelist as fl
on fl.id = fd.filelist_id and fl.user_id=1 and fl.id=2);



-- remove file by id and user.id
DELETE from filediscr WHERE id in(SELECT fd.id from filediscr as fd
JOIN filelist as fl
on fl.user_id=1 and fd.filelist_id=fl.id and fd.id=2);


-- remove filelist by id and user.id
DELETE from filelist WHERE id=2 and user_id=1;

connect 'jdbc:derby:dfsserver.mytest.db;create=true';


CREATE TABLE "user"
    (id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE,
    pass VARCHAR(40) NOT NULL,
    session VARCHAR(40) UNIQUE);


CREATE TABLE filelist
    (id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    state INTEGER NOT NULL,
    name VARCHAR(4000) NOT NULL,
    user_id INTEGER NOT NULL,
    Foreign Key (user_id) references "user"(id),
    unique(name, user_id));


CREATE TABLE filediscr
    (id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(4000) NOT NULL,
    size INTEGER NOT NULL,
    copy INTEGER NOT NULL,
    state INTEGER NOT NULL,
    filelist_id INTEGER NOT NULL,
    Foreign Key (filelist_id) references filelist(id),
    unique(name, filelist_id));


CREATE TABLE tag
    (id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(4000) NOT NULL UNIQUE);


CREATE TABLE storage
    (id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    hostname VARCHAR(4000) NOT NULL,
    port INTEGER NOT NULL,
    state INTEGER NOT NULL,
    unique(hostname, port));

insert into filepart(num, size, filediscr_id, storage_id)
values(0, ?, ?, 1);

CREATE TABLE filepart
    (id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    num INTEGER NOT NULL,
    size INTEGER NOT NULL,
    filediscr_id INTEGER NOT NULL,
    storage_id INTEGER NOT NULL,
    Foreign Key (filediscr_id) references filediscr(id),
    Foreign Key (storage_id) references storage(id));


CREATE TABLE filediscr_tag
    (id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    filediscr_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    Foreign Key (filediscr_id) references filediscr(id),
    Foreign Key (tag_id) references tag(id));

