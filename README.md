# projeto-stoom
Teste de Qualificação Backend STOOM

Como não era necessário adicionar qualquer configuração do banco de dados ao Dockerfile, eu realizei a conexão da API a uma instancia do MySQL no Docker da seguinte forma:
1- Realizei o pull de uma imagem do MySQL com o comando: docker pull mysql:5.7.23
2- Executei a instancia do MySQL com o comando: docker run --name mysql-standalone -e MY_SQL_DATABASE=projetostoom -e MYSQL_ALLOW_EMPTY_PASSWORD=1 -e MY_SQL_USER=root -d mysql:5.7.23
3- Loguei no banco utilizando o Docker Desktop e criei os banco de dados "projetostoom" e "projetostoom_testes"
4- Inicializei a API com o comando: docker run -d -p 8080:8080 --name projeto-stoom --link mysql-standalone:mysql projeto-stoom
