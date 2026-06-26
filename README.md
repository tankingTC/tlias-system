# 智学云帆 - 教学管理系统

> 基于 SpringBoot 3 + Vue 3 的全栈教学管理系统

## 项目简介

智学云帆是一款专为教育机构设计的综合性管理平台，帮助学校高效管理教学资源、学员信息、员工数据以及就业服务等核心业务。系统采用前后端分离架构，提供完整的 CRUD 操作、权限认证、数据统计、操作审计等企业级功能。

### 核心特点

- **全栈分离架构**：后端 SpringBoot 3.2 + 前端 Vue 3，职责清晰
- **安全认证体系**：JWT Token + BCrypt 密码加密 + 拦截器校验
- **操作审计追踪**：AOP 自动记录所有关键操作日志（异常安全，不阻断业务）
- **数据可视化**：ECharts 多图表展示（饼图、柱状图、仪表盘、雷达图）
- **Excel 导入导出**：EasyExcel 实现学员数据批量处理
- **容器化部署**：Docker Compose 一键部署全套服务
- **企业级 UI**：可折叠侧边栏、统计卡片、骨架屏加载、批量操作、表单校验
- **精美登录页**：实景背景图 + 玻璃拟态卡片 + 炫彩渐变旋转边框

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 开发语言 |
| Spring Boot | 3.2.0 | Web 框架 |
| MyBatis | 3.0.3 | ORM 框架 |
| MySQL | 8.0 | 关系数据库 |
| Redis | 7.x | 缓存 (可选) |
| JWT (jjwt) | 0.12.3 | Token 认证 |
| BCrypt | spring-security-crypto | 密码加密 |
| PageHelper | 2.1.0 | 分页插件 |
| EasyExcel | 3.3.3 | Excel 处理 |
| MinIO | 8.5.17 | 对象存储 (S3兼容) |
| Spring AOP | 6.1.1 | 操作日志切面 |
| SLF4J + Logback | Spring Boot 内置 | 结构化日志 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | 3.4+ | 前端框架 (Composition API) |
| Element Plus | 2.5+ | UI 组件库 |
| Vue Router | 4.2+ | 路由管理 |
| Axios | 1.6+ | HTTP 请求 |
| ECharts | 5.4+ | 数据可视化 |
| Vite | 5.0+ | 构建工具 |

### 部署

| 技术 | 说明 |
|------|------|
| Docker | 容器化部署 |
| Docker Compose | 多容器编排 |
| Nginx | 静态资源服务 + 反向代理 |

## 项目结构

