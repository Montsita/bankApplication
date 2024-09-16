package bankapp.bankApplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationUpdateAllDTO {
    @NotBlank
    private String userName;
}
