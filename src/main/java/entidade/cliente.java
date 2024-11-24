package entidade;

public class cliente {
	private int idade;
	private String name;
	private int cpf;

	
	
	// get and setters
	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		if (idade <= 0) {
			System.out.println("Ta de sacanagem 0 anos?");
		}
		this.idade = idade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCpf() {
		return cpf;
	}

	public void setCpf(int cpf) {
		this.cpf = cpf;
	}

	// construtor

	public cliente(int idade, String name, int cpf) {
		super();
		this.idade = idade;
		this.name = name;
		this.cpf = cpf;
	}

}
