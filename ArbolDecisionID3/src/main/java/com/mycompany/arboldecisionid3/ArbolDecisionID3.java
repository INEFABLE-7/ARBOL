/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.arboldecisionid3;
package arboldecisionid3;

import java.util.*;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

import java.util.List;
import java.util.Map;

import java.util.List;

import java.util.List;

class NodoArbol {
    String atributo;
    Map<String, NodoArbol> hijos;
    String resultado;

    NodoArbol(String atributo) {
        this.atributo = atributo;
        hijos = new HashMap<>();
        resultado = null;
    }
}
/**
 *
 * @author JEAN
 */
public class ArbolDecisionID3 {

    public static void main(String[] args) {
         List<String[]> datos = new ArrayList<>();
        datos.add(new String[]{"Soleado", "Caluroso", "Alta", "No"});
        datos.add(new String[]{"Soleado", "Caluroso", "Normal", "No"});
        datos.add(new String[]{"Nublado", "Templado", "Alta", "Sí"});
        datos.add(new String[]{"Lluvioso", "Fresco", "Alta", "Sí"});
        datos.add(new String[]{"Lluvioso", "Fresco", "Normal", "Sí"});
        datos.add(new String[]{"Nublado", "Fresco", "Normal", "Sí"});
        datos.add(new String[]{"Soleado", "Templado", "Alta", "No"});
        
            String[] atributos = {"Clima", "Temperatura", "Humedad"};
        NodoArbol arbol = construirArbol(datos, atributos, "JugarTenis");

        imprimirArbol(arbol, "");
     
    }
     public static NodoArbol construirArbol(List<String[]> datos, String[] atributos, String clase) {
        if (todosEjemplosSonDeLaMismaClase(datos, clase)) {
            NodoArbol nodoHoja = new NodoArbol(null);
            nodoHoja.resultado = datos.get(0)[datos.get(0).length - 1];
            return nodoHoja;
        }
   if (atributos.length == 0) {
            NodoArbol nodoHoja = new NodoArbol(null);
            nodoHoja.resultado = claseMasComun(datos, clase);
            return nodoHoja;
        }
  
   String mejorAtributo = calcularMejorAtributo(datos, atributos, clase);

        NodoArbol nodo = new NodoArbol(mejorAtributo);
        Map<String, List<String[]>> subconjuntos = dividirDatos(datos, mejorAtributo, atributos);
        
        for (String valor : subconjuntos.keySet()) {
            List<String[]> subconjunto = subconjuntos.get(valor);
          
            if (subconjunto.isEmpty()) {
                NodoArbol nodoHoja = new NodoArbol(null);
                nodoHoja.resultado = claseMasComun(datos, clase);
                nodo.hijos.put(valor, nodoHoja);
                
            }   else {
                String[] nuevosAtributos = quitarAtributo(atributos, mejorAtributo);
                NodoArbol subArbol = construirArbol(subconjunto, nuevosAtributos, clase);
                nodo.hijos.put(valor, subArbol);
            }
        }

        return nodo;
    }
     
public static boolean todosEjemplosSonDeLaMismaClase(List<String[]> datos, String clase) {
    String primeraClase =datos.get(0)[datos.get(0).length - 1];
    for (String[] ejemplo : datos) {
        if (!ejemplo[ejemplo.length - 1].equals(primeraClase)) {
            return false;
        }
    }
    return true;
}
}

            