```
tlias-system/
├── tlias-backend/                      # 后端 SpringBoot 项目
│   ├── pom.xml                         # Maven 依赖配置
│   └── src/main/
│       ├── java/com/tlias/
│       │   ├── TliasApplication.java          # 启动类
│       │   ├── config/                        # 配置类
│       │   │   ├── CorsConfig.java            # 跨域配置 (限制来源)
│       │   │   ├── SecurityConfig.java        # 安全配置 (BCrypt Bean)
│       │   │   ├── OssConfig.java             # OSS 配置
│       │   │   ├── WebConfig.java             # Web 配置 (拦截器)
│       │   │   └── DataInitializer.java       # 数据初始化
│       │   ├── controller/                    # 控制层 (11个)
│       │   ├── service/                       # 业务逻辑层
│       │   │   └── impl/                      # 实现类
│       │   ├── mapper/                        # MyBatis 数据访问层 (含COUNT查询)
│       │   ├── pojo/                          # 实体类/DTO/VO/Result
│       │   │   └── entity/                    # 实体类
│       │   ├── exception/                     # 异常处理
│       │   │   ├── GlobalExceptionHandler.java # 全局异常 (多类型+HTTP状态码)
│       │   │   └── BusinessException.java     # 业务异常
│       │   ├── interceptor/                   # JWT 拦截器
│       │   ├── aspect/                        # AOP 操作日志切面 (异常安全)
│       │   └── util/                          # 工具类
│       └── resources/
│           └── application.yml                # 配置文件 (环境变量)
│
├── tlias-frontend/                     # 前端 Vue3 项目
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── main.js                            # 入口文件
│       ├── App.vue                            # 根组件
│       ├── styles/global.css                  # 全局系统样式
│       ├── assets/images/                     # 静态资源
│       │   └── background.png                 # 登录页背景图
│       ├── layout/
│       │   └── MainLayout.vue                 # 主布局 (可折叠侧边栏+环绕流光)
│       ├── views/                             # 页面组件 (11个)
│       │   ├── Login.vue                      # 登录页 (背景图+玻璃拟态+炫彩边框)
│       │   ├── dashboard/
│       │   │   ├── Dashboard.vue              # 首页仪表盘 (图表+快捷操作+动态)
│       │   │   └── Statistics.vue             # 数据统计 (4种ECharts图表)
│       │   ├── dept/DeptManagement.vue        # 部门管理
│       │   ├── emp/EmpManagement.vue          # 员工管理
│       │   ├── class/ClassManagement.vue      # 班级管理
│       │   ├── student/StudentManagement.vue  # 学员管理
│       │   ├── exam/ExamManagement.vue        # 考试管理
│       │   ├── job/JobManagement.vue          # 就业管理
│       │   ├── feedback/FeedbackManagement.vue # 反馈管理
│       │   └── log/OperationLog.vue           # 操作日志
│       ├── api/                               # API 请求模块 (6个)
│       ├── router/index.js                    # 路由配置
│       └── utils/request.js                   # Axios 封装 (支持blob)
│
├── deploy/                             # 部署配置
│   ├── Dockerfile.backend              # 后端 Docker 镜像
│   ├── Dockerfile.frontend             # 前端 Docker 镜像
│   ├── nginx.conf                      # Nginx 配置
│   └── docker-compose.yml              # Docker Compose 编排
│
├── docs/                               # 文档
│   ├── tlias-full.sql                  # 数据库完整脚本 (建表+测试数据)
│   ├── 数据库设计文档.md                # 数据库 ER 图 + 表结构详细设计
│   └── 接口设计文档.md                  # RESTful API 接口详细文档 (48个接口)
│
└── README.md                           # 项目说明文档
```

## 功能模块

```
智学云帆-教学管理系统
├── 1. 用户认证
│   ├── 用户登录 (JWT Token)
│   ├── 获取用户信息
│   └── 退出登录
├── 2. 部门管理
│   ├── 部门列表 (搜索/排序/批量删除)
│   ├── 新增部门 (表单校验)
│   ├── 修改部门
│   └── 删除部门
├── 3. 员工管理
│   ├── 条件分页查询 (姓名/性别/入职日期)
│   ├── 新增员工 (密码BCrypt加密 + 表单校验)
│   ├── 修改员工
│   ├── 删除员工 (支持批量)
│   └── 头像上传 (阿里云OSS)
├── 4. 班级管理
│   ├── 班级列表 (搜索/排序/批量删除)
│   ├── 新增班级 (表单校验)
│   ├── 修改班级
│   └── 删除班级
├── 5. 学员管理
│   ├── 条件分页查询 (姓名/班级/学习状态)
│   ├── 新增学员 (表单校验)
│   ├── 修改学员
│   ├── 删除学员 (支持批量)
│   ├── Excel 批量导入
│   └── Excel 批量导出 (文件名含日期)
├── 6. 考试管理
│   ├── 考试列表 (搜索/排序/批量删除)
│   ├── 创建考试 (表单校验)
│   ├── 修改考试
│   └── 删除考试
├── 7. 就业管理
│   ├── 就业信息列表 (搜索/排序/薪资格式化)
│   ├── 录入就业信息 (表单校验)
│   ├── 修改就业信息
│   └── 删除就业信息 (支持批量)
├── 8. 反馈管理
│   ├── 反馈列表 (类型/状态筛选/排序/批量删除)
│   ├── 新增反馈 (表单校验)
│   ├── 处理反馈
│   └── 删除反馈
├── 9. 数据统计
│   ├── 首页仪表盘 (统计卡片+柱状图+饼图+快捷操作+最近动态)
│   ├── 数据统计页 (饼图+柱状图+仪表盘+雷达图)
│   └── 就业率分析 (ECharts 仪表盘)
└── 10. 系统管理
    ├── 操作日志 (AOP自动记录，异常安全)
    └── 日志筛选 (用户名/请求方式)
```

