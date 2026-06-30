package com.example.generator.ui.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.example.generator.ui.dto.*;
import com.example.generator.ui.util.JdbcUrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * MyBatis-Plus 代码生成核心服务。
 * <p>
 * 将 UI 配置（{@link GeneratorProfile}）映射为 {@link FastAutoGenerator} 链式调用，
 * 支持 Global/Package/Strategy/Injection 全量配置，并通过 {@link #setLogConsumer} 将
 * 生成过程日志实时推送至界面。
 * </p>
 */
@Slf4j
public class GeneratorService {

    /** UI 日志回调，默认空操作。 */
    private Consumer<String> logConsumer = message -> {
    };

    /** 本次生成已处理的表/文件记录（线程安全）。 */
    private final List<String> generatedFiles = Collections.synchronizedList(new ArrayList<>());

    /** 本次生成因未开启覆盖而跳过的文件（线程安全）。 */
    private final List<String> skippedFiles = Collections.synchronizedList(new ArrayList<>());

    /**
     * 设置 UI 日志消费者。
     *
     * @param logConsumer 接收日志行的回调，null 时使用空操作
     */
    public void setLogConsumer(Consumer<String> logConsumer) {
        this.logConsumer = logConsumer == null ? message -> {
        } : logConsumer;
    }

    /** 外部直接推送一行日志（如手动提示信息）。 */
    public void handleLog(String message) {
        emitLog(message);
    }

    private void emitLog(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (isSkippedFileMessage(message)) {
            skippedFiles.add(extractSkippedFileName(message));
        }
        logConsumer.accept(message);
    }

    private boolean isSkippedFileMessage(String message) {
        return message.contains("already exists") && message.contains("Overwrite mode is disabled");
    }

    private String extractSkippedFileName(String message) {
        int start = message.indexOf('[');
        int end = message.indexOf(']', start + 1);
        if (start >= 0 && end > start) {
            return message.substring(start + 1, end);
        }
        return message;
    }

    /**
     * 执行代码生成。
     *
     * @param dataSource 当前选中的数据源，不可为 null
     * @param profile    生成配置方案，须包含至少一张选中的表
     * @return 成功或失败的 {@link GenerationResult}
     */
    public GenerationResult generate(DataSourceConfigDto dataSource, GeneratorProfile profile) {
        if (dataSource == null) {
            return GenerationResult.failure("Please select a data source.");
        }
        if (profile.getSelectedTables() == null || profile.getSelectedTables().isEmpty()) {
            return GenerationResult.failure("Please select at least one table.");
        }

        generatedFiles.clear();
        skippedFiles.clear();
        GlobalConfigDto global = profile.getGlobalConfig();
        if (global.getOutputDir() == null || global.getOutputDir().isBlank()) {
            return GenerationResult.failure("Output directory is required.");
        }

        try {
            Files.createDirectories(Path.of(global.getOutputDir()));
            String url = JdbcUrlBuilder.buildUrl(dataSource);
            String username = dataSource.getUsername() == null ? "" : dataSource.getUsername();
            String password = dataSource.getPassword() == null ? "" : dataSource.getPassword();

            FastAutoGenerator.create(url, username, password)
                    .dataSourceConfig(builder -> {
                        if (dataSource.getSchema() != null && !dataSource.getSchema().isBlank()) {
                            builder.schema(dataSource.getSchema());
                        }
                    })
                    .globalConfig(builder -> applyGlobal(builder, global))
                    .packageConfig(builder -> applyPackage(builder, profile.getPackageConfig()))
                    .strategyConfig(builder -> applyStrategy(builder, profile))
                    .injectionConfig(builder -> applyInjection(builder, profile.getInjectionConfig(), this::emitLog))
                    .templateEngine(createTemplateEngine(profile.getInjectionConfig()))
                    .execute();

            if (!skippedFiles.isEmpty()) {
                emitLog("Skipped " + skippedFiles.size() + " existing file(s). "
                        + "Enable override in Global/Strategy settings to overwrite.");
            }
            emitLog("Generation finished. Generated tables: " + generatedFiles.size()
                    + ", skipped files: " + skippedFiles.size());
            return GenerationResult.success(new ArrayList<>(generatedFiles), new ArrayList<>(skippedFiles));
        } catch (Exception e) {
            log.error("Generation failed", e);
            return GenerationResult.failure(e.getMessage() == null ? "Generation failed." : e.getMessage());
        }
    }

    /** 映射 {@link GlobalConfigDto} 至 FastAutoGenerator GlobalConfig.Builder。 */
    private void applyGlobal(com.baomidou.mybatisplus.generator.config.GlobalConfig.Builder builder,
                             GlobalConfigDto global) {
        builder.author(global.getAuthor())
                .outputDir(global.getOutputDir())
                .commentDate(global.getCommentDate());
        if (global.isDisableOpenDir()) {
            builder.disableOpenDir();
        }
        if (global.isEnableSwagger()) {
            builder.enableSwagger();
        }
        if (global.isEnableKotlin()) {
            builder.enableKotlin();
        }
        builder.dateType(parseDateType(global.getDateType()));
    }

    /** 映射 {@link PackageConfigDto} 至 PackageConfig.Builder。 */
    private void applyPackage(com.baomidou.mybatisplus.generator.config.PackageConfig.Builder builder,
                              PackageConfigDto pkg) {
        builder.parent(pkg.getParent());
        if (pkg.getModuleName() != null && !pkg.getModuleName().isBlank()) {
            builder.moduleName(pkg.getModuleName());
        }
        builder.entity(pkg.getEntity())
                .service(pkg.getService())
                .serviceImpl(pkg.getServiceImpl())
                .mapper(pkg.getMapper())
                .controller(pkg.getController())
                .xml(pkg.getXml());
        if (pkg.getMapperXmlPath() != null && !pkg.getMapperXmlPath().isBlank()) {
            builder.pathInfo(Collections.singletonMap(OutputFile.xml, pkg.getMapperXmlPath()));
        }
    }

    /** 映射 {@link StrategyConfigDto} 至 StrategyConfig.Builder（Entity/Mapper/Service/Controller）。 */
    private void applyStrategy(com.baomidou.mybatisplus.generator.config.StrategyConfig.Builder builder,
                               GeneratorProfile profile) {
        StrategyConfigDto strategy = profile.getStrategyConfig();
        builder.addInclude(profile.getSelectedTables().toArray(new String[0]));

        if (!strategy.getTablePrefixes().isEmpty()) {
            builder.addTablePrefix(strategy.getTablePrefixes().toArray(new String[0]));
        }
        if (!strategy.getTableSuffixes().isEmpty()) {
            builder.addTableSuffix(strategy.getTableSuffixes().toArray(new String[0]));
        }
        if (!strategy.getFieldPrefixes().isEmpty()) {
            builder.addFieldPrefix(strategy.getFieldPrefixes().toArray(new String[0]));
        }
        if (!strategy.getFieldSuffixes().isEmpty()) {
            builder.addFieldSuffix(strategy.getFieldSuffixes().toArray(new String[0]));
        }
        if (!strategy.getExcludeTables().isEmpty()) {
            builder.addExclude(strategy.getExcludeTables().toArray(new String[0]));
        }

        var entityBuilder = builder.entityBuilder();
        if (strategy.isDisableEntity()) {
            entityBuilder.disable();
        } else {
            if (strategy.isEnableLombok()) {
                entityBuilder.enableLombok();
            }
            if (strategy.isEnableTableFieldAnnotation()) {
                entityBuilder.enableTableFieldAnnotation();
            }
            if (strategy.isEnableChainModel()) {
                entityBuilder.enableChainModel();
            }
            if (strategy.isEnableRemoveIsPrefix()) {
                entityBuilder.enableRemoveIsPrefix();
            }
            if (strategy.isEnableActiveRecord()) {
                entityBuilder.enableActiveRecord();
            }
            if (strategy.isEntityFileOverride()) {
                entityBuilder.enableFileOverride();
            }
            if (strategy.getLogicDeleteColumnName() != null && !strategy.getLogicDeleteColumnName().isBlank()) {
                entityBuilder.logicDeleteColumnName(strategy.getLogicDeleteColumnName());
            }
            if (strategy.getVersionColumnName() != null && !strategy.getVersionColumnName().isBlank()) {
                entityBuilder.versionColumnName(strategy.getVersionColumnName());
            }
            entityBuilder.naming(parseNaming(strategy.getNamingStrategy()));
            if (strategy.getColumnNamingStrategy() != null && !strategy.getColumnNamingStrategy().isBlank()) {
                entityBuilder.columnNaming(parseNaming(strategy.getColumnNamingStrategy()));
            }
            entityBuilder.idType(parseIdType(strategy.getIdType()));
            if (strategy.getEntityJavaTemplate() != null && !strategy.getEntityJavaTemplate().isBlank()) {
                entityBuilder.javaTemplate(strategy.getEntityJavaTemplate());
            }
        }

        var controllerBuilder = builder.controllerBuilder();
        if (strategy.isDisableController()) {
            controllerBuilder.disable();
        } else {
            if (strategy.isControllerRestStyle()) {
                controllerBuilder.enableRestStyle();
            }
            if (strategy.isControllerHyphenStyle()) {
                controllerBuilder.enableHyphenStyle();
            }
            if (strategy.isControllerFileOverride()) {
                controllerBuilder.enableFileOverride();
            }
            if (strategy.getControllerTemplate() != null && !strategy.getControllerTemplate().isBlank()) {
                controllerBuilder.template(strategy.getControllerTemplate());
            }
        }

        var mapperBuilder = builder.mapperBuilder();
        if (strategy.isDisableMapper()) {
            mapperBuilder.disable();
        } else {
            if (strategy.isMapperAnnotation()) {
                mapperBuilder.mapperAnnotation(Mapper.class);
            }
            if (strategy.isMapperBaseResultMap()) {
                mapperBuilder.enableBaseResultMap();
            }
            if (strategy.isMapperBaseColumnList()) {
                mapperBuilder.enableBaseColumnList();
            }
            if (strategy.isMapperFileOverride()) {
                mapperBuilder.enableFileOverride();
            }
            if (strategy.getMapperTemplate() != null && !strategy.getMapperTemplate().isBlank()) {
                mapperBuilder.mapperTemplate(strategy.getMapperTemplate());
            }
            if (strategy.getMapperXmlTemplate() != null && !strategy.getMapperXmlTemplate().isBlank()) {
                mapperBuilder.mapperXmlTemplate(strategy.getMapperXmlTemplate());
            }
        }

        var serviceBuilder = builder.serviceBuilder();
        if (strategy.isDisableService()) {
            serviceBuilder.disableService().disableServiceImpl();
        } else {
            if (strategy.isServiceFileOverride()) {
                serviceBuilder.enableFileOverride();
            }
            if (strategy.getServiceTemplate() != null && !strategy.getServiceTemplate().isBlank()) {
                serviceBuilder.serviceTemplate(strategy.getServiceTemplate());
            }
            if (strategy.getServiceImplTemplate() != null && !strategy.getServiceImplTemplate().isBlank()) {
                serviceBuilder.serviceImplTemplate(strategy.getServiceImplTemplate());
            }
        }
    }

    /** 映射 {@link InjectionConfigDto} 至 InjectionConfig.Builder，并注册生成进度回调。 */
    private void applyInjection(com.baomidou.mybatisplus.generator.config.InjectionConfig.Builder builder,
                                InjectionConfigDto injection,
                                Consumer<String> consumer) {
        if (injection.getCustomMap() != null && !injection.getCustomMap().isEmpty()) {
            builder.customMap(new HashMap<>(injection.getCustomMap()));
        }
        if (injection.getCustomFiles() != null) {
            for (CustomFileDto customFile : injection.getCustomFiles()) {
                if (customFile.getFileName() == null || customFile.getFileName().isBlank()) {
                    continue;
                }
                builder.customFile(cf -> cf.fileName(customFile.getFileName())
                        .templatePath(customFile.getTemplatePath())
                        .packageName(customFile.getPackageName()));
            }
        }
        builder.beforeOutputFile((tableInfo, objectMap) -> {
            String line = "Generating: " + tableInfo.getName() + " -> " + tableInfo.getEntityName();
            consumer.accept(line);
            generatedFiles.add(line);
        });
    }

    /** 根据配置创建 Freemarker 或 Velocity 模板引擎实例。 */
    private AbstractTemplateEngine createTemplateEngine(InjectionConfigDto injection) {
        String engine = injection.getTemplateEngine() == null ? "FREEMARKER" : injection.getTemplateEngine();
        return switch (engine.toUpperCase()) {
            case "VELOCITY" -> new VelocityTemplateEngine();
            default -> new FreemarkerTemplateEngine();
        };
    }

    private DateType parseDateType(String value) {
        if (value == null) {
            return DateType.TIME_PACK;
        }
        return switch (value.toUpperCase()) {
            case "ONLY_DATE" -> DateType.ONLY_DATE;
            case "SQL_PACK" -> DateType.SQL_PACK;
            default -> DateType.TIME_PACK;
        };
    }

    private NamingStrategy parseNaming(String value) {
        if (value == null || value.isBlank()) {
            return NamingStrategy.underline_to_camel;
        }
        return switch (value.toLowerCase()) {
            case "no_change" -> NamingStrategy.no_change;
            default -> NamingStrategy.underline_to_camel;
        };
    }

    private IdType parseIdType(String value) {
        if (value == null || value.isBlank()) {
            return IdType.AUTO;
        }
        return switch (value.toUpperCase()) {
            case "NONE" -> IdType.NONE;
            case "INPUT" -> IdType.INPUT;
            case "ASSIGN_ID" -> IdType.ASSIGN_ID;
            case "ASSIGN_UUID" -> IdType.ASSIGN_UUID;
            default -> IdType.AUTO;
        };
    }
}
