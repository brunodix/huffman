package br.udesc;

import com.sun.org.apache.regexp.internal.CharacterArrayCharacterIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Frequencia {

    private List<Freq> lista = new ArrayList();

    public void compressFrequencia(File fOrigem, File fDestino) throws IOException {
        List<Freq> listaResultado = new ArrayList<Freq>();
        List<Character> lista = leCaracteres(fOrigem);
        Iterator<Character> iterator = lista.iterator();
        char anterior = iterator.next();
        int contador = 1;
        while (iterator.hasNext()) {
            char caracter = iterator.next();
            if (caracter == anterior) {
                contador++;
            } else {
                listaResultado.add(new Freq(contador, anterior));
                anterior = caracter;
                contador = 1;
            }
        }
        listaResultado.add(new Freq(contador, anterior));

        FileWriter fileWriter = new FileWriter(fDestino);
        for (Freq f : listaResultado) {
            fileWriter.append(f.toString());
        }
        fileWriter.flush();
        fileWriter.close();
    }

    public void executar(boolean descompressao, File fOrigem, File fDestino) throws IOException {
        if (descompressao) {
            descompressFrequencia(fOrigem, fDestino);
        } else {
            compressFrequencia(fOrigem, fDestino);
        }
    }

    private void descompressFrequencia(File fOrigem, File fDestino) throws IOException {
        List<Character> listaResultado = new ArrayList<Character>();
        List<Character> lista = leCaracteres(fOrigem);
        Iterator<Character> iterator = lista.iterator();
        char anterior = iterator.next();
        while (iterator.hasNext()) {
            if (Character.isDigit(anterior)) {
                char proximo = iterator.next();
                if (proximo == '@') {
                    proximo = iterator.next();
                    int cont = Character.getNumericValue(anterior);
                    for (int i = 0; i < cont; i++ ){
                        listaResultado.add(proximo);
                    }
                } else if (Character.isDigit(proximo)) {
                    listaResultado.add(anterior);
                    anterior = proximo;
                }
                else {
                    int cont = Character.getNumericValue(anterior);
                    for (int i = 0; i < cont; i++ ){
                        listaResultado.add(proximo);
                    }
                    anterior = iterator.next();
                }
            } else {
                listaResultado.add(anterior);
                anterior = iterator.next();
            }

        }

        FileWriter fileWriter = new FileWriter(fDestino);
        for (Character c : listaResultado) {
            fileWriter.append(c);
        }
        fileWriter.flush();
        fileWriter.close();

    }

    public List<Character> leCaracteres(File fOrigem) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fOrigem));
        char chars[] = new char[256];
        List<Character> listaBytes = new ArrayList<Character>();
        int quantidade = reader.read(chars);
        while (quantidade > -1) {
            for (int i = 0; i < quantidade; i++) {
                listaBytes.add(chars[i]);
            }
            quantidade = reader.read(chars);
        }
        reader.close();
        return listaBytes;
    }

    private class Freq {
        int count;
        char valor;

        public Freq(int count, char valor) {
            this.count = count;
            this.valor = valor;
        }

        @Override
        public String toString() {
            if (count == 1) {
                return String.valueOf(valor);
            } else if (Character.isDigit(valor)) {
                return String.valueOf(count) + "@" + String.valueOf(valor);
            } else {
                return String.valueOf(count) + String.valueOf(valor);
            }
        }
    }
}
