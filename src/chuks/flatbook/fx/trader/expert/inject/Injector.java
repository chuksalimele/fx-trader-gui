/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.expert.inject;

import expert.reflect.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author user
 * @param <X> The target instance
 * @param <Y> The injected instance
 */
public class Injector <X,Y>{
    private final DependencyRegistry registry;
    private final Class clazzOwer;

    public Injector(DependencyRegistry registry, Class clazz) {
        this.registry = registry;
        this.clazzOwer = clazz;
    }

    private <T> T getInstance(Class<T> clazz, Object target) throws Exception {
        T instance = clazz.getDeclaredConstructor(clazzOwer).newInstance(target);
        return instance;
    }

    public Y injectFieldAndGet(X target) throws Exception {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                Class<?> fieldType = field.getType();
                Y dependency = (Y)getDependencyInstance(fieldType, target);
                field.setAccessible(true);//by pass private visibility
                field.set(target, dependency);
                return dependency;
            }
        }
        return null;
    }

    public Y injectMethodAndGet(X target) throws Exception {
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Inject.class)) {
                Class<?> paramType = method.getParameterTypes()[0];
                Y dependency = (Y)getDependencyInstance(paramType, target);
                method.setAccessible(true);//by pass private visibility
                method.invoke(target, dependency);
                return dependency;
            }
        }
        return null;
    }

    private Object getDependencyInstance(Class<?> fieldType, Object target) throws Exception {
        if (fieldType.isInterface()) {
            // Look up the implementation for the interface
            Class<?> implementationType = registry.getImplementation(fieldType);
            if (implementationType == null) {
                throw new RuntimeException("No implementation registered for interface: " + fieldType.getName());
            }
            return getInstance(implementationType, target);
        } else {
            // If it's not an interface, instantiate directly
            return getInstance(fieldType, target);
        }
    }
}

