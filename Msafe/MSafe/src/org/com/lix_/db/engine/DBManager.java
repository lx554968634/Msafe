package org.com.lix_.db.engine;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.Define;
import org.com.lix_.util.Debug;

import dalvik.system.BaseDexClassLoader;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * ���ݹ�����
 */
public class DBManager {
	
	private String TAG = "DBManager" ;

	private Context m_pContext;

	private SQLiteDatabase m_pDatabase;

	public DBManager(Context context) {
		m_pContext = context;
		m_pDatabase = context.openOrCreateDatabase(DB_define.DATABASE_NAME, Context.MODE_PRIVATE,
				null);
	}

	/**
	 * 
	 * ִ��SQL,�������Զ����Query
	 * 
	 * 
	 * 
	 * @param sql
	 * 
	 * @param bindArgs
	 * 
	 * @return
	 */
	public List<QueryResult> execQuerySQL(String sql, String... bindArgs) {
		Cursor cursor = null;
		try {
			List<QueryResult> resultList = new ArrayList<QueryResult>();
			openDB();
			cursor = m_pDatabase.rawQuery(sql, bindArgs);
			parseCursorToResult(cursor, resultList);
			return resultList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (cursor != null)
				cursor.close();
		}

	}

	/**
	 * 
	 * Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE.
	 * 
	 * 
	 * 
	 * @param sql
	 * 
	 * @param bindArgsonly
	 * 
	 *            byte[], String, Long and Double are supported in bindArgs.
	 * 
	 * @return
	 */
	public boolean execSQL(String sql, Object... bindArgs) {
		try {
			openDB();
			m_pDatabase.execSQL(sql, bindArgs);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * ��ѯ�����
	 * 
	 * 
	 * 
	 * @param table
	 * 
	 * @param columns
	 * 
	 *            ��Ϊnull��ʱ����������
	 * 
	 * @param selection
	 * 
	 * @param selectionArgs
	 * 
	 * @return
	 */
	public List<QueryResult> query(String table, String[] columns,
			String selection, String... selectionArgs) {
		return query(table, columns, selection, null, null, null, selectionArgs);
	}

	/**
	 * 
	 * ��ѯ�����
	 * 
	 * 
	 * 
	 * @param table
	 * 
	 * @param columns
	 * 
	 *            ��Ϊnull��ʱ����������
	 * 
	 * @param selection
	 * 
	 * @param selectionArgs
	 * 
	 * @return �����쳣�Ļ�����null
	 */
	public List<QueryResult> query(String table, String[] columns,
			String selection, String groupBy, String having, String orderBy,
			String... selectionArgs) {
		Cursor cursor = null;
		try {
			openDB();
			cursor = m_pDatabase.query(table, columns, selection,
					selectionArgs, groupBy, having, orderBy);
			List<QueryResult> resultList = new ArrayList<QueryResult>();
			parseCursorToResult(cursor, resultList);
			return resultList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	/**
	 * 
	 * ɾ������
	 * 
	 * 
	 * 
	 * @param table
	 * 
	 * @param whereClause
	 * 
	 * @param whereArgs
	 * 
	 * @return
	 */
	public boolean delete(String table, String whereClause, String[] whereArgs) {
		try {
			openDB();
			return m_pDatabase.delete(table, whereClause, whereArgs) > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * �����������
	 * 
	 * @param table
	 * 
	 * @param listVal
	 * 
	 * @return
	 */
	public boolean batchInsert(String table, List<ContentValues> listVal) {
		try {
			openDB();
			for (ContentValues contentValues : listVal) {
				m_pDatabase.insertOrThrow(table, null, contentValues);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * �������������������Ƿ���»��߲���
	 * 
	 * 
	 * 
	 * @param table
	 * 
	 * @param values
	 * 
	 * @return
	 */
	public boolean insertOrReplace(String table, ContentValues values) {
		try {
			openDB();
			return m_pDatabase.replaceOrThrow(table, null, values) != -1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * �������
	 * 
	 * 
	 * 
	 * @param table
	 * 
	 *            ����
	 * 
	 * @param values
	 * 
	 * @return
	 */
	public boolean insert(String table, ContentValues values) {
		try {
			openDB();
			Debug.i(TAG, "insert:"+table+":����");
			return m_pDatabase.insertOrThrow(table, null, values) != -1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * ���²���
	 * 
	 * 
	 * 
	 * @param table
	 * 
	 *            ����
	 * 
	 * @param values
	 * 
	 * @param whereClause
	 * 
	 * @param whereArgs
	 * 
	 * @return
	 */
	public boolean update(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		try {
			openDB();
			return m_pDatabase.update(table, values, whereClause, whereArgs) > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * �ر����ݿ�
	 */
	public void closeDB() {
		if (m_pDatabase.isOpen()) {
			m_pDatabase.close();
		}
	}

	/*
	 * * �����ݿ�
	 */
	public void openDB() {
		if (m_pDatabase.isOpen() == false)
			m_pDatabase = m_pContext.openOrCreateDatabase(
					DB_define.DATABASE_NAME, Context.MODE_PRIVATE, null);
	}

	private void parseCursorToResult(Cursor cursor, List<QueryResult> resultList) {
		int columnCount;
		int columnType;
		Object columnVal = null;
		while (cursor.moveToNext()) {
			columnCount = cursor.getColumnCount();
			QueryResult result = new QueryResult();
			for (int index = 0; index < columnCount; ++index) {
				columnType = cursor.getType(index);
				switch (columnType) {
				case Cursor.FIELD_TYPE_BLOB:
					columnVal = cursor.getBlob(index);
					break;
				case Cursor.FIELD_TYPE_FLOAT:
					columnVal = cursor.getFloat(index);
					break;
				case Cursor.FIELD_TYPE_INTEGER:
					columnVal = cursor.getInt(index);
					break;
				case Cursor.FIELD_TYPE_STRING:
					columnVal = cursor.getString(index);
					break;
				default:
					columnVal = cursor.getString(index);// δ֪����ȡ��getString

					break;
				}
				result.setProperty(cursor.getColumnName(index), columnVal);
			}
			resultList.add(result);
		}
	}
}
