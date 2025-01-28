/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.expert;

import chuks.flatbook.fx.trader.exception.NotExpertClassException;
import expert.ExpertAdvisorMQ4;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.objectweb.asm.ClassReader;

/**
 *
 * @author user
 */
public class ExpertUtil {

    static private String expertAdvisorClassName = null;

    private static String getRawClassName(String path) throws FileNotFoundException, IOException {

        // Create a FileInputStream to read the .class file
        FileInputStream fis = new FileInputStream(path);

        // Create a ClassReader to read the class file
        ClassReader classReader = new ClassReader(fis);

        String className = classReader.getClassName();

        fis.close();
        return className;
    }

    public static boolean isExpert(File file) {
        boolean is_expert = false;
        try {
            if (!file.getName().endsWith(".class")) {
                return false;
            }
            if (expertAdvisorClassName == null) {
                expertAdvisorClassName = ExpertAdvisorMQ4.class.getCanonicalName();
            }
            // Create a FileInputStream to read the .class file
            FileInputStream fis = new FileInputStream(file);

            // Create a ClassReader to read the class file
            ClassReader classReader = new ClassReader(fis);

            String superName = classReader.getSuperName();

            String fullyQualifiedSuperName = superName.replace('/', '.');

            is_expert = fullyQualifiedSuperName.equals(expertAdvisorClassName);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return is_expert;
    }

    public static String expertSimpleName(String filename) {
        int index_dot = filename.lastIndexOf('.');
        int index_back_slash = filename.lastIndexOf('/');
        int index_forward_slash = filename.lastIndexOf('\\');

        int index_slash = index_back_slash > index_forward_slash
                ? index_back_slash
                : index_forward_slash;

        if (index_dot == -1) {
            index_dot = filename.length();
        }

        String name = filename.substring(index_slash + 1, index_dot);
        return name;
    }

    private static int getClassNameIndex(String path, String clazz) {
        String clazz_A = clazz;
        String clazz_B = clazz;
        if (clazz.indexOf('/') > 0) {
            clazz_B = clazz.replace('/', '\\');
        } else if (clazz.indexOf('\\') > 0) {
            clazz_B = clazz.replace('\\', '/');
        }

        char[] path_chars = path.toCharArray();
        char[] clazz_A_chars = clazz_A.toCharArray();
        int path_index = path_chars.length;
        for (int i = clazz_A_chars.length - 1; i > -1; --i) {
            --path_index;
            if (path_chars[path_index] != clazz_A_chars[i]) {
                break;
            }
        }

        if (path_index + clazz_A_chars.length == path_chars.length) {
            return path_index;
        }

        char[] clazz_B_chars = clazz_B.toCharArray();
        path_index = path_chars.length;
        for (int i = clazz_B_chars.length - 1; i > -1; --i) {
            --path_index;
            if (path_chars[path_index] != clazz_B_chars[i]) {
                break;
            }
        }

        if (path_index + clazz_B_chars.length == path_chars.length) {
            return path_index;
        }

        return -1;//should not happen except something is wrong
    }

    public static ExpertAdvisorMQ4 createExpert(String path) {

        try {

            String rawCls = getRawClassName(path);
            int index = getClassNameIndex(path, rawCls+".class");
            String classPath = path.substring(0, index);

            // Create a URL pointing to the directory containing the .class file
            File classFileDirectory = new File(classPath);
            URL classFileUrl = classFileDirectory.toURI().toURL();
            URL[] classLoaderUrls = new URL[]{classFileUrl};

            // Create a URLClassLoader that will load the class from the directory
            try (URLClassLoader classLoader = new URLClassLoader(classLoaderUrls)) {
                // Load the class dynamically
                String fullyQualifiedSuperName = rawCls.replace('/', '.');
                Class<?> loadedClass = classLoader.loadClass(fullyQualifiedSuperName);

                // Create an instance of the loaded class
                Object instance = loadedClass.getDeclaredConstructor().newInstance();
                if (instance instanceof ExpertAdvisorMQ4 expertAdvisorMQ4) {
                    return expertAdvisorMQ4;
                } else {
                    throw new NotExpertClassException("Failed to instantiate class - class is expected to extend " + ExpertAdvisorMQ4.class.getName() + " as its super class");
                }

            } catch (IOException | ClassNotFoundException | NoSuchMethodException
                    | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
