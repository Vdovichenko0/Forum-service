package telran.java52.accounting.service;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java52.accounting.dao.UserAccountRepository;
import telran.java52.accounting.dto.RolesDto;
import telran.java52.accounting.dto.UserDto;
import telran.java52.accounting.dto.UserEditDto;
import telran.java52.accounting.dto.UserRegisterDto;
import telran.java52.accounting.dto.exceptions.UserAccountNotFoundException;
import telran.java52.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

	final ModelMapper modelMapper;
	final UserAccountRepository userAccountRepository;

	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override ///////////////////////////////
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserAccountNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserAccountNotFoundException::new);
		userAccountRepository.delete(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserAccountNotFoundException::new);
		String firstName = userEditDto.getFirstName();
		if (firstName != null) {
			userAccount.setFirstName(firstName);
		}
		String lastName = userEditDto.getLastName();
		if (lastName != null) {
			userAccount.setLastName(lastName);
		}
		userAccount = userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserAccountNotFoundException::new);

		if (isAddRole) {
			userAccount.addRole(role);
		} else {
			userAccount.removeRole(role);
		}

		userAccountRepository.save(userAccount);

		return RolesDto.builder().login(userAccount.getLogin())
				.roles(userAccount.getRoles().stream().map(Enum::name).collect(Collectors.toSet())).build();
	}

	@Override ////////////////////////////
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserAccountNotFoundException::new);
		userAccount.setPassword(newPassword);
		userAccountRepository.save(userAccount);

	}

}
