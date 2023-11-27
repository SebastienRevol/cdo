/*
 * Copyright (c) 2023 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.security;

/**
 * @since 3.23
 */
public interface AdministrationPredicate
{
  /**
   * Queries whether a given user has administrative privileges.
   *
   * @param userID an user ID, which may or may not exist
   *
   * @return whether the userID exists and has administrative privileges
   */
  public boolean isAdministrator(String userID);
}