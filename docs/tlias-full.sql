-- ============================================
-- 智学云帆 - 教学管理系统 完整数据库脚本
-- 版本：v1.6.0
-- 包含：建表 + 操作日志表 + 扩展字段 + 工作经历表 + 测试数据
-- ============================================

CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE tlias;

-- ============================================
-- 第一部分：创建核心表结构
-- ============================================

-- 1. 部门表
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，部门ID',
  `name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 2. 员工表（含薪资字段）
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，员工ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别（0-女，1-男）',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `job` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `salary` DECIMAL(10,2) DEFAULT NULL COMMENT '薪资',
  `dept_id` INT DEFAULT NULL COMMENT '所属部门ID',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 3. 班级表（含教室、学科字段）
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，班级ID',
  `name` VARCHAR(100) NOT NULL COMMENT '班级名称',
  `classroom` VARCHAR(20) DEFAULT NULL COMMENT '班级教室',
  `teacher_id` INT DEFAULT NULL COMMENT '班主任ID',
  `start_date` DATE DEFAULT NULL COMMENT '开班时间',
  `end_date` DATE DEFAULT NULL COMMENT '结课时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态（0-未开班，1-开班中，2-已结课）',
  `subject` VARCHAR(50) DEFAULT NULL COMMENT '学科',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 4. 学员表（含学号、是否院校学员、联系地址、违纪字段）
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，学员ID',
  `student_no` VARCHAR(20) DEFAULT NULL COMMENT '学号',
  `name` VARCHAR(50) NOT NULL COMMENT '学员姓名',
  `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别（0-女，1-男）',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
  `is_college` TINYINT DEFAULT 0 COMMENT '是否院校学员（0-否，1-是）',
  `address` VARCHAR(100) DEFAULT NULL COMMENT '联系地址',
  `class_id` INT DEFAULT NULL COMMENT '所属班级ID',
  `education` VARCHAR(50) DEFAULT NULL COMMENT '学历',
  `school` VARCHAR(100) DEFAULT NULL COMMENT '毕业院校',
  `major` VARCHAR(100) DEFAULT NULL COMMENT '专业',
  `enroll_date` DATE DEFAULT NULL COMMENT '报名日期',
  `study_status` TINYINT NOT NULL DEFAULT 0 COMMENT '学习状态（0-在读，1-休学，2-毕业）',
  `violation_count` INT DEFAULT 0 COMMENT '违纪次数',
  `violation_score` INT DEFAULT 0 COMMENT '违纪扣分',
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
  `class_id` INT DEFAULT NULL COMMENT '参考班级ID',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `total_score` INT NOT NULL DEFAULT 100 COMMENT '总分',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态（0-未发布，1-进行中，2-已结束）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试表';

-- 6. 就业信息表
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，就业ID',
  `student_id` INT NOT NULL COMMENT '学员ID',
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
  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '类型（0-学员反馈，1-员工反馈）',
  `title` VARCHAR(200) NOT NULL COMMENT '反馈标题',
  `content` TEXT NOT NULL COMMENT '反馈内容',
  `contact` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态（0-未处理，1-处理中，2-已处理）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈表';

-- 8. 操作日志表
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '操作人',
  `method` VARCHAR(20) DEFAULT NULL COMMENT '请求方法',
  `url` VARCHAR(255) DEFAULT NULL COMMENT '请求路径',
  `params` TEXT COMMENT '请求参数',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 9. 工作经历表
DROP TABLE IF EXISTS `work_experience`;
CREATE TABLE `work_experience` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `emp_id` INT NOT NULL COMMENT '员工ID',
  `start_date` DATE DEFAULT NULL COMMENT '开始日期',
  `end_date` DATE DEFAULT NULL COMMENT '结束日期',
  `company` VARCHAR(50) DEFAULT NULL COMMENT '公司名称',
  `position` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作经历表';

-- ============================================
-- 第二部分：插入测试数据
-- ============================================

-- 1. 部门数据（15条）
INSERT INTO dept (name, create_time, update_time) VALUES
('教研部', NOW(), NOW()), ('市场部', NOW(), NOW()), ('技术部', NOW(), NOW()),
('行政部', NOW(), NOW()), ('财务部', NOW(), NOW()), ('人事部', NOW(), NOW()),
('运维部', NOW(), NOW()), ('产品部', NOW(), NOW()), ('设计部', NOW(), NOW()),
('质量保证部', NOW(), NOW()), ('项目管理部', NOW(), NOW()), ('数据分析部', NOW(), NOW()),
('安全合规部', NOW(), NOW()), ('客户成功部', NOW(), NOW()), ('战略发展部', NOW(), NOW());

