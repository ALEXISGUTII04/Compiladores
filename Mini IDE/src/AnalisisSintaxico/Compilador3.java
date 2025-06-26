/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalisisSintaxico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Compilador3 {
	public static void main(String[] args) {
		int estadoFinal = 13;

		try (BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\Alexis Caballero\\Documents\\NetBeansProjects\\Com3.txt"))) {
			String linea;

			while ((linea = br.readLine()) != null) {
				System.out.println("\nLeyendo ecuación: " + linea);
				int estado = 0;
				boolean esValida = true;

				for (char c : linea.toCharArray()) {
					int nuevo = transformar(c);
					if (nuevo == -1) {
						System.out.println("❌ ERROR: Caracter no válido -> '" + c + "'");
						esValida = false;
						break;
					}

					int estadoAnterior = estado;
					estado = evaluar(estado, nuevo);

					if (estado == -1) {
						System.out.println(
								"❌ ERROR: Transición no válida en estado " + estadoAnterior + " con entrada " + c);
						esValida = false;
						break;
					}

					System.out.println(
							"✔ Transición: Estado " + estadoAnterior + " -> Estado " + estado + " con entrada " + c);
				}

				if (esValida && estado == estadoFinal) {
					System.out.println("✅ Resultado: Aceptado");
				} else {
					System.out.println("❌ Resultado: No Aceptado");
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
	        case '2': return 2;
	        case '3': return 3;
	        case '4': return 4;
	        case '5': return 5;
	        case '6': return 6;
	        case '7': return 7;
	        case '8': return 8;
	        case '9': return 9;
	        case 'x': return 10;
	        case '^': return 11;
	        case '+': return 12;
	        case '-': return 13;
	        case '=': return 14;
	        default: return -1;
	    }
	}

	public static int evaluar(int estado, int nuevo) {
	    int[][] TL = {
   //    | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | x | ^ | + | - | = |
	     {-1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  3, -1,  1,  1, -1 },  // Q0  
	     {-1,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3, -1, -1, -1, -1 },  // Q1  
	     {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  3, -1, -1, -1, -1 },  // Q2  
	     {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  3,  4, -1, -1, -1 },  // Q3  
	     {-1, -1,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },  // Q4  
	     {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  6,  6, 11 },  // Q5  
	     {-1,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7, -1, -1, -1, -1 },  // Q6  
	     {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  8, -1, -1, -1, 11 },  // Q7  
	     {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  9,  9, 11 },  // Q8  
	     {-1, 10, 10, 10, 10, 10, 10, 10, 10, 10, -1, -1, -1, -1, -1 },  // Q9  
	     {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11 },  // Q10  
	     {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, -1, -1, -1, 12, 12 },  // Q11  
	     {13, 13, 13, 13, 13, 13, 13, 13, 13, 13, -1, -1, -1, -1, -1 },  // Q12  
	     {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }   // QF  
	    			};
	    return (estado >= 0 && estado < TL.length && nuevo >= 0 && nuevo < TL[0].length) ? TL[estado][nuevo] : -1;
	}
}