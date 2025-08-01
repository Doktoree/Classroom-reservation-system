package doktoree.backend.services;

import java.util.List;

import doktoree.backend.domain.User;
import doktoree.backend.dtos.UserDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;

public interface UserService {

	public Response<UserDto> findUserById(Long id)
			throws EntityNotExistingException;

	public Response<UserDto> saveUser(UserDto dto)
			throws EntityNotSavedException;

	public Response<UserDto> deleteUser(Long id)
			throws EntityNotExistingException, EntityNotDeletedException;

	public Response<List<UserDto>> getAllUsers(int pageNumber)
			throws EmptyEntityListException;

	public Response<UserDto> updateUser(UserDto dto)
			throws EntityNotExistingException, EntityNotSavedException;
			
	public User validateUser(User user)
			throws EntityNotSavedException;
}
