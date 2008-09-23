/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.internal.cdo.protocol;

import org.eclipse.emf.cdo.common.CDODataInput;
import org.eclipse.emf.cdo.common.CDODataOutput;
import org.eclipse.emf.cdo.common.CDOProtocolConstants;
import org.eclipse.emf.cdo.common.id.CDOID;

import org.eclipse.emf.internal.cdo.bundle.OM;

import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.util.om.trace.ContextTracer;

import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class ResourcePathRequest extends CDOClientRequest<String>
{
  private static final ContextTracer PROTOCOL_TRACER = new ContextTracer(OM.DEBUG_PROTOCOL, ResourcePathRequest.class);

  private int viewID;

  private CDOID id;

  public ResourcePathRequest(IChannel channel, int viewID, CDOID id)
  {
    super(channel);
    this.viewID = viewID;
    this.id = id;
  }

  @Override
  protected short getSignalID()
  {
    return CDOProtocolConstants.SIGNAL_RESOURCE_PATH;
  }

  @Override
  protected void requesting(CDODataOutput out) throws IOException
  {
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Writing viewID: {0}", viewID);
    }

    out.writeInt(viewID);
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Writing ID: {0}", id);
    }

    out.writeCDOID(id);
  }

  @Override
  protected String confirming(CDODataInput in) throws IOException
  {
    String path = in.readString();
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Read path: {0}", path);
    }

    return path;
  }
}
