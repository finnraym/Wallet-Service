package ru.egorov.in.mappers;

import org.mapstruct.Mapper;
import ru.egorov.in.dto.TransactionResponse;
import ru.egorov.model.Transaction;

import java.util.List;

/**
 * Mapper for transaction entity
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Mapping transactions list entity to dto list
     *
     * @param entities the transactions entities
     * @return mapped transaction dto list
     */
    List<TransactionResponse> toDTOList(List<Transaction> entities);

}