/*
 * Copyright (c) 2010-2012, 2014-2016, 2019-2021 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.etypes.impl;

import org.eclipse.emf.cdo.common.branch.CDOBranchPointRef;
import org.eclipse.emf.cdo.common.branch.CDOBranchRef;
import org.eclipse.emf.cdo.common.lob.CDOBlob;
import org.eclipse.emf.cdo.common.lob.CDOClob;
import org.eclipse.emf.cdo.common.lob.CDOLob;
import org.eclipse.emf.cdo.common.model.CDOClassifierRef;
import org.eclipse.emf.cdo.etypes.Annotation;
import org.eclipse.emf.cdo.etypes.EtypesFactory;
import org.eclipse.emf.cdo.etypes.EtypesPackage;
import org.eclipse.emf.cdo.etypes.ModelElement;
import org.eclipse.emf.cdo.etypes.util.EtypesValidator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>.
 *
 * @since 4.0
 * @noextend This interface is not intended to be extended by clients. <!-- end-user-doc -->
 * @generated
 */
public class EtypesPackageImpl extends EPackageImpl implements EtypesPackage
{
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass modelElementEClass = null;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EClass annotationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringToStringMapEntryEClass = null;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EDataType lobEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType inputStreamEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType readerEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType classifierRefEDataType = null;

  /**
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   */
  private EDataType branchRefEDataType = null;

