package telran.java52.accounting.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.java52.accounting.model.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String>{

}
