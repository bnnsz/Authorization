/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.components;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * Helper class which is able to autowire a specified class. It holds a static
 * reference to the {@link org
 * .springframework.context.ApplicationContext}.
 */
public final class AutowireHelper {

    private static final AutowireHelper INSTANCE = new AutowireHelper();
    private static ApplicationContext applicationContext;

    private AutowireHelper() {
    }

    public AutowireHelper autowireHelper() {
        return AutowireHelper.getInstance();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }

    public static <T> T autowire(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * Tries to autowire the specified instance of the class if one of the
     * specified beans which need to be autowired are null.
     *
     * @param classToAutowire the instance of the class which holds @Autowire
     * annotations
     * @param beansToAutowireInClass the beans which have the @Autowire
     * annotation in the specified {#classToAutowire}
     */
    public static void autowire(Object classToAutowire, Object... beansToAutowireInClass) {
        for (Object bean : beansToAutowireInClass) {
            if (bean == null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
                return;
            }
        }
    }

    /**
     * @return the singleton instance.
     */
    public static AutowireHelper getInstance() {
        return INSTANCE;
    }

}
