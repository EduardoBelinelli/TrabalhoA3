package entidade;

public class produto {
	private int id;
	private String tipo;
	private double price;

	
	
		//get and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTipo() {
		return tipo;
	}
	
	//construtores
	
	public produto(int id, String tipo, double price) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.price = price;
	}
	
	
}
