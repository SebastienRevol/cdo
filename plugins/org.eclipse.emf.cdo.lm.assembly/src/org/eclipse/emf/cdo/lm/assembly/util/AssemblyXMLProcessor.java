/*
 * Copyright (c) 2022 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.lm.assembly.util;

import org.eclipse.emf.cdo.lm.assembly.AssemblyPackage;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

import java.util.Map;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * @generated
 */
public class AssemblyXMLProcessor extends XMLProcessor
{
  /**
   * Public constructor to instantiate the helper.
   * <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * @generated
   */
  public AssemblyXMLProcessor()
  {
    super(EPackage.Registry.INSTANCE);
    AssemblyPackage.eINSTANCE.eClass();
  }

  /**
   * Register for "*" and "xml" file extensions the AssemblyResourceFactoryImpl factory.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected Map<String, Resource.Factory> getRegistrations()
  {
    if (registrations == null)
    {
      super.getRegistrations();
      registrations.put(XML_EXTENSION, new AssemblyResourceFactoryImpl());
      registrations.put(STAR_EXTENSION, new AssemblyResourceFactoryImpl());
    }
    return registrations;
  }

} // AssemblyXMLProcessor
