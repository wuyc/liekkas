package io.liekkas.ioc.reader;

import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.entity.ClassEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public class PackageReader implements ClassReader {

    private String packageName;

    @Override
    public Set<ClassEntity> readClasses() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        Set<ClassEntity> classes = new HashSet<>();
        try {
            Enumeration<URL> dirs = classLoader.getResources(packagePath);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String filePath = new URI(url.getFile()).getPath();
                findClassesByPackage(packageName, filePath, classes);
            }
        } catch (Exception e) {
            throw new LiekkasException("Package scan failed.", e);
        }
        return classes;
    }

    private Set<ClassEntity> findClassesByPackage(String packageName,
                                                 String packageAbsolutePath,
                                                 Set<ClassEntity> classes)
            throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File dir = new File(packageAbsolutePath);
        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("The package [{}] not found.", packageName);
        }
        File[] files = dirFilter(dir);
        if (null != files && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = classLoader.loadClass(packageName + "." + className);
                    classes.add(new ClassEntity(clazz));
                }
                if (file.isDirectory()) {
                    findClassesByPackage(packageName + "." + file.getName(), file.getAbsolutePath(), classes);
                }
            }
        }
        return classes;
    }

    private File[] dirFilter(File dir) {
        return dir.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(".class"));
    }

}
