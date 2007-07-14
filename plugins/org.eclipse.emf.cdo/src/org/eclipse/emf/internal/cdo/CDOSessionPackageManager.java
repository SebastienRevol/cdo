/***************************************************************************
 * Copyright (c) 2004 - 2007 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.internal.cdo;

import org.eclipse.emf.cdo.internal.protocol.model.CDOPackageImpl;
import org.eclipse.emf.cdo.internal.protocol.model.CDOPackageManagerImpl;

/**
 * @author Eike Stepper
 */
public class CDOSessionPackageManager extends CDOPackageManagerImpl
{
  private CDOSessionImpl session;

  public CDOSessionPackageManager(CDOSessionImpl session)
  {
    this.session = session;
  }

  public CDOSessionImpl getSession()
  {
    return session;
  }

  @Override
  protected CDOPackageImpl resolve(String packageURI)
  {
    return null;
  }
}
