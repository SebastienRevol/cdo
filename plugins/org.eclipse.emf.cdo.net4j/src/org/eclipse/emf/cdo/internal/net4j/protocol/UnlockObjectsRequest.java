/*
 * Copyright (c) 2009-2012, 2016, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Simon McDuff - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.net4j.protocol;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.lock.CDOLockState;
import org.eclipse.emf.cdo.common.protocol.CDODataInput;
import org.eclipse.emf.cdo.common.protocol.CDODataOutput;
import org.eclipse.emf.cdo.common.protocol.CDOProtocolConstants;

import org.eclipse.net4j.util.concurrent.IRWLockManager.LockType;

import org.eclipse.emf.spi.cdo.CDOSessionProtocol.UnlockObjectsResult;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Simon McDuff
 */
public class UnlockObjectsRequest extends CDOClientRequest<UnlockObjectsResult>
{
  private int viewID;

  private Collection<CDOID> objectIDs;

  private LockType lockType;

  private boolean recursive;

  public UnlockObjectsRequest(CDOClientProtocol protocol, int viewID, Collection<CDOID> objects, LockType lockType, boolean recursive)
  {
    this(protocol, CDOProtocolConstants.SIGNAL_UNLOCK_OBJECTS, viewID, objects, lockType, recursive);
  }

  protected UnlockObjectsRequest(CDOClientProtocol protocol, short signalID, int viewID, Collection<CDOID> objectIDs, LockType lockType, boolean recursive)
  {
    super(protocol, signalID);
    this.viewID = viewID;
    this.objectIDs = objectIDs;
    this.lockType = lockType;
    this.recursive = recursive;
  }

  @Override
  protected void requesting(CDODataOutput out) throws IOException
  {
    out.writeXInt(viewID);
    out.writeCDOLockType(lockType);
    out.writeBoolean(recursive);

    if (objectIDs == null)
    {
      out.writeXInt(CDOProtocolConstants.RELEASE_ALL_LOCKS);
    }
    else
    {
      out.writeXInt(objectIDs.size());

      for (CDOID id : objectIDs)
      {
        out.writeCDOID(id);
      }
    }
  }

  @Override
  protected UnlockObjectsResult confirming(CDODataInput in) throws IOException
  {
    long timestamp = in.readXLong();
    int n = in.readXInt();
    CDOLockState[] newLockStates = new CDOLockState[n];

    for (int i = 0; i < n; i++)
    {
      newLockStates[i] = in.readCDOLockState();
    }

    return new UnlockObjectsResult(newLockStates, timestamp);
  }
}