## 快速开始

### 环境要求

| 环境 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | Java 运行环境 |
| Node.js | 18+ | 前端运行环境 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 (可选) |

> 项目已内置 Maven 3.9.6 本地分发版，无需单独安装 Maven。

### 数据库初始化

#### Windows (PowerShell)

```powershell
# 1. 启动 MySQL 服务
net start MySQL80

# 2. 创建数据库并导入数据
cd tlias-system
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
Get-Content "docs\tlias-full.sql" | mysql -u root -p -D tlias
```

#### Linux / macOS

```bash
# 1. 启动 MySQL 服务
sudo systemctl start mysql

# 2. 创建数据库并导入数据
cd tlias-system
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
mysql -u root -p -D tlias < docs/tlias-full.sql
```

> 导入时会提示输入 MySQL root 密码，请根据实际情况修改命令中的密码参数。

### 配置数据库连接

编辑 `tlias-backend/src/main/resources/application.yml`，通过环境变量配置：

```yaml
spring:
  datasource:
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:你的MySQL密码}
```

或直接设置环境变量：

```powershell
# Windows
$env:DB_PASSWORD="你的MySQL密码"

# Linux / macOS
export DB_PASSWORD="你的MySQL密码"
```

### 后端启动

#### Windows (PowerShell)

```powershell
cd tlias-backend

# 方式一：使用本地 Maven 3.9.6
D:\My\system\tlias-system\.mvn-dist\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run

# 方式二：编译后运行 jar
D:\My\system\tlias-system\.mvn-dist\apache-maven-3.9.6\bin\mvn.cmd package -DskipTests
java -jar target\tlias-backend-1.0.0.jar
```

#### Linux / macOS

```bash
cd tlias-backend

# 方式一：Maven 直接运行
mvn spring-boot:run

# 方式二：编译后运行 jar
mvn package -DskipTests
java -jar target/tlias-backend-1.0.0.jar
```

后端默认运行在 **http://localhost:8080**

### 前端启动

```bash
cd tlias-frontend

# 1. 安装依赖 (首次运行)
npm install

# 2. 启动开发服务器
npm run dev

# 3. 生产构建
npm run build
```

前端默认运行在 **http://localhost:5173**

### 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 系统管理员 |

## 接口文档

### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 错误响应

| HTTP 状态码 | 说明 |
|-------------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 / 表单校验失败 |
| 401 | 未认证 / Token 无效 |
| 405 | 不支持的请求方法 |
| 500 | 系统异常 |

### 核心接口

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 登录 | POST | /login | 用户登录 |
| 用户信息 | GET | /user/info | 获取用户信息 |
| 登出 | POST | /logout | 退出登录 |
| 部门列表 | GET | /depts | 查询所有部门 |
| 新增部门 | POST | /depts | 新增部门 |
| 修改部门 | PUT | /depts | 修改部门 |
| 删除部门 | DELETE | /depts/{id} | 删除部门 |
| 员工列表 | GET | /emps | 分页查询员工 |
| 新增员工 | POST | /emps | 新增员工 |
| 修改员工 | PUT | /emps | 修改员工 |
| 删除员工 | DELETE | /emps/{id} | 删除员工 |
| 文件上传 | POST | /upload | 上传头像到OSS |
| 班级列表 | GET | /classes | 查询所有班级 |
| 学员列表 | GET | /students | 分页查询学员 |
| 学员导入 | POST | /students/import | Excel导入 |
| 学员导出 | GET | /students/export | Excel导出 |
| 考试列表 | GET | /exams | 查询所有考试 |
| 就业列表 | GET | /jobs | 查询就业信息 |
| 反馈列表 | GET | /feedbacks | 查询反馈列表 |
| 数据统计 | GET | /statistics/dashboard | 仪表盘数据 |
| 操作日志 | GET | /logs | 查询操作日志 |

