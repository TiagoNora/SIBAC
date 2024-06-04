package org.sibac.model;

import org.sibac.haemorrhage.Haemorrhage;

public class Conclusion extends Fact{
    public static final String BEBER_AGUA = "Deve beber água";
    public static final String TOMAR_MEDICAMENTO = "Deve tomar medicamento";
    public static final String REALIZAR_CIRURGIA = "Deve realizar cirurgia";
    public static final String NAO_HA_TRATAMENTO = "Não hã lugar a tratamento";
    public static final String TRATAMENTO_HBP = "Terapia com farmacos|Redução do volume prostatico através de cirurgia|Redução do volume prostatico por vapor de água|Aquabalação";
    public static final String CANCRO_PROSTATA = "Detetado cancro da próstata";
    public static final String PEDRAS_RINS = "Detetadas pedras nos rins";
    public static final String HBP_ANORMAL = "Presença de HBP anormal";
    public static final String HBP_NORMAL = "Presença de HBP normal";

    public static final String CANCRO_VIAS = "Presença de cancro nas vias urinárias";

    public static final String VIGILANCIA_ATIVA = "Paciente será posto em vigilancia";

    public static final String UNKNOWN   = "Não sabemos";
    private String description;

    public Conclusion(String description) {
        this.description = description;
        Haemorrhage.agendaEventListener.addRhs(this);
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return ("Conclusion: " + description);
    }

}
