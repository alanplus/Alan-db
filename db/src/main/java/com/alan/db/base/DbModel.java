package com.alan.db.base;

import com.alan.db.table.Table;
import com.alan.db.table.TableFactory;


/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：<功能简述>
 */
public class DbModel {


    public Table getTable() {
        return TableFactory.getTable(this.getClass());
    }


}
