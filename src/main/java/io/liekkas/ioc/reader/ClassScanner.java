package io.liekkas.ioc.reader;

import io.liekkas.ioc.entity.ClassEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public class ClassScanner {

    public static Set<ClassEntity> getClasses(String packageName) {
        ClassReader classReader = getClassReader(packageName);
        return classReader.readClasses();
    }

    private static ClassReader getClassReader(String packageName) {
        if (isJarPackage(packageName)) {
            return new JarFileReader(packageName);
        }
        return new PackageReader(packageName);
    }

    private static boolean isJarPackage(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String packagePath = packageName.replace(".", "/");
            Enumeration<URL> dirs = classLoader.getResources(packagePath);
            if (dirs.hasMoreElements()) {
                String url = dirs.nextElement().toString();
                return url.indexOf(".jar!") != -1 || url.indexOf(".zip!") != -1;
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return false;
    }

}
