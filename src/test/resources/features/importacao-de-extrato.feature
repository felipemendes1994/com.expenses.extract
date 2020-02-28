#language: pt
Funcionalidade: Importação de extrato bancário

	Cenario: Importacao de extrato para conta de bancaria que nao existe
		Dado que o account esteja respondendo na API de GET "/accounts/19977530/agencies/0001-9" o status 404
		E que o account esteja respondendo na API de POST "/accounts" o status 201 e request payload
		"""
		{
			"agencyNumber":"0001-9",
			"accountNumber":"19977530",
			"bankNumber":"077",
			"bankName":"Banco Intermedium S/A"
		}
		"""
		Quando a API de POST para importacao de extrato "/extract/ofx" for chamada com o body "/home/mce_fmendes/projects/expenses/extratos/Extrato.ofx"
