"# WaveProject" 
1) Following steps have to be followed for the project to run. First of all mysql version 5.1 needs to be installed on the PC. Database needs to be created with the following script:
-------------Database Script Starting----------------
create schema rohit;

CREATE TABLE `reports` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `entrydate` date NOT NULL,
  PRIMARY KEY (`id`));



CREATE TABLE `reportsdata` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `report_id` int(10) unsigned NOT NULL DEFAULT '0',
  `employee_id` varchar(10) NOT NULL DEFAULT '',
  `dateWorked` date NOT NULL DEFAULT '0000-00-00',
  `hoursWorked` double NOT NULL DEFAULT '0',
  `jobGroup` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `FK_reportsdata_1` (`report_id`),
  CONSTRAINT `FK_reportsdata_1` FOREIGN KEY (`report_id`) REFERENCES `reports` (`id`));
  
  Commit;
  -------------Database Script Ending----------------
  
  2) Secondly the properties file needs to be updated to make sure that the right URL, username and password for the mysql is set up.
  
  3) Thirdly the project needs to be made part of a server. Preferably Apache Tomcat. Either a jar file can be placed or we can use eclipse to set up the server. 
  
  4) Forthly we have 2 files which can be used to upload and reporting which are UploadFile.jsp and DisplayReport.jsp. I could have made the the index or default file but I just wanted to turn in the project as quickly as possible.
