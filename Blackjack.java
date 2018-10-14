import java.util.*;

public class Blackjack{
	
	private static Scanner scanner = new Scanner(System.in);
	private static Timer timer = new Timer();
	private static int interval = 10;
	
	public static void main(String args[]){
		int totaalWaarde = 0;
		boolean play = true;
		while(play){
			SpeelTafel blackJack = new SpeelTafel();
			
			// welkom speler
			System.out.println("Welkom bij blackjack!");
			
			// voeg speler toe
			System.out.println("Wat is je naam?");
			String input = scanner.nextLine();
			Speler speler = blackJack.voegSpelerToe(input);
			// geef opties
			System.out.println("\n===============================");
			System.out.println("Welkom " + speler.getNaam() + ".\nDruk enter om je kaarten te ontvangen");
			System.out.println("===============================");
			input = scanner.nextLine();
			int result = play(blackJack, speler, "Totaal waarde: ", true);
			//System.out.println(result + " is de waarde van result");
			if(result == 21){
				System.out.println("Je hebt gewonnen!\nJouw kaarten zijn in totaal " + result + " waard");
				play = playAgain();
				if(!play){
					System.exit(0);
				}
			}else if(result > 21){
				System.out.println("Je hebt verloren!\nJouw kaarten zijn in totaal " + result + " waard");
				play = playAgain();
				if(!play){
					System.exit(0);
				}
			}else{
				System.out.println("Tot ziens!");
				System.exit(0);
			}
		}
	}
	
	public static boolean playAgain(){
		System.out.println("\n===============================");
		System.out.println("Play again? j/n");
		System.out.println("===============================");
		String input = scanner.nextLine();
		if(input.equalsIgnoreCase("j")){
			return true;
		}else{
			//System.out.println(input + " is the input");
			return false;
		}
	}
	
	public static int play(SpeelTafel tafel, Speler speler, String message, boolean nieuwSpel){
		//System.out.println("Restart");
		// geef speler zijn kaarten
		if(nieuwSpel){
			Kaart kaart1 = tafel.geefKaart(), kaart2 = tafel.geefKaart();
			if(kaart1 != null && kaart2 != null){
				System.out.println("===============================");
				speler.ontvangKaart(kaart1, kaart2);
			}else{
				System.out.println("\n===============================");
				System.out.println("De kaarten worden opnieuw geschud.\nDruk enter om verder te gaan.");
				System.out.println("===============================");
				tafel.pakNieuweDek();
				speler.ontvangKaart(tafel.geefKaart(), tafel.geefKaart());
			}
			speler.berekenKaartenWaarde(tafel.getTotaalWaardeOpTafel());
			System.out.println("Totaal: " + speler.getWaardeInHand());
			//System.out.println("\nEr zitten nog " + tafel.getAantalKaarten() + " kaarten in het dek");
		}
		
		System.out.println("\n============Opties=============");
		System.out.println("k - om een kaart te krijgen\np - om te passen\nq - om te stoppen");
		System.out.println("===============================");
		System.out.println("Je hebt " + interval + " seconden om een keuze te maken");
		startTimer();
		System.out.println();
		String input = scanner.nextLine();
		int totaalWaarde = 0;
		if(input.equalsIgnoreCase("k")){
			stopTimer();
			System.out.println("\n===============================");
			System.out.println("Er wordt een nieuwe kaart gepakt.\nDruk enter om verder te gaan");
			System.out.println("===============================");
			input = scanner.nextLine();
			// kaart op tafel
			tafel.plaatsKaartOpTafel();
			System.out.println("Totaal: " + tafel.getTotaalWaardeOpTafel());
			speler.toonHuidigeKaarten();
			System.out.println("Totaal: " + speler.getWaardeInHand());
			
			totaalWaarde += speler.getWaardeInHand() + tafel.getTotaalWaardeOpTafel();
			System.out.println("\n" + message + totaalWaarde);
			if(totaalWaarde < 21){
				//System.out.println("repeat");
				play(tafel, speler, message, false);
			}
		}else if(input.equalsIgnoreCase("p")){
			stopTimer();
			tafel.resetTafel();
			System.out.println("\n===============================");
			System.out.println("Er worden nieuwe kaarten voor je gepakt.\nDruk enter om verder te gaan");
			System.out.println("===============================");
			input = scanner.nextLine();
			play(tafel, speler, message, true);
		}
		return speler.getWaardeInHand() + tafel.getTotaalWaardeOpTafel();
	}
	
