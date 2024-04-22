package com.github.microservice.core.util.scan;

import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    // 扫描指定包下的所有 class
    @SneakyThrows
    public static List<Class<?>> scanPackage(String packageName)  {
        List<Class<?>> classes = new ArrayList<>();
        // 将包名转换为文件路径
        String packagePath = packageName.replace('.', '/');
        // 获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 获取资源的 URL
        Enumeration<URL> urls = classLoader.getResources(packagePath);
        // 遍历 URL
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            // 获取协议
            String protocol = url.getProtocol();
            // 如果是文件协议
            if ("file".equals(protocol)) {
                // 获取文件路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                // 扫描文件夹下的 class
                scanFile(classes, packageName, filePath);
            }
            // 如果是 jar 协议
            else if ("jar".equals(protocol)) {
                // 扫描 jar 包下的 class
                scanJar(classes, packageName, url);
            }
        }
        return classes;
    }

    // 扫描文件夹下的 class
    @SneakyThrows
    public static void scanFile(List<Class<?>> classes, String packageName, String filePath)  {
        // 获取文件对象
        File file = new File(filePath);
        // 如果是文件夹
        if (file.isDirectory()) {
            // 获取子文件
            File[] files = file.listFiles();
            // 遍历子文件
            for (File f : files) {
                // 获取子文件名
                String fileName = f.getName();
                // 如果是文件夹，递归扫描
                if (f.isDirectory()) {
                    scanFile(classes, packageName + "." + fileName, f.getPath());
                }
                // 如果是 class 文件，加载类
                else if (fileName.endsWith(".class")) {
                    String className = fileName.substring(0, fileName.length() - 6);
                    Class<?> clazz = Class.forName(packageName + "." + className);
                    classes.add(clazz);
                }
            }
        }
    }

    // 扫描 jar 包下的 class
    @SneakyThrows
    public static void scanJar(List<Class<?>> classes, String packageName, URL url)  {
        // 获取 jar 文件的连接
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        // 获取 jar 文件对象
        JarFile jarFile = jarURLConnection.getJarFile();
        // 获取 jar 文件中的条目
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        // 遍历条目
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            // 获取条目名
            String jarEntryName = jarEntry.getName();
            // 如果是 class 文件，并且在指定包下
            if (jarEntryName.endsWith(".class") && jarEntryName.startsWith(packageName)) {
                // 去掉文件后缀
                String className = jarEntryName.substring(0, jarEntryName.length() - 6);
                // 将文件路径转换为类名
                className = className.replace('/', '.');
                // 加载类
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }
    }

}
