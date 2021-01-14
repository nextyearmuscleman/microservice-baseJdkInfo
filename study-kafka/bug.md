* 使用kafkaTool无法链接腾讯云服务器里面的kafka服务
   * kafka/conf/server.properties里面的SOCKET SERVER SETTING,添加配置host.name=内网ip。放开里面的advertised.listeners=PLAINTEXT://公网ip:port