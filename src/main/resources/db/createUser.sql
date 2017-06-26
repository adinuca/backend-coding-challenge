CREATE USER 'userDev'@'%' IDENTIFIED by 'passDev';

GRANT ALL ON expenses.* TO 'userDev'@'%';