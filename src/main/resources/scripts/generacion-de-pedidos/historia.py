import pandas as pd
import re

# Cargar el archivo CSV original
df = pd.read_csv("inscripciones.csv", encoding="utf-8")

# Renombrar columnas largas
df = df.rename(columns={
    "DNI": "legajo",
    "Nombre/s": "nombre",
    "Apellido/s": "apellido",
    "Mail de contacto": "mail",
    "Coeficiente según la información publicada en http://campus.uvq.edu.ar --> Autogestion Guarani": "coeficiente",
    "Cantidad de materias en las que ya se inscribió en TPI o LI incluyendo el Ciclo introductorio en Guaraní (vía web)": "inscTot",
    "Materias en las que se inscribió ahora (1er C 2025)": "inscAct",
    "Que materias Curso 2024 1er C": "insc1C2024",
    "Que materias Aprobó 2024 1er C": "aprob1C2024",
    "Que materias Curso 2024 2do C": "insc2C2024",
    "Que materias Aprobó 2024 2do C": "aprob2C2024"
})

# Función que devuelve un string tipo "1033,90000,80000" encerrado entre comillas dobles
def extraer_codigos_str(texto):
    if pd.isna(texto):
        return "-"
    # Busca secuencias de 3 a 5 dígitos (códigos de materia), ya sea entre paréntesis o sueltos
    codigos = re.findall(r'\b\d{3,5}\b', texto)
    codigos_sin_ceros = [str(int(c)) for c in codigos]  # Elimina ceros a la izquierda
    return f'"{",".join(codigos_sin_ceros)}"' if codigos_sin_ceros else "-"

# Aplicar la función
columnas_codigos = ["inscAct", "insc1C2024", "aprob1C2024", "insc2C2024", "aprob2C2024"]
for col in columnas_codigos:
    df[col] = df[col].apply(extraer_codigos_str)

# Selección final de columnas
columnas_finales = ["legajo", "nombre", "apellido", "mail", "coeficiente", "inscTot"] + columnas_codigos
df_salida = df[columnas_finales]

# Exportar
df_salida.to_csv("resultado.csv", index=False, sep='|')
