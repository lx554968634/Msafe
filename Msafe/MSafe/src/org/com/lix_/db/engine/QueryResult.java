package org.com.lix_.db.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.com.lix_.util.UiUtils;

public class QueryResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8740283615808815960L;

	// 保持原来的顺序[列名，值]

	private Map<String, Object> nameValueMap = new LinkedHashMap<String, Object>();

	// 保持原来的顺序[索引号,值]

	private Map<Integer, Object> indexValueMap = new LinkedHashMap<Integer, Object>();

	// 列名

	private List<String> columnNameList = new ArrayList<String>();

	private int index = 0;

	public static final int ERROR_VALUE = 0xFFFFFFFF;

	/**
	 * 
	 * 设置某一列的值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 *            列名
	 * 
	 * @param columnValue
	 */
	public synchronized void setProperty(String columnName, Object columnValue) {
		columnName = columnName.toLowerCase();
		columnNameList.add(columnName);
		if (columnName != null && columnValue == null) {
			nameValueMap.put(columnName, "");
			indexValueMap.put(index, "");
		} else {
			nameValueMap.put(columnName, columnValue);
			indexValueMap.put(index, columnValue);
		}
		++index;
	}

	/**
	 * 
	 * 改变某index列的值
	 * 
	 * 
	 * 
	 * @param index
	 * 
	 * @param value
	 */
	public void changeProperty(int index, Object value) {
		if (indexValueMap.containsKey(index)) {
			indexValueMap.put(index, value);
			nameValueMap.put(columnNameList.get(index), value);
		}
	}

	/**
	 * 
	 * 获取某一列的值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 *            列名
	 * 
	 * @return 不存在返回null
	 */
	public Object getProperty(String columnName) {
		return nameValueMap.get(columnName.toLowerCase());
	}

	/**
	 * 
	 * 获取Boolean类型值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 * @return 如果该值不能正常转换，会返回false
	 */
	public boolean getBooleanProperty(String columnName) {
		Object value = getProperty(columnName);
		if (value instanceof Boolean) {
			return (Boolean) value;
		} else if (value instanceof String) {
			if (value.equals("true")) {
				return true;
			} else if (value.equals("false")) {
				return false;
			}
		} else if (value instanceof Long) {
			return (Long) value == 1;
		} else if (value instanceof Integer) {
			return (Integer) value == 1;
		} else if (value instanceof Short) {
			return (Short) value == 1;
		}
		return false;
	}

	/**
	 * 
	 * 
	 * 
	 * 获取Long类型值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 * @return 如果该值不能正常转换，会返回 ERROR_VALUE
	 */
	public long getLongProperty(String columnName) {
		return (long) getDoubleProperty(columnName);
	}

	/**
	 * 
	 * 
	 * 
	 * 获取Int类型值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 * @return 如果该值不能正常转换，会返回 ERROR_VALUE
	 * 
	 * @see QueryResult.ERROR_VALUE
	 */
	public int getIntProperty(String columnName) {
		return (int) getLongProperty(columnName);
	}

	/**
	 * 
	 * 
	 * 
	 * 获取Short类型值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 * @return 如果该值不能正常转换，会返回 ERROR_VALUE
	 */
	public short getShortProperty(String columnName) {
		return (short) getIntProperty(columnName);
	}

	/**
	 * 
	 * 获取Double类型值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 * @return 如果该值不能正常转换，会返回ERROR_VALUE
	 */
	public double getDoubleProperty(String columnName) {
		Object value = getProperty(columnName);
		if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof Float) {
			return (Float) value;
		} else if (value instanceof Long) {
			return (Long) value;
		} else if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof Short) {
			return (Short) value;
		} else if (value instanceof String) {
			if (UiUtils.isNum((String) value)) {
				return Double.parseDouble((String) value);
			}
		}
		return ERROR_VALUE;
	}

	/**
	 * 
	 * 
	 * 
	 * 获取Float类型值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 * @return 如果该值不能正常转换，会返回ERROR_VALUE
	 */
	public float getFloatProperty(String columnName) {
		return (float) getDoubleProperty(columnName);
	}

	/**
	 * 
	 * 
	 * 
	 * 获取String类型值
	 * 
	 * 
	 * 
	 * @param columnName
	 * 
	 * @return 如果该值不能正常转换，会返回 ""
	 */
	public String getStringProperty(String columnName) {
		Object value = getProperty(columnName);
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Double || value instanceof Float
				|| value instanceof Long || value instanceof Integer
				|| value instanceof Short) {
			return String.valueOf(value);
		} else if (value == null) {
			return "";
		} else {
			return value.toString();
		}
	}

	/**
	 * 
	 * 获取第columnIndex列的值
	 * 
	 * 
	 * 
	 * @param columnIndex
	 * 
	 * @return
	 */
	public Object getProperty(int columnIndex) {
		return indexValueMap.get(columnIndex);
	}

	/**
	 * 
	 * 获取列数
	 * 
	 * 
	 * 
	 * @return
	 */
	public int getSize() {
		return nameValueMap.size();
	}

	/**
	 * 
	 * 获取【name:value】结果集
	 * 
	 * 
	 * 
	 * @return
	 */
	public Map<String, Object> getNameValueMap() {
		return nameValueMap;
	}

	/**
	 * 
	 * 获取【index:value】结果集
	 * 
	 * 
	 * 
	 * @return
	 */
	public Map<Integer, Object> getIndexValueMap() {
		return indexValueMap;
	}

	public boolean isEmpty() {
		return nameValueMap.isEmpty();
	}

	@Override
	public String toString() {
		return "Result [nameValueMap=" + nameValueMap + "]";
	}

	public String getColumnNameByIndex(int columnNum) {
		return columnNameList.get(columnNum);
	}

	public int indexOfColumnName(String columnName) {
		return columnNameList.indexOf(columnName.toLowerCase());
	}

}