-- 2. 员工数据（16条，含admin）
-- admin 账号密码：123456（BCrypt加密），所有员工密码统一为 123456
INSERT INTO emp (username, password, name, gender, phone, job, salary, dept_id, entry_date, create_time, update_time) VALUES
('admin', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '系统管理员', 1, '13800138000', '系统管理员', 20000.00, 3, '2023-01-01', NOW(), NOW()),
('zhangsan', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '张三', 1, '13800138001', '教研主管', 15000.00, 1, '2023-03-01', NOW(), NOW()),
('lisi', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '李四', 0, '13800138002', '市场专员', 8000.00, 2, '2023-04-15', NOW(), NOW()),
('wangwu', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '王五', 1, '13800138003', 'Java讲师', 12000.00, 1, '2023-05-20', NOW(), NOW()),
('zhaoliu', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '赵六', 1, '13800138004', '前端讲师', 11000.00, 1, '2023-06-10', NOW(), NOW()),
('sunqi', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '孙七', 0, '13800138005', '班主任', 9000.00, 6, '2023-07-01', NOW(), NOW()),
('zhouba', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '周八', 1, '13800138006', '技术经理', 18000.00, 3, '2023-02-15', NOW(), NOW()),
('wujiu', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '吴九', 1, '13800138007', '运维工程师', 10000.00, 7, '2023-08-01', NOW(), NOW()),
('zhengshi', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '郑十', 0, '13800138008', '产品经理', 14000.00, 8, '2023-09-01', NOW(), NOW()),
('chenyi', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '陈一', 1, '13800138009', 'UI设计师', 9500.00, 9, '2023-10-01', NOW(), NOW()),
('liuer', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '刘二', 0, '13800138010', '测试工程师', 8500.00, 10, '2023-11-01', NOW(), NOW()),
('huangsan', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '黄三', 1, '13800138011', '项目经理', 16000.00, 11, '2023-12-01', NOW(), NOW()),
('linsi', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '林四', 0, '13800138012', '数据分析师', 11000.00, 12, '2024-01-15', NOW(), NOW()),
('xuwu', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '吴五', 1, '13800138013', '安全工程师', 13000.00, 13, '2024-02-01', NOW(), NOW()),
('yangliu', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '杨六', 0, '13800138014', '客户成功经理', 10500.00, 14, '2024-03-01', NOW(), NOW()),
('heqi', '$2b$10$IimNT/lWKphJJKhdzPzTKecw0djCUU7hAg/Lx.OVbJ7t./qDX5Jhm', '何七', 1, '13800138015', '战略规划师', 15000.00, 15, '2024-04-01', NOW(), NOW());

-- 3. 班级数据（15条）
-- teacher_id 对应员工ID：admin=1, 张三=2, 李四=3, 王五=4, 赵六=5, 孙七=6, 周八=7, 吴九=8
INSERT INTO class (name, classroom, teacher_id, start_date, end_date, status, subject, create_time, update_time) VALUES
('JavaEE就业300期', '308', 4, '2024-03-01', '2024-09-01', 2, 'Java', NOW(), NOW()),
('前端开发258期', '1204', 5, '2024-04-01', '2024-10-01', 2, '前端', NOW(), NOW()),
('JavaEE就业301期', '309', 4, '2024-06-01', '2024-12-01', 1, 'Java', NOW(), NOW()),
('前端开发259期', '1205', 5, '2024-07-01', '2025-01-01', 1, '前端', NOW(), NOW()),
('Python大数据101期', '401', 3, '2024-08-01', '2025-02-01', 1, '大数据', NOW(), NOW()),
('JavaEE就业302期', '310', 4, '2024-09-01', '2025-03-01', 1, 'Java', NOW(), NOW()),
('前端开发260期', '1206', 5, '2024-10-01', '2025-04-01', 0, '前端', NOW(), NOW()),
('Go语言开发50期', '501', 7, '2024-11-01', '2025-05-01', 0, 'Go', NOW(), NOW()),
('嵌入式开发30期', '601', 8, '2024-12-01', '2025-06-01', 0, '嵌入式', NOW(), NOW()),
('JavaEE就业303期', '311', 4, '2025-01-01', '2025-07-01', 0, 'Java', NOW(), NOW()),
('前端开发261期', '1207', 5, '2025-02-01', '2025-08-01', 0, '前端', NOW(), NOW()),
('Python大数据102期', '402', 3, '2025-03-01', '2025-09-01', 0, '大数据', NOW(), NOW()),
('JavaEE就业304期', '312', 4, '2025-04-01', '2025-10-01', 0, 'Java', NOW(), NOW()),
('前端开发262期', '1208', 5, '2025-05-01', '2025-11-01', 0, '前端', NOW(), NOW()),
('全栈开发101期', '701', 7, '2025-06-01', '2025-12-01', 0, 'Java', NOW(), NOW());

