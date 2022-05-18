package com.xz.helpful.config;

import com.xz.helpful.shiro.UserRealm;
import com.xz.helpful.shiro.session.RedisSessionDao;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    //将自己的验证方式加入容器
    @Bean
    public UserRealm myShiroRealm() {
        return new UserRealm();
    }

    /**
     * 获取自定义SessionDao，并设置过期时间
     * @return
     */
    @Bean
    public RedisSessionDao getRedisSessionDao() {
        final int TIMEOUT = 60 * 60 * 24 * 3;//session过期时间 n 天
        return new RedisSessionDao(TIMEOUT);
    }

    /**
     * 给shiro的sessionId默认的JSSESSIONID名字改掉
     *
     * @return
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sessionId");
        return simpleCookie;
    }

    //shiro的session过期时间
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO(getRedisSessionDao());
        sessionManager.setSessionIdCookie(getSessionIdCookie());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
       /*
        @function securityManager.setSessionManager(sessionManager());
        如果要开启这个，就要使用字节码来进行反序列化等操作，不可以使用json反序列化
        注释这里关闭自定义redisTemplate,关闭后上面的session过期将不会生效
        如果开启了时不时会报这个异常：java.lang.IllegalStateException: LettuceConnectionFactory was destroyed and cannot be used anymore
        暂时不知怎么解决
        */
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();

        map.put("/user/logout", "logout");
        map.put("/", "anon");
        map.put("/user/login", "anon");
        map.put("/user/register", "anon");
        map.put("/user/verify", "anon");
        //AffairController接口公开访问
        map.put("/event/**", "anon");
        //druid监控面板，测试用
        map.put("/druid/**", "anon");
        //静态资源访问
        map.put("/css/**", "anon");
        map.put("/js/**", "anon");
        map.put("/images/**", "anon");
        map.put("/favicon.ico", "anon");
        map.put("/**", "authc");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}