package org.com.lix_.db.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.com.lix_.db.engine.DBManager;
import org.com.lix_.db.engine.QueryResult;
import org.com.lix_.db.engine.SqlHelper;
import org.com.lix_.db.engine.Table.Column;

import android.content.ContentValues;
import android.content.Context;

public class IDao {

	protected String TAG = "IDAO";

	private DBManager m_pDb;

	private Class m_pOriginClass;

	private String m_sTableName;
	private String m_sPk;

	public IDao() {
	}
	
	public void finish()
	{
		if(m_pDb != null)
		{
			m_pDb.closeDB(); 
		}
	}

	public IDao(Context pContext, Class pClass) {
		m_pOriginClass = pClass;
		m_sTableName = SqlHelper.getTableName(pClass);
		if (m_pDb == null) {
			m_pDb = new DBManager(pContext);
		}
	}

	public void createTable() {
		String createTableSQL = SqlHelper.getCreateTableSQL(m_pOriginClass,
				new SqlHelper.OnPrimaryKeyListener() {
					@Override
					public void onGetPrimaryKey(String keyName) {
						m_sPk = keyName;
					}
				});
		m_pDb.execSQL(createTableSQL);
	}

	/**
	 * 插入一条记录
	 * 
	 * @param model
	 * @return
	 */
	public boolean insert(Object model) {
		ContentValues contentValues = new ContentValues();
		SqlHelper.parseModelToContentValues(model, contentValues);
		return m_pDb.insert(m_sTableName, contentValues);
	}

	/**
	 * 插入多条记录
	 * 
	 * @param model
	 * @return
	 */
	public boolean batchInsert(List dataList) {
		List<ContentValues> listVal = new ArrayList<ContentValues>();
		for (Object model : dataList) {
			ContentValues contentValues = new ContentValues();
			SqlHelper.parseModelToContentValues(model, contentValues);
			listVal.add(contentValues);
		}
		return m_pDb.batchInsert(m_sTableName, listVal);
	}

	/**
	 * 更新记录
	 * 
	 * @param model
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean update(Object model, String whereClause, String... whereArgs) {
		ContentValues contentValues = new ContentValues();
		SqlHelper.parseModelToContentValues(model, contentValues);
		return m_pDb
				.update(m_sTableName, contentValues, whereClause, whereArgs);
	}

	private void reGainPrimaryKey() {
		createTable();
	}

	/**
	 * 决定是否insert或者Update
	 * 
	 * @param model
	 * @param bindColumnNames
	 *            绑定的列名 ,默认是主键
	 * @return
	 */
	public boolean insertOrUpdate(Object model, String... bindColumnNames) {
		String[] bindColumnValues;
		String selection;
		Object modelInDb;
		if (bindColumnNames == null || bindColumnNames.length == 0) {
			if (m_sPk == null)
				reGainPrimaryKey();
			bindColumnValues = new String[1];
			selection = m_sPk + "=?";
			bindColumnValues[0] = getValueByColumnName(model, m_sPk);
		} else {
			bindColumnValues = new String[bindColumnNames.length];
			StringBuilder selectionBuidler = new StringBuilder();
			String columnName;
			String columnValue;
			for (int index = 0; index < bindColumnNames.length; ++index) {
				columnName = bindColumnNames[index];
				columnValue = getValueByColumnName(model, columnName);
				if (columnValue == null) {
					selectionBuidler.append(" " + columnName + " is null ");
				} else {
					bindColumnValues[index] = columnValue;
					selectionBuidler.append(" " + columnName + "=? ");
				}
				selectionBuidler.append("and");
			}
			selection = selectionBuidler.substring(0,
					selectionBuidler.length() - 3);
		}
		modelInDb = queryUniqueRecord(selection, bindColumnValues);
		ContentValues contentValues = new ContentValues();
		SqlHelper.parseModelToContentValues(model, contentValues);
		if (modelInDb == null) {
			return m_pDb.insert(m_sTableName, contentValues);
		} else {
			return m_pDb.update(m_sTableName, contentValues, selection,
					bindColumnValues);
		}
	}

	/**
	 * 
	 * 根据列名获取对象中的值
	 * 
	 * 
	 * 
	 * @param model
	 * 
	 * @param columnName
	 * 
	 * @return
	 */
	private String getValueByColumnName(Object model, String columnName) {
		Class<?> mClazz = model.getClass();
		Field[] fields = mClazz.getDeclaredFields();

		Column column;
		for (Field field : fields) {
			field.setAccessible(true);
			column = field.getAnnotation(Column.class);
			if (column != null && columnName.equalsIgnoreCase(column.name())) {
				try {
					return field.get(model) == null ? "" : field.get(model)
							.toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 删除记录
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean delete(String whereClause, String... whereArgs) {
		return m_pDb.delete(m_sTableName, whereClause, whereArgs);
	}

	/**
	 * 删除全部记录
	 * 
	 * @return
	 */
	public boolean deleteAll() {
		return delete(null);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public List queryByCondition(String selection, String... selectionArgs) {
		return queryByCondition(null, selection, null, selectionArgs);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param columns
	 * @param selection
	 * @param orderBy
	 * @param selectionArgs
	 * @return
	 */
	public List queryByCondition(String[] columns, String selection,
			String orderBy, String... selectionArgs) {
		return queryByCondition(columns, selection, null, null, orderBy,
				selectionArgs);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param columns
	 * @param selection
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param selectionArgs
	 * @return
	 */
	public List queryByCondition(String[] columns, String selection,
			String groupBy, String having, String orderBy,
			String... selectionArgs) {
		List resultList = new ArrayList();
		List<QueryResult> queryList = m_pDb.query(m_sTableName, columns,
				selection, groupBy, having, orderBy, selectionArgs);
		SqlHelper.parseQueryResultListToModelList(queryList, resultList,
				m_pOriginClass);
		return resultList;
	}

	/**
	 * 只有唯一一条记录的查询
	 * 
	 * @return 如果没有则返回null
	 */
	public Object queryUniqueRecord(String selection, String... selectionArgs) {
		List resultList = queryByCondition(selection, selectionArgs);
		if (resultList != null && resultList.size() == 1) {
			return resultList.get(0);
		} else if (resultList != null && resultList.size() > 1) {
			StringBuilder args = new StringBuilder();
			for (String arg : selectionArgs) {
				args.append(arg);
				args.append(",");
			}
			throw new RuntimeException("表名:" + m_sTableName + ",条件为:selection="
					+ selection + ",selectionArgs=" + args + "超过一条数据!");
		} else {
			return null;
		}
	}

	/**
	 * 自定义查询
	 * 
	 * @param sql
	 * @param bindArgs
	 * @return
	 */
	public List<QueryResult> execQuerySQL(String sql, String... bindArgs) {
		return m_pDb.execQuerySQL(sql, bindArgs);
	}

	/**
	 * 执行Insert/Update/Delete等其他非查询SQL
	 * 
	 * @param sql
	 * @param bindArgs
	 * @return
	 */
	public boolean execUpdateSQL(String sql, Object... bindArgs) {
		return m_pDb.execSQL(sql, bindArgs);
	}
}
