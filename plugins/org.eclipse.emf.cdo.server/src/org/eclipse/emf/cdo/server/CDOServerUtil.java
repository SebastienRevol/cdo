/*
 * Copyright (c) 2007-2013, 2015-2017, 2019-2021, 2023 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.server;

import org.eclipse.emf.cdo.common.CDOCommonRepository;
import org.eclipse.emf.cdo.common.branch.CDOBranchPoint;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.common.revision.CDORevisionManager;
import org.eclipse.emf.cdo.common.revision.CDORevisionProvider;
import org.eclipse.emf.cdo.internal.server.Repository;
import org.eclipse.emf.cdo.internal.server.ServerCDOView;
import org.eclipse.emf.cdo.internal.server.ServerCDOView.ServerCDOSession;
import org.eclipse.emf.cdo.internal.server.SessionManager;
import org.eclipse.emf.cdo.internal.server.bundle.OM;
import org.eclipse.emf.cdo.internal.server.syncing.FailoverParticipant;
import org.eclipse.emf.cdo.internal.server.syncing.OfflineClone;
import org.eclipse.emf.cdo.internal.server.syncing.RepositorySynchronizer;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.session.CDOSessionConfigurationFactory;
import org.eclipse.emf.cdo.spi.common.branch.CDOBranchUtil;
import org.eclipse.emf.cdo.spi.common.revision.ManagedRevisionProvider;
import org.eclipse.emf.cdo.spi.server.InternalRepository;
import org.eclipse.emf.cdo.spi.server.InternalRepositorySynchronizer;
import org.eclipse.emf.cdo.spi.server.InternalSession;
import org.eclipse.emf.cdo.spi.server.InternalStore;
import org.eclipse.emf.cdo.spi.server.RepositoryFactory;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.net4j.util.ObjectUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMPlatform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Various static methods that may help with CDO {@link IRepository repositories} and server-side {@link CDOView views}.
 *
 * @author Eike Stepper
 */
public final class CDOServerUtil
{
  private CDOServerUtil()
  {
  }

  /**
   * @since 4.20
   */
  public static void prepareContainer(IManagedContainer container)
  {
    OM.BUNDLE.prepareContainer(container);
  }

  /**
   * @since 4.2
   */
  public static CDOView openView(ISession session, CDOBranchPoint branchPoint, CDORevisionProvider revisionProvider)
  {
    return new ServerCDOView((InternalSession)session, branchPoint, revisionProvider);
  }

  /**
   * @since 4.2
   */
  public static CDOView openView(ISession session, CDOBranchPoint branchPoint)
  {
    CDORevisionManager revisionManager = session.getRepository().getRevisionManager();
    CDORevisionProvider revisionProvider = new ManagedRevisionProvider(revisionManager, branchPoint);
    return openView(session, branchPoint, revisionProvider);
  }

  /**
   * @since 4.2
   */
  public static CDOView openView(IView view)
  {
    ISession session = view.getSession();
    CDOBranchPoint branchPoint = CDOBranchUtil.copyBranchPoint(view);
    return openView(session, branchPoint, view);
  }

  /**
   * @since 4.2
   */
  public static CDOView openView(IStoreAccessor.CommitContext commitContext)
  {
    ISession session = commitContext.getTransaction().getSession();
    CDOBranchPoint branchPoint = commitContext.getBranchPoint();
    return openView(session, branchPoint, commitContext);
  }

  /**
   * @since 4.0
   * @deprecated As of 4.2 the legacy mode is always enabled, use {@link #openView(ISession, CDOBranchPoint, CDORevisionProvider)}.
   */
  @Deprecated
  public static CDOView openView(ISession session, CDOBranchPoint branchPoint, boolean legacyModeEnabled, CDORevisionProvider revisionProvider)
  {
    return openView(session, branchPoint, revisionProvider);
  }

  /**
   * @since 4.0
   * @deprecated As of 4.2 the legacy mode is always enabled, use {@link #openView(ISession, CDOBranchPoint)}.
   */
  @Deprecated
  public static CDOView openView(ISession session, CDOBranchPoint branchPoint, boolean legacyModeEnabled)
  {
    return openView(session, branchPoint);
  }

