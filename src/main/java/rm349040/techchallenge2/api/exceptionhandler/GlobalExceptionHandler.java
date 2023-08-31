package rm349040.techchallenge2.api.exceptionhandler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import rm349040.techchallenge2.domain.exception.DomainException;
import rm349040.techchallenge2.domain.exception.EnderecoNaoAssociadoAoMesmoUsuarioException;
import rm349040.techchallenge2.domain.exception.EntityNotFoundException;
import rm349040.techchallenge2.domain.exception.NullException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorType errorType = ErrorType.INVALID_DATA;

        String detail = String.format("Um ou mais campos estão inválidos. Corrija e tente novamente.");

        BindingResult bindingResult = ex.getBindingResult();

        List<ApiError.Field> fields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {

                    String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());

                    return ApiError.Field.builder()
                        .name(fieldError.getField())
                        .userMessage(message)
                        .build();})

                .collect(Collectors.toList());

        ApiError error = newApiErrorBuilder(HttpStatus.valueOf(status.value()), errorType, detail)
                .userMessage("Um ou mais campos estão inválidos. Corrija e tente novamente.")
                .fields(fields)
                .build();

        return handleExceptionInternal(ex,error,headers,status,request);

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorType errorType = ErrorType.RESOURCE_NOT_FOUND;

        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",ex.getRequestURL() );

        ApiError error = newApiErrorBuilder(HttpStatus.valueOf(status.value()), errorType, detail).build();

        return handleExceptionInternal(ex,error,headers,status,request);

    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorType errorType = ErrorType.INVALID_PARAMETER;

        String detail = String.format("O token '%s' da URL recebeu o valor '%s' que é um tipo inválido." +
                " Corrija e informe um valor compatível com o tipo %s",ex.getPropertyName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ApiError error = newApiErrorBuilder(HttpStatus.valueOf(status.value()), errorType, detail)
                .build();

        return handleExceptionInternal(ex,error,headers,status,request);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof JsonParseException) {
            return handleJsonParseException((JsonParseException) rootCause, headers, status, request);
        }

        if(rootCause instanceof InvalidFormatException){
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }

        if(rootCause instanceof PropertyBindingException){
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        String detail = "O body da request está inválido. Corrija erro de sintaxe";


        ApiError apiError = newApiErrorBuilder(HttpStatus.valueOf(status.value()), ErrorType.MESSAGE_NOT_READABLE, detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorType errorType = ErrorType.MESSAGE_NOT_READABLE;

        String detail = String.format("A propriedade '%s' não é permitida." +
                " Corrija excluindo-a.",ex.getPropertyName());

        ApiError error = newApiErrorBuilder(HttpStatus.valueOf(status.value()), errorType, detail)
                .build();

        return handleExceptionInternal(ex,error,headers,status,request);

    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {


        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        ErrorType errorType = ErrorType.MESSAGE_NOT_READABLE;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s' que é um tipo inválido." +
                " Corrija e informe um valor compatível com o tipo %s",path, ex.getValue(), ex.getTargetType().getSimpleName());

        ApiError error = newApiErrorBuilder(HttpStatus.valueOf(status.value()), errorType, detail)
                .build();

        return handleExceptionInternal(ex,error,headers,status,request);

    }


    private ResponseEntity handleJsonParseException(JsonParseException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {


        if(ex instanceof JsonEOFException){

            ErrorType errorType = ErrorType.MESSAGE_NOT_READABLE;
            String detail = "O corpo da solicitação está mal escrito. Corrija adicionando '}' ao fim da mensagem.";

            ApiError error = newApiErrorBuilder(HttpStatus.valueOf(status.value()), errorType, detail)
                    .build();

            return handleExceptionInternal(ex,error,headers,status,request);

        }


        ErrorType errorType = ErrorType.MESSAGE_NOT_READABLE;
        String detail = "Corrija o corpo da solicitação. Ele está mal escrito" ;

        ApiError error = newApiErrorBuilder(HttpStatus.valueOf(status.value()), errorType, detail)
                .build();


        return handleExceptionInternal(ex,error,headers,status,request);

        //cant get unexpected token even expected values directly from JsonParseException...
        //so i wrote this workaround... ;)
//        String unexpectedToken = ex.getLocalizedMessage().split(" ")[2];
//        String values = ex.getLocalizedMessage().split("\\)")[0];
//        values = values.split("\\(")[1];
//
//        String expectedValue="";
//        for(String s : values.split(" ")){
//            expectedValue+=s;
//        }
//
//        expectedValue = expectedValue.replace("JSON","");
//
//        ErrorType errorType = ErrorType.MESSAGE_NOT_READABLE;
//        String detail = String.format("A propriedade '%s' recebeu o valor '%s' que é um tipo inválido." +
//                " Corrija e informe um valor compatível com o tipo %s",ex.getProcessor().getParsingContext().getCurrentName(), unexpectedToken, expectedValue);
//
//        ApiError error = newApiBuilder(HttpStatus.valueOf(status.value()), errorType, detail)
//                .timeStamp(Instant.now())
//                .build();
//
//
//        return handleExceptionInternal(ex,error,headers,status,request);

    }



    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {


        HttpStatus status = HttpStatus.NOT_FOUND;
        String detail = ex.getMessage() + ". Tentando te ajudar ... passe um id que exista na base de dados que daí você poderá receber o que solicita.";

        ApiError apiError = newApiErrorBuilder(status, ErrorType.RESOURCE_NOT_FOUND, detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }


    @ExceptionHandler(NullException.class)
    public ResponseEntity<Object> handleNullException(NullException ex, WebRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String detail = ex.getMessage();

        ApiError apiError = newApiErrorBuilder(status, ErrorType.INVALID_DATA, detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);

    }

    @ExceptionHandler(EnderecoNaoAssociadoAoMesmoUsuarioException.class)
    public ResponseEntity<Object> handleEnderecoNaoAssociadoAoMesmoUsuarioException(EnderecoNaoAssociadoAoMesmoUsuarioException ex, WebRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String detail = ex.getMessage();

        ApiError apiError = newApiErrorBuilder(status, ErrorType.INVALID_DATA, detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);

    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> handleDomainException(DomainException ex, WebRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String detail = ex.getMessage();

        ApiError apiError = newApiErrorBuilder(status, ErrorType.INVALID_DATA, detail)
                .build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);

    }
   
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException ex, WebRequest request){

        DomainException domainException = new DomainException(
        										String.format("ERRO ORDENAÇÂO : não existe essa propriedade %s em %s"
        												,ex.getPropertyName()
        												,ex.getType().toString().split("\\.")[ ex.getType().toString().split("\\.").length-1]
        												));
        
        return handleDomainException(domainException, request);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptionGlobal(Exception ex, WebRequest request) {

        ErrorType errorType = ErrorType.INTERNAL_ERROR;

        String detail = String.format("Ocorreu um erro interno inesperado no sistema. " +
                "Tente novamente e se o problema persistir, " +
                "entre em contato com o administrador do sistema. ");

        ApiError error = newApiErrorBuilder(HttpStatus.INTERNAL_SERVER_ERROR, errorType, detail)
                .build();

        return handleExceptionInternal(ex,error,null,HttpStatus.INTERNAL_SERVER_ERROR,request);

    }
    
 
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        if (body == null) {

            body = ApiError.builder().
                    title(HttpStatus.valueOf(statusCode.value()).getReasonPhrase()).
                    status(statusCode.value()).
                    timeStamp(Instant.now()).
                    build();

        } else if (body instanceof String) {

            body = ApiError.builder().
                    title((String) body).
                    timeStamp(Instant.now()).
                    status(statusCode.value()).
                    build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private ApiError.ApiErrorBuilder newApiErrorBuilder(HttpStatus status, ErrorType errorType, String detail) {
        return ApiError.builder()
                .status(status.value())
                .type(errorType.getUri())
                .title(errorType.getTitle())
                .timeStamp(Instant.now())
                .detail(detail);
    }

}
