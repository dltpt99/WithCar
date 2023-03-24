package anu.ice.WithCar.repository;

import anu.ice.WithCar.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
}
