package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegister {

    private String email;
    private String password;


    public UserRegister(String email) {
        this.email=email;
    }
}
