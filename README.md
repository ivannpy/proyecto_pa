# 📊 Análisis de Reseñas de Steam  
### Procesamiento secuencial y concurrente usando hilos virtuales (Java 21)

Este proyecto implementa un sistema orientado a objetos para procesar **113 millones de reseñas de videojuegos** tomadas de la plataforma Steam.  
El objetivo es comparar el rendimiento de un procesamiento **secuencial** frente a uno **concurrente** utilizando **hilos virtuales** introducidos en Java 21, así como facilitar un análisis estadístico posterior del dataset.

La herramienta permite:
- Filtrar reseñas por idioma y otros criterios.
- Seleccionar columnas específicas del archivo CSV masivo (39.5 GB).
- Dividir el archivo en múltiples subarchivos para facilitar el procesamiento.
- Procesar datos usando dos estrategias distintas (secuencial / concurrente).
- Obtener n-gramas frecuentes para análisis de sentimientos simple (word clouds).

---

## 🏗️ Tecnologías utilizadas

- **Java 21**
- **Hilos virtuales**
- **Maven**
- Programación **orientada a objetos** (principios SOLID) y patrones de diseño
- **I/O eficiente** y manejo de archivos grandes
- N-gramas para analizar sentimientos y opiniones generales

---

## 📚 Dataset Utilizado

Steam Reviews – 113M reviews

Fuente: Kaggle
[https://www.kaggle.com/datasets](https://www.kaggle.com/datasets/kieranpoc/steam-reviews)

---

## 📁 Estructura del Proyecto

```text
unam.pcic/
├── analisis/
│   ├── AnalizadorRendimiento.java
│   └── Estadisticas.java
├── dominio/
│   ├── Almacen.java
│   ├── AlmacenRenglones.java
│   ├── CondicionFiltro.java
│   ├── CondicionIgualdad.java
│   ├── CondicionMayor.java
│   ├── CondicionMayorIgual.java
│   ├── CondicionMenor.java
│   ├── CondicionMenorIgual.java
│   ├── CriterioFiltro.java
│   ├── RegistroCSV.java
│   └── SeleccionRenglon.java
├── io/
│   ├── AdminArchivosTmp.java
│   ├── DivisorArchivo.java
│   ├── EscritorCSV.java
│   ├── EscritorCSVMultiple.java
│   ├── LectorCSV.java
│   └── Logger.java
├── procesamiento/
│   ├── AdministradorTrabajo.java
│   ├── HiloDeTrabajo.java
│   ├── ProcesadorConcurrente.java
│   ├── ProcesadorCSV.java
│   └── ProcesadorSecuencial.java
├── utilidades/
│   ├── Configuracion.java
│   ├── Opciones.java
│   ├── Validaciones.java
│   ├── ControladorAplicacion.java
│   ├── FabricaProcesador.java
│   └── Main.java
└── resources/
    └── ...
```

---

## 🚀 Ejecución

1. Clonar el repositorio

```text
git clone https://github.com/ivannpy/proyecto_pa.git
cd proyecto_pa
```

2. Compilar y ejecutar pruebas con Maven
```text
mvn clean
mvn compile
mvn test
```

3. Generar jar y ejecutar
```text
mvn install
java -jar target/proyecto.jar
```

Al iniciar, el sistema solicitará:
- Ruta del archivo CSV de 39.5 GB.
- Columnas a procesar.
- Filtros deseados.
- Modo de procesamiento: Secuencial o Concurrente.

---

## 👥 Autores

- Iván Reyes-Hernández, PCIC, UNAM
- 
- 
