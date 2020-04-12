package io.liekkas.ioc.reader;

import io.liekkas.exception.LiekkasException;
import io.liekkas.ioc.entity.ClassEntity;
import lombok.AllArgsConstructor;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@AllArgsConstructor
public class JarFileReader implements ClassReader {

    private String packageName;

    @Override
    public Set<ClassEntity> readClasses() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL resource = classLoader.getResource(packagePath);
        JarFile jarFile = null;
        try {
            jarFile = ((JarURLConnection) resource.openConnection()).getJarFile();
            return findClassesByJar(packagePath, jarFile);
        } catch (Exception e) {
            throw new LiekkasException("Jar file scan failed.", e);
        }
    }

    private Set<ClassEntity> findClassesByJar(String packagePath, JarFile jarFile) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<ClassEntity> classes = new HashSet<>();
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }
            if (jarEntry.isDirectory() || !name.startsWith(packagePath) || !name.endsWith(".class")) {
                continue;
            }
            String className = name.substring(0, name.length() - 6);
            Class<?> clazz = classLoader.loadClass(className.replace("/", "."));
            classes.add(new ClassEntity(clazz));
        }
        return classes;
    }

}