-- 4. 学员数据（15条）
-- class_id 对应班级ID：JavaEE300期=1, 前端258期=2, JavaEE301期=3, 前端259期=4, Python101期=5, JavaEE302期=6, 前端260期=7
INSERT INTO student (student_no, name, gender, phone, id_card, is_college, address, class_id, education, school, major, enroll_date, study_status, violation_count, violation_score, job_status, create_time, update_time) VALUES
('A220505001', '赵敏', 0, '13900139001', '110101199501011234', 1, '北京市海淀区', 1, '本科', '北京大学', '计算机科学', '2024-03-01', 2, 0, 0, 1, NOW(), NOW()),
('A220505002', '钱多多', 1, '13900139002', '110101199602021234', 1, '北京市朝阳区', 1, '本科', '清华大学', '软件工程', '2024-03-01', 2, 0, 0, 1, NOW(), NOW()),
('A220505003', '孙悦', 0, '13900139003', '110101199703031234', 1, '上海市浦东新区', 2, '硕士', '复旦大学', '计算机科学与技术', '2024-04-01', 2, 0, 0, 1, NOW(), NOW()),
('A220505004', '周杰', 1, '13900139004', '110101199804041234', 1, '广州市天河区', 1, '本科', '浙江大学', '信息技术', '2024-03-01', 2, 1, 5, 1, NOW(), NOW()),
('A220505005', '吴用', 1, '13900139005', '110101199905051234', 0, '深圳市南山区', 2, '本科', '武汉大学', '网络工程', '2024-04-01', 2, 0, 0, 1, NOW(), NOW()),
('A220505006', '郑爽', 0, '13900139006', '110101200006061234', 1, '杭州市西湖区', 3, '本科', '南京大学', '软件工程', '2024-06-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505007', '王磊', 1, '13900139007', '110101200107071234', 1, '成都市武侯区', 3, '硕士', '电子科技大学', '计算机科学', '2024-06-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505008', '冯小刚', 1, '13900139008', '110101200208081234', 0, '武汉市洪山区', 4, '本科', '华中科技大学', '软件工程', '2024-07-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505009', '陈晓', 0, '13900139009', '110101200309091234', 1, '南京市鼓楼区', 4, '本科', '东南大学', '计算机科学', '2024-07-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505010', '楚天阔', 1, '13900139010', '110101200410101234', 1, '西安市雁塔区', 5, '硕士', '西安交通大学', '大数据技术', '2024-08-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505011', '魏晨', 1, '13900139011', '110101200511111234', 0, '重庆市渝中区', 5, '本科', '重庆大学', '数据科学', '2024-08-01', 1, 2, 10, 0, NOW(), NOW()),
('A220505012', '蒋欣', 0, '13900139012', '110101200612121234', 1, '天津市南开区', 6, '本科', '南开大学', '软件工程', '2024-09-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505013', '沈腾', 1, '13900139013', '110101200701131234', 1, '哈尔滨市南岗区', 6, '本科', '哈尔滨工业大学', '计算机科学', '2024-09-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505014', '韩雪', 0, '13900139014', '110101200802141234', 0, '长沙市岳麓区', 7, '本科', '中南大学', '软件工程', '2024-10-01', 0, 0, 0, 0, NOW(), NOW()),
('A220505015', '杨洋', 1, '13900139015', '110101200903151234', 1, '合肥市包河区', 7, '硕士', '中国科学技术大学', '计算机科学', '2024-10-01', 0, 0, 0, 0, NOW(), NOW());

