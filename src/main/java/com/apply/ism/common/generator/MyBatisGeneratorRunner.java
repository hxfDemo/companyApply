package com.apply.ism.common.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.google.common.base.Splitter;
import com.apply.ism.common.ReplacePatternUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author xiehao
 * @date 2019-07-29
 */
public class MyBatisGeneratorRunner {
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
    @SneakyThrows
    public static void main(String[] args) {
        List<String> warnings = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("mybatis-generator.yml");
        YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
        List<PropertySource<?>> propertySources = sourceLoader.load("mybatis-resource", resource);
        PropertySource propertySource = propertySources.get(0);
        String tableName = scanner("表名，多个英文逗号分割");
        List<String> tableList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(tableName);
        if (tableList == null || tableList.size() == 0) {
            return;
        }

        boolean exampleStatementEnabled = Boolean.valueOf(ReplacePatternUtils.replace("mybatis.generator.file", propertySource));
        String generateFile = ReplacePatternUtils.replace("mybatis.generator.file", propertySource);
        String classPathEntry = ReplacePatternUtils.replace("mybatis.generator.classPathEntry", propertySource);
        String targetProject = ReplacePatternUtils.replace("mybatis.generator.targetProject", propertySource);
        String daoTargetPackage = ReplacePatternUtils.replace("mybatis.generator.daoTargetPackage", propertySource);
        String domainTargetProject = ReplacePatternUtils.replace("mybatis.generator.domainTargetProject", propertySource);
        String domainTargetPackage = ReplacePatternUtils.replace("mybatis.generator.domainTargetPackage", propertySource);
        String mapperTargetPackage = ReplacePatternUtils.replace("mybatis.generator.mapperTargetPackage", propertySource);
        String mapperTargetProject = ReplacePatternUtils.replace("mybatis.generator.mapperTargetProject", propertySource);

        String serviceTargetPackage = ReplacePatternUtils.replace("mybatis.generator.serviceTargetPackage",propertySource);

        String jdbcUrl = propertySource.getProperty("mybatis.generator.jdbc-url").toString();
        String driverClass = propertySource.getProperty("mybatis.generator.jdbc-driver").toString();
        String userId = propertySource.getProperty("mybatis.generator.jdbc-userId").toString();
        String password = propertySource.getProperty("mybatis.generator.jdbc-password").toString();

        File configFile = new File(generateFile);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        config.addClasspathEntry(classPathEntry);
        Context context = config.getContext("context1");

        context.addProperty("autoDelimitKeywords", "true");
        context.addProperty("beginningDelimiter", "`");
        context.addProperty("endingDelimiter", "`");
        context.addProperty("javaFormatter", "org.mybatis.generator.api.dom.DefaultJavaFormatter");
        context.addProperty("xmlFormatter", "org.mybatis.generator.api.dom.DefaultXmlFormatter");
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
        context.addPluginConfiguration(pluginConfiguration);

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(jdbcUrl);
        jdbcConnectionConfiguration.setDriverClass(driverClass);
        jdbcConnectionConfiguration.setUserId(userId);
        jdbcConnectionConfiguration.setPassword(password);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = context.getSqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(mapperTargetProject);
        sqlMapGeneratorConfiguration.setTargetPackage(mapperTargetPackage);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(targetProject);
        javaClientGeneratorConfiguration.setTargetPackage(daoTargetPackage);

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(domainTargetProject);
        javaModelGeneratorConfiguration.setTargetPackage(domainTargetPackage);



        context.getTableConfigurations().clear();
        for (String table : tableList) {
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setTableName(table);
            tableConfiguration.setSchema("company");
            tableConfiguration.setDomainObjectName(convertDomain(table));
            tableConfiguration.setCountByExampleStatementEnabled(exampleStatementEnabled);
            tableConfiguration.setUpdateByExampleStatementEnabled(exampleStatementEnabled);
            tableConfiguration.setDeleteByExampleStatementEnabled(exampleStatementEnabled);
            tableConfiguration.setSelectByExampleStatementEnabled(exampleStatementEnabled);

            context.addTableConfiguration(tableConfiguration);
        }

        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    private static String convertDomain(String tableName) {
        String[] values = tableName.split("_");
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            builder.append(String.valueOf(value.charAt(0)).toUpperCase())
                    .append(value.substring(1));
        }
        return builder.toString();
    }

}
