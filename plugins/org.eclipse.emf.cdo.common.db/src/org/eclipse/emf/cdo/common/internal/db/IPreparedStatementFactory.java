/***************************************************************************
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Dietisheim - initial API and implementation
 *    Eike Stepper - maintenance
 **************************************************************************/
package org.eclipse.emf.cdo.common.internal.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author Andre Dietisheim
 */
public interface IPreparedStatementFactory<T>
{
  PreparedStatement getPreparedStatement(T t, Connection connection) throws Exception;

  void close();

}
