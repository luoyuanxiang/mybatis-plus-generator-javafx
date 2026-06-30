# MyBatis-Plus Generator UI

基于 JavaFX 21 的 MyBatis-Plus 交互式代码生成器，封装 `FastAutoGenerator` 全量配置能力。

## 环境要求

- JDK 21+（编译与运行）
- Maven 3.9+
- JDK 24+ 运行时会自动启用 `--enable-native-access=javafx.graphics`（已在 Maven 插件与启动脚本中配置）

## 运行

本项目使用 **JPMS 模块化**（`module-info.java` 声明 JavaFX 等依赖），推荐：

```bash
mvn javafx:run
```

Windows 也可双击 [`run.bat`](run.bat) 或执行：

```powershell
.\run.ps1
```

### IntelliJ IDEA

**方式 1（推荐）**：使用预置运行配置 **GeneratorApp (Maven)**，执行 `javafx:run`。

**方式 2**：使用预置运行配置 **Launcher**（模块化主类 `com.example.generator.ui/com.example.generator.ui.app.Launcher`）。

> 模块化后 IntelliJ 会自动解析 module-path，无需手动配置 `--module-path` / `--add-modules`。

### 模块说明

主模块 `com.example.generator.ui` 在 [`module-info.java`](src/main/java/module-info.java) 中声明：

- `requires javafx.controls`、`requires javafx.fxml` — JavaFX 依赖
- `opens ...controller to javafx.fxml` — FXML 反射注入
- `opens ...dto to com.fasterxml.jackson.databind` — JSON 序列化

### 常见警告与解决

| 警告 | 原因 | 解决 |
|------|------|------|
| `Unsupported JavaFX configuration: classes were loaded from 'unnamed module'` | 未使用模块化启动 | 使用 `mvn javafx:run`、`run.ps1` 或预置 **Launcher** 配置 |
| `Use --enable-native-access=...` | JDK 24+ 限制 native 调用 | 已在 Maven 插件与启动脚本中配置 |
| `sun.misc.Unsafe::allocateMemory` | JavaFX 21 在 JDK 24+ 上的兼容性提示，不影响使用 | 可忽略；或升级 JavaFX 至 25 并使用 JDK 23+ 编译 |

## 功能概览

| 模块 | 说明 |
|------|------|
| 数据源管理 | 多数据源 CRUD、连接测试、本地 JSON 持久化（`~/.mp-generator-ui/datasources.json`） |
| 表与字段 | 加载表列表、搜索/全选/反选、字段预览（列名、类型、主键、注释） |
| 全局配置 | author、输出目录、Swagger、日期类型、Kotlin 等 |
| 包配置 | parent/module/entity/service/mapper/controller/xml 及 XML 独立路径 |
| 策略配置 | Lombok、RestController、@Mapper、表前缀、逻辑删除、乐观锁、文件覆盖等 |
| 模板与注入 | 模板引擎、模板路径覆盖、customMap、CustomFile 自定义文件 |
| 配置方案 | Profile 保存/加载/导入导出（`~/.mp-generator-ui/profiles.json`） |
| 生成 | 后台 Task 执行、实时日志、生成文件列表、打开输出目录 |

## 支持的数据库

- MySQL
- PostgreSQL
- SQL Server
- Oracle
- H2（可用于本地测试，Database 填 `mem:test` 或完整 JDBC URL）

## 快速上手

1. 启动应用：`mvn javafx:run`
2. 配置数据源并点击 **测试连接**
3. 点击 **加载表**，勾选需要生成的表
4. 在 **全局配置** 中设置作者与输出目录
5. 在 **包配置** / **策略配置** 中按需调整
6. 点击 **生成代码**

### H2 本地测试示例

| 字段 | 值 |
|------|-----|
| 类型 | H2 |
| Database | `mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL` |
| 用户名 | `sa` |
| 密码 | （留空） |

连接后需先在 H2 中建表，或通过已有 MySQL 数据库测试。

### 自定义 DTO 模板

内置示例模板：[`src/main/resources/templates/entityDTO.java.ftl`](src/main/resources/templates/entityDTO.java.ftl)

在 **模板与注入 → CustomFile** 中添加：

- 文件名：`entityDTO.java`
- 模板路径：`/templates/entityDTO.java.ftl`
- 包名：`dto`

## 配置存储

| 文件 | 路径 |
|------|------|
| 数据源 | `~/.mp-generator-ui/datasources.json` |
| 配置方案 | `~/.mp-generator-ui/profiles.json` |

密码默认不持久化；勾选「保存密码(本地)」后才会写入本地文件。

## 项目结构

```
src/main/java/
  module-info.java   JPMS 模块定义（JavaFX、Jackson、MyBatis-Plus 等）
  com/example/generator/ui/
  app/          应用入口
  controller/   FXML 控制器
  dto/          配置与元数据模型
  service/      数据源、元数据、生成器、Profile 服务
  util/         JDBC、路径、校验工具
src/main/resources/
  fxml/         主界面
  css/          样式
  templates/    自定义 Freemarker 模板
```

## 技术栈

- Java 21 + JavaFX 21
- Lombok（DTO 与工具类）
- MyBatis-Plus Generator 3.5.15
- Freemarker 模板引擎
- Jackson JSON 持久化

## 打包（可选）

开发阶段使用 `mvn javafx:run`。如需原生安装包，可在此基础上配置 `jpackage`。
