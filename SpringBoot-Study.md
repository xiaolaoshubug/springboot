## 配置文件

### 配置文件加载位置，配置多环境切换

````xml
***-file:./config/***  
***-file:./***
***-classpath:/config/***
***-classpath:/***
### SpringBoot使用一个全局的配置文件 ， 配置文件名称是固定的
### file代表项目根目录
### 优先级1：项目路径下的config文件夹配置文件
### 优先级2：项目路径下配置文件
### 优先级3：资源路径下的config文件夹配置文件
### 优先级4：资源路径下配置文件
````

- 环境切换

    ````yml
    # 多环境配置可以使用 --- 分割开
    # 默认环境
    server:
      port: 8080
    # 指定环境
    spring:
      profiles:
        active: dev
    ---
    # 开发环境
    server:
      port: 8081
    spring:
      config:
        activate:
          on-profile: dev
    ---
    # 测试环境
    server:
      port: 8082
    spring:
      config:
        activate:
          on-profile: test
    ````

    

### yaml配置文件

- application.properties

    - 语法结构 ：key=value

- application.yml

    - 语法结构 ：key：空格 value

- 比如我们可以在配置文件中修改Tomcat 默认启动的端口号

    ```yaml
    server:
      port: 80
    ```

- yaml配置实体类参数

    - 注意点：文件编码采用`UTF-8`

    - 实体类

        ````java
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Component
        @ConfigurationProperties(prefix = "person")
        public class Person {
        
            private String name;
            private Integer age;
            private Boolean happy;
            private Date birth;
            private Map<String, Object> maps;
            private List<Object> lists;
            private Dog dog;
        
        }
        ````

        ````yaml
        person:
          name: oay
          age: 18
          happy: false
          birth: 2000/01/01
          maps: {k1: v1,k2: v2}
          lists:
            - code
            - girl
            - music
          dog:
            name: 旺财
            age: 1
        ````

- properties配置实体类参数

    - 实体类

        ````java
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Component
        @PropertySource("classpath:dog.properties")
        public class Dog implements Serializable {
        
            //  SPEL表达式取出配置文件的值
            @Value("${name}")
            private String name;
            @Value("${age}")
            private Integer age;
        
        }
        ````

        ````properties
        name=旺财
        age=3
        ````

## JSR303数据校验

- 实体类

    ````java
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Component
    @ConfigurationProperties(prefix = "person")
    @Validated      //  开启数据校验
    public class Person {
    
        @Email(message = "请输入合法的邮箱")
        private String name;
        private Integer age;
        private Boolean happy;
        private Date birth;
        private Map<String, Object> maps;
        private List<Object> lists;
        private Dog dog;
    
    }
    ````

- 添加依赖

    ````xml
    <!--jsr303校验-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    ````

- 空检查

    ```
    @Null       验证对象是否为null
    @NotNull    验证对象是否不为null, 无法查检长度为0的字符串
    @NotBlank   检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格.
    @NotEmpty   检查约束元素是否为NULL或者是EMPTY
    ```

- Booelan检查

    ```
    @AssertTrue     验证 Boolean 对象是否为 true  
    @AssertFalse    验证 Boolean 对象是否为 false
    ```

- 长度检查

    ```
    @Size(min=, max=) 验证对象（Array,Collection,Map,String）长度是否在给定的范围之内  
    @Length(min=, max=) string is between min and max included.
    ```

- 日期检查

    ```
    @Past       验证 Date 和 Calendar 对象是否在当前时间之前  
    @Future     验证 Date 和 Calendar 对象是否在当前时间之后  
    @Pattern    验证 String 对象是否符合正则表达式的规则
    ```

## properties配置文件自动装配原理

```
每个配置都存在一个 ConfigurationProperties ：默认值  XxxProperties一个类和配置文件绑定，这样我们就可以使用自定义配置了！
```

````Java
//表示这是一个配置类，和以前编写的配置文件一样，也可以给容器中添加组件；
@Configuration 

//启动指定类的ConfigurationProperties功能；
//进入这个HttpProperties查看，将配置文件中对应的值和HttpProperties绑定起来；
//并把HttpProperties加入到ioc容器中
@EnableConfigurationProperties({HttpProperties.class}) 

//Spring底层@Conditional注解
//根据不同的条件判断，如果满足指定的条件，整个配置类里面的配置就会生效；
//这里的意思就是判断当前应用是否是web应用，如果是，当前配置类生效
@ConditionalOnWebApplication(
	type = Type.SERVLET
)

//判断当前项目有没有这个类CharacterEncodingFilter；SpringMVC中进行乱码解决的过滤器；
@ConditionalOnClass({CharacterEncodingFilter.class})

//判断配置文件中是否存在某个配置：spring.http.encoding.enabled；
//如果不存在，判断也是成立的
//即使我们配置文件中不配置pring.http.encoding.enabled=true，也是默认生效的；
@ConditionalOnProperty(
    prefix = "spring.http.encoding",
    value = {"enabled"},
    matchIfMissing = true
)

public class HttpEncodingAutoConfiguration {
//他已经和SpringBoot的配置文件映射了
private final Encoding properties;
//只有一个有参构造器的情况下，参数的值就会从容器中拿
public HttpEncodingAutoConfiguration(HttpProperties properties) {
    this.properties = properties.getEncoding();
}

//给容器中添加一个组件，这个组件的某些值需要从properties中获取
@Bean
@ConditionalOnMissingBean //判断容器没有这个组件？
public CharacterEncodingFilter characterEncodingFilter() {
    CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
    filter.setEncoding(this.properties.getCharset().name());
  filter.setForceRequestEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.REQUEST));
    filter.setForceResponseEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.RESPONSE));
    return filter;
}
//。。。。。。。
}
````

````
一句话总结 ：
	根据当前不同的条件判断，决定这个配置类是否生效！
    一但这个配置类生效；这个配置类就会给容器中添加各种组件；
    这些组件的属性是从对应的properties类中获取的，这些类里面的每一个属性又是和配置文件绑定的；
    所有在配置文件中能配置的属性都是在xxxxProperties类中封装着；
    配置文件能配置什么就可以参照某个功能对应的这个属性类
精髓：
    1、SpringBoot启动会加载大量的自动配置类
    2、我们看我们需要的功能有没有在SpringBoot默认写好的自动配置类当中；
    3、我们再来看这个自动配置类中到底配置了哪些组件；（只要我们要用的组件存在在其中，我们就不需要再手动配置了）
    4、给容器中自动配置类添加组件的时候，会从properties类中获取某些属性。我们只需要在配置文件中指定这些属性的值即可；
    xxxxAutoConfigurartion：自动配置类；给容器中添加组件
    xxxxProperties:封装配置文件中相关属性；
````

- springBoot存在大量的配置文件，检查是否生效

    ````yaml
    #  开启springboot的调试类，查看打印输出日志
    debug: true
    ````

## springboot+WebMvc开发

### 在springboot，我们可以使用以下方式处理静态资源

- webjars			`localhost:8080/webjars/`
- public、static、resources         `localhost:8080/`
- 优先级：resources > static (默认) > public

### 静态资源源码分析

````java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //	判断是否自定义了配置
    if (!this.resourceProperties.isAddMappings()) {
        logger.debug("Default resource handling disabled");
        return;
    }
    Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
    CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
    //	springboot定义了静态资源访问路径
    if (!registry.hasMappingForPattern("/webjars/**")) {
        customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl)
                .setUseLastModified(this.resourceProperties.getCache().isUseLastModified()));
    }
    //	默认是使用了  private String staticPathPattern = "/**";
    String staticPathPattern = this.mvcProperties.getStaticPathPattern();
    //	springboot定义了静态资源访问路径  this.resourceProperties
    if (!registry.hasMappingForPattern(staticPathPattern)) {
        	customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)
               /**
                private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
                        "classpath:/resources/", "classpath:/static/", "classpath:/public/" };                    
                private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS;
                */                                 
                .addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
                .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl)
                .setUseLastModified(this.resourceProperties.getCache().isUseLastModified()));
    }
}
````

