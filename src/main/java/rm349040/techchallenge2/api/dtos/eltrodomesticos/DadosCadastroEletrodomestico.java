package rm349040.techchallenge2.api.dtos.eltrodomesticos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import rm349040.techchallenge2.api.dtos.enderecos.output.EnderecoAPI;

public record DadosCadastroEletrodomestico(


        @NotBlank(message = "O nome NÃO pode ser nulo ou em BRANCO")
        @Size(max = 60, message = "O nome do eletrodoméstico NÃO pode conter mais do que 60 chars")
        String nome,

        @NotBlank(message = "O modelo NÃO pode ser nulo ou em BRANCO")
        @Size(max = 60, message = "O modelo NÃO pode conter mais do que 60 chars")
        String modelo,

        @NotNull(message = "A potência NÃO pode ser nula")
        @PositiveOrZero(message = "A potência deve ser maior ou igual do que zero")
        Double potencia,
        EnderecoAPI endereco


) {}
