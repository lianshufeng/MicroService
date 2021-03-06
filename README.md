# Framework
Spring cloud and boot Framework


## 依赖
- consul
- starter-config
- spring-cloud-bus
- starter-zipkin 
- spring gateway
- starter-openfeign


## 特点：
- 微服务设计：分布式，高可用，水平扩展
- 用户统一身份认证
- 内聚功能模块
- 约定配置，拧包入住

	
## 环境
- JDK 11+ 
- IDE idea (推荐插件) 
    - lombok plugin 
    - Spring Assistant 
    - Maven Helper
    - YAML/Ansible support
    - JRebel
    - cmd Support
 
## 编译与部署
- jar : 需要修改pom配置


## 启动顺序
应用中心 -> 配置中心 -> 用户中心 -> [自定义模块] -> 链路追踪 -> 网关 



### 服务
- 依赖服务
    - kafka
        - 消息总线
    - redis 
        - 用户令牌
    - elasticsearch
        - 搜索引擎
    - mongo
        - 用户数据
        - 业务数据
    - zipkin
        - 链路追踪
    - consul
        - 注册发现服务 
- 核心服务
    - 配置中心 (centers/ApplicationCenter/Config)
        - 配置集中管理
    - 网关 (centers/ApplicationCenter/Gateway)
        - 网关
        - 公测(预发布)环境
    - 用户中心 （centers/AuthCenter）
        - 用户身份认证 (颁发用户令牌与密钥令牌)
        - 统一鉴权功能 
- 依赖模块
    - 资源锁模块
        - 任务队列
    - 缓存模块
        - 注解缓存
    - DB模块
        - spring data jpa sql
        - spring data jpa mongo
        - spring data jpa es
        
        