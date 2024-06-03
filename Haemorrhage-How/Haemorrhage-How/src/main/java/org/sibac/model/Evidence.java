package org.sibac.model;

public class Evidence<T> extends Fact{
    public static final String CISTOSCOPIA_FACT = "A cistoscopia acusa algo";
    public static final String CISTOSCOPIA_PSA = "O nível de PSA é alto (>10)";
    public static final String CISTOSCOPIA_TOMOGRAFIA = "Foi detetado alguma anomalia na tomografia";
    public static final String CISTOSCOPIA_BIOPSIA = "Foi detatado alguma anomalia na biopsia";
    public static final String EXPECTATIVA_UTENTE = "Se o tratamento for realizado a expectativa do utente aumenta em 10 anos";
    public static final String UROTAC_FACT = "O urotac acusa algo";
    public static final String UROTAC_RESULT = "Qual o resultado do exame (Pedras|HBP|CancroVias|CancroProstata)";
    public static final String UROTAC_PEDRAS = "Qual é o tamanho das pedras (mm)";
    public static final String UROTAC_HBP_ULTRASSONOGRAFIA = "Qual é volume da prostata (ml)";
    public static final String IDADE_PACIENTE = "Qual é a idade do paciente";
    public static final String CONDICAO_PACIENTE = "Qual é a condição do paciente";
    public static final String CANCRO_RISCO = "Como quer avaliar o risco";
    public static final String CANCRO_PSA = "Qual é o valor do PSA";
    public static final String CANCRO_GG = "Qual é o valor do GG";
    public static final String CANCRO_CT2 = "Qual é o tipo de CT2 (b|c|d)";

    public static final String ECOGRAFIA_BASIC = "Acusa alguma coisa? (sim/nao)";

    public static final String ECOGRAFIA_BASIC_SIM = "O que acusa? (cancro/hbp)";

    private String evidence;
    private String value;

    public Evidence(String ev, String v) {
        evidence = ev;
        value = v;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return (evidence + " = " + value);
    }

}

