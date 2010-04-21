/**
 * Copyright (c) 2004 - 2010 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Eike Stepper - maintenance
 */
package org.eclipse.emf.internal.cdo;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.common.revision.delta.CDOAddFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDOClearFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDOContainerFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDOFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDOFeatureDeltaVisitor;
import org.eclipse.emf.cdo.common.revision.delta.CDOListFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDOMoveFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDORemoveFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDORevisionDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDOSetFeatureDelta;
import org.eclipse.emf.cdo.common.revision.delta.CDOUnsetFeatureDelta;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevision;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevisionManager;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import java.util.Set;

/**
 * @author Simon McDuff
 * @since 2.0
 */
public class CDONotificationBuilder implements CDOFeatureDeltaVisitor
{
  private CDOView view;

  private InternalEObject object;

  private CDORevisionDelta revisionDelta;

  private CDODeltaNotificationImpl notification;

  private InternalCDORevision revision;

  private boolean revisionLookedUp;

  private Set<CDOObject> detachedObjects;

  private InternalCDORevision oldRevision;

  /**
   * @since 3.0
   */
  public CDONotificationBuilder(CDOView view)
  {
    this.view = view;
  }

  /**
   * @since 3.0
   */
  public CDOView getView()
  {
    return view;
  }

  /**
   * @since 3.0
   */
  public synchronized NotificationImpl buildNotification(InternalEObject object, InternalCDORevision oldRevision,
      CDORevisionDelta revisionDelta, Set<CDOObject> detachedObjects)
  {
    notification = null;
    revision = null;
    revisionLookedUp = false;

    this.object = object;
    this.revisionDelta = revisionDelta;
    this.detachedObjects = detachedObjects;
    this.oldRevision = oldRevision;
    revisionDelta.accept(this);
    return notification;
  }

  public void visit(CDOMoveFeatureDelta delta)
  {
    EStructuralFeature feature = delta.getFeature();
    int oldPosition = delta.getOldPosition();
    int newPosition = delta.getNewPosition();
    add(new CDODeltaNotificationImpl(object, NotificationImpl.MOVE, getEFeatureID(feature), Integer
        .valueOf(oldPosition), getOldValue(feature), newPosition));
  }

  public void visit(CDOAddFeatureDelta delta)
  {
    EStructuralFeature feature = delta.getFeature();
    add(new CDODeltaNotificationImpl(object, NotificationImpl.ADD, getEFeatureID(feature), getOldValue(feature), delta
        .getValue(), delta.getIndex()));
  }

  public void visit(CDORemoveFeatureDelta delta)
  {
    EStructuralFeature feature = delta.getFeature();
    int index = delta.getIndex();
    if (!revisionLookedUp)
    {
      InternalCDORevisionManager revisionManager = (InternalCDORevisionManager)view.getSession().getRevisionManager();
      revision = revisionManager.getRevisionByVersion(revisionDelta.getID(), revisionDelta, CDORevision.UNCHUNKED,
          false);
    }

    Object oldValue = revision == null ? null : revision.get(feature, index);
    if (oldValue instanceof CDOID)
    {
      CDOID id = (CDOID)oldValue;
      oldValue = view.getObject(id, false);
      if (oldValue == null)
      {
        for (CDOObject detachedObject : detachedObjects)
        {
          if (detachedObject.cdoID().equals(id))
          {
            oldValue = detachedObject;
            break;
          }
        }
      }
    }

    add(new CDODeltaNotificationImpl(object, NotificationImpl.REMOVE, getEFeatureID(feature), oldValue, null, index));
  }

  public void visit(CDOSetFeatureDelta delta)
  {
    EStructuralFeature feature = delta.getFeature();
    add(new CDODeltaNotificationImpl(object, NotificationImpl.SET, getEFeatureID(feature), getOldValue(feature), delta
        .getValue()));
  }

  public void visit(CDOUnsetFeatureDelta delta)
  {
    EStructuralFeature feature = delta.getFeature();
    add(new CDODeltaNotificationImpl(object, NotificationImpl.UNSET, getEFeatureID(feature), getOldValue(feature), null));
  }

  public void visit(CDOListFeatureDelta deltas)
  {
    for (CDOFeatureDelta delta : deltas.getListChanges())
    {
      delta.accept(this);
    }
  }

  public void visit(CDOClearFeatureDelta delta)
  {
    EStructuralFeature feature = delta.getFeature();
    add(new CDODeltaNotificationImpl(object, NotificationImpl.REMOVE_MANY, getEFeatureID(feature),
        getOldValue(feature), null));
  }

  public void visit(CDOContainerFeatureDelta delta)
  {
    Object oldValue = null;
    if (oldRevision != null)
    {
      oldValue = oldRevision.getContainerID();
    }

    add(new CDODeltaNotificationImpl(object, NotificationImpl.SET, EcorePackage.eINSTANCE.eContainmentFeature(),
        oldValue, delta.getContainerID()));
  }

  protected void add(CDODeltaNotificationImpl newNotificaton)
  {
    newNotificaton.setRevisionDelta(revisionDelta);
    if (notification == null)
    {
      notification = newNotificaton;
    }
    else
    {
      notification.add(newNotificaton);
    }
  }

  private int getEFeatureID(EStructuralFeature eFeature)
  {
    return object.eClass().getFeatureID(eFeature);
  }

  private Object getOldValue(EStructuralFeature feature)
  {
    if (oldRevision == null)
    {
      return null;
    }

    return oldRevision.getValue(feature);
  }
}
