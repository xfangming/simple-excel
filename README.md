#simple-excel简介
###当前版本：v2.2.0


>友情提示
<br/>
&nbsp;&nbsp;&nbsp;&nbsp;点击下载使用指南：
    <a href="https://tobiasy.oss-cn-beijing.aliyuncs.com/file/simple-excel.docx">下载</a><br>
&nbsp;&nbsp;&nbsp;&nbsp;在线指南文件：
    <a href="https://dev.tencent.com/s/abd36ee0-c697-4177-a238-43e6bf235431">查看</a><br>
&nbsp;&nbsp;&nbsp;&nbsp;博客园：
    <a href="https://www.cnblogs.com/tobiasy/articles/11310508.html">点击进入</a>

##一、准备工作

###1、导出模板配置文件

<p align="justify"><span style="font-family: 宋体;">　　配置文件样板如下：</span></p>

> 简易配置[simple-excel.xml]: https://dev.tencent.com/s/abd36ee0-c697-4177-a238-43e6bf235431

> 复杂配置[complex-excel.xml]: https://dev.tencent.com/s/abd36ee0-c697-4177-a238-43e6bf235431

###2、实体类

<p align="justify">&nbsp;</p>
User.class: 
    
    public class User {
        //编号   姓名  年龄  性别  出生日期     爱好
        private Integer id;
        private Integer code;
        private String name;
        private int age;
        private SexEnum sex;
        private Date birthday;
        private String favourite;
        //setters and getters
    }
    
SexEnum.class: 
    
    public enum SexEnum {
        MALE(1, "男"),
        FEMALE(2, "女"),
        OTHER(3, "其他");
        SexEnum(Integer code, String name){
            this.code = code;
            this.name = name;
        }
        private Integer code;
        private String name;
     
        //..getters and setters..
    }
    

##二、Excel导出

###1、纯模板方式

####1）、定制表头模板

<p align="justify">　　先定义全局变量（下文中会用到）：</p>

    private String[] attrs = new String[]{"code", "name", "sex", "age", "birthday", "favourite"};
    private String[] titles = new String[]{"编码", "姓名", "性别", "年龄", "出生年月", "爱好"};
    private Function<Student, Object>[] functions = ArrayUtils.asArray(
            Student::getCode, Student::getName,
            (u) -> u.getSex().getName(),
            Student::getAge,
            (u) -> DateUtils.format(u.getBirthday(), DateConst.SIMPLE_DATE_PATTERN),
            (u) -> u.getFavourite().getValue());
    private List<Student> list = ObjectData.getStudentList(60000);

代码：
   
生成默认模板：
 
    public void buildTemplate() {
        new ExportCreator<User>()
                .template("测试API模板", titles)
                .buildTemplate()
                .toExcel(new File("F:/test/excel/user-template.xls"));
    }

自定义生成样式模板：    
    
    public void buildStyleTemplate() {
        new ExportCreator<User>()
                .template(titles, new ExcelFont().setColor((short)20).setBold(true))
                .buildTemplate()
                .toExcel(new File("F:/test/excel-test/user-template1.xls"));
    }
    
                    

<p align="justify">&nbsp;</p>

####2）、定制复杂模板

复杂模板的定制可通过XML配置，&lt;tr&gt; &lt;td&gt;标签基本上跟HTML语言一致，可设置跨行跨列等基本操作，可根据任何用户个性化需求定制相应的模板，代码：

    public void buildTemplateXml() {
        new ExportCreator<User>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-user.xml"))
                .type(ExcelTypeEnum.XLS)
                .build().
                toExcel(new File("F:/test/excel/simple-user.xls"));
    }
    
<p align="justify">处理后的excel文件如图2-1-1：</p>
<p align="justify">&nbsp;</p>
<p style="text-align: center;" align="justify">&nbsp;<img src="https://dev.tencent.com/api/share/image/fa0f1253-f2ed-4d16-8bee-97a8e28ba67d" alt="" /></p>
<p style="text-align: center;" align="center"><span style="font-family: 仿宋;">图</span> 2-1-1</p>
<p align="justify">　　模板样式文件导出既然这么灵活，那我们又如何在模板中把需要的数据添加上去呢？</p>

###2、添加导出数据

####1) 属性模板方式
    
<p align="justify"><span style="font-family: 宋体;">　　代码：</span></p>

    public void buildAttrTemplate() {
        new ExportCreator<User>()
                .template("测试API模板", titles)
                .header(attrs)
                .body(list)
                .buildTemplate()
                .toExcel(new File("F:/test/excel/user-template.xls"));
    }

<p>&nbsp;</p>


####2) 属性XML方式
    
