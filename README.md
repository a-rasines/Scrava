# Scrava: Diseño y desarrollo de una aplicacion de escritorio Java similar a Scratch
Scrava es una aplicación de escritorio similar a Scratch pero con el añadido de que el código generado con bloques es exportable a código funcional en Java.

Scratch es una aplicación tanto web como de escritorio en la cual se programan videojuegos y otras aplicaciones basadas en la animación de objetos (animaciones, simulaciones...) mediante un lenguaje de programación BOP (Block Oriented Programming)

La aplicación cuenta con conexión a un servidor mediante RPC (Remote Procedure Call) donde se guardan los proyectos, se actualiza la propia aplicación y se disponen tutoriales de apoyo tanto para el manejo del sistema de bloques como para la programación en Java como tal codificados en Markdown, uno de los lenguajes de formateo de texto mas populares entre programadores (El equivalente para matematicos sería LaTeX).

La parte visual del cliente es similar a la de Scratch, contando con un panel izquierdo superior en el que correr y visualizar, guardar o exportar el código programado con los bloques, debajo de ese, otro con los sprites existentes, la franja derecha posee un panel con los bloques colocados y los existentes agrupados por tipo o el equivalente a los bloques colocados en código escrito. El usuario puede crear, eliminar y juntar bloques y definir variables (las cuales son un bloque cuya exclusiva función es almacenar un valor) para generar código único con el que crear una experiencia 2D la cual luego se puede ejecutar para comprobar el comportamiento generado por los bloques, revisar en modo escrito con una de las opciones del panel derecho, o exportar el proyecto para pasar el código en bloques a código escrito.

Al exportar el proyecto se genera una carpera en la cual se encuentra un proyecto de Java completamente configurado con dos paquetes dentro del código, uno de ellos con código base inmutable que viene con la función de exportar con la finalidad de que el código generado se asimile al de los bloques, y, en el otro paquete, una clase por cada Sprite con su propio código y una clase que albergue todas las variables globales (Variables que no estan asociadas a ningun Sprite).

El arbol generado al exportar:
```
{appname}
|
+ - src
|   |
|   + - main
|       |
|       + - java
|       |   |
|       |   + - generated
|       |   |   |
|       |   |   + - GlobalVariables.java
|       |   |   |
|       |   |   + - Sprite1.java
|       |   |
|       |   + - base
|       |       |
|       |       + - VisualWindow.java
|       |       |
|       |       + - SpriteBase.java
|       |
|       + - resources
|           |
|           + - Sprite1.png
+ - bin
|   |
|   ...
|
+ - dist
|   |
|   + - {appname}.jar
|
+ - compile.bat
|
+ - README.md (explicación de carpetas y archivos)
|
+ - .project
|
+ - .classpath
|
+ - .gitignore

```



## Minimos
### Backend
- Bloques de movimiento
- Bloques de apariencia del sprite
- Bloques para definir el fondo
- Generar el código de dichos bloques
- Eventos sencillos (Bandera, click, tecla, mensajes)
- Variables globales y de sprite (String, int y float)
- Condicionales y loops sin variable (for x veces, no x en lista)
- Operadores aritmeticos
- Sistema de sprites

### Frontend
- Panel de previsualizacion
- Lista de bloques
- Generar bloques segun texto, sub-bloques y nombre de variables
- Visualización de bloques puestos (con o sin animaciones)
- Apartado con lista de frames del sprite
- Apartado con el código generado en Java

### Networking
- Sistema de sesiones
- Encriptado de mensajes mediante java Cipher
- Guardado de proyectos

## Añadidos
### Backend
- Bloques de sonido
- Renderizado de texto en nubes (como en Scratch)
- Interaccion con el fondo
- Editor de sprites (visual)
- Bloques de funciones personalizadas

### Frontend
- Editor de sprites
- Sistema de tutoriales

### Networking
- Sistema de tutoriales
- Actualizaciones automaticas

## Poco probable
### Backend
- Paint para sprites y fondos
- Sistema de plugins
- Interprete de código no representado por bloques (edición a tiempo real del código escrito)

### Frontend
- Paint para sprites y fondos
- Acceso a proyectos ajenos

### Networking
- Acceso a proyectos ajenos

## Librerias
- [TxtMark](https://github.com/rjeschke/txtmark) para proyectar los tutoriales en markdown
- [gRPC](https://github.com/grpc/grpc-java) como middleware
- [TwelveMonkeys + Batik plugin](https://github.com/haraldk/TwelveMonkeys) para cargar mas formatos de imagen
- [Apache Batik](https://xmlgraphics.apache.org/batik/) como libreria base para cargar SVG