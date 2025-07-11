package org.example;

public enum StatusTarefa {
    // Para cada status, passamos o texto que queremos exibir no construtor.
    NAO_INICIADA("Não Iniciada"),
    EM_PROCESSAMENTO("Em Processamento"),
    CONCLUIDA("Concluída");

    private final String nomeExibicao;

    StatusTarefa(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    @Override
    public String toString() {
        return this.nomeExibicao;
    }
}
