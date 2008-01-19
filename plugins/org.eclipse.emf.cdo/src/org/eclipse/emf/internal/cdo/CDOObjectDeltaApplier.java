/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Eike Stepper - maintenance
 **************************************************************************/
package org.eclipse.emf.internal.cdo;

import org.eclipse.emf.cdo.CDOState;
import org.eclipse.emf.cdo.internal.protocol.revision.CDORevisionImpl;
import org.eclipse.emf.cdo.internal.protocol.revision.delta.CDORevisionDeltaApplier;
import org.eclipse.emf.cdo.protocol.revision.delta.CDORevisionDelta;

/**
 * @author Simon McDuff
 */
public class CDOObjectDeltaApplier extends CDORevisionDeltaApplier
{
  public CDOObjectDeltaApplier()
  {
  }

  public void apply(InternalCDOObject object, CDORevisionDelta delta)
  {
    CDORevisionImpl revision = new CDORevisionImpl((CDORevisionImpl)object.cdoRevision());
    revision.increaseVersion();
    object.cdoInternalSetRevision(revision);
    object.cdoInternalSetState(CDOState.DIRTY);
    apply(revision, delta);
  }
}
