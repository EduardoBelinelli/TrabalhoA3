package entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class cliente {
	@Id
	private Long id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "CPF")
	private int cpf;

}
