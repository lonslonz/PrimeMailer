PrimeMailer
==========
Http server that relay mails to SMTP server.

- Receive mail requests via HTTP.
- Buffer mail requests and send mails periodically. 
- Remove the same mails of buffered window.
- Ignore square brackets of mail content for variables like current time when removing the same mails. 
- Mail aliasing for multiple recipients.
- Need to SMTP server's access right. 

### Installation

Prerequisite
- install ant, ivy
- jdk 1.6.x or jdk 1.7.x
- MySQL 5.0.x

Install
- download
- unzip
- ant resolve
- ant
- deploy ./PrimMailer-x.x.x.tar.gz and unzip 

Configuration
- ./conf/server.xml : copy from server.default.xml, set your port & email SMTP server information.
- ./conf/hibernate.cfg.xml : copy from hibernate.cfg.default.xml, set your mysql information. 

DB Configuration
- create database rakeflurry
- execute ./sql/create.sql on rakeflurry db.

Aliasing Configuration
- if you want to register mail aliases, insert aliases and rcpt address into tb_rcpt_alias. When there is alias in tb_rcpt_alias matching with "to" address, mailer will send a mail to "rcpt_addr" of the tb_rcpt_alias.


Server management
- startup : ./bin/start.sh
- stop : ./bin/stop.sh


### API

When server is installed, default addresss is http://localhost:8100/mail/.

##### /mail/send

Description
- send mail from HTTP get request
Method
- HTTP GET
Parameters
- from  : mail address
- to : mail address, can use comma and semi-colon for many recipients.
- subject : mail subject
- msg : mail content
- (optional) : can be defined in server.xml or GET parameters.
 - ssl : use ssl
 - smtpServer : smtp server address
 - smtpPort : smtp server port
 - id : smtp server's id
 - password : smtp server's password

eg.)
``
http://rakeflurry1:8200/mail/send?from=lonslonz@daum.net&to=lonslonz@daum.net&subject=testget&msg=msg
``

##### /mail/sendpost

Description
- HTTP Post로 mail보냄
- Post message를 json으로 정의
Method
- HTTP POST
Parameters
- Completely the same as parameters of /mail/send. Only difference is using son. 

eg.)
``
http://localhost:8200/mail/sendpost
``

Post message : 
```json
{
"from" : "from@smtp.server.com",
"to" : "recipient@smtp.server.com",
"subject" : "my test local",
"msg" : "now\nMy Test\nnow is 2013",
"smtpServer" : "your.smtp.server.com",
"smtpPort" : "465",
"id" : "your id",
"password" : "your password",
"ssl" : "true"
}
```
