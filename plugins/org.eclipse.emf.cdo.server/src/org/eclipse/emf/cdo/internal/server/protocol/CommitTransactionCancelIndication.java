/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Simon McDuff - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.internal.server.protocol;

import org.eclipse.emf.cdo.common.CDODataInput;
import org.eclipse.emf.cdo.common.CDODataOutput;
import org.eclipse.emf.cdo.common.CDOProtocolConstants;

import org.eclipse.net4j.util.om.monitor.IMonitor;

/**
 * @author Simon McDuff
 */
public class CommitTransactionCancelIndication extends CommitTransactionIndication
{
  public CommitTransactionCancelIndication(CDOServerProtocol protocol)
  {
    super(protocol, CDOProtocolConstants.SIGNAL_COMMIT_TRANSACTION_CANCEL);
  }

  @Override
  protected void indicating(CDODataInput in, IMonitor monitor) throws Exception
  {
    indicationTransaction(in);
  }

  @Override
  protected void responding(CDODataOutput out, IMonitor monitor) throws Exception
  {
    String exceptionMessage = null;
    try
    {
      if (commitContext != null)
      {
        getRepository().getCommitManager().rollback(commitContext);
      }
    }
    catch (Exception exception)
    {
      exceptionMessage = exception.getMessage();
    }

    if (commitContext != null && exceptionMessage == null)
    {
      exceptionMessage = commitContext.getRollbackMessage();
    }

    respondingException(out, exceptionMessage);
  }

  @Override
  protected void indicationTransaction(CDODataInput in) throws Exception
  {
    int viewID = in.readInt();
    commitContext = getRepository().getCommitManager().get(getTransaction(viewID));
  }
}
