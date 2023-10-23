package ru.egorov.in.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.egorov.in.dto.TransactionDTO;
import ru.egorov.model.Transaction;

import java.util.List;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction toEntity(TransactionDTO dto);

    TransactionDTO toDTO(Transaction entity);

    List<TransactionDTO> toDTOList(List<Transaction> entities);

    List<Transaction> toEntityList(List<TransactionDTO> dtos);
}