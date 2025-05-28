package br.edu.utfpr.exemplo.controller;

import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.model.vo.UserVO;
import br.edu.utfpr.exemplo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private UserService userService;

    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UserVO> save(@RequestBody UserVO userVO) {
        User user = modelMapper.map(userVO, User.class);
        userService.save(user);
        userVO.setId(user.getId());
        return new ResponseEntity<>(userVO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserVO> update(@PathVariable("id") Long id, @RequestBody UserVO userVO) {
        userVO.setId(id);
        User user = modelMapper.map(userVO, User.class);
        userService.update(user);
        return new ResponseEntity<>(userVO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserVO> findById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        UserVO userVO = modelMapper.map(user, UserVO.class);
        return new ResponseEntity<>(userVO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserVO>> list() {
        List<User> users = userService.findAll();
        List<UserVO> userVOs = users.stream().map(user -> modelMapper.map(user, UserVO.class)).toList();
        return new ResponseEntity<>(userVOs, HttpStatus.CREATED);
    }

    /*
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}
