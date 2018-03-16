package ecc.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import ecc.utiles.GlobalArea;

public class MappingServiceImpl implements MappingService{
	GlobalArea gParams;
	
	public MappingServiceImpl(GlobalArea m) {
		gParams = m;
	}


	@Override
	public void setOpcionMapping() throws Exception {
		try {
			/**
			0:	ani				: 100000000000001	100000000001101	100000000001111 100000000000011
			1:	dnis				: 010000000000001	010000000001101	010000000001111 010000000000011
			2:	connid			: 001000000000001	001000000001101	001000000001111 001000000000011
			3:	agente			: 000100000000001	000100000001101	000100000001111 000100000000011
			4:	uniqueid			: 000010000000001	000010000001101	000010000001111 000010000000011
			5:	codigoservicio	: 000001000000001	000001000001101	000001000001111 000001000000011
			6:	interactionid	: 000000100000001	000000100001101	000000100001111 000000100000011
			7:	fname			: 000000010000001
			8:	zone				: 000000001000001	000000001001101	000000001001111 000000001000011
			9:	rutagente		: 000000000100001	000000000101101	000000000101111 000000000100011
			10:	tipointeract		: 000000000011101	000000000011111	000000000010011
			11: fechaDesde		: 
			12: fechaHasta		:
			13: skill 			: 000000000001111
			14: Limite 			: Obligaciom
			**/ 
			
			gParams.getMapOpcion().put("100000000000001", "0:Ani");
			gParams.getMapOpcion().put("100000000001111", "2:Ani & Fechas & Skill");
			gParams.getMapOpcion().put("100000000000011", "3:Ani & Skill");
			gParams.getMapOpcion().put("010000000000001", "4:Dnis");
			gParams.getMapOpcion().put("010000000001111", "6:Dnis & Fechas & Skill");
			gParams.getMapOpcion().put("010000000000011", "7:Dnis & Skill");
			gParams.getMapOpcion().put("001000000000001", "8:ConnID");
			gParams.getMapOpcion().put("001000000001111", "10:ConnID & Fechas & Skill");
			gParams.getMapOpcion().put("001000000000011", "11:ConnID & Skill");
			gParams.getMapOpcion().put("000100000000001", "12:Agente");
			gParams.getMapOpcion().put("000100000001111", "14:Agente & Fechas & Skill");
			gParams.getMapOpcion().put("000100000000011", "15:Agente & Skill");
			gParams.getMapOpcion().put("000010000000001", "16:UniqueID");
			gParams.getMapOpcion().put("000010000001111", "18:UniqueID & Fechas & Skill");
			gParams.getMapOpcion().put("000010000000011", "19:UniqueID & Skill");
			gParams.getMapOpcion().put("000001000000001", "20:CodigoServicio");
			gParams.getMapOpcion().put("000001000001111", "22:CodigoServicio & Fechas & Skill");
			gParams.getMapOpcion().put("000001000000011", "23:CodigoServicio & Skill");
			gParams.getMapOpcion().put("000000100000001", "24:InteractionID");
			gParams.getMapOpcion().put("000000100001111", "26:InteractionID & Fechas & Skill");
			gParams.getMapOpcion().put("000000100000011", "27:InteractionID & Skill");
			gParams.getMapOpcion().put("000000010000001", "28:Nombre Grabacion");
			gParams.getMapOpcion().put("000000001000001", "29:Zone");
			gParams.getMapOpcion().put("000000001001111", "31:Zone & Fechas & Skill");
			gParams.getMapOpcion().put("000000001000011", "32:Zone & Skill");
			gParams.getMapOpcion().put("000000000100001", "33:RutAgente");
			gParams.getMapOpcion().put("000000000101111", "35:RutAgente & Fechas & Skill");
			gParams.getMapOpcion().put("000000000100011", "36:RutAgente & Skill");
			gParams.getMapOpcion().put("000000000011111", "38:TipoInteraction & Fechas & Skill");
			gParams.getMapOpcion().put("000000000010011", "39:TipoInteraction & Skill");
			gParams.getMapOpcion().put("000000000001111", "40:Skill & Fechas");
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	public void setConfigParams() throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void getSolrFiltersMapping(String opcionMatriz) throws Exception {
		try {
			/**
			0:	ani
			1:	dnis
			2:	connid
			3:	agente
			4:	uniqueid
			5:	codigoservicio
			6:	interactionid
			7:	fname
			8:	zone	
			9:	rutagente
			10:	tipointeract
			11: fechaDesde 
			12: fechaHasta
			13: skill
			14: Limite
			**/ 

			List<String> f = new ArrayList<>();
			
			String str = opcionMatriz;
			char[] charArray = str.toCharArray();
			Character[] charObjectArray = ArrayUtils.toObject(charArray);
			
			if (charObjectArray[0].toString().equals("1")) {
				f.add("ani:"+gParams.getDr().getAni());
			}
			
			if (charObjectArray[1].toString().equals("1")) {
				f.add("dnis:"+gParams.getDr().getDnis());
			}
			
			if (charObjectArray[2].toString().equals("1")) {
				f.add("connid:"+gParams.getDr().getConnid());
			}
			
			if (charObjectArray[3].toString().equals("1")) {
				f.add("agente:"+gParams.getDr().getAgente());
			}

			if (charObjectArray[4].toString().equals("1")) {
				f.add("uniqueid:"+gParams.getDr().getUniqueid());
			}

			if (charObjectArray[5].toString().equals("1")) {
				f.add("codigoservicio:"+gParams.getDr().getCodigoservicio());
			}

			if (charObjectArray[6].toString().equals("1")) {
				f.add("interactionid:"+gParams.getDr().getInteractionid());
			}

			if (charObjectArray[7].toString().equals("1")) {
				f.add("fname:"+gParams.getDr().getFname());
			}
			
			if (charObjectArray[8].toString().equals("1")) {
				f.add("zone:"+gParams.getDr().getZone());
			}
			
			if (charObjectArray[9].toString().equals("1")) {
				f.add("rutagente:"+gParams.getDr().getRutagente());
			}

			if (charObjectArray[10].toString().equals("1")) {
				f.add("tipointeract:"+gParams.getDr().getTipointeract());
			}

			if (charObjectArray[13].toString().equals("1")) {
				List<String> newList = new ArrayList<>();
				if (charObjectArray[11].toString().equals("1")  && charObjectArray[12].toString().equals("1")) {
				    	String fechaDesde = gParams.getDr().getFechaDesde();
				    	String fechaHasta = gParams.getDr().getFechaHasta();
				    	
				    	
				    	
				    	for (String sk : gParams.getDr().getLstSkill()) {
				    		newList.add(String.format("id:[%s+%s+ TO %s+%s+]", 
				    				sk, fechaDesde, 
				    				sk, fechaHasta));
				    	}
				} else {
			        for (String sk : gParams.getDr().getLstSkill()) {
			            newList.add(String.format("id:%s+*", sk));
			        }
				}
				String strList = StringUtils.join(newList, " OR "); 
			    f.add("("+strList+")");
			}

			gParams.setSolRfqFilters(StringUtils.join(f, " AND "));
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}


}
