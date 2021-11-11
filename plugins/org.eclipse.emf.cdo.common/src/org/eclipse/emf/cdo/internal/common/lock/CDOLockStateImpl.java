/*
 * Copyright (c) 2011-2013, 2015, 2016, 2019, 2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Caspar De Groot - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.common.lock;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.emf.cdo.common.lock.CDOLockOwner;
import org.eclipse.emf.cdo.common.lock.CDOLockState;
import org.eclipse.emf.cdo.common.lock.CDOLockUtil;
import org.eclipse.emf.cdo.common.revision.CDOIDAndBranch;
import org.eclipse.emf.cdo.spi.common.lock.InternalCDOLockState;

import org.eclipse.net4j.util.CheckUtil;
import org.eclipse.net4j.util.concurrent.IRWLockManager.LockType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Caspar De Groot
 */
public class CDOLockStateImpl implements InternalCDOLockState
{
  private static final Set<CDOLockOwner> NO_LOCK_OWNERS = Collections.emptySet();

  private final Object lockedObject;

  private Set<CDOLockOwner> readLockOwners;

  private CDOLockOwner writeLockOwner;

  private CDOLockOwner writeOptionOwner;

  public CDOLockStateImpl(Object lockedObject)
  {
    assert lockedObject instanceof CDOID || lockedObject instanceof CDOIDAndBranch : "lockedObject is of wrong type";
    assert !CDOIDUtil.isNull(CDOLockUtil.getLockedObjectID(lockedObject)) : "lockedObject is null";
    CheckUtil.checkArg(lockedObject, "lockedObject");
    this.lockedObject = lockedObject;
  }

  public CDOLockStateImpl copy()
  {
    return copy(lockedObject);
  }

  public CDOLockStateImpl copy(Object lockedObject)
  {
    CDOLockStateImpl newLockState = new CDOLockStateImpl(lockedObject);

    if (readLockOwners != null)
    {
      for (CDOLockOwner owner : readLockOwners)
      {
        newLockState.addReadLockOwner(owner);
      }
    }

    newLockState.writeLockOwner = writeLockOwner;
    newLockState.writeOptionOwner = writeOptionOwner;
    return newLockState;
  }

  @Override
  @Deprecated
  public void updateFrom(Object object, CDOLockState source)
  {
    updateFrom(source);
  }

  @Override
  public void updateFrom(CDOLockState source)
  {
    Set<CDOLockOwner> owners = source.getReadLockOwners();
    if (owners.isEmpty())
    {
      readLockOwners = null;
    }
    else
    {
      readLockOwners = new HashSet<>(owners);
    }

    writeLockOwner = source.getWriteLockOwner();
    writeOptionOwner = source.getWriteOptionOwner();
  }

  @Override
  public boolean isLocked(LockType lockType, CDOLockOwner lockOwner, boolean others)
  {
    if (lockedObject == null)
    {
      return false;
    }

    if (lockType == null)
    {
      return isReadLocked(lockOwner, others) || isWriteLocked(lockOwner, others) || isOptionLocked(lockOwner, others);
    }

    switch (lockType)
    {
    case READ:
      return isReadLocked(lockOwner, others);

    case WRITE:
      return isWriteLocked(lockOwner, others);

    case OPTION:
      return isOptionLocked(lockOwner, others);
    }

    return false;
  }

  private boolean isReadLocked(CDOLockOwner by, boolean others)
  {
    if (readLockOwners == null)
    {
      return false;
    }

    int n = readLockOwners.size();
    if (n == 0)
    {
      return false;
    }

    boolean contained = readLockOwners.contains(by);

    if (others)
    {
      int ownCount = contained ? 1 : 0;
      return n > ownCount;
    }

    return contained;
  }

  private boolean isWriteLocked(CDOLockOwner by, boolean others)
  {
    if (writeLockOwner == null)
    {
      return false;
    }

    return writeLockOwner == by ^ others;
  }

  private boolean isOptionLocked(CDOLockOwner by, boolean others)
  {
    if (writeOptionOwner == null)
    {
      return false;
    }

    return writeOptionOwner == by ^ others;
  }

