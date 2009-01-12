/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.ui.ide;

import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.session.CDOPackageRegistry;
import org.eclipse.emf.cdo.team.IRepositoryProject;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.emf.ecore.EPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class Node
{
  private static final Object[] EMPTY = {};

  private IRepositoryProject repositoryProject;

  public Node(IRepositoryProject repositoryProject)
  {
    this.repositoryProject = repositoryProject;
  }

  public IRepositoryProject getRepositoryProject()
  {
    return repositoryProject;
  }

  public abstract String getText();

  public abstract String getImageKey();

  public Object[] getChildren()
  {
    return EMPTY;
  }

  public abstract Type getType();

  /**
   * @author Eike Stepper
   */
  public static enum Type
  {
    SESSIONS, PACKAGES, RESOURCES
  }

  /**
   * @author Eike Stepper
   */
  public static final class SessionsNode extends Node
  {
    public SessionsNode(IRepositoryProject repositoryProject)
    {
      super(repositoryProject);
    }

    @Override
    public Type getType()
    {
      return Type.SESSIONS;
    }

    @Override
    public String getText()
    {
      return "Sessions";
    }

    @Override
    public String getImageKey()
    {
      return "icons/full/obj16/Sessions.gif";
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class PackagesNode extends Node
  {
    public PackagesNode(IRepositoryProject repositoryProject)
    {
      super(repositoryProject);
    }

    @Override
    public Type getType()
    {
      return Type.PACKAGES;
    }

    @Override
    public String getText()
    {
      return "Packages";
    }

    @Override
    public String getImageKey()
    {
      return "icons/full/obj16/Packages.gif";
    }

    @Override
    public EPackage[] getChildren()
    {
      CDOView view = getRepositoryProject().getView();
      CDOPackageRegistry packageRegistry = view.getSession().getPackageRegistry();
      List<EPackage> children = new ArrayList<EPackage>();
      for (String nsURI : packageRegistry.keySet())
      {
        EPackage ePackage = packageRegistry.getEPackage(nsURI);
        children.add(ePackage);
      }

      return children.toArray(new EPackage[children.size()]);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class ResourcesNode extends Node
  {
    public ResourcesNode(IRepositoryProject repositoryProject)
    {
      super(repositoryProject);
    }

    @Override
    public Type getType()
    {
      return Type.RESOURCES;
    }

    @Override
    public String getText()
    {
      return "Resources";
    }

    @Override
    public String getImageKey()
    {
      return "icons/full/obj16/Resources.gif";
    }

    @Override
    public CDOResourceNode[] getChildren()
    {
      CDOView view = getRepositoryProject().getView();
      List<CDOResourceNode> resources = view.queryResources(null, null, false);
      List<CDOResourceNode> children = new ArrayList<CDOResourceNode>();
      for (CDOResourceNode resourceNode : resources)
      {
        children.add(resourceNode);
      }

      return children.toArray(new CDOResourceNode[children.size()]);
    }
  }
}
