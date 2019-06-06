-- Creates
create table access (id bigint not null, agent varchar(255), date datetime not null, ip varchar(255) not null, request varchar(255), status varchar(255), primary key (id)) engine=InnoDB
create table ip_log (id bigint not null, comment varchar(255), duration varchar(10) not null, finish datetime not null, ip varchar(15) not null, requests integer not null, start datetime not null, threshold integer not null, primary key (id)) engine=InnoDB

-- Selects
SELECT ip, COUNT(ip) FROM access WHERE date BETWEEN ?1 AND ?2 GROUP BY ip HAVING count >= ?3