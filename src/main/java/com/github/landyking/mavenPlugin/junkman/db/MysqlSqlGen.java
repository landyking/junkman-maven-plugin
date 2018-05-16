package com.github.landyking.mavenPlugin.junkman.db;

import com.github.landyking.mavenPlugin.junkman.utils.MyAssert;
import com.github.landyking.mavenPlugin.junkman.utils.Texts;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by landy on 2017/12/7.
 */
public class MysqlSqlGen {
    private static String getColumnType(String dbType,String colType, Integer colLen, Integer decimalLen) {
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

    public void xml2DDL(Log log, File databaseXmlFile, File ddlSqlFile) throws ConfigurationException, URISyntaxException, FileNotFoundException {
        XMLConfiguration cfg = new XMLConfiguration(databaseXmlFile);
        List<HierarchicalConfiguration> tables = cfg.configurationsAt("tables.table");
        log.info("检测到" + tables.size() + "张表");
        log.info("#######################");
        for (HierarchicalConfiguration tb : tables) {
            log.info(tb.getString("[@name]") + " : " + tb.getString("[@desc]"));
        }
        log.info("#######################");
        PrintWriter out = new PrintWriter(ddlSqlFile);
        log.info("开始生成mysql建表语句:" + ddlSqlFile.getAbsolutePath());
        for (HierarchicalConfiguration tb : tables) {
            String tableName = tb.getString("[@name]");
            String tableDesc = tb.getString("[@desc]");
            StringBuilder sb = new StringBuilder();
            sb.append("/*!50001 DROP TABLE IF EXISTS `" + tableName + "`*/;\r\n");
            sb.append("create table ");
            sb.append(tableName);
            sb.append(" (\r\n");
            List<HierarchicalConfiguration> columns = tb.configurationsAt("columns.column");
            int flag = 0;
            for (HierarchicalConfiguration col : columns) {
                flag++;
                sb.append("    ");
                String colName = col.getString("[@name]");
                String dbType = col.getString("[@dbtype]",null);
                String colType = col.getString("[@type]",null);
                Integer colLen = col.getInteger("[@len]", null);
                Integer decimalLen = col.getInteger("[@decimalLen]", null);
                String colDesc = col.getString("[@desc]");
                String colDictCode = col.getString("[@dictCode]");
                String colForeignKey = col.getString("[@foreignKey]");
                StringBuilder comment = new StringBuilder("'");
                comment.append(colDesc);
                if (Texts.hasLength(colDictCode)) {
                    comment.append("#对应字典" + colDictCode);
                }
                if (Texts.hasLength(colForeignKey)) {
                    comment.append("#关联字段" + colForeignKey);
                }
                comment.append("'");
                boolean colNullable = col.getBoolean("[@nullable]");
                boolean colPrimaryKey = col.getBoolean("[@primaryKey]");
                sb.append(colName);
                sb.append(" " + getColumnType(dbType,colType, colLen, decimalLen));
                sb.append(" " + (colNullable ? "NULL" : "NOT NULL"));
                if (colPrimaryKey) {
                    sb.append(" PRIMARY KEY");
                }
                sb.append(" comment " + comment.toString());
                if (flag < columns.size()) {
                    sb.append(",");
                }
                sb.append("\r\n");
            }
            sb.append(");\r\n");
            sb.append("alter table " + tableName + " comment  '" + tableDesc + "';\r\n");
            out.println(sb.toString());
        }
        out.flush();
        out.close();
        log.info("生成结束!!!");
    }
}
