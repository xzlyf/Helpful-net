package com.xz.helpful.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xz.helpful.serializer.FastJson2JsonRedisSerializer;
import com.xz.helpful.serializer.MyRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 */
@Slf4j
@Configuration
@EnableCaching //开启注解
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.port}")
    private Integer REDIS_PORT;
    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    /**
     * lettuce 连接工厂
     * 负责操作 redis db 0 的库
     */
    @Bean(name = "firstRedisFactory")
    @Primary
    public LettuceConnectionFactory firstLettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setHostName(REDIS_HOST);
        redisStandaloneConfiguration.setPort(REDIS_PORT);
        //redisStandaloneConfiguration.setPassword(RedisPassword.of(password));//无密码可省略
        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }
    /**
     * lettuce 连接工厂
     * 负责操作 redis db 1 的库
     */
    @Bean(name = "secRedisFactory")
    public LettuceConnectionFactory secLettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(1);
        redisStandaloneConfiguration.setHostName(REDIS_HOST);
        redisStandaloneConfiguration.setPort(REDIS_PORT);
        //redisStandaloneConfiguration.setPassword(RedisPassword.of(password));//无密码可省略
        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }

    /**
     * redisTemplate配置
     * 该模板对应 com.xz.helpful.utils.RedisShiroUtil.java工具类
     * 该模板使用字节码序列化 ， 适合不支持json序列化的任务
     * @return
     */
    @Bean(name = "codeTemplate")
    public RedisTemplate<String, Object> redisShiroTemplate(@Qualifier("firstRedisFactory") LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);// 配置连接工厂
        customRedisSerializer(template);//使用字节码序列化,可以解决SimpleSession反序列化异常的问题
        //jackson2JsonRedisSerializer(template);//使用jackson序列化
        //fastjson2fastJsonRedisSerializer(template);//使用fastjson序列化;
        template.setKeySerializer(new StringRedisSerializer());//使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setHashKeySerializer(new StringRedisSerializer());// 设置hash key 和value序列化模式
        template.afterPropertiesSet();
        return template;
    }

    /**
     * reteTemplate相关配置，配置redis序列发方式，和连接工厂
     * 该模板对应 com.xz.helpful.utils.RedisUtil.java工具类
     * 该模板使用jackson序列化 ， 可用于一般业务
     * @param factory @Qualifier("secRedisFactory") 自动装配 LettuceConnectionFactory的对象
     */
    @Bean(name = "jsonTemplate")
    public RedisTemplate<String, Object> reteTemplate(@Qualifier("secRedisFactory") LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);// 配置连接工厂
        //customRedisSerializer(template);//使用字节码序列化,可以解决SimpleSession反序列化异常的问题
        jackson2JsonRedisSerializer(template);//使用jackson序列化
        //fastjson2fastJsonRedisSerializer(template);//使用fastjson序列化;
        template.setKeySerializer(new StringRedisSerializer());//使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setHashKeySerializer(new StringRedisSerializer());// 设置hash key 和value序列化模式
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 使用字节码方式进行序列化和反序列化
     */
    private void customRedisSerializer(RedisTemplate<String, Object> template) {
        MyRedisSerializer serializer = new MyRedisSerializer();
        template.setValueSerializer(serializer);//暂不使用自定义字节码序列化
        template.setHashValueSerializer(serializer);

    }

    /**
     * 使用Jackson来进行序列化和反序列化
     */
    private void jackson2JsonRedisSerializer(RedisTemplate<String, Object> template) {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        template.setHashValueSerializer(jacksonSeial);
    }

    /**
     * 使用fastJson来进行序列化和反序列化
     */
    private void fastjson2fastJsonRedisSerializer(RedisTemplate<String, Object> template) {
        FastJson2JsonRedisSerializer<Object> fastSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(fastSerializer);//fastJson序列化
        template.setHashValueSerializer(fastSerializer);

    }

}

