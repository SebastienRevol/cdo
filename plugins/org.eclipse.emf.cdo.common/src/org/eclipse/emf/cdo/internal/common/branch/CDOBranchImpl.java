/*
 * Copyright (c) 2010-2016, 2019, 2021 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Erdal Karaca - bug 414270: check if repository supports branching when querying sub branches
 */
package org.eclipse.emf.cdo.internal.common.branch;

import org.eclipse.emf.cdo.common.CDOCommonRepository;
import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.branch.CDOBranchChangedEvent.ChangeKind;
import org.eclipse.emf.cdo.common.branch.CDOBranchCreationContext;
import org.eclipse.emf.cdo.common.branch.CDOBranchPoint;
import org.eclipse.emf.cdo.common.branch.CDOBranchVersion;
import org.eclipse.emf.cdo.common.branch.CDODuplicateBranchException;
import org.eclipse.emf.cdo.spi.common.branch.InternalCDOBranch;
import org.eclipse.emf.cdo.spi.common.branch.InternalCDOBranchManager;
import org.eclipse.emf.cdo.spi.common.branch.InternalCDOBranchManager.BranchLoader;
import org.eclipse.emf.cdo.spi.common.branch.InternalCDOBranchManager.BranchLoader.BranchInfo;
import org.eclipse.emf.cdo.spi.common.branch.InternalCDOBranchManager.BranchLoader.SubBranchInfo;
import org.eclipse.emf.cdo.spi.common.branch.InternalCDOBranchManager.BranchLoader3;

