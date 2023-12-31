/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package arboldecisionid3;


import java.util.ArrayList;
import java.util.Arrays;

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
    return nuevosAtributos.toArray(String[]::new);
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
public static double calcularEntropiaAtributo(List<String[]> datos, String atributo, String[] atributos, String clase) {
    int indiceAtributo = Arrays.asList(atributos).indexOf(atributo);

    Map<String, Integer> frecuenciaValores = new HashMap<>();
    Map<String, Map<String, Integer>> frecuenciaValoresClase = new HashMap<>();

    for (String[] ejemplo : datos) {
        String valorAtributo = ejemplo[indiceAtributo];
        String claseEjemplo = ejemplo[ejemplo.length - 1];

        frecuenciaValores.put(valorAtributo, frecuenciaValores.getOrDefault(valorAtributo, 0) + 1);

        if (!frecuenciaValoresClase.containsKey(valorAtributo)) {
            frecuenciaValoresClase.put(valorAtributo, new HashMap<>());
        }
        frecuenciaValoresClase.get(valorAtributo).put(claseEjemplo, frecuenciaValoresClase.get(valorAtributo).getOrDefault(claseEjemplo, 0) + 1);
    }
    
    double entropiaAtributo = 0;
    for (String valor : frecuenciaValores.keySet()) {
        double probabilidadValor = (double) frecuenciaValores.get(valor) / datos.size();
        double entropiaValor = 0;
        for (String claseValor : frecuenciaValoresClase.get(valor).keySet()) {
            double probabilidadClase = (double) frecuenciaValoresClase.get(valor).get(claseValor) / frecuenciaValores.get(valor);
            entropiaValor -= probabilidadClase * Math.log(probabilidadClase) / Math.log(2);
        }
        entropiaAtributo += probabilidadValor * entropiaValor;
    }

    return entropiaAtributo;
}
    private static void imprimirArbol(NodoArbol arbol, String espacio) {
    if (arbol.atributo == null) {
        System.out.println(espacio + "Resultado: " + arbol.resultado);
        return;
    }
    System.out.println(espacio + arbol.atributo + ":");
    for (String valor : arbol.hijos.keySet()) {
        System.out.println(espacio + "  " + valor);
        imprimirArbol(arbol.hijos.get(valor), espacio + "    ");
    }
    }
}

    




