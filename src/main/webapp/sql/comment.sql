drop table comm cascade constraints purge;

create table comm(
	num					number primary key,
	id					varchar2(30) references member(id),
	content 			varchar2(200),
	reg_date			date,
	comment_board_num	number references board(board_num) on delete cascade, -- comm 테이블이 참조
	comment_re_lev		number(1) check(comment_re_lev in (0,1,2)), -- 원문이면 0 답글이면 1
	comment_re_seq		number, -- 원문이면 9, 1레벨이면 시퀀스 + 1
	comment_re_ref		number  -- 원문은 자신 글번호, 답글이면 원문 글번호
);
-- 게시판 글이 삭제되면 참조하는 댓글도 삭제됩니다.

drop sequence com_seq;

-- 시퀀스를 생성합니다.
create sequence com_seq;

delete from comm;

select * from comm;

insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
values(1, '처음', 'admin', 1);
insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
values(2, '둘째', 'admin', 2);
insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
values(3, '셋째', 'admin', 3);

insert into comm (num, id, comment_board_num) values(1,'admin',1);
insert into comm (num, id, comment_board_num) values(2,'admin',1);
insert into comm (num, id, comment_board_num) values(3,'admin',2);
insert into comm (num, id, comment_board_num) values(4,'admin',2);

update board
set	   board_subject = '오늘도 행복하세요'
where  board_num=1;

-- 1. comm 테이블에서 comment_board_num 별 갯수를 구합니다.
COMMENT_BOARD_NUM CNT
1				  2
2				  2

select comment_board_num, count(*) cnt
from   comm
group by comment_board_num;

-- 2. board와 조인을 합니다.
BOARD_NUM	BOARD_SUBJECT	CNT
1			오늘도 행복하세요		2
2			둘째				2

select board_num, board_subject, cnt
from board join (select comment_board_num, count(*) cnt
				 from comm
				 group by comment_board_num)
on board_num=comment_board_num;

BOARD_NUM	BOARD_SUBJECT	CNT
1			오늘도 행복하세요		2
2			둘째				2
3			세째				NULL

-- 2단계) cnt가 null인 경우 0으로 만들어요
select board_num, board_subject, nvl(cnt,0) as cnt
from board left outer join (select comment_board_num, count(*) cnt 
							from comm
							group by comment_board_num)
on board_num=comment_board_num
order by BOARD_RE_REF desc, 
BOARD_RE_SEQ asc;

