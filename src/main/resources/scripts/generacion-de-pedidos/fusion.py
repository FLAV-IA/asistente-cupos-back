import csv
import json
# script que apartir de dos archivos, uno de pedidos de materias con sus respectivas comisiones y otro de la historia academica de los alumnos que solicitaron
#las solicitaron. Se fusionan los datos y se genera un archivo json con la siguiente estructura:
 #{
 #     "dni": "23232189",
 #      "historiaAcademica": {
 #      "totalInscripcionesHistoricas": "20",
 #        "totalHistoricasAprobadas": "10",
 #        "coeficiente": "25.41",
 #        "cursadasAnteriores": [
 #          {"codigoMateria": "1038", "fueAprobada": false},
 #          {"codigoMateria": "1035", "fueAprobada": false},
 #          {"codigoMateria": "487", "fueAprobada": false}
 #        ],
 #        "codigosMateriasInscriptasActuales": ["1041"]
 #      },
 #      "peticiones": [
 #        {
 #          "materiasPosibles": ["646-1-CP"],
 #          "cumpleCorrelativa": true
 #        }
 #      ]
 #    }
 # Este script se ejecuta desde la terminal con el siguiente comando py fusion.py
def leer_pedidos_cupo(ruta_pedidos):
    pedidos = {}
    with open(ruta_pedidos, newline='', encoding='utf-8') as f:
        lector = csv.DictReader(f)
        for fila in lector:
            dni = fila['DNI'].strip()
            codigo = fila['Código'].strip()
            comisiones = fila['Comisión'].replace('"', '').split(',')
            comisiones = [c.strip() for c in comisiones if c.strip()]
            if dni not in pedidos:
                pedidos[dni] = []
            pedidos[dni].append({
                "codigo": codigo,
                "comisiones_preferidas": comisiones
            })
    return pedidos

def leer_historia_academica(ruta_historia):
    alumnos = []
    with open(ruta_historia, newline='', encoding='utf-8') as f:
        lector = csv.DictReader(f, delimiter='|')
        for fila in lector:
            dni = fila['legajo'].strip()
            alumno = {
                "dni": dni,
                "nombre": fila['nombre'].strip(),
                "apellido": fila['apellido'].strip(),
                "mail": fila['mail'].strip(),
                "coeficiente": float(fila['coeficiente']),
                "materias_cursadas": [],
                "materias_aprobadas": [],
            }

            # Extraer materias cursadas
            for campo in ['inscAct', 'insc1C2024', 'insc2C2024']:
                materias = fila.get(campo, "").replace('"', '').split(',')
                alumno["materias_cursadas"].extend([m.strip() for m in materias if m.strip() and m.strip() != '-'])

            # Extraer materias aprobadas
            for campo in ['aprob1C2024', 'aprob2C2024']:
                materias = fila.get(campo, "").replace('"', '').split(',')
                alumno["materias_aprobadas"].extend([m.strip() for m in materias if m.strip() and m.strip() != '-'])

            alumnos.append(alumno)
    return alumnos

def fusionar_datos(alumnos, pedidos):
    resultado = []
    for alumno in alumnos:
        dni = alumno["dni"]
        alumno["peticiones"] = pedidos.get(dni, [])
        resultado.append(alumno)
    return resultado

def main():
    pedidos_path = 'pedidos-comisiones.csv'
    historia_path = 'historia.csv'
    salida_path = 'peticiones-finales.json'

    pedidos = leer_pedidos_cupo(pedidos_path)
    alumnos = leer_historia_academica(historia_path)
    datos_finales = fusionar_datos(alumnos, pedidos)

    with open(salida_path, 'w', encoding='utf-8') as f:
        json.dump(datos_finales, f, ensure_ascii=False, indent=2)

    print(f"Archivo generado: {salida_path}")

if __name__ == "__main__":
    main()
