package org.sibac.haemorrhage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.LiveQuery;
import org.kie.api.runtime.rule.Row;
import org.kie.api.runtime.rule.ViewChangedEventListener;

import org.sibac.model.Conclusion;
import org.sibac.model.Justification;
import org.sibac.model.Information;
import org.sibac.model.Hypothesis;
import org.sibac.view.UI;
import org.sibac.model.UserInput;

public class Haemorrhage {
    public static KieSession KS;
    public static BufferedReader BR;
    public static TrackingAgendaEventListener agendaEventListener;
    public static Map<Integer, Justification> justifications;

    public static final void main(String[] args) {
        UI.uiInit();
        runEngine();
        UI.uiClose();
    }

    private static void runEngine() {
        Information info = new Information();
        try {
            Haemorrhage.justifications = new TreeMap<Integer, Justification>();

            // load up the knowledge base
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            final KieSession kSession = kContainer.newKieSession("ksession-rules");
            Haemorrhage.KS = kSession;
            Haemorrhage.agendaEventListener = new TrackingAgendaEventListener();
            kSession.addEventListener(agendaEventListener);

            kSession.setGlobal("info", info);

            // Read the file and store patient data
            List<Patient> patients = readPatientsFromFile("pacientes.txt");

            // Ask the user for the patient number
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter patient number:");
            int patientNumber = scanner.nextInt();

            // Retrieve and use the patient information
            if (patientNumber > 0 && patientNumber <= patients.size()) {
                Patient patient = patients.get(patientNumber - 1);

                // Create UserInput object and set the inputs
                UserInput input = new UserInput(String.valueOf(patient.age), String.valueOf(patient.physicalCondition));

                // Insert facts into session
                kSession.insert(input);
                kSession.insert(new Hypothesis("Analisar paciente", "idade"));

                // Query listener
                ViewChangedEventListener listener = new ViewChangedEventListener() {
                    @Override
                    public void rowDeleted(Row row) {}

                    @Override
                    public void rowInserted(Row row) {
                        Conclusion conclusion = (Conclusion) row.get("$conclusion");
                        System.out.println(">>>" + conclusion.toString());

                        How how = new How(Haemorrhage.justifications);
                        System.out.println(how.getHowExplanation(conclusion.getId()));

                        // stop inference engine as soon as got a conclusion
                        kSession.halt();
                    }

                    @Override
                    public void rowUpdated(Row row) {}
                };

                LiveQuery query = kSession.openLiveQuery("Conclusions", null, listener);

                kSession.fireAllRules();

                query.close();
            } else {
                System.out.println("Invalid patient number.");
            }

            scanner.close();

            // Get the processed information
            ArrayList<String> cistoFacts = info.getCisto_facts();
            ArrayList<String> uroFacts = info.getUro_facts();
            ArrayList<String> ecoFacts = info.getEco_facts();

            Patient patient = patients.get(patientNumber - 1);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("frontend/information/factos.txt"))) {
                // Write patient information
                writer.write("Patient Information:\n");
                writer.write("Name: " + patient.name + "\n");
                writer.write("Age: " + patient.age + "\n");
                writer.write("Physical Condition: " + patient.physicalCondition + "\n");
                writer.write("\n");

                // Write facts
                writer.write("Cistocospia Facts:\n");
                for (String fact : cistoFacts) {
                    writer.write(fact);
                    writer.newLine();
                }

                writer.write("\nUroTac Facts:\n");
                for (String fact : uroFacts) {
                    writer.write(fact);
                    writer.newLine();
                }

                writer.write("\nEcoGrafia Facts:\n");
                for (String fact : ecoFacts) {
                    writer.write(fact);
                    writer.newLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Process or display the information as needed
            System.out.println("Factos da cistoscopia:");
            System.out.println(patient.physicalCondition);
            for (String fact : cistoFacts) {
                System.out.println(fact);
            }

            System.out.println("--------------------------");

            System.out.println("Factos do urotac:");
            for (String fact : uroFacts) {
                System.out.println(fact);
            }

            System.out.println("--------------------------");

            System.out.println("Factos da ecografia:");
            for (String fact : ecoFacts) {
                System.out.println(fact);
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static List<Patient> readPatientsFromFile(String fileName) {
        List<Patient> patients = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String name = null;
            int age = 0;
            int physicalCondition = 0;

            Pattern pattern = Pattern.compile("Paciente (\\d+):");
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    if (name != null) {
                        patients.add(new Patient(name, age, physicalCondition));
                    }
                    // Reset variables for the new patient
                    name = null;
                    age = 0;
                    physicalCondition = 0;
                } else if (line.startsWith("Nome->")) {
                    name = line.substring(6);
                } else if (line.startsWith("Idade->")) {
                    String ageStr = line.substring(7).trim();
                    if (!ageStr.isEmpty()) {
                        age = Integer.parseInt(ageStr);
                    }
                } else if (line.startsWith("CondicaoFisica->")) {
                    //System.out.println(line);
                    
                    String conditionStr = line.substring(16).trim();
                    if (!conditionStr.isEmpty()) {
                        physicalCondition = Integer.parseInt(conditionStr);
                    }
                }
            }
            // Add the last patient if present
            if (name != null) {
                patients.add(new Patient(name, age, physicalCondition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return patients;
    }
}

class Patient {
    String name;

    int age;
    int physicalCondition;

    public Patient(String name, int age, int physicalCondition) {
        this.name = name;

        this.age = age;
        this.physicalCondition = physicalCondition;
    }
}
