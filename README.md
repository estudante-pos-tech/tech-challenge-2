## [tech-challenge-1](https://github.com/estudante-pos-tech/tech-challenge-1#readme)
# tech-challenge-2 (5.1MB)

Implementação ***CRUD** Java Rest Api, com **dados persistentes** em banco de dados **mysql dockerizado** e **ORM** JPA/Hibernate*.<br>
Os endpoints são **usuarios**, enderecos, pessoas e eletrodomesticos
## Relatório Técnico

### Tecnologias e ferramentas utilizadas
<div style='text-align: justify;'>
  
**DDD**, Debian host, Oracle virtual machine, bash scripting, git, IDE eclipse STS-4 (spring tool suit), maven, Spring Boot, Spring MVC, Spring Data JPA, Spring DevTools, Flyway, Mysql, Lombok, [model mapper](https://modelmapper.org/), [apache commons lang3](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3), curl, Insomnia e Postman são as ferramentas e tecnologias usadas para desenvolver a api rest e a api sistema.

### Resumo técnico. Visão panorâmica.

#### UTC

O sistema **armazena e devolve data/hora em UTC** (sem offsets, utc Z). Isso permite que qualquer leitura da informção data/hora possa ser convertida para o local, time-zone, fuso-horário do cliente da api, mantendo consistência de dados. De maneira análoga, qualquer input de data/hora, vindo de um fuso-horário qualquer, será convertido para **UTC ZERO**, e armazenado em **UTC Z**.
<br><br><br> O sistema usa o padrão **ISO-8601** para formatar data/hora.
<br><br><br>
#### VERSIONAMENTO 
##### flyway e versionamento profissinal de schemas
O projeto usa ferramenta **flyway** para fazer **versionamento profissional** de schema do banco de dados. 
<br>O banco de dados está na versão 0001; a V0001 foi criada à partir do DDL gerado pelo Hibernate schema generator.
<br><br><br>
#### PERSISTÊNCIA E DOCKER
**Está implementada persistência de dados usando bando de dados**.<br>
**Mysql** é o dbms escolhido. Hibernate é a implementação JPA usada. <br>
##### dockerização do mysql
O Mysql foi **dockerizado**, rodando o comando <br>**sudo docker container run -d -p 3316:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes --name apiTechChallenge-mysql mysql:8.0**
##### flyway dados de teste
Uma pequena massa de dados é gerada pelo flyway (**afterMigrate.sql**), quando o **spring default profile** é o profile de desenvolvimento (application-dev.properties)
<br><br><br>
#### PAGINAÇÃO E ORDENAÇÃO NA WEB API
Decidiu-se implementar paginação e ordenação **APENAS** na listagem de **TODOS** usuários e na listagem de **TODOS** endereços e na listagem de **TODOS** parentes(pessoas) e na listagem de **TODOS** eletrodomésticos.<br>
###### customização da serialização das páginas do Spring
Notou-se que a representação JSON da página que o Spring cria tem propriedades demais. Talvez até redundantes. <br> Observe o exemplo:

		{
		    "content": [
      			{
		            "id": 1,
		            "nome": "USUARIO-CREATED-BOIDLE",
		            "nascimento": "2000-01-15",
		            "sexo": "MASCULINO",
		            "ultimoAcesso": "2023-08-28T16:00:55Z"
		        },
		        {
		            "id": 2,
		            "nome": "USUARIO-CREATEO-TRINITY",
		            "nascimento": "1900-01-15",
		            "sexo": "FEMININO",
		            "ultimoAcesso": "2023-08-28T19:00:55Z"
		        }   
		    ],
		    "pageable": {
		        "sort": {
		            "sorted": false,
		            "unsorted": true,
		            "empty": true
		        },
		        "pageNumber": 0,
		        "pageSize": 20,
		        "offset": 0,
		        "unpaged": false,
		        "paged": true
		    },
		    "totalElements": 2,
		    "totalPages": 1,
		    "last": true,
		    "first": true,
		    "sort": {
		        "sorted": false,
		        "unsorted": true,
		        "empty": true
		    },
		    "number": 0,
		    "numberOfElements": 2,
		    "size": 20,
		    "empty": false
		}

Diante dessa constatação, escreveu-se um **serializador customizado**, o **rm349040.techchallenge2.util.PageJsonSerializer**. Ele extende o **JsonSerializer**, do Jackson. 
<br><br>O serializador serializa objetos Page do Spring de maneira + enxuta.<br><br>
O customizado json serializado, representando um Page, é um json bem enxuto, como o abaixo :

		{
		    "content": [
		        {
		            "id": 1,
		            "nome": "USUARIO-CREATED-BOIDLE",
		            "nascimento": "2000-01-15",
		            "sexo": "MASCULINO",
		            "ultimoAcesso": "2023-08-28T16:00:55Z"
		        },
		        {
		            "id": 2,
		            "nome": "USUARIO-CREATEO-TRINITY",
		            "nascimento": "1900-01-15",
		            "sexo": "FEMININO",
		            "ultimoAcesso": "2023-08-28T19:00:55Z"
		        }
		    ],
		    "size": 20,
		    "totalElements": 2,
		    "totalPages": 1,
		    "number": 0
		}
 
<br><br><br>
#### ARQUITETURA
###### ARQUITERURA - DDD e Desacoplamento
**Nesta fase-2**, se experimentou uma arquitetura baseada no domínio e em desacoplamento. Focou-se em DDD : foco no modelo de domínio; foi dada pouca, ou quase nenhuma, importância para periféricos, como infraestrutura de persistência, ou periféricos do tipo se o cliente da aplicação é uma rest api ou se o cliente da aplicação é um test code ou se o cliente é uma aplicação java-FX... isso pouco importa.
<br><br>A arquitetura baseada no modelo do domínio e em desacoplamento é quase que totalmente despreocupada com entornos. Ela permite, por exemplo, substituir facilmente a tecnologia de persistência de dados baseada em banco de dados por outra tecnologia de persistência, por exemplo, tecnologia baseada em aquivos de texto, como xml, json, whaterever to come, etc. **Mais** : por causa do desacoplamento, por exemplo, a aplicacao permite que seja usada uma implementação de banco de dados em memoria, bastando para isso que a implementação assine os contratos com os repositórios da aplicação (implemente as interfaces repositório). <br>**Mais ainda** : o desacoplamento também permite que simplesmente a rest api, um dos possíveis clientes da aplicação, seja substituida por outro cliente, por exemplo, uma outra aplicacao java. 
<br>**A aplicação, em si mesma, é uma API**.
###### ARQUITERURA - desacoplamento e ORM
Para se elevar ao próximo nível a consistência da decisão por uma arquitetura baseada em desacoplamento, onde, por exemplo, "pouco importa a tecnologia de persistência de dados", todo metadado ORM precisa se tornar materializado num arquivo xml. Assim, serão colocados, em lugares diferentes, objetos do domínio da aplicação e informações de persistência.
<br> Isso seria o graal no contexto desta arquitetura, mas os metadados ORM estão na forma de @annotations e , neste arquitetura, "poluem" com @annotações as entidades do modelo de domínio.
<br><br>
##### ARQUITETURA - Integridade de dados
Por causa desta decisão, a integridade de dados da aplicação é baseada em validações que **não dependem do banco de dados** . Por exemplo, o SDJ (Spring Data Jpa) checa integridade e lança DataIntegrityVioloationException, mas a aplicação não se baseia em SDJ. Apenas o usa nesta implementação;  a aplicação faz suas próprias validações de consistência de regras de neǵocio. Assim, toda operação CRUD manterá, no repositório, objetos consistentes com as regras de negócio.
<br><br>
##### ARQUITETURA - Performance
###### performance da camada de persistência
Alguma atenção foi dada à performance da camada de persistência : os relacionamentos de hierarquia de classes foram mapeados para JPA Single Table Strategy, reduzindo o número de joins; relacionamentos @OneToMany e @ManyToMany, que disparavam vários selects em pontos críticos, foram optimizados usando uma @Query adequada.
<br><br>
##### ARQUITETURA - Customização do Spring Data Jpa (SDJ)
Quanto ao uso do SDJ (Spring Data Jpa), a classe SimpleJpaRepository<T, ID>, que é a implementação padrão da interface JpaRepository<T,ID>, foi customizada criando-se a classe CustomJpaRepositoryImpl<T,ID>; esta classe implementa uma interface, também customizada, CustomJpaRepository<T, ID>. Isso tudo feito para que possamos escrever nossos próprios métodos quando não forem suficientes os da implementação padrão. Por exemplo, foi decidido ser interessante saber quais são as 10 primeiras entidades de um dado tipo T, então, na interface customizada CustomJpaRepository<T, ID>, foi declarado o método Collection< T > find10First() e ele foi implementado na classe CustomJpaRepositoryImpl<T,ID> :

	@Override
	public Collection<T> find10First() {
		
		String jpql = "from " + getDomainClass().getName();
		
		Collection<T> tenFirst = entityManager.createQuery(jpql, getDomainClass())
				.setMaxResults(10)
				.getResultList();
	
		return tenFirst;
	}
<br><br>
##### ARQUITETURA - Design da web API : endpoints fluentes
"Maior semântica.Melhor" . Esse foi o princípio para desenhar os endpoints. Facilidade de leitura das uris e facilidade de saber, num piscar de olhos, o que o endpoit faz, são os princípios usados aqui. Por exemplos:
	<br> 
 		<br> usuarios/1/meus-enderecos
   		<br><br> usuarios/1/meus-parentes
     		<br><br> usuarios/1/meus-eletrodomesticos
		<br><br> enderecos/deste-usuario/1 
		<br><br> enderecos/quem-mora-aqui/1
		<br><br> pessoas/que-moram-comigo/5
		<br><br> pessoas/que-moram-neste-endereco/1
		<br><br> pessoas/deste-usuario/1 ( quer dizer quais são os parentes deste usuário !!! Este é o endpoint mais obscuro, criptografado, difícil de saber seu propósito )
		<br><br> eletrodomesticos/deste-usuario/2
		<br><br> eletrodomesticos/que-estao-neste-endereco/1

<br><br><br>
#### JAVA POWER - Streams, Java Generics e Programação Funcional
No projeto, o código java foi quase que totalmente (até onde foi possível) escrito usando java streams e programação funcional e **java generics**. <br>Por exemplo, na camada **api**, é necessário o uso exaustivo de conversões entre coleções de entidades de vários tipos e coleções de DTOs de vários tipos.
<br>Um método que usa **java generics** foi criado para retornar coleções de dtos de qualquer tipo, recebendo como fonte, uma  coleção de entidades de qualquer tipo que seja. Aqui está ele :
##### poderoso java generics em ação
</div>

	     /**
	     * Maps a T type collection to a S type DTO collection.
	     * @param <T> the T type
	     * @param <S> the S type DTO
	     * @param sourceCollection the source collection of elements of T type
	     * @param DTO the target type this algorithm will build a collection of
	     * @return a collection of elements of S type DTO, 
	     * each of them identified/mapped by its correspondent at source collection of T type
	     * (sounds monumental, but it is very simple and useful)
	     */
	    public <T, S> Collection<S> toDtoCollection(Collection<T> sourceCollection, Class<S> DTO) {
	
	    	if(sourceCollection == null || DTO == null) return new HashSet<S>(); 
	    	
	    	return sourceCollection
	    	.stream()
			.map(typeT -> {
				try {
					S dto = DTO.newInstance();
					identify(typeT, dto);
					return dto;
				} catch (Exception e) {
				}
				return null;
			}).collect(Collectors.toSet());
	
	    }

##### **+ 1 exemplo de generics : método chamado da web api** :

	    /**
	     * Paging and sorting converter facility method. Invoked usefully when we want to turn an entity collection into a DTO instances spring page.   
	     * @param <T> the type T DTO
	     * @param Page<T> A Spring page.
	     * @param cadastroService the service we get entities from.
	     * @param pageable a spring generated pageable instance (or any other customized pageable instance)  
	     * @param DTO the dto
	     * @return a T type DTO instances Spring Page.
	     */
	      public <T> Page<T> toPage(CadastroService cadastroService, Pageable pageable, Class<T> DTO) {
			if(cadastroService == null || pageable == null || DTO == null)	return Page.empty();
			Page<?> page = cadastroService.listar(pageable);
			Collection<?> sourceCollection = page.getContent();
			ArrayList<T> dtoCollecion = new ArrayList<T>(toDtoCollection(sourceCollection, DTO));
			return new PageImpl<T>(dtoCollecion, pageable, page.getTotalElements());
	      }
<br><br><br>
### Desafios técnicos

<div style='text-align: justify;'>
<b>Nesta fase-2</b>, o desafio tem sido o de escrever este extenso, muito comprido, relatório/README.md

</div>
<br><br><br><br>

## Documentação da API
O ***CRUD*** foi implementado seguindo o mapa : 
-  VERBO HTTP **POST** - **CRIAR**
-  VERBO HTTP **PUT** - **ATUALIZAR**
-  VERBO HTTP **DELETE** - **DELETAR**
-  VERBO HTTP **GET** - **LISTAR**
<br><br>

### **OBSERVAÇÃO**: dados de teste
###### flyway dados de teste
Uma pequena massa de dados é gerada pelo flyway (**afterMigrate.sql**), quando o **spring default profile** é o profile de desenvolvimento (application-dev.properties)

### **OBSERVAÇÃO**: Um CRUD para endpoint USUARIOS foi criado. **É necessário ter usuário criado para o sistema funcionar.**
<br>A documentação da API dá suporte ao CRUD de usários.
<br>

### **OBSERVAÇÃO**: No ambiente em que este projeto foi desenvolvido, para o host http://localhost:8080 foi dado um nome mais legal :  **api.tech-challenge**
<br>Nas urls dos exemplos curls da documentação da api, por exemplo, aparece **api.tech-challenge/eletrodomesticos** ao invés de http://localhost:8080/eletrodomesticos
<br>Se for rodar os exemplos curls, substitua o nome do servidor **api.tech-challenge** pelo local onde está rodando seu servidor.

<br><br>

___
#### Endpoint usuarios : REQUESTS, Curls, RESPONSES
___
##### **POST**<br><br>
No body da **POST** request, devem estar os pares key-value: 
  -    ***nome*** , *não em-branco e no máximo 60 caracteres* 
  -    **nascimento**, *não-nulo e NÃO pode ser "hoje" ou qualquer outro dia depois de "hoje"*
  -    ***sexo***, *não-nulo e MASCULINO ou FEMININO*

*EXEMPLO:*   

        POST api.tech-challenge/usuarios
        Content-Type: application/json
        
		{
			"nome" : "USUARIO-CREATEO-BOIDLE",
			"nascimento":"2000-01-15",
			"sexo":"MASCULINO"
		}
        
        curl -i -X POST --location 'http://localhost:8080/usuarios' --header 'Content-Type: application/json' --data 
		'{

			"nome" : "USUARIO-CREATEO-BOIDLE",
			"nascimento":"2000-01-15",
			"sexo":"MASCULINO"
		}'
        
        HTTP/1.1 201
        Content-Type: application/json
             
        	{

			"nome" : "USUARIO-CREATEO-BOIDLE",
			"nascimento":"2000-01-15",
			"sexo":"MASCULINO"
		}


