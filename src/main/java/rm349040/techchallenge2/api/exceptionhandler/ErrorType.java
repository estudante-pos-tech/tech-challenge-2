package rm349040.techchallenge2.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ErrorType {

    RESOURCE_NOT_FOUND("/recurso-nao-encontrado", "Recurso não encontrado"),
    MESSAGE_NOT_READABLE("/mensagem-nao-legivel", "Mensagem não legível"),
    INTERNAL_ERROR("/erro-interno", "Erro interno do sistema"),
    INVALID_DATA("/dado-invalido", "Dado inválido"),
    INVALID_PARAMETER("/parametro-invalido", "Parâmetro da url inválido");

    private String uri = "https://github.com/estudante-pos-tech/tech-challenge-1/blob/master/src/main/java/rm349040/techchallenge1/documentation";
    private String title;

    ErrorType(String path, String title) {
        uri += path;
        this.title = title;
    }
}
