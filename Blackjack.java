import java.util.*;

public class Blackjack{
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String args[]){
		
		/*
		KaartenMaker dek = new KaartenMaker();
		
		dek.printKaarten();
		*/
		
		/*
		SpeelTafel tafel = new SpeelTafel();
		
		tafel.plaatsKaartOpTafel();
		tafel.plaatsKaartOpTafel();
		tafel.plaatsKaartOpTafel();
		
		System.out.println(tafel.totaalWaardeKaartenOpTafel());
		*/
		
		boolean play = true;
		while(play){
			System.out.println("k - om een kaart te krijgen\np - om te passen\nq - om te stoppen");
			String input = scanner.nextLine();
			
			if(input.equalsIgnoreCase("q")){
				play = false;
			}
			
			
			
		}
		
		
	}
	
}

class Speler{
	
	private String naam;
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
	}
	
	private void verwijderKaarten(){
		kaartInHand.clear();
	}
	
	public int waardeHuidigeKaarten(int totalWaarde){
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
		return eersteWaarde + tweedeWaarde;
	}
	
	public void toonHuidigeKaarten(){
		System.out.println("Jouw kaarten:");
		for(Kaart kaart : kaartInHand){
			System.out.print(kaart + " ");
		}
	}
	
}

class SpeelTafel{
	
	private Kaart[] kaartenDek;
	private List<Kaart> kaartenOpTafel = new ArrayList<>();
	private List<Speler> aantalSpeler = new ArrayList<>();
	
	private int aantalKaarten = 52;
	private int huidigeLocatieInDek = 0;
	private int totaalWaardeOpTafel = 0;
	
	public SpeelTafel(){
		kaartenDek = new KaartenMaker().getKaartenDek();
	}
	
	public void pakNieuweDek(){
		kaartenDek = new KaartenMaker().getKaartenDek();
		aantalKaarten = kaartenDek.length;
		//System.out.println("Aantal kaarten zijn: " + aantalKaarten);
	}
	
	public void voegSpelerToe(Speler speler){
		aantalSpeler.add(speler);
	}
	
	public boolean plaatsKaartOpTafel(){
		if(aantalKaarten > 0){
			aantalKaarten--;
			kaartenOpTafel.add(kaartenDek[huidigeLocatieInDek++]);
			toonAlleKaartenOpTafel();
			return true;
		}else{
			System.out.println("Het huidige dek heeft geen kaarten meer");
			return false;
		}
	}
	
	public int totaalWaardeKaartenOpTafel(){
		for(Kaart kaart : kaartenOpTafel){
			if(!kaart.isEenAas()){
				totaalWaardeOpTafel += kaart.geefDeWaarde();
			}else{
				totaalWaardeOpTafel += kaart.bepaalDeWaardeVanAas(totaalWaardeOpTafel);
			}
		}
		return totaalWaardeOpTafel;
	}
	
	private void toonAlleKaartenOpTafel(){
		if(!kaartenOpTafel.isEmpty()){
			System.out.println("Kaarten in het spel");
			for(Kaart kaart : kaartenOpTafel){
			System.out.print(kaart + " ");
			}
		}else{
			System.out.println("Er zijn geen kaarten op tafel");
		}
		System.out.println();
	}
	
	public Kaart geefKaart(){
		if(aantalKaarten > 0){
			aantalKaarten--;
			return kaartenDek[huidigeLocatieInDek++];
		}else{
			System.out.println("Je hebt geen kaarten meer in het dek");
			return null;
		}
	}
	
	public List getKaartenOpTafel(){
		return kaartenOpTafel;
	}
	
	public int totaalWaardeOpTafel(){
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

