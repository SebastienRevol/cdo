/*
 * Copyright (c) 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.util.test;

/**
 * @author Eike Stepper
 * @since 3.3
 */
public final class TestExecuter
{
  private static Object currentTest;

  private TestExecuter()
  {
  }

  public static Object getCurrrentTest()
  {
    return currentTest;
  }

  public static void execute(Object test, Executable executable) throws Throwable
  {
    if (currentTest != null)
    {
      throw new IllegalStateException("Recursive calls are not supported");
    }

    try
    {
      currentTest = test;
      executable.execute();
    }
    finally
    {
      currentTest = null;
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface Executable
  {
    public void execute() throws Throwable;
  }
}
