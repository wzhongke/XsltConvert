CREATE TABLE [IF NOT EXISTS] table_name (
	column_name data_type,
	...
);
CREATE TABLE tb1 (
	id TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,  -- 自增的主键
	username VARCHAR(20) NOT NULL UNIQUE KEY, -- 唯一约束
	age TINYINT(1) UNSIGNED NULL DEFAULT 3, -- 当插入记录时，若没有明确赋值，则赋予默认值
	salary FLOAT(8,2) UNSIGNED
);
DESC table_name; -- 查看数据表结构
SHOW CREATE tab_name;

SHOW TABLES [FROM db_name] [LIKE `pattern` | WHERE expr];
SHOW COLUMNS FROM tb_name;  -- 查看数据表结构

INSERT [INTO] tb_name [(col_name, ...)] VALUES(val, ...) -- 如果省略列名，所有的value都必须有
INSERT tb1 VALUES('Tom', 25, 7863.25);

SELECT expr, ... FROM tb_name;

CREATE TABLE provice (
	id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL
);

CREATE TABLE users(
	id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(10) NOT NULL,
	pid SMALLINT UNSIGNED,
	FOREIGN KEY (pid) REFERENCES provice(id) ON DELETE CASCADE   -- 从父表删除或更新且自动删除或更新子表中匹配的行
);

SHOW INDEXES FROM provice;
-- 添加列
ALTER TABLE tb_name ADD [COLUMN] col_name column_definition [FIRST | AFTER]
-- 删除列
ALTER TABLE tb_name DROP col1, DROP col2;

ALTER TABLE tb_name ADD CONSTRAINT [symbol] PRIMARY KEY [index_type] col_name;
-- 添加唯一约束
ALTER TABLE tb_name ADD CONSTRAINT [symbol] UNIQUE KEY [index_type] col_name;
-- 添加外键约束
ALTER TABLE tb_name ADD CONSTRAINT [symbol] FOREIGN KEY [index_type] (col_name) REFERENCES tb (col_name);
-- 添加/删除默认约束
ALTER TABLE tb ALTER [COLUMN] col_name {SET DEFAULT literal | DROP DEFAULT}
-- 删除主键约束
ALTER TABLE tb_name DROP PRIMARY KEY;
-- 删除唯一约束
ALTER TABLE tb_name DROP INDEX col_name;
-- 删除外键约束
ALTER TABLE tb_name DROP FOREIGN KEY key_symbol;

-- 修改数据表
ALTER TABLE tb_name MODIFY [COLUMN] col_name column_definition  [FIRST | AFTER];
ALTER TABLE users MODIFY id TINYINT UNSIGNED NOT NULL FIRST;
-- 修改列名称
ALTER TABLE tb_name CHANGE old_colname new_colname col_definition [FIRST | AFTER col_name];

-- 修改数据表名称
ALTER TABLE tb_name RENAME [TO | AS] new_tbname;
RENAME TABLE tb_name TO new_tb_name [, tb2_name TO new_tb_name2];

-- INSERT ： ①可以一次插入多条数据
INSERT [INTO] tb_name [(col_name, ...)] {VALUES | VALUE} ({expr | DEFAULT},...),(...), ...
-- ②如果为默认自动编号值赋值，可以使用NULL或DEFAULT
INSERT users VALUES(NULL, 'Tom', md5('123'), 25,1);
-- ③使用SET，只能一次插入一条记录
INSERT [INTO] tb_name SET col_name={expr | DEFAULT},...
INSERT users SET username='ben', password='456';

INSERT [INTO] tb_name [(col_name,...)] SELECT ...;
INSERT test(username) SELECT username FROM users WHERE age>=30;

-- 多表插入
INSERT [INTO] tb_name [(col_name, ...)] SELECT ...

INSERT tdb_goods_cates(cate_name) SELECT goods_cate FROM tdb_goods GROUP BY goods_cate;

