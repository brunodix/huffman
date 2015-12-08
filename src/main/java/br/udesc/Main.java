package br.udesc;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            StringBuilder builder = new StringBuilder();
            builder.append("Informe os parâmetros: <algoritmo> <nome_do_arquivo>");
            builder.append("\nAlgoritmo:");
            builder.append("\n\t\t\t1 - Frequência de Caracteres");
            builder.append("\n\t\t\t2 - Huffman");
            System.out.println(builder.toString());
            return;
        }

        int algoritmo = Integer.valueOf(args[0]);
        if (algoritmo < 1 || algoritmo > 2) {
            System.out.println("Algoritmo desconhecido!");
            return;
        }

        String nomeOrigem = args[1];
        boolean descompressao = nomeOrigem.endsWith(".pra");

        File arquivoOrigem = new File(nomeOrigem);
        String nomeDestino;
        if (descompressao) {
            nomeDestino = nomeOrigem.substring(0, nomeOrigem.indexOf('.')) + ".txt";
        } else {
            nomeDestino = nomeOrigem.substring(0, nomeOrigem.indexOf('.')) + ".pra";
        }

        File arquivoDestino = new File(nomeDestino);

        if (!arquivoOrigem.exists()) {
            System.out.println("Arquivo inexistente!");
            return;
        } else {
            try {
                if (algoritmo == 1) {
                    new Frequencia().executar(descompressao, arquivoOrigem, arquivoDestino);
                } else {
                    new Huffman().executar(descompressao, arquivoOrigem, arquivoDestino);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void huffman(FileInputStream fitOrigem, FileOutputStream fitDestino) {

    }
}
