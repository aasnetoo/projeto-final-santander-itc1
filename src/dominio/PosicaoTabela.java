package dominio;

public record PosicaoTabela(Time time,
                            Long vitorias,
                            Long derrotas,
                            Long empates,
                            Long golsPositivos,
                            Long golsSofridos,
                            Long saldoDeGols,
                            Long jogos) {

    @Override
    public String toString() {
        return  time +
                ", pontos=" + getPontuacao() + // desenvolver forma de obter a pontuação
                ", vitorias=" + vitorias +
                ", derrotas=" + derrotas +
                ", empates=" + empates +
                ", golsPositivos=" + golsPositivos +
                ", golsSofridos=" + golsSofridos +
                ", saldoDeGols=" + saldoDeGols +
                ", jogos=" + jogos +
                '}';
    }

    public Long getPontuacao(){

        return ((3*vitorias) + empates);
    }

}
