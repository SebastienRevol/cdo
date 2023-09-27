/*
 * Copyright (c) 2007-2009, 2011-2013, 2015, 2019, 2021 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Christian W. Damus (CEA) - private plug-in container instances
 */
package org.eclipse.net4j.util.container;

import org.eclipse.net4j.internal.util.container.PluginContainer;
import org.eclipse.net4j.util.StringConverter;
import org.eclipse.net4j.util.concurrent.ExecutorServiceFactory;
import org.eclipse.net4j.util.concurrent.TimerLifecycle;
import org.eclipse.net4j.util.event.EventUtil;
import org.eclipse.net4j.util.event.IListener;
import org.eclipse.net4j.util.properties.PropertiesContainerUtil;
import org.eclipse.net4j.util.security.FileUserManagerFactory;
import org.eclipse.net4j.util.security.RandomizerFactory;

/**
 * Various static helper methods for dealing with {@link IContainer containers}.
 *
 * @author Eike Stepper
 */
public final class ContainerUtil
{
  /**
   * @since 3.15
   */
  public static final String PROP_CONTAINER = "org.eclipse.net4j.util.container";

  private static final Object[] NO_ELEMENTS = {};

  private static final IContainer<Object> EMPTY = new IContainer<Object>()
  {
    @Override
    public Object[] getElements()
    {
      return NO_ELEMENTS;
    }

    @Override
    public boolean isEmpty()
    {
      return true;
    }

    @Override
    public void addListener(IListener listener)
    {
    }

    @Override
    public void removeListener(IListener listener)
    {
    }

    @Override
    public IListener[] getListeners()
    {
      return EventUtil.NO_LISTENERS;
    }

    @Override
    public boolean hasListeners()
    {
      return false;
    }

    @Override
    public String toString()
    {
      return "EMPTY_CONTAINER"; //$NON-NLS-1$
    }
  };

  private ContainerUtil()
  {
  }

  /**
   * @since 3.15
   */
  public static IManagedContainer getContainer(Object object)
  {
    if (object instanceof IManagedContainerProvider)
    {
      return ((IManagedContainerProvider)object).getContainer();
    }

    return PropertiesContainerUtil.getProperty(object, PROP_CONTAINER, IManagedContainer.class);
  }

  /**
   * @since 2.0
   */
  public static void prepareContainer(IManagedContainer container)
  {
    container.registerFactory(new TimerLifecycle.DaemonFactory());
    container.registerFactory(new ExecutorServiceFactory());
    container.registerFactory(new RandomizerFactory());
    container.registerFactory(new FileUserManagerFactory());
    container.registerFactory(new StringConverter.MetaFactory());
  }

  public static IContainer<Object> emptyContainer()
  {
    return EMPTY;
  }

  public static IManagedContainer createContainer()
  {
    return new ManagedContainer();
  }

  public static boolean isEmpty(Object container)
  {
    if (container instanceof IContainer<?>)
    {
      return ((IContainer<?>)container).isEmpty();
    }

    return true;
  }

  public static Object[] getElements(Object container)
  {
    if (container instanceof IContainer<?>)
    {
      return ((IContainer<?>)container).getElements();
    }

    return NO_ELEMENTS;
  }

  /**
   * Creates a new managed container that discovers factory and element processor
   * registrations in plug-in extensions.
   *
   * @since 3.3
   */
  public static IManagedContainer createPluginContainer()
  {
    return new PluginContainer();
  }
}
