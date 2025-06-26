/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalisisDecendente;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Programa4 {

    static Map<String, Map<String, String>> tablaPredictiva = new HashMap<>();
    static List<String> derivacion = new ArrayList<>();

    static {
        // tabla predictiva
        String[][] tabla = {
              //  +        (       )      id       $
                {" ",     "1",    " ",    "1",    " "},
                {"2",     " ",    "3",    " ",    "3"},
                {" ",     "4",    " ",    "5",    " "}
        };
        String[] filas = {"E", "E'", "T"};
        String[] columnas = {"+", "(", ")", "id", "$"};

        for (int i = 0; i < filas.length; i++) {
            tablaPredictiva.put(filas[i], new HashMap<>());
            for (int j = 0; j < columnas.length; j++) {
                if (!tabla[i][j].isEmpty())
                    tablaPredictiva.get(filas[i]).put(columnas[j], tabla[i][j]);
            }
        }
    }

    public static List<String> leerArchivo(String ruta) throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea = br.readLine();
        br.close();
        return new ArrayList<>(Arrays.asList((linea+"$").split(" ")));
    }

    public static void analizar(List<String> entrada) {
        Stack<String> pila = new Stack<>();
        pila.push("$");
        pila.push("E");

        System.out.printf("| %-20s | %-40s |\n", "PILA", "ENTRADA");

        while (!pila.isEmpty()) {
            String pilaTop = pila.peek();
            String entradaTop = entrada.get(0);

            System.out.printf("| %-20s | %-40s |\n", pila, String.join(" ", entrada));

            if (pilaTop.equals(entradaTop)) {
                pila.pop();
                entrada.remove(0);
            } else if (esTerminal(pilaTop)) {
                System.out.println("✖ Error: Terminal no coincide: " + pilaTop + " vs " + entradaTop);
                return;
            } else {
                String regla = tablaPredictiva.getOrDefault(pilaTop, new HashMap<>()).getOrDefault(entradaTop, null);
                if (regla == null) {
                    System.out.println("✖ Error sintáctico: No hay regla para [" + pilaTop + ", " + entradaTop + "]");
                    return;
                }

                pila.pop();

                switch (regla) {
                case "1": // E → T E'
                    derivacion.add("E → T E'");
                    pila.push("E'");
                    pila.push("T");
                    break;
                case "2": // E' → + T E'
                    derivacion.add("E' → + T E'");
                    pila.push("E'");
                    pila.push("T");
                    pila.push("+");
                    break;
                case "3": // E' → ε
                    derivacion.add("E' → λ");
                    break;
                case "4": // T → ( E )
                    derivacion.add("T → ( E )");
                    pila.push(")");
                    pila.push("E");
                    pila.push("(");
                    break;
                case "5": // T → id
                    derivacion.add("T → id");
                    pila.push("id");
                    break;
                    default:
                        System.out.println("✖ Regla no reconocida: " + regla);
                        return;
                }
            }
        }

        if (entrada.size() == 0) {
            System.out.println("✔ Palabra aceptada.");
            System.out.println("\n📘 Árbol de derivación:");
            for (String regla : derivacion) {
                System.out.println("  " + regla);
            }
        } else {
            System.out.println("✖ Entrada no consumida completamente.");
        }
    }

    static List<String> noTerminales = Arrays.asList("E", "E'", "T");

    static boolean esTerminal(String simbolo) {
        return !noTerminales.contains(simbolo);
    }

    public static void main(String[] args) {
        try {
            List<String> entrada = leerArchivo("C:\\Users\\Alexis Caballero\\Documents\\NetBeansProjects\\En4.txt");
            analizar(entrada);
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
    }
}

