/*
 * Copyright (c) 2013, 2015, 2016, 2023 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.expressions.provider;

import org.eclipse.emf.cdo.expressions.ExpressionsFactory;
import org.eclipse.emf.cdo.expressions.ExpressionsPackage;
import org.eclipse.emf.cdo.expressions.Invocation;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemColorProvider;
import org.eclipse.emf.edit.provider.ITableItemFontProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.cdo.expressions.Invocation} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class InvocationItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider,
    IItemLabelProvider, IItemPropertySource, ITableItemLabelProvider, ITableItemColorProvider, ITableItemFontProvider, IItemColorProvider, IItemFontProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InvocationItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      super.getPropertyDescriptors(object);

    }
    return itemPropertyDescriptors;
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS);
      childrenFeatures.add(ExpressionsPackage.Literals.INVOCATION__NAME);
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EStructuralFeature getChildFeature(Object object, Object child)
  {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    return super.getChildFeature(object, child);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean hasChildren(Object object)
  {
    return hasChildren(object, true);
  }

  /**
   * This returns <code>getImage(object)</code> for the column index <code>0</code> or <code>super.getImage(object)</code> otherwise.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getText(Object)
   * @see #getColumnText(Object, int)
   * @generated NOT
   */
  @Override
  public Object getColumnImage(Object object, int columnIndex)
  {
    return columnIndex == 0 ? getImage(object) : super.getImage(object);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean shouldComposeCreationImage()
  {
    return true;
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getText(Object object)
  {
    return getString("_UI_Invocation_type");
  }

  /**
   * This returns <code>getText(object)</code> for the column index <code>0</code> or <code>super.getText(object)</code> otherwise.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getImage(Object)
   * @see #getColumnImage(Object, int)
   * @generated NOT
   */
  @Override
  public String getColumnText(Object object, int columnIndex)
  {
    return columnIndex == 0 ? getText(object) : super.getText(object);
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(Invocation.class))
    {
    case ExpressionsPackage.INVOCATION__ARGUMENTS:
    case ExpressionsPackage.INVOCATION__NAME:
      fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
      return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createBooleanValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createByteValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createShortValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createIntValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createLongValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createFloatValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createDoubleValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createCharValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createStringValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createFunctionInvocation()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createMemberInvocation()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createStaticAccess()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createMemberAccess()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createContextAccess()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createContainedObject()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createLinkedObject()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createLinkedExpression()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__ARGUMENTS, ExpressionsFactory.eINSTANCE.createListConstruction()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createBooleanValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createByteValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createShortValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createIntValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createLongValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createFloatValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createDoubleValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createCharValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createStringValue()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createFunctionInvocation()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createMemberInvocation()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createStaticAccess()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createMemberAccess()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createContextAccess()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createContainedObject()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createLinkedObject()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createLinkedExpression()));

    newChildDescriptors.add(createChildParameter(ExpressionsPackage.Literals.INVOCATION__NAME, ExpressionsFactory.eINSTANCE.createListConstruction()));
  }

  /**
   * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection)
  {
    Object childFeature = feature;
    Object childObject = child;

    boolean qualify = childFeature == ExpressionsPackage.Literals.INVOCATION__ARGUMENTS || childFeature == ExpressionsPackage.Literals.INVOCATION__NAME;

    if (qualify)
    {
      return getString("_UI_CreateChild_text2", new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
    }
    return super.getCreateChildText(owner, feature, child, selection);
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    return ((IChildCreationExtender)adapterFactory).getResourceLocator();
  }

}
