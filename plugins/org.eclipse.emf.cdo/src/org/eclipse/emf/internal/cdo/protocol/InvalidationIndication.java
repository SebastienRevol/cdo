/***************************************************************************
 * Copyright (c) 2004-2007 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.internal.cdo.protocol;

import org.eclipse.emf.cdo.internal.protocol.CDOIDImpl;
import org.eclipse.emf.cdo.internal.protocol.bundle.CDOProtocol;
import org.eclipse.emf.cdo.protocol.CDOID;
import org.eclipse.emf.cdo.protocol.CDOProtocolConstants;

import org.eclipse.net4j.internal.util.om.trace.ContextTracer;
import org.eclipse.net4j.signal.Indication;
import org.eclipse.net4j.util.io.ExtendedDataInputStream;

import org.eclipse.emf.internal.cdo.CDOSessionImpl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class InvalidationIndication extends Indication
{
  private static final ContextTracer PROTOCOL = new ContextTracer(CDOProtocol.DEBUG_PROTOCOL,
      InvalidationIndication.class);

  public InvalidationIndication()
  {
  }

  @Override
  protected short getSignalID()
  {
    return CDOProtocolConstants.INVALIDATION_SIGNAL;
  }

  @Override
  protected void indicating(ExtendedDataInputStream in) throws IOException
  {
    long timeStamp = in.readLong();
    if (PROTOCOL.isEnabled())
    {
      PROTOCOL.format("Read timeStamp: {0,date} {0,time}", timeStamp);
    }

    int size = in.readInt();
    if (PROTOCOL.isEnabled())
    {
      PROTOCOL.format("Reading {0} IDs", size);
    }

    Set<CDOID> dirtyOIDs = new HashSet();
    for (int i = 0; i < size; i++)
    {
      CDOID dirtyOID = CDOIDImpl.read(in);
      dirtyOIDs.add(dirtyOID);
    }

    getSession().notifyInvalidation(timeStamp, dirtyOIDs, null);
  }

  private CDOSessionImpl getSession()
  {
    return ((CDOClientProtocol)getProtocol()).getSession();
  }
}
