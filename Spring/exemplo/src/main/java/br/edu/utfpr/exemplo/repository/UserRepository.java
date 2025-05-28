package br.edu.utfpr.exemplo.repository;

import br.edu.utfpr.exemplo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
