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
package org.eclipse.emf.cdo;

import org.eclipse.emf.cdo.common.model.CDOFeature;
import org.eclipse.emf.cdo.common.revision.CDORevision;

/**
 * @author Simon McDuff
 * @since 2.0
 */
public interface CDOCollectionLoadingPolicy
{
  public static final CDOCollectionLoadingPolicy DEFAULT = new CDOCollectionLoadingPolicy()
  {
    public int getInitialChunkSize()
    {
      return CDORevision.UNCHUNKED;
    }

    public Object resolveProxy(CDORevisionManager revisionManager, CDORevision revision, CDOFeature feature,
        int accessIndex, int serverIndex)
    {
      return revisionManager.loadChunkByRange(revision, feature, accessIndex, serverIndex, accessIndex, accessIndex);
    }
  };

  /**
   * Returns the maximum number of CDOIDs to be loaded for collections when an object is loaded.
   */
  public int getInitialChunkSize();

  /**
   * Defines a strategy to be used when the collection needs to resolve one element.
   * {@link CDORevisionManager#loadChunkByRange(CDORevision, CDOFeature, int, int, int, int)} should be used to resolve
   * them.
   */
  public Object resolveProxy(CDORevisionManager revisionManager, CDORevision revision, CDOFeature feature,
      int accessIndex, int serverIndex);
}
