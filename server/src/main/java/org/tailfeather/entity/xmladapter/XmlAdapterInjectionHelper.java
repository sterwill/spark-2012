package org.tailfeather.entity.xmladapter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
class XmlAdapterInjectionHelper implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		XmlAdapterInjectionHelper.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	@SuppressWarnings("unchecked")
	public static <T> T autowire(Class<?> beanClass) {
		if (context == null) {
			return null;
		}
		return (T) getContext().getAutowireCapableBeanFactory().autowire(beanClass,
				AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
	}
}
