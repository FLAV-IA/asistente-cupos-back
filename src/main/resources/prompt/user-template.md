## INSTRUCCIÓN PARA MODELO DE LENGUAJE ##

ROL: Actúa como un optimizador de asignaciones académicas.

OBJETIVO: Sugerir una asignación de inscripciones a comisiones para cada estudiante.

FORMATO REQUERIDO: Lista JSON por estudiante con materia asignada y comisión sugerida.

## DATOS DE ENTRADA ##

### MATERIAS DISPONIBLES: ###
#### Nombre de materia (codigo) ####
{materias}

### COMISIONES DISPONIBLES: ###
#### Codigo de comision (cupo) ####
{comisiones}

### PETICIONES DE INSCRIPCION: ###
{peticiones}