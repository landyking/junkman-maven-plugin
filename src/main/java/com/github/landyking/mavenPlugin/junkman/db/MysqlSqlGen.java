package com.github.landyking.mavenPlugin.junkman.db;

import com.github.landyking.mavenPlugin.junkman.utils.MyAssert;
import com.github.landyking.mavenPlugin.junkman.utils.Texts;
import com.google.common.collect.LinkedHashMultimap;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

/**
 * Created by landy on 2017/12/7.
 */
public class MysqlSqlGen extends SqlGen {
    @Override
    protected void genUniqueKeySql(StringBuilder sb, String tableName, LinkedHashMultimap<String, String> uniqueCols) {
        for (String key : uniqueCols.keySet()) {
            Set<String> cols = uniqueCols.get(key);
            sb.append("alter table " + tableName + " add unique key(" + StringUtils.join(cols, ",") + ");\r\n");
        }
    }

    @Override
    protected String genColumnCommentWhenCreate(String tableName, String colName, String colDesc, String colDictCode, String colForeignKey) {
        StringBuilder comment = new StringBuilder("'");
        comment.append(colDesc);
        if (Texts.hasLength(colDictCode)) {
            comment.append("#对应字典" + colDictCode);
        }
        if (Texts.hasLength(colForeignKey)) {
            comment.append("#关联字段" + colForeignKey);
        }
        comment.append("'");
        return " comment " + comment.toString();
    }

    @Override
    protected void genPrimaryKeySql(StringBuilder sb, String tableName, Set<String> primaryKeyNames) {
        String columns = StringUtils.join(primaryKeyNames, ",");
        sb.append("alter table " + tableName + " add primary key (" + columns + ");\r\n");
    }

    @Override
    protected void genTableComment(StringBuilder sb, String tableName, String tableDesc) {
        sb.append("alter table " + tableName + " comment '" + tableDesc + "';\r\n");
    }

    @Override
    protected void genColumnCommentAfterCreate(StringBuilder sb, String tableName, String colName, String colDesc, String colDictCode, String colForeignKey) {

    }

    @Override
    protected String getColumnType(String dbType, String colType, Integer colLen, Integer decimalLen) {
        if (Texts.hasText(dbType)) {
            return dbType;
        }
        MyAssert.notNull(colType);
        if (colType.equalsIgnoreCase("long")) {
            return "bigint";
        }
        if (colType.equalsIgnoreCase("text")) {
            MyAssert.notNull(colLen);
            return "nvarchar(" + colLen + ")";
        }
        if (colType.equalsIgnoreCase("int")) {
            return "int";
        }
        if (colType.equalsIgnoreCase("longtext")) {
            return "text";
        }
        if (colType.equalsIgnoreCase("float")) {
            MyAssert.notNull(colLen);
            MyAssert.notNull(decimalLen);
            return "DECIMAL(" + colLen + "," + decimalLen + ")";
        }
        throw new IllegalArgumentException("未知的列类型:" + colType);
    }

    @Override
    protected void genDropTableSql(StringBuilder sb, String tableName) {
        sb.append("/*!50001 DROP TABLE IF EXISTS `" + tableName + "`*/;\r\n");
    }

    @Override
    protected String databaseName() {
        return "MySQL";
    }
}
