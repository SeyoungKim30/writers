create table message(
                        sender varchar(20),
                        sendgingmessage varchar(200),
                        sendingtime DATETIME
);

insert into message values ('admin',
                     'hello world', now());

select * from message;