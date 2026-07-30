# Reporte de cobertura de pruebas - SNAAR TekMess

**Fecha de generación:** 2026-07-30 09:45:29

## Resumen ejecutivo

Este reporte resume la cobertura obtenida por las pruebas automatizadas JUnit del prototipo Java. La información fue tomada de los reportes generados por JaCoCo y Surefire.

| Métrica | Cubierto | Total | Cobertura |
|---|---:|---:|---:|
| Instrucciones | 2361 | 2791 | 84.59% |
| Ramas / decisiones | 153 | 265 | 57.74% |
| Líneas | 611 | 712 | 85.81% |
| Complejidad | 234 | 346 | 67.63% |
| Métodos | 195 | 212 | 91.98% |
| Clases | 24 | 24 | 100% |

## Resultado de ejecución de pruebas

- **Total de pruebas ejecutadas:** 17
- **Fallos:** 0
- **Errores:** 0
- **Omitidas:** 0
- **Tiempo total:** 6.479 s

| Suite de pruebas | Pruebas | Fallos | Errores | Omitidas | Tiempo (s) |
|---|---:|---:|---:|---:|---:|
| `com.tekmess.snaar.CoberturaComplementariaTest` | 7 | 0 | 0 | 0 | 2.102 |
| `com.tekmess.snaar.RequisitosFuncionalesTest` | 10 | 0 | 0 | 0 | 4.377 |

## Cobertura por paquete

| Paquete | Líneas | Ramas | Métodos | Clases |
|---|---:|---:|---:|---:|
| `com.tekmess.snaar.controlador.servicio` | 64.59% (135/209) | 42.75% (59/138) | 76.19% (16/21) | 100% (3/3) |
| `com.tekmess.snaar.modelo.entidad` | 92.89% (209/225) | 75% (15/20) | 91.67% (110/120) | 100% (8/8) |
| `com.tekmess.snaar.patron.command` | 100% (84/84) | 81.82% (18/22) | 100% (25/25) | 100% (5/5) |
| `com.tekmess.snaar.patron.observer` | 97.22% (105/108) | 85.71% (18/21) | 96.3% (26/27) | 100% (5/5) |
| `com.tekmess.snaar.util` | 90.7% (78/86) | 67.19% (43/64) | 94.74% (18/19) | 100% (3/3) |

## Interpretación

- La cobertura de líneas alcanzada es **85.81%**, lo que indica que la mayor parte del código funcional incluido en el análisis fue ejecutado por pruebas automatizadas.
- La cobertura de ramas es **57.74%**. Esta métrica es más exigente porque mide decisiones lógicas como validaciones, condiciones y caminos alternos.
- La cobertura de métodos es **91.98%**, por lo que los casos de prueba ejercitan la mayoría de operaciones principales incluidas en el alcance de cobertura.

## Archivos de referencia

- Reporte HTML de JaCoCo: `target/site/jacoco/index.html`
- Reporte XML de JaCoCo: `target/site/jacoco/jacoco.xml`
- Resultados Surefire: `target/surefire-reports/`

## Observaciones

- El reporte considera las clases incluidas en la configuración actual de JaCoCo.
- Las pruebas ejecutadas corresponden a las suites JUnit disponibles en `src/test/java`.
- Para actualizar este reporte, se debe ejecutar nuevamente `mvn clean test jacoco:report surefire-report:report` dentro del contenedor Maven y regenerar el Markdown.