  /**
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   */
  private EDataType branchPointRefEDataType = null;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EDataType blobEDataType = null;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private EDataType clobEDataType = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry
   * EPackage.Registry} by the package package URI value.
   * <p>
   * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
   * performs initialization of the package, or returns the registered package, if one already exists. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   *
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipse.emf.cdo.etypes.EtypesPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private EtypesPackageImpl()
  {
    super(eNS_URI, EtypesFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link EtypesPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static EtypesPackage init()
  {
    if (isInited)
    {
      return (EtypesPackage)EPackage.Registry.INSTANCE.getEPackage(EtypesPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredEtypesPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    EtypesPackageImpl theEtypesPackage = registeredEtypesPackage instanceof EtypesPackageImpl ? (EtypesPackageImpl)registeredEtypesPackage
        : new EtypesPackageImpl();

    isInited = true;

    // Create package meta-data objects
    theEtypesPackage.createPackageContents();

    // Initialize created meta-data
    theEtypesPackage.initializePackageContents();

    // Register package validator
    EValidator.Registry.INSTANCE.put(theEtypesPackage, new EValidator.Descriptor()
    {
      @Override
      public EValidator getEValidator()
      {
        return EtypesValidator.INSTANCE;
      }
    });

    // Mark meta-data to indicate it can't be changed
    theEtypesPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(EtypesPackage.eNS_URI, theEtypesPackage);
    return theEtypesPackage;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getModelElement()
  {
    return modelElementEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getModelElement_Annotations()
  {
    return (EReference)modelElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EOperation getModelElement__GetAnnotation__String()
  {
    return modelElementEClass.getEOperations().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getAnnotation()
  {
    return annotationEClass;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getAnnotation_Source()
  {
    return (EAttribute)annotationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getAnnotation_Details()
  {
    return (EReference)annotationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getAnnotation_ModelElement()
  {
    return (EReference)annotationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getAnnotation_Contents()
  {
    return (EReference)annotationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getAnnotation_References()
  {
    return (EReference)annotationEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getStringToStringMapEntry()
  {
    return stringToStringMapEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getStringToStringMapEntry_Key()
  {
    return (EAttribute)stringToStringMapEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getStringToStringMapEntry_Value()
  {
    return (EAttribute)stringToStringMapEntryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   *
   * @since 4.1 <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getLob()
  {
    return lobEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * @since 4.3
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getInputStream()
  {
    return inputStreamEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * @since 4.3
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getReader()
  {
    return readerEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getClassifierRef()
  {
    return classifierRefEDataType;
  }

  /**
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getBranchRef()
  {
    return branchRefEDataType;
  }

  /**
   * <!-- begin-user-doc -->
  	 * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getBranchPointRef()
  {
    return branchPointRefEDataType;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getBlob()
  {
    return blobEDataType;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EDataType getClob()
  {
    return clobEDataType;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EtypesFactory getEtypesFactory()
  {
    return (EtypesFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated)
    {
      return;
    }
    isCreated = true;

    // Create classes and their features
    modelElementEClass = createEClass(MODEL_ELEMENT);
    createEReference(modelElementEClass, MODEL_ELEMENT__ANNOTATIONS);
    createEOperation(modelElementEClass, MODEL_ELEMENT___GET_ANNOTATION__STRING);

    annotationEClass = createEClass(ANNOTATION);
    createEAttribute(annotationEClass, ANNOTATION__SOURCE);
    createEReference(annotationEClass, ANNOTATION__DETAILS);
    createEReference(annotationEClass, ANNOTATION__MODEL_ELEMENT);
    createEReference(annotationEClass, ANNOTATION__CONTENTS);
    createEReference(annotationEClass, ANNOTATION__REFERENCES);

    stringToStringMapEntryEClass = createEClass(STRING_TO_STRING_MAP_ENTRY);
    createEAttribute(stringToStringMapEntryEClass, STRING_TO_STRING_MAP_ENTRY__KEY);
    createEAttribute(stringToStringMapEntryEClass, STRING_TO_STRING_MAP_ENTRY__VALUE);

    // Create data types
    blobEDataType = createEDataType(BLOB);
    clobEDataType = createEDataType(CLOB);
    lobEDataType = createEDataType(LOB);
    inputStreamEDataType = createEDataType(INPUT_STREAM);
    readerEDataType = createEDataType(READER);
    classifierRefEDataType = createEDataType(CLASSIFIER_REF);
    branchRefEDataType = createEDataType(BRANCH_REF);
    branchPointRefEDataType = createEDataType(BRANCH_POINT_REF);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized)
    {
      return;
    }
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters
    addETypeParameter(lobEDataType, "IO"); //$NON-NLS-1$

    // Set bounds for type parameters

    // Add supertypes to classes
    annotationEClass.getESuperTypes().add(getModelElement());

    // Initialize classes, features, and operations; add parameters
    initEClass(modelElementEClass, ModelElement.class, "ModelElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getModelElement_Annotations(), getAnnotation(), getAnnotation_ModelElement(), "annotations", null, 0, -1, ModelElement.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    EOperation op = initEOperation(getModelElement__GetAnnotation__String(), getAnnotation(), "getAnnotation", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "source", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(annotationEClass, Annotation.class, "Annotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getAnnotation_Source(), ecorePackage.getEString(), "source", null, 0, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getAnnotation_Details(), getStringToStringMapEntry(), null, "details", null, 0, -1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getAnnotation_ModelElement(), getModelElement(), getModelElement_Annotations(), "modelElement", null, 0, 1, Annotation.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getAnnotation_Contents(), ecorePackage.getEObject(), null, "contents", null, 0, -1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getAnnotation_References(), ecorePackage.getEObject(), null, "references", null, 0, -1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringToStringMapEntryEClass, Map.Entry.class, "StringToStringMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getStringToStringMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStringToStringMapEntry_Value(), ecorePackage.getEString(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize data types
    initEDataType(blobEDataType, CDOBlob.class, "Blob", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(clobEDataType, CDOClob.class, "Clob", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(lobEDataType, CDOLob.class, "Lob", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(inputStreamEDataType, InputStream.class, "InputStream", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(readerEDataType, Reader.class, "Reader", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(classifierRefEDataType, CDOClassifierRef.class, "ClassifierRef", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(branchRefEDataType, CDOBranchRef.class, "BranchRef", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(branchPointRefEDataType, CDOBranchPointRef.class, "BranchPointRef", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
   * <!-- begin-user-doc -->
   * @since 4.22
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/Ecore"; //$NON-NLS-1$
    addAnnotation(annotationEClass, source, new String[] { "constraints", "WellFormed WellFormedSourceURI" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

} // EtypesPackageImpl
