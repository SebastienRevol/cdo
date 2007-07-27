/***************************************************************************
 * Copyright (c) 2004 - 2007 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.net4j.util.om;

import org.eclipse.net4j.internal.util.bundle.OM;
import org.eclipse.net4j.internal.util.om.OSGiBundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 */
public abstract class OSGiActivator implements BundleActivator
{
  private OMBundle omBundle;

  public OSGiActivator(OMBundle omBundle)
  {
    this.omBundle = omBundle;
  }

  public final OMBundle getOMBundle()
  {
    return omBundle;
  }

  public void start(BundleContext context) throws Exception
  {
    OM.Activator.traceStart(context);
    OSGiBundle bundle = (OSGiBundle)getOMBundle();
    if (bundle == null)
    {
      throw new IllegalStateException("bundle == null");
    }

    try
    {
      bundle.setBundleContext(context);
      bundle.start();
    }
    catch (Error error)
    {
      bundle.logger().error(error);
      throw error;
    }
    catch (Exception ex)
    {
      bundle.logger().error(ex);
      throw ex;
    }
  }

  public void stop(BundleContext context) throws Exception
  {
    OM.Activator.traceStop(context);
    OSGiBundle bundle = (OSGiBundle)getOMBundle();
    if (bundle == null)
    {
      throw new IllegalStateException("bundle == null");
    }

    try
    {
      bundle.stop();
      bundle.setBundleContext(null);
    }
    catch (Error error)
    {
      bundle.logger().error(error);
      throw error;
    }
    catch (Exception ex)
    {
      bundle.logger().error(ex);
      throw ex;
    }
  }

  @Override
  public final boolean equals(Object obj)
  {
    return super.equals(obj);
  }

  @Override
  public final int hashCode()
  {
    return super.hashCode();
  }

  @Override
  public final String toString()
  {
    return super.toString();
  }

  @Override
  protected final Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  @Override
  protected final void finalize() throws Throwable
  {
    super.finalize();
  }
}
