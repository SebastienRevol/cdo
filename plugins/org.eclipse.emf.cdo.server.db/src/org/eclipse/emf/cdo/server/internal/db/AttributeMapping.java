package org.eclipse.emf.cdo.server.internal.db;

import org.eclipse.emf.cdo.internal.protocol.CDOIDImpl;
import org.eclipse.emf.cdo.internal.protocol.revision.CDORevisionImpl;
import org.eclipse.emf.cdo.protocol.model.CDOFeature;
import org.eclipse.emf.cdo.server.db.IAttributeMapping;

import org.eclipse.net4j.db.DBException;
import org.eclipse.net4j.db.IDBAdapter;
import org.eclipse.net4j.db.IDBField;
import org.eclipse.net4j.util.ImplementationError;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Eike Stepper
 */
public abstract class AttributeMapping extends FeatureMapping implements IAttributeMapping
{
  private IDBField field;

  public AttributeMapping(ValueMapping valueMapping, CDOFeature feature)
  {
    super(valueMapping, feature);
    field = valueMapping.addField(feature, valueMapping.getTable());
  }

  public IDBField getField()
  {
    return field;
  }

  public void appendValue(StringBuilder builder, CDORevisionImpl revision)
  {
    IDBAdapter dbAdapter = getDBAdapter();
    Object value = getRevisionValue(revision);
    dbAdapter.appendValue(builder, field, value);
  }

  protected Object getRevisionValue(CDORevisionImpl revision)
  {
    CDOFeature feature = getFeature();
    return revision.getValue(feature);
  }

  public void extractValue(ResultSet resultSet, int column, CDORevisionImpl revision)
  {
    if (column != field.getPosition() + 1)
    {
      throw new ImplementationError("Column mismatch");
    }

    try
    {
      Object value = getResultSetValue(resultSet, column);
      if (resultSet.wasNull())
      {
        value = null;
      }

      revision.setValue(getFeature(), value);
    }
    catch (SQLException ex)
    {
      throw new DBException(ex);
    }
  }

  protected abstract Object getResultSetValue(ResultSet resultSet, int column) throws SQLException;

  /**
   * @author Eike Stepper
   */
  public static class AMString extends AttributeMapping
  {
    public AMString(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getString(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMShort extends AttributeMapping
  {
    public AMShort(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getShort(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMObject extends AttributeMapping
  {
    public AMObject(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      long id = resultSet.getLong(column);
      if (resultSet.wasNull())
      {
        return null;
      }

      return CDOIDImpl.create(id);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMLong extends AttributeMapping
  {
    public AMLong(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getLong(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMInteger extends AttributeMapping
  {
    public AMInteger(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getInt(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMFloat extends AttributeMapping
  {
    public AMFloat(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getFloat(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMDouble extends AttributeMapping
  {
    public AMDouble(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getDouble(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMDate extends AttributeMapping
  {
    public AMDate(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      // TODO Is getDate() correct?
      return resultSet.getDate(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMCharacter extends AttributeMapping
  {
    public AMCharacter(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      String str = resultSet.getString(column);
      if (resultSet.wasNull())
      {
        return null;
      }

      return str.charAt(0);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMByte extends AttributeMapping
  {
    public AMByte(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getByte(column);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AMBoolean extends AttributeMapping
  {
    public AMBoolean(ValueMapping valueMapping, CDOFeature feature)
    {
      super(valueMapping, feature);
    }

    @Override
    protected Object getResultSetValue(ResultSet resultSet, int column) throws SQLException
    {
      return resultSet.getBoolean(column);
    }
  }
}