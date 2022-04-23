# lanchinho_compose
## Motivação:
Hoje a maioria dos restaurantes tem um sistema manual de organização em seus atendimentos presenciais. Precisamos de garçons para realizar os pedidos, 
ter informação do andamento, realizar o pagamento, solicitar mudanças nos pratos e etc.
Esse projeto é um esboço de uma idéia que buscar tornar esse processo transparente, automático e de baixo custo.

O principal objetivo é criar uma aplicação que dê a praticidade de um delivery com a experiência dos ambientes presenciais.


## Momento Atual do Projeto <br/>
Esse projeto acabou de ser iniciado, no momento ele faz o consumo de uma API gratuita simples, apenas para apresentar variedades de pratos e uma lista de pedido dinâmica, sem persistência local <br/>
Sua estrutura está em uma módulo único separado em pacotes.  <br/>
<b> Informações sobre os pacotes: </b> <br/>
Data: Local destinado a todos as entidades de acesso ao serviço remoto, assim como as apis, interfaces e implementações de serviço. O acesso a essa camada é feito exclusivamente através do repositório.<br/>

Domain - Local destinado à camada lógica da aplicação. 
Temos entidades intermediárias que traduzem as entidades da camada de dados em modelos para consumo da camada de UI, as extensions  relacionadas as regras da aplicação e os stateholders (view models) <br/>

UI - Este pacote é destinado à fragments, activities, e composable functions.<br/>


## Próximos passos? <br/>
- Criação de testes instrumentais com a utilização do espresso;<br/>
- Criação de API própria para o consumo de informações;<br/>
- Criação de testes unitários para a camada de domain, realizando o mock das chamadas de API;<br/>
- Implementação da lógica de pedidos;<br/>
- Jetpack Navigation;<br/>
- Criação de uma tela de detalhes;<br/>
- Vinculo com o Firebase para notificações sobre o avanço dos pedidos;<br/>
- Crashlytics;<br/>


<b> Pontos de atenção: <b/>
A lib do jetpack compose ainda é extremamente recente, o que causa diversas dificuldades tecnológicas, principalmente ao suporte de libs, animação, navegação e força o uso de algumas bibliotecas experimentais e annotations adicionais.


## Informações Tecnológicas
Language: Kotlin <br/>
Architeture: MVVM <br/>
DI: Koin <br/>
Libs: Retrofit, Jetpack Compose, Coil, Gson <br/>