import org.eclipse.net4j.util.AdapterUtil;
import org.eclipse.net4j.util.ObjectUtil;
import org.eclipse.net4j.util.container.Container;
import org.eclipse.net4j.util.event.Event;
import org.eclipse.net4j.util.io.RemoteException;
import org.eclipse.net4j.util.om.monitor.Monitor;
import org.eclipse.net4j.util.om.monitor.OMMonitor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class CDOBranchImpl extends Container<CDOBranch> implements InternalCDOBranch
{
  private static final InternalCDOBranch[] NO_BRANCHES = new InternalCDOBranch[0];

  private final InternalCDOBranchManager branchManager;

  private final CDOBranchPoint head = getPoint(CDOBranchPoint.UNSPECIFIED_DATE);

  private int id;

  private String name;

  private CDOBranchPoint base;

  private InternalCDOBranch[] branches;

  private boolean deleted;

  public CDOBranchImpl(InternalCDOBranchManager branchManager, int id, String name, CDOBranchPoint base)
  {
    this.branchManager = branchManager;
    this.id = id;
    this.name = name;
    this.base = base;
    activate();
  }

  @Override
  public boolean isMainBranch()
  {
    return false;
  }

  @Override
  public boolean isLocal()
  {
    return id < 0;
  }

  @Override
  public boolean isDeleted()
  {
    return deleted;
  }

  @Override
  public synchronized void setDeleted()
  {
    if (base != null)
    {
      InternalCDOBranch branch = (InternalCDOBranch)base.getBranch();
      if (branch != null)
      {
        branch.removeChild(this);
      }
    }

    name = null;
    base = null;
    branches = null;
    deleted = true;
  }

  @Override
  public InternalCDOBranchManager getBranchManager()
  {
    return branchManager;
  }

  @Override
  public int getID()
  {
    return id;
  }

  @Override
  public synchronized String getName()
  {
    if (name == null)
    {
      load();
    }

    return name;
  }

  @Override
  public void setName(String name) throws CDODuplicateBranchException
  {
    checkActive();

    CDOBranchManagerImpl.checkBranchName(name);

    BranchLoader branchLoader = branchManager.getBranchLoader();
    if (!(branchLoader instanceof BranchLoader3))
    {
      throw new UnsupportedOperationException("Renaming branches is not supported by " + branchLoader);
    }

    String oldName = getName();
    if (!ObjectUtil.equals(name, oldName))
    {
      try
      {
        ((BranchLoader3)branchLoader).renameBranch(id, oldName, name);
      }
      catch (RemoteException ex)
      {
        throw ex.unwrap();
      }

      basicSetName(name);
    }
  }

  @Override
  public void basicSetName(String name)
  {
    this.name = name;
    branchManager.handleBranchChanged(this, ChangeKind.RENAMED, id);
  }

  @Override
  public CDOBranch getBranch()
  {
    return deleted ? null : base.getBranch();
  }

  @Override
  public long getTimeStamp()
  {
    return deleted ? CDOBranchPoint.INVALID_DATE : base.getTimeStamp();
  }

  @Override
  public boolean isProxy()
  {
    return name == null || base == null;
  }

  @Override
  public String getPathName()
  {
    if (deleted)
    {
      return null;
    }

    StringBuilder builder = new StringBuilder();
    computePathName(this, builder);
    return builder.toString();
  }

  private void computePathName(CDOBranch branch, StringBuilder builder)
  {
    CDOBranchPoint base = branch.getBase();
    CDOBranch parent = base.getBranch();
    if (parent != null)
    {
      computePathName(parent, builder);
      builder.append(PATH_SEPARATOR);
    }

    builder.append(branch.getName());
  }

  @Override
  public CDOBranchPoint[] getBasePath()
  {
    List<CDOBranchPoint> path = new ArrayList<>();
    computeBasePath(this, path);
    return path.toArray(new CDOBranchPoint[path.size()]);
  }

  private void computeBasePath(CDOBranch branch, List<CDOBranchPoint> path)
  {
    CDOBranchPoint base = branch.getBase();
    CDOBranch parent = base.getBranch();
    if (parent != null)
    {
      computeBasePath(parent, path);
    }

    path.add(base);
  }

  @Override
  public synchronized CDOBranchPoint getBase()
  {
    if (base == null)
    {
      load();
    }

    return base;
  }

  @Override
  public CDOBranchPoint getHead()
  {
    return head;
  }

  @Override
  public CDOBranchPoint getPoint(long timeStamp)
  {
    return new CDOBranchPointImpl(this, timeStamp);
  }

  @Override
  public CDOBranchVersion getVersion(int version)
  {
    return new CDOBranchVersionImpl(this, version);
  }

  @Override
  public InternalCDOBranch createBranch(String name, long timeStamp) throws CDODuplicateBranchException
  {
    return branchManager.createBranch(BranchLoader.NEW_BRANCH, name, this, timeStamp);
  }

  @Override
  public InternalCDOBranch createBranch(String name) throws CDODuplicateBranchException
  {
    return createBranch(name, CDOBranchPoint.UNSPECIFIED_DATE);
  }

  @Override
  public CDOBranch[] delete(OMMonitor monitor)
  {
    checkActive();

    if (monitor == null)
    {
      monitor = new Monitor();
    }

    return branchManager.deleteBranches(id, monitor);
  }

  @Override
  public void fireDeletedEvent()
  {
    fireEvent(new BranchDeletedEventImpl(this));
  }

  @Override
  public InternalCDOBranch[] getElements()
  {
    return getBranches();
  }

  @Override
  public InternalCDOBranch[] getBranches(boolean loadOnDemand)
  {
    if (!branchManager.getRepository().isSupportingBranches())
    {
      return NO_BRANCHES;
    }

    if (branches == null && loadOnDemand)
    {
      loadBranches();
    }

    return branches;
  }

  @Override
  public synchronized InternalCDOBranch[] getBranches()
  {
    return getBranches(true);
  }

  @Override
  public InternalCDOBranch getBranch(String path)
  {
    if (!branchManager.getRepository().isSupportingBranches())
    {
      return null;
    }

    while (path.startsWith(PATH_SEPARATOR))
    {
      path = path.substring(1);
    }

    while (path.endsWith(PATH_SEPARATOR))
    {
      path = path.substring(0, path.length() - PATH_SEPARATOR.length());
    }

    int sep = path.indexOf(PATH_SEPARATOR);
    if (sep == -1)
    {
      return getChild(path);
    }

    String name = path.substring(0, sep);
    InternalCDOBranch child = getChild(name);
    if (child == null)
    {
      return null;
    }

    // Recurse
    String rest = path.substring(sep + 1);
    return child.getBranch(rest);
  }

  @Override
  @Deprecated
  public void rename(String newName)
  {
    setName(newName);
  }

  private InternalCDOBranch getChild(String name)
  {
    InternalCDOBranch[] branches = getBranches();
    for (InternalCDOBranch branch : branches)
    {
      if (name.equals(branch.getName()))
      {
        return branch;
      }
    }

    return null;
  }

  @Override
  public BranchInfo getBranchInfo()
  {
    CDOBranchPoint base = getBase();
    return new BranchInfo(getName(), base.getBranch().getID(), base.getTimeStamp());
  }

  @Override
  public void setBranchInfo(String name, InternalCDOBranch baseBranch, long baseTimeStamp)
  {
    this.name = name;
    base = baseBranch.getPoint(baseTimeStamp);
  }

  @Override
  public void addChild(InternalCDOBranch branch)
  {
    synchronized (this)
    {
      if (branches == null)
      {
        loadBranches();
      }
      else
      {
        InternalCDOBranch[] newBranches = new InternalCDOBranch[branches.length + 1];
        System.arraycopy(branches, 0, newBranches, 0, branches.length);
        newBranches[branches.length] = branch;
        branches = newBranches;
      }
    }

    fireElementAddedEvent(branch);
  }

  @Override
  public void removeChild(InternalCDOBranch branch)
  {
    boolean changed = true;

    synchronized (this)
    {
      if (branches == null)
      {
        loadBranches();
      }
      else
      {
        List<InternalCDOBranch> list = new ArrayList<>(Arrays.asList(branches));
        if (list.remove(branch))
        {
          branches = list.toArray(new InternalCDOBranch[list.size()]);
        }
        else
        {
          changed = false;
        }
      }
    }

    if (changed)
    {
      fireElementRemovedEvent(branch);
    }
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Object getAdapter(Class adapter)
  {
    if (adapter == CDOBranchCreationContext.class)
    {
      CDOCommonRepository repository = branchManager.getRepository();
      if (repository.isSupportingBranches())
      {
        return new CDOBranchCreationContext()
        {
          @Override
          public CDOBranchPoint getBase()
          {
            return getHead();
          }
        };
      }
    }

    return AdapterUtil.adapt(this, adapter, false);
  }

  @Override
  public int compareTo(CDOBranch o)
  {
    int otherID = o.getID();
    return id < otherID ? -1 : id == otherID ? 0 : 1;
  }

  @Override
  public int hashCode()
  {
    return id;
  }

  @Override
  public boolean equals(Object obj)
  {
    return obj == this;
  }

  @Override
  public String toString()
  {
    if (isProxy())
    {
      if (deleted)
      {
        return MessageFormat.format("Branch[id={0}, DELETED]", id); //$NON-NLS-1$
      }

      return MessageFormat.format("Branch[id={0}, PROXY]", id); //$NON-NLS-1$
    }

    return MessageFormat.format("Branch[id={0}, name={1}]", id, name); //$NON-NLS-1$
  }

  private synchronized void load()
  {
    if (deleted)
    {
      return;
    }

    BranchInfo branchInfo = branchManager.getBranchLoader().loadBranch(id);
    CDOBranch baseBranch = branchManager.getBranch(branchInfo.getBaseBranchID());

    name = branchInfo.getName();
    base = baseBranch.getPoint(branchInfo.getBaseTimeStamp());
  }

  private synchronized void loadBranches()
  {
    if (deleted)
    {
      return;
    }

    SubBranchInfo[] infos = branchManager.getBranchLoader().loadSubBranches(id);
    branches = new InternalCDOBranch[infos.length];
    for (int i = 0; i < infos.length; i++)
    {
      SubBranchInfo info = infos[i];
      branches[i] = branchManager.getBranch(info.getID(), info.getName(), this, info.getBaseTimeStamp());
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Main extends CDOBranchImpl
  {
    private boolean local;

    public Main(InternalCDOBranchManager branchManager, boolean local, long timeStamp)
    {
      super(branchManager, MAIN_BRANCH_ID, MAIN_BRANCH_NAME, new CDOBranchPointImpl(null, timeStamp));
      this.local = local;
    }

    @Override
    public boolean isMainBranch()
    {
      return true;
    }

    @Override
    public boolean isLocal()
    {
      return local;
    }

    @Override
    public void setName(String name)
    {
      throw new IllegalArgumentException("Renaming the MAIN branch is not supported");
    }

    @Override
    public CDOBranch[] delete(OMMonitor monitor)
    {
      throw new IllegalArgumentException("Deleting the MAIN branch is not supported");
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class BranchDeletedEventImpl extends Event implements BranchDeletedEvent
  {
    private static final long serialVersionUID = 1L;

    public BranchDeletedEventImpl(CDOBranch branch)
    {
      super(branch);
    }

    @Override
    public CDOBranch getSource()
    {
      return (CDOBranch)super.getSource();
    }

    @Override
    public CDOBranch getBranch()
    {
      return getSource();
    }
  }
}
