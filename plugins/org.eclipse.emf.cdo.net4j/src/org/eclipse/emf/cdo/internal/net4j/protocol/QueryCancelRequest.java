/*
 * Copyright (c) 2009-2012, 2015, 2017, 2021 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Eike Stepper - maintenance
 */
package org.eclipse.emf.cdo.internal.net4j.protocol;

import org.eclipse.emf.cdo.common.protocol.CDODataInput;
import org.eclipse.emf.cdo.common.protocol.CDODataOutput;
import org.eclipse.emf.cdo.common.protocol.CDOProtocolConstants;

import java.io.IOException;

/**
 * @author Simon McDuff
 */
public class QueryCancelRequest extends CDOClientRequest<Boolean>
{
  private int queryID;

  public QueryCancelRequest(CDOClientProtocol protocol, int queryID)
  {
    super(protocol, CDOProtocolConstants.SIGNAL_QUERY_CANCEL);
    this.queryID = queryID;
  }

  @Override
  protected void requesting(CDODataOutput out) throws IOException
  {
    out.writeXInt(queryID);
  }

  @Override
  protected Boolean confirming(CDODataInput in) throws IOException
  {
    boolean exception = in.readBoolean();
    if (exception)
    {
      String message = in.readString();
      throw new RuntimeException(message);
    }

    return true;
  }
}
