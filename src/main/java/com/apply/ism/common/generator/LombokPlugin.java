package com.apply.ism.common.generator;

import com.apply.ism.common.ReplacePatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author xiehao
 */
public class LombokPlugin extends PluginAdapter {

    public LombokPlugin() {
        init();
    }

    private FullyQualifiedJavaType domainClass;

    private PropertySource propertySource;

    private void init() {
        ClassPathResource resource = new ClassPathResource("mybatis-generator.yml");
        YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
        List<PropertySource<?>> propertySources = null;
        try {
            propertySources = sourceLoader.load("mybatis-resource", resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertySource = propertySources.get(0);
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");
        topLevelClass.addImportedType("com.baomidou.mybatisplus.annotation.TableName");

        //添加domain的注解
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addAnnotation("@TableName(\"" + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\")");

        topLevelClass.addImportedType(new FullyQualifiedJavaType("BaseEntity"));
        topLevelClass.setSuperClass(new FullyQualifiedJavaType("BaseEntity"));

        //添加domain的注释
        topLevelClass.addJavaDocLine("/**");
        if (StringUtils.isNotBlank(introspectedTable.getRemarks())) {
            topLevelClass.addJavaDocLine(" * 注释:" + introspectedTable.getRemarks());
        }
        topLevelClass.addJavaDocLine(
                " *\n" +
                        " * @author Leon\n" +
                        " * @date " + date2Str(LocalDate.now()));
        topLevelClass.addJavaDocLine(" */");
        domainClass = topLevelClass.getType();
        if (String.valueOf(true).equals(ReplacePatternUtils.replace("mybatis.generator.createDomain", propertySource))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        //添加domain的注释
        interfaze.addJavaDocLine("/**");
        if (StringUtils.isNotBlank(introspectedTable.getRemarks())) {
            interfaze.addJavaDocLine(" * 注释:" + introspectedTable.getRemarks());
        }
        interfaze.addJavaDocLine(
                " *\n" +
                        " * @author Leon\n" +
                        " * @date " + date2Str(LocalDate.now()));
        interfaze.addJavaDocLine(" */");
        interfaze.addImportedType(new FullyQualifiedJavaType("com.baomidou.mybatisplus.core.mapper.BaseMapper"));
        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
        interfaze.addAnnotation("@Repository");
        interfaze.addImportedType(domainClass);

        interfaze.addSuperInterface(new FullyQualifiedJavaType("BaseMapper<"
                + interfaze.getType().getShortName().split("Mapper")[0]
                + ">"));
        if (String.valueOf(true).equals(ReplacePatternUtils.replace("mybatis.generator.createJavaClient", propertySource))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成getter
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }

    @Override
    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }


    @Override
    public boolean sqlMapBlobColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    private String date2Str(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String actualColumnName = introspectedColumn.getActualColumnName();
        String fieldName = field.getName();
        if (actualColumnName.startsWith("is_")) {
            fieldName = String.valueOf(fieldName.charAt(2)).toLowerCase() + fieldName.substring(3);
            field.setName(fieldName);
            introspectedColumn.setJavaProperty(fieldName);
            field.addAnnotation("@TableField(\"" + actualColumnName + "\")");
            topLevelClass.addImportedType("com.baomidou.mybatisplus.annotation.TableField");
        }
        if (actualColumnName.equals("id")) {
            return false;
//            field.addAnnotation("@TableId(type = IdType.AUTO)");
//            topLevelClass.addImportedType("com.baomidou.mybatisplus.annotation.TableId");
//            topLevelClass.addImportedType("com.baomidou.mybatisplus.annotation.IdType");
        }
        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            java.lang.reflect.Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        isColumnConvert(introspectedTable);
        if (String.valueOf(true).equals(ReplacePatternUtils.replace("mybatis.generator.createMapper", propertySource))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        isColumnConvert(introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        isColumnConvert(introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        isColumnConvert(introspectedTable);
        return true;
    }


    private void isColumnConvert(IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> columns = introspectedTable.getBaseColumns();
        for (IntrospectedColumn column : columns) {
            String javaProperty = column.getJavaProperty();
            if (javaProperty.startsWith("is")) {
                javaProperty = String.valueOf(javaProperty.charAt(2)).toLowerCase() + javaProperty.substring(3);
                column.setJavaProperty(javaProperty);
            }
        }
    }


    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }


    @Override
    public boolean clientBasicCountMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientBasicDeleteMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientBasicInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientBasicSelectOneMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientBasicUpdateMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

}
