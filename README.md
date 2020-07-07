# 在线教育项目

## 1. 商业模式

1. C2C模式（平台对平台）

2. **B2C模式**（商家到用户模式）

   两个角色：管理员 和 普通用户

   - 管理员：增删改查
   - 普通用户：查询

3. B2B2C（商家到商家到用户）

   例子：京东 --> 自营、普通商家

   

## 2. 项目功能模块

### 2.1 系统后台

 	1. **<font color='red'>讲师管理模块</font>**
 	2. 课程分类管理模块
 	3. **<font color="green">课程管理模块</font>**
      	1. 视频
 	4. **<font color="blue">统计分析模块（图表）</font>**
 	5. <font color="brown">banner管理（首页轮播图管理）</font>
 	6. 权限管理

### 2.2 系统前台

1. **<font color="teal">首页数据的显示</font>**
2. 讲师列表和详情
3. 课程列表和课程详情
    	1. 视频在线播放
 4. 登录和注册功能
     	1. 微信扫描登录
 5. 微信扫码支付



## 3. 技术架构（前后端分离开发）

## 3.1 后端

| **SpringBoot** |   **SpringCloud**   | **MybatisPlus** |
| :------------: | :-----------------: | :-------------: |
|   **redis**    |      **maven**      |  **easyExcel**  |
|   **OAuth2**   | **Spring security** |     **jwt**     |

## 3.2 前端

|    **Vue**     | **Axios** | **Node.js** |
| :------------: | :-------: | :---------: |
| **Element-ui** |           |             |

## 3.3 其他技术

|   **阿里云OSS**    | **阿里云视频点播服务** | **阿里云短信服务** |
| :----------------: | :--------------------: | :----------------: |
| **微信支付和登录** |       **docker**       |      **git**       |
|    **jenkins**     |                        |                    |



## <font color="brown">4. MybatisPlus</font>

### 4.1 入门

1. 创建数据库和表，用于mp操作
2. mapper接口继承`BaseMapper<E>`，既可以自动继承增删改查方法
3. 在实体类的主键上写注解：`@TableId(type = IdType.AUTO)`来改变主键策略
   1. `IdType.ID_WOKER`：mp自带策略，针对数字类型
   2. `IdType.ID_WORKER_STR`：mp自带策略，针对字符串类型

### 4.2 主键自动生成策略

1. 自增长 Auto Increment
   1. 缺点：分表时id生成不是很方便
2. UUID：每次生成唯一值
   1. 缺点：无法进行排序
3. redis实现
   1. redis原子操作，按照步长递增
4. mp自带策略：snowflake算法，生成19位的数字
   1. 根据机器mac地址和时间来进行生成

### 4.3 Mp自动填充

不需要手动set值，通常用于`create_time`和`update_time`

