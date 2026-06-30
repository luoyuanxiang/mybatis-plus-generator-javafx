package com.example.generator.ui.service;

import com.example.generator.ui.dto.GeneratorProfile;
import com.example.generator.ui.util.AppPaths;
import com.example.generator.ui.util.IdUtil;
import com.example.generator.ui.util.StringListUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfileService {

    private final List<GeneratorProfile> profiles = new ArrayList<>();

    public ProfileService() {
        load();
    }

    public List<GeneratorProfile> findAll() {
        return new ArrayList<>(profiles);
    }

    public Optional<GeneratorProfile> findById(String id) {
        return profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public GeneratorProfile save(GeneratorProfile profile) {
        if (profile.getId() == null || profile.getId().isBlank()) {
            profile.setId(IdUtil.newId());
        }
        profiles.removeIf(p -> p.getId().equals(profile.getId()));
        profiles.add(copyProfile(profile));
        persist();
        return profile;
    }

    public void delete(String id) {
        profiles.removeIf(p -> p.getId().equals(id));
        persist();
    }

    public GeneratorProfile importFromFile(Path file) throws Exception {
        GeneratorProfile profile = StringListUtil.mapper().readValue(file.toFile(), GeneratorProfile.class);
        if (profile.getId() == null || profile.getId().isBlank()) {
            profile.setId(IdUtil.newId());
        }
        return save(profile);
    }

    public void exportToFile(GeneratorProfile profile, Path file) throws Exception {
        StringListUtil.mapper().writerWithDefaultPrettyPrinter().writeValue(file.toFile(), profile);
    }

    public String toJson(GeneratorProfile profile) throws Exception {
        return StringListUtil.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(profile);
    }

    private void load() {
        try {
            AppPaths.ensureAppHome();
            if (!Files.exists(AppPaths.profilesFile())) {
                GeneratorProfile defaultProfile = new GeneratorProfile();
                defaultProfile.setId(IdUtil.newId());
                defaultProfile.setName("default");
                profiles.add(defaultProfile);
                persist();
                return;
            }
            List<GeneratorProfile> loaded = StringListUtil.mapper().readValue(
                    AppPaths.profilesFile().toFile(),
                    new TypeReference<List<GeneratorProfile>>() {
                    }
            );
            profiles.clear();
            profiles.addAll(loaded);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load profiles.", e);
        }
    }

    private void persist() {
        try {
            AppPaths.ensureAppHome();
            StringListUtil.mapper().writerWithDefaultPrettyPrinter()
                    .writeValue(AppPaths.profilesFile().toFile(), profiles);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save profiles.", e);
        }
    }

    private GeneratorProfile copyProfile(GeneratorProfile source) {
        try {
            return StringListUtil.mapper().readValue(
                    StringListUtil.mapper().writeValueAsBytes(source),
                    GeneratorProfile.class
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to copy profile.", e);
        }
    }
}
