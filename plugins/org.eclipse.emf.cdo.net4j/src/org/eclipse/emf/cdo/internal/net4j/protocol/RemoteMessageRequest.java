/*
 * Copyright (c) 2009-2012, 2016, 2017, 2019, 2021 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.net4j.protocol;

import org.eclipse.emf.cdo.common.protocol.CDODataInput;
import org.eclipse.emf.cdo.common.protocol.CDODataOutput;
import org.eclipse.emf.cdo.common.protocol.CDOProtocolConstants;
import org.eclipse.emf.cdo.session.remote.CDORemoteSession;
import org.eclipse.emf.cdo.session.remote.CDORemoteSessionMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class RemoteMessageRequest extends CDOClientRequest<Set<Integer>>
{
  private CDORemoteSessionMessage message;

  private List<CDORemoteSession> recipients;

  public RemoteMessageRequest(CDOClientProtocol protocol, CDORemoteSessionMessage message, List<CDORemoteSession> recipients)
  {
    super(protocol, CDOProtocolConstants.SIGNAL_REMOTE_MESSAGE);
    this.message = message;
    this.recipients = recipients;
  }

  @Override
  protected void requesting(CDODataOutput out) throws IOException
  {
    message.write(out);
    out.writeXInt(recipients.size());
    for (CDORemoteSession recipient : recipients)
    {
      out.writeXInt(recipient.getSessionID());
    }
  }

  @Override
  protected Set<Integer> confirming(CDODataInput in) throws IOException
  {
    Set<Integer> sessionIDs = new HashSet<>();
    int count = in.readXInt();
    for (int i = 0; i < count; i++)
    {
      int sessionID = in.readXInt();
      sessionIDs.add(sessionID);
    }

    return sessionIDs;
  }
}