  @Override
  public Set<CDOLockOwner> getReadLockOwners()
  {
    if (readLockOwners == null)
    {
      return NO_LOCK_OWNERS;
    }

    return Collections.unmodifiableSet(readLockOwners);
  }

  @Override
  public void addReadLockOwner(CDOLockOwner lockOwner)
  {
    if (readLockOwners == null)
    {
      readLockOwners = new HashSet<>();
    }

    readLockOwners.add(lockOwner);
  }

  @Override
  public boolean removeReadLockOwner(CDOLockOwner lockOwner)
  {
    if (readLockOwners == null)
    {
      return false;
    }

    boolean changed = readLockOwners.remove(lockOwner);
    if (changed && readLockOwners.isEmpty())
    {
      readLockOwners = null;
    }

    return changed;
  }

  @Override
  public CDOLockOwner getWriteLockOwner()
  {
    return writeLockOwner;
  }

  @Override
  public void setWriteLockOwner(CDOLockOwner lockOwner)
  {
    writeLockOwner = lockOwner;
  }

  @Override
  public CDOLockOwner getWriteOptionOwner()
  {
    return writeOptionOwner;
  }

  @Override
  public void setWriteOptionOwner(CDOLockOwner lockOwner)
  {
    writeOptionOwner = lockOwner;
  }

  @Override
  public boolean removeOwner(CDOLockOwner lockOwner)
  {
    boolean changed = removeReadLockOwner(lockOwner);

    if (writeLockOwner == lockOwner)
    {
      writeLockOwner = null;
      changed = true;
    }

    if (writeOptionOwner == lockOwner)
    {
      writeOptionOwner = null;
      changed = true;
    }

    return changed;
  }

  @Override
  public boolean remapOwner(CDOLockOwner oldLockOwner, CDOLockOwner newLockOwner)
  {
    boolean changed = false;

    if (readLockOwners != null && readLockOwners.remove(oldLockOwner))
    {
      readLockOwners.add(newLockOwner);
      changed = true;
    }

    if (writeLockOwner == oldLockOwner)
    {
      writeLockOwner = newLockOwner;
      changed = true;
    }

    if (writeOptionOwner == oldLockOwner)
    {
      writeOptionOwner = newLockOwner;
      changed = true;
    }

    return changed;
  }

  @Override
  public Object getLockedObject()
  {
    return lockedObject;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (lockedObject == null ? 0 : lockedObject.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (!(obj instanceof CDOLockStateImpl))
    {
      return false;
    }

    CDOLockStateImpl other = (CDOLockStateImpl)obj;
    if (lockedObject == null)
    {
      if (other.lockedObject != null)
      {
        return false;
      }
    }
    else if (!lockedObject.equals(other.lockedObject))
    {
      return false;
    }

    if (writeLockOwner != other.writeLockOwner)
    {
      return false;
    }

    if (writeOptionOwner != other.writeOptionOwner)
    {
      return false;
    }

    if (!Objects.equals(readLockOwners, other.readLockOwners))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder("CDOLockState[lockedObject=");
    builder.append(lockedObject);

    builder.append(", readLockOwners=");
    if (readLockOwners != null && readLockOwners.size() > 0)
    {
      boolean first = true;
      for (CDOLockOwner lockOwner : readLockOwners)
      {
        if (first)
        {
          first = false;
        }
        else
        {
          builder.append(", ");
        }

        builder.append(lockOwner);
      }

      builder.deleteCharAt(builder.length() - 1);
    }
    else
    {
      builder.append("NONE");
    }

    builder.append(", writeLockOwner=");
    builder.append(writeLockOwner != null ? writeLockOwner : "NONE");

    builder.append(", writeOptionOwner=");
    builder.append(writeOptionOwner != null ? writeOptionOwner : "NONE");

    builder.append("]");
    return builder.toString();
  }

  @Override
  public void dispose()
  {
    readLockOwners = null;
    writeLockOwner = null;
    writeOptionOwner = null;
  }
}
