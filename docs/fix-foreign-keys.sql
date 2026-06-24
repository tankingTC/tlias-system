USE tlias;

-- ============================================
-- 修复外键关联：更新所有外键 ID 指向正确的当前记录
-- ============================================

-- 1. 修复 emp.dept_id → 指向正确的 dept ID (51-65)
-- 张三(36)=教研部(51), 李四(37)=市场部(52), 王五(38)=教研部(51)
-- 赵六(39)=教研部(51), 孙七(40)=人事部(56), 周八(41)=技术部(53)
-- 吴九(42)=运维部(57), 郑十(43)=产品部(58), 陈一(44)=设计部(59)
-- 刘二(45)=质量保证部(60), 黄三(46)=项目管理部(61), 林四(47)=数据分析部(62)
-- 吴五(48)=安全合规部(63), 杨六(49)=客户成功部(64), 何七(50)=战略发展部(65)
UPDATE emp SET dept_id = 51 WHERE id = 36;
UPDATE emp SET dept_id = 52 WHERE id = 37;
UPDATE emp SET dept_id = 51 WHERE id = 38;
UPDATE emp SET dept_id = 51 WHERE id = 39;
UPDATE emp SET dept_id = 56 WHERE id = 40;
UPDATE emp SET dept_id = 53 WHERE id = 41;
UPDATE emp SET dept_id = 57 WHERE id = 42;
UPDATE emp SET dept_id = 58 WHERE id = 43;
UPDATE emp SET dept_id = 59 WHERE id = 44;
UPDATE emp SET dept_id = 60 WHERE id = 45;
UPDATE emp SET dept_id = 61 WHERE id = 46;
UPDATE emp SET dept_id = 62 WHERE id = 47;
UPDATE emp SET dept_id = 63 WHERE id = 48;
UPDATE emp SET dept_id = 64 WHERE id = 49;
UPDATE emp SET dept_id = 65 WHERE id = 50;
UPDATE emp SET dept_id = 53 WHERE id = 1;  -- admin=技术部

-- 2. 修复 class.teacher_id → 指向正确的 emp ID (36-50)
-- Java讲师=王五(38), 前端讲师=赵六(39), 技术经理=周八(41)
-- 运维工程师=吴九(42)
UPDATE class SET teacher_id = 38 WHERE id = 18;  -- JavaEE300期 → 王五
UPDATE class SET teacher_id = 39 WHERE id = 19;  -- 前端258期 → 赵六
UPDATE class SET teacher_id = 38 WHERE id = 20;  -- JavaEE301期 → 王五
UPDATE class SET teacher_id = 39 WHERE id = 21;  -- 前端259期 → 赵六
UPDATE class SET teacher_id = 37 WHERE id = 22;  -- Python大数据101期 → 李四(市场专员,兼职)
UPDATE class SET teacher_id = 38 WHERE id = 23;  -- JavaEE302期 → 王五
UPDATE class SET teacher_id = 39 WHERE id = 24;  -- 前端260期 → 赵六
UPDATE class SET teacher_id = 41 WHERE id = 25;  -- Go语言50期 → 周八
UPDATE class SET teacher_id = 42 WHERE id = 26;  -- 嵌入式30期 → 吴九
UPDATE class SET teacher_id = 38 WHERE id = 28;  -- JavaEE303期 → 王五
UPDATE class SET teacher_id = 39 WHERE id = 29;  -- 前端261期 → 赵六
UPDATE class SET teacher_id = 37 WHERE id = 30;  -- Python大数据102期 → 李四
UPDATE class SET teacher_id = 38 WHERE id = 31;  -- JavaEE304期 → 王五
UPDATE class SET teacher_id = 39 WHERE id = 32;  -- 前端262期 → 赵六

