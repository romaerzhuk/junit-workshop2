DROP SCHEMA IF EXISTS junit CASCADE;
DROP USER IF EXISTS junit_user;
CREATE USER junit_user WITH password 'junit_psw';
CREATE SCHEMA junit AUTHORIZATION junit_user;