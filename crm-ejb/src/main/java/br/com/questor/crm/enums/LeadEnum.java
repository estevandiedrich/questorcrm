package br.com.questor.crm.enums;

public enum LeadEnum {
	EM_AVALIACAO("Em avaliação"),
	QUALIFICADA("Qualificada"),
	DESQUALIFICADA("Desqualificada"),
	CONTA("Conta"),
	DISTRIBUIDOR("Distribuidor");
	private final String descricao;
	LeadEnum(String descricao)
	{
		this.descricao = descricao;
	}
	@Override
	public String toString()
	{
		return this.descricao;
	}
}
