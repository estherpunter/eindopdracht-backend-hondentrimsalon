package nl.novi.eindopdrachtbackendhondentrimsalon.mappers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "dogIds", source = "customer.dogs")
    CustomerDto customerToCustomerDto(Customer customer);

    default List<Long> mapDogsToDogIds(Set<Dog> dogs) {
        return dogs.stream()
                .map(Dog::getId)
                .collect(Collectors.toList());
    }

    List<CustomerDto> customersToCustomerDtos(List<Customer> customers);
}
