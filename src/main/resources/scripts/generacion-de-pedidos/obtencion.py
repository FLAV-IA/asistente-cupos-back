import pandas as pd
from unidecode import unidecode

# Leer el archivo CSV
df = pd.read_csv("inscripciones.csv")

# Diccionario: nombre -> código
materias_codigo = {
   "Elementos de Programacion y Logica": "80005",
    "Lectura y Escritura Academica": "80000",
    "Matematica": "8003N",
    "Introduccion a la Programacion": "487",
    "Organizacion de Computadoras": "1032",
    "Matematica I": "1033",
    "Programacion con Objetos 1": "1034",
    "Bases de Datos": "1035",
    "Estructuras de Datos": "1036",
    "Programacion con Objetos 2": "1037",
    "Redes de Computadoras": "1038",
    "Sistemas Operativos": "1039",
    "Programacion Concurrente": "1040",
    "Matematica II": "1041",
    "Elementos de Ingenieria de Software": "1042",
    "Construccion de Interfaces de Usuario": "1043",
    "Estrategias de Persistencia": "1044",
    "Programacion Funcional": "1045",
    "Desarrollo de Aplicaciones": "1046",
    "Laboratorio de Sistemas Operativos y Redes": "1047",
    "Ingles 1": "90000",
    "Ingles 2": "90028",
    "Seguridad Informatica": "646",
    "Introduccion a las Arquitecturas de Software": "1314",
    "Practica de Desarrollo de Software": "1305",
    "Gestion de Proyectos de Desarrollo de Software": "1304",
    "Programacion con Objetos 3": "110371",
    "Introduccion a la Bioinformatica": "1052",
    "Politicas Publicas en la Sociedad de la Informacion y la Era Digital": "1316",
    "Seminarios": "1805",
    "Sistemas de Informacion Geografica": "1306",
    "Bases de Datos II": "1053",
    "Introduccion al Blockchain": "1055",
    "Introduccion al Desarrollo de Videojuegos": "1056",
    "Logica y programacion": "1302",
    "Taller de Trabajo Intelectual": "751",
    "Taller de Trabajo Universitario": "752",
    "Trabajo de Insercion Profesional": "1060",
    "Sistemas Distribuidos": "1311",
    "Algoritmos": "1307",
    "Analisis Matematico I": "54",
    "Seguridad de la informacion": "1303",
    "Probabilidad Y Estadística": "604",
    "Matematica III": "842",
    "Arquitectura de Software II": "1313",
    "Arquitectura de Software I": "1310"
}

# Normaliza nombres: sin acentos, todo minúsculas, números romanos convertidos
def normalizar(texto):
    texto = unidecode(str(texto)).lower()
    texto = texto.replace(" i", " 1").replace(" ii", " 2").replace(" iii", " 3")
    texto = texto.replace("1", " 1").replace("2", " 2").replace("3", " 3")  # por si viene separado
    return texto.strip()

# Preprocesamos el diccionario de materias
materias_norm = {normalizar(k): v for k, v in materias_codigo.items()}

# Lista de materias únicas
materias = list(set([
    "Introducción a la Programación", "Organización de Computadoras", "Matemática 1",
    "Programación con Objetos 1", "Bases de Datos", "Estructuras de Datos",
    "Programación Funcional", "Programación Concurrente", "Programación con Objetos 2",
    "Sistemas Operativos", "Redes de Computadoras", "Laboratorio de Sistemas Operativos y Redes",
    "Introducción  a la Bioinformática", "Construcción de Interfaces de Usuario", "Estrategias de Persistencia",
    "Desarrollo de Aplicaciones (SOLO estudiantes de TPI)",
    "Programación con Objetos 3 (Complementaria para TPI y Obligatoria para LI)",
    "Elementos de Ingeniería de Software",
    "Seguridad Informática (Complementaria en TPI) / Seguridad de la Información (Obligatoria en LI)",
    "Introducción al Desarrollo de Videojuegos (Complementaria en TPI y en LI)",
    "Seminario: Introducción al Blockchain (Núcleo complementario en TPI, este cuatrimestre cuenta con créditos cómo Seminario del Núcleo de orientación en LI. La verán cómo: Seminarios.",
    "Seminarios: Introducción a la Electrónica y Programación de Controladores con Arduino (Complementaria en TPI. Este cuatrimestre no cuenta créditos cómo seminario para LI)",
    "Matemática 2", "Trabajo de Inserción Profesional (TIP) (SOLO estudiantes de TPI)", "Inglés 2",
    "Lenguajes Formales y Autómatas  (Complementaria para TPI y Obligatoria para LI)",
    "Lógica y Programación  (Complementaria para TPI y Obligatoria para LI)",
    "Gestión de Proyectos de Desarrollo de Software (SOLO estudiantes de Licenciatura en Informática)",
    "Arquitectura de Software 2 (SOLO estudiantes de Licenciatura en Informática)",
    "Teoría de la Computación (SOLO estudiantes de Licenciatura en Informática)",
    "Práctica del Desarrollo de Software (SOLO estudiantes de Licenciatura en Informática)",
    "Arquitectura de Computadoras (SOLO estudiantes de Licenciatura en Informática)",
    "Aspectos Legales y Sociales (SOLO estudiantes de Licenciatura en Informática)",
    "Redes Neuronales (SOLO estudiantes de Licenciatura en Informática)",
    "Matemática 3 (SOLO estudiantes de Licenciatura en Informática)",
    "Sistemas Distribuidos (SOLO estudiantes de Licenciatura en Informática)",
    "Algoritmos (SOLO estudiantes de Licenciatura en Informática)",
    "Inglés 1", "Análisis Matemático (SOLO para Licenciatura en Informática)",
    "Probabilidad y Estadística (SOLO para Licenciatura en Informática)",
    "Taller de Trabajo Intelectual (SOLO para Licenciatura en Informática)",
    "Taller de Trabajo Universitario (SOLO para Licenciatura en Informática)",
    "Taller de Trabajo Universitario (SOLO para estudiantes de Licenciatura en Informática)",
    "Taller de Trabajo Intelectual (SOLO para estudiantes de la Licenciatura en Informática)"
]))

# Lista para las nuevas filas
nuevas_filas = []

for _, row in df.iterrows():
    dni = row.get("DNI", "")
    for materia in materias:
        if materia in row and pd.notna(row[materia]):
            valor = str(row[materia]).strip()
            comisiones = [c.strip() for c in valor.split(',') if c.strip()]
            comisiones_str = f'"{",".join(comisiones)}"'

            materia_normalizada = normalizar(materia)

            # Buscamos una coincidencia aproximada tipo "LIKE"
            codigo = ""
            for clave, cod in materias_norm.items():
                if clave in materia_normalizada:
                    codigo = cod
                    break

            nuevas_filas.append({
                "DNI": dni,
                "Código": codigo,
                "Comisión": comisiones_str               
            })

# Crear DataFrame y exportar
df_salida = pd.DataFrame(nuevas_filas)
df_salida.to_csv("salida_cupos.csv", index=False)

