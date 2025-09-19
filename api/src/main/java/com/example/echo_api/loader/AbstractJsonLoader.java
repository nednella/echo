package com.example.echo_api.loader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class AbstractJsonLoader<T> {

    protected ObjectMapper objectMapper;

    protected AbstractJsonLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public List<T> loadJsonFromResourceFile(String filePath, Class<T> entity) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());

        return objectMapper.readValue(file,
            objectMapper.getTypeFactory().constructCollectionType(List.class, entity));
    }

    protected abstract String getFilePath();

    protected abstract void loadData() throws IOException;

    protected abstract void saveToRepository(List<T> entities);

}
