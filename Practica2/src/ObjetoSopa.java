
import java.io.*;
import java.util.ArrayList;

class ObjetoSopa implements Serializable {

    char[][] sopa = new char[16][16];
    int score;
    double time;
    String[] animales = {"lobo", "abeja", "zorro", "gato", "perro", "mono", "caballo", "pajaro", "cocodrilo", "elefante", "ballena", "pez", "ardilla", "jirafa", "hormiga", "oso", "mariposa", "tigre", "leon", "raton"};
    String[] frutas = {"mango", "manzana", "coco", "platano", "mandarina", "uva", "guayaba", "papaya", "piña", "melon", "sandia", "naranja", "ciruela", "durazno", "higo", "zarzamora", "fresa", "zapote", "frambuesa", "kiwi"};
    String[] paises = {"mexico", "colombia", "egipto", "canada", "alemania", "iran", "japon", "china", "españa", "peru", "francia", "australia", "grecia", "italia", "monaco", "ucrania", "argentina", "brasil", "marruecos", "uruguay"};
    String[] oficina = {"pluma", "lapiz", "engrapadora", "hoja", "folder", "clip", "silla", "escritorio", "goma", "laptop", "impresora", "sacapuntas", "calculadora", "escaner", "mouse", "cinta", "teclado", "computadora", "cuaderno", "calendario"};
    ArrayList<String> palabras = new ArrayList<String>();

    ObjetoSopa() {
    }

    public char getSopa(int x, int y) {
        return sopa[x][y];
    }

    public void setSopa(int x, int y, char c) {
        sopa[x][y] = c;
    }

    public String getAnimales(int x) {
        return animales[x];
    }

    public String getFrutas(int x) {
        return frutas[x];
    }

    public String getPaises(int x) {
        return paises[x];
    }

    public String getOficina(int x) {
        return oficina[x];
    }

    public String getPalabras(int i) {
        return palabras.get(i);
    }

    public void setPalabras(String palabra) {
        palabras.add(palabra);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int i) {
        score = score + i;
    }

}
