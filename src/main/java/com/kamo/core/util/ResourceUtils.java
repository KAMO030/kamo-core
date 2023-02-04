package com.kamo.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.function.Consumer;

public final class ResourceUtils {

    public static final String PROPERTIES_SUFFIX = ".properties";

    public static URL getResource(String resourcePath) {
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        return defaultClassLoader.getResource(resourcePath);
    }

    public static InputStream getResourceAsStream(String resourcePath) {
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        return defaultClassLoader.getResourceAsStream(resourcePath);
    }

    public static Reader getResourceAsReader(String resourcePath) {
        return new InputStreamReader(getResourceAsStream(resourcePath));
    }

    public static void forEachResources(String resourcePath, Consumer<URL> consumer) throws IOException {
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        forEachResources(resourcePath,defaultClassLoader,consumer);
    }
    public static void forEachResources(String resourcePath,ClassLoader classLoader, Consumer<URL> consumer) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(resourcePath);
        while (resources.hasMoreElements()) {
            consumer.accept(resources.nextElement());
        }
    }
    public static void loadResource(Properties properties, InputStream inputStream) {
        loadResource(properties, new InputStreamReader(inputStream));
    }

    public static void loadResource(Properties properties, Reader reader) {
        ObjectUtils.requireNonNull(properties, reader);
        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
