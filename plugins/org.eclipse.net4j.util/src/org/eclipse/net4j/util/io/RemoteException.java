/*
 * Copyright (c) 2022 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.io;

/**
 * An exception that wraps an exception that has been thrown in a different JVM.
 *
 * @author Eike Stepper
 * @since 3.17
 */
public class RemoteException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public RemoteException()
  {
  }

  public RemoteException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public RemoteException(String message)
  {
    super(message);
  }

  public RemoteException(Throwable cause)
  {
    super(cause);
  }

  protected RemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public RuntimeException unwrap()
  {
    Throwable cause = getCause();
    if (cause instanceof RuntimeException)
    {
      throw (RuntimeException)cause;
    }

    return this;
  }
}
