PrimMailer
==========
Relay mail server to SMTP server.

- Receive mail requests via HTTP.
- Buffering mail requests and send mails periodically. 
- Remove the same mails buffered.
- Ignore square brackets of mail content for current time when removing the same mails. 
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
- ./conf/server.xml : set your port & email SMTP server information.
- ./conf/hibernate.cfg.xml : set your mysql. 

Server management
- startup : ./bin/start.sh
- stop : ./bin/stop.sh
