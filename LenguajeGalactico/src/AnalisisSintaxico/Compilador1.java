/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalisisSintaxico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Compilador1 {
    public static void main(String[] args) {
        int finalEstado = 4;

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Alexis Caballero\\Documents\\NetBeansProjects\\Com1.txt"))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                System.out.println("\nLeyendo transición: " + linea);
                int estado = 0; 
                boolean esValida = true; 

                for (char c : linea.toCharArray()) {
                    int nuevo = transformar(c);
                    if (nuevo == -1) { 
                        System.out.println("❌ ERROR: Caracter no correcto -> '" + c + "'");
                        esValida = false;
                        break;
                    }
                    
                    int estadoAnterior = estado;
                    estado = evaluar(estado, nuevo);

                    if (estado == -1) { 
                        System.out.println("❌ ERROR: Transición no correcta en estado " + estadoAnterior + " con entrada " + c);
                        esValida = false;
                        break;
                    }

                    System.out.println("✔ Transición: Estado " + estadoAnterior + " -> Estado " + estado + " con entrada " + c);
                }

                if (esValida && estado == finalEstado) {
                    System.out.println("✅ Resultado: Correcto");
                } else {
                    System.out.println("❌ Resultado: No correcto");
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public static int transformar(char c) {
        switch (c) {
            case '0': return 0;
            case '1': return 1;
            default: return -1; 
        }
    }

    public static int evaluar(int estado, int nuevo) {
        int[][] TL = {
            {1, 5},  
            {2, 2},  
            {3, 3},  
            {4, 4},  
            {-1, -1}, 
            {6, -1},  
            {3, -1}   
        };

        return TL[estado][nuevo];
    }
}