<p align="justify"><span style="font-family: 宋体;">　　代码：</span></p>

    public void buildXmlAttr() {
        List<User> list = ObjectData.getStudentList(100);
        new ExportCreator<User>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-user.xml"))
                .header(attrs)
                .body(list, Position.of(0, 3, 0))
                .type(ExcelTypeEnum.XLSX)
                .build()
                .toExcel(new File("F:/test/excel-test/user-attr.xls"));

    }

<p>&nbsp;</p>

####(3) 函数XML方式

<p align="justify"><span style="font-family: 宋体;">　　我们先看代码：</span></p>

    public void buildWithXmlFunction() {
        new ExportCreator<User>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-user.xml"))
                .header(functions)
                .body(list, Position.of(0, 3, 0))
                .type(ExcelTypeEnum.XLSX)
                .build().
                toExcel(new File("F:/test/excel-test/user-function.xls"));
    }

<p align="justify">　　跟上述方式唯一不同的是头部属性参数。</p>

####(4) 纯注解方式

<p align="justify"><span style="font-family: 宋体;">　　我们先看代码：</span></p>

    public void buildOnlyAnnotation() {
        new ExportCreator<User>()
                .body(list)
                .buildOnlyAnnotation()
                .toExcel(new File("F:/test/excel-test/student-template.xls"));
    }

<p>&nbsp;</p>


