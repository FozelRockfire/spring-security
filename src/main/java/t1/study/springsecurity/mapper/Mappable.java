package t1.study.springsecurity.mapper;

public interface Mappable <E,D> {

    D toDTO(E dto);

    E toEntity(D entity);
}
