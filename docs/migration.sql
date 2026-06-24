USE tlias;

ALTER TABLE `emp` ADD COLUMN `salary` DECIMAL(10,2) DEFAULT NULL AFTER `job`;

ALTER TABLE `class` ADD COLUMN `classroom` VARCHAR(20) DEFAULT NULL AFTER `name`;
ALTER TABLE `class` ADD COLUMN `subject` VARCHAR(50) DEFAULT NULL AFTER `status`;

ALTER TABLE `student` ADD COLUMN `student_no` VARCHAR(20) DEFAULT NULL AFTER `id`;
ALTER TABLE `student` ADD COLUMN `is_college` TINYINT DEFAULT 0 AFTER `id_card`;
ALTER TABLE `student` ADD COLUMN `address` VARCHAR(100) DEFAULT NULL AFTER `is_college`;
ALTER TABLE `student` ADD COLUMN `violation_count` INT DEFAULT 0 AFTER `study_status`;
ALTER TABLE `student` ADD COLUMN `violation_score` INT DEFAULT 0 AFTER `violation_count`;

DROP TABLE IF EXISTS `work_experience`;
CREATE TABLE `work_experience` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `emp_id` INT NOT NULL,
  `start_date` DATE DEFAULT NULL,
  `end_date` DATE DEFAULT NULL,
  `company` VARCHAR(50) DEFAULT NULL,
  `position` VARCHAR(50) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

UPDATE `emp` SET `salary` = 15000.00 WHERE `username` = 'admin';
UPDATE `emp` SET `salary` = 12000.00 WHERE `username` = 'zhangsan';
UPDATE `emp` SET `salary` = 8000.00 WHERE `username` = 'lisi';
UPDATE `emp` SET `salary` = 10000.00 WHERE `username` = 'wangwu';

UPDATE `class` SET `classroom` = '308', `subject` = 'Java' WHERE `id` = 1;
UPDATE `class` SET `classroom` = '1204', `subject` = '前端' WHERE `id` = 2;

UPDATE `student` SET `student_no` = 'A220505001', `is_college` = 1, `address` = '北京市昌平区' WHERE `id` = 1;
UPDATE `student` SET `student_no` = 'A220505002', `is_college` = 1, `address` = '上海市浦东新区' WHERE `id` = 2;
UPDATE `student` SET `student_no` = 'A220505003', `is_college` = 0, `address` = '广州市天河区' WHERE `id` = 3;
UPDATE `student` SET `student_no` = 'A220505004', `is_college` = 1, `address` = '深圳市南山区', `violation_count` = 1, `violation_score` = 5 WHERE `id` = 4;
UPDATE `student` SET `student_no` = 'A220505005', `is_college` = 1, `address` = '武汉市洪山区' WHERE `id` = 5;

INSERT INTO `work_experience` (`emp_id`, `start_date`, `end_date`, `company`, `position`)
VALUES (1, '2020-01-01', '2022-06-30', '阿里巴巴', '技术经理'),
       (1, '2022-07-01', NULL, '智学云帆', '系统管理员');
