package org.sibac.model;

import org.sibac.haemorrhage.Haemorrhage;

public class Conclusion extends Fact{
    public static final String BEBER_AGUA = "Deve beber água";
    public static final String TOMAR_MEDICAMENTO = "Deve tomar medicamento";
    public static final String REALIZAR_CIRURGIA = "Deve realizar cirurgia";
    public static final String NAO_HA_TRATAMENTO = "Não hã lugar a tratamento";
    public static final String TRATAMENTO_HBP = "Terapia com farmacos|Redução do volume prostatico através de cirurgia|Redução do volume prostatico por vapor de água|Aquabalação";
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
