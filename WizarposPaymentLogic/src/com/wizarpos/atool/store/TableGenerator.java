package com.wizarpos.atool.store;

import java.util.ArrayList;
import java.util.List;

/**
 * 表生成器
 */
public abstract class TableGenerator {
	
	protected List<TableMeta> tableList = new ArrayList<TableMeta>();
	
	public abstract void init();

	public List<TableMeta> getTableMetaList() {
		return this.tableList;
	}
	
}
