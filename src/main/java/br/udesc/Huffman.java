package br.udesc;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {
    public void executar(boolean descompressao, File fOrigem, File fDestino) throws IOException {
        if (descompressao) {
            descompressFrequencia(fOrigem, fDestino);
        } else {
            compressFrequencia(fOrigem, fDestino);
        }
    }

    private void compressFrequencia(File fOrigem, File fDestino) throws IOException {
        List<Character> caracteres = leCaracteres(fOrigem);
        int[] contagem = new int[255];
        for (Character caracter :caracteres) {
            contagem[caracter]++;
        }

        Map<Character, String> map = new HashMap<Character, String>();
        HTree tree = montarArvore(contagem);
        gerarMapa(montarArvore(contagem), new StringBuffer(), map);
        StringBuilder resultado = new StringBuilder();
        for (Character c : caracteres) {
            resultado.append(map.get(c));
        }
        ObjectOutputStream obt = new ObjectOutputStream(new FileOutputStream(fDestino));
        obt.writeObject(tree);
        obt.writeObject(resultado.toString());
    }

    private void descompressFrequencia(File fOrigem, File fDestino) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fOrigem));
        StringBuilder result = new StringBuilder();
        try {
            HTreeNo tree = (HTreeNo) objectInputStream.readObject();
            String valor = (String) objectInputStream.readObject();
            char chars[] = valor.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                HTree folha = tree;
                while (folha instanceof HTreeNo) {
                    if (c == '0')
                        folha = ((HTreeNo)folha).esquerda;
                    else
                        folha = ((HTreeNo)folha).direita;
                    if ((i + 1) < chars.length) {
                        c = chars[++i];
                    }
                }
                if ((i + 1) < chars.length) {
                    i--;
                }
                result.append(((HTreeFolha)folha).valor);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        FileWriter fileWriter = new FileWriter(fDestino);
        fileWriter.append(result.toString());
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

    public HTree montarArvore(int[] contagem) {
        PriorityQueue<HTree> pQueue = new PriorityQueue<HTree>();
        for (int i = 0; i < contagem.length; i++) {
            if (contagem[i] > 0) {
                pQueue.offer(new HTreeFolha(contagem[i], (char) i));
            }
        }

        while (pQueue.size() > 1) {
            HTree a = pQueue.poll();
            HTree b = pQueue.poll();
            pQueue.offer(new HTreeNo(a, b));
        }
        return pQueue.poll();
    }

    public void gerarMapa(HTree tree, StringBuffer prefixo, Map<Character, String> map) {
        assert tree != null;
        if (tree instanceof HTreeFolha) {
            HTreeFolha folha = (HTreeFolha) tree;
            map.put(folha.valor, prefixo.toString());
        } else if (tree instanceof HTreeNo) {
            HTreeNo node = (HTreeNo) tree;
            prefixo.append('0');
            gerarMapa(node.esquerda, prefixo, map);
            prefixo.deleteCharAt(prefixo.length() - 1);

            prefixo.append('1');
            gerarMapa(node.direita, prefixo, map);
            prefixo.deleteCharAt(prefixo.length() - 1);
        }
    }
}
