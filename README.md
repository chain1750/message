# 消息系统

通过消息队列来消费消息发送

```yaml
# 短信
aliyun:
  sms:
    access-key-id: ''
    access-key-secret: ''
    sign-name: ''
# 系统消息
system-message:
  salt: ''
  send-url: ''
# 邮件消息
spring:
  mail:
    host: smtp.163.com
    username: ''
    password: ''
    port: 465
    protocol: smtps
    default-encoding: UTF-8
```