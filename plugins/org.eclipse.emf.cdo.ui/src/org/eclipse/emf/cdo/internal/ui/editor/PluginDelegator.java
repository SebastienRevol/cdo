/*
 * Copyright (c) 2007, 2009, 2011, 2012, 2015, 2021 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.ui.editor;

import org.eclipse.emf.cdo.internal.ui.bundle.OM;

import org.eclipse.net4j.util.om.OMBundle.TranslationSupport;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

/**
 * @author Eike Stepper
 * @generated
 */
public final class PluginDelegator extends EMFPlugin
{
  private static final TranslationSupport TRANSLATION_SUPPORT = OM.BUNDLE.getTranslationSupport();

  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public static final PluginDelegator INSTANCE = new PluginDelegator();

  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private static Implementation plugin;

  /**
   * Create the instance.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public PluginDelegator()
  {
    super(new ResourceLocator[] {});
  }

  @Override
  public String getString(String key)
  {
    return TRANSLATION_SUPPORT.getString(key);
  }

  @Override
  public String getString(String key, boolean translate)
  {
    return TRANSLATION_SUPPORT.getString(key, translate);
  }

  @Override
  public String getString(String key, Object[] substitutions)
  {
    return TRANSLATION_SUPPORT.getString(key, substitutions);
  }

  @Override
  public String getString(String key, Object[] substitutions, boolean translate)
  {
    return TRANSLATION_SUPPORT.getString(key, substitutions, translate);
  }

  /**
   * @ADDED
   */
  @Override
  public void log(Object logEntry)
  {
    if (logEntry instanceof Throwable)
    {
      OM.LOG.error((Throwable)logEntry);
    }
    else
    {
      OM.LOG.info(logEntry.toString());
    }
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @return the singleton instance.
   * @generated
   */
  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @return the singleton instance.
   * @generated
   */
  public static Implementation getPlugin()
  {
    return plugin;
  }

  /**
   * The actual implementation of the Eclipse <b>Plugin</b>.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public static class Implementation extends EclipseUIPlugin
  {
    /**
     * Creates an instance.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Implementation()
    {
      super();

      // Remember the static instance.
      //
      plugin = this;
    }
  }

}
