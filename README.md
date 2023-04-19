# framework-api-web-cucumber
Base master Framework para pruebas automatizadas de API y Web con cucumber y TestNG

## AUTOMATION - API - WEB
Las siguientes consideraciones son para la ejecución de los scripts automatizados para el proyecto.

## TOOLS
Este proyecto usa las siguientes herramientas:
* Maven
* Cucumber-JVM
* Jackson
* Testng
* ExtentReports
* RestAssured
* Apache.POI

## REQUISITOS
----------
De modo de poder correr este proyecto debe tener instalado localmente:
* Un IDE para la ejecución de pruebas, se sugiere Intellij.
* JDK 17 o superior
* Maven 3
* Establecer correctamente las variables ambientales (JAVA_HOME, MAVEN_HOME)
* Clonar el proyecto:
  * Repositorio: 
  * Branch: 
* Una vez abierto el proyecto asegurarse que estén instaladas localmente todas las dependencias descritas en el archivo POM.xml

## PROYECTO
----------
* Properties: src -> main -> java -> resources -> global.properties
Aquí se encuentran los datos globales para realizar los test, como las url de los apis necesarios para el proyecto. 

* Data Excel: src -> main -> java -> resources -> Testdata
El archivo excel contiene ya un formato establecido, en este se encuentra toda la data necesaria para realizar las pruebas. 
Si se desea editar datos del talento, jobs, etc., realizarlo en la pestaña correspondiente. (No dejar celdas o espacios vacíos; no cambiar la convención de nombres utilizados)

* Reportes: test-output -> Reports
En cada ejecución se generará un reporte nuevo con su carpeta con la fecha de ejecución.

* Logs: src -> test -> java -> logs
Estas carpetas se crearán localmente al correr los proyectos, contiene los logs de la data enviada y recibida a los API de la última prueba realizada. (Estos archivos son solo de referencia para revisar data puntual, se re-escribirán una y otra vez con cada prueba)


## EJECUCIÓN DE TESTS
----------
Los archivos que describen y configuran los test se encuentran en: src -> test -> java -> features

* Los test están escritos en Cucumber BDD(Behaviour-Driven Development) usando leguaje Gherkin's
* Los archivos en cucumber deben tener un título y un tag correspondiente para poder llevar a cabo los test, sin ellos no se llevarán a cabo o no generara reporte de prueba
* Se pueden comentar los pasos, más no cambiar el texto como se encuentra, este está asociado a el código de los tests

## Para ejecutar:

* Ubicar el archivo "TestRunner" src -> test -> java -> cucumber.Options -> TestRunner

* En la sección de "Tags" colocar la prueba que se quiere llevar a cabo. Ejemplo: tags ="@Login"
  Para llevar a cabo más de una prueba se puede colocar "and" o "or"; para llevar a cabo pruebas que tengan más de un tag colocar "And" y solo se llevara a cabo las pruebas que tengan esos tags. 

  Ejemplo: tags ="@Smoke and @blackbox" y solo se llevarán a cabo las pruebas que tengan esas dos tags

  Para llevas a cabo varias pruebas consecutivas colocar "or". 

  Ejemplo: tags = "@Login or @Smoke" se llevan a cabo primero la prueba de Login y luego la prieba de Smoke

* Si se desea correr todas las pruebas sin especificar alguna dejar la seccion de TAGS vacia. Ejemplo: tags = ""

## Existen 3 maneras basicas de llevar a cabo los test automatizados:

* A través del archivo XML testng:
  - Ubicar el archivo "testng.xml" y click derecho RUN testng.xml
  - En la consola, se mostrará cada ejecución y su estado al finalizar.
  - Visualizar el reporte para mayor detalle de que data se utilizó y en el caso de que haya fallado la prueba, los motivos de ello.

* A través de Maven:
  - Ubicar el archivo "testng.xml" o "TestRunner.java" click derecho RunMaven -> test
  - o en la Terminal con el comando "mvn test" (asegurarse que la Terminal este en la carpeta donde este guardado el proyecto)
  - En la consola, se mostrará cada ejecución y su estado al finalizar.
  - Visualizar el reporte para mayor detalle de que data se utilizó y en el caso de que haya fallado la prueba, los motivos de ello.

* A través del archivo TestRunner:
  - Ubicar el archivo TestRunner.java y click derecho RUN
  - En la consola, se mostrará cada ejecución y su estado al finalizar.
  - Visualizar el reporte para mayor detalle de que data se utilizó y en el caso de que haya fallado la prueba, los motivos de ello.