___

##### **GET**<br><br>

  ###### **GET ALL**<br><br>
    
        GET api.tech-challenge/usuarios
    
        curl -i -X GET --location "api.tech-challenge/usuarios"
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	        [
			    {
			        "id": 2,
			        "nome": "USUARIO-CREATEO-TESTE",
			        "nascimento": "2001-01-14",
			        "sexo": "FEMININO",
			        "ultimoAcesso": "2023-08-24T12:32:34Z"
			    },
			    {
			        "id": 1,
			        "nome": "USUARIO-CREATEO-BOIDLE",
			        "nascimento": "2000-01-14",
			        "sexo": "MASCULINO",
			        "ultimoAcesso": "2023-08-24T12:32:12Z"
			    }
		]

  ###### **GET ALL PAGINADO/ORDENADO**<br><br>

	  GET http://localhost:8080/usuarios?size=2&page=0&sort=nome&sort=id
	
	  curl -i -X GET --location 'http://localhost:8080/usuarios?size=2&page=0&sort=nome&sort=id'

		   {
		    "content": [
		        {
		            "id": 1,
		            "nome": "USUARIO-CREATED-BOIDLE",
		            "nascimento": "2000-01-15",
		            "sexo": "MASCULINO",
		            "ultimoAcesso": "2023-08-28T16:00:55Z"
		        },
		        {
		            "id": 2,
		            "nome": "USUARIO-CREATEO-TRINITY",
		            "nascimento": "1900-01-15",
		            "sexo": "FEMININO",
		            "ultimoAcesso": "2023-08-28T19:00:55Z"
		        }
		    ],
		    "size": 2,
		    "totalElements": 2,
		    "totalPages": 1,
		    "number": 0
		}

  ###### **GET BY ID**<br><br>

        GET api.tech-challenge/usuarios/1
    
        curl -i -X GET --location "api.tech-challenge/usuarios/1"
        
        HTTP/1.1 200
        Content-Type: application/json
        
	        {
			    "id": 1,
			    "nome": "USUARIO-CREATEO-BOIDLE",
			    "nascimento": "2000-01-14",
			    "sexo": "MASCULINO",
			    "ultimoAcesso": "2023-08-24T12:32:12Z",
			    "meusEnderecos": [
			        {
			            "id": 1,
			            "cep": "12345678",
			            "logradouro": "ROSEIRAL DO ALTO",
			            "numero": "1223ABC",
			            "bairro": "centro",
			            "cidade": "pindamonhagaba",
			            "estado": "SP"
			        }
			    ],
			    "meusParentes": [
			        {
			            "id": 3,
			            "nome": "PARENTE-TESTE",
			            "nascimento": "1234-01-14",
			            "sexo": "MASCULINO",
			            "parentesco": "PAI",
			            "endereco": {
			                "id": 1
			            }
			        }
			    ],
			    "meusEletrodomesticos": [
			        {
			            "id": 1,
			            "nome": "TESOURA ELETRICA JIN-IS-LYN",
			            "modelo": "KVW-678",
			            "potencia": 1.0,
			            "endereco": {
			                "id": 1
			            }
			        }
			    ]
		}

  ###### **GET BY QUERY PARAMETERS**<br><br>
    
        GET api.tech-challenge/usuarios/por-parametros?nome=xxx&sexo=MASCULINO&nascimento=yyyy-MM-dd
    
        curl -i -X GET --location 'http://localhost:8080/usuarios/por-parametros?nome=USU&sexo=MASCULINO'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	        [
			    {
			        "id": 1,
			        "nome": "USUARIO-CREATEO-BOIDLE",
			        "nascimento": "2000-01-14",
			        "sexo": "MASCULINO",
			        "ultimoAcesso": "2023-08-24T12:32:12Z"
			    }
		]


