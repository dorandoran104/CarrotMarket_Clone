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

--게시글 테이블
drop table carrot_articles CASCADE CONSTRAINTS;
drop sequence carr_art_id_seq;

create table carrot_secondhand_articles(
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

alter table carrot_secondhand_articles add (likecount number(10,0) default 0);
--시퀀스
create sequence carr_art_id_seq;
--인덱스
create index carr_sec_art_ind on carrot_secondhand_articles(updatedate desc);
--이미지 테이블
drop table carrot_img;

CREATE TABLE CARROT_secondhand_IMG (	
ARTICLENO NUMBER(10,0) NOT NULL, 
FILEPATH VARCHAR2(200 BYTE) NOT NULL, 
FILENAME VARCHAR2(100 BYTE) CONSTRAINT CARR_IMG_FILE_PK PRIMARY KEY,
CONSTRAINT CARR_IMG_ART_FK FOREIGN KEY (ARTICLENO)REFERENCES carrot_secondhand_articles (ID)
);
-- 게시글 좋아요 테이블
drop table carrot_secondhand_article_like;
select * from carrot_secondhand_articles;
create table carrot_secondhand_article_like(
id number(10) constraint carr_like_id_pk primary key,
articleno number(10) not null,
memberno number(10) not null,
constraint carr_like_article_fk foreign key (articleno) REFERENCES carrot_secondhand_articles (id),
constraint carr_like_member_fk foreign key (memberno) REFERENCES carrot_member (id)
);
drop sequence carr_like_seq;
create sequence carr_like_seq;

select * from carrot_secondhand_article_like;

select count(id) from carrot_secondhand_article_like where articleno = 11;

-- 채팅방
drop table carrot_chatroom;

create table carrot_chatroom(
roomid varchar2(40) constraint chatroom_pk primary key,
chatuser number(10) not null,
targetuser number(10) not null,
articleno number(10) not null,
CONSTRAINT CARR_chat_cu_FK FOREIGN KEY (chatuser) REFERENCES CARROT_member (id),
CONSTRAINT CARR_chat_tu_FK FOREIGN KEY (targetuser) REFERENCES CARROT_member (id),
CONSTRAINT CARR_chat_an_FK FOREIGN KEY (articleno) REFERENCES carrot_secondhand_articles (id)
);
drop sequence carr_chat_id_seq;
create sequence carr_chat_id_seq;

--채팅
drop table carrot_chat;

create table carrot_chat(
roomid varchar2(40) not null,
message varchar2(1000) not null,
sender number(10) not null,
regdate varchar2(25) not null,
CONSTRAINT CARR_chat_id_FK FOREIGN KEY (roomid) REFERENCES carrot_chatroom (roomid)
);