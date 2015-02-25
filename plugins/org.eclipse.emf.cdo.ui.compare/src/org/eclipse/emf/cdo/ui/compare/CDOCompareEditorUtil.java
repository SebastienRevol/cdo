/*
 * Copyright (c) 2012, 2013 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.ui.compare;

import org.eclipse.emf.cdo.common.branch.CDOBranchPoint;
import org.eclipse.emf.cdo.common.commit.CDOCommitInfo;
import org.eclipse.emf.cdo.compare.CDOCompare;
import org.eclipse.emf.cdo.compare.CDOCompareUtil;
import org.eclipse.emf.cdo.session.CDORepositoryInfo;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.spi.common.branch.CDOBranchUtil;
import org.eclipse.emf.cdo.ui.CDOItemProvider;
import org.eclipse.emf.cdo.ui.internal.compare.bundle.OM;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.cdo.view.CDOViewOpener;

import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.ui.UIUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.spi.cdo.InternalCDOView;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static methods to open an EMF Compare dialog.
 *
 * @author Eike Stepper
 * @since 4.2
 */
public class CDOCompareEditorUtil
{
  private static final ThreadLocal<Boolean> ACTIVATE_EDITOR = new ThreadLocal<Boolean>();

  private static final ThreadLocal<List<Runnable>> DISPOSE_RUNNABLES = new ThreadLocal<List<Runnable>>();

  /**
   * @since 4.3
   */
  public static boolean openEditor(CDOViewOpener viewOpener, CDOBranchPoint leftPoint, CDOBranchPoint rightPoint,
      CDOView[] originView, boolean activate)
  {
    ACTIVATE_EDITOR.set(activate);

    try
    {
      return openDialog(viewOpener, leftPoint, rightPoint, originView);
    }
    finally
    {
      ACTIVATE_EDITOR.remove();
    }
  }

  /**
   * @since 4.3
   */
  public static boolean openEditor(CDOCommitInfo leftCommitInfo, CDOBranchPoint rightPoint, boolean activate)
  {
    ACTIVATE_EDITOR.set(activate);

    try
    {
      return openDialog(leftCommitInfo, rightPoint);
    }
    finally
    {
      ACTIVATE_EDITOR.remove();
    }
  }

  /**
   * @since 4.3
   */
  public static boolean openEditor(CDOCommitInfo commitInfo, boolean activate)
  {
    ACTIVATE_EDITOR.set(activate);

    try
    {
      return openDialog(commitInfo);
    }
    finally
    {
      ACTIVATE_EDITOR.remove();
    }
  }

  /**
   * @since 4.3
   */
  public static boolean openEditor(CDOView leftView, CDOView rightView, CDOView[] originView, boolean activate)
  {
    ACTIVATE_EDITOR.set(activate);

    try
    {
      return openDialog(leftView, rightView, originView);
    }
    finally
    {
      ACTIVATE_EDITOR.remove();
    }
  }

  /**
   * @since 4.3
   */
  public static boolean openDialog(CDOSession session, CDOBranchPoint leftPoint, CDOBranchPoint rightPoint)
  {
    return openDialog(session, leftPoint, rightPoint, null);
  }

  /**
   * @since 4.3
   */
  public static boolean openDialog(CDOViewOpener viewOpener, CDOBranchPoint leftPoint, CDOBranchPoint rightPoint,
      CDOView[] originView)
  {
    final Boolean activateEditor = ACTIVATE_EDITOR.get();
    final CDOView[] leftAndRightView = { null, null };

    addDisposeRunnables(new Runnable()
    {
      public void run()
      {
        LifecycleUtil.deactivate(leftAndRightView[0]);
        LifecycleUtil.deactivate(leftAndRightView[1]);
      }
    });

    try
    {
      leftAndRightView[0] = viewOpener.openView(leftPoint, new ResourceSetImpl());
      leftAndRightView[1] = viewOpener.openView(rightPoint, new ResourceSetImpl());

      return openDialog(leftAndRightView[0], leftAndRightView[1], originView);
    }
    finally
    {
      if (activateEditor == null)
      {
        List<Runnable> list = removeDisposeRunnables();
        runDisposeRunnables(list);
      }
    }
  }

  public static boolean openDialog(CDOCommitInfo leftCommitInfo, CDOBranchPoint rightPoint)
  {
    CDORepositoryInfo repositoryInfo = (CDORepositoryInfo)leftCommitInfo.getCommitInfoManager().getRepository();
    CDOSession session = repositoryInfo.getSession();

    return openDialog(session, leftCommitInfo, rightPoint);
  }

  public static boolean openDialog(CDOCommitInfo commitInfo)
  {
    long previousTimeStamp = commitInfo.getPreviousTimeStamp();
    if (previousTimeStamp == CDOBranchPoint.UNSPECIFIED_DATE)
    {
      return false;
    }

    CDOBranchPoint previous = CDOBranchUtil.normalizeBranchPoint(commitInfo.getBranch(), previousTimeStamp);
    return openDialog(commitInfo, previous);
  }

