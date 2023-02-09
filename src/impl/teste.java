package impl;

import dominio.PosicaoTabela;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class teste {
    public static void main(String[] args) throws IOException {
        CampeonatoBrasileiroImpl campeonatoBrasileiro = new CampeonatoBrasileiroImpl( (jogo) -> jogo.data().data().getYear() == 2018 );

        System.out.println("Total vitorias do mandante: "+campeonatoBrasileiro.getTotalVitoriasEmCasa());
        System.out.println("Total vitorias do visitante: "+campeonatoBrasileiro.getTotalVitoriasForaDeCasa());
        System.out.println("Total de empates no campeonato: "+campeonatoBrasileiro.getTotalEmpates());
        System.out.println("Jogos com menos de 3 gols: "+campeonatoBrasileiro.getTotalJogosComMenosDe3Gols());
        System.out.println("Jogos com 3 ou mais gols: "+campeonatoBrasileiro.getTotalJogosCom3OuMaisGols());
        System.out.println("Estatisticas (Placar mais repetido) - "
                + campeonatoBrasileiro.getPlacarMaisRepetido().getKey() + " (" +campeonatoBrasileiro.getPlacarMaisRepetido().getValue() + " jogo(s))");
        System.out.println("Estatisticas (Placar menos repetido) - "
                + campeonatoBrasileiro.getPlacarMenosRepetido().getKey() + " (" +campeonatoBrasileiro.getPlacarMenosRepetido().getValue() + " jogo(s))");
        System.out.printf("Media de gols por ano: %.2f %n", campeonatoBrasileiro.getMediaGolsPorJogo());
        System.out.println("Estatisticas (Total de gols) - " + campeonatoBrasileiro.getEstatisticasPorJogo().getSum());
        System.out.println("Estatisticas (Total de jogos) - " + campeonatoBrasileiro.getEstatisticasPorJogo().getCount());
        System.out.println("Estatisticas (Media de gols) - " + campeonatoBrasileiro.getEstatisticasPorJogo().getAverage());
        System.out.println("time: " +campeonatoBrasileiro.timeComMaisGolsNoCampeonato().getKey());
        System.out.println("gols : " +campeonatoBrasileiro.timeComMaisGolsNoCampeonato().getValue());
        System.out.println(campeonatoBrasileiro.getTabela());
        imprimirTabela(campeonatoBrasileiro.getTabela());






    }

    public static void imprimirTabela(List<PosicaoTabela> posicoes) {
        System.out.println();
        System.out.println("## TABELA CAMPEONADO BRASILEIRO: ##");
        int colocacao = 1;
        for (PosicaoTabela posicao : posicoes) {
            System.out.println(colocacao +". " + posicao);
            colocacao++;
        }

        System.out.println();
        System.out.println();
    }
}
