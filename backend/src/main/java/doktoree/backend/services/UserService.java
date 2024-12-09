package doktoree.backend.services;

import java.util.List;

import doktoree.backend.dtos.UserDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;

public interface UserService {

	public Response<UserDto> findUserById(Long id) throws EntityNotExistingException;

	public Response<UserDto> saveUser(UserDto dto) throws EntityNotSavedException;

	public Response<UserDto> deleteUser(Long id) throws EntityNotExistingException, EntityNotDeletedException;

	public Response<List<UserDto>> getAllUsers();

	public Response<UserDto> updateUser(UserDto dto) throws EntityNotExistingException, EntityNotSavedException;
			

}