<p align="justify">　　咋一看，怎么只有导出数据，那么数据是如何对应到excel文件中的呢？</p>
<p align="justify">　　其实还需要在集合数据对象中设置对应参数：</p>

    @ExportUseAnnotation(name = "用户信息表", firstRow = 2)
    public class User {
        @ExcelField(name = "姓名", index = 1)
        private String userName;
        private String password;
        @ExcelField(name = "编码", index = 0)
        private String code;
        @ExcelField(name = "性别", index = 2)
        private SexEnum sex;
        @ExcelField(name = "年龄", index = 3, function = "TO_INTEGER")
        private Integer age;
        @ExcelField(name = "出生年月", index = 4, function = "TO_DATE", dateFormat = "yyyy/MM/dd")
        private Date birthday;

<p>&nbsp;</p>
<p align="justify">
　　原来这是通过注解标识字段并且设置好其索引数据，这样自然省去了属性参数的调用申明。表头信息使用快捷模板
导出文件依旧和图2-2一致。</p>

####(5) 注解XML方式

<p align="justify"><span style="font-family: 宋体;">　　我们先看代码：</span></p>

    public void buildByAnnotation() {
        new ExportCreator<User>()
                .xmlFile(ClassLoaderUtils.getLoaderFile("simple-student.xml"))
                .body(list)
                .type(ExcelTypeEnum.XLSX)
                .buildXmlAnnotation()
                .toExcel(new File("F:/test/excel/student-anno.xls"));
    }

<p>&nbsp;</p>
<p align="justify">　　这里，XML文件配置中的body部分转移成为注解配置，因而注解也只需要@ExcelField，此时xml文件的作用只有生成模板，而注解则是对应excel列数据。</p>

####(5) 导出时的数据跨列问题

　　通过上面案例发现，如果模板是复杂配置，导出模板如图2-2-1：<br>
    
<img src="https://dev.tencent.com/api/share/image/25f9998c-672e-41bc-a647-8d866dab9613" alt="" /><br>
    图 2-2-1<br>
　　注意图中红色标注部分均跨了两个列，如果按照我们上面的逻辑处理，导出数据后会是这样的，详见图2-2-2：<br>

   <img src="https://dev.tencent.com/api/share/image/9d56856b-88a9-46ac-8a10-343aa07b1793" alt="" /><br>
    图2-2-2<br>
　　
    这时我们会发现，数据因为跨列的问题错位了，那么如何解决呢？
　　其实解决办法也很简单，只要我们配置好每一个属性对应的列数就好了（缺省值为1）：

    
    @ExcelField(index = 3, columnSize = 2)
    private int age;

    @ExcelField(index = 2, columnSize = 2)
    private SexEnum sex;

    @ExcelField(index = 4, columnSize = 2)
    private Date birthday;

<p>&nbsp;</p>
<p align="justify">&nbsp;此时我们继续操作，导出后发现这个问题已经完美的解决了，如图2-2-3所示：</p>
<p style="text-align: center;">&nbsp;<img src="https://dev.tencent.com/api/project/4615419/files/5909978/imagePreview" alt="" /></p>
<p style="text-align: center;" align="center"><span style="font-family: 仿宋;">图</span> 2-2-3</p>
<p align="justify">&nbsp;</p>
<p align="justify"><span style="font-family: 宋体;">导出的模板几乎很完美，至于如何设置导出中的时间格式，后面章节将会仔细说明。</span></p>
<p align="justify"><span style="font-family: 宋体;">到这儿</span>excel<span style="font-family: 宋体;">导出基本讲解完了。</span></p>
<p align="justify">&nbsp;</p>
<p align="justify">&nbsp;</p>

##三、Excel导入

###1、返回List&lt;Map&gt;数据集合

<p align="justify"><span style="font-family: 宋体;">　　我们选择图</span>2-2<span style="font-family: 宋体;">的</span><span style="font-family: Calibri;">excel</span><span style="font-family: 宋体;">导出结果文件，数据条数是</span><span style="font-family: Calibri;">200</span><span style="font-family: 宋体;">条，代码：</span></p>
<p align="justify">&nbsp;&nbsp;&nbsp;</p>
<div class="cnblogs_Highlighter">
<pre class="brush:csharp;gutter:true;">public void getDataTest() {
        File file = new File("F:/test/simple-user.xls");//excel文件
        Integer firstRow = 4;//开始行
        List&lt;Map&lt;Integer, String&gt;&gt; list = ImportBuilder.getData(file, firstRow);
        System.out.println(list.size());
        list.forEach(System.out::println);
}
</pre>
</div>
<p>&nbsp;</p>
<p align="justify"><span style="font-family: 宋体;">　　控制台打印如下：</span></p>
<p align="justify">200</p>
<p align="justify">{0=0, 1=admin0, 2=<span style="font-family: 宋体;">男</span><span style="font-family: Calibri;">, 3=18, 4=2019-07-16, 5=</span><span style="font-family: 宋体;">篮球</span><span style="font-family: Calibri;">}</span></p>
<p align="justify">{0=1, 1=admin1, 2=<span style="font-family: 宋体;">女</span><span style="font-family: Calibri;">, 3=19, 4=2019-07-16, 5=</span><span style="font-family: 宋体;">足球</span><span style="font-family: Calibri;">}</span></p>
<p align="justify">{0=2, 1=admin2, 2=<span style="font-family: 宋体;">男</span><span style="font-family: Calibri;">, 3=20, 4=2019-07-16, 5=</span><span style="font-family: 宋体;">篮球</span><span style="font-family: Calibri;">}</span></p>
<p align="justify">{0=3, 1=admin3, 2=<span style="font-family: 宋体;">女</span><span style="font-family: Calibri;">, 3=21, 4=2019-07-16, 5=</span><span style="font-family: 宋体;">乒乓球</span><span style="font-family: Calibri;">}</span></p>
<p align="justify">{0=4, 1=admin4, 2=<span style="font-family: 宋体;">男</span><span style="font-family: Calibri;">, 3=22, 4=2019-07-16, 5=</span><span style="font-family: 宋体;">篮球</span><span style="font-family: Calibri;">}</span></p>
<p align="justify">{0=5, 1=admin5, 2=<span style="font-family: 宋体;">女</span><span style="font-family: Calibri;">, 3=23, 4=2019-07-16, 5=</span><span style="font-family: 宋体;">足球</span><span style="font-family: Calibri;">}</span></p>
<p align="justify">......</p>
<p align="justify"><span style="font-family: 宋体;">　　数据条数确实是</span>200<span style="font-family: 宋体;">条，而且单元格中的数据都获取到了，但是数据类型均是</span><span style="font-family: Calibri;">String</span><span style="font-family: 宋体;">，我们要用数据时，一般情况下都会将数据放置到对象中，然后再处理对象，那么如何将数据直接装配到对象集合中呢？且看下文。</span></p>

###2、返回List&lt;[对象]&gt;数据集合

####(1) 属性函数方式

<p align="justify">　　先定义全局变量（下文中会用到）：</p>
    
    private String[] attrs = new String[]{"code", "name", "sex", "age", "birthday"};//①
    private Function<String, Object>[] functions = ExcelHelper.asArray(
            TO_INTEGER, TO_STRING,
            toEnumName(SexEnum.class),
            TO_INTEGER,
            (s) -> TO_DATE_FORMAT.apply(s, DateConst.SIMPLE_DATE_PATTERN)
    );//②
    private BiConsumer<Student, String>[] setters = ArrayUtils.asArray(
            (t, o) -> t.setCode(ExcelHelper.parse(o, Integer.class)),
            (t, o) -> t.setName(ExcelHelper.parse(o, String.class)),
            (t, o) -> t.setSex((SexEnum) EnumUtils.nameInstance(SexEnum::values, ExcelHelper.parse(o, String.class))),
            (t, o) -> t.setAge(ExcelHelper.parse(o, Integer.class)),
            (t, o) -> t.setBirthday(DateUtils.parse(o, DateConst.SIMPLE_DATE_PATTERN))
    );//③
    
   ① 属性名称数组<br>
   ② 属性转化内置函数，详见ExcelFunction<br>
   ③ 属性setter函数及其转化声明

<p align="justify">　　即导入的属性等对应信息在导入函数调用时指定；代码：</p>
    
    public void toList() {
        File file = new File("F:/test/excel-test/user-attr.xls");
        new ImportCreator<User>()
                .header(attrs)
                .body(functions, Position.of(0, 10, 0))
                .inputFile(file).type(ExcelTypeEnum.XLSX)
                .build(User::new)
                .toList()
                .forEach(System.out::println);
    }
    

<p align="justify"><span style="font-family: 宋体;">　　控制台打印如下：</span></p>
<p align="justify">200</p>
<p align="justify">Student{code=0, name='admin0', age=18, sex='MALE', birthday=Tue Jul 16 00:00:00 CST 2019, favourite='BASKETBALL'}</p>
<p align="justify">Student{code=1, name='admin1', age=19, sex='FEMALE', birthday=Tue Jul 16 00:00:00 CST 2019, favourite='FOOTBALL'}</p>
<p align="justify">Student{code=2, name='admin2', age=20, sex='MALE', birthday=Tue Jul 16 00:00:00 CST 2019, favourite='BASKETBALL'}</p>
<p align="justify">Student{code=3, name='admin3', age=21, sex='FEMALE', birthday=Tue Jul 16 00:00:00 CST 2019, favourite='PINGPANG'}</p>
<p align="justify">Student{code=4, name='admin4', age=22, sex='MALE', birthday=Tue Jul 16 00:00:00 CST 2019, favourite='BASKETBALL'}</p>
<p align="justify">Student{code=5, name='admin5', age=23, sex='FEMALE', birthday=Tue Jul 16 00:00:00 CST 2019, favourite='FOOTBALL'}</p>
<p align="justify">......</p>
<p align="justify"><span style="font-family: 宋体;">　　从打印结果看来，所有的数据都已经获取到了，而且实现了自定义转换。</span></p>


####(2) setter函数方式

<p align="justify"><span style="font-family: 宋体;">　　导入的基本配置信息用注解方式实现；代码：</span></p>
<p align="justify">&nbsp;</p>
    
    public void toSimpleList() {
        File file = new File("F:/test/excel-test/user-attr.xls");
        new ImportCreator<User>()
                .header(setters)
                .body(Position.of(0, 3, 0))
                .inputFile(file).type(ExcelTypeEnum.XLSX)
                .build(User::new)
                .toList()
                .forEach(System.out::println);
    }

<p>&nbsp;</p>

####(3) 纯注解方式

<p align="justify"><span style="font-family: 宋体;">　　导入的配置信息用注解方式实现；代码：</span></p>
<p align="justify">&nbsp;</p>

    public void buildAnnotation() {
        File file = new File("F:/test/excel-test/user-attr.xls");
        new ImportCreator<User>()
                .inputFile(file)
                .type(ExcelTypeEnum.XLSX)
                .buildAnnotation(User::new)
                .toList()
                .forEach(System.out::println);
    }
    
<p>&nbsp;</p>

<p align="justify"><span style="font-family: 宋体;">　　由上述配置代码我们看明白了添加</span>@ImportUseAnnotation<span style="font-family: 宋体;">注解表示用注解方式导入，在属性上配置其转换函数得到自定义的类型值，但是</span>②③<span style="font-family: 宋体;">均为自定义属性，这两个函数系统自然不知道，那如何将这两个函数告诉系统呢？</span></p>
<p align="justify"><span style="font-family: 宋体;">　　解决办法是：写一个自定义类继承</span>ExcelFunction<span style="font-family: 宋体;">（例子中的这个类就是</span><span style="font-family: Calibri;">SubExcelFunction</span><span style="font-family: 宋体;">），然后将自定函数放置到其中，在操作对象中添加</span><span style="font-family: Calibri;">@FuctionTarget</span><span style="font-family: 宋体;">注解，指定其</span><span style="font-family: Calibri;">value</span><span style="font-family: 宋体;">值为刚创建的对象即可。那么</span>①<span style="font-family: 宋体;">的作用就很明显了，即告诉系统去</span>value<span style="font-family: 宋体;">对应的类中去扫描对应函数。</span></p>
<p align="justify"><span style="font-family: 宋体;">　　在该自定义类中还可以重写</span>getDefaultPattern()<span style="font-family: 宋体;">方法实现时间格式的自定义，这就是上文提到的自定义导出时间格式。</span></p>
<p align="justify"><span style="font-family: 宋体;">好了，至此</span>simple-excel<span style="font-family: 宋体;">相关操作已经全部阐述完毕，如果使用过程中有发现问题或者反馈建议者，可以联系邮箱：</span><em>tobiasy@163.com</em><span style="font-family: 宋体;">。</span></p>
<p align="justify">&nbsp;</p>