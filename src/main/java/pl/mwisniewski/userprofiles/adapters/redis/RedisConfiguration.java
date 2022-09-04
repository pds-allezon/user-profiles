package pl.mwisniewski.userprofiles.adapters.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
    @Value("${spring.redis.host1}")
    private String host1;

    @Value("${spring.redis.port1}")
    private int port1;

    @Value("${spring.redis.host2}")
    private String host2;

    @Value("${spring.redis.port2}")
    private int port2;

    @Bean(name = "instance1")
    public RedisTemplate<String, String> redisTemplateInstance1() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                host1, port1
        );
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        jedisConnectionFactory.afterPropertiesSet();
        return template;
    }

    @Bean(name = "instance2")
    public RedisTemplate<String, String> redisTemplateInstance2() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                host2, port2
        );
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        jedisConnectionFactory.afterPropertiesSet();
        return template;
    }
}
