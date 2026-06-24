-- 智学云帆 - 教学管理系统 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE tlias;

-- 1. 部门表
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，部门ID',
  `name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 2. 员工表
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，员工ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别（0-女，1-男）',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `job` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `dept_id` INT DEFAULT NULL COMMENT '所属部门ID（外键）',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 3. 班级表
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，班级ID',
  `name` VARCHAR(100) NOT NULL COMMENT '班级名称',
  `teacher_id` INT DEFAULT NULL COMMENT '班主任ID（外键→emp）',
  `start_date` DATE DEFAULT NULL COMMENT '开班时间',
  `end_date` DATE DEFAULT NULL COMMENT '结课时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '班级状态（0-未开班，1-开班中，2-已结课）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 4. 学员表
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，学员ID',
  `name` VARCHAR(50) NOT NULL COMMENT '学员姓名',
  `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别（0-女，1-男）',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
  `class_id` INT DEFAULT NULL COMMENT '所属班级ID（外键）',
  `education` VARCHAR(50) DEFAULT NULL COMMENT '学历',
  `school` VARCHAR(100) DEFAULT NULL COMMENT '毕业院校',
  `major` VARCHAR(100) DEFAULT NULL COMMENT '专业',
  `enroll_date` DATE DEFAULT NULL COMMENT '报名日期',
  `study_status` TINYINT NOT NULL DEFAULT 0 COMMENT '学习状态（0-在读，1-休学，2-毕业）',
  `job_status` TINYINT NOT NULL DEFAULT 0 COMMENT '就业状态（0-未就业，1-已就业）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员表';

-- 5. 考试表
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，考试ID',
  `title` VARCHAR(200) NOT NULL COMMENT '考试标题',
  `class_id` INT DEFAULT NULL COMMENT '参考班级ID（外键）',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `total_score` INT NOT NULL DEFAULT 100 COMMENT '总分',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '考试状态（0-未发布，1-进行中，2-已结束）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试表';

-- 6. 就业信息表
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，就业ID',
  `student_id` INT NOT NULL COMMENT '学员ID（外键→student）',
  `company` VARCHAR(200) DEFAULT NULL COMMENT '就业公司',
  `position` VARCHAR(100) DEFAULT NULL COMMENT '就业岗位',
  `salary` DECIMAL(10,2) DEFAULT NULL COMMENT '薪资',
  `job_date` DATE DEFAULT NULL COMMENT '就业日期',
  `city` VARCHAR(100) DEFAULT NULL COMMENT '就业城市',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就业信息表';

-- 7. 反馈表
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，反馈ID',
  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '反馈类型（0-学员反馈，1-员工反馈）',
  `title` VARCHAR(200) NOT NULL COMMENT '反馈标题',
  `content` TEXT NOT NULL COMMENT '反馈内容',
  `contact` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态（0-未处理，1-处理中，2-已处理）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈表';

-- 插入测试数据
-- 部门数据
INSERT INTO `dept` (`name`) VALUES ('教研部'), ('市场部'), ('技术部'), ('行政部');

-- 默认管理员账号 (密码: 123456, BCrypt加密)
INSERT INTO `emp` (`username`, `password`, `name`, `gender`, `phone`, `job`, `dept_id`, `entry_date`)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '系统管理员', 1, '13800138000', '系统管理员', 3, '2024-01-01');

-- 测试员工数据 (密码: 123456)
INSERT INTO `emp` (`username`, `password`, `name`, `gender`, `phone`, `job`, `dept_id`, `entry_date`)
VALUES
('zhangsan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '张三', 1, '13800138001', '教研主管', 1, '2024-03-01'),
('lisi', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '李四', 0, '13800138002', '市场专员', 2, '2024-04-15'),
('wangwu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '王五', 1, '13800138003', 'Java讲师', 1, '2024-05-20');

-- 测试班级数据
INSERT INTO `class` (`name`, `teacher_id`, `start_date`, `end_date`, `status`)
VALUES ('JavaEE第001期', 3, '2024-06-01', '2024-12-01', 1),
       ('前端开发第001期', 4, '2024-07-01', '2024-12-15', 1);

-- 测试学员数据
INSERT INTO `student` (`name`, `gender`, `phone`, `id_card`, `class_id`, `education`, `school`, `major`, `enroll_date`, `study_status`, `job_status`)
VALUES ('赵六', 1, '13900139001', '110101199001011234', 1, '本科', '北京大学', '计算机科学', '2024-06-01', 0, 0),
       ('钱七', 0, '13900139002', '110101199102021234', 1, '本科', '清华大学', '软件工程', '2024-06-01', 0, 0),
       ('孙八', 1, '13900139003', '110101199203031234', 2, '硕士', '复旦大学', '计算机科学与技术', '2024-07-01', 0, 0),
       ('周九', 0, '13900139004', '110101199304041234', 1, '本科', '浙江大学', '信息技术', '2024-06-01', 2, 1),
       ('吴十', 1, '13900139005', '110101199405051234', 2, '本科', '武汉大学', '网络工程', '2024-07-01', 0, 0);

-- 测试就业数据
INSERT INTO `job` (`student_id`, `company`, `position`, `salary`, `job_date`, `city`)
VALUES (4, '北京某某科技有限公司', 'Java开发工程师', 15000.00, '2024-12-15', '北京');

-- 测试考试数据
INSERT INTO `exam` (`title`, `class_id`, `start_time`, `end_time`, `total_score`, `status`)
VALUES ('Java基础测试', 1, '2024-09-01 09:00:00', '2024-09-01 11:00:00', 100, 2),
       ('HTML/CSS测试', 2, '2024-10-01 09:00:00', '2024-10-01 11:00:00', 100, 0);

-- 测试反馈数据
INSERT INTO `feedback` (`type`, `title`, `content`, `contact`, `phone`, `status`)
VALUES (0, '课程难度较大', 'Java基础部分内容较多，希望能增加课时', '赵六', '13900139001', 1),
       (1, '办公设备老化', '技术部电脑运行缓慢，影响工作效率', '王五', '13800138003', 0);
