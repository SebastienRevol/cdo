/*
 * Copyright (c) 2009-2013, 2018-2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Stefan Winkler - major refactoring
 */
package org.eclipse.emf.cdo.server.internal.db.mapping.horizontal;

import org.eclipse.emf.cdo.server.db.mapping.IClassMapping;
import org.eclipse.emf.cdo.server.db.mapping.IListMapping;

import org.eclipse.net4j.db.DBUtil;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Eike Stepper
 * @since 2.0
 */
public class HorizontalAuditMappingStrategy extends AbstractHorizontalMappingStrategy
{
  public HorizontalAuditMappingStrategy()
  {
  }

  @Override
  public boolean hasAuditSupport()
  {
    return true;
  }

  @Override
  public boolean hasBranchingSupport()
  {
    return false;
  }

  @Override
  public boolean hasDeltaSupport()
  {
    return false;
  }

  @Override
  protected IClassMapping doCreateClassMapping(EClass eClass)
  {
    return new HorizontalAuditClassMapping(this, eClass);
  }

  @Override
  public IListMapping doCreateListMapping(EClass containingClass, EStructuralFeature feature)
  {
    return new AuditListTableMapping(this, containingClass, feature);
  }

  @Override
  public String getListJoin(String attrTable, String listTable)
  {
    String join = super.getListJoin(attrTable, listTable);
    return modifyListJoin(attrTable, listTable, join);
  }

  protected String modifyListJoin(String attrTable, String listTable, String join)
  {
    join += " AND " + attrTable + "." + DBUtil.quoted(MappingNames.ATTRIBUTES_VERSION);
    join += "=" + listTable + "." + DBUtil.quoted(MappingNames.LIST_REVISION_VERSION);
    return join;
  }
}
