drop table if exists jp_dbprocessor_error;
create table `jp_dbprocessor_error` (
  `id` int(11) not null primary key AUTO_INCREMENT comment '自增ID',
  `class_name` varchar(100) not null comment '数据类型：order/mkt/goods/cart',
  `send_nanos` varchar(100) not null comment '发送纳秒',
  `send_date` datetime not null comment '发送时间',
  `eventid` varchar(200) not null comment 'mq eventId',
  `transactionid` varchar(200) default '' comment '事物id',
  `error_code` int(11) not null comment '错误编码：1、event过期错误；2、缓存version错误；3、拉取event错误；4、反射获取ORM错误；5、执行SQL错误；6、获取路由线程ID错误；7、衍生拉取3个版本错误；8、非法重复版本错误；',
  `data_json` varchar(3000) default '' comment 'json序列互数据',
  `sql_hendle` varchar(1000) default '' comment '执行的sql语句',
  `sql_parameter` varchar(1000) default '' comment '执行的sql语句的参数',
  `event_json` varchar(3000) default '' comment 'mq获取到的event数据json'
) ENGINE=InnoDB default CHARSET=utf8;