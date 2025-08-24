day1 MybatisPlus

service lambda不用new可以用链式法则.

生成代码插件

DB静态工具类从mybatis里获取mapper

day2
虚拟机密码123456 mysql密码123 用桥接模式配置了代理 虚拟机192.168.1.3 主机192.168.1.4 clash开启局域网模式   为啥主机ip在后面？

ubuntu的root没懂 很注重安全？

挂载容器初始化了mysql

导入hmall项目

docker启动nginx

创建了网络 可以用容器名相互访问

docker-compose.yml快速启动项目所有环境

本地编写代码后提交git 发现提交后对代码重新编译 跑测试 然后部署 这就是devops吗 后面再看看自动化测试和jenkins

day3 day4 微服务

根据业务拆分商城项目

以苍穹外卖为例，项目可以拆分为：
- 业务服务：
    - 用户服务：用户、地址、登录等相关业务
    - 产品服务：店铺、分类、菜品、套餐等业务
    - 交易服务：订单、购物车业务
    - 数据服务：工作台、报表统计等业务
- 基础服务：
    - 支付服务：支付相关业务
    - 文件服务：文件上传功能


不同微服务之间的调用变成了http请求，openfegin简化调用，抽取一个api模块暴露接口
然后后端服务很多就乱了，前端只转发8080，就需要一个网关来转发请求和鉴权
网关解析token中的用户保存到请求头，转发给相应微服务。

微服务于微服务之间也要相互调用，所以发起fegin请求时要把tl中的用户数据带上
nacos用来发现服务和管理配置
springcloud先读nacos配置，然后springboot再初始化
还可以热更新一些值

今天的问题：应该只开放8080端口让前端访问，但其他的端口服务也可以被外部访问到，应该其他端口服务拒绝访问，只接受内部相互访问

day5 Sentinel

服务启动后要访问一次Sentinel才显示

jmeter测试时候别点上面绿色按钮启动，那个是所有测试都启动
oom报错 加上--ulimit nofile=65536:65536 \

docker run --name seata \
-p 8099:8099 \
-p 7099:7099 \
-e SEATA_IP=192.168.1.3 \
-v home/xf/seata:/seata-server/resources \
--privileged=true \
--ulimit nofile=65536:65536 \
--network hm-net \
-d \
seataio/seata-server:1.5.2

AT模式可以执行完就提交 然后把原来数据存到数据库 这样可以提高效率不用卡这数据库 如果出现问题按照undolog回滚数据，否则删除原来的数据