  /**
   * @since 4.0
   * @deprecated As of 4.2 the legacy mode is always enabled, use {@link #openView(IView)}.
   */
  @Deprecated
  public static CDOView openView(IView view, boolean legacyModeEnabled)
  {
    return openView(view);
  }

  /**
   * @since 4.0
   * @deprecated As of 4.2 the legacy mode is always enabled, use {@link #openView(IStoreAccessor.CommitContext)}.
   */
  @Deprecated
  public static CDOView openView(IStoreAccessor.CommitContext commitContext, boolean legacyModeEnabled)
  {
    return openView(commitContext);
  }

  /**
   * @since 4.13
   */
  public static ITransaction getServerTransaction(CDOTransaction transaction)
  {
    IView serverView = getServerView(transaction);
    if (serverView instanceof ITransaction)
    {
      return (ITransaction)serverView;
    }

    return null;
  }

  /**
   * @since 4.13
   */
  public static IView getServerView(CDOView view)
  {
    if (view instanceof ServerCDOView)
    {
      CDORevisionProvider revisionProvider = ((ServerCDOView)view).getRevisionProvider();
      if (revisionProvider instanceof IView)
      {
        return (IView)revisionProvider;
      }
    }

    ISession session = getServerSession(view);
    if (session != null)
    {
      return session.getView(view.getViewID());
    }

    return null;
  }

  /**
   * @since 4.11
   */
  public static ISession getServerSession(CDOView view)
  {
    if (view instanceof ServerCDOView)
    {
      return ((ServerCDOView)view).getServerSession();
    }

    return getServerSession(view.getSession());
  }

  /**
   * @since 4.13
   */
  public static ISession getServerSession(CDOSession session)
  {
    if (session instanceof ServerCDOSession)
    {
      return ((ServerCDOSession)session).getInternalSession();
    }

    IRepository repository = getRepository(session);
    if (repository != null)
    {
      return repository.getSessionManager().getSession(session.getSessionID());
    }

    return null;
  }

  /**
   * @since 3.0
   */
  public static ISessionManager createSessionManager()
  {
    return new SessionManager();
  }

  public static IRepository createRepository(String name, IStore store, Map<String, String> props)
  {
    Repository repository = new Repository.Default();
    initRepository(repository, name, store, props);
    return repository;
  }

  /**
   * @since 3.0
   */
  public static IRepositorySynchronizer createRepositorySynchronizer(CDOSessionConfigurationFactory remoteSessionConfigurationFactory)
  {
    RepositorySynchronizer synchronizer = new RepositorySynchronizer();
    synchronizer.setRemoteSessionConfigurationFactory(remoteSessionConfigurationFactory);
    return synchronizer;
  }

  /**
   * @since 3.0
   */
  public static ISynchronizableRepository createOfflineClone(String name, IStore store, Map<String, String> props, IRepositorySynchronizer synchronizer)
  {
    OfflineClone repository = new OfflineClone();
    initRepository(repository, name, store, props);
    repository.setSynchronizer((InternalRepositorySynchronizer)synchronizer);
    return repository;
  }

  /**
   * @since 4.0
   */
  public static ISynchronizableRepository createFailoverParticipant(String name, IStore store, Map<String, String> props, IRepositorySynchronizer synchronizer,
      boolean master, boolean allowBackupCommits)
  {
    FailoverParticipant repository = new FailoverParticipant();
    initRepository(repository, name, store, props);

    if (synchronizer != null)
    {
      repository.setSynchronizer((InternalRepositorySynchronizer)synchronizer);
      repository.setType(master ? CDOCommonRepository.Type.MASTER : CDOCommonRepository.Type.BACKUP);
    }

    return repository;
  }

  /**
   * @since 3.0
   */
  public static ISynchronizableRepository createFailoverParticipant(String name, IStore store, Map<String, String> props, IRepositorySynchronizer synchronizer,
      boolean master)
  {
    return createFailoverParticipant(name, store, props, synchronizer, master, false);
  }

  /**
   * @since 4.0
   */
  public static ISynchronizableRepository createFailoverParticipant(String name, IStore store, Map<String, String> props, IRepositorySynchronizer synchronizer)
  {
    return createFailoverParticipant(name, store, props, synchronizer, false);
  }