[自动填充实现教程](https://mp.baomidou.com/guide/auto-fill-metainfo.html)

1. 实体类要进行自动填充的地方加注解`@TableField(fill = FieldFill.INSERT)`
2. 自定义一个Handler类
   1. 实现元对象处理器接口：com.baomidou.mybatisplus.core.handlers.MetaObjectHandler

### 4.4 <font color="red">乐观锁</font>

1. 什么是乐观锁

   针对某种特定问题的解决方案。主要解决：**丢失更新**。

2. 如果不考虑事务隔离性，会产生读写的问题。

- 读的问题
  - 脏读：读到了另一条事务未提交的数据
  - 不可重复读：事务1在它的事务期间，读到了另一条事务提交的数据
  - 幻读：：事务1在它的事务期间，读到了另一条事务提交的数据，导致读到的数据条数不一样
- 写的问题
  - 丢失更新：多个事务同时修改同一条数据，后提交的数据覆盖了先提交的数据

3. 解决丢失更新的问题
   1. 悲观锁：串行执行事务
   2. 乐观锁：**维护一个`version`**，开启事务的时候保存当前`version`，提交时检测当前数据`version`，只有一致才可以提交，提交完成后更新`version`。
4. mybatisPlus使用乐观锁
   1. 在数据库中添加`version`字段
   2. 在实体类中添加`version`字段，并添加`@Version`注解
   3. 配置乐观锁插件，[乐观锁插件配置](https://mp.baomidou.com/guide/optimistic-locker-plugin.html)。原理上，就是写个Bean给spring管理



### 4.5 Mp查询

1. 简单查询：一个id的查询

2. 批量查询：多个id的查询

3. 条件查询：`selectByMap`

4. **<font color="red">分页查询: </font>**[分页教程](https://mp.baomidou.com/guide/page.html)

   1. 配置分页插件

   2. ```java
          @Test
          public void testPage(){
              // 1. 创建Page对象
              // 2. 传入两个参数：当前页 和 每页显示的记录数
              Page<User> page = new Page<>(1, 3);
              // 3.调用mp分页查询方法
              // 把分页的数据都封装到page对象中
              userMapper.selectPage(page, null);
              System.out.println(page.getCurrent());// 当前页
              System.out.println(page.getRecords()); // 每页数据list集合
              System.out.println(page.getSize()); // 每页显示记录数
              System.out.println(page.getTotal()); // 总记录数
              System.out.println(page.getPages()); // 总页数
              System.out.println(page.hasNext()); // 是否有下一页
              System.out.println(page.hasPrevious()); // 是否有上一页
          }
      ```

5. **<font color="red">复杂条件查询</font>**

   使用`QueryWrapper`对象，和其他方法（比如MyBatis逆向工程）大同小异，这里只写两种不一样的

   1. `queryWrapper.select("id", "name")`这个语句可以让查询出的数据类中只有这两个属性有值
   2. `queryWrapper.last("limit 1 3")`这个表示在sql语句后面拼接语句

### 4.6 Mp删除

1. 物理删除：`deleteById()`

2. 批量删除：`deleteBatchIds()`

3. 逻辑删除：通过标志位，表示数据已删除，但是数据还存在于表里。这样方便数据作恢复。[逻辑删除教程](https://mp.baomidou.com/guide/logic-delete.html)。

   1. 表中加入字段`flag`

   2. 类中加入属性`flag`，并加上注解`@TableLogic`

   3. 在`application.properties`中配置全局变量

      ```yml
      mybatis-plus:
        global-config:
          db-config:
            logic-delete-
            logic-delete-value: 1 # 逻辑已删除值(默认为 1)
            logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
      ```

   **<font color="red">4. 逻辑删除取代了物理删除，mp自带的查找和删除操作都会触发逻辑删除</font>**，因此mp是无法查出已经被删除的数据的。要查询已经删除的数据，需要自己手写mapper。

### 4.7 Mp性能分析插件 

分析sql语句执行性能，以便于开发优化: [Mysql性能优化插件教程](https://mp.baomidou.com/guide/performance-analysis-plugin.html)（3.2.0版本移除）。



## 5. 项目结构

1. 创建父工程：SpringBoot工程
   1. pom类型: `<packagign>pom</packaging>`
   2. 放版本号: `<properties></properties>`
   
2. 创建子模块：maven工程

3. （可选）子子模块：maven工程

4. **在其他模块中引用一个模块**

   1. ```xml
      <dependency>
        <!--引入其他模块-->
        <groupId>com.memoforward</groupId>
        <artifactId>service_base</artifactId> 
        <version>0.0.1-SNAPSHOT</version>
      </dependency>
      ```

   2. 其他模块的启动项搜索不到该模块的配置类，因此要还要添加`@ComponentScan("com.memoforward")`扫描所有用这个包的模块下的所有类



## <font color="red">6. 用Swagger2进行接口测试（这真的究极好用，我哭了）</font>

1. 生成在线的接口文档`http://localhost:port/swagger-ui.html`
2. 方便接口测试（超级方便，比postman好用）
3. [SpringBoot 整合 Swagger2教程](https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/)

### 6.1 使用Swagger

1. 在项目下创建**公共模块**（任意取名），整合` swagger`(即引入`swagger`依赖和写`swagger`配置类)

   1. 这个公共模块中不仅仅只有`swagger`，因此一般情况下，我们会再建立一个子模块`service_base`来进行`swagger`的整合

2. 在要使用`swagger`的模块的`dependencies`里引入：

   1. ```xml
              <dependency>
                <!-- 这和上面的操作一摸一样 -->
                  <groupId>com.memoforward</groupId>
                  <artifactId>service_base</artifactId>
                  <version>0.0.1-SNAPSHOT</version>
              </dependency>
      ```

## 7. 讲师管理模块（后端）

1. 配置`application.properties`

2. 编写`controller service mapper`代码内容
   
   1. mp提供代码生成器：[代码生成器]([https://mp.baomidou.com/guide/generator.html#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B](https://mp.baomidou.com/guide/generator.html#使用教程))
   
3. 多条件组合查询

   1. 把条件值传到接口中去
      1. 思想：把条件值封装到对象里去，然后把对象传递到`controller`
      2. 建立条件类

4. <font color="red">**统一异常处理**</font>

   1. 在`common`模块中新建异常处理类

      1. ```java
         // 全局异常处理
         @ControllerAdvice
         public class GlobalException {
         
             @ExceptionHandler(Exception.class)
             @ResponseBody
             public R error(Exception e){
                 e.printStackTrace();
                 return R.error().message("执行全局异常处理..");
             }
         }
         ```

5. <font color="red">**统一日志处理**</font>

   Spring内置了logback。可以直接配置。

   1. 先删除`application.properties`
   2. 安装idea彩色日志插件：`grep-console`
   3. 在`resources`下新建`logback-spring.xml`



## 8. 前端技术

1. **ECMAScript6（ES6)基本语法**

   1. 变量

      1. `let`变量只能作用于局部，代码块外无法使用
      2. `let`变量不能重复定义
      3. `const`常量可以作用全局
      4. `const`常量的值必须初始化且值不能改变（即不能重复赋值）

   2. 解构赋值

      1. 数组解构

         ```javascript
         <script>
             // 传统
             let a = 1, b = 2, c = 3;
             // es6
             let [x, y, z] = [1, 2, 3]
         </script>
         ```

      2. 对象解构

         ```javascript
         <script>
             // 定义对象
             let user = {"name":"lucy", "age":20}
         
             // 传统从对象中取值
             let name1 = user.name;
             let age1 = user.age;
             // es6
             let {name2, age2} = user;
         </script>
         ```

   3. 模板字符串:`

      ```javascript
      <script>
          // 1. 使用`符号实现换行
          let s = `hey,
          fucking do`;
          // 2. 在`符号里面使用表达式获取变量值
          let name = "mike"
          let age = 20
          let s1 = `hello, ${name}, age ${age+1}`
          // 3. 在`符号里面调用方法
          function f1(){return "hello es6!"}
          let s2 = `demo, ${f1()}`;
          console.log(s2)
      </script>
      ```

   4. 声明对象

      ```javascript
      <script>
          const age = 12;
          const name = "cxy";
          // 传统方式定义变量
          const p1 = {name:name, age:age};
          console.log(p1);
      
          // es6
          const p2 = {name, age};
          console.log(p2);
      </script>
      ```

   5. 定义方法

      ```javascript
      <script>
          // 传统方式方式定义方法
          const person1 = {
              sayhi:function(){
                  console.log("hi");
              }
          }
          // 调用
          person1.sayhi();
      
          // es6
          const person2 = {
              sayhi(){
                  console.log("hi, p2");
              },
              saygb(){
                  console.log("goodbye, p2");
              }
          }
          person2.sayhi();
          person2.saygb();
      </script>
      ```

   6. 对象拓展运算符

      ```javascript
      <script>
          // 1.对象拷贝
          let person1 = {name:"lucy", age:20};
          let person2 = {...person1};
          console.log(person2);
      
          // 2. 对象合并
          let name = {name: "mary"};
          let age = {age: 20};
          let p2 = {...name, ...age}
          console.log(p2)
      </script>
      ```

   7. 箭头函数：参数 => 函数体

      ```javascript
      <script>
          // 传统方式
          var f1 = function(m){
              return m+1;
          }
          var f3 = function(a, b){
              return a+b;
          }
          // es6
          var f2 = m => m+1;
          var f3 = (a,b) => a+b;
      </script>
      ```

2. **Vue：用于构建用于界面的渐进式框架，只关心视图层**

   1. 简单实例

      ```javascript
      <body>
          <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
          <div id="app">
              {{msg}}
          </div>
          <script>
              new Vue({
                  el: "#app",
                  data:{
                      msg:"hello, vue"
                  }
              });
          </script>
      </body>
      ```

   2. <font color="red">**Vue指令**</font>

      1. `v-bind`：单向绑定

         ```javascript
         <body>
             <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
             <div id="app">
                 <!-- v-bind指令
                     单向数据绑定，一般用在标签属性里面，获取值
                 -->
                 <h1 v-bind:title="msg">{{content}}</h1>
                 <!-- 简写 -->
                 <h2 :title="msg">{{content}}</h2>
             </div>
             <script>
                 new Vue({
                     el: "#app",
                     data:{
                         content:"我是标题",
                         msg:"页面加载于:" + new Date().toLocaleString()
                     }
                 })
             </script>
         </body>
         ```

      2. `v-model`：双向绑定

         ```javascript
         <body>
             <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
             <div id="app">
                 <!-- 单向 -->
                 <input type="text" v-bind:value="searchMap.keyWord">
                 <!-- 双向 -->
                 <input type="text" v-model="searchMap.keyWord">
                 <p>{{searchMap.keyWord}}</p>
             </div>
             <script>
                 new Vue({
                     el: "#app",
                     data:{
                         searchMap:{
                             keyWord:"memoforward"
                         }
                     }
                 })
             </script>
         </body>
         ```

      3. Vue事件：`v-on:click=""`

         ```javascript
         <body>
             <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
             <div id="app">
                 <!-- v-on事件 -->
                 <button v-on:click="search()">查询</button>
                 <!-- 简写 -->
                 <button @click="search()">查询2</button>
             </div>
             <script>
                 new Vue({
                     el: "#app",
                     data:{
                     },          
                     methods:{
                         search(){
                             console.log("search...");
                         }
                     }
                 })
             </script>
         </body>
         ```

      4. Vue修饰符

         1. `prevent`组织标签默认行为：阻止表单提交

            ```javascript
            <body>
                <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
                <div id="app">
                    <form action="#" v-on:submit.prevent="onSubmit">
                        <input type="text" id="name" v-model="user.name">
                        <Button type="submit">提交</Button>
                    </form>
                </div>
                <script>
                    new Vue({
                        el: "#app",
                        data:{
                            user:{}
                        },
                        methods:{
                            onSubmit(){
                                // TODO
                                if(this.user.name){
                                    console.log('提交表单');
                                }else{
                                    alert("请输入用户名");
                                }
                            }
                        }
                    })
                </script>
            </body>
            ```

      5. **条件渲染**

         1. `v-if`：懒加载

            ```javascript
            <body>
                <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
                <div id="app">
                    <input type="checkbox" v-model="ok">两个人
                    <h1 v-if="ok">cxy</h1>
                    <h1 v-else="ok">fjy</h1>
                </div>
                <script>
                    new Vue({
                        el: "#app",
                        data:{
                            ok: false,
                        }
                    });
                </script>
            </body>
            ```

            

         2. `v-show`：会渲染所有的页面元素

         3. `v-for`：列表渲染

            ```javascript
            <body>
                <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
                <div id="app">
                    <ul>
                        <li v-for="n in 10">{{n}}</li>
                    </ul>
                    <ol>
                        <li v-for="(n, index) in 8">{{index}} --- {{n}}</li>
                    </ol>
                    <table border="1px">
                        <tr v-for="user in users">
                            <td>{{user.id}}</td>
                            <td>{{user.name}}</td>
                            <td>{{user.age}}</td>
                        </tr>
                    </table>
                </div>
                <script>
                    new Vue({
                        el: "#app",
                        data:{
                            users:[
                                {id:1, name:"cxy", age:20},
                                {id:2, name:"fjy", age:18},
                                {id:3, name:"mmm", age:200},
                            ]
                        }
                    });
                </script>
            </body>
            ```

            

   3. **Vue组件**

      1. 局部组件

         ```javascript
         <body>
             <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
             <div id="app">
                 <navbar></navbar>
             </div>
             <script>
                 new Vue({
                     el: "#app",
                     data:{
                     },
                     components:{
                         // 组件名字
                         'navbar':{
                             template:"<ul><li>首页</li><li>学院</li></li></ul>"
                         }
                     }
                 });
             </script>
         </body>
         ```

      2. [全局组件]([https://cn.vuejs.org/v2/guide/components-registration.html#%E5%85%A8%E5%B1%80%E6%B3%A8%E5%86%8C](https://cn.vuejs.org/v2/guide/components-registration.html#全局注册))
         
         1. 在一个js文件里面创组件，然后在引入这个js文件，就可以用组件了，语法见链接。

   4. Vue实例的生命周期:[生命周期图]([https://cn.vuejs.org/v2/guide/instance.html#%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F%E5%9B%BE%E7%A4%BA](https://cn.vuejs.org/v2/guide/instance.html#生命周期图示))

      1. created：实例被创建之后执行
      2. mounted：页面渲染之后执行

   5. Vue路由

      1. [vue-router英文实例](https://router.vuejs.org/guide/#javascript)
      2. [vue-router官方实例](https://cn.vuejs.org/v2/guide/routing.html)

3. **axios**：在vue发送ajax请求，axios并不是vue的一部分

   1. [官方文档](http://www.axios-js.com/zh-cn/docs/)

4. **element-ui**：基于vue的后台组件库

5. **node.js**：开发环境

   1. 是什么？

      运行在服务端的`javaScript`。可以理解成JavaScript的运行环境，用于执行JavaScript代码环境。

   2. 作用：
      1. 不需要浏览器，直接使用node.js运行JavaScript
      2. 可以模拟出服务器效果，比如tomcat

   3. 怎么用？

      1. 安装node.js
      2. 在终端`node xxx.js`
      3. 在Vscode里面打开终端执行

   4. <font color="red">**npm包管理工具**</font>

      Node Package Manager，是Node.js的管理工具，类似于maven，可以联网下载js依赖（如jquery）

      1. npm项目初始化操作：`npm init`

      2. npm下载js依赖

         - `npm install jquery`

         - 通过`npm install`配合pacakge.json可以快速下载依赖

      3. `--global`是全局

      4. `--save-dev`是局部，当前项目

6. babel

   babel是一个转码器，可以把es6代码转化成es5代码。

   1. 安装：`npm install --global babel-cli`

   2. 在项目根目录下配置`.babelrc`

      ```json
      {
        "presets":["es2015"],
        "plugins":[]
      }
      ```

   3. 安装es2015转码器：`npm install --save-dev babel-preset-es2015`

   4. 使用命令转码

      - 对文件转码`babel es6path/x.js -o es5path/x.js`

      - 对文件夹下的所有文件转码`babel es6path -d es5path`

7. 模块化

   1. 是什么

      - 开发后端接口的时候，开发controller service mapper，controller注入service，service注入maper。在后端中，类与类之间的调用成为后端模块化操作
      - **前端模块化，在前端中，js与js之间的调用成为前端模块化操作**

   2. es5实现模块化

      - ```javascript
        // 被引用的模块
        module.exports = {
          // 方法名
        }
        ```

      - ```javascript
        // 调用的模块
        const m = require('x.js')
        m.调用方法
        ```

   3. es6实现模块化

      - ```javascript
        // 被调用的
        export function s(){}
        
        // 更简单的
        export default{
          	a(){},
          	b(){}
        }
        ```

      - ```javascript
        // 调用方法
        import {方法名} from "path"
        import m form "path"
        m.调用方法
        ```

8. webpack

   Webpack是一个前端资源加载和打包工具。把多个静态文件打包成一个静态文件。减少页面的请求次数，提高页面的效率。

   1. 安装`npm install -g webpack webpack-cli`

   2. 打包js文件

      - 创建js文件用于打包操作：两个含方法，一个main用来调用

      - 在根目录配置`webpack.config.js`

        ```javascript
        const path = require("path") // Node.js内置模块
        module.exports = {
          entry: './src/main.js', // 因为main包含了其他的两个文件
          output:{
          	path: path.resolve(__dirname, './dist'), // 输出路径, __dirname: 当前文件所在路径
            filename:'bundle.js' // 输出文件
        	}
        }
        ```

   3. 打包css文件

      - 创建css文件：`style.css`

      - 在main.js中引入css：`require("./style.css")`

      - 安装css转换器`npm install --save-dev style-loader css-loader`

      - 修改`webpack.config.js`配置文件

        ```javascript
        const path = require("path") // Node.js内置模块
        module.exports = {
          entry: './src/main.js', // 因为main包含了其他的两个文件
          output:{...},
          // 添加这一段
          module:{
            rules:[
              {
                test:/\.css$/, // 打包规则应用到css结尾的文件上
              	use:['style-loader','css-loader']
              }
            ]
          }
        }
        ```

      4. 打包命令`webpack`或者`webpack --mode=development`

## 9. 前端项目

后台管理系统 + 前台用户系统

1. 搭建项目前端页面环境

   选取一个模板（框架）进行环境搭建：`vue-admin-template`。[模板git网址](https://gitee.com/liuxianfeng521/vue-admin-template-master)。

2. 前端页面的环境说明
   - 前端环境的入口：`index.html`和`main.js`
   - 前端页面环境使用框架（模板），主要基于两种技术：vue + element-ui
   - 框架`build`目录：构建项目的编译文件
   - `config`目录：配置修改
   - **src目录**
     - <font color="red">api</font>：定义调用方法
     - assets：静态资源
     - components：组件
     - <font color="red">router</font>：路由
     - <font color="red">views</font>：具体页面
   
3. 把后台登录的入口改成本地

   - 在`src/api/login.js`中查看请求地址
   - 在`config/dev.env.js`中改变`base_api`

4. 进行登录：`login方法`和`getInfo`方法

   1. 跨域问题：通过一个地址去访问另一个地址，这个过程中如果有三个地方不一样就会有问题
      - 访问协议：http https
      - ip地址：192.168.1.1等
      - 端口号：8001
   2. 解决跨域
      - 在`Controller`方法上加上`@CrossOrigin`
      - 使用网管解决

5. **前端开发框架使用过程**

   1. 添加路由

   2. 点击路由，跳到创建页面

   3. 在api中创建js文件，定义接口的地址和参数

   4. 在创建的vue页面中引入js文件，调用方法实现功能

   5. 补充

      - `element-ui`的`table`中有个scope表示域，可以得到每条具体数据，具体的查资料

      - 路由如何跳转：`this.$router.push({ path:..})`

      - 多次路由跳转到同一个页面，`created`方法只会执行一次，怎么解决？

        - 使用`vue`中的监听

          ```javascript
          watch: {
            $route(to, from){// 路由发生变化，方法就会执行
              this.init() // init是自定义的初始化方法
            }
          },
          ```

          

      - 同一个页面，如何用两种不同的路由？

        ```java
        // 隐藏路由
        {
          path: 'save',
          name: '添加讲师',
          component: () => import('@/views/edu/teacher/save'),
          meta: { title: '添加讲师', icon: 'tree' }
        },
        {
          path:'edit/:id', // 这里:id表示传参，相当于get请求 中的 "?"
          name:"修改讲师",
          component:() => import('@/views/edu/teacher/save'),
          meta:{title:"编辑讲师", noCache: true},
          hidden:true
        }
        ```

      - 同一个界面，不同的路由，怎么判断路径的不同？（比如添加和修改的页面是一样的，但是每次执行的方法不同）

        ```javascript
        // 判断路径的基本写法
        created() {
          console.log("created");
          if (this.$route.params && this.$route.params.id) {
            // 从路径中获取id值
            const id = this.$route.params.id;
            // 调用根据id的方法
            this.getInfo(id);
          }
        },
        ```

6. 实现讲师头像上传功能：阿里云oss

   1. 使用java代码实现上传文件到阿里云
      - 准备工作：创建操作阿里云oss的`access key`（许可证，用来进行API的交互）
      - [阿里云oss学习网址](https://help.aliyun.com/document_detail/32011.html?spm=a2c4g.11186623.6.784.349f63280cnDA9)

7. 添加课程分类功能：EasyExcel -- 阿里巴巴高效操作excel的框架，[学习网页](https://www.yuque.com/easyexcel/doc/read)

   1. 读取excel表格，加入到数据库：
      - 不仅需要引入easyexcel的依赖，还需要引入poi的依赖，因为easyexcel是对poi的封装
   2. 课程分类**添加功能**：后端接口
      1. 代码生成器把controller等类生成（不说都忘记了）
      2. 创建实体类与excel对应
      3. 创建监听器，具体看学习网址
   3. 课程分类

8. 课程分类列表

   1. 前端
      - 添加界面：上传组件
      - 列表界面
   2. 树形结构显示
      1. 根据`element-ui`d来设置后端返回的R

9. **课程管理**

   1. 使用代码生成器根据表生成代码

   2. 细节问题
      
      - 创建实体类封装表单数据
      
3. 富文本编辑器组件，将内容拷贝到相应位置
   
4. 课程章节列表怎么弄？创建两个实体类，表示章节和小节
   
   1. 删除操作：删除章节 级联删除小节（不推荐） || 只有小节全部删除了，在删除章节
   
5. 当一次性要查询多张表怎么弄？
   
   1. 手写sql：`多表连接查询`
   
      2. ```mysql
         select ec.id,ec.price,ec.lesson_num,ec.cover,
         			 ecd.description,
         			 et.name as teacherName,
         			 es1.title as oneSubject,
         			 es2.title as twoSubject
         from edu_course ec left outer join edu_course_description ecd on ec.id = ecd.id
         									 left outer join edu_teacher et on ec.teacher_id = et.id
         									 left outer join edu_subject es1 on ec.subject_parent_id = es1.id
         									 left outer join edu_subject es2 on ec.subject_id = es2.id
         where ec.id = 18
      ```
   
   3. `mybatis-plus`自动生成的`mapper`的`xml`不在资源文件夹里面，`maven`编译的时候不去处理，所以无法与自定义的方法绑定，怎么办？两步走：
   
      - 在`application.properties`里面添加配置，让`maven`找到xml文件
   
           ```properties
           # 配置xml加载
           mybatis-plus.mapper-locations=classpath:com/memoforward/eduservice/mapper/xml/*.xml
        ```
   
      - 在`pom.xml`中添加
   
           ```xml
           <build>
             <resources>
               <resource>
                 <directory>src/main/java</directory>
                 <includes>
                   <include>**/*.xml</include>
                 </includes>
                 <filtering>true</filtering>
               </resource>
             </resources>
           </build>
        ```
   
      6. 视频播放：[阿里云视频点播JavaSDK](https://help.aliyun.com/document_detail/53406.html?spm=5176.12672711.0.0.391e1fa3jtnFZv)
         1. 视频上传
         2. 视频删除
         3. 视频播放



### Nginx

反向代理服务器，功能：

1. 请求转发：客户端请求 -- nginx --> 具体的服务器(不同的服务器有不同的端口)
   - 如何知道请求到哪个端口：根据**路径的值**进行匹配
2. 复杂均衡：客户端请求 --nginx --> 服务器集群
   - 将客户端的n个请求平均分摊到不同的服务器
3. 动静分离：把静态资源和java代码分摊到不同的服务器

- 特点
  - 关闭终端，服务不会停止
  - 多路复用，多个进程
  
- 配置端口号和请求转发：在`nginx.conf`中修改

  ```
  http {
      include       mime.types;
      default_type  application/octet-stream;
      keepalive_timeout  65;
      server {
          listen       9001;
          server_name  localhost;
  
      	# 匹配路径 请求转发 ~ 表示正贼配
  		location ~ /eduservice/ {
  			proxy_pass http://localhost:8001;
  
  		}
  
  		location ~ /eduoss/ {
  			proxy_pass http://loalhost:8002;
  		}
  }
  ```



## <font color="red">SpringCloud: 微服务</font>

`SpringBoot + SpringCloud`

1. 什么是微服务？

   - 一种架构风格：**每个服务独立运行在自己的进程中。**

2. 为什么需要微服务？

   -  把一个项目拆分成多个独立的服务，每个服务占用独立的进程
   - 每个独立的服务可以单独进行部署，减少代码量，也便于维护
   - 每个服务可以单独使用不同的数据库
   - 每个服务模块可以使用不同的技术来开发

3. 什么样的项目需要微服务：可以按照业务本身来进行划分的服务

4. 微服务框架有哪些？

   - SpringCloud
   - Dubbo

5. **SpringCloud**

   并不是一种技术，而是很多技术的总称。`SpringCloud`是一系列框架的集合，用里面的技术可以实现微服务操作。

   使用`SpringCloud`需要依赖`SpringBoot`技术。

   1. `SpringCloud`和`SpringBoot`有什么关系：`SpringBoot`用来快速构建`Spring`项目，`SpringCloud`需要依赖`SpringBoot`这个技术。

   2. 相关基础服务组件

      - 服务发现：Nacos
      - 服务调用：Neflix Eureka
      - 熔断器：Neflix Hystrix
      - 服务网关：Spring Cloud GateWay
      - 分布式配置：Nacos
      - 消息总线：Nacos

   3. Spring注册中心：Nacos

      - 面临问题：”删除小节“在`edu_servive`模块，”删除视频“在`edu_vod`模块，怎么进行模块间调用？
        - 将两个模块都在注册中心进行注册，通过这个中介进行调用
      - **实现不同的微服务模块之间的调用，需要将这些模块在注册中心中注册**
      - Nacos是需要安装的：[下载网址及使用教程](https://nacos.io/zh-cn/docs/quick-start.html)

   4. Spring服务调用

      - 引入依赖`openfeign`

      - 调用端启动类添加`@EnableFeignClients`

      - 在调用端创建`interface`接口

        ```java
        @FeignClient("service-vod")
        @Component
        public interface VodClient {
            
          	// 被调用的类的方法
            @DeleteMapping(value = "/eduvod/vod/{videoId}/removeAliyunVideo")
            R removeAliyunVideo(@PathVariable("videoId") String videoId);
        }
        
        // 在Controller中
        @Autowired
        VodClient vodCilent；
        ```

      5. SpringCloud调用接口的过程
         - `接口化请求调用（在消费者这里申明生产者接口）--call--> Feign(服务发现) ---> Hystrix(熔断器，防止服务宕机，切断服务) ---> Ribbon(负载均衡) ---> http client(发送请求，具体调用)`
      6.  Hytrix熔断器介绍
         1. 容错处理：延长响应时间，自动断开连接等等
         2. 将之前调用端的接口进行实现，实现的方法就是熔断后执行的方法
      7. 网关GateWay
         - 路由：一个id，一个目的url，一组断言，一组过滤器
         - 断言：匹配规则
         - 过滤器



## 补充

1. `controller`要返回统一的数据格式：json

   1. ```json
      {
        "success":布尔,
        "code": 数字, // 响应码
        "message": 字符串, // 响应信息
        "data": Map // 具体数据
      }
      ```

   2. 创建`interface`或者`emnu`来规范数据格式：返回状态码

      1. ```java
         SUCCESS:2000;
         ERROR:2001;
         ```

   3. **<font color="red">统一返回结果对象</font>**

   4. 外键：让”多“的一方创建字段指向”一“的一方，称为外键



## <font color="red">Redis</font>

NoSQL，基于开源使用ANSI C语言编写的`key-value`存储结构

1. 存储结构
   - string list hash set zset
2. 支持持久化，集群部署，可以通过内存存到硬盘
3. 支持过期时间，支持事务

- 使用
  - 引入依赖
  - 配置ReidsTemplate类，并@Configuration
  - 在要查询的方法上`@Cacheable(vlaue="", key="‘’")`
    - 有数据从缓存返回
    - 没数据，添加
  - 在添加方法上`@CachePut`
  - 在修改和更新方法上`@CacheEvict`有属性`allEntries`可以清空缓存
- redis一般在一个专门的服务器上，因此要在spring的配置文件中配置redis

