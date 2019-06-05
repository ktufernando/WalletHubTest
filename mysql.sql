-- Creates
create table access (id bigint not null, agent varchar(255), date datetime not null, ip varchar(255) not null, request varchar(255), status varchar(255), primary key (id)) engine=InnoDB
create table ip_log (id bigint not null, comment varchar(255), duration varchar(10) not null, finish datetime not null, ip varchar(15) not null, requests integer not null, start datetime not null, threshold integer not null, primary key (id)) engine=InnoDB

-- Selects
SELECT c.ip AS ip, c.count AS count FROM ( SELECT a.ip AS ip, COUNT(a.ip) AS count FROM access AS a WHERE (a.date >= ?1 AND a.date <= ?2) GROUP BY a.ip ) AS c WHERE c.count >= ?3