### 请求头

所有需要认证的接口需要在 Header 中携带 Token：

```
token: <your_jwt_token>
```

## Docker 部署

### 一键部署

```bash
# 在项目根目录执行
docker-compose -f deploy/docker-compose.yml up -d
```

这将启动以下服务：
- MySQL (端口 3306)
- Redis (端口 6379)
- 后端 (端口 8080)
- 前端 (端口 80)

### 查看状态

```bash
docker-compose -f deploy/docker-compose.yml ps
```

### 停止服务

```bash
docker-compose -f deploy/docker-compose.yml down
```

### 独立构建

```bash
# 构建后端镜像
docker build -f deploy/Dockerfile.backend -t tlias-backend .

# 构建前端镜像 (需先构建前端)
cd tlias-frontend && npm run build && cd ..
docker build -f deploy/Dockerfile.frontend -t tlias-frontend .
```

## 常用命令速查

### 后端

```powershell
# Windows PowerShell
cd tlias-backend

# 编译
D:\My\system\tlias-system\.mvn-dist\apache-maven-3.9.6\bin\mvn.cmd compile

# 打包 (跳过测试)
D:\My\system\tlias-system\.mvn-dist\apache-maven-3.9.6\bin\mvn.cmd package -DskipTests

# 运行
D:\My\system\tlias-system\.mvn-dist\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run

# 清理
D:\My\system\tlias-system\.mvn-dist\apache-maven-3.9.6\bin\mvn.cmd clean
```

```bash
# Linux / macOS
cd tlias-backend

mvn compile
mvn package -DskipTests
mvn spring-boot:run
mvn clean
```

### 前端

```bash
cd tlias-frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 生产构建
npm run build

# 预览生产构建
npm run preview
```

### 数据库

```powershell
# Windows PowerShell - 初始化数据库
cd tlias-system
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
Get-Content "docs\tlias-full.sql" | mysql -u root -p -D tlias

# 查询数据
mysql -u root -p -D tlias -e "SELECT * FROM emp;"

# 重置数据库 (危险操作，会删除所有数据)
mysql -u root -p -e "DROP DATABASE IF EXISTS tlias;"
mysql -u root -p -e "CREATE DATABASE tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
Get-Content "docs\tlias-full.sql" | mysql -u root -p -D tlias
```

## 开发规范

### 后端规范
- 遵循阿里巴巴 Java 开发手册
- Controller 返回统一 `Result<T>` 响应，使用 `@ResponseStatus` 返回正确 HTTP 状态码
- Service 层处理业务逻辑，Controller 不直接注入 Mapper
- Mapper 使用注解编写 SQL，提供 `count()` 方法用于统计
- 密码通过 Spring Bean `BCryptPasswordEncoder` 统一加密
- 所有写操作通过 AOP 自动记录操作日志（异常安全，不阻断业务）
- 敏感字段使用 `@JsonIgnore` 防止序列化泄露
- 配置信息通过环境变量注入，敏感数据不硬编码

### 前端规范
- 使用 Vue 3 Composition API (`<script setup>`)
- API 请求统一放在 `src/api/` 目录
- 使用 Axios 拦截器统一处理 Token 和错误
- Blob 类型响应（文件下载）直接返回原始数据
- 组件命名使用 PascalCase
- 每个管理页面统一包含：页面标题、搜索栏、loading 状态、空数据占位、排序、批量操作、表单校验

