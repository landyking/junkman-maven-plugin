package com.github.landyking.mavenPlugin.junkman.db;

import com.github.landyking.mavenPlugin.junkman.utils.Texts;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

/**
 * Created by landy on 2018/6/6.
 */
public abstract class SqlGen {
    public void xml2DDL(Log log, File databaseXmlFile, File ddlSqlFile) throws Exception {
        XMLConfiguration cfg = new XMLConfiguration();
        cfg.setAttributeSplittingDisabled(true);
        cfg.load(databaseXmlFile);
        List<HierarchicalConfiguration> tables = cfg.configurationsAt("tables.table");
        log.info("检测到" + tables.size() + "张表");
        log.info("#######################");
        for (HierarchicalConfiguration tb : tables) {
            log.info(tb.getString("[@name]") + " : " + tb.getString("[@desc]"));
        }
        log.info("#######################");
        PrintWriter out = new PrintWriter(ddlSqlFile, "UTF-8");
        log.info("开始生成" + databaseName() + "建表语句:" + ddlSqlFile.getAbsolutePath());
        for (HierarchicalConfiguration tb : tables) {
            String tableName = tb.getString("[@name]");
            String tableDesc = tb.getString("[@desc]");
            StringBuilder sb = new StringBuilder();
            genDropTableSql(sb, tableName);
            sb.append("create table ");
            sb.append(tableName);
            sb.append(" (\r\n");
            List<HierarchicalConfiguration> columns = tb.configurationsAt("columns.column");
            int flag = 0;
            Set<String> primaryKeyNames = Sets.newHashSet();
            LinkedHashMultimap<String, String> uniqueCols = LinkedHashMultimap.create();
            for (HierarchicalConfiguration col : columns) {
                flag++;
                sb.append("    ");
                String colName = col.getString("[@name]");
                String dbType = col.getString("[@dbtype]", null);
                String colType = col.getString("[@type]", null);
                Integer colLen = col.getInteger("[@len]", null);
                String colDesc = col.getString("[@desc]");
                String colDictCode = col.getString("[@dictCode]");
                String colForeignKey = col.getString("[@foreignKey]");
                String uniqueName = col.getString("[@unique]");
                Integer decimalLen = col.getInteger("[@decimalLen]", null);
                String defaultValue = col.getString("[@default]");
                boolean colNullable = col.getBoolean("[@nullable]");
                boolean colPrimaryKey = col.getBoolean("[@primaryKey]");
                sb.append(colName);
                sb.append(" " + getColumnType(dbType, colType, colLen, decimalLen));
                if (Texts.hasText(defaultValue)) {
                    sb.append(" default " + genDefaultValue(defaultValue));
                }
                sb.append(" " + (colNullable ? "NULL" : "NOT NULL"));
                if (colPrimaryKey) {
                    primaryKeyNames.add(colName);
                }
                if (Texts.hasText(uniqueName)) {
                    uniqueCols.put(uniqueName, colName);
                }
                sb.append(genColumnCommentWhenCreate(tableName, colName, colDesc, colDictCode, colForeignKey));
                if (flag < columns.size()) {
                    sb.append(",");
                }
                sb.append("\r\n");
            }
            sb.append(");\r\n");
            genPrimaryKeySql(sb, tableName, primaryKeyNames);
            genUniqueKeySql(sb, tableName, uniqueCols);
            genTableComment(sb, tableName, tableDesc);
            for (HierarchicalConfiguration col : columns) {
                String colName = col.getString("[@name]");
                String colDesc = col.getString("[@desc]");
                String colDictCode = col.getString("[@dictCode]");
                String colForeignKey = col.getString("[@foreignKey]");
                genColumnCommentAfterCreate(sb, tableName, colName, colDesc, colDictCode, colForeignKey);
            }
            out.println(sb.toString());
        }
        out.flush();
        out.close();
        log.info("生成结束!!!");
    }

    protected String genDefaultValue(String defaultValue) {
        if (defaultValue.indexOf('(') > -1) {
            return defaultValue;
        }else{
            return "'"+defaultValue+"'";
        }
    }

    protected abstract void genUniqueKeySql(StringBuilder sb, String tableName, LinkedHashMultimap<String, String> uniqueCols) throws InterruptedException;

    protected abstract String genColumnCommentWhenCreate(String tableName, String colName, String colDesc, String colDictCode, String colForeignKey);

    protected abstract void genPrimaryKeySql(StringBuilder sb, String tableName, Set<String> primaryKeyNames);

    protected abstract void genTableComment(StringBuilder sb, String tableName, String tableDesc);

    protected abstract void genColumnCommentAfterCreate(StringBuilder sb, String tableName, String colName, String colDesc, String colDictCode, String colForeignKey);


    protected abstract String getColumnType(String dbType, String colType, Integer colLen, Integer decimalLen);

    protected abstract void genDropTableSql(StringBuilder sb, String tableName);

    protected abstract String databaseName();
}