###### **GET ENDEREÇOS DESTE USUÁRIO**<br><br>
    
        GET api.tech-challenge/usuarios/1/meus-enderecos
    
        curl -i -X GET --location 'http://localhost:8080/usuarios/1/meus-enderecos'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	        [
			    {
			        "id": 2,
			        "cep": "87654321",
			        "logradouro": "SOLARIO DO OESTE",
			        "numero": "555ABC",
			        "bairro": "luna",
			        "cidade": "aguaí",
			        "estado": "SP"
			    },
			    {
			        "id": 3,
			        "cep": "33344467",
			        "logradouro": "AVENIDA PLANA",
			        "numero": "45676CBA",
			        "bairro": "centro",
			        "cidade": "cáceres",
			        "estado": "MT"
			    },
			    {
			        "id": 1,
			        "cep": "12345678",
			        "logradouro": "ROSEIRAL DO ALTO",
			        "numero": "1223ABC",
			        "bairro": "centro",
			        "cidade": "pindamonhagaba",
			        "estado": "SP"
			    }
		]
  ###### **GET PARENTES DESTE USUÁRIO**<br><br>
    
        GET api.tech-challenge/usuarios/1/meus-parentes
    
        curl -i -X GET --location 'http://localhost:8080/usuarios/1/meus-parentes'

	HTTP/1.1 200
        Content-Type: application/json

 	[
			    {
			        "id": 3,
			        "nome": "GISELDA APERINA",
			        "nascimento": "2002-11-10",
			        "sexo": "FEMININO",
			        "parentesco": "TIA",
			        "endereco": {
			            "id": 1
			        }
			    },
			    {
			        "id": 7,
			        "nome": "MEDUSA APERINA",
			        "nascimento": "2015-01-01",
			        "sexo": "FEMININO",
			        "parentesco": "FILHA",
			        "endereco": {
			            "id": 2
			        }
			    },
			    {
			        "id": 4,
			        "nome": "LUCIUS MAGNUS",
			        "nascimento": "1999-10-13",
			        "sexo": "MASCULINO",
			        "parentesco": "PAI",
			        "endereco": {
			            "id": 1
			        }
			    },
			    {
			        "id": 5,
			        "nome": "LUZIA LUCIA DA LUZ",
			        "nascimento": "2000-01-27",
			        "sexo": "FEMININO",
			        "parentesco": "MAE",
			        "endereco": {
			            "id": 1
			        }
			    },
			    {
			        "id": 6,
			        "nome": "MINERVA APERINA",
			        "nascimento": "1990-10-10",
			        "sexo": "FEMININO",
			        "parentesco": "ESPOSA",
			        "endereco": {
			            "id": 2
			        }
			    }
	]

  ###### **GET ELETRODOMÉTICOS DESTE USUÁRIO**<br><br>
    
        GET api.tech-challenge/usuarios/1/meus-eletrodomesticos
    
        curl -i -X GET --location 'http://localhost:8080/usuarios/1/meus-eletrodomesticos'

	HTTP/1.1 200
        Content-Type: application/json

 	[
			    {
			        "id": 4,
			        "nome": "FACA ELÉTRICA JIN-IS-LYN",
			        "modelo": "mega-APJ-VW1",
			        "potencia": 5.0,
			        "endereco": {
			            "id": 3
			        }
			    },
			    {
			        "id": 2,
			        "nome": "FACA ELÉTRICA JIN-IS-LYN",
			        "modelo": "mega-APJ-VW1",
			        "potencia": 5.0,
			        "endereco": {
			            "id": 1
			        }
			    },
			    {
			        "id": 1,
			        "nome": "FACA ELÉTRICA JIN-IS-LYN",
			        "modelo": "APJ-VW1",
			        "potencia": 1.0,
			        "endereco": {
			            "id": 1
			        }
			    },
			    {
			        "id": 3,
			        "nome": "FACA ELÉTRICA JIN-IS-LYN",
			        "modelo": "APJ-VW1",
			        "potencia": 1.0,
			        "endereco": {
			            "id": 2
			        }
			    }
	]
        
