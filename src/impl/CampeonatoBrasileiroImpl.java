package impl;

import dominio.*;


import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CampeonatoBrasileiroImpl {

    private List<Jogo> brasileirao;
    private List<Jogo> jogos;
    private Predicate<Jogo> filtro;

    public CampeonatoBrasileiroImpl(Predicate<Jogo> filtro) throws IOException {
        this.jogos = lerArquivo();
        this.filtro = filtro;
        this.brasileirao = jogos.stream()
                .filter(filtro) //filtrar por ano
                .toList();

    }

    public List<Jogo> lerArquivo() throws IOException {

        List<Jogo> listaDeJogos = new ArrayList<>();
        String arquivoCSV = "C:\\Users\\Neto\\Desktop\\Estudos\\projeto-final-santander-itc1\\projeto-final-santander\\campeonato-brasileiro.csv";
        String csvDivisor = ";";
        try ( BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {

            String linha = br.readLine();
            linha = br.readLine();
            while (linha  != null) {

                String[] jogos = linha.split(csvDivisor);
                Jogo jogo = criarJogo(jogos);
                listaDeJogos.add(jogo);

                linha = br.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return listaDeJogos;
    }

    public Jogo criarJogo(String[] jogo) throws ParseException {
        Integer rodada = Integer.parseInt(jogo[0]);
        String data = jogo[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataDoJogo = LocalDate.parse(data, formatter);
        String horariodoJogoSemFormatar = jogo[2];

        LocalTime horarioJogo = null;
        if(!"".equals(horariodoJogoSemFormatar) && horariodoJogoSemFormatar != null ){
            String horarioFormatado = horariodoJogoSemFormatar.replaceAll("h",":");
            horarioJogo = LocalTime.parse(horarioFormatado);
        }
        DayOfWeek diaDoJogo = dataDoJogo.getDayOfWeek();
        DataDoJogo dataEHorario = new DataDoJogo(dataDoJogo,horarioJogo,diaDoJogo);


        Time mandante = new Time(jogo[4]);
        Time visitante = new Time(jogo[5]);
        Time vencedor = new Time(jogo[6]);
        String arena = jogo[7];
        Integer mandantePlacar = Integer.valueOf(jogo[8]);
        Integer visitantePlacar = Integer.valueOf(jogo[9]);
        String estadoMandante = jogo[10];
        String estadoVisitante = jogo[11];
        String estadoVencedor = jogo[12];
        return new Jogo(rodada,dataEHorario,mandante,visitante,vencedor,arena,
                mandantePlacar,visitantePlacar,estadoMandante,estadoVisitante,estadoVencedor);


    }


    public IntSummaryStatistics getEstatisticasPorJogo() {
        IntStream estatisticasBrasileirao = todosOsJogos().stream().mapToInt(jogo -> jogo.mandantePlacar()+jogo.visitantePlacar());
        return estatisticasBrasileirao.summaryStatistics();
    }

    public double getMediaGolsPorJogo() {
        double totalGols = 0L;
        var jogosDoAno = brasileirao;
        for (Jogo jogo: jogosDoAno) {
            double golsDoJogo = jogo.mandantePlacar()+jogo.visitantePlacar();
            totalGols = golsDoJogo + totalGols;
        }
        return (totalGols/jogosDoAno.size());
    }

    public IntSummaryStatistics GetEstatisticasPorJogo() {
        return null;
    }

    public List<Jogo> todosOsJogos() {
        return brasileirao;
    }

    public Long getTotalVitoriasEmCasa(){
        return todosOsJogos().stream().filter(jogo-> jogo.mandante().equals(jogo.vencedor())).count();
    }

    public Long getTotalVitoriasForaDeCasa() {
        return todosOsJogos().stream().filter(jogo -> jogo.visitante().equals(jogo.vencedor())).count();
    }

    public Long getTotalEmpates() {
        return todosOsJogos().stream().filter(jogo -> jogo.vencedor().toString().equals("-")).count();
    }

    public Long getTotalJogosComMenosDe3Gols() {
        return todosOsJogos().stream().filter(
                jogo -> jogo.mandantePlacar()+jogo.visitantePlacar() < Constantes.TOTAL_GOLS_IGUAL_A_TRES).count();
    }

    public Long getTotalJogosCom3OuMaisGols() {
        return todosOsJogos().stream().filter(jogo -> jogo.mandantePlacar()+jogo.visitantePlacar() >= Constantes.TOTAL_GOLS_IGUAL_A_TRES).count();
    }

    public List<String> getTodosOsPlacares() {
        return todosOsJogos().stream().map(jogo -> jogo.mandantePlacar()+"-"+jogo.visitantePlacar()).toList();
    }

    public Map.Entry<String,Long> getPlacarMaisRepetido() {
        List<String> placares = getTodosOsPlacares();
        Optional<Map.Entry<String,Long>> optionPlacarMaisRepetido = placares
                .stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet().stream().max(Map.Entry.comparingByValue());
        Map.Entry<String,Long> placarMaisRepetido = optionPlacarMaisRepetido.get();
        return placarMaisRepetido;

    }

    public Map.Entry<String, Long> getPlacarMenosRepetido() {
        List<String> placares = getTodosOsPlacares();
        Optional<Map.Entry<String,Long>> optionPlacarMaisRepetido = placares.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().min(Map.Entry.comparingByValue());
        Map.Entry<String,Long> placarMenosRepetido = optionPlacarMaisRepetido.get();


        return placarMenosRepetido;
    }

    public List<Time> getTodosOsTimes() {
       return todosOsJogos().stream().map(Jogo::mandante).distinct().toList();
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() {
        Map<Time, List<Jogo>> todosOsJogosPorTimeComoMandantes = new HashMap<>();
        for (int i = 0; i < getTodosOsTimes().size(); i++) {
            Time time = getTodosOsTimes().get(i);
            List<Jogo> jogosTimeMandante = todosOsJogos().stream().filter(jogo -> jogo.mandante().equals(time)).toList();
            todosOsJogosPorTimeComoMandantes.put(time,jogosTimeMandante);

        }
        return todosOsJogosPorTimeComoMandantes;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() {
        Map<Time, List<Jogo>> todosOsJogosPorTimeComoVisitante = new HashMap<>();
        for (int i = 0; i < getTodosOsTimes().size(); i++) {
            Time time = getTodosOsTimes().get(i);
            List<Jogo> jogosTimeVisitante = todosOsJogos().stream().filter(jogo -> jogo.visitante().equals(time)).toList();
            todosOsJogosPorTimeComoVisitante.put(time,jogosTimeVisitante);

        }
        return todosOsJogosPorTimeComoVisitante;
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTime() {
        Map<Time, List<Jogo>> todosOsJogosPorTime = new HashMap<>();
        for (int i = 0; i < getTodosOsTimes().size(); i++) {
            Time time = getTodosOsTimes().get(i);
            List<Jogo> jogosTime = todosOsJogos().stream().filter(jogo -> jogo.visitante().equals(time) || jogo.mandante().equals(time)).toList();
            todosOsJogosPorTime.put(time,jogosTime);

        }
        return todosOsJogosPorTime;
    }

    public Map<Time, Long> golsFeitosPorTime(){
        Map<Time,Long>golsFeitosPorTime = new HashMap<>();
        for (Map.Entry<Time,List<Jogo>> mapa : getTodosOsJogosPorTime().entrySet() ) {
            IntSummaryStatistics estatisticasTimeMandante = mapa.getValue().stream().filter(jogo -> jogo.mandante().equals(mapa.getKey())).mapToInt(Jogo::mandantePlacar).summaryStatistics();
            IntSummaryStatistics estatisticasTimeVisitante = mapa.getValue().stream().filter(jogo -> jogo.visitante().equals(mapa.getKey())).mapToInt(Jogo::visitantePlacar).summaryStatistics();
            Long somaGols = estatisticasTimeMandante.getSum() + estatisticasTimeVisitante.getSum();
            golsFeitosPorTime.put(mapa.getKey(),somaGols);

        }

        return golsFeitosPorTime;

    }

    public Map<Time, Long> golsSofridosPorTime(){
        Map<Time,Long>golsSofridosPorTime = new HashMap<>();
        for (Map.Entry<Time,List<Jogo>> mapa : getTodosOsJogosPorTime().entrySet() ) {
            IntSummaryStatistics estatisticasTimeMandante = mapa.getValue().stream().filter(jogo -> jogo.mandante().equals(mapa.getKey())).mapToInt(Jogo::visitantePlacar).summaryStatistics();
            IntSummaryStatistics estatisticasTimeVisitante = mapa.getValue().stream().filter(jogo -> jogo.visitante().equals(mapa.getKey())).mapToInt(Jogo::mandantePlacar).summaryStatistics();
            Long somaGols = estatisticasTimeMandante.getSum() + estatisticasTimeVisitante.getSum();
            golsSofridosPorTime.put(mapa.getKey(),somaGols);

        }

        return golsSofridosPorTime;

    }

    public Map.Entry<Time,Long> timeComMaisGolsNoCampeonato(){

       return  golsFeitosPorTime().entrySet().stream().max((Map.Entry.comparingByValue())).get();
    }

    public Map.Entry<Time, Long> timeComMenosGolsNoCampeonato(){
        return golsFeitosPorTime().entrySet().stream().min(Map.Entry.comparingByValue()).get();
    }

    public List<PosicaoTabela> posicaoTime(){
        List<PosicaoTabela> tabelaTimes = new ArrayList<>();
        for (Map.Entry<Time,List<Jogo>> mapa : getTodosOsJogosPorTime().entrySet()){
            Time time = mapa.getKey(); // OK
            long vitoriasTime = mapa.getValue().stream()
                    .filter(jogo -> time.equals(jogo.vencedor())).count(); // OK
            long empatesTime = mapa.getValue().stream().
                    filter(jogo -> jogo.vencedor().toString().equals("-")).count(); // OK
            long derrotasTime =  mapa.getValue().size() - vitoriasTime - empatesTime;
            long golsPositivos = golsFeitosPorTime().get(time); // OK
            long golsSofridos = golsSofridosPorTime().get(time); // OK
            long saldoDeGols = golsPositivos - golsSofridos; // OK
            long totalDeJogos = mapa.getValue().size(); // OK
            long pontuacaoTime = (3*vitoriasTime + empatesTime);
            PosicaoTabela posicaoTabela = new PosicaoTabela(time, pontuacaoTime, vitoriasTime,derrotasTime,empatesTime,golsPositivos,golsSofridos,saldoDeGols,totalDeJogos);
            tabelaTimes.add(posicaoTabela);

        }
        return tabelaTimes;

    }
    public List<PosicaoTabela> getTabela() {
        return posicaoTime().stream().sorted().toList();
    }



}