-- 3. 修复 student.class_id → 指向正确的 class ID (18-32)
-- 赵敏(22)=JavaEE300期(18), 钱多多(23)=JavaEE300期(18)
-- 孙悦(24)=前端258期(19), 周杰(25)=JavaEE300期(18)
-- 吴用(26)=前端258期(19)
-- 郑爽(27)=JavaEE301期(20), 王磊(28)=JavaEE301期(20)
-- 冯小刚(29)=前端259期(21), 陈晓(30)=前端259期(21)
-- 楚天阔(31)=Python大数据101期(22), 魏晨(32)=Python大数据101期(22)
-- 蒋欣(33)=JavaEE302期(23), 沈腾(34)=JavaEE302期(23)
-- 韩雪(35)=前端260期(24), 杨洋(36)=前端260期(24)
UPDATE student SET class_id = 18 WHERE id = 22;
UPDATE student SET class_id = 18 WHERE id = 23;
UPDATE student SET class_id = 19 WHERE id = 24;
UPDATE student SET class_id = 18 WHERE id = 25;
UPDATE student SET class_id = 19 WHERE id = 26;
UPDATE student SET class_id = 20 WHERE id = 27;
UPDATE student SET class_id = 20 WHERE id = 28;
UPDATE student SET class_id = 21 WHERE id = 29;
UPDATE student SET class_id = 21 WHERE id = 30;
UPDATE student SET class_id = 22 WHERE id = 31;
UPDATE student SET class_id = 22 WHERE id = 32;
UPDATE student SET class_id = 23 WHERE id = 33;
UPDATE student SET class_id = 23 WHERE id = 34;
UPDATE student SET class_id = 24 WHERE id = 35;
UPDATE student SET class_id = 24 WHERE id = 36;

-- 4. 修复 job.student_id → 指向正确的 student ID (22-36)
UPDATE job SET student_id = 22 WHERE id = 18;
UPDATE job SET student_id = 23 WHERE id = 19;
UPDATE job SET student_id = 24 WHERE id = 20;
UPDATE job SET student_id = 25 WHERE id = 21;
UPDATE job SET student_id = 26 WHERE id = 22;
UPDATE job SET student_id = 27 WHERE id = 23;
UPDATE job SET student_id = 28 WHERE id = 24;
UPDATE job SET student_id = 29 WHERE id = 25;
UPDATE job SET student_id = 30 WHERE id = 26;
UPDATE job SET student_id = 31 WHERE id = 27;
UPDATE job SET student_id = 32 WHERE id = 28;
UPDATE job SET student_id = 33 WHERE id = 29;
UPDATE job SET student_id = 34 WHERE id = 30;
UPDATE job SET student_id = 35 WHERE id = 31;
UPDATE job SET student_id = 36 WHERE id = 32;

-- 5. 修复 exam.class_id → 指向正确的 class ID (18-32)
UPDATE exam SET class_id = 18 WHERE id = 18;
UPDATE exam SET class_id = 19 WHERE id = 19;
UPDATE exam SET class_id = 18 WHERE id = 20;
UPDATE exam SET class_id = 19 WHERE id = 21;
UPDATE exam SET class_id = 20 WHERE id = 22;
UPDATE exam SET class_id = 21 WHERE id = 23;
UPDATE exam SET class_id = 20 WHERE id = 24;
UPDATE exam SET class_id = 21 WHERE id = 25;
UPDATE exam SET class_id = 22 WHERE id = 26;
UPDATE exam SET class_id = 23 WHERE id = 27;
UPDATE exam SET class_id = 24 WHERE id = 28;
UPDATE exam SET class_id = 25 WHERE id = 29;
UPDATE exam SET class_id = 26 WHERE id = 30;
UPDATE exam SET class_id = 28 WHERE id = 31;
UPDATE exam SET class_id = 32 WHERE id = 32;

-- 6. 修复 work_experience.emp_id → 指向正确的 emp ID (36-50)
-- 张三(36), 王五(38), 赵六(39), 周八(41), 吴九(42), 郑十(43), 陈一(44)
UPDATE work_experience SET emp_id = 36 WHERE emp_id = 2;
UPDATE work_experience SET emp_id = 36 WHERE emp_id = 2;
UPDATE work_experience SET emp_id = 38 WHERE emp_id = 4;
UPDATE work_experience SET emp_id = 38 WHERE emp_id = 4;
UPDATE work_experience SET emp_id = 39 WHERE emp_id = 5;
UPDATE work_experience SET emp_id = 39 WHERE emp_id = 5;
UPDATE work_experience SET emp_id = 41 WHERE emp_id = 7;
UPDATE work_experience SET emp_id = 41 WHERE emp_id = 7;
UPDATE work_experience SET emp_id = 41 WHERE emp_id = 7;
UPDATE work_experience SET emp_id = 42 WHERE emp_id = 8;
UPDATE work_experience SET emp_id = 42 WHERE emp_id = 8;
UPDATE work_experience SET emp_id = 43 WHERE emp_id = 9;
UPDATE work_experience SET emp_id = 43 WHERE emp_id = 9;
UPDATE work_experience SET emp_id = 44 WHERE emp_id = 10;
UPDATE work_experience SET emp_id = 44 WHERE emp_id = 10;
