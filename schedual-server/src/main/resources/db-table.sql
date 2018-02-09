CREATE TABLE `tk_schedule_service` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tid` BIGINT(20) DEFAULT NULL COMMENT 'task-reminder表的id',
  `scheduelname` VARCHAR(32) DEFAULT NULL COMMENT '调度器名称',
  `clientid` VARCHAR(32) DEFAULT NULL COMMENT '发送给哪个客户端',
  `service` VARCHAR(256) DEFAULT NULL COMMENT '发送给客户端的业务（类型）',
  `status` INT(32) DEFAULT NULL COMMENT '状态：0.发送成功.1.发送失败',
  `clientrectime` DATETIME DEFAULT NULL COMMENT '客户端收到的时间',
  `errmsg` VARCHAR(32) DEFAULT NULL COMMENT '失败信息',
  `invoketime` DATETIME DEFAULT NULL COMMENT '发送给客户端时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=689 DEFAULT CHARSET=utf8



CREATE TABLE `tk_schedule_task` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键，更新这个时，用该标示移除内存数据',
  `clientid` VARCHAR(64) DEFAULT NULL,
  `taskid` VARCHAR(32) DEFAULT NULL COMMENT '任务id，任务的唯一标示，方便客户端，进行查询，修改，删除',
  `service` VARCHAR(256) DEFAULT NULL COMMENT '服务类型，客户端根据收到的服务类型字符串进行处理',
  `status` INT(11) DEFAULT NULL COMMENT '1：可用的，2.过期的，3表示逻辑删除的',
  `rate_minutes` INT(11) DEFAULT NULL COMMENT '每多少分钟',
  `rate_hours` INT(11) DEFAULT NULL COMMENT '每多少小时',
  `rate_days` INT(11) DEFAULT NULL COMMENT '每多少天',
  `rate_weeks` INT(11) DEFAULT NULL COMMENT '每多少星期',
  `rate_months` INT(11) DEFAULT NULL COMMENT '每多少月',
  `executetime` DATETIME DEFAULT NULL COMMENT '开始执行时间（不一定执行），配合分钟频率的，30分钟执行一次。',
  `year` VARCHAR(8) DEFAULT NULL COMMENT '年份，不确定用空表示',
  `month` VARCHAR(8) DEFAULT NULL COMMENT '月份，不确定用空表示',
  `week` VARCHAR(8) DEFAULT NULL COMMENT '星期，不确定用空表示',
  `day` VARCHAR(8) DEFAULT NULL COMMENT '日期，不确定用空表示',
  `hour` VARCHAR(8) DEFAULT NULL COMMENT '小时，不确定用空表示',
  `minute` VARCHAR(8) DEFAULT NULL COMMENT '分钟，不确定用空表示',
  `fix_range_minutes` VARCHAR(64) DEFAULT NULL COMMENT '某些分钟执行 逗号隔开',
  `fix_range_hours` VARCHAR(64) DEFAULT NULL COMMENT '某些小时的范围',
  `fix_range_days` VARCHAR(64) DEFAULT NULL COMMENT '某些天的范围',
  `fix_range_weeks` VARCHAR(64) DEFAULT NULL COMMENT '某些星期的范围',
  `fix_range_month` VARCHAR(64) DEFAULT NULL COMMENT '某些月的范围',
  `range_starttime` DATETIME DEFAULT NULL COMMENT '开始时间',
  `range_endtime` DATETIME DEFAULT NULL COMMENT '结束时间',
  `create_type` INT(11) DEFAULT '3' COMMENT '1表示客户端异步注册，2.表示客户端同步注册，3表示后台创建，4表示手动新增数据库',
  `createtime` DATETIME DEFAULT NULL COMMENT 'task创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=753 DEFAULT CHARSET=utf8