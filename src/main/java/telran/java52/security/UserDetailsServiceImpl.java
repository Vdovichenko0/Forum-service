package telran.java52.security;

import java.util.Collection;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java52.accounting.dao.UserAccountRepository;
import telran.java52.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	final UserAccountRepository userAccountRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = userAccountRepository.findById(username).orElseThrow(
					()-> new UsernameNotFoundException(username));
		Collection<String> authorities = userAccount.getRoles().stream()
									.map(r -> "ROLE_" + r.name())
									.toList();
		return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(authorities));
	}

}
