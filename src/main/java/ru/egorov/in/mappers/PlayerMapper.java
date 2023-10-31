package ru.egorov.in.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.model.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerDTO toDto(Player entity);

}