	public static void startTimer(){
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
		interval = 10;
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
				System.out.print(interval-- + " ");
                if (interval == 0) {
                    timer.cancel();
					System.out.println("\nUw tijd is op! Tot ziens!");
                    System.exit(0);
                }
            }
        }, delay, period);
    }

    public static void stopTimer(){
			interval = 10;
            timer.cancel();
    }
	
}

class Speler{
	
	private String naam;
	private int waardeInHand;
	private List<Kaart> kaartInHand = new ArrayList<>();
	
	
	public Speler(String naam){
		this.naam = naam;
	}
	
	public void ontvangKaart(Kaart kaart1, Kaart kaart2){
		verwijderKaarten();
		if(kaart1 != null && kaart2 != null){
			kaartInHand.add(kaart1);
			kaartInHand.add(kaart2);
		}
		toonHuidigeKaarten();
	}
	
	private void verwijderKaarten(){
		kaartInHand.clear();
	}
	
	public void berekenKaartenWaarde(int totalWaarde){
		int eersteWaarde = 0, tweedeWaarde = 0;
		
		if(!kaartInHand.isEmpty()){
			if(!kaartInHand.get(0).isEenAas()){
				eersteWaarde = kaartInHand.get(0).geefDeWaarde();
			}else{
				eersteWaarde = kaartInHand.get(0).bepaalDeWaardeVanAas(totalWaarde);
			}
			if(!kaartInHand.get(1).isEenAas()){
				tweedeWaarde = kaartInHand.get(1).geefDeWaarde();
			}else{
				tweedeWaarde = kaartInHand.get(1).bepaalDeWaardeVanAas(totalWaarde);
			}
		}
		waardeInHand = eersteWaarde + tweedeWaarde;
	}
	
	public void toonHuidigeKaarten(){
		System.out.println("\nJouw kaarten");
		for(Kaart kaart : kaartInHand){
			System.out.print(kaart + " ");
		}
		System.out.println();
	}
	
	public int getWaardeInHand(){
		return waardeInHand;
	}
	
	public String getNaam(){
		return this.naam;
	}
	
}

class SpeelTafel{
	
	private Kaart[] kaartenDek;
	private List<Kaart> kaartenOpTafel = new ArrayList<>();
	private Speler speler;
	
	private int aantalKaarten = 52;
	private int huidigeLocatieInDek;
	private int totaalWaardeOpTafel;
	
	public SpeelTafel(){
		kaartenDek = new KaartenMaker().getKaartenDek();
	}
	
	public void pakNieuweDek(){
		kaartenDek = new KaartenMaker().getKaartenDek();
		aantalKaarten = kaartenDek.length;
		huidigeLocatieInDek = 0;
		//System.out.println("Aantal kaarten zijn: " + aantalKaarten);
	}
	
	public Speler voegSpelerToe(String spelerNaam){
		speler = new Speler(spelerNaam);
		return speler;
	}
	
	public boolean plaatsKaartOpTafel(){
		totaalWaardeOpTafel = 0;
		if(aantalKaarten > 0){
			aantalKaarten--;
			kaartenOpTafel.add(kaartenDek[huidigeLocatieInDek++]);
			totaalWaardeKaartenOpTafel();
			toonAlleKaartenOpTafel();
			return true;
		}else{
			System.out.println("Het huidige dek heeft geen kaarten meer");
			return false;
		}
	}
	
	public Kaart geefKaart(){
		if(aantalKaarten > 0){
			aantalKaarten--;
			return kaartenDek[huidigeLocatieInDek++];
		}else{
			return null;
		}
	}
	
	public void resetTafel(){
		kaartenOpTafel.clear();
	}
	
	private void totaalWaardeKaartenOpTafel(){
		for(Kaart kaart : kaartenOpTafel){
			if(!kaart.isEenAas()){
				totaalWaardeOpTafel += kaart.geefDeWaarde();
			}else{
				totaalWaardeOpTafel += kaart.bepaalDeWaardeVanAas(totaalWaardeOpTafel);
			}
		}
	}
	
	private void toonAlleKaartenOpTafel(){
		if(!kaartenOpTafel.isEmpty()){
			System.out.println("Kaarten op tafel");
			for(Kaart kaart : kaartenOpTafel){
				if(!kaart.isEenAas()){
					System.out.println(kaart + " waarde: " + kaart.geefDeWaarde());
				}else{
					System.out.println(kaart + " waarde: " + kaart.bepaalDeWaardeVanAas(totaalWaardeOpTafel));
				}
			}
		}else{
			System.out.println("Er zijn geen kaarten op tafel");
		}
	}
	
