package br.com.cardapiododia.service;

import br.com.cardapiododia.model.entity.*;
import br.com.cardapiododia.repository.ObservacaoRepository;
import br.com.cardapiododia.repository.PratoRepository;
import br.com.cardapiododia.repository.TipoPratoRepository;
import br.com.cardapiododia.repository.TipoRefeicaoRepository;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static br.com.cardapiododia.utils.CardapioUtils.hashString;

@Service
public class CardapioScraperService {
    @Autowired
    private WebDriver driver;

    @Autowired
    private PratoRepository pratoRepository;

    @Autowired
    private ObservacaoRepository observacaoRepository;

    @Autowired
    private TipoPratoRepository tipoPratoRepository;

    @Autowired
    private TipoRefeicaoRepository tipoRefeicaoRepository;

    public List<Cardapio> findCardapios() {
        List<Cardapio> cardapios = new ArrayList<>();

        driver.get("https://www.prefeitura.unicamp.br/apps/site/cardapio.php");
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1280,720));

        manageDatasCardapios(cardapios);

        return cardapios;
    }

    private void manageDatasCardapios(List<Cardapio> cardapios) {
        Integer datasCardapio = driver.findElement(By.className("dias_semana"))
                .findElement(By.tagName("tbody"))
                .findElements(By.tagName("a")).size();

        for (int i = 0; i < datasCardapio; i++) {
            driver.findElement(By.className("dias_semana"))
                    .findElement(By.tagName("tbody"))
                    .findElements(By.tagName("a")).get(i).click();

            WebElement dataCardapio = driver.findElement(By.className("dias_semana"))
                    .findElement(By.tagName("tbody"))
                    .findElements(By.tagName("a")).get(i);

            manageTiposCardapios(LocalDate.parse(dataCardapio.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    cardapios);
        }
    }

    private void manageTiposCardapios(LocalDate dataCardapio, List<Cardapio> cardapios) {
        List<WebElement> tiposEl = driver.findElements(By.className("titulo_cardapio"));
        List<WebElement> cardapiosEl = driver.findElements(By.className("fundo_cardapio"));

        for (int i = 0; i < tiposEl.size(); i++) {
            TipoRefeicao tipoRefeicao = verifyRefeicao(tiposEl.get(i).getText());
            WebElement cardapioEl = cardapiosEl.get(i);
            Cardapio cardapio = new Cardapio();
            cardapio.setData(dataCardapio);
            cardapio.setTipo(tipoRefeicao.getTipo());

            PratoCardapio pratoCardapio = managePratoCardapio(tipoRefeicao, cardapioEl);
            cardapio.setCardapio(pratoCardapio);

            cardapios.add(cardapio);
        }
    }

    private PratoCardapio managePratoCardapio(TipoRefeicao tipoRefeicao, WebElement cardapioEl) {
        PratoCardapio pratoCardapio = new PratoCardapio();

        if("Café da manhã".equals(tipoRefeicao.getRefeicao())){
            manageCafeDaManha(cardapioEl, pratoCardapio);
        } else {
            List<String> tipos = cardapioEl.findElements(By.tagName("strong")).stream()
                    .map(prato -> {
                        String pratoStr = prato.getText();
                        pratoStr = Character.toTitleCase(pratoStr.charAt(0)) +
                                pratoStr.substring(1).toLowerCase().replace(":","");

                        return pratoStr;
                    })
                    .collect(Collectors.toList());
            tipos.add(0, "Prato Geral");

            List<TipoPrato> tiposPrato = tipos.stream().limit(tipos.size()-1)
                    .map(tipo -> verifyTipoPrato(tipo))
                    .collect(Collectors.toList());

            AtomicInteger i = new AtomicInteger(0);
            Arrays.stream(cardapioEl.getText().split("\\n"))
                .filter(prato -> !prato.trim().isEmpty())
                .forEach(prato -> {
                    String tipoSearch = (tipos.size() > i.get() + 1) ? tipos.get(i.get()+1) : "End";
                    if (prato.toUpperCase().contains(tipoSearch.toUpperCase())) {
                        i.incrementAndGet();

                        prato = prato.replace(tipoSearch.toUpperCase(), "")
                                .replace(tipoSearch, "")
                                .replace(":","");
                    }

                    if(!prato.trim().isEmpty()) {
                        if (!"End".equals(tipoSearch)) {
                            prato = Character.toTitleCase(prato.charAt(0)) + prato.substring(1).toLowerCase();
                            TipoPrato tipoPrato = tiposPrato.stream()
                                    .filter(tipo -> tipo.getPrato().equals(tipos.get(i.get())))
                                    .findFirst().orElse(null);
                            Prato pratoObj = verifyPrato(prato, tipoPrato.getTipo());
                            assignPrato(tipos.get(i.get()), pratoObj, pratoCardapio);
                        } else {
                            Observacao observacao = verifyObservacao(prato);

                            if (pratoCardapio.getObservacoes() == null) {
                                pratoCardapio.setObservacoes(new ArrayList<>());
                            }

                            pratoCardapio.getObservacoes().add(observacao.getHash());
                        }
                    }
                });
        }

        return pratoCardapio;
    }

    private void assignPrato(String tipo, Prato pratoObj, PratoCardapio pratoCardapio) {
        if ("Prato Geral".equals(tipo)) {
            if (pratoCardapio.getPrato() == null) {
                pratoCardapio.setPrato(new ArrayList<>());
            }

            pratoCardapio.getPrato().add(pratoObj.getPrato());
        } else if ("Prato principal".equals(tipo)) {
            if (pratoCardapio.getPratoPrincipal() == null) {
                pratoCardapio.setPratoPrincipal(new ArrayList<>());
            }

            pratoCardapio.getPratoPrincipal().add(pratoObj.getPrato());
        } else if ("Salada".equals(tipo)) {
            if (pratoCardapio.getSalada() == null) {
                pratoCardapio.setSalada(new ArrayList<>());
            }

            pratoCardapio.getSalada().add(pratoObj.getPrato());
        } else if ("Sobremesa".equals(tipo)) {
            if (pratoCardapio.getSobremesa() == null) {
                pratoCardapio.setSobremesa(new ArrayList<>());
            }

            pratoCardapio.getSobremesa().add(pratoObj.getPrato());
        } else if ("Suco".equals(tipo)) {
            if (pratoCardapio.getSuco() == null) {
                pratoCardapio.setSuco(new ArrayList<>());
            }

            pratoCardapio.getSuco().add(pratoObj.getPrato());
        }
    }

    private void manageCafeDaManha(WebElement cardapioEl, PratoCardapio pratoCardapio) {
        TipoPrato tipoPrato = verifyTipoPrato("Prato Geral");
        pratoCardapio.setPrato(new ArrayList<>());
        Arrays.stream(cardapioEl.getText().split(", ")).forEach(prato -> {
            String pratoCap = Character.toTitleCase(prato.charAt(0)) + prato.substring(1).toLowerCase();
            Prato pratoObj = verifyPrato(pratoCap, tipoPrato.getTipo());
            pratoCardapio.getPrato().add(pratoObj.getPrato());
        });
    }

    @Transactional
    private TipoRefeicao verifyRefeicao(String refeicao) {
        String hashRefeicao = hashString(refeicao);

        TipoRefeicao tipoRefeicao = tipoRefeicaoRepository.findByTipo(hashRefeicao);

        if(tipoRefeicao == null) {
            tipoRefeicao = new TipoRefeicao();
            tipoRefeicao.setTipo(hashRefeicao);
            tipoRefeicao.setRefeicao(refeicao);

            tipoRefeicaoRepository.insert(tipoRefeicao);
        }

        return tipoRefeicao;
    }

    @Transactional
    private TipoPrato verifyTipoPrato(String prato) {
        String hashPrato = hashString(prato);

        TipoPrato tipoPrato = tipoPratoRepository.findByTipo(hashPrato);

        if(tipoPrato == null) {
            tipoPrato = new TipoPrato();
            tipoPrato.setTipo(hashPrato);
            tipoPrato.setPrato(prato);

            tipoPratoRepository.insert(tipoPrato);
        }

        return tipoPrato;
    }

    @Transactional
    private Prato verifyPrato(String prato, String hashTipo) {
        String hashPrato = hashString(prato);

        Prato pratoObj = pratoRepository.findByTipoAndPrato(hashTipo, hashPrato);

        if(pratoObj == null) {
            pratoObj = new Prato();
            pratoObj.setTipo(hashTipo);
            pratoObj.setPrato(hashPrato);
            pratoObj.setNome(prato);
            pratoObj.setMediaNotas(BigDecimal.ZERO);

            pratoRepository.insert(pratoObj);
        }

        return pratoObj;
    }

    @Transactional
    private Observacao verifyObservacao(String observacao) {
        String hashObservacao = hashString(observacao);

        Observacao tipoObservacao = observacaoRepository.findByHash(hashObservacao);

        if(tipoObservacao == null) {
            tipoObservacao = new Observacao();
            tipoObservacao.setHash(hashObservacao);
            tipoObservacao.setObservacao(observacao);

            observacaoRepository.insert(tipoObservacao);
        }

        return tipoObservacao;
    }
}