  public static boolean openDialog(CDOView leftView, CDOView rightView, CDOView[] originView)
  {
    final Input input = createComparisonInput(leftView, rightView, originView);

    final Boolean activateEditor = ACTIVATE_EDITOR.get();
    if (activateEditor != null)
    {
      List<Runnable> disposeRunnables = removeDisposeRunnables();
      input.setDisposeRunnables(disposeRunnables);

      UIUtil.getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          CompareUI.openCompareEditor(input, activateEditor);
        }
      });
    }
    else
    {
      CompareUI.openCompareDialog(input);
    }

    return input.isOK();
  }

  /**
   * @since 4.3
   */
  public static Input createComparisonInput(CDOView leftView, CDOView rightView, CDOView[] originView)
  {
    Comparison comparison = CDOCompareUtil.compare(leftView, rightView, originView);

    IComparisonScope scope = CDOCompare.getScope(comparison);
    ICompareEditingDomain editingDomain = EMFCompareEditingDomain.create(scope.getLeft(), scope.getRight(),
        scope.getOrigin());

    ComposedAdapterFactory.Descriptor.Registry registry = EMFEditPlugin.getComposedAdapterFactoryDescriptorRegistry();
    ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(registry);

    CDOBranchPoint leftBranchPoint = CDOBranchUtil.copyBranchPoint(leftView);
    CDOBranchPoint rightBranchPoint = CDOBranchUtil.copyBranchPoint(rightView);

    CDOItemProvider itemProvider = new CDOItemProvider(null)
    {
      @Override
      public boolean useFullPath(Object object)
      {
        if (object instanceof CDOBranchPoint)
        {
          return true;
        }

        return super.useFullPath(object);
      }
    };

    Image leftImage = itemProvider.getImage(leftBranchPoint);
    String leftLabel = itemProvider.getText(leftBranchPoint);
    Image rightImage = itemProvider.getImage(rightBranchPoint);
    String rightLabel = itemProvider.getText(rightBranchPoint);
    itemProvider.dispose();

    CompareConfiguration configuration = new CompareConfiguration();
    configuration.setLeftImage(leftImage);
    configuration.setLeftLabel(leftLabel);
    configuration.setLeftEditable(!leftView.isReadOnly());
    configuration.setRightImage(rightImage);
    configuration.setRightLabel(rightLabel);
    configuration.setRightEditable(!rightView.isReadOnly());

    String title = "Compare " + ((InternalCDOView)leftView).getRepositoryName() + " " + leftLabel + " and "
        + rightLabel;

    Input input = new Input(configuration, comparison, editingDomain, adapterFactory);
    input.setTitle(title);
    return input;
  }

  /**
   * @since 4.3
   */
  public static void addDisposeRunnables(Runnable... disposeRunnables)
  {
    List<Runnable> list = DISPOSE_RUNNABLES.get();
    if (list == null)
    {
      list = new ArrayList<Runnable>();
      DISPOSE_RUNNABLES.set(list);
    }

    list.addAll(Arrays.asList(disposeRunnables));
  }

  private static List<Runnable> removeDisposeRunnables()
  {
    List<Runnable> list = DISPOSE_RUNNABLES.get();
    DISPOSE_RUNNABLES.remove();
    return list;
  }

  private static void runDisposeRunnables(List<Runnable> disposeRunnables)
  {
    if (disposeRunnables != null)
    {
      for (Runnable disposeRunnable : disposeRunnables)
      {
        try
        {
          disposeRunnable.run();
        }
        catch (Exception ex)
        {
          OM.LOG.error(ex);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  @SuppressWarnings("restriction")
  private static final class Input extends org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonEditorInput
  {
    private static final Image COMPARE_IMAGE = OM.getImage("icons/compare.gif");

    private List<Runnable> disposeRunnables;

    private boolean ok;

    private Input(CompareConfiguration configuration, Comparison comparison, ICompareEditingDomain editingDomain,
        AdapterFactory adapterFactory)
    {
      super(new org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration(configuration),
          comparison, editingDomain, adapterFactory);
    }

    private void dispose()
    {
      AdapterFactory adapterFactory = getAdapterFactory();
      if (adapterFactory instanceof ComposedAdapterFactory)
      {
        ComposedAdapterFactory composedAdapterFactory = (ComposedAdapterFactory)adapterFactory;
        composedAdapterFactory.dispose();
      }

      runDisposeRunnables(disposeRunnables);
      disposeRunnables = null;
    }

    @Override
    public Image getTitleImage()
    {
      return COMPARE_IMAGE;
    }

    public void setDisposeRunnables(List<Runnable> disposeRunnables)
    {
      this.disposeRunnables = disposeRunnables;
    }

    public boolean isOK()
    {
      return ok;
    }

    @Override
    public boolean okPressed()
    {
      try
      {
        ok = true;
        return super.okPressed();
      }
      finally
      {
        dispose();
      }
    }

    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener)
    {
      try
      {
        super.removePropertyChangeListener(listener);
      }
      finally
      {
        dispose();
      }
    }
  }
}