### 数据库规范
- 表名使用小写+下划线
- 必须包含 `create_time` 和 `update_time` 字段
- 外键字段建立索引

## 安全设计

| 安全措施 | 实现方式 |
|----------|----------|
| 认证授权 | JWT Token + 拦截器校验 |
| 密码安全 | BCrypt 加密存储 (Spring Bean 统一管理) |
| SQL 注入防护 | MyBatis 预编译语句 |
| XSS 防护 | 前端输入过滤 |
| 文件上传安全 | 类型白名单 + 大小限制 |
| 跨域控制 | CORS 配置 (仅允许 localhost) |
| 参数校验 | 后端 `@Valid` + 前端 `el-form rules` |
| 操作审计 | AOP 自动记录操作日志 (异常安全) |
| 密码泄露防护 | `@JsonIgnore` 防止密码字段序列化 |
| 敏感配置 | 环境变量注入 (DB密码、JWT密钥) |

## 更新日志

### v1.8.0 (2026-06-26)
**UI 全面美化 + 代码清理 + 文档修复：**

**Header 玻璃效果：**
- 透明毛玻璃背景：`background: transparent` + `backdrop-filter: blur(16px)`
- 环绕炫彩流光边框：`conic-gradient` 蓝→青→绿 + `mask` 镂空 + `hue-rotate` 旋转动画
- 底部渐变背景层：蓝→靛蓝柔和渐变 + `bgFlow` 缓慢流动动画

**侧边栏增强：**
- 5 色极光光晕：蓝/青/绿/紫/粉，独立漂移动画（14-18s）
- 右侧边缘发光线：蓝→青→绿垂直渐变 + 呼吸动画
- 背景微动画层：渐变色缓慢上下移动
- 菜单项 hover/active：蓝色渐变背景 + 左侧发光条 + box-shadow

**统计卡片图标：**
- 彩色渐变背景（蓝/绿/橙/粉）+ 白色图标
- 发光阴影效果 + 呼吸光效动画
- 卡片内容居中显示

**原型对齐：**
- 页面标题：蓝色左侧竖线装饰
- 搜索栏：添加文字标签（姓名/性别/入职时间等）
- 查询按钮：橙黄色 `#e6a23c`（对齐原型）
- 表格表头：浅蓝色背景 `#e8f4fd`
- 分页页码：当前页蓝色圆形背景

**布局紧凑化：**
- 侧边栏宽度：220px → 190px
- 内容区 padding：16px 20px → 12px 16px
- 页面标题字号：18px → 15px
- 表格字号：14px → 13px
- 表格行高：padding 6px → 4px

**代码清理（删除冗余代码）：**
- 删除 3 个未使用 API 文件（exam.js/job.js/feedback.js）
- 删除 2 个未使用 Java 文件（EmpDTO.java/PasswordGenerator.java）
- 移除 2 个未使用依赖（pinia/sass）
- 清理 9 个 Mapper 冗余 @Mapper 注解（保留 @MapperScan）
- 清理 global.css 约 170 行重复/未使用样式
- 移除 JwtInterceptor 冗余 /login 检查
- 清理 application.yml 无效 mapper-locations 配置

**文档修复：**
- 修复 docker-compose.yml SQL 文件引用（init.sql → tlias-full.sql）
- 修复 README 数据库命令引用
- 修复 .gitignore（移除 docs/ 排除，新增 .mvn-dist/ 排除）
- 更新前端技术栈（移除 Pinia）

### v1.7.0 (2026-06-26)
**UI 全面重构 + 质感提升：**

