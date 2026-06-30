package com.example.generator.ui.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.example.generator.ui.dto.CustomFileDto;
import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.GenerationResult;
import com.example.generator.ui.dto.GeneratorProfile;
import com.example.generator.ui.dto.GlobalConfigDto;
import com.example.generator.ui.dto.InjectionConfigDto;
import com.example.generator.ui.dto.PackageConfigDto;
import com.example.generator.ui.dto.StrategyConfigDto;
import com.example.generator.ui.util.JdbcUrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GeneratorService {

    private static final Logger log = LoggerFactory.getLogger(GeneratorService.class);

    private Consumer<String> logConsumer = message -> {
    };
    private final List<String> generatedFiles = Collections.synchronizedList(new ArrayList<>());
    private final List<String> skippedFiles = Collections.synchronizedList(new ArrayList<>());

    public void setLogConsumer(Consumer<String> logConsumer) {
        this.logConsumer = logConsumer == null ? message -> {
        } : logConsumer;
    }

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
                mapperBuilder.enableMapperAnnotation();
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
