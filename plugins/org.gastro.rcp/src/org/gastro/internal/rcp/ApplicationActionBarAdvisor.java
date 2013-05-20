/*
 * Copyright (c) 2009-2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *
 *  Initial Publication:
 *    Eclipse Magazin - http://www.eclipse-magazin.de
 */
package org.gastro.internal.rcp;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the actions added to a workbench window.
 * Each window will be populated with new actions.
 * 
 * @author Eike Stepper
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{
  // Actions - important to allocate these only in makeActions, and then use
  // them
  // in the fill methods. This ensures that the actions aren't recreated
  // when fillActionBars is called with FILL_PROXY.
  private IWorkbenchAction exitAction;

  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
  {
    super(configurer);
  }

  @Override
  protected void makeActions(final IWorkbenchWindow window)
  {
    // Creates the actions and registers them.
    // Registering is needed to ensure that key bindings work.
    // The corresponding commands keybindings are defined in the plugin.xml
    // file.
    // Registering also provides automatic disposal of the actions when
    // the window is closed.

    exitAction = ActionFactory.QUIT.create(window);
    register(exitAction);
  }
}
