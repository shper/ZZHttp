package cn.shper.okhttppan.entity;

import java.io.File;

/**
 * Author: Shper
 * Description: TODO
 * Version: 0.1 16-6-12 C 创建
 */
public class FileInput {

    public String key;
    public String filename;
    public File file;

    public FileInput(String name, String filename, File file) {
        this.key = name;
        this.filename = filename;
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileInput{" +
                "key='" + key + '\'' +
                ", filename='" + filename + '\'' +
                ", file=" + file +
                '}';
    }

}
