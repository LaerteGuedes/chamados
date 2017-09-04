package br.com.chamadosweb.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.chamadosweb.padrao.BaseControl;
import br.com.chamadosweb.service.AtendimentoServiceLocal;
import br.com.chamadosweb.service.model.Atendimento;
import br.com.chamadosweb.service.model.Chamado;

/**
*
* @author Renan Celso
*/
@ManagedBean(name = "atendimentoAlteracaoControl")
@ViewScoped
public class AtendimentoAlteracaoControl extends BaseControl {

	private static final long serialVersionUID = 1158281484523184385L;

	@EJB
	private AtendimentoServiceLocal atendimentoService;
	
	private Chamado chamadoBuscar;	
	
	private List<Atendimento> listaAtendimentos;
	
	private Atendimento atendimentoDetalhar; 	
	
	private String dataAtendimentoTransferidoEquipe;
	
	private String horaAtendimentoTransferidoEquipe;
	
	private String dataAtendimentoRespostaCliente;
	
	private String horaAtendimentoRespostaCliente;
	
	@PostConstruct
	public void init() {		
		chamadoBuscar = new Chamado();
		listaAtendimentos = new ArrayList<Atendimento>();
		atendimentoDetalhar = new Atendimento();
		dataAtendimentoTransferidoEquipe = "";		
		horaAtendimentoTransferidoEquipe = "";		
		dataAtendimentoRespostaCliente = "";		
		horaAtendimentoRespostaCliente = "";
	}
	
	public String limpar(){
		chamadoBuscar = new Chamado();
		listaAtendimentos = new ArrayList<Atendimento>();
		atendimentoDetalhar = new Atendimento();
		dataAtendimentoTransferidoEquipe = "";		
		horaAtendimentoTransferidoEquipe = "";		
		dataAtendimentoRespostaCliente = "";		
		horaAtendimentoRespostaCliente = "";
		return null;
	}
	

	public String buscarAtendimentosPorChamado() {
		
		listaAtendimentos = new ArrayList<Atendimento>();
		
		Long nrChamadoAux = chamadoBuscar.getNrChamado();		
		
		if(nrChamadoAux == null || nrChamadoAux == 0){
			addErrorMessage("Informe um chamado.");
			chamadoBuscar = new Chamado();			
			return null;
		}
		
		chamadoBuscar = (Chamado) atendimentoService.consultarPorChavePrimaria(chamadoBuscar, chamadoBuscar.getNrChamado());
				
		if(chamadoBuscar == null || (chamadoBuscar.getNrChamado() == null || chamadoBuscar.getNrChamado() == 0)) {			
			addErrorMessage("Chamado não encontrado. Favor informar um número de chamado válido.");
			chamadoBuscar = new Chamado();			
			return null;		
		} 
		
		listaAtendimentos = atendimentoService.consultarAtendimentosPorChamado(chamadoBuscar);
		
		if(listaAtendimentos == null && listaAtendimentos.isEmpty()){
			addErrorMessage("Não existem atendimentos cadastrados para o chamado"+chamadoBuscar.getNrChamado());
			chamadoBuscar = new Chamado();				
			return null;
		}
						
		return null;
	}
	