___    

##### **PUT**<br><br>

Na url da **PUT** request deve estar o id do recurso que se deseja atualizar

-    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*

No body da **PUT** request, devem estar os pares key-value: 
-    **nome** , *não em-branco e no máximo 60 caracteres* 
-    **nascimento**, *não-nulo e NÃO pode ser "hoje" ou qualquer outro dia depois de "hoje"*
-    **sexo**, *não-nulo e MASCULINO ou FEMININO*

*EXEMPLO:*    

         PUT api.tech-challenge/usuarios/1
         Content-Type: application/json
    
	        {
		    "nome": "USUARIO-1-EDITADO",
		    "nascimento": "1000-01-15",
		    "sexo": "MASCULINO",
	        }
        
        curl  -i --location --request PUT 'http://localhost:8080/usuarios/1' --header 'Content-Type: application/json' --data 
		'{
			    "nome": "USUARIO-1-EDITADO",
			    "nascimento": "1000-01-15",
			    "sexo": "MASCULINO"	    
		}'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
    
        {
			    "id": 1,
			    "nome": "USUARIO-1-EDITADO",
			    "nascimento": "1000-01-15",
			    "sexo": "MASCULINO",
			    "ultimoAcesso": "2023-08-24T13:25:37Z",
	}


___    
<br>

##### **DELETE**<br><br>

No path da **DELETE** request, deve estar o ***id*** do recurso que se deseja deletar: 
-    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*
    
          DELETE api.tech-challenge/usuarios/1
                 
          curl -i -X DELETE --location "api.tech-challenge/usuarios/1"
	             
          HTTP/1.1 204


___
#### Endpoint enderecos : REQUESTS, Curls, RESPONSES
___
##### **POST**<br><br>
No body da **POST** request, devem estar os pares key-value:
  -    ***cep*** , *não em-branco e no máximo 8 caracteres(opcional hifen)* 
  -    ***logradouro*** , *não em-branco e no máximo 60 caracteres* 
  -    ***numero***, *não em-branco e no máximo 10 caracteres*
  -    ***bairro***, *não em-branco e no máximo 40 caracteres*
  -    ***cidade***, *não em-branco e no máximo 50 caracteres*
  -    ***estado***, *não em-branco e no máximo 30 caracteres*
  -    ***usuario***, "não em branco e com id válido"

*EXEMPLO:*   

        POST api.tech-challenge/enderecos
        Content-Type: application/json
        
        {
		    "cep":"12345678",
		    "logradouro":"ROSEIRAL DO ALTO",
		    "numero":"1223ABC",
		    "bairro":"centro",
		    "cidade":"pindamonhagaba",
		    "estado":"SP",
		    "usuario":{
		        "id" : 1
		    }
	}
        
        curl --location 'http://localhost:8080/enderecos' --header 'Content-Type: application/json' --data 
	'{
		    "cep":"12345678",
		    "logradouro":"ROSEIRAL DO ALTO",
		    "numero":"1223ABC",
		    "bairro":"centro",
		    "cidade":"pindamonhagaba",
		    "estado":"SP",
		    "usuario":{
		        "id" : 1
		    }
	}'
        
        HTTP/1.1 201
        Content-Type: application/json
             
        {
		    "id": 1,
		    "cep": "12345678",
		    "logradouro": "ROSEIRAL DO ALTO",
		    "numero": "1223ABC",
		    "bairro": "centro",
		    "cidade": "pindamonhagaba",
		    "estado": "SP",
		    "usuario": {
		        "id": 1
		    }
}


___

##### **GET**<br><br>

  ###### **GET ALL**<br><br>
    
        GET api.tech-challenge/enderecos
    
        curl -i -X GET --location "api.tech-challenge/enderecos"
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
        [
		    {
		        "id": 1,
		        "cep": "12345678",
		        "logradouro": "ROSEIRAL DO ALTO",
		        "numero": "1223ABC",
		        "bairro": "centro",
		        "cidade": "pindamonhagaba",
		        "estado": "SP",
		        "usuario": {
		            "id": 1
		        }
		    }
	]

 ###### **GET ALL PAGINADO/ORDENADO**<br><br>

	  GET http://localhost:8080/enderecos?size=3&page=0&sort=logradouro
	
	  curl -i -X GET --location 'http://localhost:8080/enderecos?size=3&page=0&sort=logradouro'

		{
		    "content": [
		        {
		            "id": 4,
		            "cep": "56789111",
		            "logradouro": "AVENIDA NORTE",
		            "numero": "111SDF",
		            "bairro": "centro",
		            "cidade": "recife",
		            "estado": "PE",
		            "usuario": {
		                "id": 2
		            }
		        },
		        {
		            "id": 5,
		            "cep": "56789012",
		            "logradouro": "AVENIDA NORTE",
		            "numero": "3789SDF",
		            "bairro": "centro",
		            "cidade": "recife",
		            "estado": "PE",
		            "usuario": {
		                "id": 2
		            }
		        },
		        {
		            "id": 3,
		            "cep": "33344467",
		            "logradouro": "AVENIDA PLANA",
		            "numero": "45676CBA",
		            "bairro": "centro",
		            "cidade": "cáceres",
		            "estado": "MT",
		            "usuario": {
		                "id": 1
		            }
		        }
		    ],
		    "size": 3,
		    "totalElements": 5,
		    "totalPages": 2,
		    "number": 0
		}

  ###### **GET BY ID**<br><br>

        GET api.tech-challenge/enderecos/1
    
        curl -i -X GET --location "api.tech-challenge/enderecos/1"
        
        HTTP/1.1 200
        Content-Type: application/json
        
	{
		    "id": 1,
		    "cep": "12345678",
		    "logradouro": "ROSEIRAL DO ALTO",
		    "numero": "1223ABC",
		    "bairro": "centro",
		    "cidade": "pindamonhagaba",
		    "estado": "SP",
		    "usuario": {
		        "id": 1
		    },
		    "meusParentes": [
		        {
		            "id": 2,
		            "nome": "PARENTE-TESTE",
		            "nascimento": "1234-01-15",
		            "sexo": "MASCULINO",
		            "parentesco": "PAI"
		        }
		    ],
		    "meusEletrodomesticos": [
		        {
		            "id": 1,
		            "nome": "TESOURA ELETRICA JIN-IS-LYN",
		            "modelo": "KVW-678",
		            "potencia": 1.0
		        }
		    ]
	}

