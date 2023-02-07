package impl;

import dominio.*;


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
//        IntStream estatisticasBrasileirao = jogos.stream().filter(filtro).map()
        return null;
    }

    public Map<Jogo, Integer> getMediaGolsPorJogo() {
        return null;
    }

    public IntSummaryStatistics GetEstatisticasPorJogo() {
        return null;
    }

    public List<Jogo> todosOsJogos() {
        return jogos;
    }

    public Long getTotalVitoriasEmCasa(){
        Long vitorias = 0L;

        List<Jogo> jogosDoAno = brasileirao;
        for (Jogo jogo: jogosDoAno) {
            if (jogo.mandante().equals(jogo.vencedor())){
                vitorias++;
            }
        }
        return vitorias;
    }

    public Long getTotalVitoriasForaDeCasa() {
        Long vitorias = 0L;

        List<Jogo> jogosDoAno = brasileirao;
        for (Jogo jogo: jogosDoAno) {
            if (jogo.visitante().equals(jogo.vencedor())){
                vitorias++;
            }
        }
        return vitorias;
    }

    public Long getTotalEmpates() {
        Long empates = 0L;

        List<Jogo> jogosDoAno = brasileirao;
        for (Jogo jogo: jogosDoAno) {
            if (jogo.vencedor().toString().equals("-")){
                empates++;
            }
        }
        return empates;
    }

    public Long getTotalJogosComMenosDe3Gols() {
        int totalGolsMenorQue3 = 3;
        var jogosComGolsMenosQue3 = brasileirao.stream().filter(jogo -> jogo.mandantePlacar()+jogo.visitantePlacar() < totalGolsMenorQue3);
        return jogosComGolsMenosQue3.count();
    }

    public Long getTotalJogosCom3OuMaisGols() {
        int totalGolsTresOuMais = 3;
        var jogosComGolsTresOuMais = brasileirao.stream().filter(jogo -> jogo.mandantePlacar()+jogo.visitantePlacar() >= totalGolsTresOuMais);
        return jogosComGolsTresOuMais.count();
    }

    public List<String> getTodosOsPlacares() {
        List<String> placares = new ArrayList<>();
        List<Jogo> jogosDoAno = brasileirao;
        String placar = "";
        for (Jogo jogo: jogosDoAno) {
//            if (jogo.mandantePlacar()<jogo.visitantePlacar()){
//                placar = jogo.visitantePlacar()+"-"+jogo.mandantePlacar();
//            }else{
                placar = jogo.mandantePlacar()+"-"+jogo.visitantePlacar();
            //}
            placares.add(placar);
        }
        return placares;
    }

    public Map.Entry<String,Long> getPlacarMaisRepetido() {
        List<String> placares = getTodosOsPlacares();
        Optional<Map.Entry<String,Long>> optionPlacarMaisRepetido = placares.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue());
        Map.Entry<String,Long> placarMaisRepetido = optionPlacarMaisRepetido.get();


        return placarMaisRepetido;

    }

    public Map.Entry<String, Long> getPlacarMenosRepetido() {
        List<String> placares = getTodosOsPlacares();
        Optional<Map.Entry<String,Long>> optionPlacarMaisRepetido = placares.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().min(Map.Entry.comparingByValue());
        Map.Entry<String,Long> placarMenosRepetido = optionPlacarMaisRepetido.get();


        return placarMenosRepetido;
    }

    private List<Time> getTodosOsTimes() {
        return null;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() {
        return null;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() {
        return null;
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTime() {
        return null;
    }

    public Map<Time, Map<Boolean, List<Jogo>>> getJogosParticionadosPorMandanteTrueVisitanteFalse() {
        return null;
    }

    public Set<PosicaoTabela> getTabela() {
        return null;
    }

    private DayOfWeek getDayOfWeek(String dia) {
        return null;
    }

    private Map<Integer, Integer> getTotalGolsPorRodada() {
        return null;
    }

    private Map<Time, Integer> getTotalDeGolsPorTime() {
        return null;
    }

    private Map<Integer, Double> getMediaDeGolsPorRodada() {
        return null;
    }


}