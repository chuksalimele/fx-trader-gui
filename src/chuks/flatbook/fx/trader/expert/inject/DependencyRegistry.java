/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.expert.inject;

import java.util.*;

/**
 *
 * @author user
 */
// Registry to map interfaces to implementations
public class DependencyRegistry {
    private final Map<Class<?>, Class<?>> registry = new HashMap<>();

    public <T> void register(Class<T> interfaceType, Class<? extends T> implementationType) {
        registry.put(interfaceType, implementationType);
    }

    public Class<?> getImplementation(Class<?> interfaceType) {
        return registry.get(interfaceType);
    }
}


