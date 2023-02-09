package dominio;

public record PosicaoTabela(Time time,
                            Long pontuacao,
                            Long vitorias,
                            Long derrotas,
                            Long empates,
                            Long golsPositivos,
                            Long golsSofridos,
                            Long saldoDeGols,
                            Long jogos) implements Comparable<PosicaoTabela> {

    @Override
    public String toString() {
        return  time +
                ", pontos=" + pontuacao +
                ", vitorias=" + vitorias +
                ", derrotas=" + derrotas +
                ", empates=" + empates +
                ", golsPositivos=" + golsPositivos +
                ", golsSofridos=" + golsSofridos +
                ", saldoDeGols=" + saldoDeGols +
                ", jogos=" + jogos +
                '}';
    }


    @Override
    public int compareTo(PosicaoTabela posicao) {
        if (posicao.pontuacao() != pontuacao()){
            return Long.compare(posicao.pontuacao(),pontuacao());
        }
        else if (posicao.vitorias() != vitorias()){
            return Long.compare(posicao.vitorias(),vitorias());
            }
        return Long.compare(posicao.saldoDeGols(),saldoDeGols());
    }
}