	public int getAantalKaarten(){
		return aantalKaarten;
	}
	
	public List getKaartenOpTafel(){
		return kaartenOpTafel;
	}
	
	public Speler getSpeler(){
		return speler;
	}
	
	public int getTotaalWaardeOpTafel(){
		return totaalWaardeOpTafel;
	}
	
}

class KaartenMaker{
	
	private Kaart kaartenDek[] = new Kaart[52];
	private String [] soortKaarten = "Ha Sc Kl Ru".split(" ");
	
	KaartenMaker(){
		kaartenMaken();
		schudKaarten();
		//geefLijstMetRandoms();
	}
	
	public void schudKaarten(){
		int [] randomLijst = geefLijstMetRandoms();
		
		for(int i = 0; i < kaartenDek.length; i++){
			Kaart temp = kaartenDek[i];
			kaartenDek[i] = kaartenDek[randomLijst[i]];
			kaartenDek[randomLijst[i]] = temp;
		}
		
	}
	
	public void printKaarten(){
		int nummering = 0;
		int counter = 1;
		for(Kaart kaart: kaartenDek){
			if(counter % 13 == 0){
				System.out.print(kaart + "\n\n");
				counter = 1;
			}else{
				System.out.print(kaart + " ");
				counter++;
			}
		}
	}
	
	private int[] geefLijstMetRandoms(){
		Random random = new Random();
		int randomLijst[] = new int[kaartenDek.length];
		boolean isDuplicate = false;
		
		for(int i = 0; i < randomLijst.length; i++){
			//System.out.println("i is " + i);
			int getal = random.nextInt(kaartenDek.length);
			if(i == 0){
				randomLijst[i] = getal;
			}else{
				for(int y = 0; y < i; y++){
					if(randomLijst[y] == getal){
						i--;
						//System.out.println("after being lowered i is: " + i);
						break;
					}
					randomLijst[i] = getal;
				
				}
			}
		}  
		
		return randomLijst;
	}
	
	private void kaartenMaken(){
		//System.out.println("kaarten worden gemaakt"); 
		int positie = 0;
		for(int x = 0; x < 4; x++){
			for(int i = 1; i < 14; i++){
				//System.out.println(positie);
				switch(i){
					case 1:
					kaartenDek[positie++] = new Kaart(soortKaarten[x], "A");
					//i = 1;
					break;
					case 11:
					kaartenDek[positie++] = new Kaart(soortKaarten[x], "B");
					break;
					case 12:
					kaartenDek[positie++] = new Kaart(soortKaarten[x], "Q");
					break;
					case 13:
					kaartenDek[positie++] = new Kaart(soortKaarten[x], "K");
					break;
					default:
					kaartenDek[positie++] = new Kaart(soortKaarten[x],"" + i);
				}
			}
		}
	}
	
	public Kaart[] getKaartenDek(){
		return kaartenDek;
	}
}

class Kaart{
		
	private String soort;
	private String waarde;
	
	Kaart(String soort, String waarde){
		this.soort = soort;
		this.waarde = waarde;
	}
	
	public int geefDeWaarde(){
		if(converteerbaarNaarInt()){
			return Integer.parseInt(this.waarde);
		}else{
			return 10;
		}
	}
	
	public boolean isEenAas(){
		if(waarde.equals("A")){
			return true;
		}
		return false;
	}
	
	public int bepaalDeWaardeVanAas(int waardeOpTafel){
		if(waardeOpTafel + 11 == 21){
			return 11;
		}
		return 1;
	}
	
	private boolean converteerbaarNaarInt(){
		switch(waarde){
			case "K": case "Q": case "B":
			return false;
			case "A":
			return false;
		}
		
		return true;
	}
	
	public String getWaarde(){
		return waarde;
	}
	
	@Override
	public boolean equals(Object o){
		
		if(o == this){
			return true;
		}
		
		if(!(o instanceof Kaart)){
			return false;
		}
		
		Kaart k = (Kaart) o;
		
		return k.waarde.equals(waarde) && k.soort.equals(soort);
		
	}
	
	@Override
	public String toString(){
		return "[" + soort + " " + waarde + "]";
	}
}

