package org.mybatis.generator.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Github github.com/benjia
 *
 * @author orange1438
 *         2016/10/11 23:10
 */
public class MybatisServicePlugin extends PluginAdapter {

    private FullyQualifiedJavaType slf4jLogger;
    private FullyQualifiedJavaType slf4jLoggerFactory;
    private FullyQualifiedJavaType serviceType;
    private FullyQualifiedJavaType voType;
    private FullyQualifiedJavaType controllerType;
    private FullyQualifiedJavaType daoType;
    private FullyQualifiedJavaType interfaceType;
    private FullyQualifiedJavaType pojoType;
    private FullyQualifiedJavaType pojoCriteriaType;
    private FullyQualifiedJavaType listType;
    private FullyQualifiedJavaType autowired;
    private FullyQualifiedJavaType service;
    private FullyQualifiedJavaType returnType;
    private String servicePack;
    private String serviceImplPack;
    private String voPackage;
    private String controllerPackage;
    private String project;
    private String pojoUrl;

    private List<Method> methods;
    /**
     * 是否添加注解
     */
    private boolean enableAnnotation = true;
    private boolean enableInsert = false;
    private boolean enableInsertSelective = false;
    private boolean enableDeleteByPrimaryKey = false;
    private boolean enableDeleteByExample = false;
    private boolean enableUpdateByExample = false;
    private boolean enableUpdateByExampleSelective = false;
    private boolean enableUpdateByPrimaryKey = false;
    private boolean enableUpdateByPrimaryKeySelective = false;
    private Element e;