###### **GET BY QUERY PARAMETERS**<br><br>
    
        GET api.tech-challenge/enderecos/por-parametros?logradouro=RO&cep=123
	    
        curl  -i --location 'http://localhost:8080/enderecos/por-parametros?logradouro=RO&cep=123'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	        [
		    {
			        "id": 1,
			        "cep": "12345678",
			        "logradouro": "ROSEIRAL DO ALTO",
			        "numero": "1223ABC",
			        "bairro": "centro",
			        "cidade": "pindamonhagaba",
			        "estado": "SP",
			        "usuario": {
			            "id": 1
			        }
		    }
		]


     
###### **GET MORADORES NESTE ENDEREÇO**<br><br>
    
        GET api.tech-challenge/enderecos/quem-mora-aqui/1
		    
        curl --location 'http://localhost:8080/enderecos/quem-mora-aqui/1'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	[
			    {
			        "id": 3,
			        "nome": "GISELDA APERINA",
			        "nascimento": "2002-11-10",
			        "sexo": "FEMININO",
			        "parentesco": "TIA",
	   			"usuarioId": 1
			    },
			    {
			        "id": 5,
			        "nome": "LUZIA LUCIA DA LUZ",
			        "nascimento": "2000-01-27",
			        "sexo": "FEMININO",
			        "parentesco": "MAE",
	   			"usuarioId": 1
			    },
			    {
			        "id": 4,
			        "nome": "LUCIUS MAGNUS",
			        "nascimento": "1999-10-13",
			        "sexo": "MASCULINO",
			        "parentesco": "PAI",
	   			"usuarioId": 1
			    }
	]

  ###### **GET ENDEREÇOS DESTE USUÁRIO**<br><br>
    
        GET api.tech-challenge/enderecos/deste-usuario/1
		    
        curl -i --location 'http://localhost:8080/enderecos/deste-usuario/1'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	[
		    {
		        "id": 2,
		        "cep": "87654321",
		        "logradouro": "SOLARIO DO OESTE",
		        "numero": "555ABC",
		        "bairro": "luna",
		        "cidade": "aguaí",
		        "estado": "SP"
		    },
		    {
		        "id": 3,
		        "cep": "33344467",
		        "logradouro": "AVENIDA PLANA",
		        "numero": "45676CBA",
		        "bairro": "centro",
		        "cidade": "cáceres",
		        "estado": "MT"
		    },
		    {
		        "id": 1,
		        "cep": "12345678",
		        "logradouro": "ROSEIRAL DO ALTO",
		        "numero": "1223ABC",
		        "bairro": "centro",
		        "cidade": "pindamonhagaba",
		        "estado": "SP"
		    }
	]

___

##### **PUT**<br><br>

  Na url da **PUT** request deve estar o id do recurso que se deseja atualizar
  -    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*
  
  No body da **PUT** request, devem estar os pares key-value: 
  
  -    ***cep*** , *não em-branco e no máximo 8 caracteres(opcional hifen)* 
  -    ***logradouro*** , *não em-branco e no máximo 60 caracteres* 
  -    ***numero***, *não em-branco e no máximo 10 caracteres*
  -    ***bairro***, *não em-branco e no máximo 40 caracteres*
  -    ***cidade***, *não em-branco e no máximo 50 caracteres*
  -    ***estado***, *não em-branco e no máximo 30 caracteres*
  
*EXEMPLO:*    

         PUT api.tech-challenge/enderecos/1
         Content-Type: application/json
    
        {
		    "cep": "87654321",
		    "logradouro": "EDITALDO - ROSEIRAL DO ALTO",
		    "numero": "ED-1223ABC",
		    "bairro": "EDITADO-centro",
		    "cidade": "EDITADO-pindamonhagaba",
		    "estado": "EDITADO-SP"
	}
        
        curl --location --request PUT 'http://localhost:8080/enderecos/1' --header 'Content-Type: application/json' --data 
	'{
		    "cep": "87654321",
		    "logradouro": "EDITALDO - ROSEIRAL DO ALTO",
		    "numero": "ED-1223ABC",
		    "bairro": "EDITADO-centro",
		    "cidade": "EDITADO-pindamonhagaba",
		    "estado": "EDITADO-SP"
	}'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
    
        {
		    "id": 1,
		    "cep": "87654321",
		    "logradouro": "EDITALDO - ROSEIRAL DO ALTO",
		    "numero": "ED-1223ABC",
		    "bairro": "EDITADO-centro",
		    "cidade": "EDITADO-pindamonhagaba",
		    "estado": "EDITADO-SP",
		    "usuario": {
		        "id": 1
		    }
	}


___    
<br>

##### **DELETE**<br><br>

No path da **DELETE** request, deve estar o ***id*** do recurso que se deseja deletar: 
-    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*
    
          DELETE api.tech-challenge/enderecos/1
            
          curl -i -X DELETE --location "api.tech-challenge/enderecos/1"
            
          HTTP/1.1 204
         
___
#### Endpoint pessoas : REQUESTS, Curls, RESPONSES
___

##### **POST**<br><br>

No body da **POST** request, devem estar os pares key-value: 
  -    ***nome*** , *não em-branco e no máximo 60 caracteres* 
  -    ***nascimento***, *não-nulo e NÃO pode ser "hoje" ou qualquer outro dia depois de "hoje"*
  -    ***sexo***, *não-nulo e MASCULINO ou FEMININO*
  -    ***parentesco***, *não-nulo e um dos valores : ESPOSO, ESPOSA, IRMÃO, IRMÃ, PAI, MAE, FILHO , FILHA, VOVO, CUNHADA, SOGRA, CUNHADO, SOGRO, AGREGADO, NAMORADA,NOMORADO, CONJUGE, TIA, TIO, AMIGO, AMIGA, NENHUM*
  -    ***endereco***, não-nulo e deve estar no repositório
  -    ***usuario***, não-nulo e deve estar no repositório

