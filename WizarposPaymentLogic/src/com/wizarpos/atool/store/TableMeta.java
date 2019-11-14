package com.wizarpos.atool.store;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表结构
 */
public class TableMeta {
	private String tableName;
	private List<String> fieldList = new ArrayList<String>();
	private List<String> fieldNameList = new ArrayList<String>();
	private String primaryKeyName = null;
	
	/**
	 * Note: A table must have a pirmary key. And it's displayed as 'primary key'.
	 * @param name table name
	 */
	public TableMeta(String name) {
		this.tableName = name;
	}
	
	public TableMeta addField(String fieldString) {
		fieldList.add(fieldString);
		
		String fieldName = fieldString.substring(0, fieldString.indexOf(" ")).trim();
		fieldNameList.add(fieldName);
		
		boolean isPrimaryKey = fieldString.contains("primary key");
		if (isPrimaryKey) {
			primaryKeyName = fieldName;
		}
		return this;
	}

	public String toSQLString() {
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists ").append(tableName).append(" (");
		for (int i = 0; i < fieldList.size(); i++) {
			sb.append(fieldList.get(i));
			if (i != fieldList.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append(");");
		return sb.toString();
	}
	
	public String getPrimaryKeyName() {
		return this.primaryKeyName;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	public List<String> getFieldNameList() {
		return this.fieldNameList;
	}
}