    public MybatisServicePlugin() {
        super();
        // 默认是slf4j
        slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
        slf4jLoggerFactory = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
        methods = new ArrayList<Method>();
    }

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(properties.getProperty("enableAnnotation")))
            enableAnnotation = StringUtility.isTrue(properties.getProperty("enableAnnotation"));

        String enableInsert = properties.getProperty("enableInsert");

        String enableUpdateByExampleSelective = properties.getProperty("enableUpdateByExampleSelective");

        String enableInsertSelective = properties.getProperty("enableInsertSelective");

        String enableUpdateByPrimaryKey = properties.getProperty("enableUpdateByPrimaryKey");

        String enableDeleteByPrimaryKey = properties.getProperty("enableDeleteByPrimaryKey");

        String enableDeleteByExample = properties.getProperty("enableDeleteByExample");

        String enableUpdateByPrimaryKeySelective = properties.getProperty("enableUpdateByPrimaryKeySelective");

        String enableUpdateByExample = properties.getProperty("enableUpdateByExample");

        if (StringUtility.stringHasValue(enableInsert))
            this.enableInsert = StringUtility.isTrue(enableInsert);
        if (StringUtility.stringHasValue(enableUpdateByExampleSelective))
            this.enableUpdateByExampleSelective = StringUtility.isTrue(enableUpdateByExampleSelective);
        if (StringUtility.stringHasValue(enableInsertSelective))
            this.enableInsertSelective = StringUtility.isTrue(enableInsertSelective);
        if (StringUtility.stringHasValue(enableUpdateByPrimaryKey))
            this.enableUpdateByPrimaryKey = StringUtility.isTrue(enableUpdateByPrimaryKey);
        if (StringUtility.stringHasValue(enableDeleteByPrimaryKey))
            this.enableDeleteByPrimaryKey = StringUtility.isTrue(enableDeleteByPrimaryKey);
        if (StringUtility.stringHasValue(enableDeleteByExample))
            this.enableDeleteByExample = StringUtility.isTrue(enableDeleteByExample);
        if (StringUtility.stringHasValue(enableUpdateByPrimaryKeySelective))
            this.enableUpdateByPrimaryKeySelective = StringUtility.isTrue(enableUpdateByPrimaryKeySelective);
        if (StringUtility.stringHasValue(enableUpdateByExample))
            this.enableUpdateByExample = StringUtility.isTrue(enableUpdateByExample);

        servicePack = properties.getProperty("targetPackage");
        voPackage = properties.getProperty("voPackage");
        controllerPackage=properties.getProperty("controllerPackage");
        serviceImplPack = properties.getProperty("implementationPackage");
        project = properties.getProperty("targetProject");

        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();

        if (enableAnnotation) {
            autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
            service = new FullyQualifiedJavaType("org.springframework.stereotype.Service");
        }
        return true;
    }

    /**
     * resultMap 字段增加注释
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elements = element.getElements();
        Iterator<Element> i = elements.iterator();
        while (i.hasNext()){
            e = i.next();
            XmlElement ex = e instanceof XmlElement ? ((XmlElement) e) : null;
            List<Attribute> attributes = ex.getAttributes();
            String columName = "";
            for (Attribute attr:attributes
                 ) {
                if ("column".equals(attr.getName())){
                    columName = attr.getValue();
                    break;
                }
            }
            IntrospectedColumn column = introspectedTable.getColumn(columName);
            StringBuffer sb = new StringBuffer();
            sb.append("<!--");
            if ("id".equals(column.getActualColumnName())){
                sb.append("主键id");
            }else {
                sb.append(column.getRemarks());
            }
            sb.append("-->");
            TextElement text = new TextElement(sb.toString());
            ex.addElement(text);
        }
        this.context.getCommentGenerator().addComment(element);
        return super.sqlMapResultMapWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }


    /**
     * 去除不必要的batch sql
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (element.getName().equals("insert")){
            List<Attribute> attList = element.getAttributes();
            for (Attribute attr: attList
                 ) {
                if (attr.getValue().equals("insertBatch")){
                    return  false;
                }
            }
        }
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }




    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        String table = introspectedTable.getBaseRecordType();
        String tableName = table.replaceAll(this.pojoUrl + ".", "");
        String tableActulName = tableName.substring(0,tableName.length()-2);
        //service interface
        interfaceType = new FullyQualifiedJavaType(servicePack + "." + tableActulName + "Service");

        // dao interface
        daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());

        // logger.info(toLowerCase(daoType.getShortName()));
        //serviceImpl class
        serviceType = new FullyQualifiedJavaType(serviceImplPack + "." + tableActulName + "ServiceImpl");
        //pojo class
        pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);
        //vo class
        voType = new FullyQualifiedJavaType(voPackage+ "." + tableActulName  + "VO");
        controllerType= new FullyQualifiedJavaType(controllerPackage+"."+tableActulName+"Controller");
//      pojoCriteriaType = new FullyQualifiedJavaType(pojoUrl + "." + "Criteria");
//      listType = new FullyQualifiedJavaType("java.util.List");
        Interface interface1 = new Interface(interfaceType);
        TopLevelClass topLevelClass = new TopLevelClass(serviceType);
        TopLevelClass dtoClass = new TopLevelClass(voType);
        TopLevelClass controllerClass = new TopLevelClass(controllerType);
        // 导入必要的类
        addImport(interface1, topLevelClass);

        // 接口
        addService(interface1, introspectedTable, tableName, files);
        // 实现类
        addServiceImpl(topLevelClass, introspectedTable, tableName, files);
        // VO
        addVO(dtoClass,introspectedTable,files);
        //controller
        addController(controllerClass,tableActulName,introspectedTable,files);
//        addLogger(topLevelClass);

        return files;
    }

    //生成controller
    private void addController(TopLevelClass controllerClass,String tableActualName, IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        controllerClass.addAnnotation("@RequestMapping(value=\"/"+toLowerCase(tableActualName)+"\",method=RequestMethod.POST)");
        controllerClass.addAnnotation("@RestController");
        controllerClass.addAnnotation("@Api(tags=\""+introspectedTable.getRemarks()+"\")");
        controllerClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
        controllerClass.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
        controllerClass.addImportedType("javax.annotation.Resource");
        controllerClass.addImportedType("org.springframework.web.bind.annotation.RestController");
        controllerClass.addImportedType("io.swagger.annotations.Api");
        controllerClass.addImportedType(interfaceType);
        Field field = new Field(tableActualName+"Service",interfaceType);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.addAnnotation("@Resource");
        controllerClass.addField(field);
        GeneratedJavaFile file = new GeneratedJavaFile(controllerClass, project, context.getJavaFormatter());
        files.add(file);
    }

    protected String getDateString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 普通PO model对象增加继承MybatisBasePO
     * @param topLevelClass     该类的实例就是表示当前正在生成的类的DOM结构
     * @param introspectedTable 代表的runtime环境，包含了所有context中的配置，一般从这个类中去查询生成对象的一些规则；
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String basePO = "com.raiden.cfds.manager.bean.po.MybatisBasePO";
        topLevelClass.addImportedType(basePO);
        FullyQualifiedJavaType basePOClass = new FullyQualifiedJavaType(basePO);
        topLevelClass.setSuperClass(basePOClass);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 添加dto
     * @param dtoClass
     * @param introspectedTable
     * @param files
     */
    private void addVO(TopLevelClass dtoClass, IntrospectedTable introspectedTable,List<GeneratedJavaFile> files) {
        dtoClass.setVisibility(JavaVisibility.PUBLIC);
        dtoClass.addJavaDocLine("/**");
        dtoClass.addJavaDocLine("* "+introspectedTable.getRemarks());
        dtoClass.addJavaDocLine("*@author "+this.context.getCommentGeneratorConfiguration().getProperty(PropertyRegistry.COMMENT_GENERATOR_AUTHOR));
        dtoClass.addJavaDocLine("*@Date "+getDateString());
        dtoClass.addJavaDocLine("*/");
        dtoClass.addAnnotation("@Data");
        dtoClass.addAnnotation("@ApiModel");
        dtoClass.addImportedType("lombok.Data");
        dtoClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        dtoClass.addImportedType("io.swagger.annotations.ApiModel");
        List<IntrospectedColumn>  columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column:columns
             ) {
            //遍历生成字段
            Field field = new Field(column.getJavaProperty(),column.getFullyQualifiedJavaType());
            field.setVisibility(JavaVisibility.PRIVATE);
            field.addJavaDocLine("/**");
            if ("id".equals(column.getActualColumnName())){
                field.addJavaDocLine("主键id");
            }else {
                field.addJavaDocLine(column.getRemarks());
            }
            field.addJavaDocLine("* "+column.getRemarks());
            field.addJavaDocLine("*/");
            field.addAnnotation("@ApiModelProperty(name=\""+column.getJavaProperty()+"\""+",value=\""+column.getRemarks()+"\")");
            dtoClass.addField(field);
        }
        GeneratedJavaFile file = new GeneratedJavaFile(dtoClass, project, context.getJavaFormatter());
        files.add(file);

    }

    /**
     * 添加接口
     *
     * @param tableName
     * @param files
     */
    protected void addService(Interface interface1, IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {

        interface1.setVisibility(JavaVisibility.PUBLIC);
        Method method = null;
        /*// 添加方法
        Method method = countByExample(introspectedTable, tableName);
        method.removeAllBodyLines();
        interface1.addMethod(method);

        method = selectByPrimaryKey(introspectedTable, tableName);
        method.removeAllBodyLines();
        interface1.addMethod(method);

        method = selectByExample(introspectedTable, tableName);
        method.removeAllBodyLines();
        interface1.addMethod(method);*/

        if (enableDeleteByPrimaryKey) {
            method = getOtherInteger("deleteByPrimaryKey", introspectedTable, tableName, 2);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }
        if (enableUpdateByPrimaryKeySelective) {
            method = getOtherInteger("updateByPrimaryKeySelective", introspectedTable, tableName, 1);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }
        if (enableUpdateByPrimaryKey) {
            method = getOtherInteger("updateByPrimaryKey", introspectedTable, tableName, 1);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }
        if (enableDeleteByExample) {
            method = getOtherInteger("deleteByExample", introspectedTable, tableName, 3);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }
        if (enableUpdateByExampleSelective) {
            method = getOtherInteger("updateByExampleSelective", introspectedTable, tableName, 4);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }
        if (enableUpdateByExample) {
            method = getOtherInteger("updateByExample", introspectedTable, tableName, 4);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }
        if (enableInsert) {
            method = getOtherInsertboolean("insert", introspectedTable, tableName);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }
        if (enableInsertSelective) {
            method = getOtherInsertboolean("insertSelective", introspectedTable, tableName);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }

        GeneratedJavaFile file = new GeneratedJavaFile(interface1, project, context.getJavaFormatter());
        files.add(file);
    }

    /**
     * 添加实现类
     *
     * @param introspectedTable
     * @param tableName
     * @param files
     */
    protected void addServiceImpl(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 设置实现的接口
        topLevelClass.addSuperInterface(interfaceType);
        String tableActulName = tableName.substring(0,tableName.length()-2);
        if (enableAnnotation) {
            topLevelClass.addAnnotation("@Service(\""+toLowerCase(tableActulName)+"Service\")");
            topLevelClass.addAnnotation("@Slf4j");
            topLevelClass.addImportedType("lombok.extern.slf4j.Slf4j");
            topLevelClass.addImportedType(service);
        }
        // 添加引用dao
        addField(topLevelClass);
        // 添加方法
//        topLevelClass.addMethod(countByExample(introspectedTable, tableName));
//        topLevelClass.addMethod(selectByPrimaryKey(introspectedTable, tableName));
//        topLevelClass.addMethod(selectByExample(introspectedTable, tableName));

        /**
         * type 的意义 pojo 1 ;key 2 ;example 3 ;pojo+example 4
         */
        if (enableDeleteByPrimaryKey) {
            topLevelClass.addMethod(getOtherInteger("deleteByPrimaryKey", introspectedTable, tableName, 2));
        }
        if (enableUpdateByPrimaryKeySelective) {
            topLevelClass.addMethod(getOtherInteger("updateByPrimaryKeySelective", introspectedTable, tableName, 1));

        }
        if (enableUpdateByPrimaryKey) {
            topLevelClass.addMethod(getOtherInteger("updateByPrimaryKey", introspectedTable, tableName, 1));
        }
        if (enableDeleteByExample) {
            topLevelClass.addMethod(getOtherInteger("deleteByExample", introspectedTable, tableName, 3));
        }
        if (enableUpdateByExampleSelective) {
            topLevelClass.addMethod(getOtherInteger("updateByExampleSelective", introspectedTable, tableName, 4));
        }
        if (enableUpdateByExample) {
            topLevelClass.addMethod(getOtherInteger("updateByExample", introspectedTable, tableName, 4));
        }
        if (enableInsert) {
            topLevelClass.addMethod(getOtherInsertboolean("insert", introspectedTable, tableName));
        }
        if (enableInsertSelective) {
            topLevelClass.addMethod(getOtherInsertboolean("insertSelective", introspectedTable, tableName));
        }
        // 生成文件
        GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project, context.getJavaFormatter());
        files.add(file);
    }

    /**
     * 添加字段
     *
             * @param topLevelClass
     */
    protected void addField(TopLevelClass topLevelClass) {
        // 添加 dao
        Field field = new Field();
        field.setName(toLowerCase(daoType.getShortName())); // 设置变量名
        topLevelClass.addImportedType(daoType);
        field.setType(daoType); // 类型
        field.setVisibility(JavaVisibility.PRIVATE);
        if (enableAnnotation) {
            field.addAnnotation("@Autowired");
        }
        topLevelClass.addField(field);
    }

    /**
     * 添加方法
     */
    protected Method selectByPrimaryKey(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("selectByPrimaryKey");
        method.setReturnType(pojoType);
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            method.addParameter(new Parameter(type, "key"));
        } else {
            for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
            }
        }
        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        // method.addBodyLine("try {");
        sb.append("return this.");
        sb.append(getDaoShort());
        sb.append("selectByPrimaryKey");
        sb.append("(");
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(");");
        method.addBodyLine(sb.toString());
        // method.addBodyLine("} catch (Exception e) {");
        // method.addBodyLine("logger.error(\"Exception: \", e);");
        // method.addBodyLine("return null;");
        // method.addBodyLine("}");
        return method;
    }

    /**
     * 添加方法
     */
    protected Method countByExample(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("countByExample");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(pojoCriteriaType, "example"));
        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        sb.append("int count = this.");
        sb.append(getDaoShort());
        sb.append("countByExample");
        sb.append("(");
        sb.append("example");
        sb.append(");");
        method.addBodyLine(sb.toString());
        method.addBodyLine("logger.debug(\"count: {}\", count);");
        method.addBodyLine("return count;");
        return method;
    }

    /**
     * 添加方法
     */
    protected Method selectByExample(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("selectByExample");
        method.setReturnType(new FullyQualifiedJavaType("List<" + tableName + ">"));
        method.addParameter(new Parameter(pojoCriteriaType, "example"));
        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        sb.append("return this.");
        sb.append(getDaoShort());
        if (introspectedTable.hasBLOBColumns()) {
            sb.append("selectByExampleWithoutBLOBs");
        } else {
            sb.append("selectByExample");
        }
        sb.append("(");
        sb.append("example");
        sb.append(");");
        method.addBodyLine(sb.toString());
        return method;
    }

    /**
     * 添加方法
     */
    protected Method getOtherInteger(String methodName, IntrospectedTable introspectedTable, String tableName, int type) {
        Method method = new Method();
        method.setName(methodName);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        String params = addParams(introspectedTable, method, type);
        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        // method.addBodyLine("try {");
        sb.append("return this.");
        sb.append(getDaoShort());
        if (introspectedTable.hasBLOBColumns()
                && (!"updateByPrimaryKeySelective".equals(methodName) && !"deleteByPrimaryKey".equals(methodName)
                && !"deleteByExample".equals(methodName) && !"updateByExampleSelective".equals(methodName))) {
            sb.append(methodName + "WithoutBLOBs");
        } else {
            sb.append(methodName);
        }
        sb.append("(");
        sb.append(params);
        sb.append(");");
        method.addBodyLine(sb.toString());
        return method;
    }

    /**
     * 添加方法
     */
    protected Method getOtherInsertboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName(methodName);
        method.setReturnType(returnType);
        method.addParameter(new Parameter(pojoType, "record"));
        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        if (returnType == null) {
            sb.append("this.");
        } else {
            sb.append("return this.");
        }
        sb.append(getDaoShort());
        sb.append(methodName);
        sb.append("(");
        sb.append("record");
        sb.append(");");
        method.addBodyLine(sb.toString());
        return method;
    }

    /**
     * type 的意义 pojo 1 key 2 example 3 pojo+example 4
     */
    protected String addParams(IntrospectedTable introspectedTable, Method method, int type1) {
        switch (type1) {
            case 1:
                method.addParameter(new Parameter(pojoType, "record"));
                return "record";
            case 2:
                if (introspectedTable.getRules().generatePrimaryKeyClass()) {
                    FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
                    method.addParameter(new Parameter(type, "key"));
                } else {
                    for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
                        FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                        method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
                    }
                }
                StringBuffer sb = new StringBuffer();
                for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
                    sb.append(introspectedColumn.getJavaProperty());
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                return sb.toString();
            case 3:
                method.addParameter(new Parameter(pojoCriteriaType, "example"));
                return "example";
            case 4:

                method.addParameter(0, new Parameter(pojoType, "record"));
                method.addParameter(1, new Parameter(pojoCriteriaType, "example"));
                return "record, example";
            default:
                break;
        }
        return null;
    }

    protected void addComment(JavaElement field, String comment) {
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        comment = comment.replaceAll("\n", "<br>\n\t * ");
        sb.append(comment);
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
    }

    /**
     * 添加方法
     */
    protected void addMethod(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setSuccess");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "success"));
        method.addBodyLine("this.success = success;");
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        method.setName("isSuccess");
        method.addBodyLine("return success;");
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setMessage");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "message"));
        method.addBodyLine("this.message = message;");
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("getMessage");
        method.addBodyLine("return message;");
        topLevelClass.addMethod(method);
    }

    /**
     * 添加方法
     */
    protected void addMethod(TopLevelClass topLevelClass, String tableName) {
        Method method2 = new Method();
        for (int i = 0; i < methods.size(); i++) {
            Method method = new Method();
            method2 = methods.get(i);
            method = method2;
            method.removeAllBodyLines();
            method.removeAnnotation();
            StringBuilder sb = new StringBuilder();
            sb.append("return this.");
            sb.append(getDaoShort());
            sb.append(method.getName());
            sb.append("(");
            List<Parameter> list = method.getParameters();
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j).getName());
                sb.append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append(");");
            method.addBodyLine(sb.toString());
            topLevelClass.addMethod(method);
        }
        methods.clear();
    }

    /**
     * BaseUsers to baseUsers
     *
     * @param tableName
     * @return
     */
    protected String toLowerCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * BaseUsers to baseUsers
     *
     * @param tableName
     * @return
     */
    protected String toUpperCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 导入需要的类
     */
    private void addImport(Interface interfaces, TopLevelClass topLevelClass) {
//        interfaces.addImportedType(pojoType);
//        interfaces.addImportedType(pojoCriteriaType);
//        interfaces.addImportedType(listType);
        topLevelClass.addImportedType(daoType);
        topLevelClass.addImportedType(interfaceType);
//        topLevelClass.addImportedType(pojoType);
//        topLevelClass.addImportedType(pojoCriteriaType);
//        topLevelClass.addImportedType(listType);
//        topLevelClass.addImportedType(slf4jLogger);
//        topLevelClass.addImportedType(slf4jLoggerFactory);
        if (enableAnnotation) {
            topLevelClass.addImportedType(service);
            topLevelClass.addImportedType(autowired);
        }
    }

    /**
     * 导入logger
     */
    private void addLogger(TopLevelClass topLevelClass) {
        Field field = new Field();
        field.setFinal(true);
        field.setInitializationString("LoggerFactory.getLogger(" + topLevelClass.getType().getShortName() + ".class)"); // 设置值
        field.setName("logger"); // 设置变量名
        field.setStatic(true);
        field.setType(new FullyQualifiedJavaType("Logger")); // 类型
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);
    }

    private String getDaoShort() {
        return toLowerCase(daoType.getShortName()) + ".";
    }

    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        returnType = method.getReturnType();
        return true;
    }
}
