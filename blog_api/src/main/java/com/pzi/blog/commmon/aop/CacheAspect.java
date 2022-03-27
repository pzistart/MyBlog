package com.pzi.blog.commmon.aop;

import com.alibaba.fastjson.JSON;
import com.pzi.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Duration;

@Aspect
@Component
@Slf4j
public class CacheAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.pzi.blog.commmon.aop.Cache)")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //调用的方法名
            String methodName = signature.getName();


            Class[] parameterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();
            //参数 在编辑的方法中，params就是文章id
            String params = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotEmpty(params)) {
                //加密 以防出现key过长以及字符转义获取不到的情况
                params = DigestUtils.md5Hex(params);
            }
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            //获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            //缓存过期时间
            long expire = annotation.expire();
            //缓存名称
            String name = annotation.name();
            //先从redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);

//            在去listArticle(或者其他被注解标识的操作)之前，如果从Redis中获取的不是空字符串，则说明缓存中有值，不用再查，直接从缓存中读取
            if (StringUtils.isNotEmpty(redisValue)) {
                log.info("走了缓存~~~,{},{}", className, methodName);
                Result result = JSON.parseObject(redisValue, Result.class);
                System.out.println("####BEGIN###");
                System.out.println(result);
                System.out.println("####END###");

//          这里是把data属性作为Object类传递给前端，id会以long类型的方式传递，精度损失；
//              而不走缓存的listArticle是把data作为Article<Vo>类传递给前端，id会以String的方式传递
                Result result1 = JSON.parseObject(redisValue, Result.class);

//                获取result1中data的List<?> ?
                Class clazz = result1.getClass();
                Field listField = clazz.getDeclaredField("data");
                Type genericType = listField.getGenericType();
                System.out.println("Parse的data的类型是："+ genericType);

                return JSON.parseObject(redisValue, Result.class);
//                经过 ==> JSONString之后的对象是什么样的形式？
//                {"avatar":"avatar","id":12123312,"tagName":"tagName"} 这样的形式
//                并且转化那之后的Result中data是Object类型

            }

//            这个proceed()表示执行被增强的那个方法，也就是被切入点切入的那个方法
            Object proceed = pjp.proceed();
//            这里proceed返回的就是List<ArticleVo> articleVoList这个值，它里面的id是Long型的，只需把它改为String型，就可以解决精度问题
//            否则，Redis中该key无value，说明这个key在Reids中不存在或者已经过期，那么则把该key-value对存入Redis中。value就是业务层查到的Result对象的json格式
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed), Duration.ofMillis(expire));

//            System.out.println(JSON.toJSONString(proceed));
            log.info("被存入的key为 ~~~ {}",redisKey);
            log.info("存入缓存~~~ {},{}", className, methodName);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999, "系统错误");
    }

}
