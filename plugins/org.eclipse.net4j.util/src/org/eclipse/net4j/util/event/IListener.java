/*
 * Copyright (c) 2007, 2011, 2012, 2015, 2019, 2021 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.event;

import java.util.EventListener;

/**
 * A callback interface that {@link INotifier notifiers} use to pass {@link IEvent events} to.
 *
 * @author Eike Stepper
 */
@FunctionalInterface
public interface IListener extends EventListener
{
  public void notifyEvent(IEvent event);

  /**
   * @author Eike Stepper
   * @since 3.16
   */
  public interface NotifierAware extends IListener
  {
    public void addNotifier(INotifier notifier);

    public void removeNotifier(INotifier notifier);
  }
}
