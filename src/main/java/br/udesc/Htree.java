package br.udesc;

abstract class HTree implements Comparable<HTree> {
    public final int contagem; // the contagem of this tree
    public HTree(int freq) { contagem = freq; }

    // compares on the contagem
    public int compareTo(HTree tree) {
        return contagem - tree.contagem;
    }
}

class HTreeFolha extends HTree {
    public final char valor; // the character this leaf represents

    public HTreeFolha(int contagem, char valor) {
        super(contagem);
        this.valor = valor;
    }
}

class HTreeNo extends HTree {
    public final HTree esquerda, direita; // subtrees

    public HTreeNo(HTree l, HTree r) {
        super(l.contagem + r.contagem);
        esquerda = l;
        direita = r;
    }
}