## dosimple
### spring boot 2.1.3,spring cloud Greenwich.RELEASE
#### 介绍
- 该项目架构主要希望将自己所学揉入进来
##### spring cloud架构。 
- eureka
- druid
- redis
- rabbitmq >>>>>dosimple-message
- gateway >>>>>dosimple-gateway
- elastic-job >>>>>dosimple-task(该模块引用了git上[Elastic-Job Spring Boot](https://github.com/yinjihuan/elastic-job-spring-boot-starter)
集成的项目,因为无法maven引入,直接拷贝使用了)
- feign, hhystrix, ribbon >>>>> dosimple-biz-rpc
- sharding-jdbc >>>>> dosimple-biz-shop
- actuator >>>>> dosimple-monitor
- config-server >>>>> dosimple-config
- 多数据源路由 >>>>> dosimple-biz-user
#### 安装教程
ps:此处的饭非本人撰写,只是本人在搭建时参考过博文。
- clone之后idea打开
- zookeeper集群自行在虚拟机上部署，<a href="https://blog.csdn.net/cruise_h/article/details/19046357" target="_blank">喂一口饭</a>
- rabbitmq自行在虚拟机或本地安装,<a href="https://blog.csdn.net/qq_16538827/article/details/82838419" target="_blank">饭在这里</a>
- prometheus + Grafana,<a href="http://www.itmuch.com/spring-boot/actuator-prometheus-grafana/" target="_blank">饭在这里</a>
- search-user,search-shop模块resources有需要执行的sql语句
- elastic-job控制台，<a href="https://blog.csdn.net/qq_31289187/article/details/84843044" target="_blank">饭在这里</a>
#### 说明(demo放在单元测试中)

- dosimple-biz-shop,dosimple-biz-user有feign rpc调用demo,hyxtrix熔断
- dosimple-common模块中FiltUtils可以多线程读取文件并计算单词数量, 多线程分割文件
，多线程合并文件这个三个工具（感觉封装的挺好了，全网没找到一样的。如有不足请指出ps:也可能是我没找到吧）
多线程读取文件计算单词，在4核CPU，16G内存的环境下，可在30s内计算完1G的文档
- dosimple-biz-user 中有多数据源路由demo
- dosimple-biz-shop集成sharding-jdbc的demo
- dosimple-biz-rpc熔断后将数据存入mongodb
- dosimple-gateway 网关的路由以及限流功能
- dosimple-config 配置中心功能
- dosimle-monitor服务器监控,采用了prometheus + Grafana应用可视化监控.如果想要直接看数据则直接访问<b>http://ip:port/actuator/prometheus</b>即可,但数据可视化并不好
[prometheus官网地址](https://prometheus.io/),从该链接下载prometheus.
[Grafana官网地址](https://grafana.com/),从该链接下载Grafana.
在网上查看了这个教程，感觉还行
[Spring Boot 2.x监控数据可视化(Actuator + Prometheus + Grafana](http://www.itmuch.com/spring-boot/actuator-prometheus-grafana/)
可以将该monitor模块引入到不同模块的pom文件中，每个模块properties中设置好参数。这样可以监控所有服务器了
- 考虑到注册中心有可能需要暴露到外网。在eureka中加入了spring-boot-starter-security,并配置了WebSecurityConfig
所有客户端注册都采用http://${user}:${password}@${host}:${port}/eureka/这种方式注册
```
    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // Configure HttpSecurity as needed (e.g. enable http basic).
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
            http.csrf().disable();
            //为了可以使用 http://${user}:${password}@${host}:${port}/eureka/ 这种方式登录,所以必须是httpBasic,
            //如果是form方式,不能使用url格式登录
            http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
        }
    }
```
- dosimple-idgenerator 引用了<a href="https://tech.meituan.com/2017/04/21/mt-leaf.html" target="_blank">美团的ID生成系统的</a>,
[美团leaf](https://github.com/Meituan-Dianping/Leaf)
- to be continued
 



