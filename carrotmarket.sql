drop table carrot_member CASCADE CONSTRAINTS;

drop sequence carr_mem_id_seq;
create table carrot_member(
id number(10) constraint carr_mem_id_pk primary key,
userid varchar2(20) not null,
userpwd varchar2(60) not null,
username varchar2(10) not null,
usernickname varchar2(20) not null,
useraddress varchar2(40) not null,
useremail varchar2(60),
usergender char(1)
);

create sequence carr_mem_id_seq;
select * from carrot_member;

--게시글 테이블
drop table carrot_articles CASCADE CONSTRAINTS;
drop sequence carr_art_id_seq;

create table carrot_articles(
ID NUMBER(10,0) constraint carr_art_id_pk primary key,
MEMBERNO NUMBER(10,0) NOT NULL, 
TITLE VARCHAR2(50 BYTE) NOT NULL, 
BODY VARCHAR2(2000 BYTE) NOT NULL, 
COST NUMBER(10,0), 
COSTOFFER CHAR(1 BYTE) DEFAULT 0, 
REGDATE DATE DEFAULT sysdate, 
UPDATEDATE DATE DEFAULT sysdate,
LNG VARCHAR2(35),
LAT VARCHAR2(35),
LOCATIONINFO VARCHAR2(1000), 
SELL NUMBER(1,0) DEFAULT 0, 
HITCOUNT NUMBER(10,0) DEFAULT 0, 
chatcount number(10,0) default 0,
CONSTRAINT CARR_ART_MEM_FK foreign key(MEMBERNO) references carrot_member (id)
);
create sequence carr_art_id_seq;

--이미지 테이블
drop table carrot_img;

CREATE TABLE CARROT_IMG (	
ARTICLENO NUMBER(10,0) NOT NULL, 
FILEPATH VARCHAR2(200 BYTE) NOT NULL, 
FILENAME VARCHAR2(100 BYTE) CONSTRAINT CARR_IMG_FILE_PK PRIMARY KEY,
CONSTRAINT CARR_IMG_ART_FK FOREIGN KEY (ARTICLENO)REFERENCES CARROT_ARTICLES (ID)
);
drop table carrot_thumbnail;
create table carrot_thumbnail(
ARTICLENO NUMBER(10,0) NOT NULL, 
FILEPATH VARCHAR2(200 BYTE) NOT NULL, 
FILENAME VARCHAR2(100 BYTE) CONSTRAINT CARR_THUM_FILE_PK PRIMARY KEY,
CONSTRAINT CARR_THUM_ART_FK FOREIGN KEY (ARTICLENO)REFERENCES CARROT_ARTICLES (ID)
);



select * from carrot_member;
select * from carrot_articles;
select * from carrot_thumbnail;
select * from carrot_img;

select * from carrot_member;