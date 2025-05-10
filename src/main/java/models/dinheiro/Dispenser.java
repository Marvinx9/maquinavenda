package models.dinheiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dispenser {
    private final Dinheiro[] estoque = new Dinheiro[] {
            new MoedaDeCincoCentavos(),
            new MoedaDeDezCentavos(),
            new MoedaDeVinteECincoCentavos(),
            new MoedaDeCinquentaCentavos(),
            new MoedaDeUmReal(),
            new NotaDeUmReal(),
            new NotaDeDoisReais(),
            new NotaDeCincoReais(),
            new NotaDeDezReais(),
            new NotaDeVinteReais(),
            new NotaDeCinquentaReais(),
            new NotaDeCemReais(),
    };

    public int getQuantidade(Class<? extends Dinheiro> clazz){
        return getDinheiro(clazz).getQuantidade();
    }

    public double getSaldoTotal() {
        double total = 0;
        for (Dinheiro dinheiro : estoque) {
            total += dinheiro.getQuantidade() * dinheiro.valor();
        }
        return total;
    }

    private Dinheiro getDinheiro(Class<? extends Dinheiro> clazz) {
        for (Dinheiro dinheiro : estoque) {
            if (dinheiro.getClass() == clazz) {
                return dinheiro;
            }
        }
        throw new IllegalArgumentException("Denominação não registrada: " + clazz.getSimpleName());
    }

    public void definirEstoque(Class<? extends Dinheiro> clazz, int quantidade){
        getDinheiro(clazz).setQuantidade(quantidade);
    }

    public void incrementarEstoque(Class<? extends Dinheiro> clazz, int quantidade){
        Dinheiro dinheiro = getDinheiro(clazz);
        dinheiro.setQuantidade(dinheiro.getQuantidade() + quantidade);
    }

    public void decrementarEstoque(Class<? extends Dinheiro> clazz, int quantidade){
        Dinheiro dinheiro = getDinheiro(clazz);
        dinheiro.setQuantidade(dinheiro.getQuantidade() - quantidade);
    }

    public Dinheiro[] trocoPara(double valorPago, double valorProduto) {
        double troco = valorPago - valorProduto;

        if (troco == 0.0) {
            return new Dinheiro[0];
        }

        List<Dinheiro> lista = new ArrayList<>();

        for (Dinheiro dinheiro : estoque) {
            if (dinheiro.getQuantidade() > 0) {
                lista.add(dinheiro);
            }
        }
        Dinheiro[] dinheiroExistente = lista.toArray(new Dinheiro[0]);

        Dinheiro[] retorno = calcularTroco(dinheiroExistente, troco, 0);
        if (retorno == null && dinheiroExistente.length % 2 == 0) {
            Dinheiro[] segundaTentativa = ordenarInventario(dinheiroExistente);
            return calcularTroco(realocarValores(segundaTentativa), troco,1);
        }
        return retorno;
    }

    public Dinheiro[] calcularTroco(Dinheiro[] dinheiroExistente, Double valorDoTroco,int numero) {
        for (int index = 0; index < dinheiroExistente.length; index++) {
            Dinheiro[] ordenado = Arrays.copyOf(dinheiroExistente, dinheiroExistente.length);
            if (numero == 0) {
                ordenado = ordenarInventario(ordenado);
            }

            ordenado = removerIndice(ordenado, index);

            Double troco = valorDoTroco;
            Dinheiro[] usados = new Dinheiro[100];
            int usadosIndex = 0;

            for (int i = 0; i < ordenado.length; i++) {
                Dinheiro dinheiro = ordenado[i];

                int quantidadeDisponivel = dinheiro.getQuantidade();

                if (dinheiro.getQuantidade() == 0) {
                    continue;
                }

                while (troco + 1e-6 >= dinheiro.valor() && quantidadeDisponivel > 0) {
                    if (usadosIndex == usados.length) {
                        return null;
                    }
                    troco = (double) Math.round((troco - dinheiro.valor()) * 100.0) / 100;

                    usados[usadosIndex++] = novaInstancia(dinheiro);
                    quantidadeDisponivel--;

                    if (troco == 0.0) {
                        return Arrays.copyOf(usados, usadosIndex);
                    }
                }
            }
        }
        return null;
    }

    private Dinheiro[] realocarValores(Dinheiro[] lista) {
        for (int i = 0; i < lista.length; i += 2) {
            Dinheiro temp = lista[i];
            lista[i] = lista[i + 1];
            lista[i + 1] = temp;
        }
        return lista;
    }

    private Dinheiro[] removerIndice(Dinheiro[] array, int indiceRemover) {
        if(indiceRemover == 0) {
            return array;
        }
        Dinheiro[] novoArray = new Dinheiro[array.length - 1];
        for (int i = 0, j = 0; i < array.length; i++) {
            if (i != indiceRemover - 1) {
                novoArray[j++] = array[i];
            }
        }
        return novoArray;
    }

    public Dinheiro[] ordenarInventario(Dinheiro[] ordenado){
        for (int i = 0; i < ordenado.length - 1; i++) {
            for (int j = i + 1; j < ordenado.length; j++) {
                if (ordenado[j].valor() > ordenado[i].valor()) {
                    Dinheiro tmp = ordenado[i];
                    ordenado[i] = ordenado[j];
                    ordenado[j] = tmp;
                }
            }
        }
        return ordenado;
    }

    private Dinheiro novaInstancia(Dinheiro dinheiro) {
        if (dinheiro instanceof MoedaDeCincoCentavos)       return new MoedaDeCincoCentavos();
        if (dinheiro instanceof MoedaDeDezCentavos)         return new MoedaDeDezCentavos();
        if (dinheiro instanceof MoedaDeVinteECincoCentavos) return new MoedaDeVinteECincoCentavos();
        if (dinheiro instanceof MoedaDeCinquentaCentavos)   return new MoedaDeCinquentaCentavos();
        if (dinheiro instanceof MoedaDeUmReal)              return new MoedaDeUmReal();
        if (dinheiro instanceof NotaDeUmReal)               return new NotaDeUmReal();
        if (dinheiro instanceof NotaDeDoisReais)            return new NotaDeDoisReais();
        if (dinheiro instanceof NotaDeCincoReais)           return new NotaDeCincoReais();
        if (dinheiro instanceof NotaDeDezReais)             return new NotaDeDezReais();
        if (dinheiro instanceof NotaDeVinteReais)           return new NotaDeVinteReais();
        if (dinheiro instanceof NotaDeCinquentaReais)       return new NotaDeCinquentaReais();
        if (dinheiro instanceof NotaDeCemReais)             return new NotaDeCemReais();

        throw new IllegalArgumentException("Tipo de dinheiro desconhecido");
    }

    public void zerarEstoque() {
        for (int i = 0; i < estoque.length; i++) {
            estoque[i].setQuantidade(0);
        }
    }
}
