/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 * 		Simon McDuff - maintenance
 **************************************************************************/
package org.eclipse.emf.cdo;

import org.eclipse.emf.cdo.common.model.CDOFeature;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.common.revision.CDORevisionResolver;

/**
 * @author Eike Stepper
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface CDORevisionManager extends CDORevisionResolver
{
  public CDOSession getSession();

  /**
   * @param revision
   * @param feature
   * @param accessIndex
   *          Index of the item access at the client (with modifications)
   * @param fetchIndex
   *          Index of the item access at the server (without any modifications)
   * @param fromIndex
   *          Load objects at the client from fromIndex (inclusive)
   * @param toIndex
   *          Load objects at the client to toIndex (inclusive)
   * @since 2.0
   */
  public Object loadChunkByRange(CDORevision revision, CDOFeature feature, int accessIndex, int fetchIndex,
      int fromIndex, int toIndex);
}
