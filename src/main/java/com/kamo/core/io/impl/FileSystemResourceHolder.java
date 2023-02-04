package com.kamo.core.io.impl;

import com.kamo.core.io.ResourceHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件资源信息解析器
 */
public class FileSystemResourceHolder implements ResourceHolder {


    private final String path;

    public FileSystemResourceHolder(File file) {
        this.path = file.getPath();
    }

    public FileSystemResourceHolder(String path) {

        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(path);
    }

    public final String getPath() {
        return this.path;
    }

}