## spring boot定时任务

### 异步请求

````java
@Async      //  告诉Spring这是一个异步方法，注解生效还需要在主线程加上@EnableAsync 开启注解
@Service    //  添加到Spring容器
public class AsyncService {
    public void hello() {
        try {
            System.out.println("业务进行中...");
            Thread.sleep(3000);
            System.out.println("业务结束...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@EnableAsync        //  开启异步注解
@SpringBootApplication
public class Springboot05TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot05TaskApplication.class, args);
    }
}
````

### 定时执行任务

````java
@Service
public class ScheduledService {
    //秒   分   时     日   月   周几
    @Scheduled(cron = "0/2 * * * * ?")  //  每两秒执行一次
    public void hello() {
        System.out.println("hello.....");
    }
}

@EnableScheduling   //  开启定时任务注解
@SpringBootApplication
public class Springboot05TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot05TaskApplication.class, args);
    }
}
````

````xml
Spring为我们提供了异步执行任务调度的方式，提供了
    两个接口：
    	TaskExecutor接口
		TaskScheduler接口
    两个注解：
		@EnableScheduling	 //	用于main主线程上
		@Scheduled			//	用于方法上，可以使用cron表达式	@Scheduled(cron = "0 * * * * 0-7")
	cron表达式：
		秒 分 时 日 月份 星期
        0 0 10,14,16 * * ? 每天上午10点，下午2点，4点
        0 0/30 9-17 * * ? 朝九晚五工作时间内每半小时
        0 0 12 ? * WED 表示每个星期三中午12点
        "0 0 12 * * ?" 每天中午12点触发
        "0 15 10 ? * *" 每天上午10:15触发
        "0 15 10 * * ?" 每天上午10:15触发
        "0 15 10 * * ? *" 每天上午10:15触发
        "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
        "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
        "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
        "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
        "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
        "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
        "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
        "0 15 10 15 * ?" 每月15日上午10:15触发
        "0 15 10 L * ?" 每月最后一日的上午10:15触发
        "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
        "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
        "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
        
        特殊字符 代表含义
        ,		枚举
        -		区间		
	    *	    任意
		/		步长
		?		日/星期冲突匹配
		L		最后
		W		工作日
		C		和calendar联系后计算过的值
		#		星期，4#2，第2个星期三
