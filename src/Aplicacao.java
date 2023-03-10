import dominio.PosicaoTabela;
import impl.CampeonatoBrasileiroImpl;
import java.util.*;

public class Aplicacao {

    public static void main(String[] args){

        Scanner scan = new Scanner(System.in);
        System.out.println("Bem vindo ao histórico dos jogos do Campeonato Brasileiro de Futebol!");
        try{
            System.out.println("Digite o ano que você deseja obter os dados entre 2003 e 2020: ");
            long anoCampeonato = scan.nextLong();
            scan.nextLine();

            CampeonatoBrasileiroImpl resultados =
                    new CampeonatoBrasileiroImpl( (jogo) -> jogo.data().data().getYear() == anoCampeonato);

            imprimirEstatisticas(resultados);
            imprimirTabela(resultados.getTabela());

        }catch(Exception e){
            System.err.println("Dado inválido! Inicie de novo o programa e digite um ano entre 2003 e 2020");
        }


    }

    private static void imprimirEstatisticas(CampeonatoBrasileiroImpl brasileirao) {
        IntSummaryStatistics statistics = brasileirao.getEstatisticasPorJogo();

        System.out.println("Estatisticas (Total de gols) - " + statistics.getSum());
        System.out.println("Estatisticas (Total de jogos) - " + statistics.getCount());
        System.out.printf("Estatisticas (Media de gols) -  %.2f %n", statistics.getAverage());
        System.out.println("Estatisticas (Melhor Ataque) - "
                + brasileirao.timeComMaisGolsNoCampeonato().getKey() + " - " + brasileirao.timeComMaisGolsNoCampeonato().getValue()+ " gols");
        System.out.println("Estatisticas (Pior Ataque) - "
                + brasileirao.timeComMenosGolsNoCampeonato().getKey() + " - " + brasileirao.timeComMenosGolsNoCampeonato().getValue()+ " gols");

        Map.Entry<String,Long> placarMaisRepetido = brasileirao.getPlacarMaisRepetido();

        System.out.println("Estatisticas (Placar mais repetido) - "
                + placarMaisRepetido.getKey() + " (" +placarMaisRepetido.getValue() + " jogo(s))");

        Map.Entry<String, Long> placarMenosRepetido = brasileirao.getPlacarMenosRepetido();

        System.out.println("Estatisticas (Placar menos repetido) - "
                + placarMenosRepetido.getKey() + " (" +placarMenosRepetido.getValue() + " jogo(s))");

        Long jogosCom3OuMaisGols = brasileirao.getTotalJogosCom3OuMaisGols();
        Long jogosComMenosDe3Gols = brasileirao.getTotalJogosComMenosDe3Gols();

        System.out.println("Estatisticas (3 ou mais gols) - " + jogosCom3OuMaisGols);
        System.out.println("Estatisticas (-3 gols) - " + jogosComMenosDe3Gols);

        Long totalVitoriasEmCasa = brasileirao.getTotalVitoriasEmCasa();
        Long vitoriasForaDeCasa = brasileirao.getTotalVitoriasForaDeCasa();
        Long empates = brasileirao.getTotalEmpates();

        System.out.println("Estatisticas (Vitorias Fora de casa) - " + vitoriasForaDeCasa);
        System.out.println("Estatisticas (Vitorias Em casa) - " + totalVitoriasEmCasa);
        System.out.println("Estatisticas (Empates) - " + empates);
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