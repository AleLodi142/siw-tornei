package siw_tornei.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import siw_tornei.model.Classifica;
import siw_tornei.model.Partita;
import siw_tornei.model.Squadra;

@Service
@Transactional(readOnly = true)
public class ClassificaService {

    private PartitaService partitaService;

    public ClassificaService(PartitaService partitaService) {
        this.partitaService = partitaService;
    }

    public List<Classifica> calcolaClassifica(Long torneoId) {

        List<Partita> partite = partitaService.findByTorneoIdOrderByDataOra(torneoId);

        Map<Long, Classifica> classificaMap = new HashMap<>();

        for (Partita partita : partite) {

            if (!"PLAYED".equals(partita.getStato())) {
                continue;
            }

            Squadra casa = partita.getSquadraCasa();
            Squadra trasferta = partita.getSquadraTrasferta();

            Classifica rigaCasa = classificaMap.get(casa.getId());
            if (rigaCasa == null) {
                rigaCasa = new Classifica();
                rigaCasa.setSquadra(casa);
                classificaMap.put(casa.getId(), rigaCasa);
            }

            Classifica rigaTrasferta = classificaMap.get(trasferta.getId());
            if (rigaTrasferta == null) {
                rigaTrasferta = new Classifica();
                rigaTrasferta.setSquadra(trasferta);
                classificaMap.put(trasferta.getId(), rigaTrasferta);
            }

            int goalsHome = partita.getGoalsHome();
            int goalsAway = partita.getGoalsAway();

            rigaCasa.setGiocate(rigaCasa.getGiocate() + 1);
            rigaTrasferta.setGiocate(rigaTrasferta.getGiocate() + 1);

            rigaCasa.setGolFatti(rigaCasa.getGolFatti() + goalsHome);
            rigaCasa.setGolSubiti(rigaCasa.getGolSubiti() + goalsAway);

            rigaTrasferta.setGolFatti(rigaTrasferta.getGolFatti() + goalsAway);
            rigaTrasferta.setGolSubiti(rigaTrasferta.getGolSubiti() + goalsHome);

            if (goalsHome > goalsAway) {
                rigaCasa.setPunti(rigaCasa.getPunti() + 3);
                rigaCasa.setVinte(rigaCasa.getVinte() + 1);
                rigaTrasferta.setPerse(rigaTrasferta.getPerse() + 1);
            } else if (goalsHome < goalsAway) {
                rigaTrasferta.setPunti(rigaTrasferta.getPunti() + 3);
                rigaTrasferta.setVinte(rigaTrasferta.getVinte() + 1);
                rigaCasa.setPerse(rigaCasa.getPerse() + 1);
            } else {
                rigaCasa.setPunti(rigaCasa.getPunti() + 1);
                rigaTrasferta.setPunti(rigaTrasferta.getPunti() + 1);
                rigaCasa.setPareggiate(rigaCasa.getPareggiate() + 1);
                rigaTrasferta.setPareggiate(rigaTrasferta.getPareggiate() + 1);
            }
        }

        List<Classifica> classifica = new ArrayList<>(classificaMap.values());

        classifica.sort(
            Comparator.comparingInt(Classifica::getPunti).reversed()
                    .thenComparing(Comparator.comparingInt(Classifica::getDifferenzaReti).reversed())
                    .thenComparing(Comparator.comparingInt(Classifica::getGolFatti).reversed())
                    .thenComparing(c -> c.getSquadra().getNome())
        );

        return classifica;
    }
}