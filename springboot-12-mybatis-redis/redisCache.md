Spring Cache 注解

    @Cacheable: 将数据保存到缓存；
    @CacheEvict: 将数据从缓存删除；失效模式
    @CachePut: 不影响方法执行更新缓存；双写模式
    @Caching: 组合多个缓存操作；
    @CacheConfig: 在类级别共享缓存的相同配置；
    
    注：Cacheable和CachePut的返回值一定要一致，详细看代码UserServiceImpl
    
Spring-Cache的不足之处
    
    读模式
        缓存穿透：查询一个null数据；
            解决方案：缓存空数据；
        缓存击穿：大量并发进来同时查询一个正好过期的数据；
            解决方案：默认是无加锁的；使用@Cacheable(sync = true)来解决击穿问题；
        缓存雪崩：大量的key同时过期；
            解决方案：加过期时间；
    写模式：（缓存与数据库一致）
        读写加锁；
        引入Canal，感知到MySQL的更新去更新Redis；
        读多写多，直接去数据库查询就行；
        
    总结：
        常规数据（读多写少，即时性，一致性要求不高的数据，完全可以使用Spring-Cache）
        写模式(只要缓存的数据有过期时间就足够了)；
        特殊数据：特殊设计。