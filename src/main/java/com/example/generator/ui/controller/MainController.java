package com.example.generator.ui.controller;

import com.example.generator.ui.dto.ColumnMeta;
import com.example.generator.ui.dto.CustomFileDto;
import com.example.generator.ui.dto.DataSourceConfigDto;
import com.example.generator.ui.dto.DatabaseType;
import com.example.generator.ui.dto.GenerationResult;
import com.example.generator.ui.dto.GeneratorProfile;
import com.example.generator.ui.dto.GlobalConfigDto;
import com.example.generator.ui.dto.InjectionConfigDto;
import com.example.generator.ui.dto.PackageConfigDto;
import com.example.generator.ui.dto.StrategyConfigDto;
import com.example.generator.ui.dto.TableMeta;
import com.example.generator.ui.service.DataSourceRepository;
import com.example.generator.ui.service.DatabaseMetaService;
import com.example.generator.ui.service.GeneratorService;
import com.example.generator.ui.service.ProfileService;
import com.example.generator.ui.service.UiLogAppender;
import com.example.generator.ui.util.IdUtil;
import com.example.generator.ui.util.StringListUtil;
import com.example.generator.ui.util.ValidationUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainController {

    @FXML private ComboBox<DataSourceConfigDto> dataSourceCombo;
    @FXML private ComboBox<GeneratorProfile> profileCombo;
    @FXML private TextField profileNameField;
    @FXML private TextField dsNameField;
    @FXML private ComboBox<DatabaseType> dbTypeCombo;
    @FXML private TextField schemaField;
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private TextField databaseField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox savePasswordCheck;
    @FXML private TextField jdbcUrlField;

    @FXML private TextField tableFilterField;
    @FXML private ListView<CheckBoxTableItem> tableListView;
    @FXML private TableView<ColumnMeta> columnTableView;
    @FXML private TableColumn<ColumnMeta, String> colNameColumn;
    @FXML private TableColumn<ColumnMeta, String> colTypeColumn;
    @FXML private TableColumn<ColumnMeta, Number> colSizeColumn;
    @FXML private TableColumn<ColumnMeta, String> colNullableColumn;
    @FXML private TableColumn<ColumnMeta, String> colPkColumn;
    @FXML private TableColumn<ColumnMeta, String> colCommentColumn;

    @FXML private TextField authorField;
    @FXML private TextField outputDirField;
    @FXML private ComboBox<String> dateTypeCombo;
    @FXML private TextField commentDateField;
    @FXML private CheckBox enableSwaggerCheck;
    @FXML private CheckBox disableOpenDirCheck;
    @FXML private CheckBox enableKotlinCheck;
    @FXML private CheckBox enableAllFileOverrideCheck;

    @FXML private TextField parentPackageField;
    @FXML private TextField moduleNameField;
    @FXML private TextField entityPackageField;
    @FXML private TextField servicePackageField;
    @FXML private TextField serviceImplPackageField;
    @FXML private TextField mapperPackageField;
    @FXML private TextField controllerPackageField;
    @FXML private TextField xmlPackageField;
    @FXML private TextField mapperXmlPathField;

    @FXML private TextField tablePrefixField;
    @FXML private TextField tableSuffixField;
    @FXML private TextField excludeTablesField;
    @FXML private ComboBox<String> idTypeCombo;
    @FXML private ComboBox<String> namingStrategyCombo;
    @FXML private CheckBox enableLombokCheck;
    @FXML private CheckBox enableTableFieldAnnotationCheck;
    @FXML private CheckBox enableChainModelCheck;
    @FXML private CheckBox entityFileOverrideCheck;
    @FXML private TextField logicDeleteField;
    @FXML private TextField versionColumnField;
    @FXML private CheckBox controllerRestStyleCheck;
    @FXML private CheckBox controllerHyphenStyleCheck;
    @FXML private CheckBox mapperAnnotationCheck;
    @FXML private CheckBox mapperBaseResultMapCheck;
    @FXML private CheckBox mapperBaseColumnListCheck;
    @FXML private CheckBox controllerFileOverrideCheck;
    @FXML private CheckBox mapperFileOverrideCheck;
    @FXML private CheckBox serviceFileOverrideCheck;
    @FXML private CheckBox disableControllerCheck;
    @FXML private CheckBox disableServiceCheck;
    @FXML private CheckBox disableMapperCheck;

    @FXML private ComboBox<String> templateEngineCombo;
    @FXML private TextField entityTemplateField;
    @FXML private TextField mapperTemplateField;
    @FXML private TextField mapperXmlTemplateField;
    @FXML private TextField serviceTemplateField;
    @FXML private TextField serviceImplTemplateField;
    @FXML private TextField controllerTemplateField;
    @FXML private TextField customMapKeyField;
    @FXML private TextField customMapValueField;
    @FXML private ListView<String> customMapListView;
    @FXML private TextField customFileNameField;
    @FXML private TextField customFileTemplateField;
    @FXML private TextField customFilePackageField;
    @FXML private ListView<CustomFileDto> customFileListView;

    @FXML private TextArea configPreviewArea;
    @FXML private TextArea logArea;
    @FXML private ListView<String> generatedFilesListView;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statusLabel;

    private Stage stage;
    private final DataSourceRepository dataSourceRepository = new DataSourceRepository();
    private final DatabaseMetaService databaseMetaService = new DatabaseMetaService();
    private final GeneratorService generatorService = new GeneratorService();
    private final ProfileService profileService = new ProfileService();

    private final ObservableList<CheckBoxTableItem> allTableItems = FXCollections.observableArrayList();
    private final ObservableList<CheckBoxTableItem> filteredTableItems = FXCollections.observableArrayList();
    private final Map<String, String> customMap = new LinkedHashMap<>();
    private final ObservableList<CustomFileDto> customFiles = FXCollections.observableArrayList();

    private DataSourceConfigDto currentDataSource;
    private GeneratorProfile currentProfile;
    private boolean suppressProfileLoad;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        UiLogAppender.install(generatorService::handleLog);
        generatorService.setLogConsumer(message -> Platform.runLater(() -> appendLog(message)));

        dbTypeCombo.setItems(FXCollections.observableArrayList(DatabaseType.values()));
        dbTypeCombo.getSelectionModel().select(DatabaseType.MYSQL);
        dbTypeCombo.setOnAction(e -> onDbTypeChanged());

        dateTypeCombo.setItems(FXCollections.observableArrayList("TIME_PACK", "ONLY_DATE", "SQL_PACK"));
        dateTypeCombo.getSelectionModel().selectFirst();

        idTypeCombo.setItems(FXCollections.observableArrayList("AUTO", "NONE", "INPUT", "ASSIGN_ID", "ASSIGN_UUID"));
        idTypeCombo.getSelectionModel().selectFirst();

        namingStrategyCombo.setItems(FXCollections.observableArrayList("underline_to_camel", "no_change"));
        namingStrategyCombo.getSelectionModel().selectFirst();

        templateEngineCombo.setItems(FXCollections.observableArrayList("FREEMARKER", "VELOCITY"));
        templateEngineCombo.getSelectionModel().selectFirst();

        dataSourceCombo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(DataSourceConfigDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        dataSourceCombo.setButtonCell(dataSourceCombo.getCellFactory().call(null));

        profileCombo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(GeneratorProfile item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        profileCombo.setButtonCell(profileCombo.getCellFactory().call(null));

        tableListView.setItems(filteredTableItems);
        tableListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(CheckBoxTableItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(item.getCheckBox());
                    setText(null);
                }
            }
        });
        tableListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadColumns(newVal.getTableName());
            }
        });

        colNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        colNullableColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().isNullable() ? "Y" : "N"));
        colPkColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().isPrimaryKey() ? "Y" : "N"));
        colCommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        customFileListView.setItems(customFiles);
        customFileListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(CustomFileDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null
                        : item.getFileName() + " | " + item.getTemplatePath() + " | " + item.getPackageName());
            }
        });

        customFileTemplateField.setText("/templates/entityDTO.java.ftl");
        customFilePackageField.setText("dto");

        enableAllFileOverrideCheck.selectedProperty().addListener((obs, oldVal, selected) -> {
            if (Boolean.TRUE.equals(selected)) {
                entityFileOverrideCheck.setSelected(true);
                controllerFileOverrideCheck.setSelected(true);
                mapperFileOverrideCheck.setSelected(true);
                serviceFileOverrideCheck.setSelected(true);
            }
        });

        customMapListView.setItems(FXCollections.observableArrayList());
        refreshDataSources();
        refreshProfiles();
        onNewDataSource();
    }

    @FXML
    public void onNewDataSource() {
        currentDataSource = new DataSourceConfigDto();
        currentDataSource.setId(IdUtil.newId());
        currentDataSource.setName("new-datasource");
        bindDataSourceToForm(currentDataSource);
    }

    @FXML
    public void onSaveDataSource() {
        try {
            DataSourceConfigDto config = readDataSourceFromForm();
            ValidationUtil.validateDataSource(config);
            dataSourceRepository.save(config);
            currentDataSource = config;
            refreshDataSources();
            selectDataSource(config);
            setStatus("Data source saved.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onDeleteDataSource() {
        if (currentDataSource == null || currentDataSource.getId() == null) {
            return;
        }
        Optional<ButtonType> confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete data source '" + currentDataSource.getName() + "'?",
                ButtonType.YES, ButtonType.NO).showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
            dataSourceRepository.delete(currentDataSource.getId());
            refreshDataSources();
            onNewDataSource();
            setStatus("Data source deleted.");
        }
    }

    @FXML
    public void onDataSourceSelected() {
        DataSourceConfigDto selected = dataSourceCombo.getSelectionModel().getSelectedItem();
        if (selected != null) {
            currentDataSource = selected.copy();
            bindDataSourceToForm(currentDataSource);
        }
    }

    @FXML
    public void onDbTypeChanged() {
        DatabaseType type = dbTypeCombo.getSelectionModel().getSelectedItem();
        if (type != null && type != DatabaseType.H2) {
            portField.setText(String.valueOf(type.getDefaultPort()));
        }
    }

    @FXML
    public void onTestConnection() {
        runBackground("Testing connection...", () -> {
            DataSourceConfigDto config = readDataSourceFromForm();
            ValidationUtil.validateDataSource(config);
            databaseMetaService.testConnection(config);
            return "Connection successful.";
        });
    }

    @FXML
    public void onLoadTables() {
        runBackground("Loading tables...", () -> {
            DataSourceConfigDto config = readDataSourceFromForm();
            ValidationUtil.validateDataSource(config);
            List<TableMeta> tables = databaseMetaService.listTables(config);
            Platform.runLater(() -> populateTables(tables));
            return "Loaded " + tables.size() + " tables.";
        });
    }

    @FXML
    public void onTableFilterChanged() {
        applyTableFilter();
    }

    @FXML
    public void onSelectAllTables() {
        filteredTableItems.forEach(item -> item.getCheckBox().setSelected(true));
    }

    @FXML
    public void onInvertTables() {
        filteredTableItems.forEach(item -> item.getCheckBox().setSelected(!item.getCheckBox().isSelected()));
    }

    @FXML
    public void onBrowseOutputDir() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select output directory");
        File selected = chooser.showDialog(stage);
        if (selected != null) {
            outputDirField.setText(selected.getAbsolutePath());
        }
    }

    @FXML
    public void onBrowseMapperXmlPath() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Mapper XML output directory");
        File selected = chooser.showDialog(stage);
        if (selected != null) {
            mapperXmlPathField.setText(selected.getAbsolutePath());
        }
    }

    @FXML
    public void onAddCustomMap() {
        String key = customMapKeyField.getText();
        String value = customMapValueField.getText();
        if (key == null || key.isBlank()) {
            showError("customMap key is required.");
            return;
        }
        customMap.put(key.trim(), value == null ? "" : value);
        refreshCustomMapList();
        customMapKeyField.clear();
        customMapValueField.clear();
    }

    @FXML
    public void onRemoveCustomMap() {
        String selected = customMapListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int idx = selected.indexOf('=');
            if (idx > 0) {
                customMap.remove(selected.substring(0, idx).trim());
            }
            refreshCustomMapList();
        }
    }

    @FXML
    public void onAddCustomFile() {
        CustomFileDto dto = new CustomFileDto(
                customFileNameField.getText(),
                customFileTemplateField.getText(),
                customFilePackageField.getText()
        );
        if (dto.getFileName() == null || dto.getFileName().isBlank()) {
            showError("Custom file name is required.");
            return;
        }
        customFiles.add(dto);
        customFileNameField.clear();
        customFileTemplateField.setText("/templates/entityDTO.java.ftl");
        customFilePackageField.setText("dto");
    }

    @FXML
    public void onRemoveCustomFile() {
        CustomFileDto selected = customFileListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            customFiles.remove(selected);
        }
    }

    @FXML
    public void onNewProfile() {
        currentProfile = new GeneratorProfile();
        currentProfile.setId(IdUtil.newId());
        currentProfile.setName("new-profile");
        profileNameField.setText(currentProfile.getName());
        bindProfileToForm(currentProfile);
        setStatus("New profile created. Configure and save.");
    }

    @FXML
    public void onProfileSelected() {
        if (suppressProfileLoad) {
            return;
        }
        GeneratorProfile selected = profileCombo.getSelectionModel().getSelectedItem();
        if (selected != null) {
            currentProfile = selected;
            profileNameField.setText(selected.getName());
            bindProfileToForm(selected);
            if (selected.getDataSourceId() != null) {
                dataSourceRepository.findById(selected.getDataSourceId()).ifPresent(ds -> {
                    currentDataSource = ds.copy();
                    bindDataSourceToForm(currentDataSource);
                    selectDataSource(ds);
                });
            }
            restoreSelectedTables(selected.getSelectedTables());
        }
    }

    @FXML
    public void onSaveProfile() {
        try {
            GeneratorProfile profile = readProfileFromForm();
            profile.setName(profileNameField.getText());
            if (profile.getName() == null || profile.getName().isBlank()) {
                profile.setName("profile-" + System.currentTimeMillis());
            }
            if (currentDataSource != null) {
                profile.setDataSourceId(currentDataSource.getId());
            }
            profileService.save(profile);
            currentProfile = profile;
            refreshProfiles();
            selectProfile(profile);
            setStatus("Profile saved.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onDeleteProfile() {
        if (currentProfile == null || currentProfile.getId() == null) {
            return;
        }
        Optional<ButtonType> confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete profile '" + currentProfile.getName() + "'?",
                ButtonType.YES, ButtonType.NO).showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
            profileService.delete(currentProfile.getId());
            refreshProfiles();
            if (!profileService.findAll().isEmpty()) {
                selectProfile(profileService.findAll().get(0));
            }
            setStatus("Profile deleted.");
        }
    }

    @FXML
    public void onImportProfile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import profile JSON");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            try {
                GeneratorProfile profile = profileService.importFromFile(file.toPath());
                refreshProfiles();
                selectProfile(profile);
                bindProfileToForm(profile);
                setStatus("Profile imported.");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    @FXML
    public void onExportProfile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export profile JSON");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            try {
                GeneratorProfile profile = readProfileFromForm();
                profileService.exportToFile(profile, Path.of(file.getAbsolutePath()));
                setStatus("Profile exported.");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    @FXML
    public void onPreviewConfig() {
        try {
            GeneratorProfile profile = readProfileFromForm();
            configPreviewArea.setText(profileService.toJson(profile));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onGenerate() {
        GeneratorProfile profile = readProfileFromForm();
        DataSourceConfigDto dataSource = readDataSourceFromForm();
        if (profile.getStrategyConfig().isEntityFileOverride()
                || profile.getStrategyConfig().isControllerFileOverride()
                || profile.getStrategyConfig().isMapperFileOverride()
                || profile.getStrategyConfig().isServiceFileOverride()) {
            Optional<ButtonType> confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "File override is enabled. Existing files may be overwritten. Continue?",
                    ButtonType.YES, ButtonType.NO).showAndWait();
            if (confirm.isEmpty() || confirm.get() != ButtonType.YES) {
                return;
            }
        }

        logArea.clear();
        generatedFilesListView.getItems().clear();
        progressIndicator.setVisible(true);
        setStatus("Generating code...");

        Task<GenerationResult> task = new Task<>() {
            @Override
            protected GenerationResult call() {
                return generatorService.generate(dataSource, profile);
            }
        };
        task.setOnSucceeded(e -> {
            progressIndicator.setVisible(false);
            GenerationResult result = task.getValue();
            if (result.isSuccess()) {
                List<String> displayItems = new ArrayList<>(result.getGeneratedFiles());
                if (result.hasSkippedFiles()) {
                    displayItems.add("--- Skipped (enable file override to overwrite) ---");
                    displayItems.addAll(result.getSkippedFiles());
                }
                generatedFilesListView.setItems(FXCollections.observableArrayList(displayItems));
                setStatus(result.getMessage());
                if (result.hasSkippedFiles()) {
                    new Alert(Alert.AlertType.WARNING,
                            result.getSkippedFiles().size() + " file(s) already exist and were skipped.\n"
                                    + "Example: " + result.getSkippedFiles().get(0) + "\n\n"
                                    + "Go to Global Config and enable:\n"
                                    + "覆盖已存在的生成文件（Entity/Mapper/Service/Controller）",
                            ButtonType.OK).showAndWait();
                }
            } else {
                showError(result.getMessage());
                setStatus("Generation failed.");
            }
        });
        task.setOnFailed(e -> {
            progressIndicator.setVisible(false);
            showError(task.getException() == null ? "Generation failed." : task.getException().getMessage());
            setStatus("Generation failed.");
        });
        new Thread(task, "code-generator").start();
    }

    @FXML
    public void onOpenOutputDir() {
        String dir = outputDirField.getText();
        if (dir == null || dir.isBlank()) {
            showError("Output directory is empty.");
            return;
        }
        try {
            Desktop.getDesktop().open(new File(dir));
        } catch (Exception e) {
            showError("Unable to open directory: " + e.getMessage());
        }
    }

    private void refreshDataSources() {
        List<DataSourceConfigDto> items = dataSourceRepository.findAll();
        dataSourceCombo.setItems(FXCollections.observableArrayList(items));
    }

    private void refreshProfiles() {
        suppressProfileLoad = true;
        List<GeneratorProfile> items = profileService.findAll();
        profileCombo.setItems(FXCollections.observableArrayList(items));
        if (currentProfile == null && !items.isEmpty()) {
            currentProfile = items.get(0);
            selectProfile(currentProfile);
            profileNameField.setText(currentProfile.getName());
            bindProfileToForm(currentProfile);
        }
        suppressProfileLoad = false;
    }

    private void selectDataSource(DataSourceConfigDto config) {
        dataSourceCombo.getSelectionModel().select(config);
    }

    private void selectProfile(GeneratorProfile profile) {
        profileCombo.getSelectionModel().select(profile);
        currentProfile = profile;
    }

    private void bindDataSourceToForm(DataSourceConfigDto config) {
        dsNameField.setText(config.getName());
        dbTypeCombo.getSelectionModel().select(config.getDbType());
        schemaField.setText(config.getSchema() == null ? "" : config.getSchema());
        hostField.setText(config.getHost());
        portField.setText(String.valueOf(config.getPort()));
        databaseField.setText(config.getDatabase());
        usernameField.setText(config.getUsername());
        passwordField.setText(config.getPassword());
        savePasswordCheck.setSelected(config.isSavePassword());
        jdbcUrlField.setText(config.getJdbcUrl() == null ? "" : config.getJdbcUrl());
    }

    private DataSourceConfigDto readDataSourceFromForm() {
        DataSourceConfigDto config = currentDataSource == null ? new DataSourceConfigDto() : currentDataSource.copy();
        config.setName(dsNameField.getText());
        config.setDbType(dbTypeCombo.getSelectionModel().getSelectedItem());
        config.setSchema(schemaField.getText());
        config.setHost(hostField.getText());
        try {
            config.setPort(Integer.parseInt(portField.getText().trim()));
        } catch (NumberFormatException e) {
            config.setPort(config.getDbType().getDefaultPort());
        }
        config.setDatabase(databaseField.getText());
        config.setUsername(usernameField.getText());
        config.setPassword(passwordField.getText());
        config.setSavePassword(savePasswordCheck.isSelected());
        config.setJdbcUrl(jdbcUrlField.getText());
        return config;
    }

    private void bindProfileToForm(GeneratorProfile profile) {
        GlobalConfigDto global = profile.getGlobalConfig();
        authorField.setText(global.getAuthor());
        outputDirField.setText(global.getOutputDir());
        dateTypeCombo.getSelectionModel().select(global.getDateType());
        commentDateField.setText(global.getCommentDate());
        enableSwaggerCheck.setSelected(global.isEnableSwagger());
        disableOpenDirCheck.setSelected(global.isDisableOpenDir());
        enableKotlinCheck.setSelected(global.isEnableKotlin());

        PackageConfigDto pkg = profile.getPackageConfig();
        parentPackageField.setText(pkg.getParent());
        moduleNameField.setText(pkg.getModuleName());
        entityPackageField.setText(pkg.getEntity());
        servicePackageField.setText(pkg.getService());
        serviceImplPackageField.setText(pkg.getServiceImpl());
        mapperPackageField.setText(pkg.getMapper());
        controllerPackageField.setText(pkg.getController());
        xmlPackageField.setText(pkg.getXml());
        mapperXmlPathField.setText(pkg.getMapperXmlPath());

        StrategyConfigDto strategy = profile.getStrategyConfig();
        enableAllFileOverrideCheck.setSelected(isAllFileOverrideEnabled(strategy));
        tablePrefixField.setText(StringListUtil.joinCsv(strategy.getTablePrefixes()));
        tableSuffixField.setText(StringListUtil.joinCsv(strategy.getTableSuffixes()));
        excludeTablesField.setText(StringListUtil.joinCsv(strategy.getExcludeTables()));
        idTypeCombo.getSelectionModel().select(strategy.getIdType());
        namingStrategyCombo.getSelectionModel().select(strategy.getNamingStrategy());
        enableLombokCheck.setSelected(strategy.isEnableLombok());
        enableTableFieldAnnotationCheck.setSelected(strategy.isEnableTableFieldAnnotation());
        enableChainModelCheck.setSelected(strategy.isEnableChainModel());
        entityFileOverrideCheck.setSelected(strategy.isEntityFileOverride());
        controllerFileOverrideCheck.setSelected(strategy.isControllerFileOverride());
        mapperFileOverrideCheck.setSelected(strategy.isMapperFileOverride());
        serviceFileOverrideCheck.setSelected(strategy.isServiceFileOverride());
        logicDeleteField.setText(strategy.getLogicDeleteColumnName());
        versionColumnField.setText(strategy.getVersionColumnName());
        controllerRestStyleCheck.setSelected(strategy.isControllerRestStyle());
        controllerHyphenStyleCheck.setSelected(strategy.isControllerHyphenStyle());
        mapperAnnotationCheck.setSelected(strategy.isMapperAnnotation());
        mapperBaseResultMapCheck.setSelected(strategy.isMapperBaseResultMap());
        mapperBaseColumnListCheck.setSelected(strategy.isMapperBaseColumnList());
        disableControllerCheck.setSelected(strategy.isDisableController());
        disableServiceCheck.setSelected(strategy.isDisableService());
        disableMapperCheck.setSelected(strategy.isDisableMapper());

        entityTemplateField.setText(strategy.getEntityJavaTemplate());
        mapperTemplateField.setText(strategy.getMapperTemplate());
        mapperXmlTemplateField.setText(strategy.getMapperXmlTemplate());
        serviceTemplateField.setText(strategy.getServiceTemplate());
        serviceImplTemplateField.setText(strategy.getServiceImplTemplate());
        controllerTemplateField.setText(strategy.getControllerTemplate());

        InjectionConfigDto injection = profile.getInjectionConfig();
        templateEngineCombo.getSelectionModel().select(injection.getTemplateEngine());
        customMap.clear();
        if (injection.getCustomMap() != null) {
            customMap.putAll(injection.getCustomMap());
        }
        refreshCustomMapList();
        customFiles.setAll(injection.getCustomFiles() == null ? List.of() : injection.getCustomFiles());
    }

    private GeneratorProfile readProfileFromForm() {
        GeneratorProfile profile = currentProfile == null ? new GeneratorProfile() : currentProfile;
        if (profile.getId() == null || profile.getId().isBlank()) {
            profile.setId(IdUtil.newId());
        }

        GlobalConfigDto global = profile.getGlobalConfig();
        global.setAuthor(authorField.getText());
        global.setOutputDir(outputDirField.getText());
        global.setDateType(dateTypeCombo.getSelectionModel().getSelectedItem());
        global.setCommentDate(commentDateField.getText());
        global.setEnableSwagger(enableSwaggerCheck.isSelected());
        global.setDisableOpenDir(disableOpenDirCheck.isSelected());
        global.setEnableKotlin(enableKotlinCheck.isSelected());

        PackageConfigDto pkg = profile.getPackageConfig();
        pkg.setParent(parentPackageField.getText());
        pkg.setModuleName(moduleNameField.getText());
        pkg.setEntity(entityPackageField.getText());
        pkg.setService(servicePackageField.getText());
        pkg.setServiceImpl(serviceImplPackageField.getText());
        pkg.setMapper(mapperPackageField.getText());
        pkg.setController(controllerPackageField.getText());
        pkg.setXml(xmlPackageField.getText());
        pkg.setMapperXmlPath(mapperXmlPathField.getText());

        StrategyConfigDto strategy = profile.getStrategyConfig();
        StringListUtil.applyCsvToList(tablePrefixField.getText(), strategy.getTablePrefixes());
        StringListUtil.applyCsvToList(tableSuffixField.getText(), strategy.getTableSuffixes());
        StringListUtil.applyCsvToList(excludeTablesField.getText(), strategy.getExcludeTables());
        strategy.setIdType(idTypeCombo.getSelectionModel().getSelectedItem());
        strategy.setNamingStrategy(namingStrategyCombo.getSelectionModel().getSelectedItem());
        strategy.setEnableLombok(enableLombokCheck.isSelected());
        strategy.setEnableTableFieldAnnotation(enableTableFieldAnnotationCheck.isSelected());
        strategy.setEnableChainModel(enableChainModelCheck.isSelected());
        strategy.setEntityFileOverride(entityFileOverrideCheck.isSelected());
        strategy.setControllerFileOverride(controllerFileOverrideCheck.isSelected());
        strategy.setMapperFileOverride(mapperFileOverrideCheck.isSelected());
        strategy.setServiceFileOverride(serviceFileOverrideCheck.isSelected());
        if (enableAllFileOverrideCheck.isSelected()) {
            strategy.setEntityFileOverride(true);
            strategy.setControllerFileOverride(true);
            strategy.setMapperFileOverride(true);
            strategy.setServiceFileOverride(true);
        }
        strategy.setLogicDeleteColumnName(logicDeleteField.getText());
        strategy.setVersionColumnName(versionColumnField.getText());
        strategy.setControllerRestStyle(controllerRestStyleCheck.isSelected());
        strategy.setControllerHyphenStyle(controllerHyphenStyleCheck.isSelected());
        strategy.setMapperAnnotation(mapperAnnotationCheck.isSelected());
        strategy.setMapperBaseResultMap(mapperBaseResultMapCheck.isSelected());
        strategy.setMapperBaseColumnList(mapperBaseColumnListCheck.isSelected());
        strategy.setDisableController(disableControllerCheck.isSelected());
        strategy.setDisableService(disableServiceCheck.isSelected());
        strategy.setDisableMapper(disableMapperCheck.isSelected());
        strategy.setEntityJavaTemplate(entityTemplateField.getText());
        strategy.setMapperTemplate(mapperTemplateField.getText());
        strategy.setMapperXmlTemplate(mapperXmlTemplateField.getText());
        strategy.setServiceTemplate(serviceTemplateField.getText());
        strategy.setServiceImplTemplate(serviceImplTemplateField.getText());
        strategy.setControllerTemplate(controllerTemplateField.getText());

        InjectionConfigDto injection = profile.getInjectionConfig();
        injection.setTemplateEngine(templateEngineCombo.getSelectionModel().getSelectedItem());
        injection.setCustomMap(new LinkedHashMap<>(customMap));
        injection.setCustomFiles(new ArrayList<>(customFiles));

        profile.setSelectedTables(allTableItems.stream()
                .filter(item -> item.getCheckBox().isSelected())
                .map(CheckBoxTableItem::getTableName)
                .collect(Collectors.toList()));
        return profile;
    }

    private void populateTables(List<TableMeta> tables) {
        allTableItems.clear();
        for (TableMeta table : tables) {
            CheckBox checkBox = new CheckBox(table.getName() + (table.getComment().isBlank() ? "" : " - " + table.getComment()));
            allTableItems.add(new CheckBoxTableItem(table.getName(), checkBox));
        }
        applyTableFilter();
    }

    private void applyTableFilter() {
        String filter = tableFilterField.getText();
        filteredTableItems.setAll(allTableItems.stream()
                .filter(item -> filter == null || filter.isBlank()
                        || item.getTableName().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList()));
    }

    private void restoreSelectedTables(List<String> selectedTables) {
        if (selectedTables == null) {
            return;
        }
        allTableItems.forEach(item -> item.getCheckBox().setSelected(selectedTables.contains(item.getTableName())));
    }

    private void loadColumns(String tableName) {
        DataSourceConfigDto config = readDataSourceFromForm();
        Task<List<ColumnMeta>> task = new Task<>() {
            @Override
            protected List<ColumnMeta> call() throws Exception {
                return databaseMetaService.listColumns(config, tableName);
            }
        };
        task.setOnSucceeded(e -> columnTableView.setItems(FXCollections.observableArrayList(task.getValue())));
        task.setOnFailed(e -> showError(task.getException() == null ? "Failed to load columns." : task.getException().getMessage()));
        new Thread(task, "load-columns").start();
    }

    private void refreshCustomMapList() {
        ObservableList<String> items = FXCollections.observableArrayList();
        customMap.forEach((k, v) -> items.add(k + " = " + v));
        customMapListView.setItems(items);
    }

    private void runBackground(String runningMessage, BackgroundCallable callable) {
        progressIndicator.setVisible(true);
        setStatus(runningMessage);
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return callable.call();
            }
        };
        task.setOnSucceeded(e -> {
            progressIndicator.setVisible(false);
            setStatus(task.getValue());
        });
        task.setOnFailed(e -> {
            progressIndicator.setVisible(false);
            showError(task.getException() == null ? "Operation failed." : task.getException().getMessage());
            setStatus("Operation failed.");
        });
        new Thread(task, "background-task").start();
    }

    private void appendLog(String message) {
        logArea.appendText(message + System.lineSeparator());
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    private boolean isAllFileOverrideEnabled(StrategyConfigDto strategy) {
        return strategy.isEntityFileOverride()
                && strategy.isControllerFileOverride()
                && strategy.isMapperFileOverride()
                && strategy.isServiceFileOverride();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    @FunctionalInterface
    private interface BackgroundCallable {
        String call() throws Exception;
    }

    public static class CheckBoxTableItem {
        private final String tableName;
        private final CheckBox checkBox;

        public CheckBoxTableItem(String tableName, CheckBox checkBox) {
            this.tableName = tableName;
            this.checkBox = checkBox;
        }

        public String getTableName() {
            return tableName;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }
}
