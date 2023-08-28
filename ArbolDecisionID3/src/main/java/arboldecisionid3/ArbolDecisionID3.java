/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package arboldecisionid3;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public static String claseMasComun(List<String[]> datos, String clase) {
    Map<String, Integer> frecuenciaClases = new HashMap<>();
    for (String[] ejemplo : datos) {
        String claseEjemplo = ejemplo[ejemplo.length - 1];
        frecuenciaClases.put(claseEjemplo, frecuenciaClases.getOrDefault(claseEjemplo, 0) + 1);
    }
    
    int maxFrecuencia = -1;
    String claseMasComun = null;
    for (String key : frecuenciaClases.keySet()) {
        int frecuencia = frecuenciaClases.get(key);
        if (frecuencia > maxFrecuencia) {
            maxFrecuencia = frecuencia;
            claseMasComun = key;
        }
    }
    return claseMasComun;
}
public static String calcularMejorAtributo(List<String[]> datos, String[] atributos, String clase) {
    double mejorGanancia = -1;
    String mejorAtributo = null;
 
    
    double entropiaInicial = calcularEntropia(datos, clase);
 for (String atributo : atributos) {
        double ganancia = entropiaInicial - calcularEntropiaAtributo(datos, atributo, atributos, clase);
        if (ganancia > mejorGanancia) {
            mejorGanancia = ganancia;
            mejorAtributo = atributo;
        }
    }

    return mejorAtributo;
}
public static Map<String, List<String[]>> dividirDatos(List<String[]> datos, String atributo, String[] atributos) {
    Map<String, List<String[]>> subconjuntos = new HashMap<>();
    int indiceAtributo = Arrays.asList(atributos).indexOf(atributo);

    for (String[] ejemplo : datos) {
        String valorAtributo = ejemplo[indiceAtributo];
        subconjuntos.putIfAbsent(valorAtributo, new ArrayList<>());
        subconjuntos.get(valorAtributo).add(ejemplo);
    }

    return subconjuntos;
}
public static String[] quitarAtributo(String[] atributos, String atributo) {
    List<String> nuevosAtributos = new ArrayList<>(Arrays.asList(atributos));
    nuevosAtributos.remove(atributo);
    return nuevosAtributos.toArray(new String[0]);
}

public static double calcularEntropia(List<String[]> datos, String clase) {
    int totalEjemplos = datos.size();
    Map<String, Integer> frecuenciaClases = new HashMap<>();
    for (String[] ejemplo : datos) {
        String claseEjemplo = ejemplo[ejemplo.length - 1];
        frecuenciaClases.put(claseEjemplo, frecuenciaClases.getOrDefault(claseEjemplo, 0) + 1);
    }
double entropia = 0;
    for (String key : frecuenciaClases.keySet()) {
        double probabilidad = (double) frecuenciaClases.get(key) / totalEjemplos;
        entropia -= probabilidad * Math.log(probabilidad) / Math.log(2);
    }

    return entropia;
}
}


