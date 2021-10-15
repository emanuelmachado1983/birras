# birras

El programa consta de backend y de front end. 
El backend está hecho en Java 11 y el front end está hecho en Angular.

Les hice un deploy en heroku para que no tenga que andar realizando un ustedes: https://birras-ema.herokuapp.com/

El usuario administrador es "admin" y su password "1234"

Después tienen varios usuarios con los que pueden entrar: "emanuel", "mariana", "eliana". Igual pueden registrarse si quieren. 
De todas formas no hice la lógica para que la registración te mande un mail con un token. Te registrar, te logueas y lo podes empezar a usar.

En heroku se puede hacer deploy automático. Se podría hacer como hacemos en el Hipotecario. O sea tenés una rama de develop, de la que sacas ramas de desarrollo, y cuando alguien con permisos mergea la rama de develop con la de master se deploya automáticamente.

**Las historias de usuario que pude hacer son**:

-Como admin quiero saber cuántas cajas de birras tengo que comprar para poder aprovisionar la meetup: 
Esta se puede ver cuando entras con el admin y haces clic en el botón "administrar" en alguna de las meetup generadas. Para no pegarle tanto a la api de temperaturas lo que hago es guardarla en una tabla. 
Me guardo el momento en que la grabé en la base, y si al otro día el usuario vuelve a fijarse cuántas cervezas necesita, vuelve a llamar a la api. O sea te guardo el valor en la base por un día.
Tomé la temperatura máxima para hacer el cálculo de cajas ya que la idea es redondear para arriba.

-Como admin y usuario quiero conocer la temperatura del día de la meetup para saber si va a hacer calor o no.

-Como admin quiero armar una meetup para poder invitar otras personas.

-Como usuario quiero inscribirme en una meetup para poder asistir.

-Como usuario quiero hacer check-in en una meetup para poder avisar que estuve ahí.


**Deploy**

Como dije más arriba, les hice un deploy en heroku. Igual si quieren ejecutar el programa ustedes:
1) Bajense el SpringToolSuite4 que es el eclipse que ya está "tuneado" para usar Spring Boot
2) Importan el proyecto de git.
3) Modifican el archivo application.properties, y lo configuran con la base de datos que ustedes quieran. Son estos tres archivos que tienen que modificar: spring.datasource.url, spring.datasource.url, spring.datasource.password.
4) Le dan a ejecutar. Les abre todo en el puerto 8080.

**Aspectos técnicos**:

Como seguridad usé JWT.
Lo ideal hubiera sido cifrar la password de usuario.

Lo de I18N lo hice del lado del front, ya que es el lugar donde el usuario va a ver la info. Los mensajes que vienen del backend se mapean.

Cacheo la temperatura en base de datos. En mis otros proyectos suelo cachear información como para que no le pegue tanto a la base de datos. No fue este el proyecto por temas de tiempo.

El front es responsivo. Usé bootstrap que te ayuda con esto. También usé material para algunos controles.

No llegué a hacer testing automático.