**Header 重构：**
- 玻璃效果对齐登录页风格：半透明背景 `rgba(255,255,255,0.25)` + `blur(24px)` 毛玻璃
- 渐变发光边框：`conic-gradient` 白蓝渐变 + 6s 旋转动画
- 顶部流光条：蓝→青→绿 3px 渐变流动
- Logo 重设计：蓝青渐变六边形 SVG + 渐变文字 "智学云帆-教学管理系统"
- 毛玻璃 Header + 内阴影高光效果

**侧边栏重构：**
- 极光玻璃效果：深蓝半透明背景 `rgba(15,23,42,0.88)` + `blur(16px)` 毛玻璃
- 3 个极光光晕：蓝/青/绿，独立漂移动画（20-25s）
- 菜单项：左侧彩色指示条（选中=蓝绿渐变，悬浮=淡蓝）
- 子菜单：白色 60% 透明度文字，悬浮/选中时高亮
- 分组分隔线：半透明白色

**Dashboard 欢迎横幅：**
- 玻璃拟态 + 旋转彩虹渐变边框
- 实时时钟（时:分:秒）+ 日期显示
- 微粒子装饰
- 统计卡片：立体阴影 + 悬浮缩放图标

**全局色调统一：**
- 主色蓝 `#3b82f6` → 青 `#06b6d4` → 绿 `#10b981`（无紫色）
- 背景色 `#f1f5f9`
- 文字色 `#1e293b` / `#64748b`
- 微交互：卡片悬浮、按钮点击、页面过渡

### v1.6.0 (2026-06-23)
**对象存储迁移：从阿里云 OSS 切换到 Sealos OSS（MinIO SDK）：**

**后端变更：**
- `pom.xml`：替换 `aliyun-sdk-oss:3.17.4` → `minio:8.5.17`
- `application.yml`：配置从 `aliyun.oss.*` 改为 `minio.*`，支持环境变量注入
- `OssConfig.java`：重写为 MinIO 客户端 Bean（S3 兼容协议）
- `UploadController.java`：重写上传逻辑，使用 MinIO SDK 的 `putObject` 方法
- 文件 URL 格式：`https://{endpoint}/{bucket}/{uuid}.{ext}`

**配置信息：**
- Endpoint：`https://objectstorageapi.hzh.sealos.run`
- Bucket：`e09w3hj8-system`
- 支持环境变量：`MINIO_ENDPOINT`、`MINIO_ACCESS_KEY`、`MINIO_SECRET_KEY`、`MINIO_BUCKET`

### v1.5.0 (2026-06-23)
**数据丰富 + 代码注释 + Bug 修复：**

**测试数据：**
- 每张表补充 15 条完整测试数据（部门/员工/班级/学员/考试/就业/反馈/工作经历）
- 修复所有外键关联（删除重插后 ID 漂移问题）
- 数据覆盖多种业务场景：不同学科班级、不同学历学员、不同状态考试、不同处理状态反馈

**Bug 修复：**
- 修复 Token 过期时多个并发请求重复弹出错误提示（`isRedirecting` 标志位）
- 修复整个页面滚动问题（Header + 侧边栏固定，仅内容区滚动）
- 学员相关配色统一改为粉色（紫色 → 粉色 `#f093fb`）

**代码注释：**
- 后端核心文件添加中文注释（11个文件）：LoginController、EmpController、JwtUtil、JwtInterceptor、GlobalExceptionHandler、EmpServiceImpl、OperationLogAspect、Result、SecurityConfig、WebConfig、DataInitializer
- 前端核心文件添加中文注释（3个文件）：request.js、router/index.js、MainLayout.vue
- 注释风格：解释"为什么这样做"而不仅仅是"做了什么"

**配置优化：**
- 部门管理去掉批量删除（对齐原型设计）
- 布局组件 `MainLayout.vue` 固定高度布局，阻止页面整体滚动
- `global.css` 新增 `html, body, #app { overflow: hidden }` 全局滚动控制

