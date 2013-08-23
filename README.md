PrimeMailer
==========
Http server that relay mails to SMTP server.

- Receive mail requests via HTTP.
- Buffer mail requests and send mails periodically. 
- Remove the same mails of buffered window.
- Ignore square brackets of mail content for variables like current time when removing the same mails. 
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

Server management
- startup : ./bin/start.sh
- stop : ./bin/stop.sh
