package br.com.questor.crm.enums;

public enum FaseOportunidadeEnum {
	CONTATO_INICIAL("Contato inicial"),
	PROSPECT("Prospect"),
	PROSPECT_QUALIFICADO("Prospect qualificado"),
	CLIENTE_EM_NEGOCIACAO("Cliente em negociação"),
	NEGOCIO_FECHADO("Negócio fechado"),
	NEGOCIO_PERDIDO("Negócio perdido"),
	NEGOCIO_TRANSFERIDO("Negócio transferido");
	private final String descricao;
	FaseOportunidadeEnum(String descricao)
	{
		this.descricao = descricao;
	}
	@Override
	public String toString()
	{
		return this.descricao;
	}
}
