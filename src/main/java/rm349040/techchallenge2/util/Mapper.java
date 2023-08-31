package rm349040.techchallenge2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import rm349040.techchallenge2.domain.service.CadastroService;

/**
 * Generally, this class converts an object of type T into another S type object.<br>
 * Sometimes, it maps properties from a T type object to another T type object.<br>
 * Its mapping power goes beyond 1:1 mappings. It can map T entities collection into S entities collection.<br>
 * 
 * 
 *     
 */
@Component
@Scope("prototype")
public class Mapper {

    @Autowired
    private ModelMapper modelMapper;

    public <T, S> S toDomain(T source, Class<S> destination) {
    	if(source == null || destination == null) return null;
        return modelMapper.map(source, destination);
    }

    public <T, S> S toDto(T source, Class<S> destination) {
    	if(source == null || destination == null) return null;
        return modelMapper.map(source,destination);
    }

    /**
     * Maps a T type collection to a S type DTO collection.
     * @param <T> the T type
     * @param <S> the S type DTO
     * @param sourceCollection the source collection of elements of T type
     * @param DTO the target type this algorithm will build a collection of
     * @return a collection of elements of S type DTO, 
     * each of them identified/mapped by its correspondent at source collection of T type
     * (sounds monumental, but it is very simple and useful)
     */
    public <T, S> Collection<S> toDtoCollection(Collection<T> sourceCollection, Class<S> DTO) {

    	if(sourceCollection == null || DTO == null) return new HashSet<S>(); 
    	
    	return sourceCollection
    	.stream()
		.map(typeT -> {
			try {
				S dto = DTO.newInstance();
				identify(typeT, dto);
				return dto;
			} catch (Exception e) {
			}
			return null;
		}).collect(Collectors.toList());

    }
    
    /**
     * Paging and sorting converter facility method. Invoked usefully when we want to turn an entity collection into a DTO instances spring page.   
     * @param <T> the type T DTO
     * @param Page<T> A Spring page.
     * @param cadastroService the service we get entities from.
     * @param pageable a spring generated pageable instance (or any other customized pageable instance)  
     * @param DTO the dto
     * @return a T type DTO instances Spring Page.
     */
	public <T> Page<T> toPage(CadastroService cadastroService, Pageable pageable, Class<T> DTO) {
		if(cadastroService == null || pageable == null || DTO == null)	return Page.empty();
		Page<?> page = cadastroService.listar(pageable);
		Collection<?> sourceCollection = page.getContent();
		ArrayList<T> dtoCollecion = new ArrayList<T>(toDtoCollection(sourceCollection, DTO));
		return new PageImpl<T>(dtoCollecion, pageable, page.getTotalElements());
	}

    /**
     * Makes destination's attributes values equal to source's attributes values.
     * @param source the instance we read settings from 
     * @param destination the instance we write settings to
     * @param <T> a type
     */
    public <T> void identify(T source, T destination){
    	if(source == null) {
    		destination = null;
    		return;
    	};
    	if(destination == null) return;
    	try {
    		modelMapper.map(source,destination);
		} catch (Exception e) {
			System.err.println(
							 String.format("MAPPER : mapping failed identifying source %s to destination %s"
							,source.getClass().getSimpleName()
							,destination.getClass().getSimpleName()));
		}
        
    }

}
