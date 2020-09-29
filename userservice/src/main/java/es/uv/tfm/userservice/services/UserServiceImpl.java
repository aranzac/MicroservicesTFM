package es.uv.tfm.userservice.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uv.tfm.userservice.entities.Role;
import es.uv.tfm.userservice.entities.User;
import es.uv.tfm.userservice.exceptions.ResourceNotFoundException;
import es.uv.tfm.userservice.exceptions.UserExistsException;
import es.uv.tfm.userservice.repository.RoleRepository;
import es.uv.tfm.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public User createUser(User user) {
		
		if(userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent() )
			throw new UserExistsException("User already exists");
			
		Optional<Role> role = roleRepository.findByName("USER");
		
		if(!role.isPresent())
			throw new ResourceNotFoundException("Role not found");
		
		user.addRole(role.get());
		
		return userRepository.save(user);
	}

	@Override
	public User updateUser(int id, User user) {
		
		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));
		
		user.setId(id);

		return userRepository.save(user);
	}

	@Override
	public Boolean deleteUser(int id) {
		try {
			userRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public User findById(int id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("No user found with Username " + username));
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No user found with Email " + email));
	}
}
