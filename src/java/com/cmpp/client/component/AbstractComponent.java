package com.cmpp.client.component;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractComponent
  implements BeanFactoryAware, InitializingBean, DisposableBean
{
  protected boolean initialized = false;
  protected boolean destroied = false;
  protected BeanFactory beanFactory = null;
  protected CacheManager cacheManager = null;

  public void setCacheManager(CacheManager manager) {
    this.cacheManager = manager;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if ((this.initialized) || (this.destroied)) {
      throw new Exception("initialize failed cause initialized or disposed");
    }

    this.initialized = true;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public void destroy() throws Exception {
    this.destroied = true;
  }
}