--UPDATE语句
--1.单表更新
UPDATE [LOW_PRIOROTY] [IGNORE] table_reference SET col_name1={expr1|DEFAULT} [, col_name2={expr2|DEFAULT] ...
[WHERE where_condition]

UPDATE users SET age=age-id, sex=0;
UPDATE users SET age=age+10 WHERE id%2=0;
--2.多表更新
UPDATE table_references SET col_name1={expr1|DEFAULT} [, col_name2={expr2|DEFAULT] ... [WHERE where_condition]

UPDATE table_name INNER JOIN tdb_goods_cates ON goods_cate = cate_name SET goods_cate=cate_id;

UPDATE tdb_goods 

--DELETE
--1. 单表删除
DELETE FROM tb_name WHERE condition;

--2. 多表删除
DELETE tbl_name[.*] [, tbl_name[.*]] ... FROM table_references [WHERE where_conditionw]

DELETE 	t1 FROM tdb_goods AS t1 LEFT JOIN 
(SELECT goods_id, goods_name FROM tdb_goods GROUP BY goods_name HAVING COUNT(goods_nam) > 2 ) AS t2 
ON t1.goods_name=t2.goods_name WHERE t1.goods_id > t2.goods_id;

--SELECT 
SELECT select_expr [, select_expr2] 
[
	FROM table_reference 
	[WHERE where_condition]
	[GROUP BY {col_name1 | position} [ASC | DESC],...]
	[HAVING where_condition] --聚合函数 (SUM,AVG) 或字段出现在SELECT后
	[ORDER BY {col_name |expr|position} [ASC |DESC],...]
	[LIMIT {[offset,] row_count | row_count OFFSET offset}]
]

-- 查询表达式
-- 每个表达式想要
-- 别名可用于GROUP BY, HAVING或ORDER BY
SELECT sex, age FROM users GROUP BY sex HAVING age>35; 


---- 子查询: 指嵌套在查询内部，且必须始终出现在圆括号内 ----
SET NAMES gbk;   -- 以GBK编码查看数据，不影响数据库原始数据

SELECT * FROM t1 WHERE col1 = (SELECT col2 FROM t2);

-- 1. 使用比较运算符的子查询：=, >, <, >=, <=, <>, !=, <>
SELECT ROUND(AVG(goods_price), 2) FROM tdb_goods;
SELECT goods_id, goods_name FROM tbd_goods WHERE goods_price >= (SELECT ROUND(AVG(goods_price), 2) FROM tdb_goods);

--2. ANY, SOME 或 ALL
--   ANY, SOME : 符合其中任一个
--   ALL : 全部符合
SELECT goods_id, goods_name FROM tbd_goods WHERE goods_price >= ANY(SELECT goods_proce FROM tdb_goods where goods_name='超极本');

--3. IN 等价于 =ANY() , = SOME()
--   NOT IN 等价于 != ALL()

--4. EXISTS： 如果子查询返回任何行，EXISTS返回true；否则返回FALSE


-- 创建数据表同时将查询结果写入到数据表
CREATE TABLE [IF NOT EXISTS] tbl_name [(create_definition, ...)] select_statement;

CREATE TABLE tdb_good_brands (
	brand_id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	brand_name VARCHAR(40) NOT NULL
) SELECT brand_name FROM tdb_goods GROUP BY brand_name;

--连接

table_reference {[INNER | CROSS] JOIN | {LEFT|RIGHT} [OUTER] JOIN } table_reference ON conditional_expr;

-- FROM 子句的子查询中，必须为表赋予别名

-- 内连接： 只显示符合条件的记录
-- 左外连接： 现实坐标中的全部记录，和右表中符合条件的记录

-- 多表连接
SELECT goods_id, goods_name, cate_name, brand_name, goods_price FROM tdb_goods AS g 
INNER JOIN tdb_goods_cates AS C ON g.cate_id = c.cate_id
INNER JOIN tdb_goods_brands AS b ON g.brand_id = b.brand_id;

-- 自身连接
SELECT s.type_id, s.type_name, p.type_name FROM tdb_goods_types AS s LEFT JOIN tdb_goods_types AS p ON s.parent_id=p.type_id;


-- 运算符 和 函数

SELECT TRIM(LEADING '?' FROM '??MySQL??');  -- MySQL??
SELECT TRIM(TRAILING '?' FROM '??MySQL??');  -- ??MySQL
SELECT TRIM(BOTH '?' FROM '??MySQL??');  -- MySQL
SELECT REPLACE('??My??SQL??','?' '');  -- MySQL

SUBSTRING('MySQL', 1, 3 ) -- My


SELECT DATE_ADD('2014-3-12' , INTERVAL 3 WEEK);
SELECT DATEDIFF('2014-3-12', '2015-4-12');
SELECT DATE_FORMAT('2014-5-12', '%m/%d/%Y');

-- 自定义函数
CREATE FUNCTION function_name 
RETURNS
{STRING|INTEGER|REAL|DECIMAL} 
routine_body;

CREATE FUNCTION f1() RETURNS VARCHAR(30)
RETURN DATA_FORMAT(NOW(), '%m/%d/%Y');

CREATE FUNCTION f2(num1 SMALLINT UNSIGNED, num2 SMALLINT UNSIGNED)
RETURNS FLOAT(10, 2) UNSIGNED
RETURN (num1 + num2) / 2;

-- 需要将MySQL的默认结束符 ; 修改, 复合结构必须包含在 BEGIN... END 之间
CREATE FUNCTION adduser(username VARCHAR(20))
RETURNS INT UNSIGNED
BEGIN
INSERT test(username) VALUES (username);
RETURN LAST_INSERT_ID();
END


-- 存储过程：
CREATE PROCEDURE sp1() SELECT VERSION();
CALL sp1[()]; 

DELIMITER //
CREATE PROCEDURE removeUserById(IN userId INT UNSIGNED) -- 参数名不能和数据表的字段名相同
BEGIN
DELETE FROM users WHERE id=userId
END
DELIMITER ;

CALL removeUserById(3);

DROP PROCEDURE IF EXISTS removeUserById;

CREATE PROCEDURE removeUserAndReturnUserNums(IN p_id INT USIGNED, OUT userNums INT UNSIGNED)
BEGIN
DELETE FROM users WHERE id=p_id;
SELECT count(id) FROM users INTO userNums;
END

CALL removeUserAndReturnUserNums(27, @nums);

SELECT @nums;

SET @i = 7; -- 设置变量


CREATE PROCEDURE removeUserByAgeAndReturnInfos (IN p_age SMALLINT UNSIGNED, OUT deleteUser SMALLINT UNSIGNED, OUT userCounts SMALLINT UNSIGNED) 
BEGIN
DELETE FROM users WHERE age=p_age;
SELECT ROW_COUNT() INTO deleteUser;
SELECT COUNT(id)FROM users INTO userCounts;
END

CALL removeUserByAgeAndReturnInfos(20, @a, @b);


