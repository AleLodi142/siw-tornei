package siw_tornei;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import siw_tornei.service.TorneoService;

@SpringBootApplication
public class SiwTorneiApplication implements CommandLineRunner {

    	private TorneoService torneoService;

    	public SiwTorneiApplication(TorneoService torneoService) {
        	this.torneoService = torneoService;
    	}

    	public static void main(String[] args) {
        	SpringApplication.run(SiwTorneiApplication.class, args);
    	}

   		@Override
		public void run(String... args) {

    		try {
        		testPrestazioni();
    		}

    		catch (Exception e) {
       		 	e.printStackTrace();
    		}
		}

    	/**
    	 * Punto 8
    	 *
    	 * Caso d'uso analizzato:
    	 * Visualizzazione del dettaglio di un torneo con le squadre partecipanti.
    	 *
    	 * Strategie confrontate:
    	 * - caricamento LAZY
    	 * - JOIN FETCH
     	*/
    	private void testPrestazioni() {

    	Long torneoId = 1L;

    	int numeroProve = 1000;

    	System.out.println();
    	System.out.println("=========================================");
    	System.out.println(" ANALISI PRESTAZIONI JPA/HIBERNATE");
    	System.out.println("=========================================");
    	System.out.println();

    	/*
    	 * Warm-up
    	 */

    	for (int i = 0; i < 100; i++) {

        	torneoService.testLazy(torneoId);
        	torneoService.testFetch(torneoId);
    	}

    	/*
    	 * Strategia LAZY
     	*/

    	long startLazy = System.nanoTime();

    	for (int i = 0; i < numeroProve; i++) {

        	torneoService.testLazy(torneoId);

    	}

    	long endLazy = System.nanoTime();

    	/*
    	 * Strategia JOIN FETCH
    	 */

    	long startFetch = System.nanoTime();

    	for (int i = 0; i < numeroProve; i++) {

        	torneoService.testFetch(torneoId);

    	}

    	long endFetch = System.nanoTime();

    	double tempoLazy =
        	    (endLazy - startLazy) / 1_000_000.0;

    	double tempoFetch =
        	    (endFetch - startFetch) / 1_000_000.0;

    	System.out.println("Numero prove: " + numeroProve);
    	System.out.println();

    	System.out.printf(
            	"Strategia LAZY       : %.3f ms%n",
        	    tempoLazy);

    	System.out.printf(
            	"Strategia JOIN FETCH : %.3f ms%n",
        	    tempoFetch);

    	System.out.println();

    	if (tempoFetch < tempoLazy) {

        	double miglioramento =
                	((tempoLazy - tempoFetch)
            	            / tempoLazy) * 100.0;

        	System.out.printf(
            	    "JOIN FETCH è risultato più veloce del %.2f%%%n",
        	        miglioramento);

    	}

    	else {

        	double miglioramento =((tempoFetch - tempoLazy)/ tempoFetch) * 100.0;

        	System.out.printf(
            	    "LAZY è risultato più veloce del %.2f%%%n",
        	        miglioramento);

    	}

    	System.out.println();
    	System.out.println("=========================================");
	}

	/**
 * ============================================================
 * ANALISI SPERIMENTALE DELLE PRESTAZIONI (Punto 8)
 * ============================================================
 *
 * Caso d'uso analizzato:
 * Visualizzazione del dettaglio di un torneo con le squadre
 * partecipanti.
 *
 * Sono state confrontate due strategie di caricamento dei dati:
 *
 * 1) LAZY
 *    - Recupero del torneo tramite findById().
 *    - La collezione delle squadre viene caricata solo quando
 *      viene effettivamente utilizzata.
 *
 * 2) JOIN FETCH
 *    - Recupero del torneo tramite findByIdWithSquadre().
 *    - Torneo e squadre vengono recuperati con un'unica query SQL.
 *
 * Per entrambe le strategie sono state eseguite 1000 iterazioni,
 * misurando il tempo complessivo tramite System.nanoTime().
 *
 * Risultati ottenuti:
 *
 * LAZY       : 1670,823 ms
 * JOIN FETCH : 1199,740 ms
 *
 * Nel test effettuato la strategia JOIN FETCH è risultata
 * più veloce del 28,19%, grazie alla riduzione del numero di
 * interrogazioni al database necessarie per recuperare le
 * squadre appartenenti al torneo.
 *
 * ============================================================
 */
}