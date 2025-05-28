package br.edu.utfpr.exemplo.service;

import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user){
        return userRepository.save(user);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Transactional
    public void delete(Long id){
        userRepository.deleteById(id);
    }

}
