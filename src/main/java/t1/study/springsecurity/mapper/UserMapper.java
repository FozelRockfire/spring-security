package t1.study.springsecurity.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import t1.study.springsecurity.dto.UserDTO;
import t1.study.springsecurity.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDTO> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
