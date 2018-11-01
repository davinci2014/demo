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