*EXEMPLO:*   

    
       POST api.tech-challenge/pessoas
       Content-Type: application/json
        
        {
		    "nome" : "TIA-PARENTE-TESTE",
		    "nascimento":"1234-01-15",
		    "sexo":"FEMININO",
		    "parentesco":"TIA",
		    "endereco":{
		        "id":1,
		        "usuario":{
		            "id":1
		        }
		    }
	}
    
        curl -i --location 'http://localhost:8080/pessoas' --header 'Content-Type: application/json' --data 
	'{
		    "nome" : "TIA-PARENTE-TESTE",
		    "nascimento":"1234-01-15",
		    "sexo":"FEMININO",
		    "parentesco":"TIA",
		    "endereco":{
		        "id":1,
		        "usuario":{
		            "id":1
		        }
		    }
	}'
    
        HTTP/1.1 201 
        Server: nginx/1.18.0
        Content-Type: application/json

        {
		    "id": 3,
		    "nome": "TIA-PARENTE-TESTE",
		    "nascimento": "1234-01-15",
		    "sexo": "FEMININO",
		    "parentesco": "TIA",
		    "endereco": {
		        "id": 1,
		        "usuario": {
		            "id": 1
		        }
		    }
	}

___

  ##### **GET**<br><br>

   ###### **GET ALL**<br><br>
    
        GET api.tech-challenge/pessoas
        
        curl -i -X GET --location "api.tech-challenge/pessoas"
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
           
        [
		    {
		        "id": 2,
		        "nome": "PARENTE-TESTE",
		        "nascimento": "1234-01-15",
		        "sexo": "MASCULINO",
		        "parentesco": "PAI",
		        "endereco": {
		            "id": 1,
		            "usuario": {
		                "id": 1
		            }
		        }
		    },
		    {
		        "id": 3,
		        "nome": "TIA-PARENTE-TESTE",
		        "nascimento": "1234-01-15",
		        "sexo": "FEMININO",
		        "parentesco": "TIA",
		        "endereco": {
		            "id": 1,
		            "usuario": {
		                "id": 1
		            }
		        }
		    }
	]

 ###### **GET ALL PAGINADO/ORDENADO**<br><br>

	  GET http://localhost:8080/pessoas?size=3&page=0&sort=nome&sort=id
	
	  curl -i -X GET --location 'http://localhost:8080/pessoas?size=2&page=0&sort=nome&sort=id'
		{
		    "content": [
		        {
		            "id": 3,
		            "nome": "GISELDA APERINA",
		            "nascimento": "2002-11-10",
		            "sexo": "FEMININO",
		            "parentesco": "TIA",
		            "endereco": {
		                "id": 1,
		                "usuario": {
		                    "id": 1
		                }
		            }
		        },
		        {
		            "id": 8,
		            "nome": "HERÁCIO PETROV",
		            "nascimento": "2017-01-01",
		            "sexo": "MASCULINO",
		            "parentesco": "FILHO",
		            "endereco": {
		                "id": 5,
		                "usuario": {
		                    "id": 2
		                }
		            }
		        },
		        {
		            "id": 9,
		            "nome": "HORTENCIA PETROVINA",
		            "nascimento": "1990-10-20",
		            "sexo": "FEMININO",
		            "parentesco": "ESPOSA",
		            "endereco": {
		                "id": 5,
		                "usuario": {
		                    "id": 2
		                }
		            }
		        }
		    ],
		    "size": 3,
		    "totalElements": 7,
		    "totalPages": 3,
		    "number": 0
		}		

  ###### **GET BY ID**<br><br>

        GET api.tech-challenge/pessoas/2
        
        curl -i -X GET --location "api.tech-challenge/pessoas/2"
        
        HTTP/1.1 200
        Server: nginx/1.18.0
        Content-Type: application/json
        
        {
		    "id": 2,
		    "nome": "PARENTE-TESTE",
		    "nascimento": "1234-01-15",
		    "sexo": "MASCULINO",
		    "parentesco": "PAI",
		    "endereco": {
		        "id": 1,
		        "cep": "87654321",
		        "logradouro": "EDITALDO - ROSEIRAL DO ALTO",
		        "numero": "ED-1223ABC",
		        "bairro": "EDITADO-centro",
		        "cidade": "EDITADO-pindamonhagaba",
		        "estado": "EDITADO-SP",
		        "usuario": {
		            "id": 1
		        }
		    }
	}

 ###### **GET BY QUERY PARAMETERS**<br><br>
    
        GET api.tech-challenge/pessoas/por-parametros?nome=parente&parentesco=TIA
	    
        curl  -i --location 'http://localhost:8080/pessoas/por-parametros?nome=parente&parentesco=TIA'
	
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	[
		    {
		        "id": 3,
		        "nome": "TIA-PARENTE-TESTE",
		        "nascimento": "1234-01-15",
		        "sexo": "FEMININO",
		        "parentesco": "TIA",
		        "endereco": {
		            "id": 1,
		            "usuario": {
		                "id": 1
		            }
		        }
		    }
        ]

###### **GET QUEM MORA COMIGO**<br><br>
    
        GET api.tech-challenge/pessoas/que-moram-comigo/5
		    
        curl -i --location 'http://localhost:8080/pessoas/que-moram-comigo/5'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	[
		    {
		        "id": 4,
		        "nome": "LUCIUS MAGNUS",
		        "nascimento": "1999-10-13",
		        "sexo": "MASCULINO",
		        "parentesco": "PAI",
		        "endereco": {
		            "id": 1,
		            "usuario": {
		                "id": 1
		            }
		        }
		    },
		    {
		        "id": 3,
		        "nome": "GISELDA APERINA",
		        "nascimento": "2002-11-10",
		        "sexo": "FEMININO",
		        "parentesco": "TIA",
		        "endereco": {
		            "id": 1,
		            "usuario": {
		                "id": 1
		            }
		        }
		    }
	]

 ###### **GET PARENTES(PESSOAS) QUE MORAM NESTE ENDEREÇO**<br><br>
    
        GET api.tech-challenge/pessoas/que-moram-neste-endereco/1
		    
        curl -i --location 'http://localhost:8080/pessoas/que-moram-neste-endereco/1'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	[
			    {
			        "id": 5,
			        "nome": "LUZIA LUCIA DA LUZ",
			        "nascimento": "2000-01-27",
			        "sexo": "FEMININO",
			        "parentesco": "MAE",
	   			"usuarioId": 1
			    },
			    {
			        "id": 3,
			        "nome": "GISELDA APERINA",
			        "nascimento": "2002-11-10",
			        "sexo": "FEMININO",
			        "parentesco": "TIA",
	   			"usuarioId": 1
			    },
			    {
			        "id": 4,
			        "nome": "LUCIUS MAGNUS",
			        "nascimento": "1999-10-13",
			        "sexo": "MASCULINO",
			        "parentesco": "PAI".
	   			"usuarioId": 1
			    }
	]

  ###### **GET PARENTES(PESSOAS) DESTE USUÁRIO**<br><br>
    
        GET api.tech-challenge/pessoas/deste-usuario/1
		    
        curl -i --location 'http://localhost:8080/pessoas/deste-usuario/1'
        
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        
	[
			    {
			        "id": 4,
			        "nome": "LUCIUS MAGNUS",
			        "nascimento": "1999-10-13",
			        "sexo": "MASCULINO",
			        "parentesco": "PAI",
			        "endereco": {
			            "id": 1
			        }
			    },
			    {
			        "id": 6,
			        "nome": "MINERVA APERINA",
			        "nascimento": "1990-10-10",
			        "sexo": "FEMININO",
			        "parentesco": "ESPOSA",
			        "endereco": {
			            "id": 2
			        }
			    },
			    {
			        "id": 5,
			        "nome": "LUZIA LUCIA DA LUZ",
			        "nascimento": "2000-01-27",
			        "sexo": "FEMININO",
			        "parentesco": "MAE",
			        "endereco": {
			            "id": 1
			        }
			    },
			    {
			        "id": 7,
			        "nome": "MEDUSA APERINA",
			        "nascimento": "2015-01-01",
			        "sexo": "FEMININO",
			        "parentesco": "FILHA",
			        "endereco": {
			            "id": 2
			        }
			    },
			    {
			        "id": 3,
			        "nome": "GISELDA APERINA",
			        "nascimento": "2002-11-10",
			        "sexo": "FEMININO",
			        "parentesco": "TIA",
			        "endereco": {
			            "id": 1
			        }
			    }
	]