### v1.4.0 (2026-06-23)
**对齐原型图设计要求，全面完善功能和校验：**

**数据库变更：**
- emp 表新增 `salary`（薪资）字段
- class 表新增 `classroom`（教室）、`subject`（学科）字段
- student 表新增 `student_no`（学号）、`is_college`（是否院校学员）、`address`（联系地址）、`violation_count`（违纪次数）、`violation_score`（违纪扣分）字段
- 新建 `work_experience` 工作经历表

**后端变更：**
- EmpController：新增手机号唯一性校验、用户名 2-20 位仅字母校验、姓名 2-10 位校验、新增员工默认密码 123456
- ClazzController：新增分页查询（支持班级名称+结课时间范围搜索）、删除时检查关联学生
- StudentController：新增违纪处理接口 `POST /students/{id}/violation`
- 所有 Mapper 排序改为 `update_time DESC`
- 新增 WorkExperienceMapper 工作经历数据访问

**前端变更：**
- 部门管理：表头改为"最后操作时间"（update_time）、去掉批量删除、名称长度 2-10 位校验、删除确认文案统一
- 员工管理：新增薪资字段、职位改为下拉框（5个预设选项）、新增工作经历区域（可动态添加多条）、用户名 2-20 位仅字母校验、姓名 2-10 位校验、手机号 11 位校验、头像上传 2MB 限制+格式提示、新增员工默认密码
- 班级管理：新增教室和学科字段、搜索栏改为班级名称+结课时间范围、新增分页、删除时检查关联学生
- 学员管理：新增学号/是否院校学员/联系地址字段、搜索栏改为姓名+学历+班级、表头对齐原型（含违纪次数/扣分）、新增违纪处理弹窗

### v1.3.2 (2026-06-22)
**新增设计文档：**
- 新增数据库设计文档 (`docs/数据库设计文档.md`)：8 张表 ER 关系图 + 完整字段定义 + 索引 + 外键关系 + 测试数据
- 新增接口设计文档 (`docs/接口设计文档.md`)：42 个 RESTful API 完整文档，含请求参数、响应格式、认证机制

### v1.3.1 (2026-06-22)
**登录页重构：**
- 登录背景改为蓝天盐湖实景图片，全屏展示 + 缓慢缩放动画
- 背景图通过 JS import + 动态 style 绑定，兼容 Vite 开发/生产模式
- 半透明蓝色渐变遮罩保证文字可读性
- 玻璃拟态卡片：`rgba(255,255,255,0.25)` + `blur(24px)` + 内发光
- 炫彩渐变旋转边框：`conic-gradient` 白/蓝/淡紫色调 + `hue-rotate` 动画
- 输入框白色半透明背景，聚焦时蓝色边框
- 登录按钮蓝色渐变 `#3b82f6 → #2563eb`
- 标题深蓝色 `#1a3a5c` 与蓝天背景呼应

### v1.3.0 (2026-06-22)
**界面重构：**
- 全新可折叠侧边栏，支持分组菜单（教学管理/运营管理/系统管理）
- Dashboard 首页仪表盘：统计卡片(彩色图标) + 柱状图 + 饼图 + 快捷操作 + 最近动态
- 数据统计页增加 4 种图表：饼图、柱状图、仪表盘(就业率)、雷达图
- 所有管理页面增加页面标题区、loading 骨架屏、空数据占位、列排序、批量操作
- 所有表单增加 `el-form` rules 校验替代手动 if 判断
- 全局样式统一（统计卡片、搜索栏、批量操作栏、表格操作按钮组）

**安全优化：**
- 数据库密码、JWT Secret 改用环境变量注入，不再硬编码
- `Emp.password` 字段增加 `@JsonIgnore` 防止密码哈希泄露
- CORS 配置限制来源为 localhost
- 操作日志切面异常安全（try-catch 包裹，不阻断业务）

