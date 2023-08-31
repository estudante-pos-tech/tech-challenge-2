 create table eletrodomestico (
        potencia float(53),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        modelo varchar(255),
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB default charset=UTF8MB4;

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
    ) engine=InnoDB default charset=UTF8MB4;

    create table pessoa (
        nascimento date,
        endereco_id bigint,
        id bigint not null auto_increment,
        ultimo_acesso datetime,
        pessoa_tipo varchar(31) not null,
        nome varchar(255),
        parentesco enum ('ESPOSO','ESPOSA','AGREGADO','AMIGA','AMIGO','VOVO','CONJUGE','CUNHADA','CUNHADO','FILHA','FILHO','IRMA','IRMAO','MAE','NAMORADA','NENHUM','NOMORADO','PAI','SOGRA','SOGRO','TIA','TIO','AVO'),
        sexo enum ('FEMININO','MASCULINO'),
        primary key (id)
    ) engine=InnoDB default charset=UTF8MB4;

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
