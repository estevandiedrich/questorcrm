package br.com.questor.crm.enums;

public enum TipoProdutoEnum {
	MANUTENCAO("Manutenção"),
	SERVICO("Serviço");
	private final String descricao;
	TipoProdutoEnum(String descricao)
	{
		this.descricao = descricao;
	}
	@Override
	public String toString()
	{
		return this.descricao;
	}
}