___

  ##### **PUT**<br><br>
Na url da **PUT** request deve estar o id do recurso que se deseja atualizar
-    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*

No body da **PUT** request, devem estar os pares key-value: 
  -    ***nome*** , *não em-branco e no máximo 60 caracteres* 
  -    ***nascimento***, *não-nulo e NÃO pode ser "hoje" ou qualquer outro dia depois de "hoje"*
  -    ***sexo***, *não-nulo e MASCULINO ou FEMININO*
  -    ***parentesco***, *não-nulo e um dos valores : IRMÃO, IRMÃ, PAI, MAE, FILHO , FILHA, AVÔ, AVÓ, CUNHADA, SOGRA, CUNHADO, SOGRO, AGREGADO, NAMORADA,NOMORADO, CONJUGE, TIA, TIO, AMIGO, AMIGA, NENHUM*

*EXEMPLO:*  
    
        PUT api.tech-challenge/pessoas/2
        Content-Type: application/json
        
        {
		        "nome": "PARENTE-RINITI",
		        "nascimento": "1000-01-14",
		        "sexo": "FEMININO",
		        "parentesco": "TIA"
	}
        
        curl --location --request PUT 'http://localhost:8080/pessoas/2' --header 'Content-Type: application/json' --data 
	'{
		        "nome": "PARENTE-RINITI",
		        "nascimento": "1000-01-14",
		        "sexo": "FEMININO",
		        "parentesco": "TIA"
	}'
        
        HTTP/1.1 200 
        Server: nginx/1.18.0
        Content-Type: application/json

        {
			    "id": 2,
			    "nome": "PARENTE-RINITI",
			    "nascimento": "1000-01-14",
			    "sexo": "FEMININO",
			    "parentesco": "TIA",
			    "endereco": {
			        "id": 1,
			        "usuario": {
			            "id": 1
			        }
			    }
	}

___

  ##### **DELETE**<br><br>

  No path da **DELETE** request, deve estar o ***id*** do recurso que se deseja deletar: 
  -    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*
        
    DELETE api.tech-challenge/pessoas/2
    
    curl -i -X DELETE --location "api.tech-challenge/pessoas/2"

    HTTP/1.1 204
    Server: nginx/1.18.0



___
#### Endpoint eletrodomesticos : REQUESTS, Curls, RESPONSES
___

##### **POST**<br><br>

No body da **POST** request, devem estar os pares key-value: 
  -    ***nome*** , *não em-branco e no máximo 60 caracteres* 
  -    ***modelo***, *não em-branco e no máximo 60 caracteres*
  -    ***potencia***, *não-nula e no range [ 0 , Double.MAX_VALUE ]*
  -    ***endereco***, não-nulo e deve estar no repositório
  -    ***usuario***, não-nulo e deve estar no repositório    

*EXEMPLO:*  
  
        POST api.tech-challenge/eletrodomesticos
        Content-Type: application/json
    
        {
		    "nome":"CADEIRA ELETRICA JIN-IS-LYN",
		    "modelo":"KVW-678",
		    "potencia":1000.0,
		    "endereco":{
		        "id":1,
		        "usuario":{
		            "id":1
		        }
		    }
	}
    
        curl -i -X POST --location 'http://localhost:8080/eletrodomesticos' --header 'Content-Type: application/json' --data 
	'{
		    "nome":"CADEIRA ELETRICA JIN-IS-LYN",
		    "modelo":"KVW-678",
		    "potencia":1000.0,
		    "endereco":{
		        "id":1,
		        "usuario":{
		            "id":1
		        }
		    }
	}'
        
        HTTP/1.1 201 
        Server: nginx/1.18.0
        Content-Type: application/json

        {
		    "id": 2,
		    "nome": "CADEIRA ELETRICA JIN-IS-LYN",
		    "modelo": "KVW-678",
		    "potencia": 1000.0,
		    "endereco": {
		        "id": 1,
		        "usuario": {
		            "id": 1
		        }
		    }
	}


___

##### **GET**<br><br>

  ###### **GET ALL**<br><br>

        GET api.tech-challenge/eletrodomesticos
    
        curl -i -X GET --location "api.tech-challenge/eletrodomesticos"
        
        HTTP/1.1 200
        Server: nginx/1.18.0
        Content-Type: application/json
         
        [
		    {
			"id": 2,
			"nome": "CADEIRA ELETRICA JIN-IS-LYN",
			"modelo": "KVW-678",
			"potencia": 1000.0,
			"endereco": {
			    "id": 1,
			    "usuario": {
				"id": 1
			    }
			}
		    },
		    {
			"id": 1,
			"nome": "FACA ELETRICA JIN-IS-LYN",
			"modelo": "KVW-678",
			"potencia": 1000.0,
			"endereco": {
			    "id": 1,
			    "usuario": {
				"id": 1
			    }
			}
		    }
	]

  ###### **GET ALL PAGINADO/ORDENADO**<br><br>

	  GET http://localhost:8080/eletrodomesticos?size=7&page=0&sort=nome
	
	  curl -i -X GET --location 'http://localhost:8080/eletrodomesticos?size=7&page=0&sort=nome'
   
		{
		    "content": [
		        {
		            "id": 5,
		            "nome": "CADEIRA ELÉTRICA JIN-IS-LYN",
		            "modelo": "APJ-VW1",
		            "potencia": 13.3,
		            "endereco": {
		                "id": 4,
		                "usuario": {
		                    "id": 2
		                }
		            }
		        },
		        {
		            "id": 1,
		            "nome": "FACA ELÉTRICA JIN-IS-LYN",
		            "modelo": "APJ-VW1",
		            "potencia": 1.0,
		            "endereco": {
		                "id": 1,
		                "usuario": {
		                    "id": 1
		                }
		            }
		        },
		        {
		            "id": 2,
		            "nome": "FACA ELÉTRICA JIN-IS-LYN",
		            "modelo": "mega-APJ-VW1",
		            "potencia": 5.0,
		            "endereco": {
		                "id": 1,
		                "usuario": {
		                    "id": 1
		                }
		            }
		        },
		        {
		            "id": 3,
		            "nome": "FACA ELÉTRICA JIN-IS-LYN",
		            "modelo": "APJ-VW1",
		            "potencia": 1.0,
		            "endereco": {
		                "id": 2,
		                "usuario": {
		                    "id": 1
		                }
		            }
		        },
		        {
		            "id": 4,
		            "nome": "FACA ELÉTRICA JIN-IS-LYN",
		            "modelo": "mega-APJ-VW1",
		            "potencia": 5.0,
		            "endereco": {
		                "id": 3,
		                "usuario": {
		                    "id": 1
		                }
		            }
		        },
		        {
		            "id": 6,
		            "nome": "FACA ELÉTRICA JIN-IS-LYN",
		            "modelo": "mega-APJ-VW1",
		            "potencia": 5.0,
		            "endereco": {
		                "id": 5,
		                "usuario": {
		                    "id": 2
		                }
		            }
		        }
		    ],
		    "size": 7,
		    "totalElements": 6,
		    "totalPages": 1,
		    "number": 0
		}

   ###### **GET BY ID**<br><br>

        GET api.tech-challenge/eletrodomesticos/1
        
        curl -i -X GET --location "api.tech-challenge/eletrodomesticos/1"
        
        HTTP/1.1 200 
        Server: nginx/1.18.0
        Content-Type: application/json
        
        {
		    "id": 1,
		    "nome": "FACA ELETRICA JIN-IS-LYN",
		    "modelo": "KVW-678",
		    "potencia": 1000.0,
		    "endereco": {
		        "id": 1,
		        "usuario": {
		            "id": 1
		        }
		    }
	}   

 ###### **GET BY QUERY PARAMETERS**<br><br>
    
	GET api.tech-challenge/eletrodomesticos/por-parametros?modelo=kvw&nome=faca
	    
	curl  -i -X GET --location 'http://localhost:8080/eletrodomesticos/por-parametros?modelo=kvw&nome=faca'
	
	HTTP/1.1 200
	Content-Type: application/json
	Transfer-Encoding: chunked
	
	[
		    {
		        "id": 1,
		        "nome": "FACA ELETRICA JIN-IS-LYN",
		        "modelo": "KVW-678",
		        "potencia": 1000.0,
		        "endereco": {
		            "id": 1,
		            "usuario": {
		                "id": 1
		            }
		        }
		    }
	]