````

### 定时发送邮件

- 邮件发送需要引入spring-boot-start-mail

- SpringBoot 自动配置MailSenderAutoConfiguration

- 定义MailProperties内容，配置在application.yml中

- 自动装配JavaMailSender

- 测试邮件发送

    ````java
    @Autowired
    private JavaMailSenderImpl mailSender;  //  注入mailSender
    
    @Test
    void contextLoads() {
        //邮件设置1：一个简单的邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("通知");
        message.setText("今天下午不用上班");
    
        message.setTo("aoyio@qq.com");          //  接收
        message.setFrom("354191718@qq.com");    //  发送
        mailSender.send(message);
    }
    
    @Test
    void contextLoads2() throws MessagingException {
        //邮件设置2：一个复杂的邮件
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage,true);
    	//	标题
        helper.setSubject("通知");
        //  内容支持html,true开启
        helper.setText("<h1 style='color:red'>今天下午不用上班</h1>",true);
        //  发送附件
        helper.addAttachment("xiaolaos.jpg", new File("D:/upload/xiaolaos.jpg"));
    
        helper.setTo("aoyio@qq.com");          //  接收
        helper.setFrom("354191718@qq.com");    //  发送
    
        mailSender.send(mimeMailMessage);
    }
    ````

## springboot+Mybatis整合

- entity

    ````java
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class User implements Serializable {
    
        private Integer id;
        private String name;
        private String pwd;
    }
    ````

- mapper

    ````java
    @Repository
    public interface UserMapper {
        List<User> queryAll();
    }
    ````

- service代码和mapper一样

    - serviceImpl

        ````java
        @Component
        public class UserServiceImpl implements UserService {
        
            @Autowired
            private UserMapper userMapper;
        
            @Override
            public List<User> queryAll() {
                return userMapper.queryAll();
            }
        }
        ````

- controller

    ````java
    @RestController
    public class UserController {
    
        @Autowired
        private UserService userService;
    
        @GetMapping("/all")
        public List<User> queryAll() {
            return userService.queryAll();
        }
    }
    ````

