package br.com.chamadosweb.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.com.chamadosweb.padrao.GenericService;
import br.com.chamadosweb.service.model.Atendimento;
import br.com.chamadosweb.service.model.Chamado;

/**
 * @author Renan Celso
 */
@Stateless
public class AtendimentoService extends GenericService implements AtendimentoServiceLocal {

	private static final long serialVersionUID = 3168122228285071290L;

	@Override
	public List<Chamado> consultarChamados(Chamado chamadoFiltroConsulta) {
		try {
			List<Chamado> listaChamados = new ArrayList<Chamado>();
					
			StringBuilder sql = new StringBuilder();
			sql.append("select o from ").append(Chamado.class.getSimpleName()).append(" o where 1=1");			
			
			if(chamadoFiltroConsulta.getNrChamado() != null && chamadoFiltroConsulta.getNrChamado() > 0){
				sql.append(" and o.nrChamado = ").append(chamadoFiltroConsulta.getNrChamado());
			}		
			
			sql.append(" order by o.nrChamado desc");
			
			listaChamados = (List<Chamado>) consultarPorQuery(sql.toString(),0, 0);
								
			return listaChamados;
			
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	@Override
	public List<Atendimento> consultarAtendimentosPorChamado(Chamado chamado) {
		try {
			List<Atendimento> listaAtendimento = new ArrayList<Atendimento>();
					
			StringBuilder sql = new StringBuilder();
			sql.append("select o from ").append(Atendimento.class.getSimpleName()).append(" o where 1=1");	
			sql.append(" and o.chamado.nrChamado = ").append(chamado.getNrChamado());			
			sql.append(" order by o.nrSq desc");
						
			listaAtendimento = (List<Atendimento>) consultarPorQuery(sql.toString(), 0, 0);
								
			return listaAtendimento;
			
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
	}


}