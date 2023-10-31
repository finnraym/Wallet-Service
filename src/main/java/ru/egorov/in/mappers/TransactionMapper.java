package ru.egorov.in.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.egorov.in.dto.TransactionResponse;
import ru.egorov.model.Transaction;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    List<TransactionResponse> toDTOList(List<Transaction> entities);

}