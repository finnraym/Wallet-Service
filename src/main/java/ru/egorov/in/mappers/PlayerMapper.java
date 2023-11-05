package ru.egorov.in.mappers;

import org.mapstruct.Mapper;
import ru.egorov.in.dto.PlayerDTO;
import ru.egorov.model.Player;

/**
 * Mapper for player entity
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper {

    /**
     * Mapping player entity to dto
     *
     * @param entity the player entity
     * @return mapped player dto
     */
    PlayerDTO toDto(Player entity);

}
