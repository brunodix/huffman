package br.udesc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

        mostrarCodigos(montarArvore(contagem), new StringBuffer());
    }

    private void descompressFrequencia(File fOrigem, File fDestino) {
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

    private class Freq implements Serializable, Comparable {
        int cont;
        char valor;

        public Freq(int cont, char valor) {
            this.cont = cont;
            this.valor = valor;
        }


        public int compareTo(Object o) {
            if (this == o) {
                return 0;
            } else {
                Freq other = (Freq) o;
                if (this.cont == other.cont) {
                    return Character.valueOf(this.valor).compareTo(other.valor);
                } else {
                    return this.cont - other.cont;
                }
            }
        }
    }

    public HTree montarArvore(int[] contagem) {
        PriorityQueue<HTree> pQueue = new PriorityQueue<HTree>();
        for (int i = 0; i < contagem.length; i++)
            if (contagem[i] > 0)
                pQueue.offer(new HTreeFolha(contagem[i], (char) i));

        assert pQueue.size() > 0;
        while (pQueue.size() > 1) {
            HTree a = pQueue.poll();
            HTree b = pQueue.poll();
            pQueue.offer(new HTreeNo(a, b));
        }
        return pQueue.poll();
    }

    public void mostrarCodigos(HTree tree, StringBuffer prefixo) {
        assert tree != null;
        if (tree instanceof HTreeFolha) {
            HTreeFolha leaf = (HTreeFolha) tree;
            System.out.println(leaf.valor + "\t" + leaf.contagem + "\t" + prefixo);

        } else if (tree instanceof HTreeNo) {
            HTreeNo node = (HTreeNo) tree;
            prefixo.append('0');
            mostrarCodigos(node.esquerda, prefixo);
            prefixo.deleteCharAt(prefixo.length() - 1);

            prefixo.append('1');
            mostrarCodigos(node.direita, prefixo);
            prefixo.deleteCharAt(prefixo.length() - 1);
        }
    }
}
