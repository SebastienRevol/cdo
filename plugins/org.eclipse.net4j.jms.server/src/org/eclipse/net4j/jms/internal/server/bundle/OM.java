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
package org.eclipse.net4j.jms.internal.server.bundle;

import org.eclipse.net4j.jms.internal.server.Server;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.OSGiActivator;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.trace.OMTracer;

/**
 * @author Eike Stepper
 */
public final class OM
{
  public static final String BUNDLE_ID = "org.eclipse.net4j.jms.server"; //$NON-NLS-1$

  public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, OM.class);

  public static final OMTracer DEBUG = BUNDLE.tracer("debug"); //$NON-NLS-1$

  public static final OMTracer DEBUG_PROTOCOL = DEBUG.tracer("protocol"); //$NON-NLS-1$

  public static final OMTracer DEBUG_STORE = DEBUG.tracer("store"); //$NON-NLS-1$

  public static final OMLogger LOG = BUNDLE.logger();

  private OM()
  {
  }

  public static final class Activator extends OSGiActivator
  {
    @Override
    protected OMBundle getOMBundle()
    {
      return BUNDLE;
    }

    @Override
    protected void start() throws Exception
    {
      Server.INSTANCE.activate();
    }

    @Override
    protected void stop() throws Exception
    {
      Server.INSTANCE.deactivate();
    }
  }
}