- resource

    - mapper

        注意：这里的mapper包和上面的mapper包一定要一样

        ````xml
        <?xml version="1.0" encoding="UTF-8" ?>
        <!DOCTYPE mapper
                PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
        
        <mapper namespace="com.oay.mapper.UserMapper">
            <select id="queryAll" resultType="user">
                SELECT *
                FROM user;
            </select>
        </mapper>
        ````

    - yaml

        ````yaml
        spring:
          datasource:
            username: root
            password: root123456
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/mybatis?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf8
        
        #mybatis的相关配置
        mybatis:
          #mapper配置文件
          mapper-locations: classpath:mapper/*.xml
          type-aliases-package: com.oay.entity
          #开启驼峰命名
          configuration:
            map-underscore-to-camel-case: true
            # mybatis日志输出
            log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
        
        # 访问端口
        server:
          port: 80
        ````

    - log4j

        ````properties
        #将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
        log4j.rootLogger=DEBUG,console,file
        
        #控制台输出的相关设置
        log4j.appender.console = org.apache.log4j.ConsoleAppender
        log4j.appender.console.Target = System.out
        log4j.appender.console.Threshold=DEBUG
        log4j.appender.console.layout = org.apache.log4j.PatternLayout
        log4j.appender.console.layout.ConversionPattern=[%c]-%m%n
        
        #文件输出的相关设置
        log4j.appender.file = org.apache.log4j.RollingFileAppender
        log4j.appender.file.File=./logs/log.log
        log4j.appender.file.MaxFileSize=10mb
        log4j.appender.file.Threshold=DEBUG
        log4j.appender.file.layout=org.apache.log4j.PatternLayout
        log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n
        
        #打印sql部分
        log4j.logger.java.sql=DEBUG
        log4j.logger.java.sql.Connection = DEBUG
        log4j.logger.java.sql.Statement = DEBUG
        log4j.logger.java.sql.PreparedStatement = DEBUG
        log4j.logger.java.sql.ResultSet = DEBUG
        
        #配置logger扫描的包路径  这样才会打印sql
        log4j.logger.com.oay.mapper=DEBUG
        ````

- 测试

    http://localhost/all

## springboot+Redis

#### springboot 2.x后原来的jedis被替换成了lettuce

- jedis：采用直连，多线程操作不安全，避免不安全使用 jedis pool 连接池 ！更像 BIO 模式

- lettuce：采用netty，多线程共享，不存在线程不安全，可减少线程数量，更像 NIO 模式

- 源码分析

    ````java
    @Bean
    @ConditionalOnMissingBean(
        name = {"redisTemplate"}
    )
    //	redisTemplate 不在bean容器里面时生效
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    // 常用的string操作
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    ````

- 测试

    ````java
    @SpringBootTest
    class Springboot09RedisApplicationTests {
        @Autowired
        private RedisTemplate redisTemplate;
    
        @Test
        void contextLoads() {
            redisTemplate.opsForValue().set("mykey","cs");
            System.out.println(redisTemplate.opsForValue().get("mykey"));
        }
    }
    ````


注意点：value是对象的时候，一定要有无参构造方法

#### 封装自己的RedisTemplate

````java
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    //	加入bean容器
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
````

#### 封装一些常用的Redis命令方法

````java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public final class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(String.valueOf(CollectionUtils.arrayToList(key)));
            }
        }
    }


    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }


    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }


    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取set缓存的长度
     *
     * @param key 键
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */

    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */

    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
````

## 分布式Springboot+Zookeeper+Dubbo

### 安装zookeeper

- 下载安装包

- 注意点，使用zkServer.cmd会出现闪退，在conf文件夹下复制zoo_sample.cfg修改成zoo.cfg
- zookeeper是一个注册中心

### 打包dubbo-admin

- dubbo-admin是一个监控管理后台
- dubbo是一个jar包
- github下载项目：`https://github.com/apache/dubbo-admin/tree/master`
- maven打包：`mvn clean package -Dmaven.test.skip=e=true`
- 也使用idea打包，导入项目
- dubooWeb系统默认用户名密码 `root/root`

### 使用服务

1. #### **前提先打开zookeeper服务**

2. 服务端应用配置(提供者)

    - 代码

        `TicketService.java`

        ````java
        public interface TicketService {
            String getTicket();
        }
        ````

        `TicketServiceImpl.java`

        ````java
        
        import org.apache.dubbo.config.annotation.Service;
        import org.springframework.stereotype.Component;
        
        @Service	// 在项目一启动就自动注册到注册中心,这里要使用dubbo下面的包
        @Component  //  这里尽量使用@Component注解注入bean
        public class TicketServiceImpl implements TicketService {
            @Override
            public String getTicket() {
                return "学习Springboot+Dubbo+zookeeper";
            }
        }
        ````

        `application.properties`

        ````properties
        # 应用端口号
        server.port=8001
        # 服务名
        dubbo.application.name=provider
        #服务地址地址
        dubbo.registry.address=zookeeper://127.0.0.1:2181
        #扫描指定包下面的服务
        dubbo.scan.base-packages=com.oay.service
        ````

3. 客户端应用配置(消费者)

    - 代码

        `TicketService.java`

        注意这里是和服务端一致，包括类的路径和名称

        ````java
        public interface TicketService {
            String getTicket();
        }
        ````

        `UserService.java`

        ````java
        import org.apache.dubbo.config.annotation.Reference;
        import org.springframework.stereotype.Component;
        
        @Component
        public class UserService {
        
            @Reference
            private TicketService ticketService;
        
            public void bugTicket() {
                System.out.println(ticketService.getTicket());
            }
        }
        ````

        `application.properties`

        ````properties
        #应用端口号
        server.port=8002
        # 服务名
        dubbo.application.name=consumer-service
        #服务注册地址
        dubbo.registry.address=zookeeper://127.0.0.1:2181
        ````

    - 测试

        代码

        ````java
        @Autowired
        private UserService userService;
        
        @Test
        void contextLoads() {
            userService.bugTicket();
        }
        
        consoel输出：学习Springboot+Dubbo+zookeeper
        ````

        

