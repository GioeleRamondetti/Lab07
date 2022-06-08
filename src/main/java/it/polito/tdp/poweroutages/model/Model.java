package it.polito.tdp.poweroutages.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;  

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	private int p=0;
	private int max;
	private boolean flag=false;
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public int startRecursion(int anni,int oreMax) {   
		//passo 0 come numero di persone e livello zero
		List<events> parziale=new ArrayList<events>();
		this.max=0;
		ricorsione(parziale,anni,oreMax,0,0);
		return this.p;
	}

	private void ricorsione(List<events> parziale, int anni, int oreMax,int persone,long livello) {
		List<events> evtot=new ArrayList<events>(podao.getEvnts());
		//System.out.println(persone+" max "+this.max+" liv "+livello);
		if(this.flag==true){
			//condizone finale
			if(persone>max) {
				this.max=persone;
				this.p=max;
			}
		}
		for(int i=0;i<evtot.size();i++) {
			long cntore=0;
			for(int j=0;j<parziale.size();j++) {
				cntore=cntore+parziale.get(j).getOre();
			}
				boolean ok=check(parziale,anni,oreMax,evtot.get(i));
				if(ok==true) {
					this.flag=true;
					parziale.add(evtot.get(i));
					//System.out.println(persone+" aggiungo "+persone);
					ricorsione(parziale,anni,oreMax,persone+evtot.get(i).getCustomers_affected(),livello+evtot.get(i).getCustomers_affected());
					parziale.remove(parziale.size()-1);
				}else {
					this.flag=false;
				}
			
		}
		
	}

	private boolean check(List<events> parziale, int anni, int oreMax,events e) {
		boolean flag=true;
		long oretot=e.getOre();
		int min=100000000;
		int maxx=-10;
		for(int i=0;i<parziale.size();i++) {
			oretot=oretot+parziale.get(i).getOre();
			if(parziale.get(i).getAnnoStart()<min) {
				min=parziale.get(i).getAnnoStart();
			}
			if(parziale.get(i).getAnnoEnd()>maxx) {
				maxx=parziale.get(i).getAnnoEnd();
			}
			
		}
		//controllo di quello che sto per aggiungere
		//System.out.println("minimo: "+min+" max: "+maxx+" annos "+e.getAnnoStart()+" annoff "+e.getAnnoEnd());
		if(e.getAnnoStart()<min) {
			min=e.getAnnoStart();
		}
		if(e.getAnnoEnd()>maxx) {
			maxx=e.getAnnoEnd();
		}
		if(oretot>oreMax) {
			flag=false;
		}
		if((maxx-min)>anni) {
			flag=false;
		}
		return flag;
	}



}
