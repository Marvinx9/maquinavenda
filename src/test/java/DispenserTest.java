import models.dinheiro.Dispenser;
import models.dinheiro.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DispenserTest {
    private Dispenser dispenser;

    @Before
    public void deveIniciarlizarODispenser(){
        dispenser = new Dispenser();
        dispenser.definirEstoque(MoedaDeCincoCentavos.class, 1);
        dispenser.definirEstoque(MoedaDeDezCentavos.class, 10);
        dispenser.definirEstoque(MoedaDeVinteECincoCentavos.class, 10);
        dispenser.definirEstoque(MoedaDeCinquentaCentavos.class, 10);
        dispenser.definirEstoque(MoedaDeUmReal.class, 10);
        dispenser.definirEstoque(NotaDeUmReal.class, 2);
        dispenser.definirEstoque(NotaDeDoisReais.class, 10);
        dispenser.definirEstoque(NotaDeCincoReais.class, 10);
        dispenser.definirEstoque(NotaDeDezReais.class, 10);
        dispenser.definirEstoque(NotaDeVinteReais.class, 10);
        dispenser.definirEstoque(NotaDeCinquentaReais.class, 10);
        dispenser.definirEstoque(NotaDeCemReais.class, 10);
        dispenser.definirEstoque(NotaDeDozentosReais.class, 1);
    }

    @Test
    public void deveEstarInicializado(){
        double total = dispenser.getSaldoTotal();
        assertEquals(2090.55, total, 0);
    }

    @Test
    public void deveIncrementarEstoque() {
        int qntdMoedasEstoqueTotalEsperado = 15;
        dispenser.incrementarEstoque(MoedaDeDezCentavos.class, 5);
        assertEquals(qntdMoedasEstoqueTotalEsperado, dispenser.getQuantidade(MoedaDeDezCentavos.class));
    }

    @Test
    public void deveDecrementarEstoque() {
        int qntdMoedasEstoqueTotalEsperado = 5;
        dispenser.decrementarEstoque(MoedaDeDezCentavos.class, 5);
        assertEquals(qntdMoedasEstoqueTotalEsperado, dispenser.getQuantidade(MoedaDeDezCentavos.class));
    }

    @Test
    public void deveDarZeroDeTroco() {
        Dinheiro[] troco = dispenser.trocoPara(200, 200);
        assertArrayEquals(new Dinheiro[0],troco);
    }

    @Test
    public void naoDeveTerTroco() {
        Dinheiro[] troco = dispenser.trocoPara(20000, 123.40);
        assertNull(troco);
    }

    @Test
    public void deveCalcularTrocoSimples() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(MoedaDeCinquentaCentavos.class, 1);
        dispenser.definirEstoque(MoedaDeVinteECincoCentavos.class, 2);

        Dinheiro[] troco = dispenser.trocoPara(5.00, 4.00);

        assertTrue(troco.length > 0);

        for (int i = 1; i < troco.length; i++) {
            assertTrue(troco[i - 1].valor() >= troco[i].valor());
        }

        double total = 0.0;
        double totalExperado = 1.0;
        for (Dinheiro dinheiro : troco) {
            total += dinheiro.valor();
        }
        assertEquals(totalExperado, total, 0.0001);
    }

    @Test
    public void deveDarTrocoEmUmaNota() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(NotaDeVinteReais.class, 3);
        Dinheiro[] troco = dispenser.trocoPara(50.00, 30.00);

        assertEquals(1, troco.length);
        assertEquals(20.00, troco[0].valor(), 0.0001);
    }

    @Test
    public void deveDarTrocoComMoedasDiversas() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(MoedaDeUmReal.class, 2);
        dispenser.definirEstoque(MoedaDeCinquentaCentavos.class, 2);
        dispenser.definirEstoque(MoedaDeVinteECincoCentavos.class, 2);
        dispenser.definirEstoque(MoedaDeDezCentavos.class, 2);

        Dinheiro[] troco = dispenser.trocoPara(10.00, 8.15);

        double total = 0.0;
        for (Dinheiro d : troco) total += d.valor();
        assertEquals(1.85, total, 0.0001);

        for (int i = 1; i < troco.length; i++) {
            assertTrue(troco[i - 1].valor() >= troco[i].valor());
        }
    }

    @Test
    public void deveRetornarNullQuandoTrocoDecimalImpossivel() {
        dispenser.zerarEstoque();
        Dinheiro[] troco = dispenser.trocoPara(5.00, 4.03);
        assertNull(troco);
    }

    @Test
    public void deveDarTrocoUsandoNotasEMoedas() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(NotaDeVinteReais.class, 2);
        dispenser.definirEstoque(NotaDeDezReais.class, 2);
        dispenser.definirEstoque(NotaDeCincoReais.class, 5);
        dispenser.definirEstoque(MoedaDeUmReal.class, 5);
        dispenser.definirEstoque(MoedaDeCinquentaCentavos.class, 3);
        dispenser.definirEstoque(MoedaDeDezCentavos.class, 2);

        Dinheiro[] troco = dispenser.trocoPara(100.00, 63.40);
        double total = 0.0;
        for (Dinheiro d : troco) total += d.valor();
        assertEquals(36.60, total, 0.0001);
    }

    @Test
    public void deveRetornarNullQuandoEstoqueInsuficiente() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(MoedaDeUmReal.class, 1);
        Dinheiro[] troco = dispenser.trocoPara(10.00, 8.00);
        assertNull(troco);
    }

    @Test
    public void deveRetornarTrocoCorretamente() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(NotaDeCincoReais.class, 1);
        dispenser.definirEstoque(NotaDeDoisReais.class, 8);
        Dinheiro[] troco = dispenser.trocoPara(10.00, 4.00);
        double total = 0.0;
        for (Dinheiro d : troco) total += d.valor();
        assertEquals(6.00 ,total, 0.0001);
    }

    @Test
    public void deveRetornarTrocoCentavosCorretamente() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(NotaDeCincoReais.class, 1);
        dispenser.definirEstoque(NotaDeDoisReais.class, 8);
        dispenser.definirEstoque(MoedaDeCinquentaCentavos.class, 5);
        Dinheiro[] troco = dispenser.trocoPara(10.00, 9.50);
        double total = 0.0;
        for (Dinheiro d : troco) total += d.valor();
        assertEquals(0.50 ,total, 0.0001);
    }

    @Test
    public void deveRetornarOTrocoCorretamente() {
        dispenser.zerarEstoque();
        dispenser.definirEstoque(NotaDeCemReais.class, 1);
        dispenser.definirEstoque(NotaDeCincoReais.class, 1);
        dispenser.definirEstoque(NotaDeDoisReais.class, 1);

        Dinheiro[] troco = dispenser.trocoPara(110, 8.0);
        double total = 0.0;
        for (Dinheiro d : troco) total += d.valor();
        assertEquals(102.0 ,total, 0.0001);
    }
}