	public String detalhar() {
		
		SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");	
		SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
		
		if(atendimentoDetalhar.getDhTransferidoEquipe() != null) {	
			dataAtendimentoTransferidoEquipe = sdfData.format(atendimentoDetalhar.getDhTransferidoEquipe());			
			horaAtendimentoTransferidoEquipe = sdfHora.format(atendimentoDetalhar.getDhTransferidoEquipe());
		}
		    		
		if(atendimentoDetalhar.getDhRespostaCliente() != null) {			
			dataAtendimentoRespostaCliente = sdfData.format(atendimentoDetalhar.getDhRespostaCliente());			
			horaAtendimentoRespostaCliente = sdfHora.format(atendimentoDetalhar.getDhRespostaCliente());
		}		
					
		return null;
	}
	
	
	public String alterarAtendimento() {
		try {
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			if(dataAtendimentoTransferidoEquipe != null 
    				&& horaAtendimentoTransferidoEquipe != null
    				&& !dataAtendimentoTransferidoEquipe.equalsIgnoreCase("") 
    				&& !horaAtendimentoTransferidoEquipe.equalsIgnoreCase("")){
	    		Date dhAtendimentoTransfEquipe = 
	    				(Date) formatter.parse(dataAtendimentoTransferidoEquipe + " "+horaAtendimentoTransferidoEquipe);	   
	    		atendimentoDetalhar.setDhTransferidoEquipe(dhAtendimentoTransfEquipe);
			}
    		    		
    		if(dataAtendimentoRespostaCliente != null 
    				&& horaAtendimentoRespostaCliente != null
    				&& !dataAtendimentoRespostaCliente.equalsIgnoreCase("") 
    				&& !horaAtendimentoRespostaCliente.equalsIgnoreCase("")){
	    		Date dhAtendimentoRespostaCliente = 
	    				(Date) formatter.parse(dataAtendimentoRespostaCliente + " "+horaAtendimentoRespostaCliente);	
	    		atendimentoDetalhar.setDhRespostaCliente(dhAtendimentoRespostaCliente);    	
    		}			
    		
			atendimentoService.atualizar(atendimentoDetalhar);
			
			addInfoMessage("Atendimento "+atendimentoDetalhar.getNrSq()+" alterado com sucesso no chamado "+atendimentoDetalhar.getChamado().getNrChamado());	
			
		} catch(Exception e){
			addErrorMessage("Erro ao atualizar atendimento. "+e.getMessage());
		}
		
		return null;
	}
	
	public String voltar() {
		
		atendimentoDetalhar = new Atendimento();
		dataAtendimentoTransferidoEquipe = "";		
		horaAtendimentoTransferidoEquipe = "";		
		dataAtendimentoRespostaCliente = "";		
		horaAtendimentoRespostaCliente = "";
		
		return null;
	}
	

	public Chamado getChamadoBuscar() {
		return chamadoBuscar;
	}

	public void setChamadoBuscar(Chamado chamadoBuscar) {
		this.chamadoBuscar = chamadoBuscar;
	}

	public List<Atendimento> getListaAtendimentos() {
		return listaAtendimentos;
	}

	public void setListaAtendimentos(List<Atendimento> listaAtendimentos) {
		this.listaAtendimentos = listaAtendimentos;
	}

	public Atendimento getAtendimentoDetalhar() {
		return atendimentoDetalhar;
	}

	public void setAtendimentoDetalhar(Atendimento atendimentoDetalhar) {
		this.atendimentoDetalhar = atendimentoDetalhar;
	}

	public String getDataAtendimentoTransferidoEquipe() {
		return dataAtendimentoTransferidoEquipe;
	}

	public void setDataAtendimentoTransferidoEquipe(
			String dataAtendimentoTransferidoEquipe) {
		this.dataAtendimentoTransferidoEquipe = dataAtendimentoTransferidoEquipe;
	}

	public String getHoraAtendimentoTransferidoEquipe() {
		return horaAtendimentoTransferidoEquipe;
	}

	public void setHoraAtendimentoTransferidoEquipe(
			String horaAtendimentoTransferidoEquipe) {
		this.horaAtendimentoTransferidoEquipe = horaAtendimentoTransferidoEquipe;
	}

	public String getDataAtendimentoRespostaCliente() {
		return dataAtendimentoRespostaCliente;
	}

	public void setDataAtendimentoRespostaCliente(
			String dataAtendimentoRespostaCliente) {
		this.dataAtendimentoRespostaCliente = dataAtendimentoRespostaCliente;
	}

	public String getHoraAtendimentoRespostaCliente() {
		return horaAtendimentoRespostaCliente;
	}

	public void setHoraAtendimentoRespostaCliente(
			String horaAtendimentoRespostaCliente) {
		this.horaAtendimentoRespostaCliente = horaAtendimentoRespostaCliente;
	}	
		
	
}