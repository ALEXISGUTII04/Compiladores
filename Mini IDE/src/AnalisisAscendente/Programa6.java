/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalisisAscendente;

import java.io.*;
import java.util.*;

import AnalisisAscendente.Programa7.Entrada;

public class Programa6 {

    static class Entrada {
        int estado;
        String simbolo;

        Entrada(int estado, String simbolo) {
            this.estado = estado;
            this.simbolo = simbolo;
        }

        @Override
        public int hashCode() {
            return Objects.hash(estado, simbolo);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Entrada)) return false;
            Entrada e = (Entrada) obj;
            return estado == e.estado && simbolo.equals(e.simbolo);
        }
    }

    static Map<Entrada, String> tabla = new HashMap<>();
    static List<String> producciones = new ArrayList<>();
    static List<String> arbolDerivacion = new ArrayList<>();

    static {
        producciones.add("E -> T E'");        // r1
        producciones.add("E' -> + T E'");     // r2
        producciones.add("E' -> %");          // r3
        producciones.add("T -> F T'");        // r4
        producciones.add("T' -> * F T'");     // r5
        producciones.add("T' -> %");          // r6
        producciones.add("F -> ( E )");       // r7
        producciones.add("F -> id");          // r8

        tabla.put(new Entrada(0, "id"), "s5");
        tabla.put(new Entrada(0, "("), "s4");
        tabla.put(new Entrada(0, "E"), "1");
        tabla.put(new Entrada(0, "T"), "2");
        tabla.put(new Entrada(0, "F"), "3");

        tabla.put(new Entrada(1, "$"), "aceptada");

        tabla.put(new Entrada(2, "+"), "s7");
        tabla.put(new Entrada(2, "E'"), "6");
        tabla.put(new Entrada(2, ")"), "r3");
        tabla.put(new Entrada(2, "$"), "r3");

        tabla.put(new Entrada(3, "*"), "s9");
        tabla.put(new Entrada(3, "T'"), "8");
        tabla.put(new Entrada(3, "+"), "r6");
        tabla.put(new Entrada(3, ")"), "r6");
        tabla.put(new Entrada(3, "$"), "r6");

        tabla.put(new Entrada(4, "F"), "3");
        tabla.put(new Entrada(4, "E"), "10");
        tabla.put(new Entrada(4, "T"), "2");
        tabla.put(new Entrada(4, "("), "s4");
        tabla.put(new Entrada(4, "id"), "s5");

        tabla.put(new Entrada(5, "+"), "r8");
        tabla.put(new Entrada(5, "*"), "r8");
        tabla.put(new Entrada(5, ")"), "r8");
        tabla.put(new Entrada(5, "$"), "r8");

        tabla.put(new Entrada(6, ")"), "r1");
        tabla.put(new Entrada(6, "$"), "r1");

        tabla.put(new Entrada(7, "("), "s4");
        tabla.put(new Entrada(7, "id"), "s5");
        tabla.put(new Entrada(7, "F"), "3");
        tabla.put(new Entrada(7, "T"), "11");

        tabla.put(new Entrada(8, "+"), "r4");
        tabla.put(new Entrada(8, ")"), "r4");
        tabla.put(new Entrada(8, "$"), "r4");

        tabla.put(new Entrada(9, "("), "s4");
        tabla.put(new Entrada(9, "id"), "s5");
        tabla.put(new Entrada(9, "F"), "12");

        tabla.put(new Entrada(10, ")"), "s15");

        tabla.put(new Entrada(11, "+"), "s7");
        tabla.put(new Entrada(11, "E'"), "13");
        tabla.put(new Entrada(11, ")"), "r3");
        tabla.put(new Entrada(11, "$"), "r3");

        tabla.put(new Entrada(12, "*"), "s9");
        tabla.put(new Entrada(12, "T'"), "14");
        tabla.put(new Entrada(12, "+"), "r6");
        tabla.put(new Entrada(12, ")"), "r6");
        tabla.put(new Entrada(12, "$"), "r6");

        tabla.put(new Entrada(13, ")"), "r2");
        tabla.put(new Entrada(13, "$"), "r2");

        tabla.put(new Entrada(14, "+"), "r5");
        tabla.put(new Entrada(14, ")"), "r5");
        tabla.put(new Entrada(14, "$"), "r5");

        tabla.put(new Entrada(15, "+"), "r7");
        tabla.put(new Entrada(15, ")"), "r7");
        tabla.put(new Entrada(15, "$"), "r7");
        tabla.put(new Entrada(15, "*"), "r7");
    }

    public static void analizar(String entrada) {
        Stack<Integer> pila = new Stack<>();
        pila.push(0);
        List<String> tokens = tokenizar(entrada);
        tokens.add("$");
        int i = 0;

        System.out.printf("%-30s %-30s %-15s\n", "Pila", "Entrada", "Acción");

        while (true) {
            int estadoActual = pila.peek();
            String simboloActual = tokens.get(i);
            Entrada entradaTabla = new Entrada(estadoActual, simboloActual);
            String accion = tabla.getOrDefault(entradaTabla, "error");

            System.out.printf("%-30s %-30s %-15s\n",
                    pila.toString(), String.join(" ", tokens.subList(i, tokens.size())), accion);

            if (accion.equals("aceptada")) {
                System.out.println("Cadena aceptada.");

                // Imprimir árbol de derivación inversa
                System.out.println("\nÁrbol de derivación inversa:");
                for (int j = arbolDerivacion.size() - 1; j >= 0; j--) {
                    System.out.println(arbolDerivacion.get(j));
                }
                break;
            } else if (accion.startsWith("s")) {
                int nuevoEstado = Integer.parseInt(accion.substring(1));
                pila.push(nuevoEstado);
                i++;
            } else if (accion.startsWith("r")) {
                int regla = Integer.parseInt(accion.substring(1)) - 1;
                String produccion = producciones.get(regla);
                System.out.println("Usando regla: " + produccion);
                arbolDerivacion.add(produccion); // <- Guardar la producción

                String[] partes = produccion.split("->");
                String ladoDerecho = partes[1].trim();
                int elementos = ladoDerecho.equals("%") ? 0 : ladoDerecho.split(" ").length;
                for (int j = 0; j < elementos; j++) {
                    pila.pop();
                }

                int estadoTope = pila.peek();
                String noTerminal = partes[0].trim();
                String accionGoto = tabla.get(new Entrada(estadoTope, noTerminal));
                if (accionGoto == null) {
                    System.out.println("Error en GOTO.");
                    break;
                }
                pila.push(Integer.parseInt(accionGoto));
            } else {
                System.out.println("Error de sintaxis.");
                break;
            }
        }
    }
    public static List<String> tokenizar(String entrada) {
        List<String> tokens = new ArrayList<>();
        Set<String> palabrasValidas = new HashSet<>(Arrays.asList("+", "*", "(", ")", "id"));

        entrada = entrada.replaceAll("\\s+", "");
        int i = 0;
        while (i < entrada.length()) {
            boolean encontrado = false;
            for (String palabra : palabrasValidas) {
                if (entrada.startsWith(palabra, i)) {
                    tokens.add(palabra);
                    i += palabra.length();
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("Token no reconocido en la posición " + i);
                System.exit(1);
            }
        }
        return tokens;
    }

    public static String leerArchivo(String rutaArchivo) {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea.trim()).append(" ");
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            System.exit(1);
        }
        return contenido.toString().trim();
    }

    public static void main(String[] args) {
        String ruta = "C:\\Users\\Alexis Caballero\\Documents\\NetBeansProjects\\En6.txt";
        String contenido = leerArchivo(ruta);
        System.out.println("Analizando: " + contenido);
        analizar(contenido);
    }
}