###### **GET ELETRODOMÉSTICOS DESTE USUÁRIO**<br><br>
    
	GET api.tech-challenge/eletrodomesticos/deste-usuario/1
	    
	curl  -i -X GET --location 'http://localhost:8080/eletrodomesticos/deste-usuario/1'
	
	HTTP/1.1 200
	Content-Type: application/json
	Transfer-Encoding: chunked
	
	[
		    {
		        "id": 3,
		        "nome": "FACA ELÉTRICA JIN-IS-LYN",
		        "modelo": "APJ-VW1",
		        "potencia": 1.0,
		        "endereco": {
		            "id": 2
		        }
		    },
		    {
		        "id": 2,
		        "nome": "FACA ELÉTRICA JIN-IS-LYN",
		        "modelo": "mega-APJ-VW1",
		        "potencia": 5.0,
		        "endereco": {
		            "id": 1
		        }
		    },
		    {
		        "id": 4,
		        "nome": "FACA ELÉTRICA JIN-IS-LYN",
		        "modelo": "mega-APJ-VW1",
		        "potencia": 5.0,
		        "endereco": {
		            "id": 3
		        }
		    },
		    {
		        "id": 1,
		        "nome": "FACA ELÉTRICA JIN-IS-LYN",
		        "modelo": "APJ-VW1",
		        "potencia": 1.0,
		        "endereco": {
		            "id": 1
		        }
		    }
	]
 ###### **GET ELETRODOMÉSTICOS NESTE ENDEREÇO**<br><br>
    
	GET api.tech-challenge/eletrodomesticos/que-estao-neste-endereco/1
	    
	curl -i -X GET --location 'http://localhost:8080/eletrodomesticos/que-estao-neste-endereco/1'
	
	HTTP/1.1 200
	Content-Type: application/json
	Transfer-Encoding: chunked
	
	[
		    {
		        "id": 2,
		        "nome": "FACA ELÉTRICA JIN-IS-LYN",
		        "modelo": "mega-APJ-VW1",
		        "potencia": 5.0,
	  		"usuarioId": 1
		    },
		    {
		        "id": 1,
		        "nome": "FACA ELÉTRICA JIN-IS-LYN",
		        "modelo": "APJ-VW1",
		        "potencia": 1.0,
	  		"usuarioId": 1
		    }
	]


 ###### **GET CONSUMO DESTE ELETRODOMÉSTICO**<br><br>
 No path da **GET** request, devem estar o ***id*** do eletrodomestico e o **tempoDeUsoEmSegundos** : 
  -    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*
  -    ***tempoDeUsoEmSegundos*** , *não-nulo e no range [ 0, Long.MAX_VALUE ]*
    
	GET api.tech-challenge/eletrodomesticos/consumo/1/1800
	    
	curl -i -X GET --location 'http://localhost:8080/eletrodomesticos/consumo/1/1800'
	
	HTTP/1.1 200
	Content-Type: application/json
	Transfer-Encoding: chunked
	
	{
		    "id": 1,
		    "nome": "FACA ELÉTRICA JIN-IS-LYN",
		    "potencia": "1.0W ( 0.0010000000 KW )",
		    "tempoDeUso": "1800s ( 0.5000000000 h )",
		    "consumo": "0.0005000000 KWh"
	}


 
___

##### **PUT**<br><br>

Na url da **PUT** request deve estar o id do recurso que se deseja atualizar
-    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*

No body da **PUT** request, devem estar os pares key-value:
  -    ***nome*** , *não em-branco e no máximo 60 caracteres* 
  -    ***modelo***, *não em-branco e no máximo 60 caracteres*
  -    ***potencia***, *não-nula e no range [ 0 , Double.MAX_VALUE ]*
  -    **endereco**  , não-nulo e deve estar no repositório  

*EXEMPLO:* 

        PUT api.tech-challenge/eletrodomesticos/2
        Content-Type: application/json
        
        {
		    "nome":"TESOURA ELETRICA JIN-IS-LYNA",
		    "modelo":"KVW-678",
		    "potencia":333,
		    "endereco":{
		        "id":1
		    }
	}
        
        curl -i -X PUT --location --request PUT 'http://localhost:8080/eletrodomesticos/2' --header 'Content-Type: application/json' --data '{"nome":"TESOURA ELETRICA JIN-IS-LYNA","modelo":"KVW-678","potencia":333,"endereco":{"id":1}}'
        
        HTTP/1.1 200 
        Server: nginx/1.18.0
        Content-Type: application/json
        
        {
		    "id": 2,
		    "nome": "TESOURA ELETRICA JIN-IS-LYNA",
		    "modelo": "KVW-678",
		    "potencia": 333.0,
		    "endereco": {
		        "id": 1,
		        "usuario": {
		            "id": 1
		        }
		    }
	}
___

##### **DELETE**<br><br>

No path da **DELETE** request, deve estar o ***id*** do recurso que se deseja deletar: 
  -    ***id*** , *não-nulo e no range [ Long.MIN_VALUE, Long.MAX_VALUE ]*
    
            DELETE api.tech-challenge/eletrodomesticos/1
            
            curl -i -X DELETE --location "api.tech-challenge/eletrodomesticos/1"
            
            HTTP/1.1 204 
