package kodlamaio.hrms.business.abstracts;

import kodlamaio.hrms.core.utilities.results.DataResult;
import kodlamaio.hrms.entities.concretes.User;

public interface UserService {

	DataResult<User> getUserByEmail(String email);
}
