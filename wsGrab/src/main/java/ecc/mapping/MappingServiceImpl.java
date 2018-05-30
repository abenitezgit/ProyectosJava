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
			0:	ani				: 1000000000000010	1000000000011010 	1000000000011110 1000000000000110
			1:	dnis				: 0100000000000010	0100000000011010 	0100000000011110 0100000000000110
			2:	connid			: 0010000000000010	0010000000011010 	0010000000011110 0010000000000110
			3:	agente			: 0001000000000010	0001000000011010 	0001000000011110 0001000000000110
			4:	uniqueid			: 0000100000000010	0000100000011010 	0000100000011110 0000100000000110
			5:	codigoservicio	: 0000010000000010	0000010000011010 	0000010000011110 0000010000000110
			6:	interactionid	: 0000001000000010	0000001000011010 	0000001000011110 0000001000000110
			7:	fname			: 0000000100000010
			8:	zone				: 0000000010000010	0000000010011010	 0000000010011110 0000000010000110
			9:	rutagente		: 0000000001000010	0000000001011010	 0000000001011110 0000000001000110
			10:	tipointeract		: 0000000000111010	0000000000111110	 0000000000100110
			11: fechaDesde		: 
			12: fechaHasta		:
			13: skill 			: 0000000000011110
			14: Limite 			: Obligaciom
			15: Fono				: 0000000000000011  0000000000011111 0000000000000111
			**/ 
			
			gParams.getMapOpcion().put("1000000000000010", "0:Ani");
			gParams.getMapOpcion().put("1000000000011110", "2:Ani & Fechas & Skill");
			gParams.getMapOpcion().put("1000000000000110", "3:Ani & Skill");
			gParams.getMapOpcion().put("0100000000000010", "4:Dnis");
			gParams.getMapOpcion().put("0100000000011110", "6:Dnis & Fechas & Skill");
			gParams.getMapOpcion().put("0100000000000110", "7:Dnis & Skill");
			gParams.getMapOpcion().put("0010000000000010", "8:ConnID");
			gParams.getMapOpcion().put("0010000000011110", "10:ConnID & Fechas & Skill");
			gParams.getMapOpcion().put("0010000000000110", "11:ConnID & Skill");
			gParams.getMapOpcion().put("0001000000000010", "12:Agente");
			gParams.getMapOpcion().put("0001000000011110", "14:Agente & Fechas & Skill");
			gParams.getMapOpcion().put("0001000000000110", "15:Agente & Skill");
			gParams.getMapOpcion().put("0000100000000010", "16:UniqueID");
			gParams.getMapOpcion().put("0000100000011110", "18:UniqueID & Fechas & Skill");
			gParams.getMapOpcion().put("0000100000000110", "19:UniqueID & Skill");
			gParams.getMapOpcion().put("0000010000000010", "20:CodigoServicio");
			gParams.getMapOpcion().put("0000010000011110", "22:CodigoServicio & Fechas & Skill");
			gParams.getMapOpcion().put("0000010000000110", "23:CodigoServicio & Skill");
			gParams.getMapOpcion().put("0000001000000010", "24:InteractionID");
			gParams.getMapOpcion().put("0000001000011110", "26:InteractionID & Fechas & Skill");
			gParams.getMapOpcion().put("0000001000000110", "27:InteractionID & Skill");
			gParams.getMapOpcion().put("0000000100000010", "28:Nombre Grabacion");
			gParams.getMapOpcion().put("0000000010000010", "29:Zone");
			gParams.getMapOpcion().put("0000000010011110", "31:Zone & Fechas & Skill");
			gParams.getMapOpcion().put("0000000010000110", "32:Zone & Skill");
			gParams.getMapOpcion().put("0000000001000010", "33:RutAgente");
			gParams.getMapOpcion().put("0000000001011110", "35:RutAgente & Fechas & Skill");
			gParams.getMapOpcion().put("0000000001000110", "36:RutAgente & Skill");
			gParams.getMapOpcion().put("0000000000111110", "38:TipoInteraction & Fechas & Skill");
			gParams.getMapOpcion().put("0000000000100110", "39:TipoInteraction & Skill");
			gParams.getMapOpcion().put("0000000000011110", "40:Skill & Fechas");
			gParams.getMapOpcion().put("0000000000000011", "41:Fono");
			gParams.getMapOpcion().put("0000000000011111", "42:Fono & Fechas & Skill");
			gParams.getMapOpcion().put("0000000000000111", "43:Fono & Skill");
			
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
			15: Fono
			**/ 

			List<String> f = new ArrayList<>();
			
			String str = opcionMatriz;
			char[] charArray = str.toCharArray();
			Character[] charObjectArray = ArrayUtils.toObject(charArray);
			
			if (charObjectArray[0].toString().equals("1")) {
				f.add("ani:\""+gParams.getDr().getAni()+"\"");
			}
			
			if (charObjectArray[1].toString().equals("1")) {
				f.add("dnis:\""+gParams.getDr().getDnis()+"\"");
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

			if (charObjectArray[15].toString().equals("1")) {
				f.add("(ani:\""+gParams.getDr().getFono()+"\" OR dnis:\""+gParams.getDr().getFono()+"\")");
			}

			gParams.setSolRfqFilters(StringUtils.join(f, " AND "));
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
