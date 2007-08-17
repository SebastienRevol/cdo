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
package org.eclipse.emf.cdo.server.db;

import org.eclipse.emf.cdo.internal.protocol.model.CDOPackageImpl;
import org.eclipse.emf.cdo.server.IStore;

import org.eclipse.net4j.db.IDBSchema;
import org.eclipse.net4j.db.IDBTable;

import java.util.Properties;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface IMappingStrategy
{
  public String getType();

  public IStore getStore();

  public void setStore(IStore store);

  public Properties getProperties();

  public void setProperties(Properties properties);

  public IDBSchema getSchema();

  /**
   * @return A collection of the affected tables.
   */
  public Set<IDBTable> map(CDOPackageImpl[] cdoPackages);
}
