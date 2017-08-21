# shiro-sso (1.共用认证信息;2.分开权限获取)
二级域名共用cookie实现sso  
标准的maven项目，基于spring，shiro，session通过redis进行共享  
使用项目需要在hosts中添加，127.0.0.1 webdemo.bigsys.com, 127.0.0.1 webdemo2.bigsys.com  
然后在idea中配置对应域名的tomcat启动即可
