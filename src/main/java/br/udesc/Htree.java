package br.udesc;

import java.io.Serializable;

abstract class HTree implements Comparable<HTree>, Serializable {
    public final int contagem;
    public HTree(int freq) { contagem = freq; }

    public int compareTo(HTree tree) {
        return contagem - tree.contagem;
    }
}

class HTreeFolha extends HTree implements Serializable {
    public final char valor;

    public HTreeFolha(int contagem, char valor) {
        super(contagem);
        this.valor = valor;
    }
}

class HTreeNo extends HTree implements Serializable {
    public final HTree esquerda, direita;

    public HTreeNo(HTree l, HTree r) {
        super(l.contagem + r.contagem);
        esquerda = l;
        direita = r;
    }
}