-- 5. 考试数据（15条）
-- class_id 对应班级ID
INSERT INTO exam (title, class_id, start_time, end_time, total_score, status, create_time, update_time) VALUES
('Java基础测试', 1, '2024-04-15 09:00:00', '2024-04-15 11:00:00', 100, 2, NOW(), NOW()),
('HTML/CSS测试', 2, '2024-05-15 09:00:00', '2024-05-15 11:00:00', 100, 2, NOW(), NOW()),
('Java面向对象测试', 1, '2024-05-20 09:00:00', '2024-05-20 11:00:00', 100, 2, NOW(), NOW()),
('JavaScript基础测试', 2, '2024-06-15 09:00:00', '2024-06-15 11:00:00', 100, 2, NOW(), NOW()),
('Java集合框架测试', 3, '2024-07-15 09:00:00', '2024-07-15 11:00:00', 100, 2, NOW(), NOW()),
('Vue.js框架测试', 4, '2024-08-15 09:00:00', '2024-08-15 11:00:00', 100, 2, NOW(), NOW()),
('Java数据库编程测试', 3, '2024-09-01 09:00:00', '2024-09-01 11:00:00', 100, 1, NOW(), NOW()),
('React框架测试', 4, '2024-10-01 09:00:00', '2024-10-01 11:00:00', 100, 1, NOW(), NOW()),
('Python基础测试', 5, '2024-09-15 09:00:00', '2024-09-15 11:00:00', 100, 1, NOW(), NOW()),
('JavaWeb综合测试', 6, '2024-10-15 09:00:00', '2024-10-15 11:00:00', 100, 1, NOW(), NOW()),
('前端综合项目测试', 7, '2024-11-15 09:00:00', '2024-11-15 11:00:00', 100, 0, NOW(), NOW()),
('Go语言基础测试', 8, '2024-12-15 09:00:00', '2024-12-15 11:00:00', 100, 0, NOW(), NOW()),
('嵌入式系统测试', 9, '2025-01-15 09:00:00', '2025-01-15 11:00:00', 100, 0, NOW(), NOW()),
('Java高级特性测试', 10, '2025-02-15 09:00:00', '2025-02-15 11:00:00', 100, 0, NOW(), NOW()),
('全栈项目答辩', 15, '2025-07-01 09:00:00', '2025-07-01 17:00:00', 100, 0, NOW(), NOW());

-- 6. 就业数据（15条）
-- student_id 对应学员ID：赵敏=1, 钱多多=2, 孙悦=3, 周杰=4, 吴用=5, 郑爽=6, 王磊=7, 冯小刚=8, 陈晓=9, 楚天阔=10, 魏晨=11, 蒋欣=12, 沈腾=13, 韩雪=14, 杨洋=15
INSERT INTO job (student_id, company, position, salary, job_date, city, create_time, update_time) VALUES
(1, '阿里巴巴股份有限公司', 'Java开发工程师', 18000.00, '2024-09-15', '杭州', NOW(), NOW()),
(2, '腾讯科技有限公司', '前端开发工程师', 16000.00, '2024-09-20', '深圳', NOW(), NOW()),
(3, '百度在线网络技术有限公司', '算法工程师', 20000.00, '2024-10-01', '北京', NOW(), NOW()),
(4, '字节跳动科技有限公司', 'Java开发工程师', 17000.00, '2024-09-25', '北京', NOW(), NOW()),
(5, '美团点评科技有限公司', '全栈开发工程师', 15000.00, '2024-10-10', '北京', NOW(), NOW()),
(6, '京东集团股份有限公司', 'Java开发工程师', 14000.00, '2024-10-15', '北京', NOW(), NOW()),
(7, '网易有道信息技术有限公司', '后端开发工程师', 15500.00, '2024-10-20', '杭州', NOW(), NOW()),
(8, '华为技术有限公司', '软件开发工程师', 18000.00, '2024-11-01', '深圳', NOW(), NOW()),
(9, '小米科技有限公司', '前端开发工程师', 13000.00, '2024-11-10', '北京', NOW(), NOW()),
(10, '滴滴出行科技有限公司', '数据工程师', 16500.00, '2024-11-15', '北京', NOW(), NOW()),
(11, '中国平安保险股份有限公司', 'Java开发工程师', 14500.00, '2024-12-01', '深圳', NOW(), NOW()),
(12, '招商银行股份有限公司', '全栈开发工程师', 15000.00, '2024-12-10', '深圳', NOW(), NOW()),
(13, '海康威视数字技术股份有限公司', '嵌入式开发工程师', 14000.00, '2024-12-15', '杭州', NOW(), NOW()),
(14, '大疆创新科技有限公司', '软件开发工程师', 16000.00, '2025-01-01', '深圳', NOW(), NOW()),
(15, '比亚迪股份有限公司', '测试开发工程师', 12000.00, '2025-01-10', '深圳', NOW(), NOW());