**性能优化：**
- StatisticsController 改用 `SELECT COUNT(*)` 替代全表加载
- 所有 Mapper 增加 `count()` 方法
- BCryptPasswordEncoder 提取为 Spring Bean，避免重复创建

**异常处理：**
- GlobalExceptionHandler 返回正确 HTTP 状态码（400/401/405/500）
- 新增 6 种异常类型处理（JSON解析错误、参数缺失、类型不匹配、文件超限等）
- 使用 SLF4J 替代 `e.printStackTrace()`

**Bug 修复：**
- 修复前端文件导出功能被响应拦截器破坏的问题（支持 blob 响应）
- 修复 Statistics.vue 内存泄漏（resize 事件监听器未移除）
- 修复 MainLayout 硬编码用户名，改为调用 API 获取真实用户信息
- 修复 README Maven 路径错误
- 修复 Docker 部署未加载 operation_log.sql 的问题

### v1.2.0 (2026-06-21)
- 修复 BCrypt 密码验证问题，密码哈希已正确生成
- 修复 PowerShell 环境下 Maven 命令解析问题
- 修复 DataInitializer 数据库不可用时的启动问题
- 登录页面全新设计：深色渐变背景 + 粒子动画 + 玻璃拟态卡片
- 完善 README 文档，补充完整运行命令

### v1.1.0 (2024-06-19)
- 新增 BCrypt 密码加密
- 新增操作日志功能 (AOP 自动记录)
- 新增文件上传安全限制 (类型+大小)
- 新增数据自动初始化
- 补充测试数据
- 完善参数校验

### v1.0.0 (2024-06-19)
- 初始版本发布
- 10 个核心功能模块
- 前后端完整架构
- Docker 部署支持

## 测试记录

### 测试结果 (2026-06-23)

| 测试项 | 状态 | 说明 |
|--------|------|------|
| 后端编译 | ✅ 通过 | 58 个 Java 文件全部编译成功 |
| 后端启动 | ✅ 通过 | Tomcat 在 8080 端口启动成功 |
| 前端构建 | ✅ 通过 | Vite build 成功 |
| 前端启动 | ✅ 通过 | Vite 在 5173 端口启动成功 |
| 数据库迁移 | ✅ 通过 | 新增字段、表、测试数据全部成功 |
| 外键关联 | ✅ 通过 | 所有关联查询正常（部门/班主任/班级/学员） |
| 登录认证 | ✅ 通过 | admin/123456 登录成功，JWT Token 正常 |
| 部门管理 | ✅ 通过 | 15 条数据 + 最后操作时间 + 名称校验 正常 |
| 员工管理 | ✅ 通过 | 16 条数据 + 薪资 + 职位下拉 + 工作经历 + 关联部门 正常 |
| 班级管理 | ✅ 通过 | 15 条数据 + 教室 + 学科 + 关联班主任 + 删除检查 正常 |
| 学员管理 | ✅ 通过 | 15 条数据 + 学号 + 违纪 + 关联班级 + Excel导入导出 正常 |
| 考试管理 | ✅ 通过 | 15 条数据 + 关联班级 + 状态筛选 正常 |
| 就业管理 | ✅ 通过 | 15 条数据 + 关联学员 + 薪资格式化 正常 |
| 反馈管理 | ✅ 通过 | 15 条数据 + 类型/状态筛选 正常 |
| 数据统计 | ✅ 通过 | 4种图表正常渲染 |
| 操作日志 | ✅ 通过 | 日志记录 + 筛选 正常 |
| 密码泄露 | ✅ 通过 | API 不再返回密码字段 |
| Token过期 | ✅ 通过 | 并发请求只弹一次错误提示 |
| 页面滚动 | ✅ 通过 | Header/侧边栏固定，仅内容区滚动 |
| 代码注释 | ✅ 通过 | 14 个核心文件添加中文注释 |

## License

MIT

---

*智学云帆教学管理系统 - 让教育更智慧*
