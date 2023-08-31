
    create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table endereco (
        id bigint not null auto_increment,
        usuario_id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        estado varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('AGREGADO','AMIGA','AMIGO','AVÓ','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMÃ','IRMÃO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','VOVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB;

    alter table eletrodomestico 
       add constraint FKhgw3tpuk6la5w12i5u6q55i17 
       foreign key (endereco_id) 
       references endereco (id);

    alter table endereco 
       add constraint FKq85re3w94a2sn7qnxvpio1afj 
       foreign key (usuario_id) 
       references pessoa (id);

    alter table pessoa 
       add constraint FKei4abnsw085kx27j89rp796ny 
       foreign key (endereco_id) 
       references endereco (id);

    create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table endereco (
        id bigint not null auto_increment,
        usuario_id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        estado varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('AGREGADO','AMIGA','AMIGO','AVÓ','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMÃ','IRMÃO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','VOVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB;

    alter table eletrodomestico 
       add constraint FKhgw3tpuk6la5w12i5u6q55i17 
       foreign key (endereco_id) 
       references endereco (id);

    alter table endereco 
       add constraint FKq85re3w94a2sn7qnxvpio1afj 
       foreign key (usuario_id) 
       references pessoa (id);

    alter table pessoa 
       add constraint FKei4abnsw085kx27j89rp796ny 
       foreign key (endereco_id) 
       references endereco (id);
insert into pessoa (nome,nascimento,parentesco,sexo,ultimo_acesso,endereco_id,pessoa_tipo,usuario_id) values ('usurio-001','1900-11-30', null , 1 ,'2023-08-06' , null , 'usuario' ,null ), ('parente-001','1900-11-30',3,1,null,null,'parente',1);

    create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table endereco (
        id bigint not null auto_increment,
        usuario_id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        estado varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('AGREGADO','AMIGA','AMIGO','AVÓ','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMÃ','IRMÃO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','VOVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB;

    alter table eletrodomestico 
       add constraint FKhgw3tpuk6la5w12i5u6q55i17 
       foreign key (endereco_id) 
       references endereco (id);

    alter table endereco 
       add constraint FKq85re3w94a2sn7qnxvpio1afj 
       foreign key (usuario_id) 
       references pessoa (id);

    alter table pessoa 
       add constraint FKei4abnsw085kx27j89rp796ny 
       foreign key (endereco_id) 
       references endereco (id);
insert into pessoa (nome,nascimento,parentesco,sexo,ultimo_acesso,endereco_id,pessoa_tipo,usuario_id) values ('usurio-001','1900-11-30', null , 1 ,'2023-08-06' , null , 'usuario' ,null ), ('parente-001','1900-11-30',3,1,null,null,'parente',1);

    create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table endereco (
        id bigint not null auto_increment,
        usuario_id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        estado varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('AGREGADO','AMIGA','AMIGO','AVÓ','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMÃ','IRMÃO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','VOVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB;

    alter table eletrodomestico 
       add constraint FKhgw3tpuk6la5w12i5u6q55i17 
       foreign key (endereco_id) 
       references endereco (id);

    alter table endereco 
       add constraint FKq85re3w94a2sn7qnxvpio1afj 
       foreign key (usuario_id) 
       references pessoa (id);

    alter table pessoa 
       add constraint FKei4abnsw085kx27j89rp796ny 
       foreign key (endereco_id) 
       references endereco (id);
insert into pessoa (nome,nascimento,parentesco,sexo,ultimo_acesso,endereco_id,pessoa_tipo,usuario_id) values ('usurio-001','1900-11-30', null , 1 ,'2023-08-06' , null , 'usuario' ,null ), ('parente-001','1900-11-30',3,1,null,null,'parente',1);

    create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table endereco (
        id bigint not null auto_increment,
        usuario_id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        estado varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('AGREGADO','AMIGA','AMIGO','AVÓ','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMÃ','IRMÃO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','VOVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB;

    alter table eletrodomestico 
       add constraint FKhgw3tpuk6la5w12i5u6q55i17 
       foreign key (endereco_id) 
       references endereco (id);

    alter table endereco 
       add constraint FKq85re3w94a2sn7qnxvpio1afj 
       foreign key (usuario_id) 
       references pessoa (id);

    alter table pessoa 
       add constraint FKei4abnsw085kx27j89rp796ny 
       foreign key (endereco_id) 
       references endereco (id);
insert into pessoa (nome,nascimento,parentesco,sexo,ultimo_acesso,endereco_id,pessoa_tipo,usuario_id) values ('usurio-001','1900-11-30', null , 1 ,'2023-08-06' , null , 'usuario' ,null ), ('parente-001','1900-11-30',3,1,null,null,'parente',1);

    create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table endereco (
        id bigint not null auto_increment,
        usuario_id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        estado varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('AGREGADO','AMIGA','AMIGO','AVÓ','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMÃ','IRMÃO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','VOVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB;

    alter table eletrodomestico 
       add constraint FKhgw3tpuk6la5w12i5u6q55i17 
       foreign key (endereco_id) 
       references endereco (id);

    alter table endereco 
       add constraint FKq85re3w94a2sn7qnxvpio1afj 
       foreign key (usuario_id) 
       references pessoa (id);

    alter table pessoa 
       add constraint FKei4abnsw085kx27j89rp796ny 
       foreign key (endereco_id) 
       references endereco (id);
insert into pessoa (nome,nascimento,parentesco,sexo,ultimo_acesso,endereco_id,pessoa_tipo,usuario_id) values ('usurio-001','1900-11-30', null , 1 ,'2023-08-06' , null , 'usuario' ,null ), ('parente-001','1900-11-30',3,1,null,null,'parente',1);

    create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table endereco (
        id bigint not null auto_increment,
        usuario_id bigint not null,
        bairro varchar(255),
        cep varchar(255),
        cidade varchar(255),
        estado varchar(255),
        logradouro varchar(255),
        numero varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('AGREGADO','AMIGA','AMIGO','AVÓ','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMÃ','IRMÃO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','VOVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB;

    alter table eletrodomestico 
       add constraint FKhgw3tpuk6la5w12i5u6q55i17 
       foreign key (endereco_id) 
       references endereco (id);

    alter table endereco 
       add constraint FKq85re3w94a2sn7qnxvpio1afj 
       foreign key (usuario_id) 
       references pessoa (id);

    alter table pessoa 
       add constraint FKei4abnsw085kx27j89rp796ny 
       foreign key (endereco_id) 
       references endereco (id);
insert into pessoa (nome,nascimento,parentesco,sexo,ultimo_acesso,endereco_id,pessoa_tipo,usuario_id) values ('usurio-001','1900-11-30', null , 1 ,'2023-08-06' , null , 'usuario' ,null ), ('parente-001','1900-11-30',3,1,null,null,'parente',1);