-- 7. 反馈数据（15条）
INSERT INTO feedback (type, title, content, contact, phone, status, create_time, update_time) VALUES
(0, '课程进度较快', 'Java基础部分内容较多，希望能适当放慢进度', '赵敏', '13900139001', 2, NOW(), NOW()),
(0, '项目实战不足', '希望增加更多企业级项目实战练习', '钱多多', '13900139002', 1, NOW(), NOW()),
(1, '办公设备老化', '技术部电脑运行缓慢，影响工作效率', '王五', '13800138003', 0, NOW(), NOW()),
(0, '就业指导需求', '希望学校能提供更多就业指导和面试技巧培训', '周杰', '13900139004', 1, NOW(), NOW()),
(0, '课程内容更新', '部分技术栈较老，希望更新到最新版本', '吴用', '13900139005', 2, NOW(), NOW()),
(1, '会议室预约系统', '希望开发一个会议室在线预约系统', '郑爽', '13900139006', 0, NOW(), NOW()),
(0, '晚自习安排', '希望增加晚自习时间，有老师答疑', '王磊', '13900139007', 2, NOW(), NOW()),
(0, '教材建议', '部分教材印刷质量较差，建议更换供应商', '冯小刚', '13900139008', 1, NOW(), NOW()),
(1, '网络带宽不足', '办公室网络经常卡顿，影响开发效率', '陈晓', '13900139009', 0, NOW(), NOW()),
(0, '食堂改进', '食堂菜品单一，希望增加更多选择', '楚天阔', '13900139010', 2, NOW(), NOW()),
(0, '实习机会', '希望学校能提供更多企业实习机会', '魏晨', '13900139011', 1, NOW(), NOW()),
(1, '空调维修', '3楼办公室空调制冷效果不好', '蒋欣', '13900139012', 0, NOW(), NOW()),
(0, '课程评价', '讲师授课认真负责，课程内容实用', '沈腾', '13900139013', 2, NOW(), NOW()),
(0, '班级活动', '希望组织更多班级团建活动增进同学感情', '韩雪', '13900139014', 1, NOW(), NOW()),
(1, '停车位不足', '员工停车位紧张，建议错峰停车', '杨洋', '13900139015', 0, NOW(), NOW());

-- 8. 工作经历数据（15条）
-- emp_id 对应员工ID：张三=2, 王五=4, 赵六=5, 周八=7, 吴九=8, 郑十=9, 陈一=10
INSERT INTO work_experience (emp_id, start_date, end_date, company, position, create_time, update_time) VALUES
(2, '2019-03-01', '2021-06-30', '阿里巴巴', 'Java开发工程师', NOW(), NOW()),
(2, '2021-07-01', '2023-02-28', '腾讯', '高级Java工程师', NOW(), NOW()),
(4, '2018-07-01', '2020-12-31', '百度', 'Java讲师', NOW(), NOW()),
(4, '2021-01-01', '2023-04-30', '京东', '技术培训师', NOW(), NOW()),
(5, '2017-09-01', '2019-08-31', '美团', '前端开发工程师', NOW(), NOW()),
(5, '2019-09-01', '2023-05-31', '字节跳动', '高级前端工程师', NOW(), NOW()),
(7, '2015-07-01', '2018-06-30', '华为', '软件开发工程师', NOW(), NOW()),
(7, '2018-07-01', '2020-12-31', '中兴通讯', '技术经理', NOW(), NOW()),
(7, '2021-01-01', '2023-01-31', '小米', '技术总监', NOW(), NOW()),
(8, '2016-07-01', '2019-06-30', '浪潮集团', '运维工程师', NOW(), NOW()),
(8, '2019-07-01', '2023-07-31', '阿里云', '高级运维工程师', NOW(), NOW()),
(9, '2018-03-01', '2020-12-31', '网易', '产品经理', NOW(), NOW()),
(9, '2021-01-01', '2023-08-31', '快手', '高级产品经理', NOW(), NOW()),
(10, '2019-07-01', '2022-06-30', '滴滴', 'UI设计师', NOW(), NOW()),
(10, '2022-07-01', '2023-09-30', '蚂蚁集团', '高级设计师', NOW(), NOW());