  /**
   * @since 4.0
   */
  public static ISynchronizableRepository createFailoverParticipant(String name, IStore store, Map<String, String> props)
  {
    return createFailoverParticipant(name, store, props, null);
  }

  private static void initRepository(Repository repository, String name, IStore store, Map<String, String> props)
  {
    repository.setName(name);
    repository.setStore((InternalStore)store);
    repository.setProperties(props);
  }

  public static void addRepository(IManagedContainer container, IRepository repository)
  {
    InternalRepository internal = (InternalRepository)repository;
    if (internal.getContainer() == null && container != IPluginContainer.INSTANCE)
    {
      internal.setContainer(container);
    }

    String productGroup = RepositoryFactory.PRODUCT_GROUP;
    String type = RepositoryFactory.TYPE;
    String name = repository.getName();

    container.putElement(productGroup, type, name, repository);
    LifecycleUtil.activate(repository);
  }

  public static IRepository getRepository(IManagedContainer container, String name)
  {
    return RepositoryFactory.get(container, name);
  }

  /**
   * @since 4.13
   */
  public static IRepository getRepository(String uuid)
  {
    return Repository.get(uuid);
  }

  /**
   * @since 4.13
   */
  public static IRepository getRepository(CDOSession session)
  {
    String uuid = session.getRepositoryInfo().getUUID();
    return getRepository(uuid);
  }

  public static Element getRepositoryConfig(String repositoryName) throws ParserConfigurationException, SAXException, IOException
  {
    File configFile = OMPlatform.INSTANCE.getConfigFile("cdo-server.xml"); //$NON-NLS-1$

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(configFile);

    NodeList children = document.getDocumentElement().getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE)
      {
        Element childElement = (Element)child;
        if (childElement.getNodeName().equalsIgnoreCase("repository")) //$NON-NLS-1$
        {
          String name = childElement.getAttribute("name"); //$NON-NLS-1$
          if (ObjectUtil.equals(name, repositoryName))
          {
            return childElement;
          }
        }
      }
    }

    throw new IllegalStateException("Repository config not found: " + repositoryName); //$NON-NLS-1$
  }

  /**
   * @since 4.13
   */
  public static void execute(ISession context, Runnable runnable)
  {
    StoreThreadLocal.wrap(context, runnable).run();
  }

  /**
   * @since 4.13
   */
  public static void execute(CDOSession context, Consumer<ISession> consumer)
  {
    ISession serverSession = getServerSession(context);
    execute(serverSession, () -> consumer.accept(serverSession));
  }

  /**
   * An abstract {@link IRepository.ReadAccessHandler read-access handler} that grants or denies access to single
   * {@link CDORevision revisions}.
   *
   * @author Eike Stepper
   * @since 2.0
   */
  public static abstract class RepositoryReadAccessValidator implements IRepository.ReadAccessHandler
  {
    public RepositoryReadAccessValidator()
    {
    }

    @Override
    public void handleRevisionsBeforeSending(ISession session, CDORevision[] revisions, List<CDORevision> additionalRevisions) throws RuntimeException
    {
      List<String> violations = new ArrayList<>();
      for (CDORevision revision : revisions)
      {
        String violation = validate(session, revision);
        if (violation != null)
        {
          violations.add(violation);
        }
      }

      if (!violations.isEmpty())
      {
        throwException(session, violations);
      }

      for (Iterator<CDORevision> it = additionalRevisions.iterator(); it.hasNext();)
      {
        CDORevision revision = it.next();
        String violation = validate(session, revision);
        if (violation != null)
        {
          OM.LOG.info("Revision can not be delivered to " + session + ": " + violation); //$NON-NLS-1$ //$NON-NLS-2$
          it.remove();
        }
      }
    }

    protected void throwException(ISession session, List<String> violations) throws RuntimeException
    {
      StringBuilder builder = new StringBuilder();
      builder.append("Revisions can not be delivered to "); //$NON-NLS-1$
      builder.append(session);
      builder.append(":"); //$NON-NLS-1$
      for (String violation : violations)
      {
        builder.append("\n- "); //$NON-NLS-1$
        builder.append(violation);
      }

      throwException(builder.toString());
    }

    protected void throwException(String message) throws RuntimeException
    {
      throw new IllegalStateException(message);
    }

    protected abstract String validate(ISession session, CDORevision revision);
  }
}
