Este projeto implementa um sistema simples de gerenciamento de biblioteca com uma interface gráfica JavaFX, permitindo operações CRUD (Criar, Ler, Atualizar, Deletar) para entidades Livro e Autor. A persistência de dados é feita utilizando o ORMLite com um banco de dados SQLite.
O projeto está organizado nas seguintes camadas:
controller/: Contém a lógica de controle da interface gráfica, gerenciando as interações do usuário e comunicando-se com a camada de modelo. Inclui a classe abstrata AbstractCrudController para reuso de código entre os controladores de Livro e Autor.
model/: Responsável pela lógica de negócio e persistência de dados. Contém as classes de entidade (Autor.java, Livro.java), os repositórios (Repositorio.java, AutorRepositorio.java, LivroRepositorio.java) para operações CRUD, e a classe Database.java para gerenciamento da conexão com o banco de dados.
view/: Contém os arquivos FXML que definem a estrutura da interface gráfica e as classes JavaFX View Model (view.Autor.java, view.Livro.java) que adaptam os dados do modelo para exibição na UI.

Pré-requisitos
Para executar este projeto, você precisará ter instalado:
Java Development Kit (JDK) 17 ou superior: Certifique-se de que o java e javac estão configurados corretamente em seu PATH.
JavaFX SDK 17 ou superior: Faça o download do SDK no site oficial do OpenJFX.
Bibliotecas ORMLite:
ormlite-core-<versao>.jar
ormlite-jdbc-<versao>.jar
Você pode baixá-las do repositório Maven Central.

Como Executar
Siga os passos abaixo para configurar e executar o projeto:
1. Organização dos Arquivos
Certifique-se de que a estrutura de pastas do seu projeto seja a seguinte:
seu-projeto/
├── src/
│   ├── controller/
│   │   ├── AbstractCrudController.java
│   │   ├── AppController.java
│   │   ├── AutorController.java
│   │   └── LivroController.java
│   ├── model/
│   │   ├── Autor.java
│   │   ├── AutorRepositorio.java
│   │   ├── Database.java
│   │   ├── Livro.java
│   │   ├── LivroRepositorio.java
│   │   ├── Repositorio.java
│   │   └── Repositorios.java
│   └── view/
│       ├── app.fxml
│       ├── autor_tab.fxml
│       ├── Autor.java
│       ├── livro_tab.fxml
│       ├── Livro.java
│       └── AppView.java
├── lib/  <-- Crie esta pasta
│   ├── ormlite-core-<versao>.jar
│   ├── ormlite-jdbc-<versao>.jar
│   └── (coloque os JARs do JavaFX aqui também, se preferir, ou use o caminho do SDK)
└── MainApp.java  <-- Arquivo de launcher na raiz do projeto ou em 'controller/'
Observação: Se o seu MainApp.java estiver dentro do pacote controller, ajuste o caminho de execução conforme necessário. A estrutura acima assume MainApp.java na raiz do projeto, mas o código fornecido indica que ele está em controller. Neste caso, certifique-se de que MainApp.java esteja em src/controller/.

2. Configurando as Variáveis de Ambiente
Substitua os placeholders a seguir pelos caminhos reais em seu sistema:
%PATH_TO_FX%: O caminho completo para a pasta lib dentro do seu JavaFX SDK. Por exemplo: C:\path\to\javafx-sdk-17\lib (Windows) ou /path/to/javafx-sdk-17/lib (Linux/macOS).
%PATH_TO_ORMLITE_CORE%: O caminho completo para o arquivo ormlite-core-<versao>.jar.
%PATH_TO_ORMLITE_JDBC%: O caminho completo para o arquivo ormlite-jdbc-<versao>.jar.

3.Compilação
Abra o terminal ou prompt de comando na raiz do seu projeto (seu-projeto/) e execute o seguinte comando:
No Windows:
javac -d out --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -cp ".;%PATH_TO_ORMLITE_CORE%;%PATH_TO_ORMLITE_JDBC%" src/controller/*.java src/model/*.java src/view/*.java

No Linux/macOS:
javac -d out --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -cp ".:%PATH_TO_ORMLITE_CORE%:%PATH_TO_ORMLITE_JDBC%" src/controller/*.java src/model/*.java src/view/*.java

A aplicação JavaFX deverá ser iniciada, e você poderá interagir com as abas de Livros e Autores para realizar as operações CRUD. Um arquivo de banco de dados SQLite chamado app.sqlite será criado na raiz do diretório onde você executa o comando java, se ele ainda não existir.
