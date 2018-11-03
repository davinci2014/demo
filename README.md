# demo

### 使用.yml代替.properties文件
找到application.properties文件，修改文件名为application.yml即可

### 根据不同的环境使用不同的配置文件
在SpringBoot中，根据不同环境使用不同的配置文件也极为简单。
只需要添加一个不同后缀名（application-{env}.yml）的文件即可。Spring Boot会根据文件名自动查找。
如：复制application.yml >>> application-dev.yml, application-prod.yml, application-test.yml

然后需要在主配置文件application.yml中，通过spring.profiles.active属性指定需要加载的文件。

### 配置redis缓存
可能会缺少commons-pool2包依赖，添加即可。

### Secured, Roles Allowed and Pre/Post Authorize 

#### 什么时候使用@Secured, 什么时候使用@PreAuthorize
先下结论！  
具体情况，具体分析！！  
不同的业务需求，结果都是不同的！！！

写这个备注的目的，是为了给家女王大人讲述几者在使用时，有何不同。

#### 配置
首先，无论使用哪种注解(@Secured, @PreAuthorize, @PostAuthorize, @RolesAllowed), 哪种环境(Spring Boot, XML), 都需要去配置开启。

###### Spring Boot中使用 @EnableGlobalMethodSecurity注解 配置开启
```
// @Secured
@EnableGlobalMethodSecurity(securedEnabled = true)  
// @RolesAllowed  
@EnableGlobalMethodSecurity(jsr250Enabled = true)  
// @PreAuthorize, @PostAuthorize  
@EnableGlobalMethodSecurity(prePostEnabled = true)
```

###### XML中使用 <global-method-security />节点 配置开启
```
<!-- @Secured -->
<global-method-security secured-annotations="enabled" />
<!-- @RolesAllowed -->
<global-method-security jsr250-annotations="enabled" />
<!-- @PreAuthorize, @PostAuthorize -->
<global-method-security pre-post-annotations="enabled" />
```

当然，除了以上几种单独开启的情况，也可以同时开启：
###### Spring Boot中使用 @EnableGlobalMethodSecurity注解 配置开启
```
// @Secured, @PreAuthorize, @PostAuthorize
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
```
###### XML中使用 <global-method-security />节点 配置开启
```
<!-- @Secured, @PreAuthorize, @PostAuthorize -->
<global-method-security secured-annotations="enabled" pre-post-annotations="enabled" />
```

#### 使用
因为@RolesAllowed和@Secured并无不同，只不过@RolesAllowed是Java标准注解，@Secured是Spring专用。
所以我们这里只讨论@Secured。
##### @Secured和@PreAuthorize, @PostAuthorize的区别
因为@Pre和@Post是针对于Authorize而存在的，所以这里暂不讨论。这里先分别Secured和Authorize的区别。
###### 对于方法具有单一权限的情况
```
@Secured("ROLE_ADMIN")
void updateUser(User user);
@PreAuthorize("hasRole('ADMIN')")
void updateUser(User user);
```
两者并无区别，只要是角色为ADMIN的用户都可以执行updateUser方法。
###### 对于方法具有多权限的情况
```
@Secured({ "ROLE_DBA", "ROLE_ADMIN" })
void deleteUser(int id);
@PreAuthorize("hasRole('ADMIN') AND hasRole('DBA')")
void deleteUser(int id);
```
对于@Secured, 想要执行deleteUser方法时，需要角色为ROLE_DBA或ROLE_ADMIN。
对于@PreAuthorize，则为角色同时需要为ROLE_DBA和ROLE_ADMIN

由上面的两个例子，应该不难看出两者的区别：
@Secured限制多角色权限时，使用的是OR(或)的关系；
@PreAuthorize限制多角色权限时，使用的是AND(与)的关系；(SpEL支持)

所以也可以通过@PreAuthorize来实现第一种情况：
```
@PreAuthorize("hasRole('ADMIN') OR hasRole('DBA')")
void deleteUser(int id);
```

#### 实际应用
在实际项目中，业务类型的需求是不可确定的，考虑的方面也要多一些。具体需求需要具体分析，可以单独分别使用，也可以一起使用。
比如在分配用户角色时，一个用户只能具有一个角色，那么对于某些需要具有两者权限才可以执行的方法，就需要使用@PreAuthorize来实现；
或者说，在分配角色时，一个用户可以按照角色从低到高拥有多个，那么对于某些需要具有两者权限才可以执行的方法，只需要使用@Security来指定最高角色就可以；

设定角色类型：ROLE_ADMIN > ROLE_MANAGER > ROLE_USER > ROLE_GUEST
设定方法类型：同时具有ROLE_MANAGER, ROLE_USER, ROLE_GUEST三种角色的用户才可以访问
```
// 一个用户拥有多个角色(user.authorities = [ROLE_ADMIN, ROLE_MANAGER, ROLE_USER, ROLE_GUEST])
@Secured("ROLE_MANAGER")
void deleteUser(int id);

// 一个用户只有一个角色
@PreAuthorize("hasRole('ROLE_MANAGER') AND hasRole('ROLE_USER') AND hasRole('ROLE_GUEST')")
void deleteUser(int id);
```