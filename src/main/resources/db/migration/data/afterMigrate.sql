set foreign_key_checks=0;
delete from pessoa; 
delete from endereco; 
delete from eletrodomestico;
set foreign_key_checks=1;

alter table pessoa auto_increment = 1; 
alter table endereco auto_increment = 1; 
alter table eletrodomestico auto_increment = 1;

insert into pessoa (id,nascimento,nome,sexo,ultimo_acesso,pessoa_tipo) 
values 
		(1,"2000-01-15","USUARIO-CREATED-BOIDLE","MASCULINO","2023-08-28 16:00:55",'usuario'),
        (2,"1900-01-15","USUARIO-CREATEO-TRINITY","FEMININO","2023-08-28 19:00:55",'usuario');
        

insert 
    into
        endereco
        (id,bairro,cep,cidade,estado,logradouro,numero,usuario_id) 
    values
        (1,"centro","12345678","pindamonhagaba","SP","ROSEIRAL DO ALTO","1223ABC",1),
        (2,"luna","87654321","aguaí","SP","SOLARIO DO OESTE","555ABC",1),
        (3,"centro","33344467","cáceres","MT","AVENIDA PLANA","45676CBA",1),
        (4,"centro","56789111","recife","PE","AVENIDA NORTE","111SDF",2),
        (5,"centro","56789012","recife","PE","AVENIDA NORTE","3789SDF",2);
        
insert 
    into
        pessoa
        (id,nascimento,nome,sexo,endereco_id,parentesco,pessoa_tipo) 
    values
        (3,"2002-11-10","GISELDA APERINA","FEMININO",1,"TIA",'parente'),
        (4,"1999-10-13","LUCIUS MAGNUS","MASCULINO",1,"PAI",'parente'), 
        (5,"2000-01-27","LUZIA LUCIA DA LUZ","FEMININO",1,"MAE",'parente'),
        (6,"1990-10-10","MINERVA APERINA","FEMININO",2,"ESPOSA",'parente'),
        (7,"2015-01-01","MEDUSA APERINA","FEMININO",2,"FILHA",'parente'),
        (8,"2017-01-01","HERÁCIO PETROV","MASCULINO",5,"FILHO",'parente'), 
        (9,"1990-10-20","HORTENCIA PETROVINA","FEMININO",5,"ESPOSA",'parente');
        
insert 
    into
        eletrodomestico
        (id,endereco_id,modelo,nome,potencia) 
    values
        (1,1,"APJ-VW1","FACA ELÉTRICA JIN-IS-LYN",1.0),
        (2,1,"mega-APJ-VW1","FACA ELÉTRICA JIN-IS-LYN",5.0),
        (3,2,"APJ-VW1","FACA ELÉTRICA JIN-IS-LYN",1.0),
        (4,3,"mega-APJ-VW1","FACA ELÉTRICA JIN-IS-LYN",5.0),
        (5,4,"APJ-VW1","CADEIRA ELÉTRICA JIN-IS-LYN",13.3),
        (6,5,"mega-APJ-VW1","FACA ELÉTRICA JIN-IS-LYN",5.0);         